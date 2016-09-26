package com.giift.formr;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vieony on 3/31/2016.
 */
public class Actions extends LinearLayout {
  private String LOG_TAG = Actions.class.getName();
  private List<Listener> listeners_ = new ArrayList<>();
  private JSONArray config_ = null;
  private int id_ = -1;

  /**
   * Constructor
   */
  public Actions(Context c) {
    super(c);
    this.Init();
  }

  /**
   * Constructor
   */
  public Actions(Context c, AttributeSet attrs) {
    super(c, attrs);
    this.Init();
  }

  /**
   * Constructor
   */
  public Actions(Context c, AttributeSet attrs, int defStyleAttr) {
    super(c, attrs, defStyleAttr);
    this.Init();
//    this.defStyleAttr_ = defStyleAttr;
  }

  /**
   * Constructor
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Actions(Context c, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(c, attrs, defStyleAttr, defStyleRes);
    this.Init();
//    this.defStyleAttr_ = defStyleAttr;
//    this.defStyleRes_ = defStyleRes;

  }

  private void Init() {
    this.setOrientation(HORIZONTAL);
    setSaveEnabled(true);
  }

  /**
   * Initialize the Actions
   *
   * @param actions json representation of the Actions
   */

  public void Init(@NonNull JSONArray actions) {
    LinearLayout layout = GetView(actions);
    this.id_ = Form.GenerateViewId();
    layout.setId(this.id_);
    addView(layout);
  }

  private LinearLayout GetView(@NonNull JSONArray actions) {
    this.config_ = actions;
    LinearLayout layout = new LinearLayout(getContext());
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);
    layout.setSaveEnabled(true);
    layout.setLayoutParams(lp);

    for (int j = 0; j < actions.length(); j++) {
      final JSONObject action = actions.optJSONObject(j);
      if (action != null) {
        final String label = action.optString("label");
        final String value = action.optString("value");
        final boolean validate = action.optBoolean("validation", false);

        Button b = new Button(getContext());
        b.setText(label);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            0,
            ViewGroup.LayoutParams.MATCH_PARENT,
            1f);
        layoutParams.setMargins(8, 0, 8, 0);
        b.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        b.setLayoutParams(layoutParams);
        b.setBackgroundResource(R.drawable.button_blue);
        layout.addView(b);
        if (TextUtils.isEmpty(value)) {
          b.setEnabled(false);
        } else {
          b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              for (Listener list : Actions.this.listeners_) {
                list.OnButtonClicked(value, validate);
              }
            }
          });
        }
      }
    }
    return layout;
  }

  public void AddListener(Listener listener) {
    this.listeners_.add(listener);
  }

  public interface Listener {
    void OnButtonClicked(String value, boolean validate);
  }

  /**
   * This function stores the data required to rebuild the Text IField
   * we store the value entered by the user
   * if the field was built using Json Configuration we store the json string with the value
   *
   * @return IField.SavedState
   */
  @Override
  protected Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    SavedState ss = new SavedState(superState);
    ss.SaveId(this.id_);
    if (this.config_ != null) {
      ss.SaveJsonConfig(this.config_.toString());
    }
    return ss;
  }

  @Override
  protected void onRestoreInstanceState(Parcelable state) {
    SavedState ss = (SavedState) state;
    super.onRestoreInstanceState(ss.getSuperState());
    String config = ss.GetJsonConfig();
    this.id_ = ss.GetId();
    if (!TextUtils.isEmpty(config) && this.id_ != -1) {
      try {
        JSONArray array = new JSONArray(config);
        LinearLayout layout = this.GetView(array);
        layout.setId(this.id_);
        addView(layout);
      } catch (JSONException e) {
        Log.e(LOG_TAG, "error trying to create JSON", e);
      }
    }
  }

  class SavedState extends View.BaseSavedState {
    private int id_;
    private String jsonConfig_;

    public SavedState(Parcelable superState) {
      super(superState);
    }

    private SavedState(Parcel in) {
      super(in);
      this.id_ = in.readInt();
      this.jsonConfig_ = in.readString();
    }

    public void SaveId(int id) {
      this.id_ = id;
    }

    public void SaveJsonConfig(String jsonConfig) {
      this.jsonConfig_ = jsonConfig;
    }

    public int GetId() {
      return this.id_;
    }

    public String GetJsonConfig() {
      return this.jsonConfig_;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
      super.writeToParcel(out, flags);
      out.writeInt(this.id_);
      out.writeString(this.jsonConfig_);
    }

    public final Parcelable.Creator<SavedState> CREATOR
        = new Parcelable.Creator<SavedState>() {
      public SavedState createFromParcel(Parcel in) {
        return new SavedState(in);
      }

      public SavedState[] newArray(int size) {
        return new SavedState[size];
      }
    };
  }
}