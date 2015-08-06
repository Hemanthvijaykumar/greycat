package org.kevoree.modeling.memory.space.impl;

import org.junit.Assert;
import org.junit.Test;
import org.kevoree.modeling.KCallback;
import org.kevoree.modeling.KModel;
import org.kevoree.modeling.KObject;
import org.kevoree.modeling.memory.manager.DataManagerBuilder;
import org.kevoree.modeling.memory.manager.KDataManager;
import org.kevoree.modeling.memory.manager.internal.KInternalDataManager;
import org.kevoree.modeling.meta.KMetaClass;
import org.kevoree.modeling.meta.KMetaModel;
import org.kevoree.modeling.meta.impl.MetaModel;

public class KChunkSpaceCleanerTest {

    /**
     * @native ts
     */
    @Test
    public void test() {
        KMetaModel dynamicMetaModel = new MetaModel("MyMetaModel");
        final KMetaClass sensorMetaClass = dynamicMetaModel.addMetaClass("Sensor");
        KDataManager manager = DataManagerBuilder.create().build();
        final KModel universe = dynamicMetaModel.createModel((KInternalDataManager) manager);
        universe.connect(new KCallback<Throwable>() {
            @Override
            public void on(Throwable throwable) {
                KObject sensor = universe.universe(0).time(0).create(sensorMetaClass);

                Assert.assertEquals(4, ((KInternalDataManager) manager).spaceSize());

                long sensorID = sensor.uuid();
                sensor = null;
                universe.save(null);
                System.gc();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Assert.assertEquals(1, ((KInternalDataManager) manager).spaceSize());

                /*
                //Assert.assertEquals(1, universe.manager().cache().size());
                universe.universe(0).time(0).lookup(sensorID, new KCallback<KObject>() {
                    @Override
                    public void on(KObject kObject) {
                        Assert.assertNotNull(kObject);
                        //Assert.assertEquals(4, universe.manager().cache().size());

                        kObject.jump(10, new KCallback<KObject>() {
                            @Override
                            public void on(KObject kObject2) {
                                Assert.assertNotNull(kObject2);
                                //Assert.assertEquals(4, universe.manager().cache().size());
                            }
                        });
                    }
                });
                */

            }
        });


    }

}
