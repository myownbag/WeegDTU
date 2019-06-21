package com.weegcn.weegdtu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class RootViewFragment extends Fragment {

    public View mView;
    protected BaseFragment mCurrentpage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        initview(inflater);
        initdata();
        return mView;
    }
    public abstract void initview(LayoutInflater inflater) ;
    private void initdata() {
    }
    public BaseFragment  getcurpage()
    {
        return  mCurrentpage;
    }
}
