package com.example.lecturer;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StudentReg extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private EditText editTextName;
    private EditText editTextRegNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_reg);

        tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        editTextName = findViewById(R.id.editTextName);
        editTextRegNo = findViewById(R.id.editTextRegNo);


        MyPagerAdapter adapter = new MyPagerAdapter(this, editTextName, editTextRegNo);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    Fragment fragment = adapter.createFragment(position);
                    if (fragment instanceof MyPagerAdapter.TitledFragment) {
                        tab.setText(((MyPagerAdapter.TitledFragment) fragment).getTitle());
                    }
                }
        ).attach();

        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.lavender),
                ContextCompat.getColor(this, R.color.purple)
        );
    }
    public EditText getEditTextName() {
        return editTextName;
    }

    public EditText getEditTextRegNo() {
        return editTextRegNo;
    }
}
