package adapters;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fragments.ChatFragment;
import fragments.ContactFragment;
import fragments.RequestFragment;

public class BabblePagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public BabblePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChatFragment();
            case 1:
                return new ContactFragment();
            case 2:
                return new RequestFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Chats";
            case 1:
                return "Contacts";
            case 2:
                return "Requests";
            default:
                return null;
        }
    }
}
