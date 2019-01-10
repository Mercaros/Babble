package fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import adapters.SearchFriendAdapter;
import babble.babble.R;
import models.User;
import viewmodels.UserViewModel;

public class ContactFragment extends Fragment {
    private List<User> contactList;
    private SearchFriendAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private UserViewModel mUserViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserViewModel = new UserViewModel(getContext());
        mUserViewModel.getAllContacts().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> contacts) {
                contactList = contacts;
                updateUI();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        mRecyclerView = view.findViewById(R.id.contactList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new SearchFriendAdapter(contactList, new SearchFriendAdapter.OnItemClickListener() {
                //Called when a user clicks on a item
                @Override
                public void onItemClick(User user) {
                    createDialog(user).show();
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        } else {
            //Refresh list
            mAdapter.swapList(contactList);
        }
    }

    private Dialog createDialog(final User user){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to delete this contact?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mUserViewModel.delete(user);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
