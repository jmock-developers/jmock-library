/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.functional;

import org.jmock.C;
import org.jmock.Mock;


public class ThrowableMockTest extends AbstractMockTest {
    public class TargetException extends Exception {
    };
    public interface TargetType {
        void noParams() throws TargetException;

        void oneParam(Object aParam) throws TargetException;

        void twoParams(Object param1, Object param2) throws TargetException;
    }

    public class ReturnMockTestActions implements MockTestActions {
        private Mock mockTarget = new Mock(TargetType.class);
        private TargetType targetType = ((TargetType) mockTarget.proxy());

        public void expectNoParams() {
            mockTarget.expectAndThrow("noParams", new TargetException());
        }

        public void expectOneParam() {
            mockTarget.expectAndThrow("oneParam", "one", new TargetException());
        }

        public void expectTwoParams() {
            mockTarget.expectAndThrow("twoParams", C.eq("one", "two"), new TargetException());
        }

        public void expectNotNoParams() {
            mockTarget.expectAndThrow("notNoParams", new TargetException());
        }

        public void callNoParams() {
            try {
                targetType.noParams();
            } catch (TargetException expected) {
                return;
            }
            fail("Should have thrown exception");
        }

        public void callOneParam() {
            try {
                targetType.oneParam("one");
            } catch (TargetException expected) {
                return;
            }
            fail("Should have thrown exception");
        }

        public void callTwoParams() {
            try {
                targetType.twoParams("one", "two");
            } catch (TargetException expected) {
                return;
            }
            fail("Should have thrown exception");
        }

        public void callIncorrectSecondParameter() {
            try {
                targetType.twoParams("one", "not two");
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
