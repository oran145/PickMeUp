package com.example.liron.finalproject.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Marian on 3/6/2017.
 */

public class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private OnDateSetLisetener lisetener;
    public void setOnDateSetListener(OnDateSetLisetener listener){
        this.lisetener = listener;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //fetching the date of today
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);

        Dialog dialog = new DatePickerDialog(getActivity(),this,thisYear,thisMonth,thisDay);
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        Log.d("TAG","user selected date is:" + dayOfMonth + "/" + monthOfYear + "/" + year );
        if (this.lisetener != null){
            this.lisetener.onDateSet(year,monthOfYear+1,dayOfMonth);
        }
    }

    public interface OnDateSetLisetener{
        void onDateSet(int year, int monthOfYear, int dayOfMonth);
    }
}