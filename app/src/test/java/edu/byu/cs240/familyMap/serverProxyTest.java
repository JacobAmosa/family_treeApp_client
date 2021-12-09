package edu.byu.cs240.familyMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.byu.cs240.familyMap.Data.Request.LoginRequest;
import edu.byu.cs240.familyMap.Data.Request.RegisterRequest;
import edu.byu.cs240.familyMap.Server.ServerProxy;
import shared.EventResult;
import shared.PersonResult;
import shared.RegisterLoginResult;

public class serverProxyTest {

    @Before
    public void setUp() {
        ServerProxy serverProxy = ServerProxy.getInstance();
        RegisterRequest regReq = new RegisterRequest("test","nope","no","jacob","amosa", "m");
        RegisterLoginResult registerResult = serverProxy.registerUser("10.0.2.2", "8080", regReq);
    }

    @Test
    public void loginSucceed(){
        ServerProxy serverProxy = ServerProxy.getInstance();
        LoginRequest loginRequest = new LoginRequest("test", "nope");
        RegisterLoginResult loginResult = serverProxy.loginUser("10.0.2.2", "8080", loginRequest);
        Assert.assertNotNull(loginResult.getUsername());
    }


}