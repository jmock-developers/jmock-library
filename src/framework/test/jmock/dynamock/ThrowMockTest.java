/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamock;

import org.jmock.dynamock.C;
import org.jmock.dynamock.Mock;


public class ThrowMockTest extends AbstractMockTest {
    public class TargetException extends Exception {
    }
    public interface TargetType {
        void noParams() throws TargetException;

        void oneParam(Object aParam) throws TargetException;

        void twoParams(Object param1, Object param2) throws TargetException;
    }

    public class ReturnMockTestActions implements MockTestActions {
        private Mock mockTarget = new Mock(TargetType.class);
        private TargetType targetProxy = ((TargetType) mockTarget.proxy());
        
        public void stubNoParams() {
        	mockTarget.stubAndThrow("noParams", C.NO_ARGS, new TargetException());
        }
        
        public void expectNoParams() {
            mockTarget.expectAndThrow("noParams", C.NO_ARGS, new TargetException());
        }

        public void stubOneParam() {
        	mockTarget.stubAndReturn("oneParam", C.args(C.eq("one")), new TargetException());
        }

        public void expectOneParam() {
            mockTarget.expectAndThrow("oneParam", C.args(C.eq("one")), new TargetException());
        }
        
        public void stubTwoParams() {
        	mockTarget.stubAndThrow("twoParams", C.args(C.eq("one"), C.eq("two")), new TargetException());
        }

        public void expectTwoParams() {
            mockTarget.expectAndThrow("twoParams", C.args(C.eq("one"), C.eq("two")), new TargetException());
        }

        public void expectNotNoParams() {
            mockTarget.expectAndThrow("notNoParams", C.NO_ARGS, new TargetException());
        }

        public void callNoParams() {
            try {
                targetProxy.noParams();
            } catch (TargetException expected) {
                return;
            }
            fail("Should have thrown exception");
        }

        public void callOneParam() {
            try {
                targetProxy.oneParam("one");
            } catch (TargetException expected) {
                return;
            }
            fail("Should have thrown exception");
        }

        public void callTwoParams() {
            try {
                targetProxy.twoParams("one", "two");
            } catch (TargetException expected) {
                return;
            }
            fail("Should have thrown exception");
        }

        public void callIncorrectSecondParameter() {
            try {
                targetProxy.twoParams("one", "not two");
            } catch (TargetException e) {
                return; // skip
            }
        }

        public void verifyMock() {
            mockTarget.verify();
        }
    }

    public MockTestActions createActions() {
        return new ReturnMockTestActions();
    }

    public void testMethodToMakeTestRunnerNoticeTestCase() {
    }
}
