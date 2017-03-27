package com.example.administrator.app;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/10/23 0023.
 */
public class slidingmenu extends HorizontalScrollView {

    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mScreenWidth;
    private int mMenuRightPadding=150;
    private boolean once;
    private  int mMenuwidth;

    public slidingmenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm= (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth=outMetrics.widthPixels;

        //mMenuRightPadding= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,context.
                //getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!once) {

            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);
            mMenuwidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;
            once=true;
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            this.scrollTo(mMenuwidth,0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int action=ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_UP:
               int scrollX=getScrollX();

                if(scrollX>=mMenuwidth/2)
                {
                    this.smoothScrollTo(mMenuwidth,0);
                }else {
                    this.smoothScrollTo(0,0);
                }
                return  true;

        }
        return super.onTouchEvent(ev);

    }
}
