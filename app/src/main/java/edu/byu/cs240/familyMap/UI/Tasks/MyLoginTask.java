package edu.byu.cs240.familyMap.UI.Tasks;

import android.os.AsyncTask;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.Data.Request.LoginRequest;
import edu.byu.cs240.familyMap.Server.ServerProxy;
import shared.*;

public class MyLoginTask extends AsyncTask<LoginRequest, RegisterLoginResult, RegisterLoginResult> implements DataSetterTask.taskData {
    private final taskLogin context;
    private final String host;
    private final String ip;


    public interface taskLogin {
        void onExecuteComplete(String message);
    }

    public MyLoginTask(String server, String ip, taskLogin login){
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
            DataSetterTask dataSetterTask = new DataSetterTask(host, ip, this);
            dataSetterTask.execute(result.getAuthToken());
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

