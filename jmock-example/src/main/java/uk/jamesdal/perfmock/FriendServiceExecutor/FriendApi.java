package uk.jamesdal.perfmock.FriendServiceExecutor;

import java.util.List;

interface FriendApi {
    List<Integer> getFriends();

    ProfilePic getProfilePic(Integer id);
}
