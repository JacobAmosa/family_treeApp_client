package edu.byu.cs240.familyMap.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel getViewModel(){
        return new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (DataCache.getInstance().getAuthToken() == null) {
            startLoginFragment();
        } else {
            startMapFragment();
        }
    }

    private void startLoginFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentFrameLayout,
                new LoginFragment()).commit();
    }

    public void startMapFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentFrameLayout,
                new MapFragment()).commit();
    }

}