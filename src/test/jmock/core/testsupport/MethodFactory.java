/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.testsupport;

import java.lang.reflect.Method;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Constants;
import org.objectweb.asm.Type;


public class MethodFactory extends ClassLoader
{
    public static Class[] NO_ARGUMENTS = {};
    public static Class[] NO_EXCEPTIONS = {};


    public Method newMethodReturning( Class returnType ) {
        return newMethod("ignoredMethodName", NO_ARGUMENTS, returnType, NO_EXCEPTIONS);
    }

    public Method newMethod( final String methodName,
                             final Class[] argTypes,
                             final Class returnType,
                             final Class[] exceptionTypes )
    {
        ClassLoader classLoader = new ClassLoader()
        {
            protected Class findClass( String interfaceName ) {
                ClassWriter writer = new ClassWriter(true);

                writer.visit(Constants.ACC_PUBLIC | Constants.ACC_INTERFACE,
                             nameToClassFormat(interfaceName),
                             "java/lang/Object",
                             null, /* interfaces */
                             null  /* source file */);

                writer.visitMethod(Constants.ACC_PUBLIC | Constants.ACC_ABSTRACT,
                                   methodName,
                                   methodDescriptor(returnType, argTypes),
                                   classNamesInClassFormat(exceptionTypes),
                                   null /* no attributes */);

                byte[] classAsBytes = writer.toByteArray();

                return defineClass(interfaceName, classAsBytes, 0, classAsBytes.length);
            }
        };

        try {
            Class interfaceClass = classLoader.loadClass("InterfaceDefining_" + methodName);
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

    static String[] classNamesInClassFormat( Class[] classes ) {
        String[] namesInClassFormat = new String[classes.length];

        for (int i = 0; i < classes.length; i++) {
            namesInClassFormat[i] = nameToClassFormat(classes[i].getName());
        }

        return namesInClassFormat;
    }

    static String methodDescriptor( Class returnClass, Class[] argClasses ) {
        return Type.getMethodDescriptor(Type.getType(returnClass), classesToTypes(argClasses));
    }

    private static Type[] classesToTypes( Class[] classes ) {
        Type[] types = new Type[classes.length];

        for (int i = 0; i < classes.length; i++) {
            types[i] = Type.getType(classes[i]);
        }

        return types;
    }
}
