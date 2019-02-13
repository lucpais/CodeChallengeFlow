package com.lucas.concept.codechallengeflw;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucas.concept.codechallengeflw.controller.IItemClickListener;
import com.lucas.concept.codechallengeflw.controller.WeatherController;
import com.lucas.concept.codechallengeflw.model.Weather;
import com.squareup.picasso.Picasso;

import java.util.List;

class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {
    private final Context mContext;
    private List<Weather> mData;
    private IItemClickListener mClickListener;

    public WeatherAdapter(Context context) {
        mContext = context;
        mData = WeatherController.getInstance(mContext).getForecastData();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mMaxTemp;
        public TextView mMinTemp;
        public TextView mDescription;
        public TextView mDate;
        public ImageView mWeatherThumbnail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mMaxTemp = itemView.findViewById(R.id.forecast_max_temp);
            mMinTemp = itemView.findViewById(R.id.forecast_min_temp);
            mDescription = itemView.findViewById(R.id.forecast_short_desc);
            mDate = itemView.findViewById(R.id.forecast_date);
            mWeatherThumbnail = itemView.findViewById(R.id.forecast_image);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onClickDevice(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forecast_cell, viewGroup, false);
        WeatherAdapter.MyViewHolder vh = new WeatherAdapter.MyViewHolder(itemView);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.mMaxTemp.setText(mData.get(i).getTempMax());
        myViewHolder.mMinTemp.setText(mData.get(i).getTempMin());
        myViewHolder.mDate.setText(mData.get(i).getDate());
        myViewHolder.mDescription.setText(mData.get(i).getMainStatus() + ", " + mData.get(i).getDesc());
        Picasso.with(mContext)
                .load(WeatherController.baseWeatherICON + mData.get(i).getIconId() + ".png")
                .resize((int) mContext.getResources().getDimension(R.dimen.width_image_thumb), (int) mContext.getResources().getDimension(R.dimen.height_image_thumb))
                .error(android.R.drawable.ic_menu_camera)
                .placeholder(android.R.drawable.ic_menu_camera)
                .into(myViewHolder.mWeatherThumbnail);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setClickListener(IItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }
}
