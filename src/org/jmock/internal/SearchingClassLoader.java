package org.jmock.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SearchingClassLoader extends ClassLoader {
    private final ClassLoader nextToSearch;
    
    public SearchingClassLoader(ClassLoader parent, ClassLoader nextToSearch) {
        super(parent);
        this.nextToSearch = nextToSearch;
    }
    
    public static ClassLoader combine(ClassLoader... parentLoaders) {
        return combine(Arrays.asList(parentLoaders));
    }
    
    public static ClassLoader combine(List<ClassLoader> parentLoaders) {
        ClassLoader loader = parentLoaders.get(parentLoaders.size()-1);
        
        for (int i = parentLoaders.size()-2; i >= 0; i--) {
            loader = new SearchingClassLoader(parentLoaders.get(i), loader);
        }
        
        return loader;
    }
    
    public static ClassLoader combineLoadersOf(Class... classes) {
        return combineLoadersOf(classes[0], classes);
    }
    
    public static ClassLoader combineLoadersOf(Class<?> first, Class<?>... others) {
        List<ClassLoader> loaders = new ArrayList<ClassLoader>();
        
        loaders.add(ClassLoader.getSystemClassLoader());
        addIfNotIn(loaders, first);
        for (Class c : others) {
            addIfNotIn(loaders, c);
        }
        
        return combine(loaders);
    }
    
    private static void addIfNotIn(List<ClassLoader> loaders, Class<?> c) {
        if (!loaders.contains(c.getClassLoader())) {
            loaders.add(c.getClassLoader());
        }
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return nextToSearch.loadClass(name);
    }
}
