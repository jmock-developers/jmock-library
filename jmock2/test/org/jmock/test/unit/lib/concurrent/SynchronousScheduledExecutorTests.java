package org.jmock.test.unit.lib.concurrent;


import java.util.concurrent.TimeUnit;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.api.Action;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.lib.concurrent.ScheduleOnExecutorAction;
import org.jmock.lib.concurrent.SynchronousScheduledExecutor;

public class SynchronousScheduledExecutorTests extends MockObjectTestCase {
    SynchronousScheduledExecutor executor = new SynchronousScheduledExecutor();
    
    Runnable commandA = mock(Runnable.class, "commandA");
    Runnable commandB = mock(Runnable.class, "commandB");
    Runnable commandC = mock(Runnable.class, "commandC");
    Runnable commandD = mock(Runnable.class, "commandD");
    
    
    public void testRunsPendingCommands() {
        executor.execute(commandA);
        executor.execute(commandB);
        
        final Sequence executionOrder = sequence("executionOrder");
        
        checking(new Expectations() {{
            one (commandA).run(); inSequence(executionOrder);
            one (commandB).run(); inSequence(executionOrder);
        }});
        
        executor.runPendingCommands();
    }
    
    public void testCanLeaveCommandsSpawnedByExecutedCommandsPendingForLaterExecution() {
        executor.execute(commandA);
        executor.execute(commandB);
        
        final Sequence executionOrder = sequence("executionOrder");
        
        checking(new Expectations() {{
            one (commandA).run(); inSequence(executionOrder); will(schedule(commandC));
            one (commandB).run(); inSequence(executionOrder); will(schedule(commandD));
            never (commandC).run();
            never (commandD).run();
        }

        });
        
        executor.runPendingCommands();
    }
    
    public void testCanRunCommandsSpawnedByExecutedCommandsUntilNoCommandsArePending() {
        executor.execute(commandA);
        executor.execute(commandB);
        
        final Sequence executionOrder = sequence("executionOrder");
        
        checking(new Expectations() {{
            one (commandA).run(); inSequence(executionOrder); will(schedule(commandC));
            one (commandB).run(); inSequence(executionOrder); will(schedule(commandD));
            one (commandC).run(); inSequence(executionOrder);
            one (commandD).run(); inSequence(executionOrder);
        }});
        
        executor.runUntilIdle();
    }
    
    public void testCanScheduleCommandsToBeExecutedAfterADelay() {
        executor.schedule(commandA, 10, TimeUnit.SECONDS);
        
        executor.tick(9, TimeUnit.SECONDS);
        
        checking(new Expectations() {{
            one (commandA).run();
        }});
        
        executor.tick(1, TimeUnit.SECONDS);
    }
    
    public void testTickingTimeForwardRunsAllCommandsScheduledDuringThatTimePeriod() {
        executor.schedule(commandA, 1, TimeUnit.MILLISECONDS);
        executor.schedule(commandB, 2, TimeUnit.MILLISECONDS);
        
        checking(new Expectations() {{
            one (commandA).run();
            one (commandB).run();
        }});
        
        executor.tick(3, TimeUnit.MILLISECONDS);
    }
    
    public void testTickingTimeForwardRunsCommandsExecutedByScheduledCommands() {
        executor.schedule(commandA, 1, TimeUnit.MILLISECONDS);
        executor.schedule(commandD, 2, TimeUnit.MILLISECONDS);
        
        checking(new Expectations() {{
            one (commandA).run(); will(schedule(commandB));
            one (commandB).run(); will(schedule(commandC));
            one (commandC).run();
            one (commandD).run();
        }});
        
        executor.tick(3, TimeUnit.MILLISECONDS);
    }
    
    private Action schedule(final Runnable command) {
        return ScheduleOnExecutorAction.schedule(executor, command);
    }
}
