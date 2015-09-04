package org.kevoree.modeling.memory.space.impl;

import org.kevoree.modeling.KObject;
import org.kevoree.modeling.abs.AbstractKObject;
import org.kevoree.modeling.memory.resolver.KResolver;
import org.kevoree.modeling.memory.space.KChunkSpace;
import org.kevoree.modeling.scheduler.KScheduler;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ignore ts
 */
public class PhantomQueueChunkSpaceManager extends AbstractCountingChunkSpaceManager implements Runnable {

    /* This is the very first GC collector for KMF, thanks Floreal :-) */

    private final ReferenceQueue<KObject> referenceQueue;
    private final AtomicReference<KObjectPhantomReference> headPhantom;
    private final KScheduler _scheduler;
    private KResolver _resolver;

    public PhantomQueueChunkSpaceManager(KChunkSpace p_storage, KScheduler p_scheduler) {
        super(p_storage);
        this._scheduler = p_scheduler;
        headPhantom = new AtomicReference<KObjectPhantomReference>();
        referenceQueue = new ReferenceQueue<KObject>();
        Thread cleanupThread = new Thread(this);
        cleanupThread.setDaemon(true);
        cleanupThread.start();
    }

    @Override
    public void register(KObject kobj) {
        if (_metaModel == null) {
            _metaModel = kobj.manager().model().metaModel();
        }
        KObjectPhantomReference newRef = new KObjectPhantomReference(kobj);
        do {
            newRef.next = headPhantom.get();
        } while (!headPhantom.compareAndSet(newRef.next, newRef));
        if (newRef.next != null) {
            newRef.next.previous = newRef;
        }
    }

    @Override
    public void registerAll(KObject[] kobjects) {
        for (int i = 0; i < kobjects.length; i++) {
            if (kobjects[i] != null) {
                register(kobjects[i]);
            }
        }
    }

    @Override
    public void setResolver(KResolver p_resolver) {
        this._resolver = p_resolver;
    }

    //unProtected and unVolatile because of the mono-thread access
    private int capacity = 1;
    private long[] collected_dereference = new long[3 * capacity];
    private int counter = 0;

    @Override
    public void run() {
        while (true) {
            KObjectPhantomReference kobj;
            try {
                kobj = (KObjectPhantomReference) referenceQueue.remove();
                if (kobj.previous == null) {
                    if (!headPhantom.compareAndSet(kobj, kobj.next)) {
                        //ouch should try to remove from the previous
                        KObjectPhantomReference nextRef = kobj.next;
                        KObjectPhantomReference previousRef = kobj.previous;
                        if (nextRef != null) {
                            nextRef.previous = previousRef;
                        }
                        if (previousRef != null) {
                            previousRef.next = nextRef;
                        }
                    }
                } else {
                    KObjectPhantomReference nextRef = kobj.next;
                    KObjectPhantomReference previousRef = kobj.previous;
                    if (nextRef != null) {
                        nextRef.previous = previousRef;
                    }
                    previousRef.next = nextRef;
                }
                if (_resolver != null) {

                    long[] previousResolved = kobj.previousResolved.get();
                    long previousUuid = kobj.obj;

                    collected_dereference[counter * 3] = previousResolved[AbstractKObject.UNIVERSE_PREVIOUS_INDEX];
                    collected_dereference[counter * 3 + 1] = previousResolved[AbstractKObject.TIME_PREVIOUS_INDEX];
                    collected_dereference[counter * 3 + 2] = previousUuid;
                    counter++;

                    if (counter == capacity) {
                        final long[] previousCollected = collected_dereference;
                        collected_dereference = new long[3 * capacity];
                        counter = 0;
                        _scheduler.dispatch(new SpaceUnmarkTask(this, previousCollected, _resolver));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class KObjectPhantomReference extends PhantomReference<KObject> {

        public long obj;
        public AtomicReference<long[]> previousResolved;
        private KObjectPhantomReference next;
        private KObjectPhantomReference previous;

        public KObjectPhantomReference(KObject referent) {
            super(referent, referenceQueue);
            this.obj = referent.uuid();
            previousResolved = ((AbstractKObject) referent).previousResolved();
        }
    }

}
