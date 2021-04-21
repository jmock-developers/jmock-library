package uk.jamesdal.perfmock.FriendServiceThreads;

import uk.jamesdal.perfmock.perf.concurrent.PerfThread;
import uk.jamesdal.perfmock.perf.concurrent.PerfThreadFactory;

import java.util.ArrayList;
import java.util.List;

class FriendService {

    private final FriendApi api;
    private final PerfThreadFactory threadFactory;

    public FriendService(FriendApi api, PerfThreadFactory threadFactory) {
        this.api = api;
        this.threadFactory = threadFactory;
    }


    public List<ProfilePic> getFriendsProfilePictures() throws InterruptedException {
        List<Integer> friendIds = api.getFriends();

        List<PerfThread> threads = new ArrayList<>();
        List<GetProfilePic> runnables = new ArrayList<>();

        for (Integer id : friendIds) {
            GetProfilePic runnable = new GetProfilePic(id);
            PerfThread thread = (PerfThread) threadFactory.newThread(runnable);
            thread.start();
            threads.add(thread);
            runnables.add(runnable);
        }

        List<ProfilePic> results = new ArrayList<>();

        for (int i = 0; i < threads.size(); i++) {
            PerfThread thread = threads.get(i);
            GetProfilePic runnable = runnables.get(i);
            thread.perfJoin();
            results.add(runnable.getResult());
        }

        return results;
    }

    private class GetProfilePic implements Runnable {

        private final int id;

        private ProfilePic result;

        private GetProfilePic(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            result = api.getProfilePic(id);
        }

        public ProfilePic getResult() {
            return result;
        }
    }
}
