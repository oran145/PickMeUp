package com.example.liron.finalproject.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.liron.finalproject.R;
import com.example.liron.finalproject.Utilities.utilities;
import com.example.liron.finalproject.model.Ride;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddRideFragment extends Fragment {

    private EditText from;
    private EditText to;
    private EditText date;
    private EditText time;
    private EditText hitchhiker;
    private Button save;

    public interface Delegate{
        void saveRide(Ride r);
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
        hitchhiker = (EditText)view.findViewById(R.id.ride_hitchhiker);
        save =(Button)view.findViewById(R.id.fragment_addRide_save_btn);
       save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //final long dateLong = utilities.convertDateStringToMillis(date.getText().toString());
        //final long timeLong = utilities.convertDateStringToMillis(time.getText().toString());
                Ride r=new Ride(null,null,1,2,from.getText().toString(),to.getText().toString(),Integer.parseInt( hitchhiker.getText().toString()),null);


                delegate.saveRide(r);
            }
        });


        return view;
    }

}
