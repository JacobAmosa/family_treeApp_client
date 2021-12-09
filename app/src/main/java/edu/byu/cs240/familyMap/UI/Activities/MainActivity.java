package edu.byu.cs240.familyMap.UI.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import edu.byu.cs240.familyMap.R;

public class MainActivity extends AppCompatActivity implements taskLoginFragment.LoginFragmentListener {

    private final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if ((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey("Re-sync"))){
            startMap(fragment);
        }
        else if (fragment == null) {
            startLogin(fragment);
        }
    }

    public void startLogin(Fragment fragment){
        fragment = new taskLoginFragment();
        ((taskLoginFragment) fragment).setLoginListener(this);
        fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    public void startMap(Fragment fragment){
        Fragment mapFragment = new MapFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mapFragment).commit();
    }


    @Override
    public void logComplete() {
        Fragment mapFragment = new MapFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mapFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}