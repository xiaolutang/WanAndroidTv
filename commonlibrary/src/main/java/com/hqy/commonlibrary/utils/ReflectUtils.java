package com.hqy.commonlibrary.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 * */
public class ReflectUtils {
    /**
     * 获取私有有属性
     * @param object 需要获取私有属性的对象
     * @param className 类
     * @param filedName 私有属性名称
     * @return T 返回私有属性的类型
     * */
    public static <T> T reflectPrivateField(Object object,Class className, String filedName){
        try {
            Field fieldTag = className.getDeclaredField(filedName);
            fieldTag.setAccessible(true);
            return (T) fieldTag.get(object);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * 获取私有有属性
     * @param object 需要获取私有属性的对象
     * @param filedName 私有属性名称
     * @return T 返回私有属性的类型
     * */
    public static <T> T reflectPrivateField(Object object,String filedName) {
        return reflectPrivateField(object,object.getClass(),filedName);
    }


    /**
     * 获取私有有属性
     * @param object 需要获取私有属性的对象
     * @param className 类
     * @param filedName 私有属性名称
     * @param value  T 私有属性的值
     * */
    public static <T> void reflectPrivateField(Object object,Class className, String filedName,T value){
        try {
            Field fieldTag = className.getDeclaredField(filedName);
            fieldTag.setAccessible(true);
            fieldTag.set(object,value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 获取私有有属性
     * @param object 需要获取私有属性的对象
     * @param filedName 私有属性名称
     * @param value  T 私有属性的值
     * */
    public static <T> void reflectPrivateField(Object object,String filedName,T value) {
        reflectPrivateField(object,object.getClass(),filedName,value);
    }

    /**
     * @param object 需要反射调用私有函数的类
     * @param methodName 方法名
     * @param args 调用函数所需要的参数
     * @param params 参数类型
     * */
    public static void reflectPrivateMethod(Object object,String methodName, Object[] args,Class<?>... params){
        try {
            Class objectClass = object.getClass();
            Method method = objectClass.getDeclaredMethod(methodName,params);
            method.setAccessible(true);
            method.invoke(object,args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
