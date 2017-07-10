package greycat.websocket;

import greycat.*;
import greycat.scheduler.NoopScheduler;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.CountDownLatch;

import static greycat.Tasks.newTask;

public class DeleteTest {

    @Test
    public void test() {
        final CountDownLatch latch = new CountDownLatch(1);
        final Graph graph_back = new GraphBuilder().withScheduler(new NoopScheduler()).withMemorySize(10000).build();
        graph_back.connect(null);

        Node n = graph_back.newNode(0, 0);
        n.set("name", Type.STRING, "NamedNode");

        int port = findFreePort();
        WSServer graphServer = new WSServer(graph_back, port);
        graphServer.start();

        final Graph graph = new GraphBuilder().withStorage(new WSClient("ws://localhost:" + port + "/ws")).build();
        graph.connect(new Callback<Boolean>() {
            @Override
            public void on(Boolean result) {
                final Task task = newTask().travelInTime("0").travelInWorld("0").lookup(n.id() + "").delete();
                task.execute(graph, new Callback<TaskResult>() {
                    @Override
                    public void on(TaskResult result) {
                        Assert.assertEquals(0, result.size());
                        latch.countDown();
                    }
                });
            }
        });
        //wait the completion of async
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int findFreePort() {
        int port = 8050;
        try {
            ServerSocket servSock = new ServerSocket(0);
            port = servSock.getLocalPort();
            servSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return port;
    }


}