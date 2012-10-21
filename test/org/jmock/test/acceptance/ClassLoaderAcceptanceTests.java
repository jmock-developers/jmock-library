/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.acceptance;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import junit.framework.TestCase;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;


public class ClassLoaderAcceptanceTests extends TestCase {
    Mockery mockery = new Mockery();
    ClassLoader classLoader;
    
    @Override
    public void setUp() throws MalformedURLException {
        classLoader = new URLClassLoader(new URL[]{new URL("file:build/testdata/unsigned.jar")}, null);
    }
    
    public void testMockingInterfaceFromOtherClassLoaderWithDefaultImposteriser() throws ClassNotFoundException {
        mockery.mock(classLoader.loadClass("InterfaceFromOtherClassLoader"));
    }
    
    public void testMockingInterfaceFromOtherClassLoaderWithClassImposteriser() throws ClassNotFoundException {
        mockery.setImposteriser(ClassImposteriser.INSTANCE);
        mockery.mock(classLoader.loadClass("InterfaceFromOtherClassLoader"));
    }
    
    public void testMockingClassFromOtherClassLoaderWithClassImposteriser() throws ClassNotFoundException {
        mockery.setImposteriser(ClassImposteriser.INSTANCE);
        mockery.mock(classLoader.loadClass("ClassFromOtherClassLoader"));
    }
    
    // I've been unable to reproduce the behaviour of the Maven Surefire plugin in plain JUnit tests
    public void DISABLED_testMockingClassFromThreadContextClassLoader() throws Throwable {
        Runnable task = new Runnable() {
            public void run() {
                try {
                    Class<?> classToMock = Thread.currentThread().getContextClassLoader().loadClass("ClassFromOtherClassLoader");
                    
                    Mockery threadMockery = new Mockery();
                    threadMockery.setImposteriser(ClassImposteriser.INSTANCE);
                    
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
