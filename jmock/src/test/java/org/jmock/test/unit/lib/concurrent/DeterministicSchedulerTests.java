package org.jmock.test.unit.lib.concurrent;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.api.Action;
import org.jmock.lib.concurrent.DeterministicScheduler;
import org.jmock.lib.concurrent.UnsupportedSynchronousOperationException;
import org.jmock.test.unit.internal.MockObjectTestCase;

public class DeterministicSchedulerTests extends MockObjectTestCase {
    DeterministicScheduler scheduler = new DeterministicScheduler();
    
    Runnable commandA = mock(Runnable.class, "commandA");
    Runnable commandB = mock(Runnable.class, "commandB");
    Runnable commandC = mock(Runnable.class, "commandC");
    Runnable commandD = mock(Runnable.class, "commandD");
    
    @SuppressWarnings("unchecked")
    Callable<String> callableA = mock(Callable.class, "callableA");
    
    public void testRunsPendingCommandsUntilIdle() {
        scheduler.execute(commandA);
        scheduler.execute(commandB);
        
        final Sequence executionOrder = sequence("executionOrder");
        
        checking(new Expectations() {{
            oneOf (commandA).run(); inSequence(executionOrder);
            oneOf (commandB).run(); inSequence(executionOrder);
        }});
        
        assertFalse(scheduler.isIdle());
        
        scheduler.runUntilIdle();
        
        assertTrue(scheduler.isIdle());
    }
    
    public void testCanRunCommandsSpawnedByExecutedCommandsUntilIdle() {
        scheduler.execute(commandA);
        scheduler.execute(commandB);
        
        final Sequence executionOrder = sequence("executionOrder");
        
        checking(new Expectations() {{
            oneOf (commandA).run(); inSequence(executionOrder); will(schedule(commandC));
            oneOf (commandB).run(); inSequence(executionOrder); will(schedule(commandD));
            oneOf (commandC).run(); inSequence(executionOrder);
            oneOf (commandD).run(); inSequence(executionOrder);
        }});
        
        scheduler.runUntilIdle();
    }
    
    public void testCanScheduleCommandAndReturnFuture() throws InterruptedException, ExecutionException {
        Future<?> future = scheduler.submit(commandA);
        
        checking(new Expectations() {{
            oneOf (commandA).run();
        }});
        
        assertTrue("future should not be done before running the task", !future.isDone());
        
        scheduler.runUntilIdle();
        
        assertTrue("future should be done after running the task", future.isDone());
        assertNull("result of future should be null", future.get());
    }
    
    public void testCanScheduleCommandAndResultAndReturnFuture() throws InterruptedException, ExecutionException {
        Future<String> future = scheduler.submit(commandA, "result1");
        
        checking(new Expectations() {{
            oneOf (commandA).run();
        }});
       
        scheduler.runUntilIdle();
        
        assertThat(future.get(), equalTo("result1"));
    }

    public void testCanScheduleCallableAndReturnFuture() throws Exception {
        Future<String> future = scheduler.submit(callableA);
        
        checking(new Expectations() {{
            oneOf (callableA).call(); will(returnValue("result2"));
        }});
        
        scheduler.runUntilIdle();
        
        assertThat(future.get(), equalTo("result2"));
    }

    public void testScheduledCallablesCanReturnNull() throws Exception {
        checking(new Expectations() {{
            oneOf (callableA).call(); will(returnValue(null));
        }});
        
        Future<String> future = scheduler.submit(callableA);
        
        scheduler.runUntilIdle();
        
        assertNull(future.get());
    }

    public void testSimpleGetDelay() throws Exception {
        ScheduledFuture<?> task1 = scheduler.schedule(commandA, 10, TimeUnit.SECONDS);
        scheduler.tick(1, TimeUnit.SECONDS);
        assertEquals(9, task1.getDelay(TimeUnit.SECONDS));
    }

    public void testGetDelayWithManyScheduledTasks() throws Exception {
        ScheduledFuture<?> task1 = scheduler.schedule(commandA, 10, TimeUnit.SECONDS);
        ScheduledFuture<?> task2 = scheduler.schedule(commandA, 20, TimeUnit.SECONDS);
        ScheduledFuture<?> task3 = scheduler.schedule(commandA, 15, TimeUnit.SECONDS);

        scheduler.tick(5, TimeUnit.SECONDS);

        assertEquals(5, task1.getDelay(TimeUnit.SECONDS));
        assertEquals(15, task2.getDelay(TimeUnit.SECONDS));
        assertEquals(10, task3.getDelay(TimeUnit.SECONDS));
    }

    public void testGetDelayOnPassedTasks() throws Exception {
        final Throwable thrown = new IllegalStateException();

        ScheduledFuture<?> task1 = scheduler.schedule(commandA, 1, TimeUnit.MILLISECONDS);

        checking(new Expectations() {{
            oneOf (commandA).run(); will(throwException(thrown));
        }});

        scheduler.tick(2, TimeUnit.MILLISECONDS);

        assertEquals(-1, task1.getDelay(TimeUnit.MILLISECONDS));
    }
    
    public class ExampleException extends Exception {}
    
    public void testExceptionThrownByScheduledCallablesIsThrownFromFuture() throws Exception {
        final Throwable thrown = new ExampleException();
        
        checking(new Expectations() {{
            oneOf (callableA).call(); will(throwException(thrown));
        }});
        
        Future<String> future = scheduler.submit(callableA);
        
        scheduler.runUntilIdle();
        
        try {
            future.get();
            fail("should have thrown ExecutionException");
        }
        catch (ExecutionException expected) {
            assertThat(expected.getCause(), sameInstance(thrown));
        }
    }

    public void testCanScheduleCommandsToBeExecutedAfterADelay() {
        scheduler.schedule(commandA, 10, TimeUnit.SECONDS);
        
        scheduler.tick(9, TimeUnit.SECONDS);
        
        checking(new Expectations() {{
            oneOf (commandA).run();
        }});
        
        scheduler.tick(1, TimeUnit.SECONDS);
    }
    
    public void testTickingTimeForwardRunsAllCommandsScheduledDuringThatTimePeriod() {
        scheduler.schedule(commandA, 1, TimeUnit.MILLISECONDS);
        scheduler.schedule(commandB, 2, TimeUnit.MILLISECONDS);
        
        checking(new Expectations() {{
            oneOf (commandA).run();
            oneOf (commandB).run();
        }});
        
        scheduler.tick(3, TimeUnit.MILLISECONDS);
    }
    
    public void testTickingTimeForwardRunsCommandsExecutedByScheduledCommands() {
        scheduler.schedule(commandA, 1, TimeUnit.MILLISECONDS);
        scheduler.schedule(commandD, 2, TimeUnit.MILLISECONDS);
        
        checking(new Expectations() {{
            oneOf (commandA).run(); will(schedule(commandB));
            oneOf (commandB).run(); will(schedule(commandC));
            oneOf (commandC).run();
            oneOf (commandD).run();
        }});
        
        scheduler.tick(3, TimeUnit.MILLISECONDS);
    }
    
    public void testCanExecuteCommandsThatRepeatWithFixedDelay() {
        scheduler.scheduleWithFixedDelay(commandA, 2L, 3L, TimeUnit.SECONDS);
        
        checking(new Expectations() {{
            exactly(3).of(commandA).run();
        }});
        
        scheduler.tick(8L, TimeUnit.SECONDS);
    }

    public void testCanExecuteCommandsThatRepeatAtFixedRateButAssumesThatCommandsTakeNoTimeToExecute() {
        scheduler.scheduleAtFixedRate(commandA, 2L, 3L, TimeUnit.SECONDS);
        
        checking(new Expectations() {{
            exactly(3).of(commandA).run();
        }});
        
        scheduler.tick(8L, TimeUnit.SECONDS);
    }
    
    public void testCanCancelScheduledCommands() {
        final boolean dontCare = true;
        ScheduledFuture<?> future = scheduler.schedule(commandA, 1, TimeUnit.SECONDS);
        
        assertFalse(future.isCancelled());
        future.cancel(dontCare);
        assertTrue(future.isCancelled());
        
        checking(new Expectations() {{
            never (commandA);
        }});
        
        scheduler.tick(2, TimeUnit.SECONDS);
    }

    public void testCancellingARunningCommandStopsItFromRunningAgain() {
        DeterministicScheduler deterministicScheduler = new DeterministicScheduler();
        ObjectThatCancelsARepeatingTask objectThatCancelsARepeatingTask = new ObjectThatCancelsARepeatingTask(deterministicScheduler);
        objectThatCancelsARepeatingTask.scheduleATaskThatWillCancelItself(1, TimeUnit.SECONDS);
        deterministicScheduler.tick(2,TimeUnit.SECONDS);
        assertThat("cancelling runnable run count", objectThatCancelsARepeatingTask.runCount(), is(1));
    }
    public static class ObjectThatCancelsARepeatingTask {

        private final ScheduledExecutorService scheduler;
        private ScheduledFuture<?> scheduledFuture;
        private final AtomicInteger counter = new AtomicInteger();
        public ObjectThatCancelsARepeatingTask(ScheduledExecutorService scheduler) {
            this.scheduler = scheduler;
        }
        public void scheduleATaskThatWillCancelItself(int interval, TimeUnit unit){
            scheduledFuture = scheduler.scheduleAtFixedRate(
                    new CancellingRunnable(), interval,interval,unit);
        }
        public int runCount(){
            return counter.get();
        }
        private class CancellingRunnable implements Runnable{
            @Override
            public void run() {
                scheduledFuture.cancel(true);
                counter.incrementAndGet();
            }
        }
    }
    static final int TIMEOUT_IGNORED = 1000;
    
    public void testCanScheduleCallablesAndGetTheirResultAfterTheyHaveBeenExecuted() throws Exception {
        checking(new Expectations() {{
            oneOf (callableA).call(); will(returnValue("A"));
        }});
        
        ScheduledFuture<String> future = scheduler.schedule(callableA, 1, TimeUnit.SECONDS);
        
        assertTrue("is not done", !future.isDone());
        
        scheduler.tick(1, TimeUnit.SECONDS);
        
        assertTrue("is done", future.isDone());
        assertThat(future.get(), equalTo("A"));
        assertThat(future.get(TIMEOUT_IGNORED, TimeUnit.SECONDS), equalTo("A"));
    }

    public void testCannotBlockWaitingForFutureResultOfScheduledCallable() throws Exception {
        ScheduledFuture<String> future = scheduler.schedule(callableA, 1, TimeUnit.SECONDS);
        
        try {
            future.get();
            fail("should have thrown UnsupportedSynchronousOperationException");
        }
        catch (UnsupportedSynchronousOperationException expected) {}
        
        try {
            future.get(TIMEOUT_IGNORED, TimeUnit.SECONDS);
            fail("should have thrown UnsupportedSynchronousOperationException");
        }
        catch (UnsupportedSynchronousOperationException expected) {}
    }
    
    private Action schedule(final Runnable command) {
        return ScheduleOnExecutorAction.schedule(scheduler, command);
    }
}
