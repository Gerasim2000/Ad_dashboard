package org.group5;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    // Boundary test ensuring all impressions are loaded correctly.
    @Test
    void loadImpressionsTest() throws SQLException, IOException {

        Model model = new Model();
        model.loadImpressions("samples/2_week_campaign/impression_log.csv");

        // Check first impression loaded

        ResultSet results = model.executeQuery("SELECT * FROM impressions ORDER BY ROWID ASC LIMIT 1;");

        long entryDate = results.getLong(2);
        String id = results.getString(3);
        String context = results.getString(4);
        float cost = results.getFloat(5);

        assertEquals(1420113602, entryDate);
        assertEquals("4620864431353617408", id);
        assertEquals("Blog", context);
        assertEquals(0.001713f, cost);

        // Check last impression loaded

        results = model.executeQuery("SELECT * FROM impressions ORDER BY ROWID DESC LIMIT 1;");

        entryDate = results.getLong(2);
        id = results.getString(3);
        context = results.getString(4);
        cost = results.getFloat(5);

        assertEquals(1421236800, entryDate);
        assertEquals("923607590768080896", id);
        assertEquals("Blog", context);
        assertEquals(0.002907f, cost);

        // Check number of rows correct

        results = model.executeQuery("SELECT COUNT(*) from impressions");
        int rows = results.getInt(1);

        assertEquals(486104, rows);

    }

    // Boundary test ensuring all click records are loaded correctly.
    @Test
    void loadClicksTest() throws SQLException, IOException {

        Model model = new Model();
        model.loadClicks("samples/2_week_campaign/click_log.csv");

        // Check first click loaded

        ResultSet results = model.executeQuery("SELECT * FROM clicks ORDER BY ROWID ASC LIMIT 1;");

        long entryDate = results.getLong(1);
        String id = results.getString(2);
        float cost = results.getFloat(3);

        assertEquals(1420113681, entryDate);
        assertEquals("8895519749317550080", id);
        assertEquals(11.794442f, cost);

        // Check last impression loaded

        results = model.executeQuery("SELECT * FROM clicks ORDER BY ROWID DESC LIMIT 1;");

        entryDate = results.getLong(1);
        id = results.getString(2);
        cost = results.getFloat(3);

        assertEquals(1421236795, entryDate);
        assertEquals("6752478673601913856", id);
        assertEquals(0.000000f, cost);

        // Check number of rows correct

        results = model.executeQuery("SELECT COUNT(*) from clicks");
        int rows = results.getInt(1);

        assertEquals(23923, rows);

    }

    // Boundary test ensuring all server records are loaded correctly.
    @Test
    void loadServersTest() throws SQLException, IOException {

        Model model = new Model();
        model.loadServers("samples/2_week_campaign/server_log.csv");

        // Check first impression loaded

        ResultSet results = model.executeQuery("SELECT * FROM servers ORDER BY ROWID ASC LIMIT 1;");

        long entryDate = results.getLong(1);
        String id = results.getString(2);
        long exitDate = results.getLong(3);
        int pagesViewed = results.getInt(4);
        String conversion = results.getString(5);

        assertEquals(1420113681, entryDate);
        assertEquals("8895519749317550080", id);
        assertEquals(1420113913, exitDate);
        assertEquals(7, pagesViewed);
        assertEquals("No", conversion);

        // Check last impression loaded

        results = model.executeQuery("SELECT * FROM servers ORDER BY ROWID DESC LIMIT 1;");

        entryDate = results.getLong(1);
        id = results.getString(2);
        exitDate = results.getLong(3);
        pagesViewed = results.getInt(4);
        conversion = results.getString(5);

        assertEquals(1421236796, entryDate);
        assertEquals("6752478673601913856", id);
        assertEquals(1421237273, exitDate);
        assertEquals(6, pagesViewed);
        assertEquals("No", conversion);

        // Check number of rows correct

        results = model.executeQuery("SELECT COUNT(*) from servers");
        int rows = results.getInt(1);

        assertEquals(23923, rows);

    }

    @Test
    void loadMissingCSVTest() throws SQLException {

        Model model = new Model();
        assertThrows(
                FileNotFoundException.class,
                () -> model.loadServers("samples/2_week_campaign/missing.csv")
        );

    }

    @Test
    void loadEmptyCSVTest() throws SQLException, IOException {

        Model model = new Model();
        model.loadServers("samples/2_week_campaign/empty.csv");

    }


}
