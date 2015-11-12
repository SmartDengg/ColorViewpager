package com.joker.colorviewpager;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GuideActivity extends AppCompatActivity {

  private static final String TAG = GuideActivity.class.getSimpleName();
  private static final int DURATION = 4 * 1000;

  private RelativeLayout rootView;
  private ViewPager viewPager;
  private LinearLayout indicatorLayout;
  private View gradientIv;

  private TextView skipTv;
  private ImageButton nextBtn;
  private TextView doneTv;

  private ColorPagerAdapter pagerAdapter;
  private ValueAnimator gradientAnim;

  private int totalDistance;//小黑点要移动的全部距离
  private int startX;//小黑点开始位置

  /*黑色移动圆点*/
  private TextView animIndicator;

  int[] colors = new int[] {
      Color.parseColor("#ffff4444"),//红
      Color.parseColor("#ff0099cc"),//蓝
      Color.parseColor("#ff99cc00"),//绿
      Color.parseColor("#ffffbb33"),//橘
      Color.parseColor("#ffDA498C")//
  };

  private ViewPager.OnPageChangeListener pageChangeListener =
      new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

          if (pagerAdapter.getCount() > 0) {

            float length = (position + positionOffset) / (pagerAdapter.getCount() - 1);
            int progress = (int) (length * DURATION);

            float path = length * GuideActivity.this.totalDistance;
            ViewCompat.setTranslationX(animIndicator, GuideActivity.this.startX + path);

            /*设置过度颜色*/
            gradientAnim.setCurrentPlayTime(progress);
          }
        }

        @Override public void onPageScrollStateChanged(int state) {

          /*关闭硬件加速*/
          if (state != ViewPager.SCROLL_STATE_IDLE) {
            final int childCount = viewPager.getChildCount();
            for (int i = 0; i < childCount; i++) {
              viewPager.getChildAt(i).setLayerType(View.LAYER_TYPE_NONE, null);
            }
          }
        }

        @Override public void onPageSelected(int position) {

          if (position == pagerAdapter.getCount() - 1) {
            GuideActivity.this.skipTv.setVisibility(View.GONE);
            GuideActivity.this.nextBtn.setVisibility(View.GONE);
            GuideActivity.this.doneTv.setVisibility(View.VISIBLE);
          } else {
            GuideActivity.this.skipTv.setVisibility(View.VISIBLE);
            GuideActivity.this.nextBtn.setVisibility(View.VISIBLE);
            GuideActivity.this.doneTv.setVisibility(View.GONE);
          }
        }
      };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    rootView = (RelativeLayout) findViewById(R.id.splash_layout_root_view);
    gradientIv = findViewById(R.id.main_layout_gradient_iv);
    indicatorLayout = (LinearLayout) findViewById(R.id.splash_layout_pager_indicator_ll);

    GuideActivity.this.setListener();
    GuideActivity.this.setupAnim();

    viewPager = (ViewPager) findViewById(R.id.splash_layout_gradient_viewpager);
    pagerAdapter = new ColorPagerAdapter(GuideActivity.this);
    viewPager.setAdapter(pagerAdapter);
    viewPager.addOnPageChangeListener(pageChangeListener);

    GuideActivity.this.setupIndicator();
  }

  private void setListener() {

    this.skipTv = (TextView) findViewById(R.id.splash_layout_skip_tv);
    this.nextBtn = (ImageButton) findViewById(R.id.splash_layout_next_btn);
    this.doneTv = (TextView) findViewById(R.id.splash_layout_done_tv);

    this.skipTv.setOnClickListener(doneListener);
    this.nextBtn.setOnClickListener(nextListener);
    this.doneTv.setOnClickListener(doneListener);
  }

  private View.OnClickListener nextListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
    }
  };

  private View.OnClickListener doneListener = new View.OnClickListener() {
    @Override public void onClick(View v) {

      MainActivity.navigateToMain(GuideActivity.this);
      GuideActivity.this.finish();
    }
  };

  private void setupAnim() {

    gradientAnim = ObjectAnimator.ofInt(gradientIv, "backgroundColor", colors);
    gradientAnim.setEvaluator(new ArgbEvaluator());
    gradientAnim.setDuration(DURATION);
  }

  private void setupIndicator() {

    final Drawable indicatorNormal =
        getResources().getDrawable(R.drawable.indicator_normal_background);
    Drawable indicatorSelected =
        getResources().getDrawable(R.drawable.indicator_selected_background);

    int size = getResources().getDimensionPixelSize(R.dimen.material_8dp);

    this.indicatorLayout.removeAllViews();
    final TextView[] indicators = new TextView[ColorPagerEnum.values().length];
    for (int i = 0; i < indicators.length; i++) {

      indicators[i] = new TextView(GuideActivity.this);
      indicators[i].setGravity(Gravity.CENTER);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);

      if (i != indicators.length - 1) {
        params.setMargins(0, 0, size, 0);
      } else {
        params.setMargins(0, 0, 0, 0);
      }
      indicators[i].setLayoutParams(params);
      indicators[i].setBackgroundDrawable(indicatorNormal);
      indicatorLayout.addView(indicators[i]);
    }

    animIndicator = new TextView(GuideActivity.this);
    animIndicator.setLayoutParams(new LinearLayout.LayoutParams(size, size));
    animIndicator.setBackgroundDrawable(indicatorSelected);
    rootView.addView(animIndicator);

    indicatorLayout.getViewTreeObserver()
        .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          @Override public boolean onPreDraw() {
            indicatorLayout.getViewTreeObserver().removeOnPreDrawListener(this);

            Rect rootRect = new Rect();
            Point globalOffset = new Point();
            rootView.getGlobalVisibleRect(rootRect, globalOffset);

            Rect firstRect = new Rect();
            indicatorLayout.getChildAt(0).getGlobalVisibleRect(firstRect);
            firstRect.offset(-globalOffset.x, -globalOffset.y);

            Rect lastRect = new Rect();
            indicatorLayout.getChildAt(indicators.length - 1).getGlobalVisibleRect(lastRect);

            GuideActivity.this.totalDistance = lastRect.left - indicatorLayout.getLeft();
            GuideActivity.this.startX = firstRect.left;

            ViewCompat.setTranslationX(animIndicator, firstRect.left);
            ViewCompat.setTranslationY(animIndicator, firstRect.top);
            return true;
          }
        });
  }

  @Override protected void onDestroy() {
    super.onDestroy();

    this.skipTv.setOnClickListener(null);
    this.nextBtn.setOnClickListener(null);
    this.doneTv.setOnClickListener(null);
    this.viewPager.removeOnPageChangeListener(pageChangeListener);
  }
}
