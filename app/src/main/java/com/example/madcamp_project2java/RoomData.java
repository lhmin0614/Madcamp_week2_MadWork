package com.example.madcamp_project2java;

public class RoomData {
    private String username;
    private String roomNumber;
    private String userID;

    public RoomData(String username, String roomNumber, String userID) {
        this.username = username;
        this.roomNumber = roomNumber;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getUserID(){
        return userID;
    }

    public void setUserID(String UserID){
        this.userID = UserID;
    }
}