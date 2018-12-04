package com.inti.student.randomfoodchoice;

/**
 * Created by C on 11/20/2018.
 */

public class Input {
    private String fname;
    private String lname;
    private String gender;
    private String age;
    private String email;
    private String username;
    private String pass;

    public Input(){

    }

    public Input(String fname, String lname, String gender, String age, String email, String username, String pass) {
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.username = username;
        this.pass = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
