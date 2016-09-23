package com.giift.formr;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

/**
 * @author vieony on 5/24/2016.
 *
 * Webview that holds data when orientation changes
 */
public class Html extends WebView {

  private int id_ = -1;
  private String config_ = null;

  public Html(Context context) {
    super(context);
    Init();
  }

  private void Init() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      this.setBackgroundColor(getResources().getColor(android.R.color.transparent, null));
    } else {
      //noinspection deprecation
      this.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
  }

  /**
   * Initialize the web view with the content to be displayed
   *
   * @param html string to be displayed
   */

  public void Init(@NonNull String html) {
    this.id_ = Form.GenerateViewId();
    this.setId(this.id_);
    this.config_ = html;
    html = "<html><head><style type='text/css'>body{vertical-align: middle;text-align: center;}</style></head><body><p style=\"color:gray\">" + html + "</p></body></html>";
    this.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
  }

  /**
   * This function stores the data required to rebuild the wenview IField
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
      ss.SaveJsonConfig(this.config_);
    }
    return ss;
  }

  @Override
  protected void onRestoreInstanceState(Parcelable state) {
    SavedState ss = (SavedState) state;
    super.onRestoreInstanceState(ss.getSuperState());
    this.config_ = ss.GetJsonConfig();
    this.id_ = ss.GetId();
    if (!TextUtils.isEmpty(this.config_) && this.id_ != -1) {
      this.setId(this.id_);
      this.Init(this.config_);
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
