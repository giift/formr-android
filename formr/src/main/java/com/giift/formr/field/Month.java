package com.giift.formr.field;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.giift.formr.Form;
import com.giift.formr.IField;
import com.giift.formr.R;
import com.giift.formr.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;

/**
 * @author Nicolas
 */
public class Month extends LinearLayout implements IField,
    Spinner.OnItemSelectedListener, Spinner.OnTouchListener, Spinner.OnFocusChangeListener {

  protected JSONObject config_;
  protected String id_ = null;
  private Uri onValueChanged_ = null;
  private Uri onFocusLost_ = null;
  private TextView label_ = null;
  private Spinner spinner_ = null;
  private TextView hintText_ = null;
  private TextView errorText_ = null;
  private ImageView errorIcon_ = null;
  private boolean readonly_ = false;

  public Month(Context context) {
    super(context);
    this.Init();
  }

  public Month(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.Init();
  }

  public Month(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Month(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  @Override
  public void Init(@NonNull JSONObject config) {
    this.setOrientation(VERTICAL);
    this.config_ = config;
    this.id_ = config.optString("id", null);
    int initKey = config.optInt("value", 0);
    String label = config.optString("label");
    this.SetLabel(label);

    this.onValueChanged_ = null;
    this.onFocusLost_ = null;

    if (this.spinner_.getAdapter() != null) {
      initKey--;
      this.spinner_.setSelection(initKey);
    }
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
   * This function is used to set a text label above the Month field
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
   * This function is used to display error message in red text at the end of the field
   * if error message is null the error view is hidden
   *
   * @param error error message
   */
  @Override
  public void SetError(@Nullable String error) {
    boolean showError = false;
    if (!TextUtils.isEmpty(error)) {
      showError = true;
    }
    if (this.errorText_ != null) {
      this.errorText_.setText(error);
      if (showError) {
        this.errorText_.setVisibility(VISIBLE);
      } else {
        this.errorText_.setVisibility(GONE);
      }
    }
    if (this.errorIcon_ != null) {
      if (showError) {
        this.errorIcon_.setVisibility(VISIBLE);
      } else {
        this.errorIcon_.setVisibility(GONE);
      }
    }
  }

  /**
   * This function is used to display a hint for the Month field
   * The hint is displayed below the month options and above the error (if present)
   * hint is displayed with the Typeface italics
   *
   * @param hint text to be displayed
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
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_UP && !v.hasFocus()) {
      v.performClick();
    }
    return false;
  }

  @Override
  public void onFocusChange(View v, boolean hasFocus) {
    if (v == this.spinner_) {
      this.SetSpinnerBackgroundColor(hasFocus);
    }
  }

  /**
   * This function stores the data required to rebuild the Month IField
   * we store the Json Configuration along with the currently selected value
   *
   * @return IField.SavedState
   */
  @Override
  public Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    SavedState ss = new SavedState(superState);
    String value = this.GetValue();
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
    SavedState ss = (SavedState) state;
    super.onRestoreInstanceState(ss.getSuperState());
  }

  /**
   * Perform the ui updates on the main thread.
   */
  protected void PostUpdate() {
    (new Handler(getContext().getMainLooper())).post(new Runnable() {
      @Override
      public void run() {
        if (Month.this.readonly_) {
          Month.this.spinner_.setEnabled(false);
        }
      }
    });
  }

  @UiThread
  protected void Init() {
    this.setSaveEnabled(true);
    this.setOrientation(VERTICAL);
    this.setPadding(0, 14, 0, 14);

    this.label_ = new TextView(getContext());
    this.label_.setTextSize(LABEL_TEXT_SIZE);
    this.label_.setPadding(8, 0, 8, 0);
    this.label_.setVisibility(GONE);
    this.addView(this.label_);

    LinearLayout layout = new LinearLayout(getContext());
    layout.setGravity(Gravity.CENTER_VERTICAL);
    layout.setOrientation(HORIZONTAL);
    LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);

    this.spinner_ = new Spinner(new ContextThemeWrapper(getContext(), R.style.FormR_Spinner), Spinner.MODE_DIALOG);
    this.spinner_.setLayoutParams(params);
    this.SetSpinnerBackgroundColor(false);
    this.spinner_.setFocusable(true);
    this.spinner_.setFocusableInTouchMode(true);
    this.spinner_.setOnTouchListener(this);
    this.spinner_.setOnFocusChangeListener(this);
    String[] months = DateFormatSymbols.getInstance().getMonths();
    BaseAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, months);
    this.spinner_.setAdapter(adapter);
    layout.addView(this.spinner_);

    this.errorIcon_ = new ImageView(getContext());
    this.errorIcon_.setImageResource(R.drawable.ic_error_24dp);
    this.errorIcon_.setColorFilter(ContextCompat.getColor(getContext(), R.color.major_red));
    this.errorIcon_.setVisibility(GONE);
    layout.addView(this.errorIcon_);

    this.addView(layout);

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

    // call made on post so that when adapter is set OnItemSelectedListener will not be called
    this.post(new Runnable() {
      public void run() {
        Month.this.spinner_.setOnItemSelectedListener(Month.this);
      }
    });
  }

  private String GetValue() {
    return Integer.toString(this.spinner_.getSelectedItemPosition() + 1);
  }

  /**
   * For devices with sdk version below LOLLIPOP
   * the colors are not set correctly
   * so we have to set it here accordingly
   */
  private void SetSpinnerBackgroundColor(boolean hasFocus) {
    int color;
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      if (hasFocus) {
        color = Utils.GetAccentColor(getContext());
      } else {
        color = Utils.GetColorControlNormal(getContext());
      }
      this.spinner_.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }
  }
}
