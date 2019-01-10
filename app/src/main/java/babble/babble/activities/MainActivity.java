package babble.babble.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import adapters.BabblePagerAdapter;
import babble.babble.R;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout fragmentTabs;
    private ViewPager fragmentPager;
    private BabblePagerAdapter babblePagerAdapter;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentPager = findViewById(R.id.fragmentPager);
        toolbar = findViewById(R.id.main_toolbar);
        fragmentTabs = findViewById(R.id.fragmentTabs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();
        babblePagerAdapter = new BabblePagerAdapter(getSupportFragmentManager());
        fragmentPager.setAdapter(babblePagerAdapter);
        fragmentTabs.setupWithViewPager(fragmentPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //See if user is logged in otherwise send him to Loginactivity
        if (currentUser == null) {
            sendToLogin();
        } else {
            verifyUser();
        }
    }

    public void verifyUser() {
        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("username").exists())) {

                } else {
                    sendToSetting();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("tag", "Failed to read value.", databaseError.toException());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_search_friend:
                sendToSearchFriend();
                return true;
            case R.id.menu_settings:
                sendToSetting();
                return true;
            case R.id.menu_logout:
                mAuth.signOut();
                sendToLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Die flags zorgen ervoor dat de gebruiker niet terug kan gaan naar de Mainactivity, Pas als ze klaar zijn bij de settingsactivity
    private void sendToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void sendToSetting() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    private void sendToSearchFriend(){
        Intent intent = new Intent(this, SearchFriendActivity.class);
        startActivity(intent);
    }
}
