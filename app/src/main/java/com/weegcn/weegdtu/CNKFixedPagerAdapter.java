package com.weegcn.weegdtu;

/**
 * Created by Administrator on 2017/9/10.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;


public class CNKFixedPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles;
    private LayoutInflater mInflater;
    public void setTitles(String[] titles) {
        this.titles = titles;
    }
    private List<BaseFragment> fragments;
    public CNKFixedPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    public List<BaseFragment> getFragments() {
        return fragments;
    }

    public void setFragments(List<BaseFragment> fragments) {
        this.fragments = fragments;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment=null;
        try {
            fragment=(Fragment)super.instantiateItem(container,position);
        }catch (Exception e){

        }
        return fragment;
    }

}