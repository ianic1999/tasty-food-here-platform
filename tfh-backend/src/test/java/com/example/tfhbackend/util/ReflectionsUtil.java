package com.example.tfhbackend.util;

import java.lang.reflect.Field;

public class ReflectionsUtil {

    public static void setId(Object object, Long id) {
        try {
            Field idField = object.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(object, id);
            idField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Unable to set id to " + object.getClass().getName());
        }
    }

}
