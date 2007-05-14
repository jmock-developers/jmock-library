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
        List<ClassLoader> loaders = new ArrayList<ClassLoader>();
        loaders.add(ClassLoader.getSystemClassLoader());
        
        for (Class c : classes) {
            if (!loaders.contains(c.getClassLoader())) {
                loaders.add(c.getClassLoader());
            }
        }
        
        return combine(loaders);
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return nextToSearch.loadClass(name);
    }
}
