/*  Copyright (c) 2000-2004 jMock.org
 */
package atest.jmock;

import net.sf.cglib.asm.ClassWriter;
import net.sf.cglib.asm.Constants;

import org.jmock.MockObjectTestCase;

import test.jmock.core.testsupport.MethodFactory;


public class ClassLoaderAcceptanceTest extends MockObjectTestCase
{

    static class EmptyInterfaceCreator extends ClassLoader {
        public EmptyInterfaceCreator() {
            super(null);
        }
        
        protected Class findClass( String name ) {
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

    public void testMockingTypeFromOtherClassLoader() throws ClassNotFoundException {
        ClassLoader interfaceClassLoader = new EmptyInterfaceCreator();
        Class interfaceClass = interfaceClassLoader.loadClass("$UniqueTypeName$");

        mock(interfaceClass); // Should not throw an exception
    }
}
