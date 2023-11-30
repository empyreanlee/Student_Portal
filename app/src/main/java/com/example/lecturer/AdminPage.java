package com.example.lecturer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.FirebaseApp;

public class AdminPage extends AppCompatActivity {
    private TextView tabText;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageView iconButton1, iconButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        FirebaseApp.initializeApp(this);


        tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        iconButton1 = findViewById(R.id.iconButton1);
        iconButton1.setSelected(true);

        iconButton1.setOnClickListener(v ->  {
            Intent intent = new Intent(AdminPage.this, AdminPage.class);
                startActivity(intent);
            });
        iconButton2 =findViewById(R.id.iconButton2);

        iconButton2.setOnClickListener(v ->{
            Intent intent = new Intent(AdminPage.this, SemesterAdmin.class);
            startActivity(intent);

        });

        MyPagerAdapter1 adapter = new MyPagerAdapter1(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    Fragment fragment = adapter.createFragment(position);
                    if (fragment instanceof MyPagerAdapter1.TitledFragment) {
                        tab.setText(((MyPagerAdapter1.TitledFragment) fragment).getTitle());
                    }
                }
        ).attach();

        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.turquoise),
                ContextCompat.getColor(this, R.color.black)
        );

    }
}