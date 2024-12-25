package com.example.samsungandroid_finalproject.ui.plant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.samsungandroid_finalproject.MyGardenApp;
import com.example.samsungandroid_finalproject.R;
import com.example.samsungandroid_finalproject.data.AppDatabase;
import com.example.samsungandroid_finalproject.data.ImageManager;
import com.example.samsungandroid_finalproject.data.PlantManager;
import com.example.samsungandroid_finalproject.databinding.FragmentPlantBinding;
import com.example.samsungandroid_finalproject.domain.DateConverter;
import com.example.samsungandroid_finalproject.domain.Plant;
import com.example.samsungandroid_finalproject.ui.ConfirmDialog;
import com.example.samsungandroid_finalproject.ui.FragmentAction;
import com.example.samsungandroid_finalproject.ui.MainActivity;

import java.io.File;
import java.io.IOException;

public class PlantFragment extends Fragment {
    private FragmentPlantBinding binding;
    private PlantViewModel viewModel;
    private int plantId;
    private final ImageManager imageManager = new ImageManager();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plantId = requireArguments().getInt("PLANT_ID");

        getChildFragmentManager().setFragmentResultListener(ConfirmDialog.REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.getBoolean(ConfirmDialog.RESULT_KEY)) {
                    viewModel.deletePlant();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlantBinding.inflate(inflater, container, false);
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
                return (T) new PlantViewModel(
                        plantId,
                        new PlantManager(
                                AppDatabase.getAppDatabase(requireActivity().getApplicationContext()).plantDao(),
                                ((MyGardenApp) requireActivity().getApplication()).getExecutor(),
                                requireActivity().getApplicationContext()
                        )
                );
            }
        }).get(PlantViewModel.class);

        viewModel.getPlant().observe(getViewLifecycleOwner(), new Observer<Plant>() {
            @Override
            public void onChanged(Plant plant) {
                if (plant != null) {
                    setPlantInfo(plant);
                }
            }
        });

        viewModel.getFragmentAction().observe(getViewLifecycleOwner(), new Observer<FragmentAction>() {
            @Override
            public void onChanged(FragmentAction fragmentAction) {
                if (fragmentAction != null) {
                    if (fragmentAction.canClose()) {
                        ((MainActivity) requireActivity()).launchPlantsFragment();
                    } else {
                        Toast.makeText(requireActivity(), fragmentAction.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        setListeners();
    }

    private void setListeners() {
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).launchPlantsFragment();
            }
        });

        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_edit) {
                    ((MainActivity) requireActivity()).launchAddEditPlantFragment(plantId);
                    return true;
                } else if (id == R.id.menu_delete) {
                    showConfirmDialogFragment();
                    return true;
                }
                return false;
            }
        });
    }

    private void setPlantInfo(Plant plant) {
        if (plant.getImagePath() != null) {
            File dir;
            try {
                dir = imageManager.getPlantImagesDir(requireActivity().getApplicationContext());
                File plantImageFile = new File(dir, plant.getImagePath());

                Glide.with(PlantFragment.this)
                        .load(plantImageFile)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.plantImage);
            } catch (IOException e) {
                Toast.makeText(
                        requireActivity(),
                        "Не удалось загрузить фото растения",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            binding.plantImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            binding.plantImage.setImageDrawable(ResourcesCompat.getDrawable(
                    requireActivity().getResources(),
                    R.drawable.plant_placeholder,
                    null)
            );
        }
        binding.plantName.setText(plant.getName());
        binding.plantVariety.setText(plant.getVariety());
        binding.sowingDate.setText(DateConverter.toString(plant.getSowingDate()));
        binding.plantingDate.setText(DateConverter.toString(plant.getPlantingDate()));
        binding.harvestingDate.setText(DateConverter.toString(plant.getHarvestingDate()));
        binding.plantDescription.setText(plant.getDescription());
        binding.growingConditions.setText(plant.getGrowingConditions());
    }

    private void showConfirmDialogFragment() {
        DialogFragment confirmDialog = new ConfirmDialog();
        confirmDialog.show(getChildFragmentManager(), "ConfirmDialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
