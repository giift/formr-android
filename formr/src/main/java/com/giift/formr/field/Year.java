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
 * @author Nicolas
 */
public class Year extends Text implements IField {
  public Year(Context context) {
    super(context);
    this.Init();
  }

  public Year(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.Init();
  }

  public Year(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Year(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  @UiThread
  protected void Init() {
    this.SetInputType(InputType.TYPE_CLASS_NUMBER);
    InputFilter lengthFilter = new InputFilter.LengthFilter(4);
    ArrayList<InputFilter> filtersArray = new ArrayList<>();
    filtersArray.add(lengthFilter);
    this.SetFilters(filtersArray);
  }
}
