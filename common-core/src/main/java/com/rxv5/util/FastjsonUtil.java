package com.rxv5.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

public class FastjsonUtil {

	private static SerializeConfig mapping = new SerializeConfig();

	private static String dateFormat;

	static {
		dateFormat = "yyyy-MM-dd HH:mm:ss";
		mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
	}

	/**
	 * @param obj //对象模型
	 * @return String //转换完毕的字符串
	 * @Title: obj2Json
	 * @Description: 将对象转换为JSON字符串
	 */
	public static String obj2Json(Object obj) {
		String str = JSON.toJSONString(obj, mapping, SerializerFeature.WriteMapNullValue);
		return str;
	}

	/**
	 * @param json   //需要转换的字符串
	 * @param clazz //转换时使用的实体类型
	 * @return
	 * @Title: json2Obj
	 * @Description: 将JSON字符串转换为对象
	 */
	public static <T> T json2Obj(String json, Class<T> clazz) {
		T T = null;
		if (json != null && json.trim().length() > 0 && !"null".equals(json.toLowerCase())) {
			T = JSON.parseObject(json, clazz);
		}
		return T;
	}

	public static <T> T[] json2Objs(String json, Class<T[]> clazz) {
		T[] T = null;
		if (json != null && json.trim().length() > 0 && !"null".equals(json.toLowerCase())) {
			T = JSON.parseObject(json, clazz);
		}
		return T;
	}

	/**
	 * 解析JSON到LIST
	 *
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> List<T> json2ObjList(String jsonString, Class<T> cls) {
		List<T> list = null;
		if (jsonString != null && jsonString.trim().length() > 0 && !"null".equals(jsonString.toLowerCase())) {
			list = new ArrayList<T>();
			list = JSON.parseArray(jsonString, cls);
		}
		return list;
	}

	public static <T> T json2Obj(String json, TypeReference<T> typeReference) {
		T t = null;
		if (json != null && json.trim().length() > 0) {
			t = JSON.parseObject(json, typeReference);
		}
		return t;
	}

}
