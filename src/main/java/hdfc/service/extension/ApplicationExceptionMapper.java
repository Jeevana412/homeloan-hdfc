/**
 * Copyright 2016 foreveross inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hdfc.service.extension;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.foreveross.springboot.dubbo.utils.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author VectorHo
 *
 * 捕获程序抛出ApplicationException异常, 根据用户自定义的响应码响应给前端:
 *
 * e.g. throw new ApplicationException(Response.Status.UNAUTHORIZED, "Unauthorized");
 *
 */
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public Response toResponse(ApplicationException e) {
        logger.info("Client IP is " + RpcContext.getContext().getRemoteAddressString());
        logger.error("Api message: ", e);

        String msg = e.getMessage();
        String[] msg_arr = msg.replaceAll("^@|@^", "").trim().split("@");
        // 返回Restful响应码
        return Response.status(Integer.valueOf(msg_arr[0].trim())).entity(new Payload(msg_arr[1])).type(ContentType.APPLICATION_JSON_UTF_8).build();
    }
}
