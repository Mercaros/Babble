package babble.babble.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import babble.babble.R;
import de.hdodenhof.circleimageview.CircleImageView;
import models.User;
import viewmodels.UserViewModel;

public class ProfileActivity extends AppCompatActivity {
    private TextView username;
    private TextView description;
    private TextView addFriendButton;
    private CircleImageView profileIcon;
    private User friend;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = findViewById(R.id.profile_activity_username);
        description = findViewById(R.id.profile_activity_description);
        profileIcon = findViewById(R.id.profile_activity_icon);
        addFriendButton = findViewById(R.id.addFriendButton);
        initValues();
        mUserViewModel = new UserViewModel(getApplicationContext());
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserViewModel.insert(friend);
                sendToMain();
                Toast.makeText(ProfileActivity.this, R.string.add_friend_confirm, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initValues() {
        friend = getIntent().getParcelableExtra(SearchFriendActivity.TAG);
        username.setText(friend.getUsername());
        description.setText(friend.getDescription());
        Picasso.get().load(friend.getImage()).into(profileIcon);
    }

    private void sendToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
