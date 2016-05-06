package net.masterthought.cucumber.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReflectionUtility {

    public static <T> T invokeMethod(Object instance, String methodName, Object... arguments)
            throws Exception {
        try {
            return invokeMethod(instance, instance.getClass().getDeclaredMethods(), methodName, arguments);
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            // if method throws an exception the cause needs to be extracted to pass to caller
            throw (Exception) e.getCause();
        }
    }

    public static <T> T invokeMethod(Object instance, Method[] methods, String methodName, Object... arguments)
            throws Exception {
        // instead of providing list of types let's assume
        // that there is only one method in given class with passed name
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                if (arguments == null) {
                    return (T) method.invoke(instance);
                } else {
                    return (T) method.invoke(instance, arguments);
                }
            }
        }

        // check if given method is not implemented in parent class
        if (instance.getClass().getSuperclass() == null) {
            throw new IllegalArgumentException("Could not find method: " + methodName);
        } else {
            return invokeMethod(instance, instance.getClass().getSuperclass().getDeclaredMethods(), methodName,
                    arguments);
        }
    }

    public static <T> T getField(Object instance, String fieldName, Class<T> returnType) {
        try {
            Field[] fields = instance.getClass().getSuperclass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    return (T) field.get(instance);
                }
            }

            if (instance.getClass().getSuperclass() != null) {
                return getField(instance.getClass().getSuperclass(), fieldName, returnType);
            } else {
                throw new IllegalArgumentException("Could not find attribute: " + fieldName);
            }
        } catch (SecurityException | IllegalAccessException e) {
            // if method throws an exception the cause needs to be extracted to pass to caller
            throw new RuntimeException(e);
        }
    }

    public static void setField(Object instance, String fieldName, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        } 
    }
}
