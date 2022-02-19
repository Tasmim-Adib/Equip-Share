package com.example.equipshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new AddReagentFragment()).commit();

            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new HomeFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.bottom_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.bottom_department:
                        selectedFragment = new DepartmentFragment();
                        break;

                    case R.id.bottom_search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.bottom_info:
                        selectedFragment = new InfoFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,selectedFragment).commit();
                return true;
            }
        });
    }

}