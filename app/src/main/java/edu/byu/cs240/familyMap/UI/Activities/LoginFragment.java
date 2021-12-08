package edu.byu.cs240.familyMap.UI.Activities;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.byu.cs240.familyMap.Data.Request.LoginRequest;
import edu.byu.cs240.familyMap.Data.Request.RegisterRequest;
import edu.byu.cs240.familyMap.R;
import edu.byu.cs240.familyMap.UI.Tasks.LoginTask;
import edu.byu.cs240.familyMap.UI.Tasks.RegisterTask;

public class LoginFragment extends Fragment implements LoginTask.LoginContext, RegisterTask.RegisterContext {

    private LoginListener loginListener;
    private TextWatcher mWatcher;
    private RegisterRequest mRegisterRequest;
    private LoginRequest mLoginRequest;

    private EditText mServerHost;
    private EditText mIPAddress;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;

    private Button mMaleButton;
    private Button mFemaleButton;

    private Button mLoginButton;
    private Button mRegisterButton;

    //________________________ onCreate and other Fragment functions ____________________________________
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mRegisterRequest = new RegisterRequest();
        mLoginRequest = new LoginRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mWatcher = new Enabler();

        mIPAddress = v.findViewById(R.id.portNumberInput);
        mIPAddress.addTextChangedListener(mWatcher);

        mServerHost = v.findViewById(R.id.serverHostInput);
        mServerHost.addTextChangedListener(mWatcher);

        mUsername = v.findViewById(R.id.usernameInput);
        mUsername.addTextChangedListener(mWatcher);

        mPassword = v.findViewById(R.id.passwordInput);
        mPassword.addTextChangedListener(mWatcher);

        mFirstName = v.findViewById(R.id.firstNameInput);
        mFirstName.addTextChangedListener(mWatcher);

        mLastName = v.findViewById(R.id.lastNameInput);
        mLastName.addTextChangedListener(mWatcher);

        mEmail = v.findViewById(R.id.emailInput);
        mEmail.addTextChangedListener(mWatcher);

        mMaleButton = v.findViewById(R.id.maleButton);
        mMaleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mRegisterRequest.setGender("m");
                validate();
            }
        });

        mFemaleButton = v.findViewById(R.id.femaleButton);
        mFemaleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mRegisterRequest.setGender("f");
                validate();
            }
        });

        mLoginButton = v.findViewById(R.id.loginButton);
        mRegisterButton = v.findViewById(R.id.registerButton);
        validate();

        mLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                mLoginRequest.setUsername(mUsername.getText().toString());
                mLoginRequest.setPassword(mPassword.getText().toString());
                LoginTask loginTask = new LoginTask(mServerHost.getText().toString(),
                        mIPAddress.getText().toString(),
                        LoginFragment.this);

                loginTask.execute(mLoginRequest);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                mRegisterRequest.setUsername(mUsername.getText().toString());
                mRegisterRequest.setEmail(mEmail.getText().toString());
                mRegisterRequest.setFirstName(mFirstName.getText().toString());
                mRegisterRequest.setLastName(mLastName.getText().toString());
                mRegisterRequest.setPassword(mPassword.getText().toString());

                RegisterTask regTask = new RegisterTask(mServerHost.getText().toString(),
                        mIPAddress.getText().toString(),
                        LoginFragment.this);

                regTask.execute(mRegisterRequest);
            }
        });


        return v;
    }

    //--****************************-- onExecuteComplete --*******************************--
    @Override
    public void onExecuteComplete(String message)
    {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        loginListener.loginComplete();
    }

    //--*****************-- Check to see if buttons should be enabled --*******************--
    private void validate()
    {
        if (validateRegisterButton()){
            mRegisterButton.setEnabled(false);
        }
        else {
            mRegisterButton.setEnabled(true);
        }

        if (validateLoginButton()){
            mLoginButton.setEnabled(false);
        }
        else {
            mLoginButton.setEnabled(true);
        }

    }

    private boolean validateRegisterButton()
    {
        return TextUtils.isEmpty(mServerHost.getText()) ||
                TextUtils.isEmpty(mIPAddress.getText()) ||
                TextUtils.isEmpty(mUsername.getText()) ||
                TextUtils.isEmpty(mPassword.getText()) ||
                TextUtils.isEmpty(mEmail.getText()) ||
                TextUtils.isEmpty(mFirstName.getText()) ||
                TextUtils.isEmpty(mLastName.getText()) ||
                mRegisterRequest.getGender() == null;
    }

    private boolean validateLoginButton()
    {
        return TextUtils.isEmpty(mServerHost.getText()) ||
                TextUtils.isEmpty(mIPAddress.getText()) ||
                TextUtils.isEmpty(mUsername.getText()) ||
                TextUtils.isEmpty(mPassword.getText());
    }

    ////////// Public Interface for Tasks ////////////
    public interface LoginListener {
        void loginComplete();
    }

    public void setLoginListener(LoginListener logListen)
    {
        loginListener = logListen;
    }

    ////////////// TextWatcher //////////////
    private class Enabler implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validate();
        }

        @Override
        public void afterTextChanged(Editable s) {}

    }
}