package com.oneside.model.event;

/**
 * Created by MVEN on 16/6/2.
 */
public class FollowUserEvent extends WebBaseEvent {
    private boolean followState;

    private int userID;

    public FollowUserEvent(boolean bResult) {
        super(bResult);
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setFollowState(boolean followState) {
        this.followState = followState;
    }

    public boolean isFollowed() {
        return followState;
    }
}
