package com.giift.formr;

import android.support.annotation.NonNull;

/**
 * @author vieony on 8/25/2016.
 * This is called by the
 */
public interface IFormSubmitHandler {
  void OnFormSubmit(@NonNull Form form, @NonNull IField iField);
}
