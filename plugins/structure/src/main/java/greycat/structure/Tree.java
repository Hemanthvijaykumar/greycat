/**
 * Copyright 2017 The GreyCat Authors.  All rights reserved.
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
package greycat.structure;

public interface Tree {

    //Settings param
    void setDistance(int distanceType);

    void setResolution(double[] resolution);

    void setMinBound(double[] min);

    void setMaxBound(double[] max);

    //Insert functions

    /**
     * Insert a value in the tree referenced by keys composed by the vector
     *
     * @param keys  vector of double composing the key
     * @param value value to be inserted (can be a node id to create a reference)
     */
    void insert(final double[] keys, final long value);

    void profile(final double[] keys, final long occurrence);

    //Retrieve functions
    TreeResult nearestN(final double[] keys, final int nbElem);

    TreeResult nearestWithinRadius(final double[] keys, final double radius);

    TreeResult nearestNWithinRadius(final double[] keys, final int nbElem, final double radius);

    TreeResult query(final double[] min, final double[] max);

    //Tree properties
    long size();

    long numberOfNodes();


}
