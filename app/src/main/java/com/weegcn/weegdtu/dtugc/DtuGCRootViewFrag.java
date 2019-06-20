package com.weegcn.weegdtu.dtugc;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weegcn.weegdtu.BaseFragment;
import com.weegcn.weegdtu.CNKFixedPagerAdapter;
import com.weegcn.weegdtu.MainActivity;
import com.weegcn.weegdtu.R;
import com.weegcn.weegdtu.RootViewFragment;
import com.weegcn.weegdtu.utils.Constants;

import java.util.ArrayList;

public class DtuGCRootViewFrag extends RootViewFragment {

    int mScrollX = 0;
    LayoutInflater mLayoutInflater;
    private ViewGroup mClassContainer;
    int mCurClassIndex;
    private int mCurClassIndex1;
    ViewPager info_viewpager;
    private HorizontalScrollView mScrollBar;
    private BaseFragment mCurrentpage;
    private BaseFragment mPrepage;
    private ArrayList<BaseFragment> fragments;
    private int  mPreClassIndex;
    private CNKFixedPagerAdapter mPagerAdater;
    Context context;
    @Override
    public void initview() {
        mLayoutInflater = LayoutInflater.from(MainActivity.getcurinstance());
        mClassContainer = mView.findViewById(R.id.ll_container);
        context = MainActivity.getcurinstance();

        info_viewpager = (ViewPager)mView.findViewById(R.id.info_viewpager);
        mScrollBar = (HorizontalScrollView)mView.findViewById(R.id.horizontal_info);
        initfragments();
    }

    private void initfragments() {
        int index=0;
        addScrollView(Constants.tsetDtuDCItems);
        mScrollBar.post(new Runnable() {
            @Override
            public void run() {
                mScrollBar.scrollTo(mScrollX,0);
            }
        });
        fragments = new ArrayList<>();
            testfragment1 fg1 = new testfragment1();
            fragments.add(fg1);

            testfragment2 fg2 = new testfragment2();
            fragments.add(fg2);

        mPagerAdater=new CNKFixedPagerAdapter(MainActivity.getcurinstance().getSupportFragmentManager());
        mPagerAdater.setTitles(Constants.tsetDtuDCItems);
        mPagerAdater.setFragments(fragments);
        info_viewpager.setAdapter(mPagerAdater);
        info_viewpager.addOnPageChangeListener(new OnpagechangedListernerImp());
        mCurrentpage=fg1;
        mPrepage=fg1;
    }

    private void addScrollView(String[] titles) {
        final int count = titles.length;
        for (int i = 0; i < count; i++) {
            // Log.e("tchl","onclick: i:"+i);
            final String title = titles[i];
            final View view = mLayoutInflater.inflate(R.layout.horizontal_item_layout, null);
            final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.horizontal_linearlayout_type);
            final ImageView img_type = (ImageView) view.findViewById(R.id.horizontal_img_type);
            final TextView type_name = (TextView) view.findViewById(R.id.horizontal_tv_type);
            type_name.setText(title);

            if (i == mCurClassIndex) {
                //已经选中
                type_name.setTextColor(ContextCompat.getColor(MainActivity.getcurinstance(), R.color.color_selected));
                img_type.setImageResource(R.drawable.bottom_line_blue);
            } else {
                //未选中
                type_name.setTextColor(ContextCompat.getColor(MainActivity.getcurinstance(), R.color.color_unselected));
                img_type.setImageResource(R.drawable.bottom_line_gray);
            }
            final int index=i;
            //点击顶部Tab标签，动态设置下面的ViewPager页面
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //首先设置当前的Item为正常状态
                    // Log.e("tchl","onclick: first mCurClassIndex:"+mCurClassIndex);
                    View currentItem=mClassContainer.getChildAt(mCurClassIndex);
                    ((TextView)(currentItem.findViewById(R.id.horizontal_tv_type))).setTextColor(ContextCompat.getColor(MainActivity.getcurinstance(), R.color.color_unselected));
                    ((ImageView)(currentItem.findViewById(R.id.horizontal_img_type))).setImageResource(R.drawable.bottom_line_gray);
                    mCurClassIndex=index;
                    mCurClassIndex1=index;
                    // Log.e("tchl","onclick: first index:"+index);
                    //设置点击状态
                    img_type.setImageResource(R.drawable.bottom_line_blue);
                    type_name.setTextColor(ContextCompat.getColor(context, R.color.color_selected));
                    //跳转到指定的ViewPager
                    info_viewpager.setCurrentItem(mCurClassIndex);
                    mCurrentpage=fragments.get(mCurClassIndex);
                    mCurrentpage=fragments.get(mCurClassIndex);
                    mCurrentpage.Oncurrentpageselect(mCurClassIndex);
                    if(mPrepage!=null)
                    {
                        mPrepage.Oncurrentpageselect(mCurClassIndex);
                    }
                    mPrepage=mCurrentpage;
//                    if(mCurClassIndex1!=2)
//                    {
//                        if(mPreClassIndex==2)
//                        {
//                            Log.d("zl","addScrollView bluetoothblockdisable");
//                            bluetoothblockdisable();
//                        }
//                    }
                    mPreClassIndex=mCurClassIndex1;
                }
            });

            mClassContainer.addView(view);
        }

    }

    private class OnpagechangedListernerImp implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            if(i==0)
            {
               MainActivity.getcurinstance().dragviewlayout.setTouchEvent(false);
                Log.d("zl","requestDisallowInterceptTouchEvent(true);");
            }
            else
            {
                MainActivity.getcurinstance().dragviewlayout.setTouchEvent(true);
                Log.d("zl","requestDisallowInterceptTouchEvent(false);");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }
}
