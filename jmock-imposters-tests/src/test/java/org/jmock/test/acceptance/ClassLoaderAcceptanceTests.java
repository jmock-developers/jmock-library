/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.acceptance;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import org.jmock.Mockery;
import org.jmock.api.Imposteriser;
import org.jmock.test.unit.lib.legacy.CodeGeneratingImposteriserParameterResolver;
import org.jmock.test.unit.lib.legacy.ImposteriserParameterResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;


public class ClassLoaderAcceptanceTests {

    private static final String UNSIGNED_JAR_NAME = "../testjar/target/unsigned.jar";
    private static final String CLASS_FROM_OTHER_CLASS_LOADER = "org.jmock.testjar.ClassFromOtherClassLoader";
    private static final String INTERFACE_FROM_OTHER_CLASS_LOADER = "org.jmock.testjar.InterfaceFromOtherClassLoader";
    
    Mockery mockery = new Mockery();
    ClassLoader classLoader;
    
    @BeforeEach
    public void setUp() throws MalformedURLException, URISyntaxException {
        File unsignedFile = new File(UNSIGNED_JAR_NAME);
        assertTrue("The unsigned  file is missing, mvn package will build it",unsignedFile.exists());
        classLoader = new URLClassLoader(new URL[]{unsignedFile.toURI().toURL()}, null);
    }
        
    @ParameterizedTest
    @ArgumentsSource(ImposteriserParameterResolver.class)
    public void testMockingInterfaceFromOtherClassLoaderWithClassImposteriser(Imposteriser imposteriserImpl) throws ClassNotFoundException {
        mockery.setImposteriser(imposteriserImpl);
        mockery.mock(classLoader.loadClass(INTERFACE_FROM_OTHER_CLASS_LOADER));
    }
    
    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void testMockingClassFromOtherClassLoaderWithClassImposteriser(Imposteriser imposteriserImpl) throws ClassNotFoundException {
        mockery.setImposteriser(imposteriserImpl);
        mockery.mock(classLoader.loadClass(CLASS_FROM_OTHER_CLASS_LOADER));
    }
    
    // I've been unable to reproduce the behaviour of the Maven Surefire plugin in plain JUnit tests
    @Disabled
    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void testMockingClassFromThreadContextClassLoader(final Imposteriser imposteriserImpl) throws Throwable {
        Runnable task = new Runnable() {
            public void run() {
                try {
                    Class<?> classToMock = Thread.currentThread().getContextClassLoader().loadClass(CLASS_FROM_OTHER_CLASS_LOADER);
                    
                    Mockery threadMockery = new Mockery();
                    threadMockery.setImposteriser(imposteriserImpl);
                    
                    threadMockery.mock(classToMock);
                }
                catch (ClassNotFoundException e) {
                    throw new IllegalStateException("could not load class", e);
                }
            }
        };
        
        ExceptionTrap exceptionTrap = new ExceptionTrap();
        
        Thread thread = new Thread(task, getClass().getSimpleName() + " Thread");
        thread.setContextClassLoader(new URLClassLoader(new URL[]{new URL("file:build/testdata/unsigned.jar")}, null));
        thread.setUncaughtExceptionHandler(exceptionTrap);
        
        thread.start();
        thread.join();
        
        if (exceptionTrap.exception != null) {
            throw exceptionTrap.exception;
        }
    }
    
    private static class ExceptionTrap implements UncaughtExceptionHandler {
        public Throwable exception = null;
        
        public void uncaughtException(Thread t, Throwable e) {
            exception = e;
        }
    }
}
