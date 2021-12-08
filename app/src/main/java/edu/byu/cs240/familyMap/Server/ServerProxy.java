package edu.byu.cs240.familyMap.Server;
import java.net.*;
import com.google.gson.*;
import java.io.*;

import edu.byu.cs240.familyMap.Data.Request.LoginRequest;
import edu.byu.cs240.familyMap.Data.Request.RegisterRequest;
import shared.EventResult;
import shared.PersonResult;
import shared.RegisterLoginResult;

public class ServerProxy {

    private static ServerProxy server;

    public static ServerProxy getInstance() {
        if (server != null){
            return server;
        }
        server = new ServerProxy();
        return server;
    }

    public EventResult getEvents(String host, String port, String authToken) {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + host + ":" + port + "/event");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                EventResult result = gson.fromJson(respData, EventResult.class);
                return result;
            }
            else {
                return new EventResult(false, http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new EventResult(false, "Unable to retrieve events.");
        }
    }

    public PersonResult getPeople(String host, String port, String authToken) {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + host + ":" + port + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                PersonResult result = gson.fromJson(respData, PersonResult.class);
                return result;
            }
            else {
                return new PersonResult(false,http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new PersonResult(false, "Unable to retrieve people");
        }
    }

    public RegisterLoginResult loginUser(String host, String port, LoginRequest logReq) {
        String reqData = "{\"username\":\"" + logReq.getUsername() + "\", \"password\":\"" + logReq.getPassword() + "\"}";
        return executePostRequest(reqData, host,port,"login");
    }

    public RegisterLoginResult registerUser(String host, String port, RegisterRequest regReq) {
        String reqData = "{\"username\":\"" + regReq.getUsername() + "\", \"password\":\"" + regReq.getPassword() +
            "\", \"email\":\"" + regReq.getEmail() + "\", \"firstName\":\"" + regReq.getFirstName() +
            "\", \"lastName\":\"" + regReq.getLastName() + "\", \"gender\":\"" + regReq.getGender() + "\"}";
        return executePostRequest(reqData, host,port,"register");

    }

    public RegisterLoginResult executePostRequest(String data, String host, String port, String endpoint){
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + host + ":" + port + "/user/" + endpoint);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();
            OutputStream reqBody = http.getOutputStream();
            writeString(data, reqBody);
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
            return new RegisterLoginResult("unable to Register/login User", false);
        }
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}