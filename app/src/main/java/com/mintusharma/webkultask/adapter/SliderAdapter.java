package com.mintusharma.webkultask.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.mintusharma.webkultask.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder> {

    Context context;
    List<Bitmap> bitmapList;
    LayoutInflater layoutInflater;
    ViewPager2 viewPager2;

    public SliderAdapter(Context context, List<Bitmap> bitmapList, ViewPager2 viewPager2) {
        this.context = context;
        this.bitmapList = bitmapList;
        this.viewPager2 = viewPager2;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public SliderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(bitmapList.get(position));
        if (position == bitmapList.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.container);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            bitmapList.addAll(bitmapList);
            notifyDataSetChanged();
        }
    };
}
