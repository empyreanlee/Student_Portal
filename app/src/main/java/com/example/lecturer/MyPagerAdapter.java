package com.example.lecturer;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class MyPagerAdapter extends FragmentStateAdapter {
    private final EditText editTextName;
    private final EditText editTextRegNo;

    public MyPagerAdapter(FragmentActivity fa,EditText editTextName, EditText editTextRegNo)
    {
        super(fa);
        this.editTextName = editTextName;
        this.editTextRegNo = editTextRegNo;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Fragment1();
            case 1:
                return new Fragment2();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }

    public interface TitledFragment {
        CharSequence getTitle();
        }
    }
