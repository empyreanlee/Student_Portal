package com.example.lecturer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyPagerAdapter1 extends FragmentStateAdapter {

    public MyPagerAdapter1(FragmentActivity fa){
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentPass();
            case 1:
                return new FragmentFail();
            case 2:
                return new FragmentSpec();
            case 3:
                return new FragmentMiss();
            case 4:
                return new FragmentUnreg();
            case 5:
                return new FragmentRep();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 6; // Number of tabs
    }

    public interface TitledFragment {
        CharSequence getTitle();
    }
}
