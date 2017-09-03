package com.example.liron.finalproject.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.liron.finalproject.MyAppContext;
import com.example.liron.finalproject.R;
import com.example.liron.finalproject.model.Model;
import com.example.liron.finalproject.model.Ride;
import com.example.liron.finalproject.model.User;

import java.util.ArrayList;
import java.util.Objects;


public class RidesListFragment extends Fragment {

    RidesListAdapter ridesAdapter;
    String currentUserId;

    public interface Delegate{
        void onItemClick(User user);
        void showProgressBar();
        void hideProgressBar();
    }

    Delegate delegate;
    public void setDelegate(Delegate dlg){
        this.delegate = dlg;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rides_list, container, false);
        currentUserId = Model.getInstance().getCurrentUserId();
        ArrayList<Ride> data = new ArrayList<Ride>();

        final ListView listView = (ListView) view.findViewById(R.id.fragment_all_rides_list);
        ridesAdapter = new RidesListAdapter(MyAppContext.getAppContext(), data);
        listView.setAdapter(ridesAdapter);

        getRides();

        // Inflate the layout for this fragment
        return view;
    }

    private void getRides()
    {
        Model.getInstance().getAllRidesRemote(new Model.GetAllRidesListener()
        {
            @Override
            public void onComplete(ArrayList<Ride> ridesList)
            {
                ridesAdapter.add(ridesList);
            }
          
            public Context getAppContext() {
                return MyAppContext.getAppContext();
            }

            public void showProgressBar() {
                delegate.showProgressBar();
            }

            public void hideProgressBar()
            {
                delegate.hideProgressBar();
            }
        });
    }

    public class RidesListAdapter extends BaseAdapter
    {
        private ArrayList listData;
        private LayoutInflater layoutInflater;
        ViewHolder holder;

        public RidesListAdapter(Context context, ArrayList listData)
        {
            this.listData = listData;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
            {
                convertView = layoutInflater.inflate(R.layout.list_row, null);
                holder = new ViewHolder();
                holder.contactImage = (ImageView) convertView.findViewById(R.id.list_row_contactImage_imageView);
                holder.firstName = (TextView) convertView.findViewById(R.id.list_row_firstName_textView);
                holder.lastName = (TextView) convertView.findViewById(R.id.list_row_lastName_textView);
                holder.rideId = (TextView) convertView.findViewById(R.id.list_row_invisibleRideId_textView);
                holder.rideDate = (TextView) convertView.findViewById(R.id.list_row_ride_date_textView);
                holder.freeSeats = (TextView) convertView.findViewById(R.id.list_row_ride_hitchhiker_textView);
                holder.from = (TextView) convertView.findViewById(R.id.list_row_ride_from_textView);
                holder.to = (TextView) convertView.findViewById(R.id.list_row_ride_to_textView);
                holder.plusButton = (ImageButton) convertView.findViewById(R.id.list_row_ride_plus_button);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();

            }

            if (listData == null)
            {
                return convertView;
            }

            Ride rideInPosition = (Ride) listData.get(position);



            holder.firstName.setText(rideInPosition.getRideOwner().getFirstName());
            holder.lastName.setText(rideInPosition.getRideOwner().getLastName());
            holder.contactImage.setImageBitmap(rideInPosition.getRideOwner().getUserImage());
            holder.rideId.setText(rideInPosition.getRideID());
            holder.rideDate.setText(Objects.toString(rideInPosition.getDate(),null));
            holder.freeSeats.setText(Objects.toString(rideInPosition.getFreeSeats(),null));
            holder.from.setText(rideInPosition.getFrom());
            holder.to.setText(rideInPosition.getTo());
            holder.plusButton.setTag(position);

            String imgeUrlTest = rideInPosition.getRideOwner().getImageFireBaseUrl();

            Glide.with(MyAppContext.getAppContext())
                    .load(imgeUrlTest)
                    //.asBitmap()
                    // .toBytes()
                    .centerCrop()
                    .into(holder.contactImage);

            holder.plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    switch (v.getId()) {
                        case R.id.list_row_ride_plus_button:

                            int pos = (Integer) v.getTag();
                            final Ride tempRide = (Ride) listData.get(pos);

                            PopupMenu popup = new PopupMenu(MyAppContext.getAppContext(), v);
                            popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());

                            Menu myMenu = popup.getMenu();
                            if(currentUserId.equals(tempRide.getRideOwner().getUserID()))
                            {
                                myMenu.findItem(R.id.unsubscribe).setEnabled(false);
                                myMenu.findItem(R.id.subscribe).setEnabled(false);
                            }
                            else if( tempRide.getHitchhikers().contains(currentUserId))
                            {
                                myMenu.findItem(R.id.subscribe).setEnabled(false);
                                myMenu.findItem(R.id.delete).setEnabled(false);
                            }
                            else if(tempRide.getFreeSeats() == 0)
                            {
                                myMenu.findItem(R.id.subscribe).setEnabled(false);
                                myMenu.findItem(R.id.delete).setEnabled(false);
                                myMenu.findItem(R.id.unsubscribe).setEnabled(false);
                            }
                            else
                            {
                                myMenu.findItem(R.id.delete).setEnabled(false);
                                myMenu.findItem(R.id.unsubscribe).setEnabled(false);
                            }


                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item)
                                {

                                    switch (item.getItemId())
                                    {
                                        case R.id.subscribe:

                                            Model.getInstance().addHitchhiker(tempRide.getRideID());
                                            sendEmail(tempRide,1);
                                            break;

                                        case R.id.unsubscribe:
                                            Model.getInstance().removeHitchhiker(tempRide.getRideID());
                                            sendEmail(tempRide,2);
                                            break;

                                        case R.id.delete:
                                            Model.getInstance().removeRide(tempRide.getRideID());
                                            sendEmail(tempRide,3);
                                            break;

                                        default:
                                            break;
                                    }

                                    return true;
                                }
                            });

                            break;

                        default:
                            break;
                    }
                }
            });

            return convertView;
        }

        class ViewHolder
        {
            ImageView contactImage;
            ImageButton plusButton;
            TextView firstName;
            TextView lastName;
            TextView rideId;
            TextView rideDate;
            TextView freeSeats;
            TextView from;
            TextView to;
        }


        public void add(ArrayList<Ride> rides)
        {
            listData = rides;
            this.notifyDataSetChanged();
        }

        protected void sendEmail(Ride ride ,int flag)
        {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            String[] TO={""};
            String[] CC={""};

            switch (flag)
            {
                case 1 :

                    Log.i("Send email", "");
                    TO = new String[1];

                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                    break;

                case 2 :

                    Log.i("Send email", "");

                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                    break;

                case 3 :

                    Log.i("Send email", "");

                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                    break;
            }

            try
            {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                //finish();
            }
            catch (android.content.ActivityNotFoundException ex)
            {
            }
        }


    }
}