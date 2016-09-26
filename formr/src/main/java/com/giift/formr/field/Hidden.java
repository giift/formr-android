package com.giift.formr.field;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.giift.formr.IField;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Nicolas on 2015/09/15.
 */
public class Hidden extends View implements IField {

  private String id_ = null;
  private String value_ = null;
  private JSONObject config_ = null;

  public Hidden(Context context) {
    super(context);
    this.Init();
  }

  public Hidden(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.Init();
  }

  public Hidden(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Hidden(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  @Override
  public void Init(@NonNull JSONObject config) {
    this.config_ = config;
    this.id_ = config.optString("id", "");
    this.value_ = config.optString("value", "");
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

  protected void Init() {
    this.setSaveEnabled(true);
    this.setVisibility(View.GONE);
  }

  /**
   * This function stores the data required to rebuild the Hidden IField
   * we store the Json Configuration
   *
   * @return IField.SavedState
   */
  @Override
  protected Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    SavedState ss = new SavedState(superState);
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
  protected void onRestoreInstanceState(Parcelable state) {
    SavedState ss = (SavedState) state;
    super.onRestoreInstanceState(ss.getSuperState());
  }

  private String GetValue() {
    return this.value_;
  }
}
