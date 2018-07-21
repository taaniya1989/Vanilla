package edu.sdsu.tvidhate.vanilla;

import android.content.Context;
import android.graphics.Movie;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter <PlacesAdapter.MyViewHolder>{

    private List<Place> placesList;
    private Context appContext;

    public PlacesAdapter(List<Place> placesList, Context appContext) {
        this.placesList = placesList;
        this.appContext = appContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Place place = placesList.get(position);
        int width= appContext.getResources().getDisplayMetrics().widthPixels;
        int height= appContext.getResources().getDisplayMetrics().heightPixels;
        if (place.getmPlaceImageURL() != null)
            Picasso.with(appContext).load(place.getmPlaceImageURL()).resize(width/2,height/4).into(holder.placeImage);
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView placeImage;

        public MyViewHolder(View view) {
            super(view);
            placeImage = view.findViewById(R.id.placeImage);
        }
    }
}
