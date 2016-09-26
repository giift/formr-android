package com.giift.formr;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;

import org.json.JSONObject;

/**
 * @author nicolas on 2015/05/27.
 */
public interface IField {

  int LABEL_TEXT_SIZE = 14;
  int HINT_TEXT_SIZE = 11;
  int ERROR_TEXT_SIZE = 11;

  void Init(@NonNull JSONObject config);

  String GetFieldId();

  boolean Validate();

  String[] GetValues();

  boolean Update(@NonNull JSONObject config);

  /**
   * Checks if next field can gain focus and should show keyboard
   *
   * @return true if should show keyboard else false
   */
  boolean IsFocusable();

  void SetLabel(String label);

  void SetError(String error);

  void SetHint(String hint);

  void SetImeActionLabel(String label, int action);

  /**
   * This class is used by the Form Fields to save their state
   * we store the value of the field and the Json config
   * so that when there is change in instance state (e.g device orientation change)
   * we can rebuild the form correctly
   **/

  class SavedState extends View.BaseSavedState {
    private String value_;
    private String jsonConfig_;

    public SavedState(Parcelable superState) {
      super(superState);
    }

    private SavedState(Parcel in) {
      super(in);
      this.value_ = in.readString();
      this.jsonConfig_ = in.readString();
    }

    public void SaveValue(String value) {
      this.value_ = value;
    }

    public void SaveJsonConfig(String jsonConfig) {
      this.jsonConfig_ = jsonConfig;
    }

    public String GetValue() {
      return this.value_;
    }

    public String GetJsonConfig() {
      return this.jsonConfig_;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
      super.writeToParcel(out, flags);
      out.writeString(this.value_);
      out.writeString(this.jsonConfig_);
    }

    public static final Parcelable.Creator<SavedState> CREATOR
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