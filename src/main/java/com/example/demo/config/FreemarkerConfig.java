package com.example.demo.config;

import com.example.demo.freemarker.CommonTplDirective;
import com.example.demo.freemarker.IncludeXDirective;
import com.example.demo.freemarker.PortalFnDirective;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by panda on 2018/3/10.
 */
@Component
public class FreemarkerConfig {

    @Autowired
    private Configuration configuration;
    @Autowired
    private IncludeXDirective includeXDirective;
    @Autowired
    private CommonTplDirective commonTplDirective;
    @Autowired
    private PortalFnDirective portalFnDirective;

    @PostConstruct
    public void setSharedVariable() throws TemplateModelException {
        configuration.setSharedVariable("includeX", includeXDirective);
        configuration.setSharedVariable("common", commonTplDirective);
        configuration.setSharedVariable("portal_fn", portalFnDirective);
    }

}