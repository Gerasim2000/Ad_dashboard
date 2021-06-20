package org.group5;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    @Test
    void loadFilesLargeTest() throws SQLException, IOException {

        Controller controller = new Controller(new Model());
        controller.loadFiles(
                "samples/12_month_campaign/impression_log_12month.csv",
                "samples/2_week_campaign/click_log.csv",
                "samples/2_week_campaign/server_log.csv"
        );

        // Check first impression loaded
        int rows = controller.countImpressions();

        assertEquals(11666496, rows);

    }

    @Test
    void loadFilesTwiceTest() throws SQLException, IOException {
        Controller controller = new Controller(new Model());
        controller.loadFiles(
                "samples/2_week_campaign/impression_log.csv",
                "samples/2_week_campaign/click_log.csv",
                "samples/2_week_campaign/server_log.csv"
        );
        controller.loadFiles(
                "samples/2_week_campaign/impression_log.csv",
                "samples/2_week_campaign/click_log.csv",
                "samples/2_week_campaign/server_log.csv"
        );
        int rows = controller.countImpressions();
        assertEquals(486104, rows);
    }

    @Test
    void invalidFilterTest() throws SQLException, IOException {

        Controller controller = new Controller(new Model());
        controller.loadFiles(
                "samples/2_week_campaign/impression_log.csv",
                "samples/2_week_campaign/click_log.csv",
                "samples/2_week_campaign/server_log.csv"
        );

        assertThrows(SQLException.class,
                () -> controller.countImpressions(new Filter(new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() - 10000)), Controller.GRAN_DAY)
                );

    }

    @Test
    void loadFileTiming() throws SQLException, IOException {
        Controller controller = new Controller(new Model());
        controller.loadFiles(
                "samples/2_week_campaign/impression_log.csv",
                "samples/2_week_campaign/click_log.csv",
                "samples/2_week_campaign/server_log.csv"
        );
    }

}
