package adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import babble.babble.R;
import de.hdodenhof.circleimageview.CircleImageView;
import models.User;

public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.MyViewHolder> {
    private List<User> userList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public SearchFriendAdapter(List<User> userList, OnItemClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final User user  = userList.get(position);
        Picasso.get().load(user.getImage()).into(holder.profileIcon);
        holder.username.setText(user.getUsername());
        holder.description.setText(user.getDescription());
        holder.bind(userList.get(position), listener);
    }

    @NonNull
    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView profileIcon;
        public TextView username;
        public TextView description;


        public MyViewHolder(View view) {
            super(view);
            profileIcon = view.findViewById(R.id.profileIconRow);
            username = view.findViewById(R.id.usernameRow);
            description = view.findViewById(R.id.descriptionRow);
        }

        public void bind(final User user, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(user);
                }
            });
        }
    }

    public void swapList(List<User> newList) {
        userList = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
}
