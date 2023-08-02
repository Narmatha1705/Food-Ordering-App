package com.example.foodscout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ImageSliderAdapter extends SliderViewAdapter<SliderViewHolder> {

    Context context;
    List<ImageSliderModel> imageSliderModelList;
    //boolean showShimmer=true;

    public ImageSliderAdapter(Context context, List<ImageSliderModel> imageSliderModelList) {
        this.context = context;
        this.imageSliderModelList = imageSliderModelList;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,parent,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
            viewHolder.sliderImageView.setImageResource(imageSliderModelList.get(position).getImage());

    }

    @Override
    public int getCount() {
        //int SHIMMER_ITEM_NUMBER=imageSliderModelList.size();
        return imageSliderModelList.size();
                //showShimmer ? SHIMMER_ITEM_NUMBER :
    }
}

class SliderViewHolder extends SliderViewAdapter.ViewHolder{

    ImageView sliderImageView;
    //ShimmerFrameLayout shimmerFrameLayout;
    public SliderViewHolder(View itemView) {
        super(itemView);
        //shimmerFrameLayout=itemView.findViewById(R.id.shimmer_frame);
        sliderImageView=itemView.findViewById(R.id.slideritem);
    }
}