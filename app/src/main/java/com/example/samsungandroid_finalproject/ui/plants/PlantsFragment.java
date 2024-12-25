package com.example.samsungandroid_finalproject.ui.plants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.samsungandroid_finalproject.MyGardenApp;
import com.example.samsungandroid_finalproject.data.AppDatabase;
import com.example.samsungandroid_finalproject.data.ImageManager;
import com.example.samsungandroid_finalproject.data.PlantManager;
import com.example.samsungandroid_finalproject.ui.MainActivity;
import com.example.samsungandroid_finalproject.databinding.FragmentPlantsBinding;
import com.example.samsungandroid_finalproject.domain.Plant;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlantsFragment extends Fragment {
    private FragmentPlantsBinding binding;
    private PlantsAdapter plantsAdapter;
    private PlantsViewModel viewModel;
    private final ImageManager imageManager = new ImageManager();
    private OnItemClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlantsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new PlantsViewModel(
                        new PlantManager(
                                AppDatabase.getAppDatabase(requireActivity().getApplicationContext()).plantDao(),
                                ((MyGardenApp) requireActivity().getApplication()).getExecutor(),
                                requireActivity().getApplicationContext()
                        )
                );
            }
        }).get(PlantsViewModel.class);

        setListeners();

        plantsAdapter = new PlantsAdapter(listener, getPlantImagesDir());
        binding.plants.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        binding.plants.setItemAnimator(new DefaultItemAnimator());
        binding.plants.setEmptyView(binding.emptyPlantsText);
        binding.plants.setAdapter(plantsAdapter);

        viewModel.getPlants().observe(getViewLifecycleOwner(), new Observer<List<Plant>>() {
            @Override
            public void onChanged(List<Plant> plants) {
                if (plants != null) {
                    plantsAdapter.setPlants(plants);
                }
            }
        });
    }

    private File getPlantImagesDir() {
        File dir = null;
        try {
            dir = imageManager.getPlantImagesDir(requireActivity().getApplicationContext());
        } catch (IOException e) {
            Toast.makeText(
                    requireActivity(),
                    "Не удалось получить папку с фотографиями растений",
                    Toast.LENGTH_SHORT).show();
        }
        return dir;
    }

    private void setListeners() {
        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                // Запуск фрагмента с информацией о растении
                ((MainActivity) requireActivity()).launchPlantFragment(id);
            }
        };

        binding.addPlantFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запуск фрагмента для добавления растения
                ((MainActivity) requireActivity()).launchAddEditPlantFragment(0);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
