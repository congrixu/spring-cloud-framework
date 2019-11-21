package com.rxv5.redis.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.rxv5.util.FastjsonUtil;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Tuple;
import redis.clients.util.SafeEncoder;

@SuppressWarnings("unchecked")
@Slf4j
public class JedisUtil {

    private static JedisPool jedisPool;

    public static void init(JedisPool pool) {
        JedisUtil.jedisPool = pool;
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    public static <T extends Serializable> T get(final String key) {
        Jedis jedis = null;
        Object retObj = null;
        try {
            jedis = jedisPool.getResource();
            // jedis.select(indexdb);
            byte[] value = jedis.get(SafeEncoder.encode(key));
            if (value != null) {
                try {
                    retObj = SerializableUtil.toObject(value);
                } catch (Exception e) {
                    retObj = SafeEncoder.encode(value);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return (T) retObj;
    }

    public static <T extends Serializable> T getObj(final String key, final Class<T> clazz) {
        T t = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // jedis.select(indexdb);
            byte[] value = jedis.get(SafeEncoder.encode(key));

            Object retObj = null;
            if (value != null) {
                try {
                    retObj = SerializableUtil.toObject(value);
                } catch (Exception e) {
                    retObj = SafeEncoder.encode(value);
                }
            }
            if (retObj != null) {
                t = FastjsonUtil.json2Obj(retObj.toString(), clazz);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return t;
    }

    public static <T extends Serializable> List<T> getListObj(final String key, final Class<T> clazz) {
        List<T> t = null;
        Jedis jedis = null;
        try {
            Object result = null;
            jedis = jedisPool.getResource();
            byte[] retVal = jedis.get(SafeEncoder.encode(key));
            if (null != retVal) {
                try {
                    result = SerializableUtil.toObject(retVal);
                } catch (Exception e) {
                    result = SafeEncoder.encode(retVal);
                }
            }
            if (result != null) {
                t = FastjsonUtil.json2ObjList(result.toString(), clazz);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return t;
    }

    public static boolean set(String key, String value) {
        boolean bool = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // jedis.select(indexdb);
            String retVal = null;
            if (value instanceof String) {
                retVal = jedis.set(key, (String) value);
            } else {
                retVal = jedis.set(SafeEncoder.encode(key), SerializableUtil.toByteArray(value));
            }
            bool = "OK".equalsIgnoreCase(retVal);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return bool;
    }

    public static boolean setObj(String key, Serializable value) {
        boolean bool = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String retVal = null;
            if (value instanceof String) {
                retVal = jedis.set(key, (String) value);
            } else {
                retVal = jedis.set(key, FastjsonUtil.obj2Json(value));
            }
            bool = "OK".equalsIgnoreCase(retVal);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            returnResource(jedisPool, jedis);
        }
        return bool;
    }

    public static boolean set(final String key, final Serializable value, final int seconds) {
        boolean bool = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] bytes;
            if (value instanceof String) {
                bytes = SafeEncoder.encode((String) value);
            } else {
                bytes = SerializableUtil.toByteArray(value);
            }
            String retVal = jedis.setex(SafeEncoder.encode(key), seconds, bytes);
            bool = "OK".equalsIgnoreCase(retVal);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return bool;
    }

    public static List<Serializable> mquery(final String... keys) {
        Jedis jedis = null;
        List<Serializable> result = new ArrayList<Serializable>(keys.length);
        try {
            jedis = jedisPool.getResource();
            for (int index = 0; index < keys.length; index++)
                result.add(null);
            byte[][] encodeKeys = new byte[keys.length][];
            for (int i = 0; i < keys.length; i++)
                encodeKeys[i] = SafeEncoder.encode(keys[i]);
            List<byte[]> retVals = jedis.mget(encodeKeys);
            if (null != retVals) {
                int index = 0;
                for (byte[] val : retVals) {
                    if (null != val)
                        result.set(index, SerializableUtil.toObject(val));
                    index++;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static List<String> mqueryStr(final String... keys) {
        List<String> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.mget(keys);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static boolean msaveOrUpdate(final Map<String, Serializable> values) {
        boolean bool = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[][] encodeValues = new byte[values.size() * 2][];
            int index = 0;
            Iterator<Entry<String, Serializable>> iter = values.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, Serializable> entry = iter.next();
                encodeValues[index++] = entry.getKey().getBytes();
                encodeValues[index++] = SerializableUtil.toByteArray(entry.getValue());
            }
            String retVal = jedis.mset(encodeValues);
            bool = "OK".equalsIgnoreCase(retVal);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return bool;
    }

    public static boolean msaveOrUpdateStr(final Map<String, String> values) {
        boolean bool = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Iterator<Entry<String, String>> iter = values.entrySet().iterator();
            int index = 0;
            String[] encodeValues = new String[values.size() * 2];
            while (iter.hasNext()) {
                Entry<String, String> entry = iter.next();
                encodeValues[index++] = entry.getKey();
                encodeValues[index++] = entry.getValue();
            }
            bool = "OK".equalsIgnoreCase(jedis.mset(encodeValues));
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return bool;
    }

    public static Set<String> keys(final String pattern) {
        Set<String> keys = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            keys = jedis.keys(pattern);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return keys;
    }

    public static long del(final String... keys) {
        long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[][] encodeKeys = new byte[keys.length][];
            for (int i = 0; i < keys.length; i++)
                encodeKeys[i] = SafeEncoder.encode(keys[i]);
            result = jedis.del(encodeKeys);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static long hdel(final String key, final String field) {
        long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hdel(key, field);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;

    }

    public static long listAdd(final String key, final Serializable value) {
        long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.rpush(SafeEncoder.encode(key), SerializableUtil.toByteArray(value));
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static long listAddFirst(final String key, final Serializable value) {
        long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.lpush(SafeEncoder.encode(key), SerializableUtil.toByteArray(value));
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static String listSet(final String key, final int index, final Serializable value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.lset(SafeEncoder.encode(key), index, SerializableUtil.toByteArray(value));
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static Object listGet(final String key, final long index) {
        Object result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = SerializableUtil.toObject(jedis.lindex(SafeEncoder.encode(key), index));
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    /**
     * <p>
     * 通过key判断值得类型
     * </p>
     *
     * @param key
     * @return
     */
    public String type(String key) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = jedisPool.getResource();
            res = jedis.type(key);
        } catch (Exception e) {

            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return res;
    }

    public static <T> List<T> queryList(final String key, final int start, final int end) {
        List<T> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = Lists.newArrayList();
            List<byte[]> retVals = jedis.lrange(SafeEncoder.encode(key), start, end);
            if (retVals != null) {
                for (byte[] val : retVals) {
                    if (null != val)
                        result.add((T) SerializableUtil.toObject(val));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;

    }

    public static long listSize(final String key) {
        long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.llen(SafeEncoder.encode(key));
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static boolean listTrim(final String key, final int start, final int end) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String retVal = jedis.ltrim(SafeEncoder.encode(key), start, end);
            result = "OK".equalsIgnoreCase(retVal);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static long incrementAndGet(final String key) {
        long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.incr(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static long decrementAndGet(final String key) {
        long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.decr(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static boolean hmset(final String key, final Map<String, String> values) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String retVal = jedis.hmset(key, values);
            result = "OK".equalsIgnoreCase(retVal);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static List<String> hvals(final String key) {
        List<String> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hvals(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static List<String> hmget(final String key, final String... fields) {
        List<String> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hmget(key, fields);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static String hget(final String key, final String field) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hget(key, field);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static Long hset(final String key, final String field, final String value) {
        long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hset(key, field, value);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static Map<String, String> hgetAll(final String key) {
        Map<String, String> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hgetAll(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static Double zincrby(final String key, final double score, final String member) {
        Double result = 0d;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zincrby(key, score, member);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static Double zscore(final String key, final String score) {
        Double result = 0d;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zscore(key, score);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static Long zadd(final String key, final double score, final String member) {
        long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zadd(key, score, member);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static Long zrem(final String key, final String member) {
        long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zrem(key, member);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
        Set<Tuple> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zrangeWithScores(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static String watch(final String... keys) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.watch(keys);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static Boolean exists(final String key) {
        Boolean result = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.exists(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static Long lpush(final String key, final Serializable value) {
        long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (value instanceof String) {
                result = jedis.lpush(key, (String) value);
            } else {
                result = jedis.lpush(SafeEncoder.encode(key), SerializableUtil.toByteArray(value));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static <T extends Serializable> T rpop(final String key) {
        T result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Object retObj = null;
            byte[] retVal = jedis.rpop(SafeEncoder.encode(key));
            if (null != retVal) {
                try {
                    retObj = SerializableUtil.toObject(retVal);
                } catch (Exception e) {
                    retObj = SafeEncoder.encode(retVal);
                }
                result = (T) retObj;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static <T extends Serializable> List<T> lrange(final String key, final long start, final long end) {
        List<T> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = Lists.newArrayList();
            List<byte[]> results = jedis.lrange(SafeEncoder.encode(key), start, end);
            for (byte[] val : results) {
                try {
                    result.add((T) SerializableUtil.toObject(val));
                } catch (Exception e) {
                    result.add((T) SafeEncoder.encode(val));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static <T extends Serializable> T rpoplpush(final String srckey, final String dstkey) {
        T result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] retVal = jedis.rpoplpush(SafeEncoder.encode(srckey), SafeEncoder.encode(dstkey));
            try {
                result = (T) SerializableUtil.toObject(retVal);
            } catch (Exception e) {
                result = (T) SafeEncoder.encode(retVal);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static Long lrem(String key, Serializable value) {
        return lrem(key, 1, value);
    }

    public static Long lrem(final String key, final long count, final Serializable value) {
        Long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (value instanceof String) {
                result = jedis.lrem(key, count, (String) value);
            } else {
                result = jedis.lrem(SafeEncoder.encode(key), count, SerializableUtil.toByteArray(value));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static Long publish(final String key, final Serializable value) {
        Long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (value instanceof String) {
                result = jedis.publish(key, (String) value);
            } else {
                result = jedis.publish(SafeEncoder.encode(key), SerializableUtil.toByteArray(value));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static boolean subscribe(final JedisPubSub jedisPubSub, final String... channels) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (channels instanceof String[]) {
                jedis.subscribe(jedisPubSub, (String[]) channels);
            }
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    public static boolean psubscribe(final JedisPubSub jedisPubSub, final String channels) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (channels instanceof String) {
                jedis.psubscribe(jedisPubSub, new String[] { channels });
            }
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    /**
     * 设置redis key的过期时间
     *
     * @param key
     * @param seconds
     *            过期秒数
     * @return
     */
    public static Long expire(final String key, final int seconds) {
        Long result = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.expire(key, seconds);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedisPool, jedis);
        }
        return result;
    }

    /**
     * 返还到连接池
     *
     * @param jedisPool
     * @param jedis
     */
    public static void returnResource(JedisPool jedisPool, Jedis jedis) {
        if (jedis != null) {
            // jedisPool.returnResource(jedis);
            jedis.close();
        }
    }

    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            // jedisPool.returnResource(jedis);
            jedis.close();
        }
    }

}
