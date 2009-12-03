package org.jmock.auto.internal;

import java.lang.reflect.Field;

import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.States;
import org.jmock.auto.Auto;
import org.jmock.auto.Mock;


public class Mockomatic {
    private final Mockery mockery;

    public Mockomatic(Mockery mockery) {
        this.mockery = mockery;
    }

    public void fillIn(Object object) {
        for (Class<?> type = object.getClass(); type != Object.class; type = type.getSuperclass()) {
            Field[] fields = type.getDeclaredFields();
            for (Field field: fields) {
                if (field.isAnnotationPresent(Mock.class)) {
                    autoMock(object, field);
                }
                else if (field.isAnnotationPresent(Auto.class)) {
                    if (field.getType() == States.class) {
                        autoInstantiateStates(field, object);
                    }
                    else if (field.getType() == Sequence.class) {
                        autoInstantiateSequence(field, object);
                    }
                    else {
                        throw new IllegalStateException("cannot auto-instantiate field of type " + field.getType().getName());
                    }
                }
            }
        }
    }

    private void autoInstantiateStates(Field field, Object object) {
        try {
            field.setAccessible(true);
            field.set(object, mockery.states(field.getName()));
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("cannot auto-instantiate States field " + field.getName(), e);
        }
    }
    
    private void autoInstantiateSequence(Field field, Object object) {
        try {
            field.setAccessible(true);
            field.set(object, mockery.sequence(field.getName()));
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("cannot auto-instantiate Sequence field " + field.getName(), e);
        }
    }

    private void autoMock(Object object, Field field) {
        try {
            field.setAccessible(true);
            field.set(object, mockery.mock(field.getType(), field.getName()));
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("cannot auto-mock field " + field.getName(), e);
        }
    }
}
