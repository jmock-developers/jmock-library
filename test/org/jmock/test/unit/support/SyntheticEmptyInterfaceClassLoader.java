/**
 * 
 */
package org.jmock.test.unit.support;

import static org.jmock.test.unit.support.MethodFactory.CLASS_FORMAT_VERSION;

import java.util.regex.Pattern;

import net.sf.cglib.asm.ClassWriter;
import net.sf.cglib.core.Constants;

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
        }
        else {
            throw new ClassNotFoundException(name);
        }
    }

    private Class<?> synthesiseInterface(String name) throws ClassFormatError {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                writer.visit(CLASS_FORMAT_VERSION,
                             Constants.ACC_PUBLIC|Constants.ACC_INTERFACE,
                             MethodFactory.nameToClassFormat(name),
                             null,
                             "java/lang/Object",
                             null /* interfaces */);
        
        byte[] b = writer.toByteArray();

        return defineClass(name, b, 0, b.length);
    }
}