package org.jmock.test.unit.lib.concurrent;


import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.api.Action;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.lib.concurrent.ScheduleOnExecutorAction;
import org.jmock.lib.concurrent.SynchronousScheduler;

public class SynchronousSchedulerTests extends MockObjectTestCase {
    SynchronousScheduler scheduler = new SynchronousScheduler();
    
    Runnable commandA = mock(Runnable.class, "commandA");
    Runnable commandB = mock(Runnable.class, "commandB");
    Runnable commandC = mock(Runnable.class, "commandC");
    Runnable commandD = mock(Runnable.class, "commandD");
    
    @SuppressWarnings("unchecked")
    Callable<String> callableA = mock(Callable.class, "callableA");
    
    public void testRunsPendingCommands() {
        scheduler.execute(commandA);
        scheduler.execute(commandB);
        
        final Sequence executionOrder = sequence("executionOrder");
        
        checking(new Expectations() {{
            one (commandA).run(); inSequence(executionOrder);
            one (commandB).run(); inSequence(executionOrder);
        }});
        
        scheduler.runPendingCommands();
    }
    
    public void testCanLeaveCommandsSpawnedByExecutedCommandsPendingForLaterExecution() {
        scheduler.execute(commandA);
        scheduler.execute(commandB);
        
        final Sequence executionOrder = sequence("executionOrder");
        
        checking(new Expectations() {{
            one (commandA).run(); inSequence(executionOrder); will(schedule(commandC));
            one (commandB).run(); inSequence(executionOrder); will(schedule(commandD));
            never (commandC).run();
            never (commandD).run();
        }

        });
        
        scheduler.runPendingCommands();
    }
    
    public void testCanRunCommandsSpawnedByExecutedCommandsUntilNoCommandsArePending() {
        scheduler.execute(commandA);
        scheduler.execute(commandB);
        
        final Sequence executionOrder = sequence("executionOrder");
        
        checking(new Expectations() {{
            one (commandA).run(); inSequence(executionOrder); will(schedule(commandC));
            one (commandB).run(); inSequence(executionOrder); will(schedule(commandD));
            one (commandC).run(); inSequence(executionOrder);
            one (commandD).run(); inSequence(executionOrder);
        }});
        
        scheduler.runUntilIdle();
    }
    
    public void testCanScheduleCommandsToBeExecutedAfterADelay() {
        scheduler.schedule(commandA, 10, TimeUnit.SECONDS);
        
        scheduler.tick(9, TimeUnit.SECONDS);
        
        checking(new Expectations() {{
            one (commandA).run();
        }});
        
        scheduler.tick(1, TimeUnit.SECONDS);
    }
    
    public void testTickingTimeForwardRunsAllCommandsScheduledDuringThatTimePeriod() {
        scheduler.schedule(commandA, 1, TimeUnit.MILLISECONDS);
        scheduler.schedule(commandB, 2, TimeUnit.MILLISECONDS);
        
        checking(new Expectations() {{
            one (commandA).run();
            one (commandB).run();
        }});
        
        scheduler.tick(3, TimeUnit.MILLISECONDS);
    }
    
    public void testTickingTimeForwardRunsCommandsExecutedByScheduledCommands() {
        scheduler.schedule(commandA, 1, TimeUnit.MILLISECONDS);
        scheduler.schedule(commandD, 2, TimeUnit.MILLISECONDS);
        
        checking(new Expectations() {{
            one (commandA).run(); will(schedule(commandB));
            one (commandB).run(); will(schedule(commandC));
            one (commandC).run();
            one (commandD).run();
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
    
    public void testCanScheduleCallablesAndGetTheirResultAfterTheyHaveBeenExecuted() throws Exception {
        final int timeoutIgnored = 1000;
        
        checking(new Expectations() {{
            one (callableA).call(); will(returnValue("A"));
        }});
        
        ScheduledFuture<String> future = scheduler.schedule(callableA, 1, TimeUnit.SECONDS);
        
        scheduler.tick(1, TimeUnit.SECONDS);
        
        assertThat(future.get(), equalTo("A"));
        assertThat(future.get(timeoutIgnored, TimeUnit.SECONDS), equalTo("A"));
    }
    
    private Action schedule(final Runnable command) {
        return ScheduleOnExecutorAction.schedule(scheduler, command);
    }
}
