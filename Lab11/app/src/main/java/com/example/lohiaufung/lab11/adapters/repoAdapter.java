package com.example.lohiaufung.lab11.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lohiaufung.lab11.R;
import com.example.lohiaufung.lab11.model.Repo;
import com.example.lohiaufung.lab11.model.User;

import java.util.List;

/**
 * Created by LoHiaufung on 2017/12/18.
 */

public class repoAdapter extends RecyclerView.Adapter<repoAdapter.ViewHolder> {
    private List<Repo> mRepoList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView repoName, language, description;
        public ViewHolder(View view) {
            super(view);
            repoName = (TextView)view.findViewById(R.id.repoNameInRepoListItem);
            language = (TextView)view.findViewById(R.id.languageInRepoListItem);
            description = (TextView)view.findViewById(R.id.descriptionInRepoListItem);
        }
    }

    public repoAdapter(List<Repo> list) {
        mRepoList = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_list_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Repo repo = mRepoList.get(position);
        holder.repoName.setText(repo.getName());
        holder.language.setText(repo.getLanguage());
        holder.description.setText(repo.getDescription());
    }
    @Override
    public int getItemCount() {
        return mRepoList.size();
    }
}