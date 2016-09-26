package com.giift.formr.field;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giift.formr.Form;
import com.giift.formr.IField;
import com.giift.formr.IIntentResultListener;
import com.giift.formr.R;
import com.giift.formr.barcode.BarcodeCaptureActivity;
import com.giift.formr.utils.Utils;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Nicolas
 */
public class Text extends LinearLayout implements IField,
    TextInputEditText.OnFocusChangeListener, TextInputEditText.OnEditorActionListener,
    View.OnClickListener, IIntentResultListener {

  private static final String LOG_TAG = Text.class.getName();
  protected boolean mandatory_ = false;
  protected boolean readonly_ = false;
  protected JSONObject config_;
  protected String id_ = null;
  private String label_ = null;
  private String hint_ = null;
  private String value_ = null;
  private int textSizeXml_ = 0;
  private Uri onValueChanged_ = null;
  private boolean showError_ = false;
  private Uri onFocusLost_ = null;
  private int currentRequestCode_ = -1;
  private LinearLayout layout_ = null;
  private TextView hintText_ = null;
  private AppCompatImageButton scan_ = null;
  private boolean scannable_ = false;
  private List<OnScanListener> scanListeners_ = new ArrayList<>();

  public interface OnScanListener {
    int OnScanClick(@NonNull Intent intent, @NonNull IIntentResultListener listener);
  }

  protected TextWatcher formrTextWatcher = new TextWatcher() {
    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      Text.this.SetError(null);
      if (onValueChanged_ != null) {
        ViewParent parent = getParent();
        if (parent instanceof Form) {
          ((Form) parent).OnUpdateRequest(GetFieldId(), onValueChanged_);
        }
      }
    }
  };

  public Text(Context context) {
    super(context);
    this.Init();
  }

  @SuppressWarnings("ResourceType")
  public Text(Context context, AttributeSet attrs) {
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

  public Text(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Text(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
      this.scannable_ = settings.optBoolean("scannable");
      JSONObject hint = settings.optJSONObject("hint");
      if (hint != null) {
        String hintString = hint.optString("label");
        this.SetHint(hintString);
      }

      JSONObject error = settings.optJSONObject("error");
      if (error != null) {
        String errorString = error.optString("label");
        if (!TextUtils.isEmpty(errorString)) {
          this.SetError(errorString);
        }
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
      Text.this.SetError(getResources().getString(R.string.error_field_required));
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
   * This function is used to set a text label for the Tex Field
   * If the input is empty and no focus the label will be displayed inline within the text input
   * If the Text field is in focus or it has text set the label will be displayed above the text input
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
   * Sets error view at the bottom of the field
   *
   * @param error error string
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
      this.SetEditTextLineColor();
      textInputLayout.setError(error);
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
    TextInputEditText editText = GetEditText();
    if (editText != null && !TextUtils.isEmpty(label)) {
      editText.setImeActionLabel(label, action);
    }
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

  @Override
  public void onClick(View v) {
    String tag = v.getTag().toString();
    if (tag.equals("scannable")) {
      Intent scanIntent = new Intent(getContext(), BarcodeCaptureActivity.class);
      scanIntent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
//      intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());
      Form form = this.GetForm();
      if (form != null) {
        this.currentRequestCode_ = form.HandleIntent(scanIntent, this);
      } else if (this.scanListeners_.size() > 0) {
        for (OnScanListener list : this.scanListeners_) {
          this.currentRequestCode_ = list.OnScanClick(scanIntent, this);
        }

      } else {

        Log.e(LOG_TAG, "parent context is not implementing IFormIntentHandler");
      }
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (this.currentRequestCode_ == requestCode) {
      if (resultCode == CommonStatusCodes.SUCCESS) {
        if (data != null) {
          Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
          String value = barcode.displayValue;
          if (!TextUtils.isEmpty(value)) {
            this.SetText(value);
          }
        } else {
          Log.d(LOG_TAG, "No barcode captured, intent data is null");
        }
      } else {
        Log.e(LOG_TAG, "Barcode fetch failed" + CommonStatusCodes.getStatusCodeString(resultCode));
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

  public void SetText(String text) {
    TextInputEditText editText = this.GetEditText();
    if (editText != null) {
      editText.setText(text);
    }
  }

  public void SetText(CharSequence text) {
    TextInputEditText editText = this.GetEditText();
    if (editText != null) {
      editText.setText(text);
    }
  }

  public void SetMandatory(boolean mandatory) {
    this.mandatory_ = mandatory;
  }

  public String GetValue() {
    return Text.this.GetText();
  }

  public void SetScannable(boolean scannable) {
    if (scannable) {
      this.scan_.setVisibility(VISIBLE);
    } else {
      this.scan_.setVisibility(GONE);
    }
  }

  public void SetScanListener(OnScanListener listener) {
    this.scanListeners_.add(listener);
  }

  @SuppressWarnings("unused")
  public boolean RemoveScanListener(OnScanListener listener) {
    return this.scanListeners_.remove(listener);
  }

  /**
   * Perform the ui updates on the main thread.
   */
  protected void PostUpdate() {
    this.RemoveTextChangedListener();
    (new Handler(getContext().getMainLooper())).post(new Runnable() {
      @Override
      public void run() {
        if (!TextUtils.isEmpty(Text.this.label_) && !Text.this.label_.equals("null")) {
          Text.this.SetLabel(Text.this.label_);
        } else {
          Text.this.SetLabel("");
        }
        if (!TextUtils.isEmpty(Text.this.value_) && !Text.this.value_.equals("null")) {
          Text.this.SetText(Text.this.value_);
        } else {
          Text.this.SetText("");
        }
        AddTextChangedListener();
        if (Text.this.readonly_) {
          Text.this.SetReadOnly();
        }

        Text.this.SetScannable(Text.this.scannable_);
      }
    });
  }

  /**
   * Returns the current text in the EditText
   *
   * @return string value
   */
  protected String GetText() {
    String res = "";
    TextInputEditText editText = this.GetEditText();
    if (editText != null) {
      res += editText.getText().toString();
    }
    return res;
  }

  protected void SetReadOnly() {
    TextInputEditText editText = this.GetEditText();
    if (editText != null) {
      editText.setEnabled(false);
    }
  }

  protected void RemoveTextChangedListener() {
    TextInputEditText editText = this.GetEditText();
    if (editText != null) {
      editText.removeTextChangedListener(formrTextWatcher);
    }
  }

  protected void AddTextChangedListener() {
    TextInputEditText editText = this.GetEditText();
    if (editText != null) {
      editText.addTextChangedListener(formrTextWatcher);
    }
  }

  protected void SetInputType(int type) {
    TextInputEditText editText = this.GetEditText();
    if (editText != null) {
      editText.setInputType(type);
    }
  }

  protected void SetFilters(ArrayList<InputFilter> filtersArray) {
    TextInputEditText editText = this.GetEditText();
    if (editText != null) {
      InputFilter[] filters = editText.getFilters();
      filtersArray.addAll(Arrays.asList(filters));
      filters = new InputFilter[filtersArray.size()];
      filters = filtersArray.toArray(filters);
      editText.setFilters(filters);
    }
  }

  /**
   * This function stores the data required to rebuild the Text IField
   * we store the value entered by the user
   * we store the error as the error is subject to change and when we restore the field
   * the latest error must be shown
   *
   * @return IField.SavedState
   * if the field was built using Json Configuration we store the json string with the value
   * @see Text#SaveSettings()
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
  protected void onRestoreInstanceState(Parcelable state) {
    SavedState ss = (SavedState) state;
    String value = ss.GetValue();
    if (!TextUtils.isEmpty(value)) {
      this.SetText(ss.GetValue());
    }
    super.onRestoreInstanceState(ss.getSuperState());
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

  private void Init() {
    this.setSaveEnabled(true);
    this.setOrientation(VERTICAL);
    this.setPadding(0, 12, 0, 12);

    this.layout_ = new LinearLayout(getContext());
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    this.setLayoutParams(lp);

    TextInputLayout textInputLayout = new TextInputLayout(getContext());
    LinearLayout.LayoutParams elp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
    textInputLayout.setLayoutParams(elp);
    TextInputEditText et = new TextInputEditText(getContext());
    if (this.textSizeXml_ != 0) {
      et.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.textSizeXml_);
    } else {
      et.setTextSize(LABEL_TEXT_SIZE);
    }
    et.setSingleLine();
    textInputLayout.addView(et);
    textInputLayout.setHintTextAppearance(R.style.FormR_Text_Hint);
    this.layout_.addView(textInputLayout);
    this.addView(layout_);

    this.scan_ = new AppCompatImageButton(getContext());
    this.scan_.setImageResource(R.drawable.ic_barcode_24dp);
    this.scan_.setColorFilter(Utils.GetAccentColor(getContext()));
    LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT,//width
        LayoutParams.WRAP_CONTENT);//height
    param2.gravity = Gravity.CENTER;
    this.scan_.setLayoutParams(param2);
    this.scan_.setTag("scannable");
    this.scan_.setBackgroundColor(Color.TRANSPARENT);
    this.scan_.setOnClickListener(this);
    this.scan_.setVisibility(GONE);
    this.layout_.addView(this.scan_);

    this.hintText_ = new TextView(getContext());
    this.hintText_.setTextSize(HINT_TEXT_SIZE);
    this.hintText_.setPadding(8, 0, 8, 0);
    this.hintText_.setTypeface(this.hintText_.getTypeface(), Typeface.ITALIC);
    this.hintText_.setVisibility(GONE);
    this.addView(this.hintText_);
    this.SetLabel(this.label_);
    this.SetHint(this.hint_);

    et.setOnFocusChangeListener(this);
    et.setOnEditorActionListener(this);
    AddTextChangedListener();
  }

  private CharSequence GetError() {
    CharSequence error = null;
    TextInputLayout textInputLayout = this.GetTextInputLayout();
    if (textInputLayout != null) {
      error = textInputLayout.getError();
    }
    return error;
  }

  private
  @Nullable
  TextInputLayout GetTextInputLayout() {
    TextInputLayout textInputLayout = null;
    if (this.layout_ != null) {
      for (int i = 0; i < this.layout_.getChildCount(); i++) {
        View v = this.layout_.getChildAt(i);
        if (v instanceof TextInputLayout) {
          textInputLayout = (TextInputLayout) v;
        }
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

  private Form GetForm() {
    ViewParent parent = this.getParent();
    if (parent instanceof Form) {
      return (Form) parent;
    }
    return null;
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
