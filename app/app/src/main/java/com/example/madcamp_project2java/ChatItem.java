package com.example.madcamp_project2java;

public class ChatItem {
    private String name;
    private String content;
    private String sendTime;
    private int viewType;    // 0일 시 왼쪽(상대가 보낸 메세지), 1일 시 중앙(~가 입장하셨습니다), 2일 시 오른쪽(내가 보낸 메세지)
    private String profile;
    String profileDir;

    public ChatItem(String name, String profile, String content, String sendTime, int viewType) {
        this.name = name;
        this.profile = profile;
        this.content = content;
        this.sendTime = sendTime;
        this.viewType = viewType;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getProfile() {
        profileDir = "@drawable/".concat(profile).concat("_profile");
        return profileDir;
    }

    public void setProfile(String profile) { this.profile = profile;}

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getSendTime() { return sendTime; }

    public void setSendTime(String sendTime) { this.sendTime = sendTime; }

    public int getViewType() { return viewType; }

    public void setViewType(int viewType) { this.viewType = viewType; }
}