package org.jmock.lib.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * An {@link Executor} that executes commands on the thread that calls any of
 * its run methods.
 *  
 * @author nat
 */
public class SynchronousExecutor implements Executor {
    private List<Runnable> commands= new ArrayList<Runnable>();
    
    public void execute(Runnable command) {
        commands.add(command);
    }

    /**
     * Runs all commands that are currently pending.  If those commands also schedule commands for
     * execution, the scheduled commands will <em>not</em> be executed until the next call to
     * {@link #runPendingCommands()} or {@link #runUntilIdle()}.
     */
    public void runPendingCommands() {
        List<Runnable> commandsToRun = commands;
        commands = new ArrayList<Runnable>();
        
        for (Runnable command : commandsToRun) {
            command.run();
        }
    }
    
    /**
     * Runs scheduled commands until there are no commands pending execution.
     */
    public void runUntilIdle() {
        while (!commands.isEmpty()) {
            runPendingCommands();
        }
    }
}
