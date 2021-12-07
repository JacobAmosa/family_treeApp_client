package edu.byu.cs240.familyMap.Server;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import shared.RegisterLoginResult;

//this class sends and receives requests from the server.
public class ServerProxy {

    private final String serverHost;
    private final String serverPort;

    public ServerProxy(String host, String port){
        this.serverHost = host;
        this.serverPort = port;
    }

    public RegisterLoginResult login(String username, String password){
        String reqData = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
        return executePostRequest("login", reqData);
    }

    public RegisterLoginResult register(String username, String password, String email, String firstName,
                                        String lastName, String gender){
        String reqData = "{\"username\":\"" + username + "\", \"password\":\"" + password +
                "\", \"email\":\"" + email + "\", \"firstName\":\"" + firstName +
                "\", \"lastName\":\"" + lastName + "\", \"gender\":\"" + gender + "\"}";
        return executePostRequest("register", reqData);
    }

    public String getPeople(String authToken){
        return executeGetRequest("person", authToken);
    }

    public String getEvents(String authToken){
        return executeGetRequest("event", authToken);
    }

    public String executeGetRequest(String endpoint, String authToken){
        String resData = null;
        try{
            URL url = new URL("http://" + this.serverHost + ":" + this.serverPort + "/" + endpoint);

            //Start constructing HTTP body.
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");

            //Indicates that this request will not contain an HTTP request body.
            http.setDoOutput(false);

            //Specifies that we would like to receive response in JSON format by adding the Accept header to the request.
            http.addRequestProperty("Accept", "application/json");
            http.addRequestProperty("Authorization", authToken);

            //connects to server and sends the HTTP request.
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream resBody = http.getInputStream();
                resData = readString(resBody);
            }else{
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                resData = readString(resBody);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resData;
    }

    public RegisterLoginResult executePostRequest(String endpoint, String reqData){
        String resData = null;

        try{
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/" + endpoint);

            //Start constructing HTTP body.
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");

            //Indicates that this request will not contain an HTTP request body.
            http.setDoOutput(true);

            //connects to server and sends the HTTP request.
            http.connect();

            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK){
                System.out.println("Information successfully retrieved");
                InputStream resBody = http.getInputStream();
                resData = readString(resBody);
            }else{
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                resData = readString(resBody);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.fromJson(resData, RegisterLoginResult.class);
    }

    //readString allows you to read a string from an inputStream.
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while((len = sr.read(buf)) > 0){
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    //writeString writes a string to an OutputStream
    private void writeString (String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
