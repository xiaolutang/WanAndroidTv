package com.txl.commonlibrary;

import com.txl.commonlibrary.utils.ReflectUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 测试反射工具类
 * */
public class ReflectTest {

    @Test
    public void testGetReflectPrivateFiled(){
        ReflectTest reflectTest = new ReflectTest();
        Integer integer = ReflectUtils.reflectPrivateField(reflectTest,"test");
        System.out.println("获取到的结果是："+integer);
        assertEquals(123, reflectTest.getTest());
    }

    @Test
    public void testSetReflectPrivateFiled(){
        ReflectTest reflectTest = new ReflectTest();
        ReflectUtils.reflectPrivateField(reflectTest,"name","five");
        assertEquals("five", reflectTest.getName());
        ReflectUtils.reflectPrivateField(reflectTest,"test",5);
        assertEquals(5, reflectTest.getTest());
    }

    @Test
    public void testReflectPrivateMethod(){
        ReflectTest reflectTest = new ReflectTest();
        Object[] object = new Object[]{};
        ReflectUtils.reflectPrivateMethod(reflectTest,"privateMethod",object);
        Object[] object1 = new Object[]{"我来测试测试"};
        ReflectUtils.reflectPrivateMethod(reflectTest,"privateMethod",object1,String.class);
    }


    private int test = 123;

    private String name = "test";

    public int getTest(){
        return test;
    }

    public String getName(){
        return name;
    }

    private void privateMethod(){
        System.out.println("调用了零个参数的私有函数");
    }

    private void privateMethod(String s){
        System.out.println("调用了一个参数的私有函数 参数是： "+s);
    }
}
