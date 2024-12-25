package com.example.samsungandroid_finalproject.ui.plants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.samsungandroid_finalproject.R;
import com.example.samsungandroid_finalproject.databinding.PlantItemBinding;
import com.example.samsungandroid_finalproject.domain.Plant;

import java.io.File;
import java.util.List;

public class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.PlantViewHolder> {
    private List<Plant> plants;
    private final OnItemClickListener listener;
    private final File plantImagesDir;

    public PlantsAdapter(OnItemClickListener listener, File plantImagesDir) {
        this.listener = listener;
        this.plantImagesDir = plantImagesDir;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlantItemBinding binding = PlantItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PlantViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plants.get(position);
        holder.bind(plant, listener, plantImagesDir);
    }

    @Override
    public int getItemCount() {
        return plants == null ? 0 : plants.size();
    }

    // Устанавливаем (обновляем) список растений
    public void setPlants(List<Plant> plants) {
        this.plants = plants;
        notifyDataSetChanged();
    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder {
        private final PlantItemBinding binding;

        public PlantViewHolder(PlantItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Plant plant, OnItemClickListener listener, File plantImagesDir) {
            binding.image.setImageBitmap(null);
            if (plantImagesDir != null && plant.getImagePath() != null) {
                File plantImageFile = new File(plantImagesDir, plant.getImagePath());

                Glide.with(binding.getRoot().getContext())
                        .load(plantImageFile)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.image);
            } else {
                binding.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                binding.image.setImageDrawable(ResourcesCompat.getDrawable(
                        binding.getRoot().getResources(),
                        R.drawable.plant_placeholder,
                        null)
                );
            }

            binding.name.setText(plant.getName());
            binding.variety.setText(plant.getVariety());

            // Устанавливаем обработчик нажатия на карточку с растением
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(plant.getId());
                }
            });
        }
    }
}
