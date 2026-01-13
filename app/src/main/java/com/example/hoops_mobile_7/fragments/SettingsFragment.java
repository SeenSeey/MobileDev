package com.example.hoops_mobile_7.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.hoops_mobile_7.R;
import com.example.hoops_mobile_7.databinding.FragmentSettingsBinding;
import com.example.hoops_mobile_7.repository.files.FileRepository;
import com.example.hoops_mobile_7.repository.sharedPrefs.SettingsPreferences;

import java.io.File;
import java.util.Date;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private SettingsPreferences prefs;
    private FileRepository fileRepo;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private boolean isInitializing = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = new SettingsPreferences(requireContext());
        fileRepo = new FileRepository(requireContext());

        checkPermissions();

        updateViews();
        setupListeners();
        isInitializing = false;
    }

    private void updateViews() {
        binding.switchDarkTheme.setChecked(prefs.isDarkTheme());

        int textSize = prefs.getTextSize();
        int checkedTextBtnId;
        if (textSize == 0) checkedTextBtnId = R.id.btn_text_small;
        else if (textSize == 2) checkedTextBtnId = R.id.btn_text_large;
        else checkedTextBtnId = R.id.btn_text_medium;
        binding.toggleTextSize.check(checkedTextBtnId);

        String lang = prefs.getLanguage();
        binding.toggleLanguage.check(lang.equals("ru") ? R.id.btn_lang_ru : R.id.btn_lang_en);

        updateFileSectionUI();
    }

    private void updateFileSectionUI() {
        if (fileRepo.isPublicFileExists()) {
            File file = fileRepo.getPublicFileDetails();
            String date = DateFormat.format("dd.MM.yyyy HH:mm", new Date(file.lastModified())).toString();
            long sizeBytes = file.length();

            String info = "Файл: " + file.getName() + "\n" +
                    "Путь: .../Documents/\n" +
                    "Размер: " + sizeBytes + " байт\n" +
                    "Создан/Изменен: " + date;

            binding.tvFileInfo.setText(info);
            binding.btnDeleteFile.setEnabled(true);
        } else {
            binding.tvFileInfo.setText("Файл отсутствует в External Storage");
            binding.btnDeleteFile.setEnabled(false);
        }

        boolean backupExists = fileRepo.isBackupExists();
        if (backupExists) {
            binding.tvBackupInfo.setText("Резервная копия: ЕСТЬ (Internal Storage)");
            binding.btnRestoreFile.setEnabled(true);
        } else {
            binding.tvBackupInfo.setText("Резервная копия: ОТСУТСТВУЕТ");
            binding.btnRestoreFile.setEnabled(false);
        }
    }
    private void setupListeners() {
        binding.switchDarkTheme.setOnCheckedChangeListener((v, isChecked) -> {
            if (isInitializing) return;
            prefs.setDarkTheme(isChecked);
            restartApp();
        });

        binding.toggleTextSize.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isInitializing || !isChecked) return;

            int newIndex = 1;
            if (checkedId == R.id.btn_text_small) newIndex = 0;
            else if (checkedId == R.id.btn_text_large) newIndex = 2;

            if (newIndex != prefs.getTextSize()) {
                prefs.setTextSize(newIndex);
                restartApp();
            }
        });

        binding.toggleLanguage.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            String currentLang = prefs.getLanguage();
            String newLang = (checkedId == R.id.btn_lang_ru) ? "ru" : "en";

            if (!currentLang.equals(newLang)) {
                prefs.setLanguage(newLang);

                requireActivity().recreate();
            }
        });

        binding.btnResetSettings.setOnClickListener(v -> {
            prefs.resetDefaults();
            updateViews();
            restartApp();
        });

        binding.btnDeleteFile.setOnClickListener(v -> {
            if (fileRepo.isPublicFileExists()) {
                String data = fileRepo.readPublicFile();

                if (data != null) {
                    fileRepo.saveToInternalBackup(data);

                    boolean deleted = fileRepo.deletePublicFile();

                    if (deleted) {
                        Toast.makeText(requireContext(), "Файл удален (Бэкап создан)", Toast.LENGTH_SHORT).show();
                        updateFileSectionUI();
                    } else {
                        Toast.makeText(requireContext(), "Ошибка удаления", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.btnRestoreFile.setOnClickListener(v -> {
            if (fileRepo.isBackupExists()) {
                boolean restored = fileRepo.restoreFromBackup();

                if (restored) {
                    Toast.makeText(requireContext(), "Данные восстановлены и бэкап удален!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Ошибка восстановления данных.", Toast.LENGTH_SHORT).show();
                }
                updateFileSectionUI();
            }
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void restartApp() {
        requireActivity().recreate();
    }
}
