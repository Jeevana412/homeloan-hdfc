package hdfc.service.rpc;

import java.util.List;
import java.util.Map;

import hdfc.domain.MethodDescription;
import hdfc.service.rpc.api.OverviewResourceService;
import org.jboss.resteasy.core.ResourceInvoker;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by chenshaowen on 2017/6/1.
 */
@Service("overviewResourceService")
public class OverviewResourceServiceImpl implements OverviewResourceService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<MethodDescription> calls = new ArrayList<MethodDescription>();

    private static final String PATH_PARAM = "pathParam";
    private static final String QUERY_PARAM = "queryParam";
    private static final String REQUEST_BODY = "requestBody";

    @Override
    public List<MethodDescription> fromBoundResourceInvokers(Set<Map.Entry<String, List<ResourceInvoker>>> bound) {
        if (this.calls == null || this.calls.size() == 0) {
            for (Map.Entry<String, List<ResourceInvoker>> entry : bound) {
                ResourceMethodInvoker aMethod = (ResourceMethodInvoker) entry.getValue().get(0);
                String basePath = aMethod.getMethod().getDeclaringClass().getAnnotation(Path.class).value();

                for (ResourceInvoker invoker : entry.getValue()) {
                    ResourceMethodInvoker method = (ResourceMethodInvoker) invoker;
                    addMethod(basePath, method);
                }
            }
        }
        return this.calls;
    }

    /**
     * 添加api接口
     *
     * @param path
     * @param method
     */
    private void addMethod(String path, ResourceMethodInvoker method) {
        try {
            String produces = mostPreferredOrNull(Arrays.asList(method.getProduces()));
            String consumes = mostPreferredOrNull(Arrays.asList(method.getConsumes()));
            String fullPath = path.replaceAll("/$", "") + "/" + method.getMethod().getAnnotation(Path.class).value().replaceAll("^/", "");
            String returnType = method.getGenericReturnType() != null ? method.getGenericReturnType().toString() : null;

            List<Map<String, Object>> pathParams = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> queryParams = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> requestBody = new ArrayList<Map<String, Object>>();

            Annotation[][] parameterAnnotationss = method.getMethod().getParameterAnnotations();
            Class[] parameterTypes = method.getMethod().getParameterTypes();
            if (parameterTypes != null && parameterTypes.length > 0) {
                for (int i = 0; i < parameterTypes.length; i++) {
                    Map<String, Object> param = new HashMap<String, Object>();
                    param.put("type", parameterTypes[i].getName());
                    Annotation[] annotations = parameterAnnotationss[i];
                    String flag = convert(annotations, param);
                    switch (flag) {
                        case PATH_PARAM:
                            pathParams.add(param);
                            break;
                        case QUERY_PARAM:
                            queryParams.add(param);
                            break;
                        case REQUEST_BODY:
                        default:
                            //扫描requestBody中的属性
                            List<Map<String, Object>> fields = getSetterFieldsOfJaveBean(parameterTypes[i]);
                            param.put("fields", fields);
                            requestBody.add(param);
                            break;
                    }
                }
            }
            for (String verb : method.getHttpMethods()) {
                this.calls.add(new MethodDescription(verb, fullPath, produces, consumes, pathParams, queryParams, requestBody, returnType));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("扫描api列表出错:", e);
        }
    }

    /**
     * 根据注解获取一个参数完整的信息
     *
     * @param annotations
     * @param param
     * @return
     */
    private String convert(Annotation[] annotations, Map<String, Object> param) {
        String paramFlag = REQUEST_BODY;//默认
        if (annotations != null && annotations.length != 0) {
            for (Annotation annotation : annotations) {
                Class c = annotation.annotationType();
                //整个注解数组中只要出现过一次下面的注解就可以标记
                if (c == PathParam.class) {
                    paramFlag = PATH_PARAM;
                }
                if (c == QueryParam.class) {
                    paramFlag = QUERY_PARAM;
                }
                Method[] methods = c.getDeclaredMethods();
                if (methods != null && methods.length != 0) {
                    for (Method method : methods) {
                        Class returnClass = method.getReturnType();
                        try {
                            Object value = method.invoke(annotation);
                            if ("value".equals(method.getName())) {//从value中取出需要的属性
                                if (c == PathParam.class || c == QueryParam.class) {//取出参数名
                                    param.put("name", convert(returnClass, value));
                                } else if (c == DefaultValue.class) {
                                    param.put("defaultValue", convert(returnClass, value));
                                } else if (c == Min.class) {
                                    param.put("minValue", convert(returnClass, value));
                                } else if (c == Max.class) {
                                    param.put("maxValue", convert(returnClass, value));
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return paramFlag;
    }

    private String mostPreferredOrNull(List<MediaType> preferred) {
        if (preferred.isEmpty()) {
            return null;
        } else {
            return preferred.get(0).toString();
        }
    }

    /**
     * 按类型转换成可见值
     *
     * @param clazz
     * @param value
     * @return
     */
    private String convert(Class clazz, Object value) {
        if (null == clazz || null == value) return "";
        if (String.class == clazz) {
            return "" + value.toString() + "";
        } else if (int.class == clazz) {
            return (int) value + "";
        } else if (byte.class == clazz) {
            return (byte) value + "";
        } else if (boolean.class == clazz) {
            return (boolean) value + "";
        } else if (char.class == clazz) {
            return (char) value + "";
        } else if (float.class == clazz) {
            return (float) value + "";
        } else if (double.class == clazz) {
            return (double) value + "";
        } else if (long.class == clazz) {
            return (long) value + "";
        } else if (double.class == clazz) {
            return (double) value + "";
        } else if (String[].class == clazz) {
            String[] values = (String[]) value;
            StringBuilder sb = new StringBuilder("");
            for (String str : values) {
                sb.append(str);
            }
            return sb.toString();
        } else {
            return clazz.getName();
        }
    }

    /**
     * 获取类的中含有setter方法所有属性（包括父类）
     *
     * @param c
     * @return
     */
    private List getSetterFieldsOfJaveBean(Class c) {
        List<Map<String, Object>> sfields = new ArrayList<Map<String, Object>>();

        if (!c.isPrimitive() && !isWrapClass(c) && !c.isArray() && !Map.class.isAssignableFrom(c)
                && !Collection.class.isAssignableFrom(c)) {//不是基本数据类型,基本数据类型封装类,数组，map以及其子类,collection以及其子类

            while (c != null && c != Object.class) {//当父类为null的时候说明到达了最上层的父类(Object类).

                Method[] allMethods = c.getDeclaredMethods();//getDeclaredFields可能存在一些没有set方法的字段,这些字段
                for (Method method : allMethods) {
                    //TODO 有可能子类，父类含有相同名称不同类型的字段，这种情况还没做处理
                    if (method.getName().matches("^set.*")) {//可以接收参数的field
                        Map<String, Object> map = new HashMap<>();
                        String tempName = method.getName().substring(3);//setName-->name
                        String name = tempName.substring(0, 1).toLowerCase() + tempName.substring(1);
                        try {
                            map.put("type", c.getDeclaredField(name).getType());
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                        map.put("name", name);
                        sfields.add(map);
                    }
                }
                c = c.getSuperclass(); //得到父类,然后赋给自己
            }
        }
        return sfields;
    }

    /**
     * 判断类型是否为基本数据类型的包装类
     */
    private boolean isWrapClass(Class clzz) {
        try {
            return (((clzz == String.class)) || ((clzz == Long.class)) || ((clzz == Integer.class)) || ((clzz == Float.class))
                    || ((clzz == Double.class)) || ((clzz == Boolean.class)) || ((clzz == Byte.class)) ||
                    ((clzz == Character.class)) || ((clzz == Short.class)) || (clzz == Date.class));
        } catch (Exception e) {
            return false;
        }
    }

    public List<MethodDescription> getCalls() {
        return calls;
    }

    public void setCalls(List<MethodDescription> calls) {
        this.calls = calls;
    }
}
