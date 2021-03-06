/**
 * Copyright 2017-2018 The GreyCat Authors.  All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ml.classifier;

import org.junit.Test;
import org.mwg.Callback;
import org.mwg.Graph;
import org.mwg.GraphBuilder;
import org.mwg.internal.scheduler.NoopScheduler;
import org.mwg.mlx.MLXPlugin;
import org.mwg.mlx.algorithm.classifier.GaussianNaiveBayesianNode;

import static org.junit.Assert.assertTrue;

public class GaussianNaiveBayesianTest extends AbstractClassifierTest {

    @Test
    public void test() {
        //This test fails if there are too many errors

        final Graph graph = new GraphBuilder()
                .withPlugin(new MLXPlugin())
                .withScheduler(new NoopScheduler())
                .build();
        graph.connect(new Callback<Boolean>() {
            @Override
            public void on(Boolean result) {
                GaussianNaiveBayesianNode gaussianNBNode = (GaussianNaiveBayesianNode) graph.newTypedNode(0, 0, GaussianNaiveBayesianNode.NAME);
                standardSettings(gaussianNBNode);
                ClassificationJumpCallback cjc = runThroughDummyDataset(gaussianNBNode);
                gaussianNBNode.free();
                graph.disconnect(null);
                assertTrue(cjc.errors <= 3);
            }
        });
    }

}

