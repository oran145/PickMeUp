package com.example.liron.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.liron.finalproject.Dialogs.DateDialogFragment;
import com.example.liron.finalproject.Dialogs.MyProgressBar;
import com.example.liron.finalproject.Dialogs.TimeDialohFragment;
import com.example.liron.finalproject.fragments.AddRideFragment;
import com.example.liron.finalproject.fragments.RidesListFragment;
import com.example.liron.finalproject.model.Model;
import com.example.liron.finalproject.model.Ride;
import com.example.liron.finalproject.model.User;

public class PickMeAppActivity extends Activity {

    private FragmentTransaction ftr;
    MyProgressBar progressBar;
    Model.SaveRideListener saveRideListener;
    AddRideFragment addRideFragment;

    RidesListFragment ridesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = new MyProgressBar(this);
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

        setRidesListDelegate();
        setEmailDelegate();

        addRideFragment.setDelegate(new AddRideFragment.Delegate() {
            @Override
            public void saveRide(Ride r) {
                Model.getInstance().addRide(r,saveRideListener);
                ftr=getFragmentManager().beginTransaction();
                ridesListFragment = new RidesListFragment();
                setRidesListDelegate();
                ftr.replace(R.id.pickMeUp_container, ridesListFragment);
                ftr.addToBackStack("addRide");
                ftr.commit();
            }

            @Override
            public void onDateSet() {
                DateDialogFragment dialog = new DateDialogFragment();
                dialog.setOnDateSetListener(new DateDialogFragment.OnDateSetLisetener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear, int dayOfMonth) {
                        addRideFragment.setTextInDate("" + dayOfMonth + "/" + monthOfYear + "/" + year);
                    }
                });
                dialog.show(getFragmentManager(),"TAG");
            }

            @Override
            public void onTimeSet() {
                TimeDialohFragment dialog = new TimeDialohFragment();
                dialog.setOnTimeSetListener(new TimeDialohFragment.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(int hour, int minute) {
                        addRideFragment.setTextInTime("" + hour + ":" + minute);
                    }
                });
                dialog.show(getFragmentManager(),"TAG");
            }
        });

        ftr = getFragmentManager().beginTransaction();
        ftr.add(R.id.pickMeUp_container, ridesListFragment);
        ftr.show(ridesListFragment);
        ftr.commit();


    }

    private void setRidesListDelegate()
    {
        ridesListFragment.setRideDelegate(new RidesListFragment.rideDelegate() {
            @Override
            public void onItemClick(User user) {
            }

            @Override
            public void showProgressBar() {
                progressBar.showProgressDialog();
            }

            @Override
            public void hideProgressBar() {
                progressBar.hideProgressDialog();
            }
        });
    }

    private void setEmailDelegate()
    {
        ridesListFragment.setEmailDelegate(new RidesListFragment.emailDelegate() {
            @Override
            public void rideSubEmail(String ownerEmail)
            {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                String[] TO={ownerEmail};

                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ride register");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "... was registered to your ride");
                try
                {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
                catch (android.content.ActivityNotFoundException ex)
                {
                    Toast.makeText(PickMeAppActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void rideUnsubEmail(String ownerEmail)
            {

            }

            @Override
            public void rideDeleteEmail(Ride ride)
            {

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
