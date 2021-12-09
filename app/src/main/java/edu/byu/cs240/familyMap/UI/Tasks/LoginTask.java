package edu.byu.cs240.familyMap.UI.Tasks;

import android.os.AsyncTask;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.Data.Request.LoginRequest;
import edu.byu.cs240.familyMap.Server.ServerProxy;
import shared.*;

public class LoginTask extends AsyncTask<LoginRequest, RegisterLoginResult, RegisterLoginResult> implements DataTask.taskData {
    private final taskLogin context;
    private final String host;
    private final String ip;


    public interface taskLogin {
        void onExecuteComplete(String message);
    }

    public LoginTask(String server, String ip, taskLogin login){
        this.context = login;
        this.host = server;
        this.ip = ip;
    }

    @Override
    public void onExecuteCompleteData(String note){ context.onExecuteComplete(note);}

    @Override
    protected void onPostExecute(RegisterLoginResult result){
        DataCache dataCache = DataCache.getInstance();
        if (result.isSuccess()){
            dataCache.setIp(ip);
            dataCache.setHost(host);
            dataCache.setAuthToken(result.getAuthToken());
            DataTask dataTask = new DataTask(host, ip, this);
            dataTask.execute(result.getAuthToken());
        }else {
            context.onExecuteComplete(result.getMessage());
        }
    }

    @Override
    protected RegisterLoginResult doInBackground(LoginRequest... requests){
        ServerProxy proxy = ServerProxy.getInstance();
        return proxy.loginUser(host, ip, requests[0]);
    }


}

