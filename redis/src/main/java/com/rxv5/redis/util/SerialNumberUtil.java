package com.rxv5.redis.util;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.rxv5.redis.sn.SerialNumber;
import com.rxv5.redis.sn.SerialNumberGenerator;
import com.rxv5.redis.sn.generator.RedisOnlySerialNumberGenerator;
import com.rxv5.util.DateUtil;

public class SerialNumberUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerialNumberUtil.class);
    private static final Map<String, SerialNumberGenerator> generators = Maps.newConcurrentMap();

    // cannot be initialized by new()
    private SerialNumberUtil() {

    }

    public static void add(Class<?> clazz, SerialNumberGenerator generator) {
        if (clazz.isAnnotationPresent(SerialNumber.class) && null != generator)
            generators.put(clazz.getAnnotation(SerialNumber.class).name(), generator);
    }

    public static void add(String name, SerialNumberGenerator generator) {
        if (StringUtils.isNotBlank(name) && null != generator)
            generators.put(name, generator);
    }

    public static SerialNumberGenerator getGenerator(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(SerialNumber.class)) {
            LOGGER.error("SerialNumberGenerator of class {} is not defined!", clazz);
            return null;
        }
        String name = clazz.getAnnotation(SerialNumber.class).name();
        if (!generators.containsKey(name)) {
            LOGGER.error("SerialNumberGenerator with name {} is not defined!", name);
            return null;
        }
        return generators.get(name);
    }

    public static SerialNumberGenerator getGenerator(String name) {
        SerialNumberGenerator result = null;
        if (StringUtils.isNotBlank(name)) {
            if (!generators.containsKey(name)) {
                LOGGER.error("SerialNumberGenerator with name {} is not defined!", name);
                return null;
            }
            result = generators.get(name);
        }
        return result;
    }

    public static void main(String[] args) {

    }

    /*
     * 不使用SerialNumber注解生成流水号 获取产品编码 如:800.Z0000001
     * 
     * @return
     */
    @SuppressWarnings("unused")
    private String getCode() {
        String name = "YY_PRODUCT_CODE";// 运营平台KEY
        String prefix = "800.Z";// 没有时为NULL
        SerialNumberGenerator generator = SerialNumberUtil.getGenerator(name);
        if (generator == null) {
            generator = new RedisOnlySerialNumberGenerator();
            generator.init(name, 9999999, prefix);
            SerialNumberUtil.add(name, generator);
        }
        return SerialNumberUtil.getGenerator(name).nextString();
    }

    /**
     * 使用SerialNumber注解生成流水号
     * 
     * @return
     */
    public String getOrderId() {
        return DateUtil.format(new Date(), "yyyyMMddHHmmssSSS")
                + SerialNumberUtil.getGenerator(SerialNumberTestVo.class).nextString();
    }

}
