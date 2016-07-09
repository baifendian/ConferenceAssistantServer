package com.bfd.ca.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.beanutils.BeanUtils;
import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class BeanUtil {

	private volatile static BeanUtil singleton = null;

	public static BeanUtil getInstance() {
		if (singleton == null) {
			synchronized (BeanUtil.class) {
				if (singleton == null) {
					singleton = new BeanUtil();
				}
			}
			singleton = new BeanUtil();
		}
		return singleton;
	}

	private BeanUtil() {
	}

	/**
	 * 
	 * bean2DBObj:(把实体bean对象转换成DBObject).
	 * 
	 * @author sid
	 * @param obj
	 * @return
	 */
	public static BasicDBObject bean2DBObj(Object obj) {
		BasicDBObject db = new BasicDBObject();
		Method metd = null;
		String fdname = null;
		try {
			Class<?> clazz = obj.getClass();
			Field[] fds = clazz.getDeclaredFields();
			for (Field field : fds) {
				fdname = field.getName();
				metd = clazz.getMethod("get" + change(fdname));
				Object value = metd.invoke(obj);
				// 存储属性和对应值
				db.put(fdname, value);
			}
		} catch (Exception e) {
			LogUtil.getLogger(BeanUtil.class)
					.error("转换对象获取属性异常", e);
			e.printStackTrace();
		}
		return db;
	}

	/**
	 * 
	 * beans2DBObjs:(把实体bean对象转换成DBObject). 
	 *
	 * @author sid
	 * @param objs
	 * @return
	 */
	public static List<DBObject> beans2DBObjs(List<?> objs) {
		List<DBObject> list = new ArrayList<DBObject>();
		for (Object obj : objs) {
			list.add(bean2DBObj(obj));
		}
		return list;
	}

	/**
	 * 
	 * dbObject2Bean:把DBObject转换成bean对象
	 *
	 * @author sid
	 * @param dbObject
	 * @param bean
	 * @return
	 */
	public static <T> T dbObj2Bean(Document dbObject, T bean) {
		if (bean == null) {
			return null;
		}
		try {
			Field[] fields = bean.getClass().getDeclaredFields();
			for (Field field : fields) {
				String varName = field.getName();
				Object object = dbObject.get(varName);
				if (object != null) {
					if(object instanceof String||object instanceof Long||object instanceof Integer)
						BeanUtils.setProperty(bean, varName, object);
				}
			}
		} catch (Exception e) {
			LogUtil.getLogger(BeanUtil.class)
					.error("DBObject对象转为"+bean.getClass().getName()+"异常", e);
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 将字符串第一个字符大写并返还
	 * 
	 * @author sid
	 * @param src
	 *            源字符串
	 * @return 字符串，将src的第一个字母转换为大写，src为空时返回null
	 */
	public static String change(String src) {
		if (src != null) {
			StringBuffer sb = new StringBuffer(src);
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			return sb.toString();
		} else {
			return null;
		}
	}
}