package org.jmock.testjar;

import javax.annotation.Nullable;

public interface InterfaceFromOtherClassLoader<T> {
    //void doSomething();
    
    @Nullable
    T stir(T tea);
    T stir(int lumps);
}
