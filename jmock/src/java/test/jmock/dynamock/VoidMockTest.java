/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamock;

import org.jmock.C;
import org.jmock.dynamock.Mock;


public class VoidMockTest extends AbstractMockTest {
    public interface TargetType {
        void noParams();

        void oneParam(Object aParam);

        void twoParams(Object param1, Object param2);
    }

    public class VoidMockTestActions implements MockTestActions {
        private Mock mockTarget = new Mock(TargetType.class);
        private TargetType targetProxy = ((TargetType) mockTarget.proxy());

        public void stubNoParams() {
        	mockTarget.stubVoid("noParams");
        }

        public void expectNoParams() {
            mockTarget.expect("noParams");
        }

        public void stubOneParam() {
        	mockTarget.match("oneParam", "one");
        }

        public void expectOneParam() {
            mockTarget.expect("oneParam", "one");
        }

        public void stubTwoParams() {
        	mockTarget.stubVoid("twoParams", C.eq("one", "two"));
        }

        public void expectTwoParams() {
            mockTarget.expectVoid("twoParams", C.eq("one", "two"));
        }

        public void expectNotNoParams() {
            mockTarget.expect("notNoParams");
        }

        public void callNoParams() {
            targetProxy.noParams();
        }

        public void callOneParam() {
            targetProxy.oneParam("one");
        }

        public void callTwoParams() {
            targetProxy.twoParams("one", "two");
        }

        public void callIncorrectSecondParameter() {
            targetProxy.twoParams("one", "not two");
        }

        public void verifyMock() {
            mockTarget.verify();
        }
    }

    public MockTestActions createActions() {
        return new VoidMockTestActions();
    }

    public void testMethodToMakeTestRunnerNoticeTestCase() {
    }
}
