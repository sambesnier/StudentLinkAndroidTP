package com.example.sambesnier.studentlink.models;

/**
 * Created by Administrateur on 31/08/2017.
 */

public class Vote {

    private String question;
    private String username;

    public Vote(String username, String question) {
        this.question = question;
        this.username = username;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
