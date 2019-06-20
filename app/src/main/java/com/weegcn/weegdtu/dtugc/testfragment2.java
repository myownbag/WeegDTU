package com.weegcn.weegdtu.dtugc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weegcn.weegdtu.BaseFragment;
import com.weegcn.weegdtu.MainActivity;

public class testfragment2 extends BaseFragment {
    TextView mView;
    @Override
    public void OndataCometoParse(String readOutMsg1, byte[] readOutBuf1) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = new TextView(MainActivity.getcurinstance());
        mView.setText("测试页面2");
        return mView;
    }
}
