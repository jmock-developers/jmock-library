package uk.jamesdal.perfmock.perf;

import uk.jamesdal.perfmock.perf.concurrent.PerfCallable;
import uk.jamesdal.perfmock.perf.events.*;
import uk.jamesdal.perfmock.perf.postproc.IterResult;
import uk.jamesdal.perfmock.perf.postproc.PerfStatistics;
import uk.jamesdal.perfmock.perf.postproc.ReportGenerator;
import uk.jamesdal.perfmock.perf.postproc.reportgenerators.ConsoleReportGenerator;

import java.util.*;

public class Simulation {
    private HashMap<Long, List<SimEvent>> timeline = new HashMap<>();
    private HashMap<Long, PerfCallable> callableHashMap = new HashMap<>();
    private List<IterResult> results = new ArrayList<>();
    private ReportGenerator reportGenerator = new ConsoleReportGenerator();

    public void reset() {
        timeline = new HashMap<>();
        long id = Thread.currentThread().getId();
        List<SimEvent> history = new ArrayList<>();
        history.add(new BirthEvent(0.0, -1));
        timeline.put(id, history);
    }

    public void save() {
        results.add(new IterResult(0.0, getSimTime()));
    }

    public void play() {
        long curTime = System.currentTimeMillis();
        PlayEvent playEvent = new PlayEvent(getSimTime(), curTime);
        long id = Thread.currentThread().getId();
        SimEvent last = getLastPlayPauseEvent(timeline, id, timeline.get(id).size() - 1);
        if (Objects.isNull(last) || last.getType() != EventTypes.PLAY) {
            addEvent(playEvent);
        }
    }

    public void pause() {
        pause(Thread.currentThread().getId());
    }

    public void pause(long id) {
        PlayEvent playEvent = getLastPlayEvent(timeline, id, timeline.get(id).size() - 1);
        if (Objects.isNull(playEvent)) {
            System.out.println("Could not find matching play event");
            return;
        }

        long curTime = System.currentTimeMillis();
        long diff = curTime - playEvent.getRunTime();

        SimEvent last = getLastPlayPauseEvent(timeline, id, timeline.get(id).size() - 1);
        if (Objects.isNull(last) || last.getType() != EventTypes.PAUSE) {
            addEvent(new PauseEvent(getSimTime(id) + diff), id);
        }
    }

    public void setUpNewThreads(long parent, long child) {
        timeline.put(child, new ArrayList<>());
        addEvent(new ForkEvent(getSimTime(), child));
        addEvent(new BirthEvent(getSimTime(), parent), child);
    }

    public void createJoinEvent(long child) {
        double parentSimTime = getSimTime();
        double childSimTime = getSimTime(child);

        JoinEvent joinEvent = new JoinEvent(Math.max(parentSimTime, childSimTime));
        addEvent(joinEvent);
    }

    public void createTaskJoinEvent(long id, PerfCallable task) {
        double parentSimTime = getSimTime();
        TaskFinishEvent event = partialBackwardsSearch(timeline.get(id), task);
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
        List<SimEvent> history = timeline.get(id);
        return history.get(history.size() - 1).getSimTime();
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
        List<SimEvent> history = timeline.get(id);
        history.add(event);
    }

    private SimEvent getLastPlayPauseEvent(HashMap<Long, List<SimEvent>> timeline, long id, int searchIndex) {
        List<SimEvent> history = timeline.get(id);
        for (int i = searchIndex; i > 0; i--) {
            SimEvent event = history.get(i);
            if (event.getType() == EventTypes.PLAY || event.getType() == EventTypes.PAUSE) {
                return  event;
            }
        }

        SimEvent firstEvent = history.get(0);

        if (firstEvent.getType() != EventTypes.BIRTH) {
            return null;
        }

        long parentId = ((BirthEvent) firstEvent).getParent();
        List<SimEvent> parentHistory = timeline.get(parentId);

        if (Objects.isNull(parentHistory)) {
            return null;
        }

        int parentSearchIndex = -1;
        for (int i = 0; i < parentHistory.size(); i ++) {
            SimEvent event = parentHistory.get(i);
            if (event.getType() == EventTypes.FORK) {
                ForkEvent forkEvent = (ForkEvent) event;
                if (forkEvent.getChild() == id) {
                    parentSearchIndex = i;
                    break;
                }
            }
        }

        return getLastPlayPauseEvent(timeline, parentId, parentSearchIndex);
    }

    private PlayEvent getLastPlayEvent(HashMap<Long, List<SimEvent>> timeline, long id, int searchIndex) {
        List<SimEvent> history = timeline.get(id);
        for (int i = searchIndex; i > 0; i--) {
            SimEvent event = history.get(i);
            if (event.getType() == EventTypes.PLAY) {
                return (PlayEvent) event;
            }
        }

        SimEvent firstEvent = history.get(0);

        if (firstEvent.getType() != EventTypes.BIRTH) {
            return null;
        }

        long parentId = ((BirthEvent) firstEvent).getParent();
        List<SimEvent> parentHistory = timeline.get(parentId);
        if (Objects.isNull(parentHistory)) {
            return null;
        }

        int parentSearchIndex = -1;
        for (int i = 0; i < parentHistory.size(); i ++) {
            SimEvent event = parentHistory.get(i);
            if (event.getType() == EventTypes.FORK) {
                ForkEvent forkEvent = (ForkEvent) event;
                if (forkEvent.getChild() == id) {
                    parentSearchIndex = i;
                    break;
                }
            }
        }

        return getLastPlayEvent(timeline, parentId, parentSearchIndex);
    }

    private SimEvent partialBackwardsSearch(List<SimEvent> history, EventTypes[] types) {
        for (int i = history.size() - 1; i > 0; i--) {
            SimEvent event = history.get(i);
            for (EventTypes type : types) {
                if (event.getType().equals(type)) {
                    return event;
                }
            }
        }

        return null;
    }

    private TaskFinishEvent partialBackwardsSearch(List<SimEvent> history, PerfCallable task) {
        for (int i = history.size() - 1; i > 0; i--) {
            SimEvent event = history.get(i);
            if (event.getType() == EventTypes.TASK_FINISH) {
                TaskFinishEvent finishEvent = (TaskFinishEvent) event;
                if (finishEvent.getTask().equals(task)) {
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
