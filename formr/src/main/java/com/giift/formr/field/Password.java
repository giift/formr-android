package com.giift.formr.field;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.UiThread;
import android.text.InputType;
import android.util.AttributeSet;

import com.giift.formr.IField;


/**
 * @author Nicolas
 */
public class Password extends Text implements IField {
  public Password(Context context) {
    super(context);
    this.Init();
  }

  public Password(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.Init();
  }

  public Password(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Password(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  @UiThread
  protected void Init() {
    this.SetInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
  }
}
