package org.jmock.test.unit.lib.concurrent;

import java.lang.Thread.State;
import java.util.concurrent.TimeUnit;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.lib.concurrent.ExactCommandExecutor;


public class ExactCommandExecutorTest extends MockObjectTestCase {

    private final Runnable runnable1 = mock(Runnable.class, "runnable1");

    private final Runnable runnable2 = mock(Runnable.class, "runnable2");

    private final Runnable runnable3 = mock(Runnable.class, "runnable3");

    public void testNoCommands() {
        ExactCommandExecutor exec = new ExactCommandExecutor(0);
        exec.waitForExpectedTasks();
    }

    public void testRunsPendingCommands() {
        ExactCommandExecutor exec = new ExactCommandExecutor(2);
        exec.execute(runnable1);
        exec.execute(runnable2);
        final Sequence executionOrder = sequence("executionOrder");
        checking(new Expectations() {
            {
                oneOf(runnable1).run();
                inSequence(executionOrder);
                oneOf(runnable2).run();
            }
        });
        exec.waitForExpectedTasks();
    }

    public void testDoesnAcceptMore() {
        ExactCommandExecutor exec = new ExactCommandExecutor(2);
        exec.execute(runnable1);
        exec.execute(runnable2);
        try {
            exec.execute(runnable3);
            fail("Must not accept three runnables");
        }
        catch (IllegalStateException ise) {

        }
    }

    public void testAbortAfterTimeout() {
        final ExactCommandExecutor exec = new ExactCommandExecutor(1);
        exec.waitForExpectedTasks(300, TimeUnit.MILLISECONDS);
    }

    public void testWaitForCommands() throws InterruptedException {
        final ExactCommandExecutor exec = new ExactCommandExecutor(2);
        exec.execute(new MockRunnable());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                exec.waitForExpectedTasks();
            }
        });
        t.start();
        Thread.sleep(300);
        assertEquals(State.TIMED_WAITING, t.getState());
        exec.execute(new MockRunnable());
        t.join(300);
    }

    public void testAbortWithInterrupt() throws InterruptedException {
        final ExactCommandExecutor exec = new ExactCommandExecutor(2);
        exec.execute(new MockRunnable());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                exec.waitForExpectedTasks();
            }
        });
        t.start();
        Thread.sleep(300);
        assertEquals(State.TIMED_WAITING, t.getState());
        t.interrupt(); // now continue
        t.join(300);
    }

    class MockRunnable implements Runnable {

        @Override
        public void run() {

        }
    }

}
