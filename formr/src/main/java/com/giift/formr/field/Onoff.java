package com.giift.formr.field;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.giift.formr.Form;
import com.giift.formr.IField;
import com.giift.formr.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Nicolas
 */
public class Onoff extends LinearLayout implements IField, CompoundButton.OnCheckedChangeListener {

  protected JSONObject config_;
  protected String id_ = null;
  protected boolean mandatory_ = false;
  private boolean value_ = false;
  private boolean readonly_ = false;
  private String label_ = "";
  private Uri onValueChanged_ = null;
  private Uri onFocusLost_ = null;
  private Switch switch_ = null;
  private TextView hintText_ = null;
  private TextView errorText_ = null;

  /**
   * Constructor
   */
  public Onoff(Context context) {
    super(context);
    this.Init();
  }

  /**
   * Constructor
   */
  public Onoff(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.Init();
  }

  /**
   * Constructor
   */
  public Onoff(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  /**
   * Constructor
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Onoff(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  @Override
  public void Init(@NonNull JSONObject config) {
    this.config_ = config;
    this.id_ = config.optString("id", null);
    this.mandatory_ = this.config_.optBoolean("mandatory", false);

    this.label_ = this.config_.optString("label", "");
    this.value_ = this.config_.optBoolean("value", false);

    this.onValueChanged_ = null;
    this.onFocusLost_ = null;

    JSONObject settings = config.optJSONObject("settings");
    if (settings != null) {

      this.readonly_ = settings.optBoolean("readonly", false);
      JSONObject callbacks = settings.optJSONObject("callback");
      if (callbacks != null) {
        String onValueChanged = callbacks.optString("on_value_changed");
        if (!TextUtils.isEmpty(onValueChanged)) {
          this.onValueChanged_ = Uri.parse(onValueChanged);
        }
        String onFocusLost = callbacks.optString("on_focus_lost");
        if (!TextUtils.isEmpty(onFocusLost)) {
          this.onFocusLost_ = Uri.parse(onFocusLost);
        }
      }

      JSONObject hint = settings.optJSONObject("hint");
      if (hint != null) {
        String hintString = hint.optString("label");
        this.SetHint(hintString);
      }
      JSONObject error = settings.optJSONObject("error");
      if (error != null) {
        String errorString = error.optString("label");
        this.SetError(errorString);
      }
    }
    this.PostUpdate();
  }

  @Override
  public String GetFieldId() {
    return this.id_;
  }

  @Override
  public boolean Validate() {
    if (this.mandatory_) {
      if (!this.switch_.isChecked()) {
        this.SetError(getResources().getString(R.string.error_field_required));
        return false;
      }
    }
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

  /**
   * This function is used to set a text label before the OnOff switch
   *
   * @param label string to be displayed
   */
  @Override
  public void SetLabel(String label) {
    if (!TextUtils.isEmpty(label) && !label.equals("null")) {
      Onoff.this.switch_.setHint(label);
    } else {
      Onoff.this.switch_.setHint("");
    }
  }

  /**
   * This function is used to display error message in red text at the end of the field
   * if error message is null the error view is hidden
   *
   * @param error error message
   */
  @Override
  public void SetError(@Nullable String error) {
    Drawable ic_error = AppCompatDrawableManager.get().getDrawable(getContext(), R.drawable.ic_error_24dp);
    ic_error = DrawableCompat.wrap(ic_error);
    DrawableCompat.setTint(ic_error.mutate(), ContextCompat.getColor(getContext(), R.color.major_red));
    ic_error.setBounds(0, 0, ic_error.getIntrinsicWidth(), ic_error.getIntrinsicHeight());
    this.switch_.setError(error, ic_error);
    if (this.errorText_ != null) {
      this.errorText_.setText(error);
      if (!TextUtils.isEmpty(error)) {
        this.errorText_.setVisibility(VISIBLE);
      } else {
        this.errorText_.setVisibility(GONE);
      }
    }
  }

  /**
   * This function is used to display a hint for the OnOff  field
   * The hint is displayed below the Onoff field and above the error (if present)
   * hint is displayed with the Typeface italics
   *
   * @param hint string to be displayed
   */
  @Override
  public void SetHint(String hint) {
    if (!TextUtils.isEmpty(hint)) {
      this.hintText_.setText(hint);
      this.hintText_.setVisibility(VISIBLE);
    }
  }

  @Override
  public void SetImeActionLabel(String label, int action) {
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    this.SetError(null);
    if (this.onFocusLost_ != null) {
      ViewParent parent1 = this.getParent();
      if (parent1 instanceof Form) {
        ((Form) parent1).OnUpdateRequest(this.GetFieldId(), onFocusLost_);
      }
    }
    if (this.onValueChanged_ != null) {
      ViewParent parent1 = this.getParent();
      if (parent1 instanceof Form) {
        ((Form) parent1).OnUpdateRequest(this.GetFieldId(), onValueChanged_);
      }
    }

  }

  /**
   * This function stores the data required to rebuild the Onoff IField
   * we store the Json Configuration along with the currently selected value
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
        Onoff.this.SetLabel(Onoff.this.label_);
        Onoff.this.switch_.setOnCheckedChangeListener(null);
        Onoff.this.switch_.setChecked(Onoff.this.value_);
        Onoff.this.switch_.setOnCheckedChangeListener(Onoff.this);
        if (Onoff.this.readonly_) {
          Onoff.this.switch_.setClickable(false);
        }
      }
    });
  }

  @UiThread
  protected void Init() {
    this.setSaveEnabled(true);
    this.setOrientation(VERTICAL);
    this.setPadding(0, 14, 0, 14);

    this.switch_ = new Switch(getContext());
    this.switch_.setTextSize(LABEL_TEXT_SIZE);
    this.switch_.setPadding(8, 0, 8, 0);
    this.addView(this.switch_);
    this.switch_.setOnCheckedChangeListener(this);

    this.hintText_ = new TextView(getContext());
    this.hintText_.setTextSize(HINT_TEXT_SIZE);
    this.hintText_.setTypeface(this.hintText_.getTypeface(), Typeface.ITALIC);
    this.hintText_.setVisibility(GONE);
    this.addView(this.hintText_);

    this.errorText_ = new TextView(getContext());
    this.errorText_.setTextSize(ERROR_TEXT_SIZE);
    this.errorText_.setTextColor(ContextCompat.getColor(getContext(), R.color.major_red));
    this.errorText_.setVisibility(GONE);
    this.addView(this.errorText_);
  }

  private String GetValue() {
    return this.switch_.isChecked() ? "true" : "false";
  }
}