package com.example.demo.freemarker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.AppUtil;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

/**
 * Created by panda on 2018/3/13.
 */
@Component
public class PortalFnDirective implements TemplateDirectiveModel {
    private static String paramPrefix = "param_";
    private static String service = "service";
    private static String method = "method";
    private static String name = "name";
    private static String cacheMin = "cacheMin";
    private static String result = "result";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        String beanName = map.get(service).toString();
        String methodName = map.get(method).toString();
        Map<String, String> params = getParams(map);
        String resultKey = map.get("result") == null ? result : map.get("result").toString();
        Class returnType = getReturnType(beanName, methodName);
        if (map.get(cacheMin) != null) {
            //有设置缓存
            String key = getCacheKey(beanName, methodName, params);
            if (stringRedisTemplate.hasKey(key)) {
                printOutFromCache(environment, templateDirectiveBody, resultKey, returnType, key);
                return;
            } else {
                Interner<String> pool = Interners.newWeakInterner();
                synchronized (pool.intern(key)) {
                    if (!stringRedisTemplate.hasKey(key)) {
                        Object result = getResult(beanName, methodName, params);
                        //cache
                        stringRedisTemplate.opsForValue().set(key,
                                JSON.toJSONString(result, SerializerFeature.WriteClassName),
                                Long.valueOf(map.get(cacheMin).toString()),
                                TimeUnit.MINUTES);
                        printOut(environment, templateDirectiveBody, resultKey, result);
                    } else {
                        printOutFromCache(environment, templateDirectiveBody, resultKey, returnType, key);
                    }
                }
            }

        }
    }

    private void printOut(Environment environment, TemplateDirectiveBody templateDirectiveBody, String resultKey, Object result) throws IOException, TemplateException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
        environment.setVariable(resultKey,
                builder.build().wrap(result));
        templateDirectiveBody.render(environment.getOut());
    }

    private void printOutFromCache(Environment environment, TemplateDirectiveBody templateDirectiveBody, String resultKey, Class returnType, String key) throws IOException, TemplateException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
        environment.setVariable(resultKey,
                builder.build().wrap(JSONObject.parseObject(stringRedisTemplate.opsForValue().get(key), returnType)));
        templateDirectiveBody.render(environment.getOut());
    }


    /**
     * 通过反射获取方法
     *
     * @param beanName
     * @param methodName
     * @return
     */
    private Method getMethod(String beanName, String methodName) {
        Object bean = AppUtil.getBean(beanName);
        return ReflectionUtils.findMethod(AppUtil.getBean(beanName).getClass(), methodName, new Class[]{Map.class});
    }

    /**
     * 获取返回类型
     *
     * @param beanName
     * @param methodName
     * @return
     */
    private Class getReturnType(String beanName, String methodName) {
        Method mh = getMethod(beanName, methodName);
        return mh.getReturnType();
    }

    /**
     * 解析入参
     *
     * @param map
     * @return
     */
    private Map<String, String> getParams(Map map) {
        Map<String, String> params = new HashMap<>();
        int paramPrefixLenth = paramPrefix.length();
        map.forEach((key, value) -> {
            if (key.toString().indexOf(paramPrefix) != -1) {
                params.put(key.toString().substring(paramPrefixLenth, key.toString().length()), value.toString());
            }
        });
        return params;
    }

    /**
     * 通过反射调用接口
     *
     * @param beanName
     * @param methodName
     * @return
     */
    private Object getResult(String beanName, String methodName, Map<String, String> params) {
        return ReflectionUtils.invokeMethod(getMethod(beanName, methodName), AppUtil.getBean(beanName), params);
    }

    /**
     * 缓存Key
     *
     * @param beanName
     * @param methodName
     * @param params
     * @return
     */
    private String getCacheKey(String beanName, String methodName, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(name);
        builder.append(":").append(beanName).append(":").append(methodName);
        params.forEach((key, value) -> {
            builder.append(":").append(key).append(":").append(value);
        });
        return builder.toString();
    }
}
