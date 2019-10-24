package com.example.messenger.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.R;


import java.util.ArrayList;
import java.util.List;

//import android.support.v7.widget.RecyclerView;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;

/**
 * Created by Jaison on 08/10/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable{

    public ArrayList<RecyclerViewModel> usersList = new ArrayList<>();
    private List<RecyclerViewModel> contactListFiltered;

    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTv;
        ImageView imageView;
        public CardView ll_listitem;

        public MyViewHolder(View view) {
            super(view);
//            subTitleTv = (TextView) view.findViewById(R.id.tv_posting);
            titleTv = (TextView) view.findViewById(R.id.tv_user_name);
            imageView = (ImageView) view.findViewById(R.id.thumbnail);
            ll_listitem = (CardView) view.findViewById(R.id.ll_listitem);

        }
    }


    public RecyclerViewAdapter(Context context, ArrayList<RecyclerViewModel> userList) {
        this.mContext = context;
        this.usersList = userList;
        this.contactListFiltered=userList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecyclerViewModel movie = usersList.get(position);
        holder.titleTv.setText(movie.getName());
//        holder.subTitleTv.setText(movie.getPosting());


    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                Log.d("qqq", "performFiltering: "+charString);
                if (charString.isEmpty()) {
                    contactListFiltered = usersList;
                } else {
                    List<RecyclerViewModel> filteredList = new ArrayList<>();
                    for (RecyclerViewModel row : usersList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<RecyclerViewModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public void removeItem(int position) {
        usersList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(RecyclerViewModel item, int position) {
        usersList.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<RecyclerViewModel> getData() {
        return usersList;
    }

}

