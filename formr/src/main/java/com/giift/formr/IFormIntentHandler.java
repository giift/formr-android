package com.giift.formr;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * @author vieony on 30/10/2015.
 */
public interface IFormIntentHandler {
  int HandleIntent(@NonNull Intent intent, @NonNull IIntentResultListener listener);
}
