package it.cosenonjaviste.testableandroidapps.model;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by fabiocollini on 14/09/14.
 */
public class TestUtils {
    public static String readFile(String fileName) throws IOException {
        StringBuilder b = new StringBuilder();
        BufferedReader reader = null;
        try {
            File file = new File("model/src/test/resources" + fileName);
            if (!file.exists()) {
                file = new File("src/test/resources" + fileName);
            }
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = reader.readLine()) != null) {
                b.append(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return b.toString();
    }

    public static <T> T fromJson(String fileName, Class<T> c) {
        try {
            String jsonString = readFile(fileName);
            return new Gson().fromJson(jsonString, c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
