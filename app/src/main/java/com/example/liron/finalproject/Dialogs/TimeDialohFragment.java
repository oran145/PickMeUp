package com.example.liron.finalproject.Dialogs;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

@SuppressLint("NewApi")
public class TimeDialohFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private OnTimeSetListener lisetener;

    public void setOnTimeSetListener(OnTimeSetListener listener){
        this.lisetener = listener;
    }

    public TimeDialohFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
        if (this.lisetener != null){
            this.lisetener.onTimeSet(hourOfDay,minuteOfDay);
        }
    }

    public interface OnTimeSetListener{
        void onTimeSet(int hour, int minute);
    }

}
