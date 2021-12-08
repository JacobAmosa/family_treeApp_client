package edu.byu.cs240.familyMap.Server;
import java.net.*;
import java.io.*;
import com.google.gson.*;

import edu.byu.cs240.familyMap.Data.Request.LoginRequest;
import edu.byu.cs240.familyMap.Data.Request.RegisterRequest;
import shared.EventResult;
import shared.PersonResult;
import shared.RegisterLoginResult;

public class ServerProxy {

    private static ServerProxy serverProxy;

    // ========================== Singleton Constructor ========================================
    public static ServerProxy initialize()
    {
        if (serverProxy == null){
            serverProxy = new ServerProxy();
        }
        return serverProxy;
    }


    //____________________________________ Login _________________________________
    public RegisterLoginResult login(String serverHost, String serverPort, LoginRequest loginRequest)
    {
        String reqData = "{\"username\":\"" + loginRequest.getLoginUserName() + "\", \"password\":\"" + loginRequest.getLoginPassWord() + "\"}";
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Information successfully retrieved");
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                RegisterLoginResult loginResult = gson.fromJson(respData, RegisterLoginResult.class);
                return loginResult;
            }
            else {
                return new RegisterLoginResult(http.getResponseMessage(),false);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new RegisterLoginResult("Error with Login", false);
        }
    }

    //____________________________________ Register _________________________________
    public RegisterLoginResult register(String serverHost, String serverPort, RegisterRequest regReq)
    {
        String reqData = "{\"username\":\"" + regReq.getUserNameID() + "\", \"password\":\"" + regReq.getUserPassword() +
            "\", \"email\":\"" + regReq.getUserEmail() + "\", \"firstName\":\"" + regReq.getUserFirstName() +
            "\", \"lastName\":\"" + regReq.getUserLastName() + "\", \"gender\":\"" + regReq.getUserGender() + "\"}";
        String resData = null;
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");

            http.setDoOutput(true);

            http.connect();

            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Information successfully retrieved");
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                RegisterLoginResult regResult = gson.fromJson(respData, RegisterLoginResult.class);
                return regResult;
            }
            else {
                return new RegisterLoginResult(http.getResponseMessage(), false);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new RegisterLoginResult("Error with Registering User", false);
        }
    }

    //____________________________________ Get all People _________________________________
    public PersonResult getAllPeople(String serverHost, String serverPort, String authToken)
    {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                PersonResult allPersonResults = gson.fromJson(respData, PersonResult.class);
                return allPersonResults;
            }
            else {
                return new PersonResult(false,http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new PersonResult(false, "Error with retrieving all people");
        }
    }

    //____________________________________ Get all Events _________________________________
    public EventResult getAllEvents(String serverHost, String serverPort, String authToken)
    {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                EventResult allEventResults = gson.fromJson(respData, EventResult.class);
                return allEventResults;
            }
            else {
                return new EventResult(false, http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new EventResult(false, "Error with retrieving all events");
        }
    }

    //--****************-- InputStream to String --***************--
    private static String readString(InputStream is) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    //--****************-- Write a String from an OutputStream --***************--
    private static void writeString(String str, OutputStream os) throws IOException
    {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}