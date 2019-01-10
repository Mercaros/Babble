package repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import db.AppDatabase;
import db.UserDao;
import models.User;

public class UserRepo {
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private Context context;
    private AppDatabase mAppDatabase;
    private UserDao userDao;
    private LiveData<List<User>> userList;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public UserRepo(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAppDatabase = AppDatabase.getInstance(context);
        userDao = mAppDatabase.userDao();
        userList = userDao.getAllUser();
    }

    public void updateUser(HashMap<String, String> profileMap) {
        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(profileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Update success
                            Toast.makeText(context, "Profile updated", Toast.LENGTH_LONG).show();

                        } else {
                            // Update failed
                            Toast.makeText(context, "Profile isn't updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public List<User> getAllUsersFromFirebase() {
        final List<User> userList = new ArrayList<>();
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userList.add(snapshot.getValue(User.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("tag", "Failed to read value.", databaseError.toException());
            }
        });
        return userList;
    }

    public LiveData<List<User>> getAllContacts() {
        return userList;
    }

    public void insertToRoom(final User user) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                userDao.insert(user);
            }
        });
    }


    public void updateToRoom(final User user) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                userDao.update(user);
            }
        });
    }


    public void deleteFromRoom(final User user) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                userDao.delete(user);
            }
        });
    }

}
