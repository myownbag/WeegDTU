package com.weegcn.weegdtu;

import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.weegcn.mydragview.MyLinearLayout;
import com.weegcn.mydragview.myDragviewlayout;
import com.weegcn.utils.Constants;
import com.weegcn.weegdtu.dtugc.DtuGCRootViewFrag;

public class MainActivity extends AppCompatActivity {
    public myDragviewlayout dragviewlayout;
    ListView mLeftList;
    int mCurSelectedMenuItem;
    ArrayAdapter leftmenuad;
    static MainActivity mainActivity;
    public static MainActivity getcurinstance() {

        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        dragviewlayout =findViewById(R.id.dragview);
        MyLinearLayout mLinearLayout = findViewById(R.id.mll);
        mLinearLayout.setDraglayout(dragviewlayout);
        dragviewlayout.setDragStatusListener(new myDragviewlayout.OnDragStatusChangeListener() {
            @Override
            public void onClose() {

            }

            @Override
            public void onOpen() {

            }

            @Override
            public void onDraging(float percent) {

            }

            @Override
            public void onSizeChanged() {
                dragviewlayout.open(true);
//                Log.d("zl","size changed");
            }
        });


        mLeftList =  findViewById(R.id.lv_left);
        initLeftContent();
      //  mMainList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.NAMES));
        dragviewlayout.mMainContent.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
               // Log.d("zl","onLayoutChange");
                dragviewlayout.open(false);
            }
        });
        mainActivity = this;
    }

    private void initLeftContent() {
        mCurSelectedMenuItem = 0;
        mLeftList = (ListView) findViewById(R.id.lv_left);
        leftmenuad = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Constants.MenuItems){
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView mText = (TextView) view.findViewById(android.R.id.text1);
                // mText.setTextColor(R.drawable.menu_text_selector);
                //  Log.d("zl","in get view " + mCurSelectedMenuItem );
                if (mCurSelectedMenuItem == position) {
                    mText.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    mText.setTextColor(getResources().getColor(R.color.white));
                }
                return view;
            }
        };
        mLeftList.setAdapter(leftmenuad);

        mLeftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurSelectedMenuItem = position;
                leftmenuad.notifyDataSetChanged();
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction(); //开启事务
                switch (position)
                {
                    case 0:
                        transaction.replace(R.id.lv_main,new DtuGCRootViewFrag(),
                                "DTU_GC");
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                        default:
                            break;
                }
                transaction.commit();// 提交事务
            }
        });
    }
}
