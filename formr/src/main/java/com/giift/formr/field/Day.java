package com.giift.formr.field;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.UiThread;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;

import com.giift.formr.IField;

import java.util.ArrayList;

/**
 * Field that allows the input of a day. (number between 1 and 31)
 *
 * @author Nicolas
 */
public class Day extends Text implements IField {
  public Day(Context context) {
    super(context);
    this.Init();
  }

  public Day(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.Init();
  }

  public Day(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Day(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  @UiThread
  protected void Init() {
    this.SetInputType(InputType.TYPE_CLASS_NUMBER);
    InputFilter lengthFilter = new InputFilter.LengthFilter(2);
    ArrayList<InputFilter> filtersArray = new ArrayList<>();
    filtersArray.add(lengthFilter);
    this.SetFilters(filtersArray);
  }
}
