/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.acceptance;

import junit.framework.TestCase;
import net.sf.cglib.asm.ClassWriter;
import net.sf.cglib.asm.Constants;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.test.unit.support.MethodFactory;


public class ClassLoaderAcceptanceTests extends TestCase
{
    Mockery mockery = new Mockery();
    
    public void testMockingTypeFromOtherClassLoaderWithDefaultImposteriser() throws ClassNotFoundException {
        Class<?> typeToMock = loadUniqueClass();
        mockery.mock(typeToMock);
    }
    
    public void testMockingTypeFromOtherClassLoaderWithClassImposteriser() throws ClassNotFoundException {
        mockery.setImposteriser(ClassImposteriser.INSTANCE);
        mockery.mock(loadUniqueClass());
    }
    
    private Class<?> loadUniqueClass() throws ClassNotFoundException {
        ClassLoader loader = new OrphanedFakeClassLoader();
        return loader.loadClass("$UniqueTypeName_"+System.currentTimeMillis()+"$");
    }

    static class OrphanedFakeClassLoader extends ClassLoader {
        public OrphanedFakeClassLoader() {
            super(null);
        }
        
        @Override
        protected Class<?> findClass(String name) {
            ClassWriter writer = new ClassWriter(true);
            
            writer.visit(MethodFactory.CLASS_FORMAT_VERSION, 
                         Constants.ACC_PUBLIC | Constants.ACC_INTERFACE,
                         name.replace('.', '/'),
                         "java/lang/Object",
                         null, /* interfaces */
                         null /* source file */);

            byte[] b = writer.toByteArray();
            
            return defineClass(name, b, 0, b.length);
        }
    }
}
