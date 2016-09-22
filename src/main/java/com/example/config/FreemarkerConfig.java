package com.example.config;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleDate;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
public class FreemarkerConfig {
    @Autowired
    void configurefreeMarkerConfigurer(FreeMarkerConfig configurer) {
        configurer.getConfiguration().setObjectWrapper(new CustomObjectWrapper());
    }

    private static class CustomObjectWrapper extends DefaultObjectWrapper {
        public CustomObjectWrapper() {
            super(freemarker.template.Configuration.VERSION_2_3_25);
        }

        @Override
        public TemplateModel wrap(Object obj) throws TemplateModelException {
            if (obj instanceof LocalDateTime) {
                Timestamp timestamp = Timestamp.valueOf((LocalDateTime) obj);
                return new SimpleDate(timestamp);
            } else if (obj instanceof LocalDate) {
                java.sql.Date date = java.sql.Date.valueOf((LocalDate) obj);
                return new SimpleDate(date);
            } else if (obj instanceof LocalTime) {
                Time time = Time.valueOf((LocalTime) obj);
                return new SimpleDate(time);
            } else {
                return super.wrap(obj);
            }
        }
    }

}
