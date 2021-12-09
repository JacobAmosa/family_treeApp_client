package edu.byu.cs240.familyMap.Data.Request;

public class RegisterRequest {
    private String username = null;
    private String password = null;
    private String email = null;
    private String fName = null;
    private String lName = null;
    private String gender = null;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String email, String fName, String lName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fName = fName;
        this.lName = lName;
        this.gender = gender;
    }

    public String getLastName() {
        return lName;
    }

    public void setLastName(String name) {
        this.lName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getFirstName() {
        return fName;
    }

    public void setFirstName(String name) {
        this.fName = name;
    }


}
