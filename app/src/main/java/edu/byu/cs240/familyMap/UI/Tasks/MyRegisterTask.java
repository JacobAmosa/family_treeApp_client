package edu.byu.cs240.familyMap.UI.Tasks;

import android.os.AsyncTask;

import edu.byu.cs240.familyMap.Data.Request.RegisterRequest;
import edu.byu.cs240.familyMap.Server.ServerProxy;
import shared.*;

public class MyRegisterTask extends AsyncTask<RegisterRequest, RegisterLoginResult, RegisterLoginResult> implements DataSetterTask.taskData {
    private final taskRegister regCon;
    private final String host;
    private final String ip;

    @Override
    protected void onPostExecute(RegisterLoginResult result){
        if (result.isSuccess()){
            DataSetterTask dataSetterTask = new DataSetterTask(host, ip, this);
            dataSetterTask.execute(result.getAuthToken());
        }else {
            regCon.onExecuteComplete(result.getMessage());
        }
    }

    public MyRegisterTask(String server, String ip, taskRegister task){
        this.host = server;
        this.ip = ip;
        this.regCon = task;
    }

    @Override
    protected RegisterLoginResult doInBackground(RegisterRequest... regReq){
        ServerProxy proxy = ServerProxy.getInstance();
        return proxy.registerUser(host, ip, regReq[0]);
    }

    @Override
    public void onExecuteCompleteData(String note)
    {
        regCon.onExecuteComplete(note);
    }

    public interface taskRegister {
        void onExecuteComplete(String note);
    }
}
