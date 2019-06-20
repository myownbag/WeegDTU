package com.weegcn.weegdtu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;


public abstract class BaseFragment extends Fragment {
    public Boolean mIsatart=false;
    public boolean m_dlgcancled=false;
    Bundle bundle;
    public int position=0;
    String str;
    MainActivity mainActivity;
    abstract public void OndataCometoParse(String readOutMsg1, byte[] readOutBuf1) ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (bundle != null) {
            position=bundle.getInt("position");
            str=bundle.getString("extra");
//            Log.d("zl","position:"+position);
//            Log.d("zl","title:"+str);
        }
        else
        {
            Log.d("zl","position:"+"ERROR");
        }
        mainActivity=MainActivity.getcurinstance();
    }

    public void Oncurrentpageselect(int index)
    {
        if(position!=index)
        {
            mIsatart=false;
        }
    }
    public void Ondlgcancled()
    {
        mIsatart=false;
    }
}
