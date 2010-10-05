package atest.jmock.cglib;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class SignedJarAcceptanceTest extends MockObjectTestCase {
    public void testCanMockTypesInSignedJars() throws Exception {
        File jarFile = new File("build/testdata/signed.jar");
        
        assertTrue("Signed JAR file does not exist (use Ant to build it", jarFile.exists());
        
        URL jarURL = jarFile.toURL();
        ClassLoader loader = new URLClassLoader(new URL[]{jarURL});
        Class typeInSignedJar = loader.loadClass("TypeInSignedJar");
        
        Mock mock = mock(typeInSignedJar);
        
        assertTrue(typeInSignedJar.isInstance(mock.proxy()));
    }
}
