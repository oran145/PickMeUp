package com.example.liron.finalproject.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.example.liron.finalproject.R;
import com.example.liron.finalproject.model.Ride;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddRideFragment extends Fragment {

    private EditText from;
    private EditText to;
    private EditText date;
    private EditText time;
    private NumberPicker hitchhiker;
    private Button save;
    private int hitchhikerNumber;


    public void setTextInDate(String s) {
        date.setText(s);
    }

    public void setTextInTime(String s) { time.setText(s);}


    public interface Delegate{
        void saveRide(Ride r);
        void onDateSet();
        void onTimeSet();
    }

    Delegate delegate;
    public void setDelegate(Delegate dlg){
        this.delegate = dlg;
    }
    public AddRideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_ride, container, false);
        from = (EditText)view.findViewById(R.id.ride_from);
        to = (EditText)view.findViewById(R.id.ride_to);
        date = (EditText)view.findViewById(R.id.ride_date);
        time = (EditText)view.findViewById(R.id.ride_time);
        hitchhiker = (NumberPicker)view.findViewById(R.id.ride_hitchhiker);
        save =(Button)view.findViewById(R.id.fragment_addRide_save_btn);

        hitchhiker.setMinValue(1);
        hitchhiker.setMaxValue(15);

        hitchhiker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hitchhikerNumber = newVal;
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final long dateLong = utilities.convertDateStringToMillis(date.getText().toString());
                //final long timeLong = utilities.convertDateStringToMillis(time.getText().toString());
                Ride r=new Ride(null,null,date.getText().toString(),time.getText().toString() ,from.getText().toString(),to.getText().toString(),hitchhikerNumber,null);


                delegate.saveRide(r);
            }
        });

        date.setInputType(0);
        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    delegate.onDateSet();
                }
                return true;
            }
        });


        time.setInputType(0);
        time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    delegate.onTimeSet();
                }
                return true;
            }
        });


        return view;
    }


}
