package org.kevoree.modeling.traversal.impl.actions;

import org.kevoree.modeling.KObject;
import org.kevoree.modeling.KCallback;
import org.kevoree.modeling.abs.AbstractKObject;
import org.kevoree.modeling.memory.chunk.KObjectChunk;
import org.kevoree.modeling.memory.chunk.KLongLongMap;
import org.kevoree.modeling.memory.chunk.impl.ArrayLongLongMap;
import org.kevoree.modeling.memory.chunk.KLongLongMapCallBack;
import org.kevoree.modeling.meta.KMeta;
import org.kevoree.modeling.meta.KMetaRelation;
import org.kevoree.modeling.meta.MetaType;
import org.kevoree.modeling.traversal.KTraversalAction;
import org.kevoree.modeling.traversal.KTraversalActionContext;

public class TraverseAction implements KTraversalAction {

    private KTraversalAction _next;

    private KMetaRelation _reference;

    public TraverseAction(KMetaRelation p_reference) {
        this._reference = p_reference;
    }

    @Override
    public void chain(KTraversalAction p_next) {
        _next = p_next;
    }

    @Override
    public void execute(KTraversalActionContext context) {
        if (context.inputObjects() == null || context.inputObjects().length == 0) {
            if(_next != null){
                _next.execute(context);
            } else {
                context.finalCallback().on(context.inputObjects());
            }
        } else {
            final AbstractKObject currentObject = (AbstractKObject) context.inputObjects()[0];
            KLongLongMap nextIds = new ArrayLongLongMap(-1,-1,-1,null);
            for (int i = 0; i < context.inputObjects().length; i++) {
                try {
                    AbstractKObject loopObj = (AbstractKObject) context.inputObjects()[i];
                    KObjectChunk raw = currentObject._manager.closestChunk(loopObj.universe(), loopObj.now(), loopObj.uuid(), loopObj.metaClass(), loopObj.previousResolved());
                    if (raw != null) {
                        if (_reference == null) {
                            KMeta[] metaElements = loopObj.metaClass().metaElements();
                            for (int j = 0; j < metaElements.length; j++) {
                                if (metaElements[j] != null && metaElements[j].metaType() == MetaType.REFERENCE) {
                                    KMetaRelation ref = (KMetaRelation) metaElements[j];
                                    long[] resolved = raw.getLongArray(ref.index(), currentObject.metaClass());
                                    if (resolved != null) {
                                        for (int k = 0; k < resolved.length; k++) {
                                            nextIds.put(resolved[k], resolved[k]);
                                        }
                                    }
                                }
                            }
                        } else {
                            KMetaRelation translatedRef = loopObj.internal_transpose_ref(_reference);
                            if (translatedRef != null) {
                                long[] resolved = raw.getLongArray(translatedRef.index(), currentObject.metaClass());
                                if (resolved != null) {
                                    for (int j = 0; j < resolved.length; j++) {
                                        nextIds.put(resolved[j], resolved[j]);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            final long[] trimmed = new long[nextIds.size()];
            final int[] inserted = {0};
            nextIds.each(new KLongLongMapCallBack() {
                @Override
                public void on(long key, long value) {
                    trimmed[inserted[0]] = key;
                    inserted[0]++;
                }
            });
            //call
            currentObject._manager.lookupAllObjects(currentObject.universe(), currentObject.now(), trimmed, new KCallback<KObject[]>() {
                @Override
                public void on(KObject[] kObjects) {
                    if (_next == null) {
                        context.finalCallback().on(kObjects);
                    } else {
                        context.setInputObjects(kObjects);
                        _next.execute(context);
                    }
                }
            });
        }
    }

}
