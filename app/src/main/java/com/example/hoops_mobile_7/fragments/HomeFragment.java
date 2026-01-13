package com.example.hoops_mobile_7.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoops_mobile_7.adapter.CharacterAdapter;
import com.example.hoops_mobile_7.databinding.FragmentHomeBinding;
import com.example.hoops_mobile_7.repository.files.FileRepository;
import com.example.hoops_mobile_7.repository.network.CharacterRepository;
import com.example.hoops_mobile_7.repository.sharedPrefs.UserPrefs;
import com.example.hoops_mobile_7.network.model.character.Character;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private CharacterAdapter adapter;
    private CharacterRepository repository;
    private FileRepository fileRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    saveCharacterData(adapter.getAllCharacters());
                } else {
                    Toast.makeText(requireContext(),
                            "Для сохранения данных требуется разрешение на запись.",
                            Toast.LENGTH_LONG).show();
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        repository = new CharacterRepository(requireContext());
        fileRepository = new FileRepository(requireContext());

        adapter = new CharacterAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.listView.setLayoutManager(layoutManager);
        binding.listView.setAdapter(adapter);

        binding.btnLogout.setOnClickListener(v -> {
            UserPrefs.clearUser(requireContext());
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToSignInFragment());
        });

        binding.btnSettings.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment());
        });

        binding.btnClearDb.setOnClickListener(v -> {
            clearDatabase();
        });

        subscribeToDatabase();

        binding.listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy <= 0) return;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage && isNetworkAvailable()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadApiData(currentPage, false);
                    }
                }
            }
        });
    }

    private void subscribeToDatabase() {
        disposables.add(repository.getCharactersFromDb()
                .subscribe(characters -> {
                    adapter.setItems(characters);
                    saveCharacterData(characters);

                    if (characters.isEmpty() && !isLoading) {
                        loadApiData(1, false);
                    }
                }, throwable -> {
                    Toast.makeText(getContext(), "Ошибка БД: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    private void loadApiData(int page, boolean isRefresh) {

        if (!isNetworkAvailable()) {
            isLoading = false;
            binding.progressBar.setVisibility(View.GONE);
            binding.noNetworkContainer.setVisibility(View.VISIBLE);
            return;
        }

        binding.noNetworkContainer.setVisibility(View.GONE);
        isLoading = true;
        binding.progressBar.setVisibility(View.VISIBLE);

        repository.loadApiAndSaveToDb(page, isRefresh, () -> {
            requireActivity().runOnUiThread(() -> {
                isLoading = false;
                binding.progressBar.setVisibility(View.GONE);
                if (isRefresh) currentPage = 2;
                else currentPage++;
            });
        }, () -> {
            requireActivity().runOnUiThread(() -> {
                isLoading = false;
                binding.progressBar.setVisibility(View.GONE);
                binding.noNetworkContainer.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Ошибка загрузки", Toast.LENGTH_SHORT).show();
            });
        });
    }


    private void clearDatabase() {
        disposables.add(repository.clearAllCharacters()
                .subscribe(() -> {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "База данных очищена", Toast.LENGTH_SHORT).show();
                    });
                }, throwable -> {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Ошибка при очистке БД: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                })
        );
    }

    private void saveCharacterData(List<Character> characters) {
        if (characters == null || characters.isEmpty()) return;

        if (!checkWritePermissions()) return;

        String data = fileRepository.serializeCharactersToTxt(characters);
        fileRepository.saveToPublicStorage(data);
    }

    private boolean checkWritePermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                showSettingsDialog();
            }
            return false;
        }
        return true;
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Требуется разрешение на хранение")
                .setMessage("Для сохранения данных в общую папку, пожалуйста, предоставьте разрешение вручную.")
                .setPositiveButton("Перейти в Настройки", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) return false;

        Network network = cm.getActiveNetwork();
        if (network == null) return false;

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        return capabilities != null &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
        binding = null;
    }
}