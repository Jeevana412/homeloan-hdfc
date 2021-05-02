package hdfc.domain;

import java.util.List;
import java.util.Map;

/**
 * Created by shao_win on 2017/5/26.
 */
public final class MethodDescription {

    private String method;
    private String fullPath;
    private String produces;
    private String consumes;
    private List<Map<String,Object>> pathParams;
    private List<Map<String,Object>> queryParams;
    private List<Map<String,Object>> requestBody;
    private String returnType;

    public MethodDescription(String method, String fullPath, String produces, String consumes) {
        super();
        this.method = method;
        this.fullPath = fullPath;
        this.produces = produces;
        this.consumes = consumes;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getProduces() {
        return produces;
    }

    public void setProduces(String produces) {
        this.produces = produces;
    }

    public String getConsumes() {
        return consumes;
    }

    public void setConsumes(String consumes) {
        this.consumes = consumes;
    }

    public List<Map<String, Object>> getPathParams() {
        return pathParams;
    }

    public void setPathParams(List<Map<String, Object>> pathParams) {
        this.pathParams = pathParams;
    }

    public List<Map<String, Object>> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(List<Map<String, Object>> queryParams) {
        this.queryParams = queryParams;
    }


    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<Map<String, Object>> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(List<Map<String, Object>> requestBody) {
        this.requestBody = requestBody;
    }

    public MethodDescription(String method, String fullPath, String produces, String consumes, List<Map<String, Object>> pathParams, List<Map<String, Object>> queryParams, List<Map<String, Object>> requestBody, String returnType) {
        this.method = method;
        this.fullPath = fullPath;
        this.produces = produces;
        this.consumes = consumes;
        this.pathParams = pathParams;
        this.queryParams = queryParams;
        this.requestBody = requestBody;
        this.returnType = returnType;
    }
}
