package org.jmock.test.acceptance;

public interface MockedType {
	void doSomething();
	void doSomethingWith(String s);
	void doSomethingWith(String x, String y);
	void doSomethingWithArray(String[] strings);

    void method1();
    void method2();
    void method3();
    void method4();
    
    String returnString();
    int returnInt();
}
