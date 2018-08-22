package com.dupleit.chatapp.a1to1chat.fragments.FriendsFrag.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dupleit.chatapp.a1to1chat.R;
import com.dupleit.chatapp.a1to1chat.fragments.FriendsFrag.model.getFriendsData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mandeep on 4/9/17.
 */

public class allFriendsAdapter extends RecyclerView.Adapter<allFriendsAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    public ArrayList<getFriendsData> studentList;
    private List<getFriendsData> studentListFiltered;
    private ContactsAdapterListener listener;
    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public CircleImageView friendImage;
        public ImageView checked_online;
        public TextView friendName, friendStatus;
        public LinearLayout mCardView;
        public MyViewHolder(View view) {
            super(view);
            friendImage = view.findViewById(R.id.friendImage);
            friendName = view.findViewById(R.id.friendName);
            friendStatus = view.findViewById(R.id.friendStatus);
            checked_online= view.findViewById(R.id.checked_online);
            mCardView = view.findViewById(R.id.card_view);
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(studentListFiltered.get(getAdapterPosition()));
                }
            });

        }


    }
    public allFriendsAdapter(Context mContext, ArrayList<getFriendsData> studentList, ContactsAdapterListener listener) {
        this.mContext = mContext;
        this.studentList = studentList;
        this.studentListFiltered = studentList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_friends, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        getFriendsData friends = studentListFiltered.get(position);
        holder.mCardView.setTag(position);
        if (!friends.getImage().equals("default")){

            Glide.with(mContext).load(friends.getImage()).into(holder.friendImage);
        }else {
            Glide.with(mContext).load(R.drawable.ic_account_circle_black_36dp).into(holder.friendImage);
        }
        if (friends.getOnline().equals("true")){
            holder.checked_online.setVisibility(View.VISIBLE);
        }else {
            holder.checked_online.setVisibility(View.GONE);
        }
        holder.friendName.setText(friends.getName());

        if (!friends.getStatus().equals("")){
            holder.friendStatus.setText(friends.getStatus());
        }
    }

    @Override
    public int getItemCount() {
        return studentListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    studentListFiltered = studentList;
                } else {
                    List<getFriendsData> filteredList = new ArrayList<>();
                    for (getFriendsData row : studentList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName()
                                .toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    studentListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = studentListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                studentListFiltered = (ArrayList<getFriendsData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public interface ContactsAdapterListener {
        void onContactSelected(getFriendsData getStudentData);
    }
}
