package edu.byu.cs240.familyMap.UI.Tasks;

import android.os.AsyncTask;

import edu.byu.cs240.familyMap.Data.Model;
import edu.byu.cs240.familyMap.Data.Request.LoginRequest;
import edu.byu.cs240.familyMap.Server.ServerProxy;
import shared.*;
/** LoginTask
 * The LoginTask extends the AsyncTask and is used to check if the login or register request is valid
 * and then uses a DataTask to extract data
 */
public class LoginTask extends AsyncTask<LoginRequest, RegisterLoginResult, RegisterLoginResult> implements DataTask.DataContext {
    private String serverHost;
    private String ipAddress;
    private LoginContext context;

    ///////// Interface //////////
    public interface LoginContext {
        void onExecuteComplete(String message);
    }

    // ========================== Constructor ========================================
    public LoginTask(String server, String ip, LoginContext c)
    {
        serverHost = server;
        ipAddress = ip;
        context = c;
    }

    //--****************-- Do In Background --***************--
    @Override
    protected RegisterLoginResult doInBackground(LoginRequest... logReqs)
    {
        ServerProxy serverProxy = ServerProxy.getInstance();
        RegisterLoginResult loginResult = serverProxy.loginUser(serverHost, ipAddress, logReqs[0]);
        return loginResult;
    }

    //--****************-- On Post Execute --***************--
    @Override
    protected void onPostExecute(RegisterLoginResult loginResult)
    {
        if (loginResult.isSuccess()){
            Model model = Model.initialize();

            model.setServerHost(serverHost);
            model.setIpAddress(ipAddress);
            model.setAuthToken(loginResult.getAuthToken());

            DataTask dataTask = new DataTask(serverHost, ipAddress, this);
            dataTask.execute(loginResult.getAuthToken());
        }
        else {
            context.onExecuteComplete(loginResult.getMessage());
        }
    }

    //--****************-- Receive Completion from DataTask --***************--
    @Override
    public void onExecuteCompleteData(String message)
    {
        context.onExecuteComplete(message);
    }


}

