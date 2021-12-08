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
    private final RegisterRequest regReq = new RegisterRequest();;
    private final LoginRequest logReq = new LoginRequest();
    private Button registerButt;
    private Button loginButt;
    private EditText fName;
    private EditText lName;
    private EditText host;
    private EditText ip;
    private EditText password;
    private EditText username;
    private EditText email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        TextWatcher watcher = new Enabler();
        ip = v.findViewById(R.id.portNumberInput);
        host = v.findViewById(R.id.serverHostInput);
        username = v.findViewById(R.id.usernameInput);
        password = v.findViewById(R.id.passwordInput);
        fName = v.findViewById(R.id.firstNameInput);
        lName = v.findViewById(R.id.lastNameInput);
        email = v.findViewById(R.id.emailInput);

        ip.addTextChangedListener(watcher);
        host.addTextChangedListener(watcher);
        username.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);
        fName.addTextChangedListener(watcher);
        lName.addTextChangedListener(watcher);
        email.addTextChangedListener(watcher);
        setRadioButtons(v);
        setRegisterAndLogin(v);





        return v;
    }

    public void setRadioButtons(View v){
        Button boy = v.findViewById(R.id.maleButton);
        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regReq.setGender("m");
                validate();
            }
        });

        Button girl = v.findViewById(R.id.femaleButton);
        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regReq.setGender("f");
                validate();
            }
        });
    }

    public void setRegisterAndLogin(View v){
        loginButt = v.findViewById(R.id.loginButton);
        registerButt = v.findViewById(R.id.registerButton);
        validate();

        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logReq.setPassword(password.getText().toString());
                logReq.setUsername(username.getText().toString());
                LoginTask loginTask = new LoginTask(host.getText().toString(),
                        ip.getText().toString(),
                        LoginFragment.this);
                loginTask.execute(logReq);
            }
        });
        registerButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regReq.setLastName(lName.getText().toString());
                regReq.setUsername(username.getText().toString());
                regReq.setEmail(email.getText().toString());
                regReq.setPassword(password.getText().toString());
                regReq.setFirstName(fName.getText().toString());
                RegisterTask regTask = new RegisterTask(host.getText().toString(),
                        ip.getText().toString(),
                        LoginFragment.this);
                regTask.execute(regReq);
            }
        });
    }

    @Override
    public void onExecuteComplete(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        loginListener.loginComplete();
    }

    //--*****************-- Check to see if buttons should be enabled --*******************--
    private void validate()
    {
        if (validateRegisterButton()){
            registerButt.setEnabled(false);
        }
        else {
            registerButt.setEnabled(true);
        }

        if (validateLoginButton()){
            loginButt.setEnabled(false);
        }
        else {
            loginButt.setEnabled(true);
        }

    }

    private boolean validateRegisterButton()
    {
        return TextUtils.isEmpty(host.getText()) ||
                TextUtils.isEmpty(ip.getText()) ||
                TextUtils.isEmpty(username.getText()) ||
                TextUtils.isEmpty(password.getText()) ||
                TextUtils.isEmpty(email.getText()) ||
                TextUtils.isEmpty(fName.getText()) ||
                TextUtils.isEmpty(lName.getText()) ||
                regReq.getGender() == null;
    }

    private boolean validateLoginButton()
    {
        return TextUtils.isEmpty(host.getText()) ||
                TextUtils.isEmpty(ip.getText()) ||
                TextUtils.isEmpty(username.getText()) ||
                TextUtils.isEmpty(password.getText());
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