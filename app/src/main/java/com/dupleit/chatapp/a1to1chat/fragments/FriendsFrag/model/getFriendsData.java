package com.dupleit.chatapp.a1to1chat.fragments.FriendsFrag.model;

public class getFriendsData {

    private String name;
    private String image;
    private String status;
    private String online;
    private String uid;
    private Integer lastSeen;

    public getFriendsData() {

    }
    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public String getOnline() {
        return online;
    }

    public String getUid() {
        return uid;
    }

    public getFriendsData(String name, String image, String status, String online, String uid,Integer lastSeen) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.online = online;
        this.uid = uid;
        this.lastSeen = lastSeen;
    }
}
