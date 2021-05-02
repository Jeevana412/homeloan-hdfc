/**
 * Copyright 2016 foreveross inc.
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
package hdfc.service.optimizers;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.foreveross.springboot.dubbo.utils.Payload;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


/**
 * This class must be accessible from both the provider and consumer Adapter for kryo
 *
 * @author VectorHo
 */
public class SerializationOptimizerImpl implements SerializationOptimizer {

    public Collection<Class> getSerializableClasses() {
        List<Class> classes = new LinkedList<Class>();
        // ... 优化rpc 序列化
        classes.add(Payload.class);
        return classes;
    }
}
