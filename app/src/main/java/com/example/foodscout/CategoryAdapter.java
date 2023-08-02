package com.example.foodscout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements Filterable {

    List<CategoryModel>categoryList;
    List<CategoryModel>categoryListFull;
    boolean showShimmer=true;
    //List<CategoryModel> titles;
  //  List<CategoryModel> images;
    LayoutInflater inflater;
    Context context;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    CategoryFoodActivity fetch;

    public CategoryAdapter(Context ctx,List<CategoryModel> categoryList,RecyclerViewClickInterface recyclerViewClickInterface) {
        //this.titles = titles;
        //this.images = images;
        this.inflater = LayoutInflater.from(ctx);
        this.context= ctx;
        this.categoryList= categoryList;
        categoryListFull=new ArrayList<>(categoryList);
        this.recyclerViewClickInterface=recyclerViewClickInterface;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.category_grid_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     //holder.title.setText(titles.get(position).getName());
     //holder.images1.setImageResource(images.get(position).getImage());
        if(showShimmer){
            holder.shimmerFrameLayout.startShimmer();
        }else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            holder.image.setBackground(null);
            holder.title.setBackground(null);

            String pic = categoryList.get(position).getImage();
            String name = categoryList.get(position).getName();
            holder.setCategoryName(name, position);
            holder.setCategoryImage(pic);
        }

    }

    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER=10;
        return showShimmer ? SHIMMER_ITEM_NUMBER : categoryList.size();
    }

    @Override
    public Filter getFilter() {
        return categoryListFilter;
    }

    private Filter categoryListFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CategoryModel> filteredList=new ArrayList<>();


            if(constraint==null || constraint.length()==0){
                filteredList.addAll(categoryListFull);
            }else {
                String filterPattern=constraint.toString().toLowerCase();

                for (CategoryModel item :categoryListFull){
                    if(item.getName().toLowerCase().contains(filterPattern)){
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
            categoryList.clear();
            categoryList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        ShimmerFrameLayout shimmerFrameLayout;
        TextView title;
        ImageView image;
        View v;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shimmerFrameLayout=itemView.findViewById(R.id.shimmer_frame);
            title=itemView.findViewById(R.id.catg_txt);
            image=itemView.findViewById(R.id.catg_img);
            this.v=itemView;
        }
        private void setCategoryImage(String iconUrl) {
            if(!iconUrl.equals("null")) {
                Glide.with(v.getContext()).load(iconUrl).into(image);
            }
        }

        private void setCategoryName(final String name, final int position){
            title.setText(name);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //recyclerViewClickInterface.onItemClick(getAdapterPosition());
                    Intent categoryIntent = new Intent(itemView.getContext(), ItemsListActivity.class);
                    categoryIntent.putExtra("CategoryName", name);
                    itemView.getContext().startActivity(categoryIntent);

                }
            });


            }
        }


    }

