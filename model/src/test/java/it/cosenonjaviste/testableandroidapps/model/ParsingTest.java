package it.cosenonjaviste.testableandroidapps.model;

import com.google.gson.Gson;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParsingTest {
    @Test
    public void testLoad() throws IOException {
        String jsonString = readFile("/response.json");
        RepoResponse response = new Gson().fromJson(jsonString, RepoResponse.class);
        assertNotNull(response);
        ArrayList<Repo> items = response.getItems();
        assertNotNull(items);
        assertEquals(3, items.size());
        for (Repo item : items) {
            assertNotNull(item.getOwner());
        }
    }

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
}
