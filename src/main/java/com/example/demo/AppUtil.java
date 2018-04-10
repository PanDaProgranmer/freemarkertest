package com.example.demo;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * App Utils
 *
 * @author wangqh
 */
@Slf4j
@Component
public class AppUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static <T> T getBean(String name) {
        try {
            return (T) applicationContext.getBean(name);
        } catch (BeansException e) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        Map<String, T> beans = applicationContext.getBeansOfType(clazz);
        if (beans.size() <= 0) {
            return null;
        }

        return Lists.newArrayList(beans.values()).get(0);
    }

    public static <T> T getRpcBean(Class<T> clazz) {
        Map<String, T> beans = applicationContext.getBeansOfType(clazz);
        if (beans.size() <= 0) {
            return null;
        }

        for (Map.Entry<String, T> entry : beans.entrySet()) {
            if (entry.getKey().toLowerCase().endsWith("impl")) {
                return entry.getValue();
            }
        }

        return Lists.newArrayList(beans.values()).get(0);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        log.info("App Utils init");
        AppUtil.applicationContext = applicationContext;
    }
}
