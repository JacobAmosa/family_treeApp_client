package edu.byu.cs240.familyMap.UI;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;
import edu.byu.cs240.familyMap.Server.ServerProxy;
import shared.EventModel;
import shared.EventResult;
import shared.PersonModel;
import shared.PersonResult;
import shared.RegisterLoginResult;

public class LoginFragment extends Fragment {

    private Button login;
    private Button register;
    private RadioGroup genders;
    private RadioButton male;
    private RadioButton female;
    private EditText serverHost;
    private EditText serverPort;
    private EditText username;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private DataCache dataCache = new DataCache();
    private static final String SUCCESS = null;

    private void setMemberVariables(View view){
        login = view.findViewById(R.id.loginButton);
        register = view.findViewById(R.id.registerButton);
        genders = view.findViewById(R.id.gender);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        serverHost = view.findViewById(R.id.serverHost);
        serverPort = view.findViewById(R.id.serverPort);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);
    }

    private void addTextEditListener(EditText widget) {
        widget.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                enableButtons();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setMemberVariables(view);
        login.setEnabled(false);
        register.setEnabled(false);
        setLoginHandler();
        setRegisterHandler();
        addTextEditListener(username);
        addTextEditListener(password);
        addTextEditListener(firstName);
        addTextEditListener(lastName);
        addTextEditListener(serverHost);
        addTextEditListener(serverPort);
        addTextEditListener(email);
        genders.setOnCheckedChangeListener((group, checkedId) -> enableButtons());
        return view;
    }

    private void setRegisterHandler(){
        register.setOnClickListener((v) -> {
            String gender = genders.getCheckedRadioButtonId() == male.getId() ? "m" : "f";
            @SuppressLint("HandlerLeak") Handler uiThreadMessageHandler = new Handler(){
                @Override
                public void handleMessage(Message message){
                    Bundle bundle = message.getData();
                    if (bundle.getString(SUCCESS).equals("true")){
                        Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_SHORT).show();
                        dataSetHandler();
                    }else {
                        Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            RegisterTask task = new RegisterTask(uiThreadMessageHandler, serverHost.getText().toString(), serverPort.getText().toString(),
                    username.getText().toString(), password.getText().toString(), email.getText().toString(), firstName.getText().toString(),
                    lastName.getText().toString(), gender);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(task);
        });

    }

    private void setLoginHandler(){
        login.setOnClickListener((v) -> {
            @SuppressLint("HandlerLeak") Handler uiThreadMessageHandler = new Handler(){
                @Override
                public void handleMessage(Message message){
                    Bundle bundle = message.getData();
                    if (bundle.getString(SUCCESS).equals("true")){
                        Toast.makeText(getActivity(), "login successful", Toast.LENGTH_SHORT).show();
                        dataSetHandler();
                    }else {
                        Toast.makeText(getActivity(), "login failed", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            LoginTask task = new LoginTask(uiThreadMessageHandler, serverHost.getText().toString(), serverPort.getText().toString(),
                    username.getText().toString(), password.getText().toString());
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(task);
        });
    }

    private void dataSetHandler(){
        @SuppressLint("HandlerLeak") Handler uiThreadMessageHandler = new Handler(){
            @Override
            public void handleMessage(Message message){
                Bundle bundle = message.getData();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.fragmentFrameLayout,
                        new MapFragment()).commit();
            }
        };
        GetDataTask task = new GetDataTask(uiThreadMessageHandler, dataCache.getServerHost(),
                dataCache.getServerPort(), dataCache.getAuthToken());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);
    }

    private void enableButtons() {
        login.setEnabled(enableLogin());
        register.setEnabled(enableRegister());
    }

    private boolean enableLogin() {
        return !username.getText().toString().equals("") &&
                !password.getText().toString().equals("") &&
                !serverHost.getText().toString().equals("") &&
                !serverPort.getText().toString().equals("");
    }

    private boolean enableRegister() {
        return !username.getText().toString().equals("") &&
                !password.getText().toString().equals("") &&
                !firstName.getText().toString().equals("") &&
                !lastName.getText().toString().equals("") &&
                !email.getText().toString().equals("") &&
                !serverHost.getText().toString().equals("") &&
                !serverPort.getText().toString().equals("") &&
                genders.getCheckedRadioButtonId() != -1;
    }

    private class RegisterTask implements Runnable {
        private final Handler messageHandler;
        private final String serverHost;
        private final String serverPort;
        private final String username;
        private final String password;
        private final String email;
        private final String firstName;
        private final String lastName;
        private final String gender;

        private RegisterTask(Handler messageHandler, String... data) {
            this.messageHandler = messageHandler;
            serverHost = data[0];
            serverPort = data[1];
            username = data[2];
            password = data[3];
            email = data[4];
            firstName = data[5];
            lastName = data[6];
            gender = data[7];
        }

        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy(serverHost, serverPort);
            RegisterLoginResult result = proxy.register(username, password, email,
                    firstName, lastName, gender);
            if (result.isSuccess()) {
                dataCache.setAuthToken(result.getAuthToken());
                dataCache.setPersonId(result.getPersonID());
                dataCache.setServerHost(serverHost);
                dataCache.setServerPort(serverPort);
                sendMessage(true);
            } else {
                sendMessage(false);
            }
        }

        private void sendMessage(boolean success){
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            if (success) {
                messageBundle.putString(SUCCESS, "true");
            } else {
                messageBundle.putString(SUCCESS, "false");
            }
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }
    }

    private class LoginTask implements Runnable {
        private final Handler messageHandler;
        private final String serverHost;
        private final String serverPort;
        private final String username;
        private final String password;

        private LoginTask(Handler messageHandler, String... data){
            this.messageHandler = messageHandler;
            serverHost = data[0];
            serverPort = data[1];
            username = data[2];
            password = data[3];
        }

        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy(serverHost, serverPort);
            RegisterLoginResult result = proxy.login(username, password);

            if (result.isSuccess()) {
                dataCache.setAuthToken(result.getAuthToken());
                dataCache.setPersonId(result.getPersonID());
                dataCache.setServerHost(serverHost);
                dataCache.setServerPort(serverPort);
                sendMessage(true);
            } else {
                sendMessage(false);
            }
        }

        private void sendMessage(boolean success){
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            if (success) {
                messageBundle.putString(SUCCESS, "true");
            } else {
                messageBundle.putString(SUCCESS, "false");
            }
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }

    }

    private class GetDataTask implements Runnable {
        private final Handler messageHandler;
        private final String serverHost;
        private final String serverPort;
        private final String authToken;

        public GetDataTask(Handler messageHandler, String... data) {
            this.messageHandler = messageHandler;
            serverHost = data[0];
            serverPort = data[1];
            authToken = data[2];
        }

        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy(serverHost, serverPort);
            Gson gson = new Gson();

            String userPersonsJson = proxy.getPeople(authToken);
            System.out.println("MY JSON: " + userPersonsJson);
            ArrayList<PersonModel> people = gson.fromJson(userPersonsJson, PersonResult.class).getPersons();

            dataCache.setUserPersons(people);

            String userEventsJson = proxy.getEvents(authToken);
            ArrayList<EventModel> events = gson.fromJson(userEventsJson, EventResult.class).getEvents();

            dataCache.setUserEvents(events);
            sendMessage();
        }

        private void sendMessage() {                   // <-------   I don't really need anything in this method.. might delete later
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putString(SUCCESS, "true");
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }

    }






}