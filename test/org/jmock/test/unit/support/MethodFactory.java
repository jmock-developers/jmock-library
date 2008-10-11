/*  Copyright (c) 2000-2006 jMock.org
 */
package org.jmock.test.unit.support;

import java.lang.reflect.Method;

import net.sf.cglib.asm.ClassWriter;
import net.sf.cglib.asm.Type;
import net.sf.cglib.core.Constants;


public class MethodFactory extends ClassLoader {
    public static final int CLASS_FORMAT_VERSION = 49;
    
    public static Class<?>[] NO_ARGUMENTS = {};
    public static Class<?>[] NO_EXCEPTIONS = {};

    
    public Method newMethodReturning(Class<?> returnType) {
        return newMethod("ignoredMethodName", NO_ARGUMENTS, returnType, NO_EXCEPTIONS);
    }

    public Method newMethod(String name) {
    	return newMethod(name, NO_ARGUMENTS, void.class, NO_EXCEPTIONS);
    }
    
    public Method newMethod( final String methodName,
                             final Class<?>[] argTypes,
                             final Class<?> returnType,
                             final Class<?>[] exceptionTypes )
    {
        ClassLoader classLoader = new ClassLoader()
        {
            @Override
            protected Class<?> findClass( String interfaceName ) {
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);

                writer.visit(CLASS_FORMAT_VERSION,
                             Constants.ACC_PUBLIC|Constants.ACC_INTERFACE,
                             nameToClassFormat(interfaceName),
                             null,
                             "java/lang/Object",
                             null /* interfaces */);
                
                writer.visitMethod(Constants.ACC_PUBLIC | Constants.ACC_ABSTRACT,
                                   methodName,
                                   methodDescriptor(returnType, argTypes),
                                   null,
                                   classNamesInClassFormat(exceptionTypes));
                
                byte[] classAsBytes = writer.toByteArray();

                return defineClass(interfaceName, classAsBytes, 0, classAsBytes.length);
            }
        };

        try {
            Class<?> interfaceClass = classLoader.loadClass("InterfaceDefining_" + methodName);
            return interfaceClass.getMethod(methodName, argTypes);
        }
        catch (ClassNotFoundException ex) {
            throw new Error(ex.getMessage());
        }
        catch (NoSuchMethodException ex) {
            throw new Error(ex.getMessage());
        }
    }

    static String nameToClassFormat( String name ) {
        return name.replace('.', '/');
    }

    static String[] classNamesInClassFormat( Class<?>[] classes ) {
        String[] namesInClassFormat = new String[classes.length];

        for (int i = 0; i < classes.length; i++) {
            namesInClassFormat[i] = nameToClassFormat(classes[i].getName());
        }

        return namesInClassFormat;
    }

    static String methodDescriptor( Class<?> returnClass, Class<?>[] argClasses ) {
        return Type.getMethodDescriptor(Type.getType(returnClass), classesToTypes(argClasses));
    }

    private static Type[] classesToTypes( Class<?>[] classes ) {
        Type[] types = new Type[classes.length];

        for (int i = 0; i < classes.length; i++) {
            types[i] = Type.getType(classes[i]);
        }

        return types;
    }
}
