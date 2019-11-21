package com.rxv5.redis.sn;

/**
 * 流水号生成器
 *
 */
public interface SerialNumberGenerator {

    String PADDING_STR = "0";

    /**
     * 下一流水号long型
     * 
     * @return
     */
    long nextLong();

    /**
     * 下一流水号string型
     * 
     * @return
     */
    String nextString();

    /**
     * 下一流水号，位数补齐
     * 
     * @param padding
     *            补齐字符，默认使用0补齐
     * @return
     */
    String nextString(String padding);

    /**
     * 初始化流水号生成器
     * 
     * @param name
     * @param maxValue
     * @param prefix
     */
    void init(String name, long maxValue, String prefix);

    /**
     * 初始化流水号生成器
     * 
     * @param name
     * @param maxValue
     * @param minValue
     * @param prefix
     */
    void init(String name, long maxValue, long minValue, String prefix);
}
