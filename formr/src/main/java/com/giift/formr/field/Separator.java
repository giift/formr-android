package com.giift.formr.field;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.giift.formr.IField;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Nicolas
 */
public class Separator extends TextView implements IField {

  private String id_ = null;
  private String value_ = null;
  private String label_ = null;
  private JSONObject config_ = null;

  public Separator(Context context) {
    super(context);
    this.Init();
  }

  public Separator(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.Init();

  }

  public Separator(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Separator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  @Override
  public void Init(@NonNull JSONObject config) {
    this.config_ = config;
    this.label_ = config.optString("label", null);
    this.value_ = config.optString("value", null);
    this.id_ = config.optString("id", null);

    this.PostUpdate();
  }

  @Override
  public String GetFieldId() {
    return this.id_;
  }

  @Override
  public boolean Validate() {
    return true;
  }

  @Override
  public String[] GetValues() {
    return new String[]{this.GetValue()};
  }

  @Override
  public boolean Update(@NonNull JSONObject config) {
    this.Init(config);
    return true;
  }

  @Override
  public boolean IsFocusable() {
    return false;
  }

  @Override
  public void SetLabel(String label) {

  }

  @Override
  public void SetError(String error) {

  }

  @Override
  public void SetHint(String hint) {

  }

  @Override
  public void SetImeActionLabel(String label, int action) {
  }

  /**
   * This function stores the data required to rebuild the Separator IField
   * we store the Json Configuration along with the value
   *
   * @return IField.SavedState
   */
  @Override
  public Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    IField.SavedState ss = new IField.SavedState(superState);
    String value = this.GetValue();
    ss.SaveValue(value);
    if (this.config_ != null) {
      try {
        this.config_.remove("value");
        this.config_.put("value", value);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      ss.SaveJsonConfig(this.config_.toString());
    }
    return ss;
  }

  @Override
  public void onRestoreInstanceState(Parcelable state) {
    IField.SavedState ss = (IField.SavedState) state;
    super.onRestoreInstanceState(ss.getSuperState());
  }

  /**
   * Perform the ui updates on the main thread.
   */
  protected void PostUpdate() {
    (new Handler(getContext().getMainLooper())).post(new Runnable() {
      @Override
      public void run() {
        if (!TextUtils.isEmpty(Separator.this.label_)) {
          Separator.this.setText(Separator.this.label_);
        }
      }
    });

  }

  private String GetValue() {
    return this.value_;
  }

  private void Init() {
    this.setSaveEnabled(true);
  }
}
