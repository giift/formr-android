package com.giift.formr.field;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatImageButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giift.formr.DatePickerFragment;
import com.giift.formr.Form;
import com.giift.formr.IField;
import com.giift.formr.R;
import com.giift.formr.utils.Culture;
import com.giift.formr.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Field that allows the input of a date. It leverages the native date picker.
 *
 * @author Nicolas
 */
public class Date extends LinearLayout implements IField,
    View.OnClickListener, DatePickerFragment.DatePickerListener {

  private static final String LOG_TAG = Date.class.getName();
  public static final String DATE_FORMAT = "yyyy-MM-dd";

  private java.util.Date date_ = null;
  private java.util.Date startDate_ = null;
  private java.util.Date endDate_ = null;
  private TextView label_ = null;
  private EditText field_ = null;
  private AppCompatImageButton calendar_ = null;
  private TextView hintText_ = null;
  private TextView errorText_ = null;
  private boolean mandatory_ = false;
  private boolean readonly_ = false;
  private String id_ = null;
  private Uri onValueChanged_ = null;
  private Uri onFocusLost_ = null;
  private String labelStr_ = null;
  private JSONObject config_ = null;

  public Date(Context context) {
    super(context);
    this.Init();
  }

  public Date(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IField);
    if (a != null) {
      this.labelStr_ = a.getString(R.styleable.IField_label);
      a.recycle();
    }
    this.Init();
  }

  public Date(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Date(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  @Override
  public void onClick(View v) {
    Calendar c = null;
    if (this.date_ != null) {
      c = Calendar.getInstance();
      c.setTime(this.date_);
    }
    this.ShowDatePickerDialog(c);
  }

  @Override
  public void Init(@NonNull JSONObject config) {
    this.config_ = config;
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    //value
    String value = config.optString("value");
    if (!TextUtils.isEmpty(value)) {
      try {
        this.date_ = dateFormat.parse(value);
      } catch (ParseException e) {
        this.date_ = null;
        Log.w(LOG_TAG, String.format("Trying to parse a date :'%s'", value), e);
      }
    } else {
      this.date_ = null;
    }

    JSONObject settings = config.optJSONObject("settings");
    if (settings != null) {
      this.readonly_ = settings.optBoolean("readonly", false);
      //start date
      String startDate = settings.optString("start_date");
      if (!TextUtils.isEmpty(startDate)) {
        try {
          this.startDate_ = dateFormat.parse(startDate);
        } catch (ParseException e) {
          this.startDate_ = null;
          Log.w(LOG_TAG, String.format("Trying to parse a date :'%s'", value), e);
        }
      } else {
        this.startDate_ = null;
      }

      //end date
      String endDate = settings.optString("end_date");
      if (!TextUtils.isEmpty(endDate)) {
        try {
          this.endDate_ = dateFormat.parse(endDate);
        } catch (ParseException e) {
          this.endDate_ = null;
          Log.w(LOG_TAG, String.format("Trying to parse a date :'%s'", value), e);
        }
      } else {
        this.endDate_ = null;
      }

      JSONObject callbacks = settings.optJSONObject("callback");
      if (callbacks != null) {
        String onValueChanged = callbacks.optString("on_value_changed");
        if (!TextUtils.isEmpty(onValueChanged)) {
          this.onValueChanged_ = Uri.parse(onValueChanged);
        } else {
          this.onValueChanged_ = null;
        }
        String onFocusLost = callbacks.optString("on_focus_lost");
        if (!TextUtils.isEmpty(onFocusLost)) {
          this.onFocusLost_ = Uri.parse(onFocusLost);
        } else {
          this.onFocusLost_ = null;
        }
      } else {
        this.onValueChanged_ = null;
        this.onFocusLost_ = null;
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
    this.labelStr_ = config.optString("label", "");
    this.mandatory_ = config.optBoolean("mandatory", false);
    this.id_ = config.optString("id", null);
    this.PostUpdate();
  }

  @Override
  public String GetFieldId() {
    return this.id_;
  }

  @Override
  public boolean Validate() {
    boolean res = true;
    if (this.mandatory_) {
      if (this.date_ == null) {
        this.SetError(getResources().getString(R.string.error_field_required));
        res = false;
      }
    }
    return res;
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
   * This function is used to set a text label above the Date field
   *
   * @param label string to be displayed
   */
  @Override
  public void SetLabel(String label) {
    if (!TextUtils.isEmpty(label) && !label.equals("null")) {
      this.label_.setText(label);
      this.label_.setVisibility(VISIBLE);
    } else {
      this.label_.setVisibility(GONE);
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
    this.field_.setError(error, ic_error);
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
   * This function is used to display a hint for the Date field
   * The hint is displayed below the date field and above the error (if present)
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
  public void OnDateSelected(java.util.Date date) {
    SetDate(date);
    if (this.onFocusLost_ != null) {
      ViewParent parent = this.getParent();
      if (parent instanceof Form) {
        ((Form) parent).OnUpdateRequest(this.GetFieldId(), onFocusLost_);
      }
    }
    if (this.onValueChanged_ != null) {
      ViewParent parent = this.getParent();
      if (parent instanceof Form) {
        ((Form) parent).OnUpdateRequest(this.GetFieldId(), onValueChanged_);
      }
    }
  }

  public void SetDate(java.util.Date date) {
    this.date_ = date;
    this.SetError(null);
    this.field_.setError(null);
    this.field_.setText(Culture.GetPrettyDate(date));
  }

  protected void ShowDatePickerDialog(Calendar c) {
    Context context = this.getContext();
    Log.e(LOG_TAG, context.getClass().getName());

    if (context instanceof FragmentActivity) {
      FragmentActivity app = (FragmentActivity) context;

      if (c == null) {
        c = Calendar.getInstance();
      }

      Bundle args = new Bundle();
      args.putInt(DatePickerFragment.YEAR, c.get(Calendar.YEAR));
      args.putInt(DatePickerFragment.MONTH, c.get(Calendar.MONTH));
      args.putInt(DatePickerFragment.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
      if (startDate_ != null) {
        args.putLong(DatePickerFragment.START_DATE, startDate_.getTime());
      }
      if (endDate_ != null) {
        args.putLong(DatePickerFragment.END_DATE, endDate_.getTime());
      }

      DatePickerFragment newFragment = new DatePickerFragment();
      newFragment.setArguments(args);
      newFragment.SetListener(this);
//      newFragment.setTargetFragment(app, 0);
      newFragment.show(app.getSupportFragmentManager(), "datePicker");
    }
  }

  @UiThread
  protected void Init() {
    this.setSaveEnabled(true);
    this.setOrientation(VERTICAL);
    this.setPadding(0, 14, 0, 14);

    this.label_ = new TextView(getContext());
    this.label_.setTextSize(LABEL_TEXT_SIZE);
    this.label_.setPadding(8, 0, 8, 0);
    SetLabel(this.labelStr_);
    this.addView(this.label_);

    LinearLayout layout = new LinearLayout(getContext());
    layout.setOrientation(HORIZONTAL);

    this.field_ = new EditText(getContext());
    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
        0,//width
        LayoutParams.WRAP_CONTENT,//height
        1.0f);//weight
    this.field_.setLayoutParams(param);
    this.field_.setFocusable(false);
    this.field_.setOnClickListener(this);
    layout.addView(this.field_);

    this.calendar_ = new AppCompatImageButton(getContext());
    this.calendar_.setImageResource(R.drawable.ic_calendar_24dp);
    this.calendar_.setColorFilter(Utils.GetAccentColor(getContext()));
    LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT,//width
        LayoutParams.WRAP_CONTENT);//height
    param2.gravity = Gravity.CENTER;
    this.calendar_.setLayoutParams(param2);
    layout.addView(this.calendar_);
    this.calendar_.setBackgroundColor(Color.TRANSPARENT);
    this.calendar_.setOnClickListener(this);

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
  }

  /**
   * Perform the ui updates on the main thread.
   */
  protected void PostUpdate() {
    (new Handler(getContext().getMainLooper())).post(new Runnable() {
      @Override
      public void run() {
        Date.this.SetLabel(Date.this.labelStr_);
        if (Date.this.date_ != null) {
          Date.this.field_.setText(Culture.GetPrettyDate(Date.this.date_));
        } else {
          Date.this.field_.setText("");
        }
        if (Date.this.readonly_) {
          Date.this.field_.setEnabled(false);
          Date.this.calendar_.setEnabled(false);
          Date.this.calendar_.getDrawable().setAlpha(126);// if the button is readonly (disabled) set 50% transparency
        }
      }

    });
  }

  /**
   * This function stores the data required to rebuild the Date IField
   * we store the Json Configuration along with the currently selected value
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

  public String GetValue() {
    if (this.date_ != null) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
      return simpleDateFormat.format(this.date_);
    } else {
      return "";
    }
  }
  public java.util.Date GetDate() {
    return this.date_;
  }

}
