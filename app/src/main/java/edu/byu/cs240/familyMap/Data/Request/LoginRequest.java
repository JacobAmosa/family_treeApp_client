package edu.byu.cs240.familyMap.Data.Request;

public class LoginRequest {

    private String username = null;
    private String password = null;

    public LoginRequest() {}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
