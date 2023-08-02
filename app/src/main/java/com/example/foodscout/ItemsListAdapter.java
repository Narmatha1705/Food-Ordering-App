package com.example.foodscout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ViewHolder> implements Filterable {
    Context context;
    LayoutInflater inflate;
    List<ItemsListModel>itemsList;
    List<ItemsListModel>itemsListFull;
    boolean showShimmer=true;
    RecyclerViewClickInterface cart,click;

    public ItemsListAdapter(){

    }

    public ItemsListAdapter(Context ctx, List<ItemsListModel> itemsList,RecyclerViewClickInterface cart,RecyclerViewClickInterface click){
        this.context=ctx;
        this.inflate=LayoutInflater.from(ctx);
        this.itemsList=itemsList;
        itemsListFull=new ArrayList<>(itemsList);
        this.cart=cart;
        this.click=click;
    }
   // public ItemsListAdapter(RecyclerViewClickInterface cart){
       // this.cart=cart;
   // }

    @NonNull
    @Override
    public ItemsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflate.inflate(R.layout.items_list_view_layout,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemsListAdapter.ViewHolder holder, int position) {

        if(showShimmer){
            holder.shimmerFrameLayout.startShimmer();
        }else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            holder.itm_img.setBackground(null);
            holder.itm_name.setBackground(null);
            holder.itm_price.setBackground(null);
           // holder.addCart.setBackgroundColor(Color.parseColor("#97351E"));
          //  holder.addCart.setText("Add to Cart");

            String img = itemsList.get(position).getItem_img();
            String name = itemsList.get(position).getItem_name();
            Double price = itemsList.get(position).getItem_price();
            String description = itemsList.get(position).getDescription();
            holder.setItm_name(name, position);
            holder.setItm_img(img);
            holder.setItm_price(price, position);
            holder.setDescription(description, position);
            /*holder.addCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();
                }
            });*/
        }

    }

    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER=itemsList.size();
        return showShimmer ? SHIMMER_ITEM_NUMBER : itemsList.size();
    }

    @Override
    public Filter getFilter() {
        return itemsListFilter;
    }

    private Filter itemsListFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ItemsListModel> filteredList=new ArrayList<>();


            if(constraint==null || constraint.length()==0){
                filteredList.addAll(itemsListFull);
            }else {
                String filterPattern=constraint.toString().toLowerCase();

                for (ItemsListModel item :itemsListFull){
                    if(item.getItem_name().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results= new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemsList.clear();
            itemsList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        ShimmerFrameLayout shimmerFrameLayout;
        TextView itm_name,itm_price;
        ImageView itm_img;
        //final Button addCart;
        View itemClick;
        String pic,descr;
        Double Price;
        int pics;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shimmerFrameLayout=itemView.findViewById(R.id.shimmer_frame);
            itm_name=(TextView) itemView.findViewById(R.id.itemName);
            itm_price=(TextView)itemView.findViewById(R.id.itemPrice);
            itm_img=(ImageView)itemView.findViewById(R.id.itemImage);
            //addCart=(Button)itemView.findViewById(R.id.addToCart);
            itemClick=itemView;
        }

        private void setItm_img(String iconUrl) {
            if(!iconUrl.equals("null")) {
                Glide.with(itemView.getContext()).load(iconUrl).into(itm_img);
            }
            pic=iconUrl;
        }

        private void setItm_name(final String name, final int position) {
            itm_name.setText(name);

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
            itm_price.setText(String.valueOf(price));
            Price=price;

        }

        private void setDescription(final String description, final int position){
            descr=description;
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
           // click.onItemClick(itemsList.get(position));
            cart.onButtonClick(itemsList.get(position));

        }
    }


}
