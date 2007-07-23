package org.jmock.test.acceptance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import junit.framework.TestCase;

import org.jmock.Mockery;
import org.jmock.internal.CaptureControl;
import org.jmock.lib.legacy.ClassImposteriser;


public class MockingPackageProtectedTypeAcceptanceTests extends TestCase {
    Mockery mockery = new Mockery();

    public void testCanCreateReflectionProxyOfPackageProtectedType() {
        Class<?> typeToProxy = PackageProtectedType.class;
        
        Proxy.newProxyInstance(
            typeToProxy.getClassLoader(),
            new Class<?>[]{typeToProxy, CaptureControl.class}, 
            new InvocationHandler(){
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return null;
                }
            });
    }
    
    public void testCanMockPackageProtectedTypeWithDefaultImposteriser() {
        mockery.mock(PackageProtectedType.class, "mock");
    }
    
    public void testCanMockPackageProtectedTypeWithObjenesisImposteriser() {
        mockery.setImposteriser(ClassImposteriser.INSTANCE);
        mockery.mock(PackageProtectedType.class, "mock");
    }
}
