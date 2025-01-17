/*
 ******************************************************************************************
 *
 *    Part of the master's thesis
 *    Topic: "Supporting the development of fine motor skills in children using IT tools"
 *
 *    FRIENDLY LETTERS created by Mikolaj Szotowicz : https://github.com/szotowicz
 *
 ****************************************************************************************
 */
package com.pg.mikszo.friendlyletters;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.pg.mikszo.friendlyletters.settings.Configuration;
import com.pg.mikszo.friendlyletters.settings.SettingsManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FileHelper {

    public static File getAppFolderPath(Context context) {
        return new File(context.getFilesDir(), context.getString(R.string.resources_dir_name));
    }

    public static boolean isAppFolderExists(Context context) {
        File appFolder = getAppFolderPath(context);
        return appFolder.exists() && appFolder.isDirectory();
    }

    public static boolean isFileWithConfigurationsExists(Context context) {
        File fileWithConfigurations = getFileWithConfigurations(context);
        return fileWithConfigurations.exists() && fileWithConfigurations.isFile();
    }

    public static String getFileWithConfigurationsPath(Context context) {
        return getAppFolderPath(context) + File.separator +
                context.getString(R.string.file_with_configurations_file_name);
    }

    public static File getFileWithConfigurations(Context context) {
        return new File(getAppFolderPath(context) + File.separator +
                context.getString(R.string.file_with_configurations_file_name));
    }

    public static int getNumberOfAllFilesInAppFolder(Context context) {
        File appFolder = getAppFolderPath(context);
        List<File> files = FileHelper.getMatchesFiles(appFolder.listFiles(), context);
        return files.size();
    }

    public static File[] getAllFilesFromAppFolder(Context context) {
        addNoMediaFile(context);
        File appFolder = getAppFolderPath(context);
        List<File> files = FileHelper.getMatchesFiles(appFolder.listFiles(), context);
        return files.toArray(new File[0]);
    }

    public static File[] getAllFilesFromAppFolderOldestFirst(Context context) {
        addNoMediaFile(context);
        final List<File> files = Arrays.asList(getAllFilesFromAppFolder(context));
        final Map<File, Long> constantLastModifiedTimes = new HashMap<>();
        for (final File f : files) {
            constantLastModifiedTimes.put(f, f.lastModified());
        }
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(final File f1, final File f2) {
                return constantLastModifiedTimes.get(f1).compareTo(constantLastModifiedTimes.get(f2));
            }
        });
        return files.toArray(new File[0]);
    }

    public static File getAbsolutePathOfFile(String filename, Context context) {
        File appFolder = getAppFolderPath(context);
        File file = new File(appFolder + File.separator + filename);
        if (!file.exists()) {
            return null;
        }
        return file;
    }

    public static void copyDefaultImages(Context context) {
        File appFolder = getAppFolderPath(context);
        if (!appFolder.exists()) {
            if (!appFolder.mkdirs()) {
                Log.e("[ERROR]", "Copying default images failed");
            }
        }

        try {
            AssetManager assetManager = context.getAssets();
            List<String> images = getMatchesFiles(
                    assetManager.list(context.getString(R.string.materials_dir_name)),
                    context);
            for (String img : images) {
                copyFileAssets(context.getString(R.string.materials_dir_name)
                        + File.separator + img, img, context);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        addNoMediaFile(context);
    }

    public static void copyDefaultFileWithConfigurations(Context context) {
        File appFolder = getAppFolderPath(context);
        if (!appFolder.exists()) {
            if (!appFolder.mkdirs()) {
                Log.e("[ERROR]", "Copying default file with configurations failed");
            }
        }

        copyFileAssets(context.getString(R.string.file_with_configurations_dir_name)
                        + File.separator + context.getString(R.string.file_with_configurations_file_name),
                context.getString(R.string.file_with_configurations_file_name), context);

        SettingsManager settingsManager = new SettingsManager(context);
        Configuration activeConfiguration = settingsManager.getActiveConfiguration();
        activeConfiguration.configurationName = context.getString(R.string.settings_configurations_default_name);
        settingsManager.updateFileWithConfigurations(new Configuration[] { activeConfiguration });
    }

    public static List<String> getMatchesFiles(String[] files, Context context) {
        Pattern pattern = Pattern.compile(context.getString(R.string.prefix_shape_file_name) + "(.*?)png");
        List<String> matches = new ArrayList<>();
        for (String file : files) {
            if (pattern.matcher(file).matches()) {
                matches.add(file);
            }
        }

        return matches;
    }

    public static List<File> getMatchesFiles(File[] files, Context context) {
        Pattern pattern = Pattern.compile(context.getString(R.string.prefix_shape_file_name) + "(.*?)png");
        List<File> matches = new ArrayList<>();

        for (File file : files) {
            if (pattern.matcher(file.getName()).matches()) {
                matches.add(file);
            }
        }

        return matches;
    }

    public static void copyFileAssets(String fileSource, String fileDestination, Context context) {
        File appFolder = getAppFolderPath(context);
        AssetManager assetManager = context.getAssets();
        InputStream in;
        OutputStream out;
        try {
            in = assetManager.open(fileSource);
            out = new FileOutputStream(appFolder + File.separator + fileDestination);
            copyFile(in, out);
            in.close();
            out.flush();
            out.close();
        } catch(IOException e) {
            Log.e("[ERROR]", "Failed to copy asset file: " + fileSource);
        }
    }

    public static void copyFileAssets(String filename, Context context) {
        copyFileAssets(filename, filename, context);
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private static void addNoMediaFile(Context context) {
        File noMediaFile = new File(getAppFolderPath(context)  + File.separator + context.getString(R.string.no_media_file_name));
        try {
            if (!noMediaFile.exists()) {
                noMediaFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}