package com.example.liron.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.liron.finalproject.Dialogs.MyProgressBar;
import com.example.liron.finalproject.fragments.AddRideFragment;
import com.example.liron.finalproject.fragments.RidesListFragment;
import com.example.liron.finalproject.model.Model;
import com.example.liron.finalproject.model.Ride;

public class PickMeAppActivity extends Activity {

    private FragmentTransaction ftr;
    MyProgressBar progressBar;
    Model.SaveRideListener saveRideListener;
    AddRideFragment addRideFragment;


    RidesListFragment ridesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_me_app);
        addRideFragment=new AddRideFragment();
        ridesListFragment=new RidesListFragment();
        saveRideListener = new Model.SaveRideListener(){


            @Override
            public void showProgressBar() {
                progressBar.showProgressDialog();
            }

            @Override
            public void hideProgressBar() {
                progressBar.hideProgressDialog();
            }
        };
        addRideFragment.setDelegate(new AddRideFragment.Delegate() {
            @Override
            public void saveRide(Ride r) {
                Model.getInstance().addRide(r,saveRideListener);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
//            case R.id.sign_out:
//                Model.getInstance().signOut(new Model.() {
//                    @Override
//                    public void goToMainActivity() {
//                        Intent intent = new Intent(PickMeAppActivity.this, MainActivity.class);
//                        finish();
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void showProgressBar() {
//                        myProgressBar.showProgressDialog();
//                    }
//
//                    @Override
//                    public void hideProgressBar() {
//                        myProgressBar.hideProgressDialog();
//                    }
//                });
//                return true;
            case R.id.add:
                ftr=getFragmentManager().beginTransaction();
                ftr.add(R.id.pickMeUp_container, addRideFragment);
                ftr.hide(ridesListFragment);
                ftr.show(addRideFragment);
                ftr.addToBackStack("ridesList");
                ftr.commit();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}