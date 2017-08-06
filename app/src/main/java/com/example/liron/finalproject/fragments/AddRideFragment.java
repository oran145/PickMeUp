package com.example.liron.finalproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.liron.finalproject.R;
import com.example.liron.finalproject.model.Ride;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddRideFragment extends Fragment {

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
        Button b=(Button)view.findViewById(R.id.save);

        EditText from=(EditText)view.findViewById(R.id.ride_from) ;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ride r=new Ride();


                delegate.saveRide();
            }
        });


        return view;
    }

}
