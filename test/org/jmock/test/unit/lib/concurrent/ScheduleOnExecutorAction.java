package org.jmock.test.unit.lib.concurrent;

import java.util.concurrent.Executor;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class ScheduleOnExecutorAction implements Action {
    private final Executor executor;
    private final Runnable command;
    
    public ScheduleOnExecutorAction(Executor executor, Runnable command) {
        this.command = command;
        this.executor = executor;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        executor.execute(command);
        return null;
    }
    
    public void describeTo(Description description) {
        description.appendText("execute ")
                   .appendValue(command)
                   .appendText(" on ")
                   .appendValue(executor);
        
    }

    public static Action schedule(final Executor executor, final Runnable command) {
        return new ScheduleOnExecutorAction(executor, command);
    }
}