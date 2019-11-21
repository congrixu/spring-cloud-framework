package com.rxv5.redis.sn.generator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rxv5.redis.sn.SerialNumberGenerator;
import com.rxv5.redis.util.JedisUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RedisOnlySerialNumberGenerator implements SerialNumberGenerator {

    private final Logger logger = LoggerFactory.getLogger(RedisOnlySerialNumberGenerator.class);
    private static final String REDIS_KEY_PREFIX = "serial:";
    private String keyName;
    private long maxValue;
    private String prefix = StringUtils.EMPTY;
    private int length;
    private long minValue = 0l;

    @Override
    public void init(String name, long maxValue, String prefix) {
        this.keyName = REDIS_KEY_PREFIX + name;
        this.maxValue = maxValue;
        length = String.valueOf(maxValue).length();
        if (null != prefix)
            this.prefix = prefix;
        logger.info("Initialized RedisOnlySerialNumberGenerator with name {} and redis key name is {}", name, keyName);
    }

    public RedisOnlySerialNumberGenerator() {
    }

    @Override
    public long nextLong() {
        long ret = -1;
        Jedis jedis = JedisUtil.getJedis();
        try {
            ret = jedis.incr(keyName);
            if (ret > maxValue) {
                jedis.watch(keyName);
                Transaction transaction = jedis.multi();
                transaction.del(keyName);
                transaction.exec();
            }
        } catch (Exception e) {
            logger.info("somthing wrong at deleting serial number key:" + keyName, e);
            jedis = null;
        } finally {
            JedisUtil.returnResource(jedis);
        }
        // 如果序列号超了等会儿再取（递归）
        if (ret > maxValue)
            return nextLong();
        return ret;
    }

    @Override
    public String nextString(String padding) {
        long lRet = nextLong();
        return prefix + StringUtils.leftPad(String.valueOf(lRet), length, padding);
    }

    @Override
    public String nextString() {
        return nextString(PADDING_STR);
    }

    @Override
    public void init(String name, long maxValue, long minValue, String prefix) {
        this.keyName = REDIS_KEY_PREFIX + name;
        this.maxValue = maxValue;
        this.minValue = minValue;
        length = String.valueOf(maxValue).length();
        if (null != prefix)
            this.prefix = prefix;
        logger.info("Initialized RedisOnlySerialNumberGenerator with name {} and redis key name is {}", name, keyName);
    }
}
