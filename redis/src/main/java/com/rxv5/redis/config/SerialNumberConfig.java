package com.rxv5.redis.config;

import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;
import com.rxv5.redis.sn.SerialNumber;
import com.rxv5.redis.sn.SerialNumberGenerator;
import com.rxv5.redis.util.SerialNumberUtil;

@Configuration
public class SerialNumberConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private List<Class<?>> customClasses = Lists.newArrayList();

    public void addClass(Class<?> clazz) {
        customClasses.add(clazz);
    }

    @Bean
    public boolean start() {
        logger.info("scan package init SerialNumber..................");
        Reflections reflections = new Reflections("com.rxv5");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(SerialNumber.class);
        classes.addAll(customClasses);
        for (Class<?> clazz : classes) {
            try {
                SerialNumber serialNumber = clazz.getAnnotation(SerialNumber.class);
                SerialNumberGenerator generator = serialNumber.generator().newInstance();
                generator.init(serialNumber.name(), serialNumber.maxValue(), serialNumber.prefix());
                SerialNumberUtil.add(clazz, generator);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("SerialNumberConfig error:", e);
            }
        }
        return true;
    }

}
