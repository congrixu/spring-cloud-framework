package com.rxv5.redis.sn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.rxv5.redis.sn.generator.RedisOnlySerialNumberGenerator;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SerialNumber {

    String prefix()

    default "";

    String name();

    long maxValue()

    default 9999;

    long minValue()

    default 0;

    Class<? extends SerialNumberGenerator> generator() default RedisOnlySerialNumberGenerator.class;
}
