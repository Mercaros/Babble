package babble.babble.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import adapters.SearchFriendAdapter;
import babble.babble.R;
import models.User;

public class SearchFriendActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView searchFriendList;
    private SearchFriendAdapter searchFriendAdapter;
    private List<User> userList;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    public static final String TAG = "FRIEND";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        toolbar = findViewById(R.id.searchFriendToolbar);
        searchFriendList = findViewById(R.id.searchFriendList);
        searchFriendList.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userList = getAllUsers();
        updateUI();

        //So you can go back to MainActivity if pressed back and shows the Title of the toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.search_friends_activity_name);
    }

    private void updateUI() {
        if (searchFriendAdapter == null) {
            searchFriendAdapter = new SearchFriendAdapter(userList, new SearchFriendAdapter.OnItemClickListener() {
                //Called when a user clicks on a item
                @Override
                public void onItemClick(User user) {
                    Intent intent = new Intent(SearchFriendActivity.this, ProfileActivity.class);
                    intent.putExtra(TAG, user);
                    startActivity(intent);
                }
            });
            searchFriendList.setAdapter(searchFriendAdapter);
        } else {
            //Refresh list
            searchFriendAdapter.swapList(userList);
        }
    }

    public List<User> getAllUsers() {
        final List<User> userList = new ArrayList<>();
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userList.add(snapshot.getValue(User.class));
                }
                getAllUsersCheck(userList);
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("tag", "Failed to read value.", databaseError.toException());
            }
        });
        return userList;
    }

    //Remove the current user of the Search Friend List
    private List<User> getAllUsersCheck(List<User> checkList) {
        Iterator<User> iter = checkList.iterator();

        while(iter.hasNext()) {
            User user = iter.next();
            if (user.getUid().equals(mAuth.getUid())) {
                iter.remove();
            }
        }
        return checkList;
    }
}
