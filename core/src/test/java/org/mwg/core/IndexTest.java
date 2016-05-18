package org.mwg.core;

import org.junit.Assert;
import org.junit.Test;
import org.mwg.*;
import org.mwg.Graph;
import org.mwg.Node;
import org.mwg.core.chunk.offheap.*;
import org.mwg.core.utility.PrimitiveHelper;
import org.mwg.core.utility.Unsafe;

public class IndexTest {

    @Test
    public void heapTest() {
        test(GraphBuilder.builder().withScheduler(new NoopScheduler()).build());
    }

    /**
     * @ignore ts
     */
    @Test
    public void offHeapTest() {
        OffHeapByteArray.alloc_counter = 0;
        OffHeapDoubleArray.alloc_counter = 0;
        OffHeapLongArray.alloc_counter = 0;
        OffHeapStringArray.alloc_counter = 0;

        Unsafe.DEBUG_MODE = true;

        test(GraphBuilder.builder().withScheduler(new NoopScheduler()).withOffHeapMemory().withMemorySize(10000).withAutoSave(100).build());

        Assert.assertTrue(OffHeapByteArray.alloc_counter == 0);
        Assert.assertTrue(OffHeapDoubleArray.alloc_counter == 0);
        Assert.assertTrue(OffHeapLongArray.alloc_counter == 0);
        Assert.assertTrue(OffHeapStringArray.alloc_counter == 0);
    }

    private void test(Graph graph) {
        final int[] counter = {0};
        graph.connect(new Callback<Boolean>() {
            @Override
            public void on(Boolean o) {
                org.mwg.Node node_t0 = graph.newNode(0, 0);
                node_t0.setProperty("name", Type.STRING, "MyName");

                graph.all(0, 0, "nodes", new Callback<org.mwg.Node[]>() {
                    @Override
                    public void on(org.mwg.Node[] allNodes) {
                        counter[0]++;
                        Assert.assertTrue(allNodes.length == 0);
                    }
                });

                graph.index("nodes", node_t0, "name", new Callback<Boolean>() {
                    @Override
                    public void on(Boolean o) {
                        counter[0]++;
                    }
                });

                graph.all(0, 0, "nodes", new Callback<org.mwg.Node[]>() {
                    @Override
                    public void on(org.mwg.Node[] allNodes) {
                        counter[0]++;
                        Assert.assertTrue(allNodes.length == 1);
                        Assert.assertTrue(PrimitiveHelper.equals("{\"world\":0,\"time\":0,\"id\":1,\"data\": {\"name\": \"MyName\"}}", allNodes[0].toString()));
                    }
                });

                graph.find(0, 0, "nodes", "name=MyName", new Callback<org.mwg.Node[]>() {
                    @Override
                    public void on(org.mwg.Node[] kNode) {
                        counter[0]++;
                        Assert.assertTrue(kNode != null);
                        Assert.assertEquals(1, kNode.length);
                        Assert.assertTrue(PrimitiveHelper.equals("{\"world\":0,\"time\":0,\"id\":1,\"data\": {\"name\": \"MyName\"}}", kNode[0].toString()));
                    }
                });

                //test a null index
                graph.all(0, 0, "unknownIndex", new Callback<org.mwg.Node[]>() {
                    @Override
                    public void on(org.mwg.Node[] allNodes) {
                        counter[0]++;
                        Assert.assertTrue(allNodes.length == 0);
                    }
                });


                org.mwg.Node node_t1 = graph.newNode(0, 0);
                node_t1.setProperty("name", Type.STRING, "MyName");
                node_t1.setProperty("version", Type.STRING, "1.0");

                graph.index("nodes", node_t1, "name,version", new Callback<Boolean>() {
                    @Override
                    public void on(Boolean o) {
                        counter[0]++;
                    }
                });


                //test the old indexed node
                graph.find(0, 0, "nodes", "name=MyName", new Callback<org.mwg.Node[]>() {
                    @Override
                    public void on(org.mwg.Node[] kNode) {
                        counter[0]++;
                        Assert.assertTrue(kNode != null);
                        Assert.assertTrue(kNode.length == 1);
                        Assert.assertTrue(PrimitiveHelper.equals("{\"world\":0,\"time\":0,\"id\":1,\"data\": {\"name\": \"MyName\"}}", kNode[0].toString()));
                    }
                });

                //test the new indexed node
                graph.find(0, 0, "nodes", "name=MyName,version=1.0", new Callback<org.mwg.Node[]>() {
                    @Override
                    public void on(org.mwg.Node[] kNode) {
                        counter[0]++;
                        Assert.assertTrue(kNode != null);
                        Assert.assertTrue(kNode.length == 1);
                        Assert.assertTrue(PrimitiveHelper.equals("{\"world\":0,\"time\":0,\"id\":3,\"data\": {\"name\": \"MyName\",\"version\": \"1.0\"}}", kNode[0].toString()));
                    }
                });


                //test potential inversion
                graph.find(0, 0, "nodes", "version=1.0,name=MyName", new Callback<org.mwg.Node[]>() {
                    @Override
                    public void on(org.mwg.Node[] kNode) {
                        counter[0]++;
                        Assert.assertTrue(kNode != null);
                        Assert.assertTrue(kNode.length == 1);
                        Assert.assertTrue(PrimitiveHelper.equals("{\"world\":0,\"time\":0,\"id\":3,\"data\": {\"name\": \"MyName\",\"version\": \"1.0\"}}", kNode[0].toString()));
                    }
                });


                //unIndex the node @t1
                graph.unindex("nodes", node_t1, new String[]{"name", "version"}, new Callback<Boolean>() {
                    @Override
                    public void on(Boolean o) {
                        counter[0]++;
                    }
                });


                graph.find(0, 0, "nodes", "version=1.0,name=MyName", new Callback<org.mwg.Node[]>() {
                    @Override
                    public void on(org.mwg.Node[] kNode) {
                        counter[0]++;
                        Assert.assertTrue(kNode != null);
                        Assert.assertTrue(kNode.length == 0);
                    }
                });


                //reIndex
                graph.index("nodes", node_t1, "name,version", new Callback<Boolean>() {
                    @Override
                    public void on(Boolean o) {
                        counter[0]++;
                    }
                });


                //should work again
                graph.find(0, 0, "nodes", "version=1.0,name=MyName", new Callback<org.mwg.Node[]>() {
                    @Override
                    public void on(org.mwg.Node[] kNode) {
                        counter[0]++;
                        Assert.assertTrue(kNode != null);
                        Assert.assertTrue(kNode.length == 1);
                        Assert.assertTrue(PrimitiveHelper.equals("{\"world\":0,\"time\":0,\"id\":3,\"data\": {\"name\": \"MyName\",\"version\": \"1.0\"}}", kNode[0].toString()));
                    }
                });


                //local index usage
                org.mwg.Node node_index = graph.newNode(0, 0);
                node_index.index("children", node_t1, "name,version", new Callback<Boolean>() {
                    @Override
                    public void on(Boolean result) {
                        counter[0]++;
                    }
                });

                node_index.find("children", "name=MyName,version=1.0", new Callback<org.mwg.Node[]>() {
                    @Override
                    public void on(Node[] kNode) {
                        counter[0]++;
                        Assert.assertTrue(kNode != null);
                        Assert.assertTrue(kNode.length == 1);
                        Assert.assertTrue(PrimitiveHelper.equals("{\"world\":0,\"time\":0,\"id\":3,\"data\": {\"name\": \"MyName\",\"version\": \"1.0\"}}", kNode[0].toString()));
                    }
                });

                graph.disconnect(new Callback<Boolean>() {
                    @Override
                    public void on(Boolean result) {
                        //end of the test
                    }
                });

            }
        });
        Assert.assertTrue(counter[0] == 15);
    }

}
