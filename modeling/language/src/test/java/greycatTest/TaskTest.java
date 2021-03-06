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
package greycatTest;

import greycat.language.Checker;
import greycat.language.Model;
import org.junit.Test;

import java.io.IOException;

public class TaskTest {

    @Test
    public void testFull() throws IOException {
        Model model = new Model();
        model.parseResource("task.gcm", this.getClass().getClassLoader());
        model.consolidate();
        Checker.check(model);
    }

}
