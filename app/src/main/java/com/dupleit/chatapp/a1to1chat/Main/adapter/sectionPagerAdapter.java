package com.dupleit.chatapp.a1to1chat.Main.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dupleit.chatapp.a1to1chat.fragments.ChatFrag.chatsFragment;
import com.dupleit.chatapp.a1to1chat.fragments.FriendsFrag.friendsFragment;

public class sectionPagerAdapter extends FragmentPagerAdapter{

    public sectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                chatsFragment chatsFrag = new chatsFragment();
                return chatsFrag;
            case 1:
                friendsFragment friendsFrag = new friendsFragment();
                return friendsFrag;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){

            case 0:
                return "CHATS";
            case 1:
                return "FRIENDS";
            default:
                return null;
        }
    }
}
