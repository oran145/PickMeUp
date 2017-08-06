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

public class PickMeApp extends Activity {

    private FragmentTransaction ftr;
    MyProgressBar progressBar;

    AddRideFragment addRideFragment;

    RidesListFragment ridesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_me_app);
        addRideFragment=new AddRideFragment();
        addRideFragment.setDelegate(new AddRideFragment.Delegate() {
            @Override
            public void saveRide(Ride r) {
                Model.getInstance().addRide(r);
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
            case R.id.sign_out:
                Model.instance().signOut(new Model.ChatListener() {
                    @Override
                    public void goToMainActivity() {
                        Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                    }

                    @Override
                    public void showProgressBar() {
                        myProgressBar.showProgressDialog();
                    }

                    @Override
                    public void hideProgressBar() {
                        myProgressBar.hideProgressDialog();
                    }
                });
                return true;
            case R.id.add:

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
