package com.giift.formr;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.AbsSavedState;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.giift.formr.io.TheOkHttpClient;
import com.giift.formr.io.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author Nicolas Lagier
 */
public class Form extends LinearLayout {
  private static final String LOG_TAG = Form.class.getName();
  private static final AtomicInteger nextGeneratedId_ = new AtomicInteger(1);

  //  private int defStyleAttr_ = 0;
//  private int defStyleRes_ = 0;
  private IFormIntentHandler handler_ = null;
  private String submitLabel_ = null;
  private IFormSubmitHandler submitHandler_ = null;


  /**
   * Constructor
   */
  public Form(Context c) {
    super(c);
    this.Init();
  }

  /**
   * Constructor
   */
  public Form(Context c, AttributeSet attrs) {
    super(c, attrs);
    this.Init();
  }

  /**
   * Constructor
   */
  public Form(Context c, AttributeSet attrs, int defStyleAttr) {
    super(c, attrs, defStyleAttr);
    this.Init();
//    this.defStyleAttr_ = defStyleAttr;
  }

  /**
   * Constructor
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Form(Context c, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(c, attrs, defStyleAttr, defStyleRes);
    this.Init();
//    this.defStyleAttr_ = defStyleAttr;
//    this.defStyleRes_ = defStyleRes;

  }

  private void Init() {
    this.setOrientation(VERTICAL);
    setSaveEnabled(true);
  }

  /**
   * Initialize the form
   *
   * @param form json representation of the form
   */

  public void Init(final JSONObject form) {
    setSaveEnabled(true);
    final Handler handler = new Handler(getContext().getMainLooper());
    handler.post(new Runnable() {
      @Override
      public void run() {
        removeAllViews();
        if (form != null) {
          JSONObject fields = form.optJSONObject("fields");
          if (fields != null) {
            Iterator<String> it = fields.keys();
            while (it.hasNext()) {
              String fieldId = it.next();
              final JSONObject field = fields.optJSONObject(fieldId);
              if (field != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//              v = FieldFactory.GetField(field, getContext(), null, defStyleAttr_, defStyleRes_);
//            } else {
//              v = FieldFactory.GetField(field, getContext(), null, defStyleAttr_);
//            }
                View v = FieldFactory.GetField(field, getContext());
                if (v != null) {
                  v.setId(GenerateViewId());
                  addView(v);
                }
              }
            }
          }
          SetImeActionLabel();
        }
      }
    });
  }

  public boolean Update(@NonNull JSONObject obj) {
    boolean res = true;
    JSONArray update = obj.optJSONArray("update");
    if (update != null) {
      for (int i = 0; i < update.length(); i++) {
        JSONObject field = update.optJSONObject(i);
        if (field != null) {
          res &= this.UpdateField(field);
        }
      }
    }
    JSONArray insert = obj.optJSONArray("insert");
    if (insert != null) {
      for (int i = 0; i < insert.length(); i++) {
        JSONObject ins = insert.optJSONObject(i);
        if (ins != null) {
          JSONObject field = ins.optJSONObject("field");
          String after = ins.optString("after");
          if (field != null) {
            res &= this.InsertField(after, field);
          }
        }
      }
    }
    JSONArray delete = obj.optJSONArray("delete");
    if (delete != null) {
      for (int i = 0; i < delete.length(); i++) {
        String id = delete.optString(i);
        if (!TextUtils.isEmpty(id)) {
          res &= this.RemoveField(id);
        }
      }
    }
    return res;
  }

  public boolean RemoveField(@NonNull String id) {
    Handler handler = new Handler(getContext().getMainLooper());
    for (int i = 0; i < this.getChildCount(); i++) {
      final View v = this.getChildAt(i);
      if (v instanceof IField) {
        IField f = (IField) v;
        if (f.GetFieldId().equals(id)) {
          handler.post(new Runnable() {
            @Override
            public void run() {
              removeView(v);
            }
          });
          return true;
        }
      }
    }
    return false;
  }

  public boolean UpdateField(@NonNull JSONObject field) {
    final String id = field.optString("id");
    final View newField = FieldFactory.GetField(field, getContext());
    if (!TextUtils.isEmpty(id) && newField != null) {
      Handler handler = new Handler(getContext().getMainLooper());
      handler.post(new Runnable() {
        @Override
        public void run() {
          for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof IField) {
              IField f = (IField) v;
              if (f.GetFieldId().equals(id)) {
                int id = v.getId();
                removeView(v);
                v.setId(id);
                addView(newField, i);
              }
            }
          }
        }
      });
    }
    return false;
  }

  public boolean InsertField(String after, @NonNull JSONObject field) {
    final View v = FieldFactory.GetField(field, getContext());
    if (v == null) {
      return false;
    }
    //if after is empty, insert the view at the front
    if (TextUtils.isEmpty(after)) {
      v.setId(GenerateViewId());
      this.addView(v, 0);
      return true;
    }
    Handler handler = new Handler(getContext().getMainLooper());
    //try to insert the view in the middle
    for (int i = 0; i < this.getChildCount(); i++) {
      View child = this.getChildAt(i);
      if (child instanceof IField) {
        IField f = (IField) child;
        if (f.GetFieldId().equals(after)) {
          final int finalI = i;
          handler.post(new Runnable() {
            @Override
            public void run() {
              v.setId(GenerateViewId());
              addView(v, finalI + 1);
            }
          });

          return true;
        }
      }
    }

    //eventually insert the view at the back
    v.setId(GenerateViewId());
    this.addView(v);
    return true;
  }

  /**
   * Will highlight each problematic field.
   *
   * @return true if every field is valid
   */
  public boolean Validate() {
    boolean valid = true;
    for (int i = 0; i < this.getChildCount(); i++) {
      View v = this.getChildAt(i);
      if (v instanceof IField) {
        IField f = (IField) v;
        valid &= f.Validate();
      }
    }
    return valid;
  }

  /**
   * @return map of form items
   */
  public List<Pair> GetValues() {
    List<Pair> res = new ArrayList<>();
    for (int i = 0; i < this.getChildCount(); i++) {
      View v = this.getChildAt(i);
      if (v instanceof IField) {
        IField f = (IField) v;
        String[] values = f.GetValues();
        for (String value : values) {
          Pair<String, String> pair = new Pair<>(f.GetFieldId(), value);
          res.add(pair);
        }

      }
    }
    return res;
  }

  public void OnUpdateRequest(String srcFieldId, Uri requestUri) {
    Uri uri = requestUri.buildUpon().appendPath(srcFieldId).build();
    Request.Builder builder = new Request.Builder()
        .url(uri.toString());

    //fetch values
    List<Pair> values = this.GetValues();
    FormBody.Builder formValues = new FormBody.Builder();
    boolean empty = true;
    for (Pair pair : values) {
      String key = pair.first.toString();
      String value = pair.second.toString();
      if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
        empty = false;
        formValues.add(key, value);
      }
    }
    if (empty) {
      formValues.add("", "");
    }
    builder.post(formValues.build());

    TheOkHttpClient.GetInstance().newCall(builder.build()).enqueue(new Callback() {
      @Override
      public void onFailure(Call c, IOException e) {
        Log.e(LOG_TAG, "Error while querying for form update.", e);
      }

      @Override
      public void onResponse(Call c, Response response) throws IOException {
        JSONObject update = Utils.GetJsonObject(response);
        if (update != null) {
          Form.this.Update(update);
        } else {
          Log.e(LOG_TAG, "Error while parsing answer.");
        }
      }
    });
  }

  /**
   * When a user clicks the next button on the Keyboard
   * we check if the IField is focusable
   * if true; we request focus and show the keyboard
   * if false we hide the keyboard
   *
   * @param srcField field for which keyboard next button was clicked
   */
  public void OnNextClicked(IField srcField) {
    for (int i = 0; i < this.getChildCount(); i++) {
      View child = this.getChildAt(i);
      if (child instanceof IField) {
        IField f = (IField) child;
        if (f.GetFieldId().equals(srcField.GetFieldId())) {
          View nextChild = this.getChildAt(i + 1);
          if (nextChild != null && nextChild instanceof IField) {
            IField next = (IField) nextChild;
            if (next.IsFocusable()) {
              nextChild.requestFocus();
            } else {
              try {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nextChild.getWindowToken(), 0);
              } catch (Exception e) {
                Log.e(LOG_TAG, "There was an error while trying to hide keyboard", e);
              }
            }
          }
        }
      }
    }
  }

  /**
   * When a user clicks the done button on the Keyboard
   * we check if the form submit Handler is set
   * if true; we notify all views listening for this action
   *
   * @param iField field for which keyboard done button was clicked
   */
  public boolean OnDoneClicked(IField iField) {
    boolean handled = false;
    if (this.submitHandler_ != null) {
      handled = true;
      this.submitHandler_.OnFormSubmit(this, iField);
    }
    return handled;
  }

  public void SetSubmitHandler(@NonNull IFormSubmitHandler handler) {
    this.submitHandler_ = handler;
  }

  public void SetIntentHandler(@NonNull IFormIntentHandler handler) {
    this.handler_ = handler;
  }

  public int HandleIntent(@NonNull Intent intent, @NonNull IIntentResultListener listener) {
    if (this.handler_ != null) {
      return handler_.HandleIntent(intent, listener);
    } else {
      return -1;
    }
  }

  /**
   * Whenever there is a change in device configuration (e.g orientation rotation) we use the class
   *
   * @return Parcelable of the type Form.SavedState
   * @see com.giift.formr.Form.SavedState to store the state and order of all form child elements
   */
  @Override
  protected Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    SavedState ss = new SavedState(superState);
    for (int i = 0; i < getChildCount(); i++) {
      View v = this.getChildAt(i);
      v.saveHierarchyState(ss.childrenStates_);
      ss.order_.add(v.getId());
    }
    ss.submitLabel_ = this.submitLabel_;
    return ss;
  }

  /**
   * Whenever we return from a device configuration this is the function called so that we can retore
   * state using the values we saved in onSaveInstanceState
   * We try to get the index at which the view is to be added and then add the view at that index
   *
   * @param state
   */
  @Override
  protected void onRestoreInstanceState(Parcelable state) {
    SavedState ss = (SavedState) state;
    super.onRestoreInstanceState(ss.getSuperState());
    SparseArray<AbsSavedState> childrenStates = ss.childrenStates_;
    List<Integer> order = ss.order_;
    this.submitLabel_ = ss.submitLabel_;
    for (int i = 0; i < order.size(); i++) {
      int id = order.get(i); // get id of the view
      int key = childrenStates.indexOfKey(id); // get the position at which id is present in Sparsearray
      AbsSavedState childState = childrenStates.valueAt(key); // Get state at that position
      if (childState instanceof IField.SavedState) {
        IField.SavedState fieldState = (IField.SavedState) childState;
        try {
          String jsonConfig = fieldState.GetJsonConfig();
          if (!TextUtils.isEmpty(jsonConfig)) {
            JSONObject object = new JSONObject(jsonConfig);
            if (object != null) {
              View v = FieldFactory.GetField(object, getContext());
              if (v != null) {
                v.setId(id);
                addView(v);
              }
            }
          }
        } catch (JSONException e) {
          Log.e(LOG_TAG, "error while creating json object in onRestoreInstanceState", e);
        }
      }
    }
    SetImeActionLabel();
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
   * This class stores the states of the form children
   * It also stores the order of the elements which is needed to rebuild the form in the same
   * order before config change
   */
  static class SavedState extends BaseSavedState {
    SparseArray childrenStates_;
    List<Integer> order_;
    String submitLabel_;


    SavedState(Parcelable superState) {
      super(superState);
      this.childrenStates_ = new SparseArray();
      this.order_ = new ArrayList<>();
    }

    private SavedState(Parcel in, ClassLoader classLoader) {
      super(in);
      this.childrenStates_ = in.readSparseArray(classLoader);
      this.order_ = in.readArrayList(classLoader);
      this.submitLabel_ = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
      super.writeToParcel(out, flags);
      out.writeSparseArray(this.childrenStates_);
      out.writeList(this.order_);
      out.writeString(this.submitLabel_);
    }

    public static final Creator<SavedState> CREATOR
        = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
      @Override
      public SavedState createFromParcel(Parcel in, ClassLoader loader) {
        return new SavedState(in, loader);
      }

      @Override
      public SavedState[] newArray(int size) {
        return new SavedState[size];
      }
    });
  }

  /**
   * Generate a value suitable for use in {@link #setId(int)}.
   * This value will not collide with ID values generated at build time by aapt for R.id.
   *
   * @return a generated ID value
   */
  public static int GenerateViewId() {
    for (; ; ) {
      final int result = nextGeneratedId_.get();
      // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
      int newValue = result + 1;
      if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
      if (nextGeneratedId_.compareAndSet(result, newValue)) {
        return result;
      }
    }
  }

  /**
   * This function Sets the Submit label on the soft-keyboard for the last FormR field
   * It also sets the IField SetSubmit to true; so that the field knows it must call the
   * Submit callback on the keyboard Done Action
   */
  private void SetImeActionLabel() {
    int count = this.getChildCount();
    if (count > 0) {
      View child = this.getChildAt(count - 1);
      if (!TextUtils.isEmpty(this.submitLabel_) && child != null && child instanceof IField) {
        IField f = (IField) child;
        f.SetImeActionLabel(this.submitLabel_, EditorInfo.IME_ACTION_DONE);
      }
    }
  }

  /**
   * This sets the soft-keyboard button label
   * Set this before Initialising the form with Json Data
   *
   * @param label ImeActionLabel to be set
   */
  public void SetSubmitLabel(@NonNull String label) {
    this.submitLabel_ = label;
  }

//  public boolean HasIntentHandler(){
//    return this.handler_!=null;
//  }
}
