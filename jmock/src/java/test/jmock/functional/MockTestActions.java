/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.functional;

public interface MockTestActions {
    void expectNoParams();

    void expectOneParam();

    void expectTwoParams();

    void expectNotNoParams();

    void callNoParams();

    void callOneParam();

    void callTwoParams();

    void callIncorrectSecondParameter();

    void verifyMock();
}