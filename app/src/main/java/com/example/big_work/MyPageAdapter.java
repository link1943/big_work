package com.example.big_work;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.ListFragment;

public class MyPageAdapter extends FragmentPagerAdapter {
    int flag;
    public MyPageAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new ByListFragment();
        }
        else {
            return new PlayFragment();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }


}
