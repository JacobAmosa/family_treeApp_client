package edu.byu.cs240.familyMap.Data.Request;

public class LoginRequest {

    private String userName;
    private String password;

    // ========================== Constructors ========================================
    public LoginRequest()
    {
        userName = null;
        password = null;
    }

    public LoginRequest(String loginUserName, String loginPassWord)
    {
        this.userName = loginUserName;
        this.password = loginPassWord;
    }

    //_______________________________ Getters and Setters __________________________________________
    public String getLoginUserName() {
        return userName;
    }

    public void setLoginUserName(String loginUserName) {
        this.userName = loginUserName;
    }

    public String getLoginPassWord() {
        return password;
    }

    public void setLoginPassWord(String loginPassWord) {
        this.password = loginPassWord;
    }
}
