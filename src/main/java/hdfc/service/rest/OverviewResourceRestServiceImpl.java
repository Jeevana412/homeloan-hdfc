package hdfc.service.rest;

import java.util.List;


import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import hdfc.domain.MethodDescription;
import hdfc.service.rest.api.OverviewResourceRestService;
import hdfc.service.rpc.api.OverviewResourceService;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.core.ResourceMethodRegistry;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author shao_win 2017/5/26.
 */
@Service("overviewResourceRestService")
@Path("/api/v1/resources")
@Consumes({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class OverviewResourceRestServiceImpl implements OverviewResourceRestService {

    @Autowired
    private OverviewResourceService overviewResourceService;


    @Override
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MethodDescription> getAvailableEndpoints(@Context Dispatcher dispatcher) {
        ResourceMethodRegistry registry = (ResourceMethodRegistry) dispatcher.getRegistry();
        return overviewResourceService.fromBoundResourceInvokers(registry.getBounded().entrySet());
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public Response getAvailableEndpointsHtml(@Context Dispatcher dispatcher) {
        // Yeah, yeah, HTML per StringBuilder. I can't be bovvered to make a JSP :D
        StringBuilder sb = new StringBuilder();
        ResourceMethodRegistry registry = (ResourceMethodRegistry) dispatcher.getRegistry();
        List<MethodDescription> descriptions = overviewResourceService.fromBoundResourceInvokers(registry.getBounded().entrySet());

        sb.append("<h1>").append("REST interface overview").append("</h1>");

        for (MethodDescription method : descriptions) {
            sb.append("<ul>");

            sb.append("<li> ").append(method.getMethod()).append(" ");
            sb.append("<strong>").append(method.getFullPath()).append("</strong>");
            sb.append("</li>");
            if (method.getConsumes() != null) {
                sb.append("<li>").append("Consumes: ").append(method.getConsumes()).append("</li>");
            }
            if (method.getProduces() != null) {
                sb.append("<li>").append("Produces: ").append(method.getProduces()).append("</li>");
            }
            if (method.getPathParams() != null &&method.getPathParams().size()>0) {
                sb.append("<li>").append("PathParams: ").append(method.getPathParams()).append("</li>");
            }
            if (method.getQueryParams() != null &&method.getQueryParams().size()>0) {
                sb.append("<li>").append("QueryParams: ").append(method.getQueryParams()).append("</li>");
            }
            if (method.getRequestBody() != null &&method.getRequestBody().size()>0) {
                sb.append("<li>").append("RequestBody: ").append(method.getRequestBody()).append("</li>");
            }
            sb.append("</ul>");
        }

        return Response.ok(sb.toString()).build();

    }
}
