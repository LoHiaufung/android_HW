package com.example.lohiaufung.lab5;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
;
/**
 * Created by LoHiaufung on 2017/10/20.
 */

public class GoodAdapter extends RecyclerView.Adapter<GoodAdapter.ViewHolder> {

    private List<Good> mGoodList;
    // 内部类viewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView goodName, alphaInCircle;
        // the most outside layer of Item
        View GoodItemView;

        // 内部类viewHolder的构造函数
        public ViewHolder(View view) {
            super(view);
            GoodItemView = view;
            goodName = (TextView)view.findViewById(R.id.goodItemName);
            alphaInCircle = (TextView)view.findViewById(R.id.alphaInTheCircle);
        }
    }

    public GoodAdapter(List<Good> foodList) {
        mGoodList = foodList;
    }

    // 用于创建ViewHolder实例
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_list_item_view, parent, false);
        // 以下，为Item子元素添加控件
        final ViewHolder holder = new ViewHolder(view);
        final ViewGroup par = parent;
        //  为view添加
        holder.GoodItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int postion = holder.getAdapterPosition();
                Good good = mGoodList.get(postion);
                // Toast.makeText(view.getContext(),"You clicked view " + good.getName(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(par.getContext(), GoodDetailActivity.class);
                intent.putExtra("goodName", good.getName());
                intent.putExtra("price", good.getPrice());
                intent.putExtra("goodType", good.getInfoKind());
                intent.putExtra("goodInfo", good.getInfo());
                intent.putExtra("imageSrcID", good.getImageId());
                intent.putExtra("isInShoppingCar", good.getInShoppingCar());

                ((Activity)(par.getContext())).startActivityForResult(intent, 1);

            }
        });
       holder.GoodItemView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               int postion = holder.getAdapterPosition();
               mGoodList.remove(postion);
               notifyDataSetChanged();
               Toast.makeText(v.getContext(),"移除第" + String.valueOf(postion+1) + "个商品",Toast.LENGTH_SHORT).show();
               // 该事件接收后不再向外抛出
               return true;
           }
       });


        // 以上，为Item子元素添加控件
        return holder;
    }

    // 当Item被滚动到屏幕时
    @Override
    public void onBindViewHolder(ViewHolder holder, int postion) {
        Good good = mGoodList.get(postion);
        holder.goodName.setText(good.getName());
        holder.alphaInCircle.setText((""+ good.getName().charAt(0)).toUpperCase());
    }

    @Override
    public int getItemCount() {
        return mGoodList.size();
    }
}
