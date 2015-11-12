package com.joker.colorviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Joker on 2015/11/12.
 */
public class ColorPagerAdapter extends PagerAdapter {

  private Context mContext;

  public ColorPagerAdapter(Context context) {
    mContext = context;
  }

  @Override public Object instantiateItem(ViewGroup collection, int position) {
    ColorPagerEnum customPagerEnum = ColorPagerEnum.values()[position];
    LayoutInflater inflater = LayoutInflater.from(mContext);
    View layout = inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);
    collection.addView(layout);
    return layout;
  }

  @Override public void destroyItem(ViewGroup collection, int position, Object view) {
    collection.removeView((View) view);
  }

  @Override public int getCount() {
    return ColorPagerEnum.values().length;
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }
}
