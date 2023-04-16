package com.kuet.kothabarta.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    String uid, userName, email, password, profileImage, status;
    HashMap<String, String> lastMessage;

    public User(){

    }

    public User(String userName, String email,String password, String profileImage) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
    }

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public User(String uid, String userName, String email, String password, String profileImage, String status, HashMap<String, String> lastMessage) {
        this.uid = uid;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.status = status;
        this.lastMessage = lastMessage;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String , Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("email", email);
        result.put("lastMessage", lastMessage);

        return result;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HashMap<String, String> getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(HashMap<String, String> lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
