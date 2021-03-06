package hdfc.service.extension;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * This logging filter is not highly optimized for now
 * <p/>
 * Created by VectorHo on 2016/10/26.
 */
@Priority(Integer.MIN_VALUE)
public class LoggingFilter implements ContainerRequestFilter, ClientRequestFilter, ContainerResponseFilter, ClientResponseFilter, WriterInterceptor, ReaderInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);


    @Override
    public void filter(ClientRequestContext context) throws IOException {
        logger.info("↓↓↓↓↓↓ HTTP请求部分 ↓↓↓↓↓↓");
        logger.info(context.getMethod() + " " + context.getUri().getRawPath()
                + ( StringUtils.isBlank(context.getUri().getRawQuery()) ? "" : "?" + context.getUri().getRawQuery()) );
        logHttpHeaders(context.getStringHeaders());
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        logger.info("↓↓↓↓↓↓ HTTP响应部分 ↓↓↓↓↓↓");
        logHttpHeaders(responseContext.getHeaders());
    }

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        logger.info("↓↓↓↓↓↓ HTTP请求部分 ↓↓↓↓↓↓");
        logger.info(context.getMethod() + " " + context.getUriInfo().getAbsolutePath().getRawPath()
                + ( StringUtils.isBlank(context.getUriInfo().getAbsolutePath().getRawQuery()) ? "" : "?" + context.getUriInfo().getAbsolutePath().getRawQuery()) );
        logHttpHeaders(context.getHeaders());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        logger.info("↓↓↓↓↓↓ HTTP响应部分 ↓↓↓↓↓↓");
        logHttpHeaders(responseContext.getStringHeaders());
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        byte[] buffer = IOUtils.toByteArray(context.getInputStream());
        logger.info("The contents of request body is: \n" + new String(buffer, "UTF-8") + "\n");
        context.setInputStream(new ByteArrayInputStream(buffer));
        return context.proceed();
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        OutputStreamWrapper wrapper = new OutputStreamWrapper(context.getOutputStream());
        context.setOutputStream(wrapper);
        context.proceed();
        logger.info("The contents of response body is: \n" + new String(wrapper.getBytes(), "UTF-8"));
        logger.info("\n\n\n\n\n");
    }


    protected void logHttpHeaders(MultivaluedMap<String, String> headers) {
        StringBuilder msg = new StringBuilder("The HTTP headers are: \n");
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            msg.append(entry.getKey()).append(": ");
            for (int i = 0; i < entry.getValue().size(); i++) {
                msg.append(entry.getValue().get(i));
                if (i < entry.getValue().size() - 1) {
                    msg.append(", ");
                }
            }
            msg.append("\n");
        }
        logger.info(msg.toString());
    }

    protected static class OutputStreamWrapper extends OutputStream {

        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        private final OutputStream output;

        private OutputStreamWrapper(OutputStream output) {
            this.output = output;
        }

        @Override
        public void write(int i) throws IOException {
            buffer.write(i);
            output.write(i);
        }

        @Override
        public void write(byte[] b) throws IOException {
            buffer.write(b);
            output.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            buffer.write(b, off, len);
            output.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            output.flush();
        }

        @Override
        public void close() throws IOException {
            output.close();
        }

        public byte[] getBytes() {
            return buffer.toByteArray();
        }
    }


}