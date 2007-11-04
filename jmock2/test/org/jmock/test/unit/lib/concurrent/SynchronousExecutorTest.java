package org.jmock.test.unit.lib.concurrent;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.lib.action.CustomAction;
import org.jmock.lib.concurrent.SynchronousExecutor;


public class SynchronousExecutorTest extends MockObjectTestCase {
    SynchronousExecutor executor = new SynchronousExecutor();
    
    Runnable commandA = mock(Runnable.class, "commandA");
    Runnable commandB = mock(Runnable.class, "commandB");
    Runnable commandC = mock(Runnable.class, "commandC");
    Runnable commandD = mock(Runnable.class, "commandD");
    
    
    public void testRunsScheduledCommands() {
        executor.execute(commandA);
        executor.execute(commandB);
        
        final Sequence executionOrder = sequence("executionOrder");
        
        checking(new Expectations() {{
            one (commandA).run(); inSequence(executionOrder);
            one (commandB).run(); inSequence(executionOrder);
        }});
        
        executor.runPendingCommands();
    }
    
    public void testCanLeaveCommandsScheduledByScheduledCommandsPendingForLaterExecution() {
        executor.execute(commandA);
        executor.execute(commandB);
        
        final Sequence executionOrder = sequence("executionOrder");
        
        checking(new Expectations() {{
            one (commandA).run(); inSequence(executionOrder); will(scheduleCommand(commandC));
            one (commandB).run(); inSequence(executionOrder); will(scheduleCommand(commandD));
            never (commandC).run();
            never (commandD).run();
        }});
        
        executor.runPendingCommands();
    }
    
    public void testCanRunCommandsScheduledByScheduledCommandsUntilNoCommandsArePending() {
        executor.execute(commandA);
        executor.execute(commandB);
        
        final Sequence executionOrder = sequence("executionOrder");
        
        checking(new Expectations() {{
            one (commandA).run(); inSequence(executionOrder); will(scheduleCommand(commandC));
            one (commandB).run(); inSequence(executionOrder); will(scheduleCommand(commandD));
            one (commandC).run(); inSequence(executionOrder);
            one (commandD).run(); inSequence(executionOrder);
        }});
        
        executor.runUntilIdle();
    }
    
    private Action scheduleCommand(final Runnable command) {
        return new CustomAction("execute " + command.toString()) {
            public Object invoke(Invocation invocation) throws Throwable {
                executor.execute(command);
                return null;
            }
        };
    }
}
