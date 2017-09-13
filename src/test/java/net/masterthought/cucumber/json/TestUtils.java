package net.masterthought.cucumber.json;

import java.lang.reflect.Field;

/**
 * @author Sean Bucholtz (sean.bucholtz@gmail.com)
 */
public class TestUtils {

    /**
     * Sets the value of a field for a given instance using Reflection API.
     * @param fieldName the name of the target field
     * @param value the value to set the field to
     * @param classInstance the instance of the class containing the field to be set
     * @param <V> the value data type
     * @param <T> the class type of the instance
     * @throws Exception if there was a problem setting the field value
     */
    public static <V, T> void setFieldViaReflection(String fieldName, V value, T classInstance) throws Exception
    {
        Field targetField = classInstance.getClass().getDeclaredField(fieldName);
        targetField.setAccessible(true);
        targetField.set(classInstance, value);
    }
}
