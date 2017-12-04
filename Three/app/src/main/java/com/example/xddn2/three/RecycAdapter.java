package com.example.xddn2.three;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by LoHiaufung on 2017/11/24.
 */

public class RecycAdapter extends RecyclerView.Adapter<RecycAdapter.ViewHolder> {
    private List<MyPerson> mPersonList;

    static class ViewHolder extends  RecyclerView.ViewHolder {
        View personView;
        TextView mPersonCountry;
        CircleImageView mPersonImage;
        TextView mPersonName;
        Context mContext;

        public ViewHolder(View view) {
            super(view);
            personView = view;
            mPersonCountry = (TextView)view.findViewById(R.id.country);
            mPersonImage = (CircleImageView)view.findViewById(R.id.item_image);
            mPersonName = (TextView)view.findViewById( R.id.item_name);
            mContext = view.getContext();
        }
    }
    public RecycAdapter(List<MyPerson> inList) {
        mPersonList = inList;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.personView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                MyPerson person = mPersonList.get(position);
                // 跳转到详情界面
                Gson gson = new Gson();
                String personInString = gson.toJson(person);
                Intent intent = new Intent(parent.getContext(), DetailActivity.class);
                intent.putExtra("theSelectedPerson", personInString);
                intent.putExtra("action", "look");
                v.getContext().startActivity(intent);
            }
        });

        //删除
        holder.personView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                Intent intentBroadcast = new Intent("Delete");
                intentBroadcast.putExtra("name", mPersonList.get(position).getName());
                parent.getContext().sendBroadcast(intentBroadcast);
                return true;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyPerson person = mPersonList.get(position);
        holder.mPersonName.setText(person.getName());
        if (person.hasImageUri()) {
            // 如果用户设定了图片，则使用
            Glide.with(holder.mContext).load(person.getImageUri()).into(holder.mPersonImage);
        } else {
            // 否则使用默认图片
            holder.mPersonImage.setImageResource(person.getImage());
        }
        holder.mPersonCountry.setText(person.getBossdom());
    }

    @Override
    public int getItemCount() {
        return mPersonList.size();
    }
}
