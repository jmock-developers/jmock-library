package org.jmock.internal;


import java.util.HashSet;

public class BoxingUtils {
    private static final HashSet<Class<?>> WRAPPER_TYPES = new HashSet<Class<?>>() {{
        add(Boolean.class);
        add(Character.class);
        add(Byte.class);
        add(Short.class);
        add(Integer.class);
        add(Long.class);
        add(Float.class);
        add(Double.class);
        add(Void.class);
    }};

    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }
}
