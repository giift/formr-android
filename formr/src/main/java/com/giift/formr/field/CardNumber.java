package com.giift.formr.field;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giift.formr.Form;
import com.giift.formr.IField;
import com.giift.formr.IIntentResultListener;
import com.giift.formr.R;
import com.giift.formr.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Field that allows the input of a credit card number.
 *
 * @author Nicolas
 */
public class CardNumber extends LinearLayout implements IField,
    View.OnClickListener, IIntentResultListener, EditText.OnEditorActionListener {

  private static final String LOG_TAG = CardNumber.class.getName();

  protected String id_ = null;
  protected boolean mandatory_ = false;
  protected boolean readOnly_ = false;
  private int currentRequestCode_ = -1;
  private JSONObject config_ = null;
  private LinearLayout layout_ = null;
  private TextView label_ = null;
  private TextView hintText_ = null;
//  private Uri onValueChanged_ = null;
//  private Uri onFocusLost_ = null;

  public CardNumber(Context context) {
    super(context);
    this.Init();
  }

  public CardNumber(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.Init();
  }

  public CardNumber(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CardNumber(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  /**
   * Luhn algorithm, which says if a card number is a valid credit card number or not
   *
   * @param pnr credit card number to check
   * @return true if it is a valid credit card number
   */
  protected static boolean Luhn(String pnr) {
    int sum = 0;
    for (int i = 0; i < pnr.length(); i++) {
      int j = pnr.length() - i - 1;
      char tmp = pnr.charAt(j);
      int product = tmp - '0';
      if (i % 2 == 1) {
        product *= 2;
      }
      if (product > 9) {
        product -= 9;
      }
      sum += product;
    }
    return (sum % 10 == 0);
  }

  @Override
  public void Init(@NonNull JSONObject config) {
    this.config_ = config;
    this.id_ = config.optString("id", null);
    this.mandatory_ = config.optBoolean("mandatory", false);
    String value = config.optString("value");
    this.SetValue(value);
    String label = config.optString("label");
    this.SetLabel(label);

    JSONObject settings = config.optJSONObject("settings");
    if (settings != null) {
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
      this.readOnly_ = settings.optBoolean("readonly", false);
      if (this.readOnly_) {
        this.SetReadOnly();
      }
    }
  }

  @Override
  public String GetFieldId() {
    return this.id_;
  }

  @Override
  public boolean Validate() {
    boolean res = true;
    //only do additional checks if is valid
    String value = this.GetValue();
    if (mandatory_ && TextUtils.isEmpty(value)) {
      res = false;
      this.SetError(getResources().getString(R.string.error_field_required));
    }

    if (res && !TextUtils.isEmpty(value)) {
      res = CardNumber.Luhn(value);
      if (!res) {
        this.SetError(getResources().getString(R.string.invalid_card_number));
      }
    }
    if (res) {
      this.SetError(null);
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
    return true;
  }

  /**
   * This function is used to set a text label above the CardNumber field
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
   * Add an error marker on the 4 text-fields
   *
   * @param error message to be displayed.
   */
  @Override
  public void SetError(String error) {
    if (!TextUtils.isEmpty(error)) {
      Drawable ic_error = AppCompatDrawableManager.get().getDrawable(getContext(), R.drawable.ic_error_24dp);
      ic_error = DrawableCompat.wrap(ic_error);
      DrawableCompat.setTint(ic_error.mutate(), ContextCompat.getColor(getContext(), R.color.major_red));
      ic_error.setBounds(0, 0, ic_error.getIntrinsicWidth(), ic_error.getIntrinsicHeight());
      for (int i = 0; i < 4; i++) {
        TextView tv = (TextView) this.layout_.getChildAt(i);
        tv.setError(error, ic_error);
      }
    } else {
      for (int i = 0; i < 4; i++) {
        TextView tv = (TextView) this.layout_.getChildAt(i);
        tv.setError(null, null);
      }
    }
  }

  /**
   * This function is used to display a hint for the CardNumber field
   * The hint is displayed below the card number input
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
    EditText editText = (EditText) this.layout_.getChildAt(3);
    editText.setImeActionLabel(label, action);
  }

  @Override
  public void onClick(View v) {
    Intent scanIntent = new Intent(getContext(), CardIOActivity.class);
    scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, true);
    scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);
    scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);
    scanIntent.putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, ContextCompat.getColor(getContext(), R.color.major_orange));
    scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true);

    Form form = this.GetForm();
    if (form != null) {
      this.currentRequestCode_ = form.HandleIntent(scanIntent, this);
    } else {
      Log.e(LOG_TAG, "parent context is not implementing IFormIntentHandler");
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (this.currentRequestCode_ == requestCode) {
      if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
        CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
        this.SetValue(scanResult.cardNumber);
      }
    } else {
      Log.e(LOG_TAG, String.format("Wrong request code :%d", requestCode));
    }
  }

  @UiThread
  protected void Init() {
    this.setSaveEnabled(true);
    this.setOrientation(VERTICAL);
    this.setPadding(0, 14, 0, 14);

    this.label_ = new TextView(getContext());
    this.label_.setTextSize(LABEL_TEXT_SIZE);
    this.label_.setVisibility(GONE);
    this.label_.setPadding(8, 0, 8, 0);
    this.addView(this.label_);

    this.layout_ = new LinearLayout(getContext());
    this.layout_.setOrientation(HORIZONTAL);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    this.layout_.setLayoutParams(lp);
    LinearLayout.LayoutParams elp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
    for (int i = 0; i < 4; i++) {
      int maxLength = 4;
      EditText et = new EditText(getContext());
      et.setInputType(InputType.TYPE_CLASS_NUMBER);
      et.setOnEditorActionListener(this);
      et.setFilters(new InputFilter[]{new InputFilter.LengthFilter.LengthFilter(maxLength)});
      et.setLayoutParams(elp);
      this.layout_.addView(et);
    }

    for (int i = 0; i < this.layout_.getChildCount(); i++) {
      View v = this.layout_.getChildAt(i);
      if (v instanceof EditText) {
        final EditText etp = (EditText) this.layout_.getChildAt(i + 1);
        final EditText etm = (EditText) this.layout_.getChildAt(i - 1);
        ((EditText) v).addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {
          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 4 && etp != null) {
              etp.requestFocus();
            }
            if (s.length() == 0 && etm != null) {
              etm.requestFocus();
            }
          }

          @Override
          public void afterTextChanged(Editable s) {
          }
        });
      }
    }

    AppCompatImageButton v = new AppCompatImageButton(getContext());
    v.setImageResource(R.drawable.ic_photo_camera_24dp);
    v.setColorFilter(Utils.GetAccentColor(getContext()));
    LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT,//width
        LayoutParams.WRAP_CONTENT);//height
    param2.gravity = Gravity.CENTER;
    v.setLayoutParams(param2);
    v.setOnClickListener(this);
    v.setBackgroundColor(Color.TRANSPARENT);
    this.layout_.addView(v);

    this.addView(this.layout_);
    this.hintText_ = new TextView(getContext());
    this.hintText_.setTextSize(HINT_TEXT_SIZE);
    this.hintText_.setPadding(8, 0, 8, 0);
    this.hintText_.setTypeface(this.hintText_.getTypeface(), Typeface.ITALIC);
    this.hintText_.setVisibility(GONE);
    this.addView(this.hintText_);
  }

  /**
   * This function stores the data required to rebuild the CardNumber IField
   * we store the value entered by the user
   * if the field was built using Json Configuration we store the json string with the  value
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
    String value = ss.GetValue();
    if (!TextUtils.isEmpty(value)) {
      this.SetValue(ss.GetValue());
    }
    super.onRestoreInstanceState(ss.getSuperState());
  }

  private String GetValue() {
    String res = "";
    for (int i = 0; i < this.layout_.getChildCount(); i++) {
      View v = this.layout_.getChildAt(i);
      if (v instanceof EditText) {
        res += ((EditText) v).getText().toString();
      }
    }

    return res;
  }

  private void SetValue(String cardNumber) {
    int j = 0;
    int k = 4;
    if (!TextUtils.isEmpty(cardNumber) && !cardNumber.equals("null")) {
      for (int i = 0; i < this.layout_.getChildCount(); i++) {
        View v = this.layout_.getChildAt(i);
        if (v instanceof EditText) {
          try {
            ((EditText) v).setText(cardNumber.substring(j, j + k));
            if (i == 3) {
              j = j + (cardNumber.length() - j);
            } else {
              j = j + 4;
            }
            if (i == 2) {
              k = cardNumber.length() - j;
            }

          } catch (Exception e) {
            Log.e(LOG_TAG, "error assigning CardNumber ", e);
          }

        }
      }
    }
  }

  /**
   * Makes the CardNumber field readonly
   */
  private void SetReadOnly() {
    for (int i = 0; i < this.layout_.getChildCount(); i++) {
      View v = this.layout_.getChildAt(i);
      v.setEnabled(false);
      if (v instanceof AppCompatImageButton) {
        try {
          ((AppCompatImageButton) v).getDrawable().setAlpha(126);// if the button is readonly (disabled) set 50% transparency
        } catch (Exception e) {
          Log.e(LOG_TAG, "Exception while setting button transparency", e);
        }
      }
    }
  }

  private Form GetForm() {
    ViewParent parent = this.getParent();
    if (parent instanceof Form) {
      return (Form) parent;
    }

    return null;
  }

  @Override
  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    switch (actionId) {
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
}
