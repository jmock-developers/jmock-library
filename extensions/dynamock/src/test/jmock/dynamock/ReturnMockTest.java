/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamock;

import org.jmock.dynamock.C;
import org.jmock.dynamock.Mock;


public class ReturnMockTest extends AbstractMockTest {
    public interface TargetType {
        Object noParams();

        Object oneParam(Object aParam);

        Object twoParams(Object param1, Object param2);
    }

    public class ThrowableMockTestActions implements MockTestActions {
        private Mock mockTarget = new Mock(TargetType.class);
        private TargetType targetProxy = ((TargetType) mockTarget.proxy());

        public void stubNoParams() {
        	mockTarget.stubAndReturn( "noParams", C.NO_ARGS, "result" );
        }
        
        public void expectNoParams() {
            mockTarget.expectAndReturn("noParams", C.NO_ARGS, "result");
        }

        public void stubOneParam() {
        	mockTarget.stubAndReturn( "oneParam", C.args(C.eq("one")), "result" );
        }
        
        public void expectOneParam() {
            mockTarget.expectAndReturn("oneParam", C.args(C.eq("one")), "result");
        }
        
        public void stubTwoParams() {
        	mockTarget.stubAndReturn( "twoParams", C.args(C.eq("one"), C.eq("two")), "result" );
        }
        
        public void expectTwoParams() {
            mockTarget.expectAndReturn("twoParams", C.args(C.eq("one"), C.eq("two")), "result" );
        }

        public void expectNotNoParams() {
            mockTarget.expectAndReturn("notNoParams", C.NO_ARGS, "result");
        }

        public void callNoParams() {
            assertEquals("Should be no params result", "result", targetProxy.noParams());
        }

        public void callOneParam() {
            assertEquals("Should be one params result", "result", targetProxy.oneParam("one"));
        }

        public void callTwoParams() {
            assertEquals("Should be two params result", "result", targetProxy.twoParams("one", "two"));
        }

        public void callIncorrectSecondParameter() {
            targetProxy.twoParams("one", "not two");
        }

        public void verifyMock() {
            mockTarget.verify();
        }
    }

    public MockTestActions createActions() {
        return new ThrowableMockTestActions();
    }

    public void testMethodToMakeTestRunnerNoticeTestCase() {
         // hack needed for JUnit
    }
}
