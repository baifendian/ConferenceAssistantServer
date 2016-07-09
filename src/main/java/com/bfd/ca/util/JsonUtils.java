package com.bfd.ca.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

@SuppressWarnings("all")
public class JsonUtils {
	private static final Log log = LogFactory.getLog(JsonUtils.class);

	private static JsonFactory _jsonFactory = null;
	private static ObjectMapper om = new ObjectMapper(_jsonFactory);

	static {
		if (_jsonFactory == null) {
			_jsonFactory = new JsonFactory();
			_jsonFactory.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
			_jsonFactory.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			
		}
	}

	public static String toJSONString(Object object) {
		try {
//			om.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
			return om.writeValueAsString(object);
		} catch (Exception e) {
			log.warn("to json string exception, will use fastjson to parse", e);
		}
		return null;
	}

	public static Object parseObject(String str) throws Exception {
		return om.readValue(str.getBytes(), Object.class);
	}

	public static Object parse(String str) throws Exception {
		return parseObject(str);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Object> parseArray(String str) throws Exception {
		return (List<Object>) om.readValue(str.getBytes(), Object.class);
	}
}
