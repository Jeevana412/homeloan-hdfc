package hdfc.service.rpc.api;

import hdfc.domain.MethodDescription;
import org.jboss.resteasy.core.ResourceInvoker;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by chenshaowen on 2017/6/1.
 */
public interface OverviewResourceService {

    /**
     * 获取api接口列表
     * @param bound
     * @return
     */
    List<MethodDescription> fromBoundResourceInvokers(Set<Map.Entry<String, List<ResourceInvoker>>> bound);
}
