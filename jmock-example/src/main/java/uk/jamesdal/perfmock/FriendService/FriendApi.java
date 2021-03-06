package uk.jamesdal.perfmock.FriendService;

import java.util.List;

public interface FriendApi {
    List<Integer> getFriends();

    ProfilePic getProfilePic(Integer id);
}
