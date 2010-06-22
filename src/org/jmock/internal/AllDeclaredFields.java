package org.jmock.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class AllDeclaredFields {
    public static List<Field> in(Class<?> clazz) {
        final ArrayList<Field> fields = new ArrayList<Field>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            fields.addAll(asList(c.getDeclaredFields()));
        }
        return fields;
    }
}
