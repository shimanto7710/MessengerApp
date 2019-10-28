package com.example.messenger.RecyclerView;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.R;
import com.example.messenger.network.ApiInterface;
import com.example.messenger.network.RetrofitApiClient;
import com.example.messenger.retrofit.ServerResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import android.support.v7.widget.RecyclerView;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;

/**
 * Created by Jaison on 08/10/16.
 */

public class RecyclerViewAdapterV2 extends RecyclerView.Adapter<RecyclerViewAdapterV2.MyViewHolder> implements Filterable{
    private Handler mHandler;
    public ArrayList<RecyclerViewModel> usersList = new ArrayList<>();
    private List<RecyclerViewModel> contactListFiltered;
    int selfId,friendId;

    Context mContext;
    private ApiInterface apiInterface;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTv;
        ImageView imageView;
        public CardView ll_listitem;
        public Button button;

        public MyViewHolder(View view) {
            super(view);
//            subTitleTv = (TextView) view.findViewById(R.id.tv_posting);
            titleTv = (TextView) view.findViewById(R.id.tv_user_name);
            imageView = (ImageView) view.findViewById(R.id.thumbnail);
            ll_listitem = (CardView) view.findViewById(R.id.ll_listitem);
             button=(Button)view.findViewById(R.id.accept_btn);

        }
    }


    public RecyclerViewAdapterV2(Context context, ArrayList<RecyclerViewModel> userList,int selfId) {
        this.mContext = context;
        this.usersList = userList;
        this.contactListFiltered=userList;
        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
        this.selfId=selfId;
        mHandler = new Handler(Looper.getMainLooper());

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_layout_v2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final RecyclerViewModel movie = usersList.get(position);
        holder.titleTv.setText(movie.getName());

//        holder.subTitleTv.setText(movie.getPosting());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                acceptFriendRequest(selfId,movie.getId(),position);

                Log.d("vvvvv",selfId+" "+movie.getId());
            }
        });

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

    public void acceptFriendRequest(int selfId, int friendId, final int pos){

//        progressDialog.show();

        Call<ServerResponse> call = apiInterface.makeFriend(selfId,friendId);
        call.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

//                progressBar.setVisibility(View.GONE);
                ServerResponse validity = response.body();
//                ipAddressTextView.setText(validity.getMessage());
//                Toast.makeText(getApplicationContext(), validity.getMessage(), Toast.LENGTH_LONG).show();
                if (validity.isSuccess()) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            usersList.remove(pos);
                            notifyDataSetChanged();
                        }
                    });

                    Log.d("aaa", "success: ");
                } else {

                }


            }

            @Override
            public void onFailure(Call call, Throwable t) {
//                Log.e(TAG, t.toString());
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("aaa", "onFailure: " + t.getMessage());
                Log.d("aaa", "onFailure: ");

            }
        });
    }

}

