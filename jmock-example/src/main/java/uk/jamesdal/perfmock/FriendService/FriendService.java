package uk.jamesdal.perfmock.FriendService;

import uk.jamesdal.perfmock.perf.concurrent.PerfThreadFactory;
import uk.jamesdal.perfmock.perf.concurrent.PerfThreadPoolExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FriendService {

    private final FriendApi api;
    private final PerfThreadFactory threadFactory;

    public FriendService(FriendApi api, PerfThreadFactory threadFactory) {
        this.api = api;
        this.threadFactory = threadFactory;
    }

    public List<ProfilePic> getFriendsProfilePictures() {
        List<Integer> friendIds = api.getFriends();

        List<Future<ProfilePic>> futureTasks = new ArrayList<>();

        ExecutorService executorService = PerfThreadPoolExecutor.newFixedThreadPool(3, threadFactory);

        for (Integer id : friendIds) {
            Future<ProfilePic> future = executorService.submit(getProfilePic(id));
            futureTasks.add(future);
        }

        List<ProfilePic> results = new ArrayList<>();

        for (Future<ProfilePic> futureTask : futureTasks) {
            try {
                results.add(futureTask.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    private Callable<ProfilePic> getProfilePic(Integer id) {
        return () -> api.getProfilePic(id);
    }
}
