package com.giift.formr.field;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.giift.formr.IField;
import com.giift.formr.R;
import com.giift.formr.fee.Calc;
import com.giift.formr.utils.Culture;
import com.giift.formr.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author nicolas on 2016/03/4.
 */
public class Slider extends LinearLayout implements IField, SeekBar.OnSeekBarChangeListener {

  public static final String LOG_TAG = Slider.class.getName();
  private SeekBar bar_ = null;
  private TextView src_ = null;
  private TextView dst_ = null;
  private TextView label_ = null;
  private TextView hintText_ = null;
  private TextView errorText_ = null;
  private ImageView errorIcon_ = null;
  private JSONObject config_ = null;
  private String id_ = null;
  private double min_ = 0.0;
  private double max_ = 100.0;
  private double step_ = 1.0;
  private String unit_ = null;
  private String dstUnit_ = null;
  private String labelStr_ = null;
  private boolean readonly_ = false;
  private int hintPaddingBottom_ = 0;
  private OnProgressChangedListener progressChangedListener_ = null;

  private Calc a_ = null;
  private Calc b_ = null;

  public interface OnProgressChangedListener {
    void OnProgressChanged(double value);
  }

  public Slider(Context context) {
    super(context);
    this.Init();
  }

  public Slider(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IField);
    if (a != null) {
      this.labelStr_ = a.getString(R.styleable.IField_label);
      this.hintPaddingBottom_ = a.getDimensionPixelSize(R.styleable.IField_hintPaddingBottom, 0);
      a.recycle();
    }
    this.Init();
  }

  public Slider(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.Init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Slider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.Init();
  }

  @Override
  public void Init(@NonNull JSONObject config) {
    this.config_ = config;
    this.id_ = config.optString("id", null);
    this.labelStr_ = config.optString("label");
    JSONObject settings = config.optJSONObject("settings");
    if (settings != null) {
      this.readonly_ = settings.optBoolean("readonly", false);
      this.unit_ = settings.optString("unit");
      double f = settings.optDouble("min", 0.0);
      this.SetMin(f);
      f = settings.optDouble("max", 0.0);
      this.SetMax(f);
      f = settings.optDouble("step", 1.0);
      this.SetStep(f);
      f = config.optDouble("value");
      this.SetValue(f);
      JSONObject fx = settings.optJSONObject("fx");
      if (fx != null) {
        this.dstUnit_ = fx.optString("unit");
        JSONObject a = fx.optJSONObject("a");
        if (a != null) {
          this.a_ = Calc.GetCalc(a);
        }
        JSONObject b = fx.optJSONObject("b");
        if (b != null) {
          this.b_ = Calc.GetCalc(b);
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

      this.PostUpdate();
    }
  }

  @Override
  public String GetFieldId() {
    return this.id_;
  }

  @Override
  public boolean Validate() {
    return true;
  }

  public String GetValue() {
    return Double.toString(this.GetValueAsDouble());
  }

  @Override
  public String[] GetValues() {
    return new String[]{GetValue()};
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
   * This function is used to set a text label above the Slider
   *
   * @param label string to be displayed
   */
  @Override
  public void SetLabel(String label) {
    if (!TextUtils.isEmpty(label) && !label.equals("null")) {
      this.label_.setVisibility(VISIBLE);
      this.label_.setText(label);
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
   * This function is used to display a hint for Slider field
   * The hint is displayed below the slider and above the error (if present)
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

  public double GetValueAsDouble() {
    return this.min_ - ((double) this.bar_.getProgress() / (double) this.bar_.getMax()) * (this.min_ - this.max_);
  }

  //---------------SeekBar.OnSeekBarChangeListener--------------------------------------------------
  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    this.SetError(null);
    this.UpdateValue();
    if (this.progressChangedListener_ != null) {
      this.progressChangedListener_.OnProgressChanged(this.GetValueAsDouble());
    }
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {

  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {

  }

  public void SetMin(double v) {
    this.min_ = v;
    this.UpdateRange();
  }

  public void SetMax(double v) {
    this.max_ = v;
    this.UpdateRange();
  }

  public void SetStep(double v) {
    if (v <= 0.0) {
      v = 1.0;
    }
    this.step_ = v;
    this.UpdateRange();
  }

  public void SetValue(double v) {
    if (v < this.min_) {
      v = this.min_;
    } else if (v > this.max_) {
      v = this.max_;
    }

    int progress = (int) ((v - this.min_) / this.step_);

    this.bar_.setProgress(progress);
  }

  public void SetUnit(String unit) {
    this.unit_ = unit;
  }

  public void SetDstUnit(String unit) {
    this.dstUnit_ = unit;
  }

  public void SetProgressChangedListener(OnProgressChangedListener listener) {
    this.progressChangedListener_ = listener;
  }

  /**
   * Perform the ui updates on the main thread.
   */
  protected void PostUpdate() {
    (new Handler(getContext().getMainLooper())).post(new Runnable() {
      @Override
      public void run() {
        Slider.this.UpdateRange();
        if (TextUtils.isEmpty(Slider.this.labelStr_)) {
          Slider.this.label_.setVisibility(View.GONE);
        } else {
          Slider.this.SetLabel(Slider.this.labelStr_);
        }
        if (Slider.this.readonly_) {
          Slider.this.SetReadOnly();

        }
      }
    });
  }

  @UiThread
  protected void Init() {
    this.setSaveEnabled(true);
    this.setPadding(0, 14, 0, 14);
    //prepare this layout, vertical orientation with horizontal fill and vertical wrap content
    this.setOrientation(VERTICAL);

    this.label_ = new TextView(getContext());
    this.label_.setTextSize(LABEL_TEXT_SIZE);
    this.label_.setPadding(8, 0, 8, this.hintPaddingBottom_);
    this.SetLabel(this.labelStr_);
    this.addView(this.label_);

    //create sub layout
    LinearLayout ll = new LinearLayout(getContext());
    ll.setOrientation(LinearLayout.HORIZONTAL);
    LayoutParams elp = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

    this.src_ = new TextView(getContext());
    this.src_.setLayoutParams(elp);
    this.src_.setTextSize(LABEL_TEXT_SIZE);
    this.src_.setPadding(8, 0, 8, 0);
    ll.addView(this.src_);

    this.dst_ = new TextView(getContext());
    this.dst_.setLayoutParams(elp);
    this.dst_.setTextSize(LABEL_TEXT_SIZE);
    this.dst_.setPadding(8, 0, 8, 0);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      this.dst_.setTextAlignment(TEXT_ALIGNMENT_VIEW_END);
    }
    ll.addView(this.dst_);

    this.addView(ll);

    //prepare and add seek bar
    LinearLayout layout = new LinearLayout(getContext());
    layout.setOrientation(HORIZONTAL);
    this.bar_ = new SeekBar(getContext());
    this.bar_.setLayoutParams(elp);
    this.bar_.setOnSeekBarChangeListener(this);
    layout.addView(this.bar_);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      int color = Utils.GetAccentColor(getContext());
      Drawable wrappedDrawable = DrawableCompat.wrap(this.bar_.getProgressDrawable());
      DrawableCompat.setTint(wrappedDrawable.mutate(), color);
      this.bar_.setProgressDrawable(wrappedDrawable);
    }

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
   * This function stores the data required to rebuild the Slider IField
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

  @Override
  protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
    dispatchFreezeSelfOnly(container);
  }

  @Override
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
    dispatchThawSelfOnly(container);
  }

  /**
   * Makes the Slider field readonly; i.e the value of the slider cannot be changed
   */
  private void SetReadOnly() {
    this.bar_.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });

  }

  private void UpdateRange() {
    if (this.step_ <= 0.0) {
      this.step_ = 1.0;
    }
    int max = (int) ((this.max_ - this.min_) / this.step_);
    this.bar_.setMax(max);
    this.bar_.setKeyProgressIncrement(1);
    this.UpdateValue();
  }

  private void UpdateValue() {
    double v = this.GetValueAsDouble();
    this.src_.setText(Culture.FormatNumberWithUnit(v, this.unit_));
    if (this.a_ != null || this.b_ != null) {
      double res = 0.0;
      if (this.a_ != null) {
        res = this.a_.GetValue(v) * v;
      }
      if (this.b_ != null) {
        res += this.b_.GetValue(v);
      }

      this.dst_.setText(Culture.FormatNumberWithUnit(res, this.dstUnit_));
    } else {
      this.dst_.setText(null);
    }
  }

}
