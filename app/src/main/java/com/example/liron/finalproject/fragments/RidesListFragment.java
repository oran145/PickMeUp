package com.example.liron.finalproject.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.liron.finalproject.MyAppContext;
import com.example.liron.finalproject.R;
import com.example.liron.finalproject.model.Model;
import com.example.liron.finalproject.model.Ride;
import com.example.liron.finalproject.model.User;

import java.util.ArrayList;


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

            public void hideProgressBar() {
                delegate.hideProgressBar();
            }


        });

        // Inflate the layout for this fragment
        return view;
    }

    public class RidesListAdapter extends BaseAdapter {
        private ArrayList listData;
        private LayoutInflater layoutInflater;

        public RidesListAdapter(Context context, ArrayList listData) {
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
//            ViewHolder holder;
//            if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row, null);
//                holder = new ViewHolder();
//                holder.contactImage = (ImageView) convertView.findViewById(R.id.list_row_contactImage_imageView);
//                holder.firstName = (TextView) convertView.findViewById(R.id.list_row_firstName_textView);
//                holder.lastName = (TextView) convertView.findViewById(R.id.list_row_lastName_textView);
//                holder.userId = (TextView) convertView.findViewById(R.id.list_row_invisibleUserId_textView);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            if (listData == null) {
//                return convertView;
//            }
//            User userInPosition = (User) listData.get(position);
//            holder.firstName.setText(userInPosition.getFirstName());
//            holder.lastName.setText(userInPosition.getLastName());
//            holder.contactImage.setImageBitmap(userInPosition.getUserImage());
//            holder.userId.setText(userInPosition.getUserID());

            return convertView;
        }

        public void add(Ride ride)
        {
            listData.add(ride);
            this.notifyDataSetChanged();

        }
    }
}