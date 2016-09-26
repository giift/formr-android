package com.giift.formr.field;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giift.formr.Form;
import com.giift.formr.IField;
import com.giift.formr.R;
import com.giift.formr.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author vieony on 3/8/2016.
 */
public class TextArea extends LinearLayout implements IField, TextInputEditText.OnFocusChangeListener {

  protected boolean mandatory_ = false;
  protected boolean readonly_ = false;
  protected JSONObject config_;
  protected String id_ = null;
  protected String label_ = null;
  protected String value_ = null;
  private Uri onValueChanged_ = null;
  private boolean showError_ = false;
  private TextView hintText_ = null;

  protected TextWatcher formrTextWatcher = new TextWatcher() {
    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      TextArea.this.SetError(null);
      if (onValueChanged_ != null) {
        ViewParent parent = getParent();
        if (parent instanceof Form) {
          ((Form) parent).OnUpdateRequest(GetFieldId(), onValueChanged_);
        }
      }
    }
  };
  private Uri onFocusLost_ = null;

  public TextArea(Context context) {
    super(context);
    this.Init();
  }

  public TextArea(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IField);
    if (a != null) {
      this.label_ = a.getString(R.styleable.IField_label);
      a.recycle();
    }
    this.Init();
  }

  public TextArea(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public TextArea(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  @Override
  public void Init(@NonNull JSONObject config) {
    this.config_ = config;
    this.label_ = config.optString("label", "");
    this.value_ = config.optString("value", "");
    this.mandatory_ = config.optBoolean("mandatory", false);
    this.id_ = config.optString("id", null);
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
    return true;
  }

  /**
   * This function is used to set a text label for the TexArea Field
   * If the input field is empty and no focus the label will be displayed inline within the text input
   * If the Text field is in focus or it has text set the label will be displayed above the text input
   *
   * @param label string to be displayed
   */
  @Override
  public void SetLabel(String label) {
    TextInputLayout textInputLayout = this.GetTextInputLayout();
    if (textInputLayout != null) {
      textInputLayout.setHint(label);
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
    TextInputLayout textInputLayout = this.GetTextInputLayout();
    if (textInputLayout != null) {
      if (!TextUtils.isEmpty(error)) {
        this.showError_ = true;
      } else {
        this.showError_ = false;
        textInputLayout.setErrorEnabled(false);
      }
      textInputLayout.setError(error);
      this.SetEditTextLineColor();
    }
  }

  /**
   * This function is used to display a hint for the Text field
   * The hint is displayed below the error (if present)
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
  public void onFocusChange(View v, boolean hasFocus) {
    this.SetEditTextLineColor();
    if (!hasFocus && this.onFocusLost_ != null) {
      ViewParent parent = this.getParent();
      if (parent instanceof Form) {
        ((Form) parent).OnUpdateRequest(this.GetFieldId(), onFocusLost_);
      }
    }
  }

  /**
   * This function stores the data required to rebuild the TextArea IField
   * we store the value entered by the user
   * if the field was built using Json Configuration we store the json string with the value
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
        JSONObject settings = this.SaveSettings();
        if (settings != null) {
          this.config_.put("settings", settings);
        }
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
    String value = ss.GetValue();
    if (!TextUtils.isEmpty(value)) {
      this.SetText(ss.GetValue());
    }
    super.onRestoreInstanceState(ss.getSuperState());
  }

  /**
   * Perform the ui updates on the main thread.
   */
  protected void PostUpdate() {
    (new Handler(getContext().getMainLooper())).post(new Runnable() {
      @Override
      public void run() {
        if (!TextUtils.isEmpty(TextArea.this.label_) && !TextArea.this.label_.equals("null")) {
          TextArea.this.SetLabel(TextArea.this.label_);
        }
        if (!TextUtils.isEmpty(TextArea.this.value_) && !TextArea.this.value_.equals("null")) {
          TextArea.this.SetText(TextArea.this.value_);
        }
        if (TextArea.this.readonly_) {
          TextArea.this.SetReadOnly();
        }
      }
    });

  }

  protected void SetText(String text) {
    TextInputEditText editText = this.GetEditText();
    if (editText != null) {
      editText.setText(text);
    }
  }

  protected void SetReadOnly() {
    TextInputEditText editText = this.GetEditText();
    if (editText != null) {
      editText.setEnabled(false);
    }
  }

  public String GetValue() {
    String res = "";
    TextInputEditText editText = this.GetEditText();
    if (editText != null) {
      res = editText.getText().toString();
    }
    return res;
  }

  @UiThread
  private void Init() {
    this.setSaveEnabled(true);
    this.setOrientation(VERTICAL);
    this.setPadding(0, 14, 0, 14);

    TextInputLayout textInputLayout = new TextInputLayout(getContext());
    TextInputEditText editText = new TextInputEditText(getContext());
    editText.setTextSize(LABEL_TEXT_SIZE);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    this.setLayoutParams(lp);
    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    editText.setSingleLine(false);
    editText.setMinLines(3);
    editText.setGravity(Gravity.TOP);
    editText.setVerticalScrollBarEnabled(true);
    editText.setOnFocusChangeListener(this);
    editText.addTextChangedListener(formrTextWatcher);
    textInputLayout.addView(editText);
    textInputLayout.setHintTextAppearance(R.style.FormR_Text_Hint);
    this.addView(textInputLayout);
    SetLabel(this.label_);

    this.hintText_ = new TextView(getContext());
    this.hintText_.setTextSize(HINT_TEXT_SIZE);
    this.hintText_.setPadding(8, 0, 8, 0);
    this.hintText_.setTypeface(this.hintText_.getTypeface(), Typeface.ITALIC);
    this.hintText_.setVisibility(GONE);
    this.addView(this.hintText_);
  }

  private
  @Nullable
  TextInputLayout GetTextInputLayout() {
    TextInputLayout textInputLayout = null;
    for (int i = 0; i < this.getChildCount(); i++) {
      View v = this.getChildAt(i);
      if (v instanceof TextInputLayout) {
        textInputLayout = (TextInputLayout) v;
      }
    }
    return textInputLayout;
  }

  /**
   * Get the EditText element from Text field
   *
   * @return EditText
   */
  private
  @Nullable
  TextInputEditText GetEditText() {
    TextInputEditText editText = null;
    TextInputLayout textInputLayout = this.GetTextInputLayout();
    if (textInputLayout != null) {
      View child = textInputLayout.getChildAt(0);
      if (child instanceof FrameLayout) {
        child = ((FrameLayout) child).getChildAt(0);
        if(child instanceof TextInputEditText){
          editText = (TextInputEditText) child;
        }
      }
    }
    return editText;
  }

  private CharSequence GetError() {
    CharSequence error = null;
    TextInputLayout textInputLayout = this.GetTextInputLayout();
    if (textInputLayout != null) {
      error = textInputLayout.getError();
    }
    return error;
  }

  /**
   * For devices with sdk version below LOLLIPOP
   * there is a problem in setting the color of the line below the TextInputEditText
   * this function sets the following
   * color - red : when an error has occurred
   * color - grey : when no error and no focus
   * color - accentColor no error and has focus
   */
  private void SetEditTextLineColor() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      TextInputEditText editText = this.GetEditText();
      if (editText != null) {
        int color;
        if (this.showError_) {
          color = ContextCompat.getColor(getContext(), R.color.text_color_error);
        } else {
          boolean hasFocus = editText.hasFocus();
          if (hasFocus) {
            color = Utils.GetAccentColor(getContext());
          } else {
            color = ContextCompat.getColor(getContext(), R.color.text_color_normal);
          }
        }
        Drawable wrappedDrawable = DrawableCompat.wrap(editText.getBackground());
        DrawableCompat.setTint(wrappedDrawable.mutate(), color);
        editText.setBackgroundDrawable(wrappedDrawable);

      }
    }
  }

  /**
   * Here we check if error is present in the config Json ; if so we replace with current error
   * if error not present we try to build the error
   *
   * @return JsonObject with updated error
   * @throws JSONException
   */
  private JSONObject SaveSettings() throws JSONException {
    JSONObject setting = this.config_.optJSONObject("settings");
    JSONObject error;
    if (setting != null) {
      error = setting.optJSONObject("error");
      if (error != null) {
        error.remove("label");
      } else {
        error = new JSONObject();
      }
    } else {
      setting = new JSONObject();
      error = new JSONObject();
    }
    error.put("label", this.GetError());
    setting.put("error", error);
    return setting;
  }
}