package uk.jamesdal.perfmock.FriendServiceThreads;

import java.util.List;

interface FriendApi {
    List<Integer> getFriends();

    ProfilePic getProfilePic(Integer id);
}
