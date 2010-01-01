package org.jmock.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AllDeclaredFields {
    
    public static List<Field> in(Class<?> clazz) {
        final ArrayList<Field> fields = new ArrayList<Field>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                fields.add(field);
            }
        }
        return fields;
    }
}
