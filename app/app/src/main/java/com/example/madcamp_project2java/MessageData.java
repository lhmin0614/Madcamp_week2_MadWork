package com.example.madcamp_project2java;

public class MessageData {
    private String type;
    private String from;
    private String to;
    private String content;
    private String sendTime;
    private String userid;
    private String profile;

    public MessageData(String type, String from, String userid, String profile, String to, String content, String sendTime) {
        this.type = type;
        this.from = from;
        this.userid = userid;
        this.profile = profile;
        this.to = to;
        this.content = content;
        this.sendTime = sendTime;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getFrom() { return from; }

    public void setFrom(String from) { this.from = from; }

    public String getUserid(){return userid;}

    public void setUserid(String userid) {this.userid = userid;}

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) { this.profile = profile;}

    public String getTo() { return to; }

    public void setTo(String to) { this.to = to; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getSendTime() { return sendTime; }

    public void setSendTime(String sendTime) { this.sendTime = sendTime; }
}