package com.weegcn.weegdtu.dtugc;

import com.weegcn.weegdtu.DtuRootViewFrag;
import com.weegcn.weegdtu.utils.Constants;

import java.util.ArrayList;

public class GCSettingFragment extends DtuRootViewFrag {

    //抽象方法，初始化需要加载的页面及标题
    @Override
    public void addfragments() {
            //初始化页面信息
            fragments = new ArrayList<>();
            testfragment1 fg1 = new testfragment1();
            fragments.add(fg1);
            testfragment2 fg2 = new testfragment2();
            fragments.add(fg2);

            //初始化标题信息
            thistitles = Constants.tsetDtuDCItems;
    }
}
