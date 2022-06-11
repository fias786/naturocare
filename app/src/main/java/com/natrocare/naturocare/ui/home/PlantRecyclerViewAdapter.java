package com.natrocare.naturocare.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.natrocare.naturocare.R;

import java.util.ArrayList;

public class PlantRecyclerViewAdapter extends RecyclerView.Adapter<PlantRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<PlantData> data;

    public PlantRecyclerViewAdapter(Context context, ArrayList<PlantData> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public PlantRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_list,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlantRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.plantImage.setImageResource(data.get(position).getPlantImage());
        holder.plantName.setText(data.get(position).getPlantName());
        holder.plantTime.setText(data.get(position).getPlantTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView plantImage;
        TextView plantName,plantTime;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            plantImage = itemView.findViewById(R.id.PlantImage);
            plantName = itemView.findViewById(R.id.PlantName);
            plantTime = itemView.findViewById(R.id.PlantTime);
        }
    }
}
