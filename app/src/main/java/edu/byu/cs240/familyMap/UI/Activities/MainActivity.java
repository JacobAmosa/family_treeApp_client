package edu.byu.cs240.familyMap.UI.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import edu.byu.cs240.familyMap.R;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener {

    private FragmentManager fm = getSupportFragmentManager();

    //________________________ onCreate and other Activity functions ____________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if ((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey("Re-sync"))){

            Fragment mapFragment = new MapFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            fragmentTransaction.add(R.id.fragment_container, mapFragment).commit();
        }
        else if (fragment == null) {
            fragment = new LoginFragment();
            ((LoginFragment) fragment).setLoginListener(this);
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    //--****************-- On Login success --***************--
    @Override
    public void loginComplete()
    {
        Fragment mapFragment = new MapFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, mapFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }


}