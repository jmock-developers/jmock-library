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
        private TargetType targetType = ((TargetType) mockTarget.proxy());

        public void expectNoParams() {
            mockTarget.expect("noParams");
        }

        public void expectOneParam() {
            mockTarget.expect("oneParam", "one");
        }

        public void expectTwoParams() {
            mockTarget.expect("twoParams", C.eq("one", "two"));
        }

        public void expectNotNoParams() {
            mockTarget.expect("notNoParams");
        }

        public void callNoParams() {
            targetType.noParams();
        }

        public void callOneParam() {
            targetType.oneParam("one");
        }

        public void callTwoParams() {
            targetType.twoParams("one", "two");
        }

        public void callIncorrectSecondParameter() {
            targetType.twoParams("one", "not two");
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
