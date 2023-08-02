package com.example.foodscout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class HomeExclusiveAdapter extends RecyclerView.Adapter<HomeExclusiveAdapter.ViewHolder>{

List<HomeExclusiveModel>list;
List<HomeExclusiveModel>listFull;
boolean showShimmer=true;
Context context;
LayoutInflater inflater;

    public HomeExclusiveAdapter(Context ctx, List<HomeExclusiveModel>list) {

        this.inflater = LayoutInflater.from(ctx);;
        this.context = ctx;
        this.list=list;
        listFull=new ArrayList<>(list);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.exclusive_item_view,parent,false);
        return new HomeExclusiveAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HomeExclusiveAdapter.ViewHolder holder, int position) {

        if(showShimmer){
            holder.shimmerFrameLayout.startShimmer();
        }else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

           // holder.img.setBackground(null);
           // holder.title.setBackground(null);

            String img = list.get(position).getImg();
            String name = list.get(position).getName();
            Double price = list.get(position).getItem_price();
            String description = list.get(position).getDescription();
            holder.setName(name);
            holder.setImg(img);
            holder.setItm_price(price, position);
            holder.setDescription(description, position);
        }
    }

    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER=10;
        return showShimmer ? SHIMMER_ITEM_NUMBER : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShimmerFrameLayout shimmerFrameLayout;
        TextView title;
        ImageView img;
        Double Price;
        String pic,descr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shimmerFrameLayout=itemView.findViewById(R.id.shimmer_frame);
            title = itemView.findViewById(R.id.exclusive_txt);
            img = itemView.findViewById(R.id.exclusive_img);
        }

        private void setImg(String iconUrl) {
            if (!iconUrl.equals("null")) {
                Glide.with(itemView.getContext()).load(iconUrl).into(img);
            }
            pic=iconUrl;
        }

        private void setName(final String name) {
            title.setText(name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //recyclerViewClickInterface.onItemClick(getAdapterPosition());
                    Intent categoryIntent = new Intent(itemView.getContext(), ItemDetailsActivity.class );
                    categoryIntent.putExtra("ItemName", name);
                    categoryIntent.putExtra("Price", Price);
                    categoryIntent.putExtra("Pic", pic);
                    categoryIntent.putExtra("Desc", descr);
                    itemView.getContext().startActivity(categoryIntent);

                }
            });

        }
        private void setItm_price(final Double price, final int position) {
            Price=price;
        }

        private void setDescription(final String description, final int position){
            descr=description;
        }
    }
}
