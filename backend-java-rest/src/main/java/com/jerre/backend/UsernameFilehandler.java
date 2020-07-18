package com.jerre.backend;

import java.io.*;
import java.util.Optional;

public class UsernameFilehandler {

    /**
     *
     * @param filename - not prefixed with slash
     * @param path - postfixed with slash
     * @return
     * @throws FileNotFoundException
     */
    public static Optional<String[]> readUserNames(String filename, String path) {
        initializeFileIfNecessary(filename, path);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path + "/" + filename))) {
            String csvFirstLine = bufferedReader.readLine();
            if (csvFirstLine == null)
            {
                return Optional.of(new String[]{});
            }
            else {
                return Optional.of(csvFirstLine.split(","));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Optional.empty();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static boolean writeUserNames(String[] usernames, String filename, String path)
    {
        initializeFileIfNecessary(filename, path);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path + "/" + filename))) {
            bufferedWriter.write(String.join(",", usernames));
            bufferedWriter.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void initializeFileIfNecessary(String filename, String path) {
        File folderPath = new File(path);
        if (!folderPath.exists()) {
            folderPath.mkdirs();
        }

        File file = new File(path + "/" + filename);
        System.out.println(file.getAbsolutePath());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
