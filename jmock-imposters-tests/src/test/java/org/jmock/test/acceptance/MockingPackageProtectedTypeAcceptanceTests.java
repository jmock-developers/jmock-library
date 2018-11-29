package org.jmock.test.acceptance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.jmock.Mockery;
import org.jmock.api.Imposteriser;
import org.jmock.internal.CaptureControl;
import org.jmock.test.unit.lib.legacy.ImposteriserParameterResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;


public class MockingPackageProtectedTypeAcceptanceTests {
    Mockery mockery = new Mockery();

    @Test
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
        
    @ParameterizedTest
    @ArgumentsSource(ImposteriserParameterResolver.class)
    public void testCanMockPackageProtectedTypeWithObjenesisImposteriser(Imposteriser imposteriserImpl) {
        mockery.setImposteriser(imposteriserImpl);
        mockery.mock(PackageProtectedType.class, "mock");
    }
}
