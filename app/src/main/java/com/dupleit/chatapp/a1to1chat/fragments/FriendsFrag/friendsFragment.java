package com.dupleit.chatapp.a1to1chat.fragments.FriendsFrag;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dupleit.chatapp.a1to1chat.R;
import com.dupleit.chatapp.a1to1chat.chat.chatActivity;
import com.dupleit.chatapp.a1to1chat.fragments.FriendsFrag.adapter.allFriendsAdapter;
import com.dupleit.chatapp.a1to1chat.fragments.FriendsFrag.model.getFriendsData;
import com.dupleit.chatapp.a1to1chat.helper.checkInternetState;
import com.dupleit.chatapp.a1to1chat.profile.profileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Random;

import es.dmoral.toasty.Toasty;


public class friendsFragment extends Fragment implements allFriendsAdapter.ContactsAdapterListener{

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView noDataFound;
    TextView noSearchResultFound;
    private ArrayList<getFriendsData> friendsList;
    private allFriendsAdapter adapter;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    View mView;
    private static final String TAG = "friendsFragment";
    String currentUserId;
    public friendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView= inflater.inflate(R.layout.fragment_friends, container, false);
        initilize(mView);
        return mView;
    }

    private void initilize(View mView) {

        recyclerView = mView.findViewById(R.id.recyclerView);
        progressBar = mView.findViewById(R.id.progressBar);
        noDataFound = mView.findViewById(R.id.noDataFound);
        noSearchResultFound = mView.findViewById(R.id.noSearchResultFound);
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mUserDatabase.keepSynced(true);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = mCurrentUser.getUid();
        friendsList = new ArrayList<>();
        adapter = new allFriendsAdapter(mView.getContext(), friendsList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mView.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



        if (!checkInternetState.getInstance(mView.getContext()).isOnline()) {
            Toasty.warning(mView.getContext(), "No internet connection.", Toast.LENGTH_SHORT, true).show();

        }else {
            progressBar.setVisibility(View.VISIBLE);
            prepareFriendList();
        }


    }

    private void prepareFriendList() {
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);

                friendsList.clear();

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    getFriendsData userdetails = dataSnapshot1.getValue(getFriendsData.class);
                    Object obj = dataSnapshot1.getKey();//for getting key

                    Log.e(TAG, "onDataChange: "+obj );
                    friendsList.add(userdetails);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("databaseError",""+error);
                progressBar.setVisibility(View.GONE);

            }
        });
    }


    @Override
    public void onContactSelected(getFriendsData getfriendsData) {

        Intent intent = new Intent(mView.getContext(),chatActivity.class);
        intent.putExtra("recUserId",getfriendsData.getUid());
        intent.putExtra("recUserName",getfriendsData.getName());
        startActivity(intent);

    }
}
