package atest.jmock;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Constants;


public class ClassLoaderAcceptanceTest extends MockObjectTestCase {
    
    static class EmptyInterfaceCreator extends ClassLoader {
        ClassLoader resourceLoader;
        
        protected Class findClass(String name) {
            ClassWriter writer = new ClassWriter(true);
            writer.visit(Constants.ACC_PUBLIC|Constants.ACC_INTERFACE,
                         name.replace('.','/'),
                         "java/lang/Object",
                         null, /* interfaces */
                         null /* source file */ );
            
            byte[] b = writer.toByteArray();
            
            return defineClass( name, b, 0, b.length );
        }
    }
    
    public void testMockingTypeFromOtherClassLoader() throws ClassNotFoundException {
        ClassLoader interfaceClassLoader = new EmptyInterfaceCreator();
        Class interfaceClass = interfaceClassLoader.loadClass("$UniqueTypeName$");
        
        Mock mock = mock(interfaceClass); // Should not throw an exception
        
        mock.verify(); // stop warning about unused variable
    }
}
