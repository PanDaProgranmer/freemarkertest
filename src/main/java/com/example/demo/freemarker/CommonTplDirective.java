package com.example.demo.freemarker;

import com.example.demo.enums.IEnum;
import com.example.demo.enums.TplType;
import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by panda on 2018/3/10.
 */
@Component
public class CommonTplDirective implements TemplateDirectiveModel {
    /**
     * todo 做配置
     */
    private static String defaultHead = "defaultHead.ftl";
    private static String head = "head.ftl";
    private static String defaultFooter = "defaultFooter.ftl";
    private static String footer = "footer.ftl";

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        String path = map.get("tpl").toString();
        String defaultPath = map.get("defaultTpl").toString();
        setTplInclude(path, defaultPath, environment);
    }

    private void setTplInclude(String path, String defaultPath, Environment environment) throws IOException, TemplateException {
        if (Strings.isNotBlank(path)) {
            TemplateLoader templateLoader = environment.getConfiguration().getTemplateLoader();
            if (templateLoader.findTemplateSource(path) == null) {
                throw new RuntimeException("模板不存在");
            }
            environment.include(environment.getTemplateForInclusion(path, null, true));
        } else {
            environment.include(environment.getTemplateForInclusion(defaultPath, null, true));
        }
    }
}
