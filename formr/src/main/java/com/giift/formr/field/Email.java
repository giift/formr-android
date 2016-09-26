package com.giift.formr.field;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giift.formr.Form;
import com.giift.formr.IField;
import com.giift.formr.R;
import com.giift.formr.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nicolas
 */
public class Email extends LinearLayout implements IField,
    AppCompatAutoCompleteTextView.OnFocusChangeListener,
    AppCompatAutoCompleteTextView.OnEditorActionListener {

  protected String id_ = null;
  protected boolean mandatory_ = false;
  private boolean readonly_ = false;
  private String label_ = null;
  private String value_ = null;
  private int textSizeXml_;
  private Uri onValueChanged_ = null;
  private boolean showError_ = false;
  private Uri onFocusLost_ = null;
  private JSONObject config_ = null;
  private TextView hintText_ = null;
  private String hint_ = null;

  private AppCompatAutoCompleteTextView emailView_ = null;
  protected TextWatcher formrTextWatcher_ = new TextWatcher() {
    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      Email.this.SetError(null);
      if (Email.this.onValueChanged_ != null) {
        ViewParent parent = getParent();
        if (parent instanceof Form) {
          ((Form) parent).OnUpdateRequest(GetFieldId(), Email.this.onValueChanged_);
        }
      }
    }
  };

  public Email(Context context) {
    super(context);
    this.Init();
  }

  @SuppressWarnings("ResourceType")
  public Email(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IField);
    if (a != null) {
      this.label_ = a.getString(R.styleable.IField_label);
      this.textSizeXml_ = a.getDimensionPixelSize(R.styleable.IField_textSize, 0);
      this.hint_ = a.getString(R.styleable.IField_giiftHint);
      a.recycle();
    }
    this.Init();
  }

  public Email(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Email(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
    boolean res = true;
    String value = this.GetValue();
    if (!TextUtils.isEmpty(value)) {
      res = Utils.IsEmailValid(value);
      if (!res) {
        this.SetError(getResources().getString(R.string.error_invalid_email));
      }
    } else if (this.mandatory_) {
      res = false;
      this.SetError(getResources().getString(R.string.error_field_required));
    }
    return res;
  }

  public void SetMandatory(boolean mandatory) {
    this.mandatory_ = mandatory;
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
   * This function is used to set a text label above the Email field if
   *
   * @param label string to be displayed
   */
  @Override
  public void SetLabel(String label) {
    TextInputLayout textInputLayout = this.GetTextInputLayout();
    if (textInputLayout != null && !TextUtils.isEmpty(label) && !label.equals("null")) {
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
   * This function is used to display a hint for the email field
   * The hint is displayed below the error (if present)
   * hint is displayed with the Typeface italics
   *
   * @param hint string to be displayed
   */
  @Override
  public void SetHint(String hint) {
    if (!TextUtils.isEmpty(hint)) {
      this.hintText_.setText(hint);
      this.hintText_.setVisibility(VISIBLE);
    } else {
      this.hintText_.setVisibility(GONE);
    }
  }

  @Override
  public void SetImeActionLabel(String label, int action) {
    this.emailView_.setImeActionLabel(label, action);
  }

  @Override
  public void onFocusChange(View v, boolean hasFocus) {
    this.SetEditTextLineColor();
    if (!hasFocus && this.onFocusLost_ != null) {
      ViewParent parent = this.getParent();
      if (parent instanceof Form) {
        ((Form) parent).OnUpdateRequest(this.GetFieldId(), this.onFocusLost_);
      }
    }
  }

  @Override
  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    switch (actionId) {
      case EditorInfo.IME_ACTION_NEXT: {
        ViewParent parent = getParent();
        if (parent instanceof Form) {
          ((Form) parent).OnNextClicked(this);
          return true;
        }
      }
      break;
      case EditorInfo.IME_ACTION_DONE: {
        ViewParent parent = getParent();
        if (parent instanceof Form) {
          return ((Form) parent).OnDoneClicked(this);
        }
      }
      break;
    }
    return false;
  }

  /**
   * This function stores the data required to rebuild the Email IField
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
      this.emailView_.setText(ss.GetValue());
    }
    super.onRestoreInstanceState(ss.getSuperState());
  }

  public void SetText(String value) {
    this.emailView_.setText(value);
  }

  public String GetValue() {
    return this.emailView_.getText().toString();
  }

  @UiThread
  protected void Init() {
    this.setSaveEnabled(true);
    this.setOrientation(VERTICAL);
    this.setPadding(0, 12, 0, 12);

    TextInputLayout textInputLayout = new TextInputLayout(getContext());
    this.emailView_ = new AppCompatAutoCompleteTextView(getContext());
    if (this.textSizeXml_ != 0) {
      emailView_.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.textSizeXml_);
    } else {
      emailView_.setTextSize(LABEL_TEXT_SIZE);
    }
    this.emailView_.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    this.emailView_.setThreshold(1);
    this.emailView_.setOnFocusChangeListener(this);
    this.emailView_.setOnEditorActionListener(this);
    textInputLayout.addView(this.emailView_);
    textInputLayout.setHintTextAppearance(R.style.FormR_Text_Hint);
    this.addView(textInputLayout);

    this.hintText_ = new TextView(getContext());
    this.hintText_.setTextSize(HINT_TEXT_SIZE);
    this.hintText_.setPadding(8, 0, 8, 0);
    this.hintText_.setTypeface(this.hintText_.getTypeface(), Typeface.ITALIC);
    SetHint(this.hint_);
    this.addView(this.hintText_);

    // set e-mail autoComplete
    Account[] accounts = AccountManager.get(getContext()).getAccounts();
    Set<String> emailSet = new HashSet<>();
    for (Account account : accounts) {
      if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
        emailSet.add(account.name);
      }
    }
    this.emailView_.setAdapter(new ArrayAdapter<>(this.getContext(),
        android.R.layout.simple_dropdown_item_1line,
        new ArrayList<>(emailSet)));
    this.emailView_.addTextChangedListener(this.formrTextWatcher_);
    this.SetLabel(this.label_);

  }

  public void SetAdapter(Set<String> emailSet) {
    this.emailView_.setAdapter(new ArrayAdapter<>(this.getContext(),
        android.R.layout.simple_dropdown_item_1line,
        new ArrayList<>(emailSet)));
  }

  /**
   * Perform the ui updates on the main thread.
   */
  protected void PostUpdate() {
    this.emailView_.removeTextChangedListener(this.formrTextWatcher_);
    (new Handler(getContext().getMainLooper())).post(new Runnable() {
      @Override
      public void run() {
        if (!TextUtils.isEmpty(Email.this.label_) && !Email.this.label_.equals("null")) {
          Email.this.SetLabel(Email.this.label_);
        } else {
          Email.this.SetLabel("");
        }
        if (!TextUtils.isEmpty(Email.this.value_) && !Email.this.value_.equals("null")) {
          Email.this.SetText(Email.this.value_);
        } else {
          Email.this.SetText("");
        }
        Email.this.emailView_.addTextChangedListener(Email.this.formrTextWatcher_);
        if (Email.this.readonly_) {
          Email.this.emailView_.setEnabled(false);
        }
      }
    });
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
      if (this.emailView_ != null) {
        int color;
        if (this.showError_) {
          color = ContextCompat.getColor(getContext(), R.color.text_color_error);
        } else {
          boolean hasFocus = this.emailView_.hasFocus();
          if (hasFocus) {
            color = Utils.GetAccentColor(getContext());
          } else {
            color = ContextCompat.getColor(getContext(), R.color.text_color_normal);
          }
        }
        Drawable wrappedDrawable = DrawableCompat.wrap(this.emailView_.getBackground());
        DrawableCompat.setTint(wrappedDrawable.mutate(), color);
        this.emailView_.setBackgroundDrawable(wrappedDrawable);

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
