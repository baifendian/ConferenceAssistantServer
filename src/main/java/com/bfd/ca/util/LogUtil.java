package com.bfd.ca.util;


import org.apache.log4j.Logger;

/**
 * Created by jinwei.li on 2016/6/27.
 */
@SuppressWarnings("all")
public class LogUtil {

    /**
     * 使用log4j记录
     * @param cls
     * @return
     */
    public static Logger getLogger(Class<?> cls) {
        Logger logger = Logger.getLogger(cls);
        return logger;
    }
}
