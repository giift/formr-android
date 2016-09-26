package com.giift.formr.field;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.giift.formr.Form;
import com.giift.formr.IField;
import com.giift.formr.R;
import com.giift.formr.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author vieony on 3/3/2016.
 */
public class ButtonChoice extends LinearLayout implements IField,
    RadioGroup.OnCheckedChangeListener {

  protected String id_ = null;
  private TextView label_ = null;
  private RadioGroup radioGroup_ = null;
  private TextView hintText_ = null;
  private TextView errorText_ = null;
  private ImageView errorIcon_ = null;
  private ArrayList<String> keys_ = null;
  private JSONObject config_ = null;
  private boolean mandatory_ = false;
  private String selectedValue_ = null;
  private OnCheckedChangeListener onCheckedChangeListener_ = null;
  private int textSize_ = 24;
  private int hintPaddingBottom_ = 0;

  private String labelStr_ = null;

  /**
   * Called when the checked radio button has selection changed
   */
  public interface OnCheckedChangeListener {
    void onCheckedChanged(String value);
  }

  public ButtonChoice(Context context) {
    super(context);
    this.Init();
  }

  public ButtonChoice(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IField);
    if (a != null) {
      this.labelStr_ = a.getString(R.styleable.IField_label);
      this.hintPaddingBottom_ = a.getDimensionPixelSize(R.styleable.IField_hintPaddingBottom, 0);
      a.recycle();
    }
    this.Init();
  }

  public ButtonChoice(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ButtonChoice(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  @Override
  public void Init(@NonNull JSONObject config) {
    this.config_ = config;
    this.id_ = config.optString("id", null);
    this.mandatory_ = config.optBoolean("mandatory", false);
    this.SetLabel(config.optString("label", null));
    String defaultValue = config.optString("value");
    boolean selected = false;
    JSONObject settings = config.optJSONObject("settings");
    if (settings != null) {
      boolean readOnly = settings.optBoolean("readonly", false);
      JSONObject options = settings.optJSONObject("options");
      if (options != null) {
        this.keys_ = new ArrayList<>();
        Iterator<String> it = options.keys();
        while (it.hasNext()) {
          String key = it.next();
          this.keys_.add(key);
          String value = options.optString(key, "");
          AppCompatRadioButton tmp = BuildRadioButton(value);
          this.radioGroup_.addView(tmp);
          if (readOnly) {
            tmp.setClickable(false);
            tmp.getBackground().setAlpha(126); // if the button is readonly (disabled) set 50% transparency
          }
          if (!TextUtils.isEmpty(defaultValue) && key.equals(defaultValue)) {
            this.radioGroup_.check(tmp.getId());
            selected = true;
          }
        }
        if (!selected && this.radioGroup_.getChildCount() > 0) {
          ((AppCompatRadioButton) this.radioGroup_.getChildAt(0)).setChecked(true);
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
  }

  @Override
  public String GetFieldId() {
    return this.id_;
  }

  @Override
  public boolean Validate() {
    if (this.mandatory_ && TextUtils.isEmpty(GetValue())) {
      this.SetError(getResources().getString(R.string.error_field_required));
      return false;
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
   * This function is used to set a text label above the ButtonChoice field
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
   * This function is used to display a hint for the ButtonChoice field
   * The hint is displayed below the choice buttons and above the error (if present)
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
  public void onCheckedChanged(RadioGroup group, int checkedId) {
    this.SetError(null);
    this.SetButtonBackgroundColor(group, checkedId);
    if (this.onCheckedChangeListener_ != null) {
      this.onCheckedChangeListener_.onCheckedChanged(GetValue());
    }
  }

  /**
   * this function adds HashMap entries as radio buttons to the radio group
   *
   * @param options key, value pair
   */
  public void InitOptionsArray(@NonNull HashMap<String, String> options) {
    this.keys_ = new ArrayList<>();
    this.radioGroup_.removeAllViews();
    for (Map.Entry<String, String> entry : options.entrySet()) {
      String key = entry.getKey();
      this.keys_.add(key);
      AppCompatRadioButton button = BuildRadioButton(entry.getValue());
      this.radioGroup_.addView(button);
      if (this.selectedValue_ != null && this.selectedValue_.equals(key)) {
        this.radioGroup_.check(button.getId());
      }
    }
  }

  /**
   * this function adds ArrayList<Pair<String, String>> entries as radio buttons to the radio group
   * this function maintains the order as a array list is used
   * the first value of the pair is the key
   * the second value is the option displayed on the radio button
   *
   * @param options key, value pair
   */
  public void InitOptionsArray(@NonNull ArrayList<Pair<String, String>> options) {
    this.keys_ = new ArrayList<>();
    this.radioGroup_.removeAllViews();
    for (Pair<String, String> entry : options) {
      String key = entry.first;
      this.keys_.add(key);
      AppCompatRadioButton button = BuildRadioButton(entry.second);
      this.radioGroup_.addView(button);
      if (this.selectedValue_ != null && this.selectedValue_.equals(key)) {
        this.radioGroup_.check(button.getId());
      }
    }
  }

  /**
   * Sets font size for the button text
   *
   * @param size font size
   */
  public void SetTextSize(int size) {
    this.textSize_ = size;
  }

  /**
   * Clears selection of radio button
   */
  public void Reset() {
    this.selectedValue_ = null;
    this.radioGroup_.clearCheck();
  }

  /**
   * This function is used by parent view to set the OnCheckedChangeListener;  so that it can
   * receive callback when the button choice option selection changes
   *
   * @param listener listener
   */
  public void SetCheckedChangeListener(OnCheckedChangeListener listener) {
    this.onCheckedChangeListener_ = listener;
  }

  /**
   * Allows to select option at a position in the options
   *
   * @param position the position to be selected
   */
  public void SetSelectedPosition(int position) {
    if (this.radioGroup_ != null) {
      int count = this.radioGroup_.getChildCount();
      if (count > 0 && position < count) {
        ((AppCompatRadioButton) this.radioGroup_.getChildAt(position)).setChecked(true);
      }
    }
  }

  /**
   * This function stores the data required to rebuild the ButtonChoice IField
   * we store the Json Configuration along with the currently selected value
   *
   * @return IField.SavedState
   */
  @Override
  protected Parcelable onSaveInstanceState() {
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
  protected void onRestoreInstanceState(Parcelable state) {
    SavedState ss = (SavedState) state;
    this.selectedValue_ = ss.GetValue();
    super.onRestoreInstanceState(ss.getSuperState());
  }

  @Override
  protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
    dispatchFreezeSelfOnly(container);
  }

  @Override
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
    dispatchThawSelfOnly(container);
  }

  /**
   * Function sets style for button
   *
   * @param button the button for which style must be set
   * @param style  style
   */
  @SuppressWarnings("deprecation")
  private void SetButtonTextStyle(@NonNull AppCompatRadioButton button, int style) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      button.setTextAppearance(getContext(), style);
    } else {
      button.setTextAppearance(style);
    }
    button.setTextSize(this.textSize_);
  }

  /**
   * For devices with sdk version below LOLLIPOP
   * we cannot use accent color in shape drawable;
   * so we have to set it here accordingly
   */
  private void SetButtonBackgroundColor(@NonNull RadioGroup group, int checkedId) {
    int color;
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      for (int i = 0; i < group.getChildCount(); i++) {
        AppCompatRadioButton b = (AppCompatRadioButton) group.getChildAt(i);
        if (b.getId() == checkedId) {
          color = Utils.GetAccentColor(getContext());
        } else {
          color = ContextCompat.getColor(getContext(), R.color.button_choice_background);
        }
        Drawable wrappedDrawable = DrawableCompat.wrap(b.getBackground());
        DrawableCompat.setTint(wrappedDrawable.mutate(), color);
        b.setBackground(wrappedDrawable);
      }
    }
  }

  private String GetValue() {
    String value = "";
    //get the position of the selected AppCompatRadioButton
    int d = this.radioGroup_.
        indexOfChild(this.radioGroup_.findViewById(this.radioGroup_.getCheckedRadioButtonId()));
    if (this.keys_ != null && d != -1 && this.keys_.size() > d) {
      value = this.keys_.get(d);
    }
    return value;
  }

  @UiThread
  private void Init() {
    this.setSaveEnabled(true);
    this.setOrientation(VERTICAL);
    this.setPadding(0, 14, 0, 14);

    this.label_ = new TextView(getContext());
    this.label_.setTextSize(LABEL_TEXT_SIZE);
    this.label_.setPadding(8, 0, 8, this.hintPaddingBottom_);
    this.SetLabel(this.labelStr_);
    this.addView(this.label_);

    LinearLayout layout = new LinearLayout(getContext());
    layout.setOrientation(HORIZONTAL);
    this.radioGroup_ = new RadioGroup(getContext());
    RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
    layoutParams.setMargins(0, 10, 0, 10);
    this.radioGroup_.setLayoutParams(layoutParams);
    this.radioGroup_.setOrientation(HORIZONTAL);
    this.radioGroup_.setOnCheckedChangeListener(this);
    layout.addView(this.radioGroup_);

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

  /**
   * creates a radio button with the given value
   *
   * @param value text to be displayed
   * @return AppCompatRadioButton
   */
  private AppCompatRadioButton BuildRadioButton(@NonNull String value) {
    AppCompatRadioButton button = new AppCompatRadioButton(getContext());
    button.setId(Form.GenerateViewId());
    button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_choice));
    button.setButtonDrawable(android.R.color.transparent);
    button.setButtonDrawable(null);
    button.setPadding(0, 16, 16, 16);
    button.setText(value);
    RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
    layoutParams.setMargins(8, 0, 8, 0);
    button.setLayoutParams(layoutParams);
    button.setGravity(Gravity.CENTER);
    this.SetButtonTextStyle(button, R.style.FormR_ButtonChoice);
    return button;
  }

}