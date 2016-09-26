package com.giift.formr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Fragment to display the DatePicker Dialog.
 */
public class DatePickerFragment extends DialogFragment implements
    DatePickerDialog.OnDateSetListener {
  public static final String YEAR = "YEAR";
  public static final String MONTH = "MONTH";
  public static final String DAY_OF_MONTH = "DAY_OF_MONTH";
  public static final String START_DATE = "START_DATE";
  public static final String END_DATE = "END_DATE";
  private DatePickerListener listener_ = null;

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Bundle args = this.getArguments();
    // Use the current date as the default date in the picker
    int year = args.getInt(YEAR);
    int month = args.getInt(MONTH);
    int day = args.getInt(DAY_OF_MONTH);

    // Create a new instance of DatePickerDialog and return it
    DatePickerDialog d = new DatePickerDialog(getActivity(), this, year, month, day);
    if (args.containsKey(START_DATE)) {
      long start = args.getLong(START_DATE);
      d.getDatePicker().setMinDate(start);
    }
    if (args.containsKey(END_DATE)) {
      long end = args.getLong(END_DATE);
      d.getDatePicker().setMaxDate(end);
    }
    return d;
  }

  @Override
  public void onDateSet(DatePicker view, int year, int month, int day) {
    GregorianCalendar c = new GregorianCalendar(year, month, day);
    Date d = c.getTime();
    Fragment target = getTargetFragment();
    if (target instanceof DatePickerListener) {
      ((DatePickerListener) target).OnDateSelected(d);
    }
    if (listener_ != null) {
      listener_.OnDateSelected(d);
    }
  }

  public void SetListener(DatePickerListener list) {
    this.listener_ = list;
  }

  /**
   * Interface to be implemented by the target Activity if it wants to be informed of the result.
   * returns the selected date
   */
  public interface DatePickerListener {
    void OnDateSelected(Date date);
  }
}
