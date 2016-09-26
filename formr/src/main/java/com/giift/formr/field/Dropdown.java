package com.giift.formr.field;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.giift.formr.Form;
import com.giift.formr.IField;
import com.giift.formr.R;
import com.giift.formr.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Nicolas
 */
public class Dropdown extends LinearLayout implements IField,
    Spinner.OnTouchListener, Spinner.OnItemSelectedListener, Spinner.OnFocusChangeListener {

  protected JSONObject config_;
  private String id_;
  private ArrayList<String> keys_ = null;
  private TextView label_ = null;
  private Spinner spinner_ = null;
  private TextView hintText_ = null;
  private TextView errorText_ = null;
  private ImageView errorIcon_ = null;
  private String labelString_ = null;
  private SpinnerAdapter adapter_ = null;
  private Uri onValueChanged_ = null;
  private Uri onFocusLost_ = null;
  private int initSelection_ = -1;
  private boolean readonly_ = false;
  private List<OnItemSelectedListener> listeners_ = new ArrayList<>();

  public interface OnItemSelectedListener {
    void OnItemSelected(AdapterView<?> parent, View view, int position, long id);

    void OnNothingSelected(AdapterView<?> parent);

  }

  public Dropdown(Context context) {
    super(context);
    this.Init();
  }

  public Dropdown(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.Init();
  }

  public Dropdown(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Dropdown(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  @Override
  public void Init(@NonNull JSONObject config) {
    this.config_ = config;
    this.id_ = config.optString("id", null);
    this.labelString_ = config.optString("label", "");
    String initKey = config.optString("value", "");
    this.initSelection_ = -1;
    this.onValueChanged_ = null;
    this.onFocusLost_ = null;
    this.adapter_ = null;

    JSONObject settings = config.optJSONObject("settings");
    if (settings != null) {
      this.readonly_ = settings.optBoolean("readonly", false);

      JSONObject options = settings.optJSONObject("options");
      if (options != null) {
        this.keys_ = new ArrayList<>();
        int select = 0;
        ArrayList<String> values = new ArrayList<>();
        Iterator<String> it = options.keys();
        while (it.hasNext()) {
          String key = it.next();
          String value = options.optString(key, "");
          keys_.add(key);
          values.add(value);
          if (!TextUtils.isEmpty(initKey) && initKey.equals(key)) {
            this.initSelection_ = select;
          }
          select++;
        }
        this.adapter_ = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, values);
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
   * This function is used to set a text label above the Dropdown field
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
   * This function is used to display a hint for the Dropdown field
   * The hint is displayed below the Dropdown spinner and above the error (if present)
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
  public boolean onTouch(View v, MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_UP && !v.hasFocus()) {
      v.performClick();
    }
    return false;
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
    for (OnItemSelectedListener list : this.listeners_) {
      list.OnItemSelected(parent, view, position, id);
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    Log.v("LOg", "nothing");
    for (OnItemSelectedListener list : this.listeners_) {
      list.OnNothingSelected(parent);
    }
  }

  @Override
  public void onFocusChange(View v, boolean hasFocus) {
    if (v == this.spinner_) {
      this.SetSpinnerBackgroundColor(hasFocus);
    }
  }

  public void SetAdapter(SpinnerAdapter adapter) {
    if (adapter != null) {
      this.spinner_.setAdapter(adapter);
    } else {
      //TODO reset the spinner
    }
  }

  public void SetSelection(int position) {
    SpinnerAdapter adapter = this.spinner_.getAdapter();
    if (adapter != null && position >= 0 && position < adapter.getCount()) {
      this.spinner_.setSelection(position);
    }
  }

  public int GetSelectedItemPosition() {
    return this.spinner_.getSelectedItemPosition();
  }

  public void SetOnItemSelectedListener(OnItemSelectedListener listener) {
    this.listeners_.add(listener);
    this.spinner_.setOnItemSelectedListener(this);
  }

  /**
   * Ui updates should be done on the main thread. So, we leave this method responsible for it.
   */
  @CallSuper
  protected void PostUpdate() {
    (new Handler(getContext().getMainLooper())).post(new Runnable() {
      @Override
      public void run() {
        Dropdown.this.SetLabel(Dropdown.this.labelString_);
        Dropdown.this.SetAdapter(Dropdown.this.adapter_);
        post(new Runnable() {
          public void run() {
            Dropdown.this.spinner_.setOnItemSelectedListener(Dropdown.this);
          }
        });

        Dropdown.this.SetSelection(Dropdown.this.initSelection_);

        if (Dropdown.this.readonly_) {
          Dropdown.this.spinner_.setEnabled(false);
        }
      }
    });
  }

  /**
   * This function stores the data required to rebuild the Dropdown IField
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

  @UiThread
  private void Init() {
    this.setSaveEnabled(true);
    this.setOrientation(VERTICAL);
    this.setPadding(0, 10, 0, 10);

    this.label_ = new TextView(getContext());
    this.label_.setTextSize(LABEL_TEXT_SIZE);
    this.label_.setPadding(8, 0, 8, 0);
    this.label_.setVisibility(GONE);
    this.addView(this.label_);

    LinearLayout layout = new LinearLayout(getContext());
    layout.setGravity(Gravity.CENTER_VERTICAL);
    layout.setOrientation(HORIZONTAL);

    this.spinner_ = new Spinner(new ContextThemeWrapper(getContext(), R.style.FormR_Spinner), Spinner.MODE_DIALOG);
    LayoutParams params2 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
    this.spinner_.setLayoutParams(params2);
    this.spinner_.setEnabled(true);
    this.spinner_.setActivated(true);
    this.SetSpinnerBackgroundColor(false);
    layout.addView(this.spinner_);
    this.spinner_.setFocusable(true);
    this.spinner_.setFocusableInTouchMode(true);
    this.spinner_.setOnTouchListener(this);
    this.spinner_.setOnFocusChangeListener(this);

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

  }

  private String GetValue() {
    int id = (int) this.spinner_.getSelectedItemId();
    if (this.keys_ != null && this.keys_.size() > id) {
      return this.keys_.get(id);
    } else {
      return "";
    }
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
