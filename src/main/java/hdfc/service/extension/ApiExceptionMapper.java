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
package hdfc.service.extension;

import com.alibaba.dubbo.rpc.RpcContext;
import com.foreveross.springboot.dubbo.utils.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author VectorHo
 *
 * dubbo会利用Filter机制对Provider中所有异常进行捕获并且抛出, 所以这里就是对ExceptionFilter抛出的异常再次捕获再次包装返回给客户端.
 *
 */
public class ApiExceptionMapper implements ExceptionMapper<Exception> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public Response toResponse(Exception e) {
        logger.debug("Api exception mapper successfully got an exception: " + e + ": " + e.getMessage());
        logger.debug("Client IP is " + RpcContext.getContext().getRemoteAddressString());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new Payload("Api exception: " + e.getStackTrace()[0].getClassName() + ": " + e.getMessage()))
                .type("application/json;charset=utf-8").build();
    }
}
