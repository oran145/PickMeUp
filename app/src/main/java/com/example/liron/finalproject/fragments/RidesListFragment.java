package com.example.liron.finalproject.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liron.finalproject.MyAppContext;
import com.example.liron.finalproject.R;
import com.example.liron.finalproject.model.Model;
import com.example.liron.finalproject.model.Ride;
import com.example.liron.finalproject.model.User;

import java.util.ArrayList;
import java.util.Objects;


public class RidesListFragment extends Fragment {

    RidesListAdapter ridesAdapter;

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

        ArrayList<Ride> data = new ArrayList<Ride>();

        final ListView listView = (ListView) view.findViewById(R.id.fragment_all_rides_list);
        ridesAdapter = new RidesListAdapter(MyAppContext.getAppContext(), data);
        listView.setAdapter(ridesAdapter);

        getRides();

        // Inflate the layout for this fragment
        return view;
    }

    private void getRides() {
        Model.getInstance().getAllRidesRemote(new Model.GetAllRidesListener()
        {
            public void onComplete(Ride ride) {
                ridesAdapter.add(ride);
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
            ViewHolder holder;
            if (convertView == null) {
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
                holder.plusButton = (Button)  convertView.findViewById(R.id.list_row_ride_plus_button);

                holder.plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        int pos = (int) v.getTag();
                        Ride rideInPosition = (Ride) listData.get(pos);


                        Button plusButton = (Button)v.findViewById(R.id.list_row_ride_plus_button);
                        String buttonText = plusButton.getText().toString();

                        switch (buttonText)
                        {
                            case "+":
                                Model.getInstance().addHitchhiker(rideInPosition.getRideID(), new Model.updateListener() {
                                    @Override
                                    public void onUpdate() {
                                        listData.clear();
                                        getRides();
                                        ridesAdapter.notifyDataSetChanged();
                                    }
                                });
                            break;

                            case "-":
                                Model.getInstance().removeHitchhiker(rideInPosition.getRideID(), new Model.updateListener() {
                                    @Override
                                    public void onUpdate() {
                                        listData.clear();
                                        getRides();
                                        ridesAdapter.notifyDataSetChanged();
                                    }
                                });
                             break;
                            case "X":
                                Model.getInstance().removeRide(rideInPosition.getRideID(), new Model.updateListener() {
                                    @Override
                                    public void onUpdate() {
                                        listData.clear();
                                        getRides();
                                        ridesAdapter.notifyDataSetChanged();
                                    }
                                });
                                break;

                        }

                    }
                });

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

            String currentUserId = Model.getInstance().getCurrentUserId();

            if(currentUserId.equals(rideInPosition.getRideOwner().getUserID()))
            {
                holder.plusButton.setText("X");

            }
            else if( rideInPosition.getHitchhikers().contains(currentUserId))
            {
                holder.plusButton.setText("-");
            }
                else if(rideInPosition.getFreeSeats() == 0)
                {
                    holder.plusButton.setVisibility(View.INVISIBLE);
                }

            holder.firstName.setText(rideInPosition.getRideOwner().getFirstName());
            holder.lastName.setText(rideInPosition.getRideOwner().getLastName());
            holder.contactImage.setImageBitmap(rideInPosition.getRideOwner().getUserImage());
            holder.rideId.setText(rideInPosition.getRideID());
            holder.rideDate.setText(Objects.toString(rideInPosition.getDate(),null));
            holder.freeSeats.setText(Objects.toString(rideInPosition.getFreeSeats(),null));
            holder.from.setText(rideInPosition.getFrom());
            holder.to.setText(rideInPosition.getTo());
            holder.plusButton.setTag(position);

            return convertView;
        }

        class ViewHolder
        {
            ImageView contactImage;
            Button plusButton;
            TextView firstName;
            TextView lastName;
            TextView rideId;
            TextView rideDate;
            TextView freeSeats;
            TextView from;
            TextView to;
        }

        public void add(Ride ride)
        {
            listData.add(ride);
            this.notifyDataSetChanged();

            //test
        }
    }
}