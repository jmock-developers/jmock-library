/**
 * 
 */
package org.jmock.test.unit.support;

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
        ClassWriter writer = new ClassWriter(true);
        writer.visit(45,
                     Constants.ACC_PUBLIC | Constants.ACC_INTERFACE,
                     name.replace('.', '/'),
                     "java/lang/Object",
                     null, /* interfaces */
                     null /* source file */);
        
        byte[] b = writer.toByteArray();

        return defineClass(name, b, 0, b.length);
    }
}