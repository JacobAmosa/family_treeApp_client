package edu.byu.cs240.familyMap.UI.Tasks;

import android.os.AsyncTask;

import edu.byu.cs240.familyMap.Data.Request.RegisterRequest;
import edu.byu.cs240.familyMap.Server.ServerProxy;
import shared.*;

/** RegisterTask
 * The RegisterTask extends AsyncTask and is used to check validity of a register Request,
 * and then pulls information from server using a DataTask
 */
public class RegisterTask extends AsyncTask<RegisterRequest, RegisterLoginResult, RegisterLoginResult> implements DataTask.DataContext {

    private String serverHost;
    private String ipAddress;
    private RegisterContext context;

    ////////// Interface ///////////
    public interface RegisterContext {
        void onExecuteComplete(String message);
    }

    // ========================== Constructor ========================================
    public RegisterTask(String server, String ip, RegisterContext c)
    {
        serverHost = server;
        ipAddress = ip;
        context = c;
    }

    //--****************-- Do In Background --***************--
    @Override
    protected RegisterLoginResult doInBackground(RegisterRequest... registerRequests)
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        RegisterLoginResult regResult = serverProxy.register(serverHost, ipAddress, registerRequests[0]);
        return regResult;
    }

    //--****************-- On Post Execute --***************--
    @Override
    protected void onPostExecute(RegisterLoginResult registerResult)
    {
        if (registerResult.isSuccess()){
            DataTask dataTask = new DataTask(serverHost, ipAddress, this);
            dataTask.execute(registerResult.getAuthToken());
        }
        else {
            context.onExecuteComplete(registerResult.getMessage());
        }
    }


    //--****************-- Completion from DataTask --***************--
    @Override
    public void onExecuteCompleteData(String message)
    {
        context.onExecuteComplete(message);
    }
}
