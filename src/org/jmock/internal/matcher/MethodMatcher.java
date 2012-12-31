package org.jmock.internal.matcher;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class MethodMatcher extends TypeSafeMatcher<Method> {
    private Method expectedMethod;
    
    public MethodMatcher(Method expectedMethod) {
        super(Method.class);
        this.expectedMethod = expectedMethod;
    }

//    @Override
//    public boolean matchesSafely(Method m) {
//        System.out.format("Expected %s, actual %s\n", expectedMethod, m);
//        return expectedMethod.equals(m);
//    }

    @Override
    public boolean matchesSafely(Method m) {
        System.out.format("Expected %s, actual %s\n", expectedMethod, m);
        if (expectedMethod.equals(m))
            return true;
        if (!expectedMethod.getName().equals(m.getName()))
            return false;
        if (!areImplementingInterfaces(expectedMethod.getDeclaringClass(), m.getDeclaringClass()))
            return false;
        if (!(Modifier.isAbstract(expectedMethod.getModifiers()) && Modifier.isAbstract(m.getModifiers())))
            return false;
        if (!m.getReturnType().isAssignableFrom(expectedMethod.getReturnType()))
            return false;
        if (!parametersAssignable(expectedMethod, m))
            return false;
        return true;
    }

    private boolean areImplementingInterfaces(Class<?> expectedClass, Class<?> actualClass) {
        if (!actualClass.isAssignableFrom(expectedClass))
            return false;
        if (!(actualClass.isInterface() && expectedMethod.getDeclaringClass().isInterface()))
            return false;
        return true;
    }

    private boolean parametersAssignable(Method m1, Method m2) {
        if (m1.getParameterTypes().length != m2.getParameterTypes().length)
            return false;
        for (int i = 0; i < m2.getParameterTypes().length; i++) {
            if (!m2.getParameterTypes()[i].isAssignableFrom(m2.getParameterTypes()[i]))
                return false;
        }
        return true;
    }

    @Override
    protected void describeMismatchSafely(Method m, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendText(m.getName());
    }
    
    public void describeTo(Description description) {
        description.appendText(expectedMethod.getName());
    }

}
