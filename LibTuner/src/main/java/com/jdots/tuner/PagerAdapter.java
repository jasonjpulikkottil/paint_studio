package com.jdots.tuner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int aNoOfTabs;
    public PagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.aNoOfTabs = numberOfTabs;
    }



    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0:
                EditImageFragment editImageFragment = new EditImageFragment();
                return editImageFragment;

            case 1:
                RotateImage rotateImage = new RotateImage();
                return rotateImage;
            default:
                return null;


        }



    }

    @Override
    public int getCount() {
        return aNoOfTabs;
    }
}
