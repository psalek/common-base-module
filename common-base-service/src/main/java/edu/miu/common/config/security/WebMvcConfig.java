package edu.miu.common.config.security;

import static org.springframework.web.method.HandlerTypePredicate.forAnnotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.miu.common.config.properties.CommonProperties;

@Configuration
@ConditionalOnProperty(value = "common.mvc.enabled", havingValue = "true", matchIfMissing = true)
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private CommonProperties properties;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
                .addPathPrefix(properties.getSecurity().getApi().getPathPrefix(), forAnnotation(RestController.class))
                .addPathPrefix(properties.getSecurity().getHmi().getPathPrefix(), forAnnotation(Controller.class));
    }

}