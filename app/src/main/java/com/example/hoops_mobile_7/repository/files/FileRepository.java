package com.example.hoops_mobile_7.repository.files;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.hoops_mobile_7.network.model.character.Character;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

public class FileRepository {
    private static final String FILE_NAME = "rick_morty_data_17.txt";
    private static final String BACKUP_FILE_NAME = "backup_" + FILE_NAME;
    private final Context context;

    public FileRepository(Context context) {
        this.context = context;
    }

    private File getPublicFile() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        if (!path.exists()) {
            path.mkdirs();
        }
        return new File(path, FILE_NAME);
    }

    public boolean isPublicFileExists() {
        return getPublicFile().exists();
    }

    public File getPublicFileDetails() {
        return getPublicFile();
    }

    public void saveToPublicStorage(String data) {
        File file = getPublicFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data.getBytes());
        } catch (IOException e) {
            Log.e("FileRepo", "Error saving public: " + e.getMessage());
        }
    }

    public String readPublicFile() {
        return readFile(getPublicFile());
    }

    public boolean deletePublicFile() {
        File file = getPublicFile();
        return file.exists() && file.delete();
    }


    private File getInternalFile() {
        return new File(context.getFilesDir(), BACKUP_FILE_NAME);
    }

    public boolean isBackupExists() {
        return getInternalFile().exists();
    }

    public void saveToInternalBackup(String data) {
        try (FileOutputStream fos = context.openFileOutput(BACKUP_FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(data.getBytes());
        } catch (IOException e) {
            Log.e("FileRepo", "Error backing up: " + e.getMessage());
        }
    }

    public String readBackupFile() {
        return readFile(getInternalFile());
    }

    public boolean deleteBackupFile() {
        File backupFile = getInternalFile();
        return backupFile.exists() && backupFile.delete();
    }

    public boolean restoreFromBackup() {
        String backupData = readBackupFile();
        if (backupData == null) {
            Log.e("FileRepo", "Backup data is null, cannot restore.");
            return false;
        }

        // 1. Изменяем заголовок в данных бэкапа
        String restoredData = modifyRestoredData(backupData);

        // 2. Записываем данные в основной публичный файл
        saveToPublicStorage(restoredData);

        // 3. Удаляем бэкап из внутреннего хранилища (Context.deleteFile используется для файлов, открытых через openFileOutput)
        boolean backupDeleted = context.deleteFile(BACKUP_FILE_NAME);

        if (backupDeleted) {
            Log.i("FileRepo", "Backup successfully restored and deleted.");
            return true;
        } else {
            Log.e("FileRepo", "Restored public file, but failed to delete backup file.");
            // Возвращаем true, т.к. восстановление данных прошло успешно, удаление бэкапа - второстепенный шаг.
            return true;
        }
    }

    private String modifyRestoredData(String data) {
        // Добавляем отметку "Restored" и обновляем дату.
        String header = "--- Restored Rick and Morty Character Data (ID: 17) ---\n";
        header += "--- Restored: " + new Date().toString() + " ---\n\n";

        // Пропускаем первые две строки (старый заголовок и старая дата)
        String[] lines = data.split("\n", 3);
        if (lines.length >= 3) {
            return header + lines[2]; // Новый заголовок + остальная часть файла
        } else {
            return header + data; // Если файл слишком короткий, просто добавляем новый заголовок
        }
    }

    private String readFile(File file) {
        StringBuilder text = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e("FileRepo", "Error reading: " + e.getMessage());
            return null;
        }
        return text.toString();
    }

    public String serializeCharactersToTxt(List<Character> characters) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Rick and Morty Character Data (ID: 17) ---\n");
        sb.append("--- Generated: ").append(new Date().toString()).append(" ---\n\n");

        for (com.example.hoops_mobile_7.network.model.character.Character c : characters) {
            String originName = c.getOrigin() != null ? c.getOrigin().getName() : "Unknown";
            String locationName = c.getLocation() != null ? c.getLocation().getName() : "Unknown";

            sb.append("ID: ").append(c.getId()).append("\n")
                    .append("Name: ").append(c.getName()).append("\n")
                    .append("Status: ").append(c.getStatus()).append("\n")
                    .append("Species: ").append(c.getSpecies()).append("\n")
                    .append("Gender: ").append(c.getGender()).append("\n")
                    .append("Origin: ").append(originName).append("\n")
                    .append("Location: ").append(locationName).append("\n")
                    .append("Created: ").append(c.getCreated()).append("\n")
                    .append("------------------------------------------\n");
        }
        return sb.toString();
    }
}