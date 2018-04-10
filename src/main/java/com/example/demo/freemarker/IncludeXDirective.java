package com.example.demo.freemarker;

import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by panda on 2018/3/10.
 */
@Component
public class IncludeXDirective implements TemplateDirectiveModel {
    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        TemplateLoader templateLoader = environment.getConfiguration().getTemplateLoader();
        String path = map.get("path").toString();
        if (templateLoader.findTemplateSource(path) != null) {
            environment.include(environment.getTemplateForInclusion(path, null, true));
        }else{
            throw new RuntimeException("不存在的模板路径");
        }

    }
}
