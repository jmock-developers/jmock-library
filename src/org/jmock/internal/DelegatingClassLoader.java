package org.jmock.internal;

import java.util.ArrayList;
import java.util.List;


public class DelegatingClassLoader extends ClassLoader {
    private final ClassLoader delegate;
    
    public DelegatingClassLoader(ClassLoader parent, ClassLoader delegate) {
        super(parent);
        this.delegate = delegate;
    }
    
    public static ClassLoader combine(ClassLoader... parentLoaders) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        
        for (int i = parentLoaders.length-1; i >= 0; i--) {
            loader = new DelegatingClassLoader(parentLoaders[i], loader);
        }
        
        return loader;
    }
    
    public static ClassLoader combine(List<ClassLoader> parentLoaders) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        
        for (int i = parentLoaders.size()-1; i >= 0; i--) {
            loader = new DelegatingClassLoader(parentLoaders.get(i), loader);
        }
        
        return loader;
    }
    
    public static ClassLoader combineLoadersOf(Class... classes) {
        List<ClassLoader> loaders = new ArrayList<ClassLoader>();
        
        for (Class c : classes) {
            if (!loaders.contains(c.getClassLoader())) {
                loaders.add(c.getClassLoader());
            }
        }
        
        return combine(loaders);
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return delegate.loadClass(name);
    }
}
