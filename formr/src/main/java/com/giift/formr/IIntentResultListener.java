package com.giift.formr;

import android.content.Intent;

/**
 * @author vieony on 30/10/2015.
 */
public interface IIntentResultListener {
  void onActivityResult(int requestCode, int resultCode, Intent data);
}
