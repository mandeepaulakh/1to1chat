package com.dupleit.chatapp.a1to1chat.fragments.FriendsFrag.model;

public class getFriendsData {

    private String name;
    private String image;
    private String status;
    private String online;
    private String uid;
    private String thumb_image;

    public getFriendsData(String name, String image, String status, String online, String uid, String thumb_image) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.online = online;
        this.uid = uid;
        this.thumb_image = thumb_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public getFriendsData() {

    }

}
