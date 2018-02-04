package com.example.lohiaufung.lab11.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lohiaufung.lab11.R;
import com.example.lohiaufung.lab11.ReposActivity;
import com.example.lohiaufung.lab11.model.User;

import java.util.List;

/**
 * Created by LoHiaufung on 2017/12/18.
 */

public class userAdapter extends RecyclerView.Adapter<userAdapter.ViewHolder> {
    private List<User> mUserList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userId, blog;
        View userView;
        public ViewHolder(View view) {
            super(view);
            userView = view;
            userName = (TextView)view.findViewById(R.id.userNameInUserListItem);
            userId = (TextView)view.findViewById(R.id.userIdInUserListItem);
            blog = (TextView)view.findViewById(R.id.userBlogInUserListItem);
        }
    }

    public userAdapter(List<User> list) {
        mUserList = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position  = holder.getAdapterPosition();
                User user = mUserList.get(position);
                Intent intent = new Intent(view.getContext(),ReposActivity.class);
                intent.putExtra("name", user.getLogin());
                view.getContext().startActivity(intent);
            }
        });
        holder.userView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
                mUserList.remove(position);
                notifyDataSetChanged();
                return false;
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUserList.get(position);
        holder.userName.setText(user.getLogin());
        holder.userId.setText(""+user.getId());
        holder.blog.setText(user.getBlog());
    }
    @Override
    public int getItemCount() {
        return mUserList.size();
    }
}