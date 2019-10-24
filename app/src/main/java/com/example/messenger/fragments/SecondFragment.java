package com.example.messenger.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.messenger.R;
import com.example.messenger.RecyclerView.RecyclerViewAdapter;
import com.example.messenger.RecyclerView.RecyclerViewAdapterV2;
import com.example.messenger.RecyclerView.RecyclerViewModel;
import com.example.messenger.network.ApiInterface;
import com.example.messenger.network.RetrofitApiClient;
import com.example.messenger.retrofit.ServerResponse;
//import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SecondFragment extends Fragment {

    private RecyclerView recyclerView;
    RecyclerViewAdapterV2 recyclerViewAdapter;
    ArrayList<RecyclerViewModel> user_list = new ArrayList<>();
    ConstraintLayout constraintLayout;

    View view;


    ProgressDialog progressDialog;
    private ApiInterface apiInterface;

    int selfId;
    String email;

    public SecondFragment(String email){
        this.email=email;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_second, container, false);


        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.ConstraintLayout);

//        user_list.add(new RecyclerViewModel(1,"shimanto"));
//        user_list.add(new RecyclerViewModel(1,"shimanto"));
//        user_list.add(new RecyclerViewModel(1,"shimanto"));


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView11);
//        recyclerViewAdapter = new RecyclerViewAdapterV2(getContext(), user_list);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
////        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
//        recyclerView.setAdapter(recyclerViewAdapter);


        getSelfId(email);
//        getFriendData(7);

//        enableSwipeToDeleteAndUndo();

        Button acceptBtn,deleteBtn;
        acceptBtn=(Button)view.findViewById(R.id.accept_btn);
//        deleteBtn=(Button)view.findViewById(R.id.delete_btn);


        return view;
    }



    public void getFriendData(final int id) {

        progressDialog.show();
//        progressBar.setVisibility(View.VISIBLE);
        Call<List<RecyclerViewModel>> call = apiInterface.getAll(id);

        call.enqueue(new Callback<List<RecyclerViewModel>>() {

            @Override
            public void onResponse(Call<List<RecyclerViewModel>> call, Response<List<RecyclerViewModel>> response) {

//                progressBar.setVisibility(View.GONE);
                List<RecyclerViewModel> validity = response.body();
//                ipAddressTextView.setText(validity.getMessage());


                if (validity.size()!=0){
                    user_list.clear();
//                    user_list= (ArrayList<RecyclerViewModel>) validity;
                    for (int i=0;i<validity.size();i++){
                        user_list.add(validity.get(i));
//                        Log.d("kkk",validity.get(i).getName());
                    }
                    recyclerViewAdapter = new RecyclerViewAdapterV2(getContext(), user_list,id);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.notifyDataSetChanged();

                }

                Toast.makeText(getContext(),  "Data Loaded", Toast.LENGTH_LONG).show();

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call call, Throwable t) {
//                Log.e(TAG, t.toString());
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("aaa", "onFailure: " + t.getMessage());
                Log.d("aaa", "onFailure: ");

            }
        });


    }


    public int getSelfId(String email) {

        final int[] id = new int[1];

//        progressBar.setVisibility(View.VISIBLE);
        Call<ServerResponse> call = apiInterface.getSelfId(email);

        call.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

//                progressBar.setVisibility(View.GONE);
                ServerResponse validity = response.body();
//                ipAddressTextView.setText(validity.getMessage());
                Toast.makeText(getContext(), validity.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("aaa", "success: " + validity.getMessage());

                id[0] = Integer.parseInt(validity.getMessage());
                getFriendData(id[0]);

            }

            @Override
            public void onFailure(Call call, Throwable t) {
//                Log.e(TAG, t.toString());
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("aaa", "onFailure: " + t.getMessage());
                Log.d("aaa", "onFailure: ");

            }
        });

        return id[0];

    }



}