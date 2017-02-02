/**
 * Copyright 2017 The MWG Authors.  All rights reserved.
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
package org.mwg.struct;

import org.mwg.plugin.NodeStateCallback;

public interface ENode {

    ENode set(String name, byte type, Object value);

    ENode setAt(int key, byte type, Object value);

    Object get(String name);

    Object getAt(int key);

    Object getOrCreate(final String key, final byte type);

    Object getOrCreateAt(final int key, final byte type);

    void drop();

    EGraph graph();

    void each(final NodeStateCallback callBack);

    ENode clear();

}
