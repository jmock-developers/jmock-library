package uk.jamesdal.perfmock.perf;

import uk.jamesdal.perfmock.perf.concurrent.PerfCallable;
import uk.jamesdal.perfmock.perf.events.*;
import uk.jamesdal.perfmock.perf.postproc.IterResult;
import uk.jamesdal.perfmock.perf.postproc.PerfStatistics;
import uk.jamesdal.perfmock.perf.postproc.ReportGenerator;
import uk.jamesdal.perfmock.perf.postproc.reportgenerators.ConsoleReportGenerator;

import java.util.*;

public class Simulation {
    private final HashMap<Long, PerfCallable> callableHashMap = new HashMap<>();
    private final List<IterResult> results = new ArrayList<>();

    private List<SimEvent> history = new ArrayList<>();
    private ReportGenerator reportGenerator = new ConsoleReportGenerator();

    // Reset timeline to beginning
    public void reset() {
        history = new ArrayList<>();
    }

    // Save Simulation results
    public void save() {
        results.add(new IterResult(0.0, getSimTime()));
    }

    // Add play event for current thread
    public void play() {
        long curTime = System.currentTimeMillis();
        PlayEvent playEvent = new PlayEvent(getSimTime(), curTime);

        // Check last play/pause event is either pause or null
        long id = Thread.currentThread().getId();
        SimEvent last = getLastPlayPauseEvent(history, id, history.size() - 1);
        if (Objects.isNull(last) || last.getType() == EventTypes.PAUSE) {
            addEvent(playEvent);
        }
    }

    // Add pause event for current thread
    public void pause() {
        pause(Thread.currentThread().getId());
    }

    // Add pause event for given thread
    public void pause(long id) {
        // Find matching play event
        SimEvent last = getLastPlayPauseEvent(history, id, history.size() - 1);

        if (last.getType() != EventTypes.PLAY) {
            System.out.println("Could not find matching play event");
            return;
        }
        PlayEvent playEvent = (PlayEvent) last;

        long curTime = System.currentTimeMillis();
        long diff = curTime - playEvent.getRunTime();

        addEvent(new PauseEvent(getSimTime(id) + diff), id);
    }

    public void setUpNewThreads(long parent, long child) {
        addEvent(new ForkEvent(getSimTime(), child), child);
    }

    public void createJoinEvent(long child) {
        double parentSimTime = getSimTime();
        double childSimTime = getSimTime(child);

        JoinEvent joinEvent = new JoinEvent(Math.max(parentSimTime, childSimTime));
        addEvent(joinEvent);
    }

    public void createTaskJoinEvent(long id, PerfCallable task) {
        double parentSimTime = getSimTime();
        TaskFinishEvent event = callableBackwardsSearch(history, task, id);
        double childSimTime = event.getSimTime();

        TaskJoinEvent taskJoinEvent = new TaskJoinEvent(Math.max(parentSimTime, childSimTime));
        addEvent(taskJoinEvent);

    }

    public void add(double time) {
        addEvent(new ModelEvent(getSimTime() + time));
    }

    public double getSimTime() {
        return getSimTime(Thread.currentThread().getId());
    }

    public double getSimTime(long id) {
        if (history.size() == 0) {
            return 0.0;
        }
        return backwardsSearch(history, null, id).getSimTime();
    }

    public void genReport() {
        reportGenerator.setStats(getStats());
        reportGenerator.generateReport();
    }

    public void setReportGenerator(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    public PerfStatistics getStats() {
        return new PerfStatistics(results);
    }

    public synchronized void addEvent(SimEvent event) {
        addEvent(event, Thread.currentThread().getId());
    }

    public synchronized void addEvent(SimEvent event, long id) {
        event.setThreadId(id);
        history.add(event);
    }

    private SimEvent getLastPlayPauseEvent(List<SimEvent> history, long threadId, int searchIndex) {
        SimEvent event = backwardsSearch(
                history,
                new EventTypes[]{EventTypes.PLAY, EventTypes.PAUSE, EventTypes.FORK},
                threadId,
                searchIndex
        );

        if (event == null) {
            return null;
        }

        // If found play event return
        if (event.getType().equals(EventTypes.PLAY) || event.getType().equals(EventTypes.PAUSE)) {
            return event;
        }

        // Else event is fork
        ForkEvent forkEvent = (ForkEvent) event;

        long parentId = forkEvent.getParent();

        return getLastPlayPauseEvent(this.history, parentId, history.indexOf(forkEvent) - 1);
    }

    // Search a History for event which matches one given in types with thread id
    private SimEvent backwardsSearch(List<SimEvent> history, EventTypes[] types, long threadId, int searchIndex) {
        for (int i = searchIndex; i >= 0; i--) {
            SimEvent event = history.get(i);
            if (Objects.isNull(types)) {
                if (event.getThreadId() == threadId) {
                    return event;
                }

                continue;
            }

            for (EventTypes type : types) {
                if (event.getType().equals(type) && event.getThreadId() == threadId) {
                    return event;
                }
            }
        }

        return null;
    }

    // Search a History for event which matches one given in types with thread id
    private SimEvent backwardsSearch(List<SimEvent> history, EventTypes[] types, long threadId) {
        return backwardsSearch(history, types, threadId, history.size() - 1);
    }

    // Search a History for task finish event which matches callable
    private TaskFinishEvent callableBackwardsSearch(List<SimEvent> history, PerfCallable task, long threadId) {
        for (int i = history.size() - 1; i > 0; i--) {
            SimEvent event = history.get(i);
            if (event.getType() == EventTypes.TASK_FINISH) {
                TaskFinishEvent finishEvent = (TaskFinishEvent) event;
                if (finishEvent.getTask().equals(task) && finishEvent.getThreadId() == threadId) {
                    return finishEvent;
                }
            }
        }

        return null;
    }

    public PerfCallable getLastPerfCallable(long id) {
        return callableHashMap.get(id);
    }

    public void setCallable(PerfCallable callable, long id) {
        callableHashMap.put(id, callable);
    }


}
