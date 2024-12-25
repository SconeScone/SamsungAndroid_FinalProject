package com.example.samsungandroid_finalproject.ui;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.samsungandroid_finalproject.R;
import com.example.samsungandroid_finalproject.databinding.ActivityMainBinding;
import com.example.samsungandroid_finalproject.ui.add_edit_plant.AddEditPlantFragment;
import com.example.samsungandroid_finalproject.ui.plant.PlantFragment;
import com.example.samsungandroid_finalproject.ui.plants.PlantsFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction
                    .setReorderingAllowed(true)
                    .add(binding.fragmentContainer.getId(), PlantsFragment.class, null)
                    .commit();
        }
    }

    // Запуск фрагмента, отображающего растения пользователя
    public void launchPlantsFragment() {
        launchFragment(PlantsFragment.class, null);
    }

    // Запуск фрагмента, отображающего подробную информацию о растении
    public void launchPlantFragment(int id) {
        Bundle args = new Bundle();
        args.putInt("PLANT_ID", id);
        launchFragment(PlantFragment.class, args);
    }

    // Запуск фрагмента для добавления/редактирования растения (id = 0, если растение добавляется)
    public void launchAddEditPlantFragment(int id) {
        Bundle args = new Bundle();
        args.putInt("PLANT_ID", id);
        launchFragment(AddEditPlantFragment.class, args);
    }

    private <T extends Fragment> void launchFragment(Class<T> fragmentClass, Bundle args) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .setReorderingAllowed(true)
                .setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out
                )
                .replace(binding.fragmentContainer.getId(), fragmentClass, args)
                .commit();
    }
}