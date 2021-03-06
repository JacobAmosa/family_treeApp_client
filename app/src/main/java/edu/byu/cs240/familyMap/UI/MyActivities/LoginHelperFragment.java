package edu.byu.cs240.familyMap.UI.MyActivities;
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
import edu.byu.cs240.familyMap.UI.MyTasks.MyLoginTask;
import edu.byu.cs240.familyMap.UI.MyTasks.MyRegisterTask;

public class LoginHelperFragment extends Fragment implements MyLoginTask.taskLogin, MyRegisterTask.taskRegister {

    private LoginFragmentListener loginFragmentListener;
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
        ip = v.findViewById(R.id.port);
        host = v.findViewById(R.id.server);
        username = v.findViewById(R.id.username);
        password = v.findViewById(R.id.password);
        fName = v.findViewById(R.id.firstname);
        lName = v.findViewById(R.id.lastname);
        email = v.findViewById(R.id.email);

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
        Button boy = v.findViewById(R.id.male);
        boy.setOnClickListener(v1 -> {
            regReq.setGender("m");
            enabler();
        });

        Button girl = v.findViewById(R.id.female);
        girl.setOnClickListener(v12 -> {
            regReq.setGender("f");
            enabler();
        });
    }

    public void setRegisterAndLogin(View v){
        loginButt = v.findViewById(R.id.myLogin);
        registerButt = v.findViewById(R.id.myRegister);
        enabler();

        loginButt.setOnClickListener(v1 -> {
            logReq.setPassword(password.getText().toString());
            logReq.setUsername(username.getText().toString());
            MyLoginTask myLoginTask = new MyLoginTask(host.getText().toString(),
                    ip.getText().toString(),
                    LoginHelperFragment.this);
            myLoginTask.execute(logReq);
        });
        registerButt.setOnClickListener(v12 -> {
            regReq.setLastName(lName.getText().toString());
            regReq.setUsername(username.getText().toString());
            regReq.setEmail(email.getText().toString());
            regReq.setPassword(password.getText().toString());
            regReq.setFirstName(fName.getText().toString());
            MyRegisterTask regTask = new MyRegisterTask(host.getText().toString(),
                    ip.getText().toString(),
                    LoginHelperFragment.this);
            regTask.execute(regReq);
        });
    }

    @Override
    public void onExecuteComplete(String note) {
        Toast.makeText(getContext(), note, Toast.LENGTH_SHORT).show();
        loginFragmentListener.logComplete();
    }

    private void enabler() {
        if (TextUtils.isEmpty(host.getText()) ||
                TextUtils.isEmpty(ip.getText()) ||
                TextUtils.isEmpty(username.getText()) ||
                TextUtils.isEmpty(password.getText()) ||
                TextUtils.isEmpty(email.getText()) ||
                TextUtils.isEmpty(fName.getText()) ||
                TextUtils.isEmpty(lName.getText()) ||
                regReq.getGender() == null){
            registerButt.setEnabled(false);
        }else {
            registerButt.setEnabled(true);
        }
        if (TextUtils.isEmpty(host.getText()) ||
                TextUtils.isEmpty(ip.getText()) ||
                TextUtils.isEmpty(username.getText()) ||
                TextUtils.isEmpty(password.getText())){
            loginButt.setEnabled(false);
        }else {
            loginButt.setEnabled(true);
        }
    }

    private class Enabler implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {}
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            enabler();
        }
    }

    public interface LoginFragmentListener {
        void logComplete();
    }

    public void setLoginListener(LoginFragmentListener listener) {
        loginFragmentListener = listener;
    }

}