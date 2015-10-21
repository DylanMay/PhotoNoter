package com.yydcdut.note.controller.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.yydcdut.note.R;
import com.yydcdut.note.adapter.IntroducePagerAdapter;
import com.yydcdut.note.controller.BaseActivity;
import com.yydcdut.note.service.InitService;
import com.yydcdut.note.utils.LollipopCompat;
import com.yydcdut.note.view.CircleProgressBarLayout;

/**
 * Created by yuyidong on 15/8/9.
 */
public class IntroduceActivity extends BaseActivity implements OnPageChangeListener, View.OnClickListener {
    private ImageView mImageDot1, mImageDot2, mImageDot3, mImageDot4, mImageDot5, mImageDot6;
    private View mBtnStart;
    private CircleProgressBarLayout mCircleProgressBar;

    private InitService.InitBinder mInitBinder;

    @Override
    public boolean setStatusBar() {
        return false;
    }

    @Override
    public int setContentView() {
        LollipopCompat.setFullWindow(getWindow());
        return R.layout.activity_introduce;
    }

    @Override
    public void initUiAndListener() {
        initViewPager();
        initDots();
        initBtn();
        initService();
        mCircleProgressBar = (CircleProgressBarLayout) findViewById(R.id.layout_progress);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mInitBinder = (InitService.InitBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mInitBinder = null;
        }
    };

    private void initService() {
        Intent initIntent = new Intent(this, InitService.class);
        bindService(initIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_introduce);
        viewPager.setAdapter(new IntroducePagerAdapter(IntroduceActivity.this));
        viewPager.addOnPageChangeListener(this);
    }

    private void initBtn() {
        mBtnStart = findViewById(R.id.btn_introduce_start);
        mBtnStart.setOnClickListener(this);
    }

    private void initDots() {
        mImageDot1 = (ImageView) findViewById(R.id.img_introduce_1);
        mImageDot2 = (ImageView) findViewById(R.id.img_introduce_2);
        mImageDot3 = (ImageView) findViewById(R.id.img_introduce_3);
        mImageDot4 = (ImageView) findViewById(R.id.img_introduce_4);
        mImageDot5 = (ImageView) findViewById(R.id.img_introduce_5);
        mImageDot6 = (ImageView) findViewById(R.id.img_introduce_6);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                resetDots(mImageDot1);
                break;
            case 1:
                resetDots(mImageDot2);
                break;
            case 2:
                resetDots(mImageDot3);
                break;
            case 3:
                resetDots(mImageDot4);
                break;
            case 4:
                resetDots(mImageDot5);
                break;
            case 5:
                resetDots(mImageDot6);
                if (mBtnStart.getVisibility() == View.GONE) {
                    AnimatorSet animation = new AnimatorSet();
                    animation.setDuration(300);
                    animation.playTogether(
                            ObjectAnimator.ofFloat(mBtnStart, "alpha", 0f, 1f)
                    );
                    animation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mBtnStart.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animation.start();
                }
                break;
        }
    }

    private void resetDots(ImageView imageView) {
        mImageDot1.setImageDrawable(getResources().getDrawable(R.drawable.img_introduce_dot));
        mImageDot2.setImageDrawable(getResources().getDrawable(R.drawable.img_introduce_dot));
        mImageDot3.setImageDrawable(getResources().getDrawable(R.drawable.img_introduce_dot));
        mImageDot4.setImageDrawable(getResources().getDrawable(R.drawable.img_introduce_dot));
        mImageDot5.setImageDrawable(getResources().getDrawable(R.drawable.img_introduce_dot));
        mImageDot6.setImageDrawable(getResources().getDrawable(R.drawable.img_introduce_dot));
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.img_introduce_dot_foucs));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (mInitBinder.isFinished()) {
            IntroduceActivity.this.unbindService(serviceConnection);
            Intent intent = new Intent(IntroduceActivity.this, HomeActivity.class);
            startActivity(intent);
            IntroduceActivity.this.finish();
        } else {
            mCircleProgressBar.show();
            finishAndJump();
        }
    }

    /**
     * 递归，不断的判断
     */
    private void finishAndJump() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mInitBinder.isFinished()) {
                    finishAndJump();
                } else {
                    mCircleProgressBar.hide();
                    IntroduceActivity.this.unbindService(serviceConnection);
                    Intent intent = new Intent(IntroduceActivity.this, HomeActivity.class);
                    startActivity(intent);
                    IntroduceActivity.this.finish();
                }
            }
        }, 500);
    }
}
