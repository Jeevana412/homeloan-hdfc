package hdfc.service.rest.api;

import java.util.List;

import hdfc.domain.MethodDescription;
import org.jboss.resteasy.core.Dispatcher;
import javax.ws.rs.core.Response;

/**
 * Created by shao_win on 2017/5/26.
 */
public interface OverviewResourceRestService{

    List<MethodDescription> getAvailableEndpoints(Dispatcher dispatcher);

    Response getAvailableEndpointsHtml(Dispatcher dispatcher);
}
