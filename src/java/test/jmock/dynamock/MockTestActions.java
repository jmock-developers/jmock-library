/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamock;

public interface MockTestActions {
	void stubNoParams();
    void expectNoParams();
    
    void stubOneParam();
    void expectOneParam();

    void stubTwoParams();
    void expectTwoParams();

    void expectNotNoParams();

    void callNoParams();

    void callOneParam();

    void callTwoParams();

    void callIncorrectSecondParameter();

    void verifyMock();
}