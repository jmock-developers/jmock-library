/**
 * 
 */
package uk.jamesdal.perfmock.test.unit.support;

import static uk.jamesdal.perfmock.test.unit.support.MethodFactory.CLASS_FORMAT_VERSION;

import java.util.regex.Pattern;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class SyntheticEmptyInterfaceClassLoader extends ClassLoader {
    private Pattern namePattern;

    public SyntheticEmptyInterfaceClassLoader() {
        this(".*");
    }

    public SyntheticEmptyInterfaceClassLoader(String namePatternRegex) {
        namePattern = Pattern.compile(namePatternRegex);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (namePattern.matcher(name).matches()) {
            return synthesiseInterface(name);
        } else {
            throw new ClassNotFoundException(name);
        }
    }

    private Class<?> synthesiseInterface(String name) throws ClassFormatError {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        writer.visit(CLASS_FORMAT_VERSION,
                Opcodes.ACC_PUBLIC | Opcodes.ACC_INTERFACE,
                MethodFactory.nameToClassFormat(name),
                null,
                "java/lang/Object",
                null /* interfaces */);

        byte[] b = writer.toByteArray();

        return defineClass(name, b, 0, b.length);
    }
}
