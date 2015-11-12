package com.joker.colorviewpager;

/**
 * Created by Joker on 2015/11/12.
 */
public enum ColorPagerEnum {

  RED(R.layout.view_red),
  green(R.layout.view_green),
  BLUE(R.layout.view_blue),
  WHITE(R.layout.view_white),
  ORANGE(R.layout.view_orange);

  private int layoutResId;

  ColorPagerEnum(int layoutResId) {
    this.layoutResId = layoutResId;
  }

  public int getLayoutResId() {
    return layoutResId;
  }

}
