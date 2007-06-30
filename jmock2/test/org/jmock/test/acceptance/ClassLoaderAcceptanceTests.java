/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.acceptance;

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
}
