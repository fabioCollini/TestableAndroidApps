package it.cosenonjaviste.testableandroidapps.model;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParsingTest {
    @Test
    public void testLoad() throws IOException {
        RepoResponse response = TestUtils.fromJson("/response.json", RepoResponse.class);
        assertNotNull(response);
        ArrayList<Repo> items = response.getItems();
        assertNotNull(items);
        assertEquals(3, items.size());
        for (Repo item : items) {
            assertNotNull(item.getOwner());
        }
    }
}
