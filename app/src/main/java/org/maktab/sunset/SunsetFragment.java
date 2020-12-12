package org.maktab.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import org.maktab.sunset.databinding.FragmentSunsetBinding;

public class SunsetFragment extends Fragment {

    private FragmentSunsetBinding mBinding;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    private boolean isSunrise = false;
    private boolean isRunningAnim = false;

    public SunsetFragment() {
        // Required empty public constructor
    }

    public static SunsetFragment newInstance() {
        SunsetFragment fragment = new SunsetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_sunset,
                container,
                false);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
//                isRunningAnim = false;
            }
        });

        return mBinding.getRoot();
    }

    private void startAnimation() {
        float sunStartY = mBinding.sun.getTop(); //10.000
        float sunEndY = mBinding.sea.getTop(); //100.000
        float seaStartY = mBinding.sea.getBottom();
        float seaEndY = mBinding.sky.getBottom();
//        if(isRunningAnim){
//            mBinding.sea.clearAnimation();
//            mBinding.sky.clearAnimation();
//            mBinding.sun.clearAnimation();
//            isSunrise = isSunrise ? false : true;
//        }
        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mBinding.sun, "y", sunStartY, sunEndY)
                .setDuration(4000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetAnimator = ObjectAnimator
                .ofInt(mBinding.sky, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                .setDuration(4000);
        sunsetAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightAnimator = ObjectAnimator
                .ofInt(mBinding.sky, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                .setDuration(3000);
        nightAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator reverseHeightAnimator = ObjectAnimator
                .ofFloat(mBinding.sun, "y", sunEndY, sunStartY)
                .setDuration(4000);
        reverseHeightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunriseAnimator = ObjectAnimator
                .ofInt(mBinding.sky, "backgroundColor", mNightSkyColor, mSunsetSkyColor, mBlueSkyColor)
                .setDuration(4000);
        sunriseAnimator.setEvaluator(new ArgbEvaluator());


        ObjectAnimator shadowAnimator = ObjectAnimator
                .ofFloat(mBinding.sunShadow, "y", seaStartY, seaEndY)
                .setDuration(4000);
        shadowAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator reverseShadowAnimator = ObjectAnimator
                .ofFloat(mBinding.sunShadow, "y", seaEndY, seaStartY)
                .setDuration(4000);
        reverseShadowAnimator.setInterpolator(new AccelerateInterpolator());
        /*heightAnimator.start();
        sunsetAnimator.start();
        nightAnimator.start();*/
        isRunningAnim = true;
        AnimatorSet animatorSet = new AnimatorSet();
        if (!isSunrise) {
            animatorSet
                    .play(heightAnimator)
                    .with(sunsetAnimator)
                    .with(shadowAnimator)
                    .before(nightAnimator);
//            shadowAnimator.start();
            isSunrise = true;
//            isRunningAnim = false;
        } else {
            animatorSet
                    .play(nightAnimator)
                    .before(reverseShadowAnimator)
                    .before(reverseHeightAnimator)
                    .before(sunriseAnimator);
//            reverseShadowAnimator.start();
            isSunrise = false;
//            isRunningAnim = false;
        }

        animatorSet.start();
//        if(isRunningAnim) {
//            animatorSet.cancel();
//        }
//        mBinding.rootLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                animatorSet.start();
//                return true;
//            }
//        });
    }
}