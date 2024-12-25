package com.example.samsungandroid_finalproject.ui.add_edit_plant;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.samsungandroid_finalproject.MyGardenApp;
import com.example.samsungandroid_finalproject.R;
import com.example.samsungandroid_finalproject.data.ImageManager;
import com.example.samsungandroid_finalproject.data.PlantManager;
import com.example.samsungandroid_finalproject.data.AppDatabase;
import com.example.samsungandroid_finalproject.databinding.FragmentAddEditPlantBinding;
import com.example.samsungandroid_finalproject.domain.DateConverter;
import com.example.samsungandroid_finalproject.domain.Plant;
import com.example.samsungandroid_finalproject.domain.PlantDataValidator;
import com.example.samsungandroid_finalproject.domain.ValidationResult;
import com.example.samsungandroid_finalproject.ui.FragmentAction;
import com.example.samsungandroid_finalproject.ui.MainActivity;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class AddEditPlantFragment extends Fragment {
    private FragmentAddEditPlantBinding binding;
    private AddEditPlantViewModel viewModel;
    private Uri plantImageUri;
    private int plantId;
    private final ImageManager imageManager = new ImageManager();
    private final ActivityResultLauncher<String> getImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            if (uri != null) {
                setImage(uri);
            }
        }
    });

    private final ActivityResultLauncher<Uri> takePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean isImageSaved) {
            if (isImageSaved) {
                setImage(plantImageUri);
            }
        }
    });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plantId = requireArguments().getInt("PLANT_ID");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddEditPlantBinding.inflate(inflater, container, false);
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
                return (T) new AddEditPlantViewModel(
                        plantId,
                        new PlantManager(
                                AppDatabase.getAppDatabase(requireActivity().getApplicationContext()).plantDao(),
                                ((MyGardenApp) requireActivity().getApplication()).getExecutor(),
                                requireActivity().getApplicationContext()
                        ));
            }
        }).get(AddEditPlantViewModel.class);

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
                        if (plantId == 0) {
                            ((MainActivity) requireActivity()).launchPlantsFragment();
                        } else {
                            ((MainActivity) requireActivity()).launchPlantFragment(plantId);
                        }
                    } else {
                        Toast.makeText(requireActivity(), fragmentAction.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                    binding.saveProgressBarLayout.setVisibility(View.GONE);
                    setStateOnLoading(true);
                }
            }
        });

        setListeners();
    }

    private void setListeners() {
        // Загрузить изображение растения из галереи
        binding.loadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage.launch("image/*");
            }
        });

        // Сделать фото растения
        binding.takePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    plantImageUri = imageManager.getPlantImageUri(requireActivity().getApplicationContext());
                    takePicture.launch(plantImageUri);
                } catch (IOException e) {
                    Toast.makeText(requireActivity(), "Что-то пошло не так при попытке сделать фото растения", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.sowingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar(binding.sowingDate);
            }
        });

        binding.plantingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar(binding.plantingDate);
            }
        });

        binding.harvestingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar(binding.harvestingDate);
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plantName = binding.plantName.getText().toString();
                String plantVariety = binding.plantVariety.getText().toString();
                String sowingDate = binding.sowingDate.getText().toString();
                String plantingDate = binding.plantingDate.getText().toString();
                String harvestingDate = binding.harvestingDate.getText().toString();
                String plantDescription = binding.plantDescription.getText().toString();
                String growingConditions = binding.growingConditions.getText().toString();

                if (isPlantInfoCorrect(plantName, plantVariety, sowingDate, plantingDate)) {
                    viewModel.savePlant(new Plant(
                            plantName,
                            plantVariety,
                            DateConverter.toDate(sowingDate),
                            DateConverter.toDate(plantingDate),
                            DateConverter.toDate(harvestingDate),
                            plantDescription,
                            growingConditions));
                    binding.saveProgressBarLayout.setVisibility(View.VISIBLE);
                    setStateOnLoading(false);
                }
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plantId == 0) { // Если отменили добавление растения, то запускаем фрагмент со всеми растениями
                    ((MainActivity) requireActivity()).launchPlantsFragment();
                } else { // Если отменили редактирование растения, то запускаем фрагмент с информацией о растении
                    ((MainActivity) requireActivity()).launchPlantFragment(plantId);
                }
            }
        });
    }

    private boolean isPlantInfoCorrect(String plantName, String plantVariety, String sowingDate, String plantingDate) {
        ValidationResult plantNameRes = PlantDataValidator.validatePlantName(plantName);
        ValidationResult plantVarietyRes = PlantDataValidator.validatePlantVariety(plantVariety);

        binding.plantNameLayout.setError(plantNameRes.getErrorMessage());
        binding.plantVarietyLayout.setError(plantVarietyRes.getErrorMessage());

        return plantNameRes.isValid() && plantVarietyRes.isValid();
    }

    private void setPlantInfo(Plant plant) {
        if (plant.getImagePath() != null) {
            File dir;
            try {
                dir = imageManager.getPlantImagesDir(requireActivity().getApplicationContext());
                File plantImageFile = new File(dir, plant.getImagePath());

                Glide.with(AddEditPlantFragment.this)
                        .load(plantImageFile)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.plantImage);
            } catch (IOException e) {
                Toast.makeText(requireActivity(), "Не удалось загрузить изображение растения", Toast.LENGTH_SHORT).show();
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

    private void openCalendar(EditText dateField) {
        LocalDate curDate = LocalDate.now();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = DateConverter.toString(dayOfMonth, month + 1, year);
                dateField.setText(date);
            }
        }, curDate.getYear(), curDate.getMonthValue() - 1, curDate.getDayOfMonth());
        datePickerDialog.show();
    }

    private void setImage(Uri uri) {
        binding.progressBar.setVisibility(View.VISIBLE);
        setStateOnLoading(false);
        Glide.with(AddEditPlantFragment.this)
                .asBitmap()
                .load(uri)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Bitmap> target, boolean isFirstResource) {
                        binding.progressBar.setVisibility(View.GONE);
                        setStateOnLoading(true);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Bitmap bitmap, @NonNull Object model, Target<Bitmap> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        viewModel.setPlantImage(bitmap);
                        binding.progressBar.setVisibility(View.GONE);
                        setStateOnLoading(true);
                        return false;
                    }
                })
                .centerCrop()
                .into(binding.plantImage);
    }

    private void setStateOnLoading(boolean isLoaded) {
        binding.saveBtn.setEnabled(isLoaded);
        binding.loadImageBtn.setEnabled(isLoaded);
        binding.takePictureBtn.setEnabled(isLoaded);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
