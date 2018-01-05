/*
 * Create Author  : dsfan
 * Create Date    : 2016年10月17日
 * File Name      : DaoFactory.java
 */

package com.baifendian.comp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dao 工厂
 * <p>
 * 
 * @author : dsfan
 * @date : 2016年10月17日
 */
public class DaoFactory {

    /** LOGGER */
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoFactory.class);

    /** dao 实例 map */
    private static Map<String, BaseDao> daoMap = new ConcurrentHashMap<>();

    /**
     * 获取 Dao 实例 （单例）
     * <p>
     *
     * @param clazz
     * @return Dao实例
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseDao> T getDaoInstance(Class<T> clazz) {
        String className = clazz.getName();
        synchronized (daoMap) {
            if (!daoMap.containsKey(className)) {
                try {
                    T t = clazz.getConstructor().newInstance();
                    t.init(); // 实例初始化
                    daoMap.put(className, t);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }

        return (T) daoMap.get(className);
    }
}
