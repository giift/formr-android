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
 * Field that allows the input of a credit card cvv.
 *
 * @author Nicolas
 */
public class Cvv extends Text implements IField {
  protected static final int MAX_LENGTH = 4;

  public Cvv(Context context) {
    super(context);
    this.Init();
  }

  public Cvv(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.Init();
  }

  public Cvv(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Cvv(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  /**
   * Make the current field a numeric password input field with a max length of MAX_LENGTH
   */
  @UiThread
  protected void Init() {
    this.SetInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    InputFilter lengthFilter = new InputFilter.LengthFilter(MAX_LENGTH);
    ArrayList<InputFilter> filtersArray = new ArrayList<>();
    filtersArray.add(lengthFilter);
    this.SetFilters(filtersArray);
  }
}
