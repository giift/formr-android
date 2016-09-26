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
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giift.formr.Form;
import com.giift.formr.IField;
import com.giift.formr.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Nicolas
 */
public class Checkbox extends LinearLayout implements IField, CheckBox.OnCheckedChangeListener {

  protected boolean mandatory_ = false;
  private String id_ = null;
  private String labelString_ = null;
  private Map<String, CheckBox> items_ = null;
  private TextView label_ = null;
  private LinearLayout checkbox_ = null;
  private TextView hintText_ = null;
  private TextView errorText_ = null;
  private String hintString_ = null;
  private Uri onValueChanged_ = null;
  private Uri onFocusLost_ = null;
  private JSONObject config_ = null;

  public Checkbox(Context context) {
    super(context);
    Init();
  }

  public Checkbox(Context context, AttributeSet attrs) {
    super(context, attrs);
    Init();
  }

  public Checkbox(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Checkbox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    Init();
  }

  @Override
  public void Init(@NonNull JSONObject config) {
    this.config_ = config;
    this.id_ = config.optString("id", null);
    this.mandatory_ = config.optBoolean("mandatory", false);
    this.labelString_ = config.optString("label");
    this.items_ = new HashMap<>();
    this.onValueChanged_ = null;
    this.onFocusLost_ = null;

    JSONObject settings = config.optJSONObject("settings");
    if (settings != null) {
      boolean readOnly = settings.optBoolean("readonly", false);
      JSONObject options = settings.optJSONObject("options");
      if (options != null) {
        Iterator<String> it = options.keys();
        while (it.hasNext()) {
          String key = it.next();
          String value = options.optString(key, "");
          CheckBox tmp = new CheckBox(getContext());
          tmp.setTextSize(14);
          tmp.setOnCheckedChangeListener(this);
          tmp.setText(value);
          this.items_.put(key, tmp);
          if (readOnly) {
            tmp.setEnabled(false);
          }
        }
      }

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
        this.hintString_ = hint.optString("label");
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
    this.SetError(null);
    boolean res = true;
    if (mandatory_ && !(this.GetValues().length > 0)) {
      res = false;
      this.SetError(getResources().getString(R.string.select_option));
    }
    return res;
  }

  @Override
  public String[] GetValues() {
    ArrayList<String> result = new ArrayList<>();
    for (Map.Entry<String, CheckBox> entry : this.items_.entrySet()) {
      if (entry.getValue().isChecked()) {
        result.add(entry.getKey());
      }
    }
    return result.toArray(new String[result.size()]);
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
   * This function is used to set a text label above the Checkbox options
   *
   * @param label string to be displayed
   */
  @Override
  public void SetLabel(String label) {
    if (!TextUtils.isEmpty(label) && !label.equals("null")) {
      this.label_.setText(label);
      this.label_.setVisibility(VISIBLE);
    }
  }

  /**
   * Show Error on the last Checkbox along with a TextView message when field is mandatory
   */
  @Override
  public void SetError(String error) {
    Drawable ic_error = AppCompatDrawableManager.get().getDrawable(getContext(), R.drawable.ic_error_24dp);
    ic_error = DrawableCompat.wrap(ic_error);
    DrawableCompat.setTint(ic_error.mutate(), ContextCompat.getColor(getContext(), R.color.major_red));
    ic_error.setBounds(0, 0, ic_error.getIntrinsicWidth(), ic_error.getIntrinsicHeight());
    if (this.items_ != null && this.items_.entrySet().size() > 0) {
      for (Map.Entry<String, CheckBox> entry : this.items_.entrySet()) {
        entry.getValue().setError(error, ic_error);
      }
      if (this.errorText_ != null) {
        this.errorText_.setText(error);
        if (!TextUtils.isEmpty(error)) {
          this.errorText_.setVisibility(VISIBLE);
        } else {
          this.errorText_.setVisibility(GONE);
        }
      }
    }
  }

  /**
   * This function is used to display a hint for the Checkbox field
   * The hint is displayed below the checkbox options and above the error (if present)
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

  protected void Init() {
    this.setSaveEnabled(true);
    this.setOrientation(VERTICAL);
    this.setPadding(0, 14, 0, 14);

    this.label_ = new TextView(getContext());
    this.label_.setTextSize(LABEL_TEXT_SIZE);
    this.label_.setPadding(8, 0, 8, 0);
    this.label_.setVisibility(GONE);
    this.addView(this.label_);

    this.checkbox_ = new LinearLayout(getContext());
    this.checkbox_.setOrientation(VERTICAL);
    this.addView(this.checkbox_);

    this.hintText_ = new TextView(getContext());
    this.hintText_.setTextSize(HINT_TEXT_SIZE);
    this.hintText_.setPadding(8, 0, 8, 0);
    this.hintText_.setTypeface(this.hintText_.getTypeface(), Typeface.ITALIC);
    this.hintText_.setVisibility(GONE);
    this.addView(this.hintText_);

    this.errorText_ = new TextView(getContext());
    this.errorText_.setTextSize(ERROR_TEXT_SIZE);
    this.errorText_.setPadding(8, 0, 8, 0);
    this.errorText_.setTextColor(ContextCompat.getColor(getContext(), R.color.major_red));
    this.errorText_.setVisibility(GONE);
    this.addView(this.errorText_);
  }

  /**
   * Perform the ui updates on the main thread.
   */
  protected void PostUpdate() {
    (new Handler(getContext().getMainLooper())).post(new Runnable() {
      @Override
      public void run() {
        Checkbox.this.SetLabel(Checkbox.this.labelString_);
        for (Map.Entry<String, android.widget.CheckBox> entry : Checkbox.this.items_.entrySet()) {
          Checkbox.this.checkbox_.addView(entry.getValue());
        }
        Checkbox.this.SetHint(Checkbox.this.hintString_);
      }
    });
  }

  /**
   * This function stores the data required to rebuild the Checkbox IField
   * we store the Json Configuration along with the currently selected value
   *
   * @return IField.SavedState
   */
  @Override
  protected Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    SavedState ss = new SavedState(superState);
//    TODO set values
//    String value = this.GetValue();
//    ss.SaveValue(value);
    if (this.config_ != null) {
//      try {
//        this.config_.remove("value");
//        this.config_.put("value", value);
//      } catch (JSONException e) {
//        e.printStackTrace();
//      }
      ss.SaveJsonConfig(this.config_.toString());
    }
    return ss;
  }

  @Override
  protected void onRestoreInstanceState(Parcelable state) {
    SavedState ss = (SavedState) state;
    super.onRestoreInstanceState(ss.getSuperState());
  }
}
