package viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import models.User;
import repository.UserRepo;

public class UserViewModel extends ViewModel {

    private UserRepo userRepo;
    private LiveData<List<User>> userList;

    public UserViewModel(Context context) {
        userRepo = new UserRepo(context);
        userList = userRepo.getAllContacts();
    }


    public LiveData<List<User>> getAllContacts() {
        return userList;
    }


    public void insert(User user) {
        userRepo.insertToRoom(user);
    }


    public void update(User user) {
       userRepo.updateToRoom(user);
    }


    public void delete(User user) {
        userRepo.deleteFromRoom(user);
    }


}
