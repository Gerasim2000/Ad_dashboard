package org.group5;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    static Controller controller;

    @BeforeAll
    static void setup() throws SQLException, IOException {
        controller = new Controller(new Model());
        controller.loadFiles(
                "samples/2_week_campaign/impression_log.csv",
                "samples/2_week_campaign/click_log.csv",
                "samples/2_week_campaign/server_log.csv"
        );
    }

    // --- countImpressions Partition Tests ---

    @Test
    void countImpressionsWithDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countImpressions(filter, Controller.GRAN_DAY);
        assertArrayEquals(
                new int[] {33320, 33978, 33752, 34406, 36630, 36719, 38402, 38013, 37837, 38680, 42472, 41979, 39916},
                result
        );
    }

    @Test
    void countImpressionsWithWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countImpressions(filter, Controller.GRAN_WEEK);
        assertArrayEquals(
                new int[] {247207, 238897},
                result
        );
    }

    @Test
    void countImpressionsWithMonthTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countImpressions(filter, Controller.GRAN_MONTH);
        assertArrayEquals(
                new int[] {486104},
                result
        );
    }

    @Test
    void countImpressionsPMWithTimeOfDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countImpressionsPM(filter, Controller.PERFORMANCE_DAY);
        assertArrayEquals(
                new int[] {4244, 1371, 1498, 1487, 4284, 9799, 15591, 21319, 26443, 29330, 29658, 29642, 29772, 29569, 29503, 29773, 29638, 29595, 29557, 29944, 27129, 21260, 15758, 9940},
                result
        );
    }

    @Test
    void countImpressionsPMWithDayOfWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countImpressionsPM(filter, Controller.PERFORMANCE_WEEK);
        assertArrayEquals(
                new int[] {75125, 76703, 79538, 52093, 59360, 71804, 71481},
                result
        );
    }


    // Clicks Tests
    @Test
    void countClicksWithDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countClicks(filter, Controller.GRAN_DAY);
        assertArrayEquals(
                new int[] {1607, 1744, 1670, 1683, 1819, 1805, 1817, 1894, 1913, 1863, 2024, 2109, 1975},
                result
        );
    }

    @Test
    void countClicksWithWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countClicks(filter, Controller.GRAN_WEEK);
        assertArrayEquals(
                new int[] {12145, 11778},
                result
        );
    }

    @Test
    void countClicksWithMonthTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countClicks(filter, Controller.GRAN_MONTH);
        assertArrayEquals(
                new int[] {23923},
                result
        );
    }


    @Test
    void countClicksPMWithTimeOfDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countClicksPM(filter, Controller.PERFORMANCE_DAY);
        assertArrayEquals(
                new int[] {212, 66, 75, 59, 192, 468, 772, 1039, 1295, 1443, 1483, 1484, 1429, 1475, 1465, 1515, 1446, 1485, 1455, 1512, 1260, 1046, 764, 483},
                result
        );
    }


    @Test
    void countClicksPMWithDayOfWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countClicksPM(filter, Controller.PERFORMANCE_WEEK);
        assertArrayEquals(
                new int[] {3673, 3771, 3886, 2583, 2876, 3595, 3539},
                result
        );
    }


    // Bounces Tests
    @Test
    void countBouncesWithDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countBounces(filter, Controller.GRAN_DAY);
        for(int a : result) System.out.print(a + ", ");
        assertArrayEquals(
                new int[] {23, 24, 26, 23, 20, 24, 20, 31, 24, 24, 27, 32, 22},
                result
        );
    }

    @Test
    void countBouncesWithWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countBounces(filter, Controller.GRAN_WEEK);
        for(int a : result) System.out.print(a + ", ");
        assertArrayEquals(
                new int[] {160, 160},
                result
        );
    }

    @Test
    void countBouncesWithMonthTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countBounces(filter, Controller.GRAN_MONTH);
        for(int a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new int[] {320},
                result
        );
    }

    @Test
    void countBouncesPMWithTimeOfDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countBouncesPM(filter, Controller.PERFORMANCE_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new int[] {212, 66, 75, 59, 192, 468, 772, 1039, 1295, 1441, 1484, 1485, 1429, 1475, 1465, 1515, 1446, 1485, 1454, 1512, 1261, 1045, 765, 483},
                result
        );
    }

    @Test
    void countBouncePMWithDayOfWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countBouncesPM(filter, Controller.PERFORMANCE_WEEK);
        for(float a : result) System.out.print(a + " ,");

        assertArrayEquals(
                new int[] {3673, 3771, 3886, 2583, 2876, 3595, 3539},
                result
        );
    }


    //Bounce Rate Tests
    @Test
    void countBounceRateWithDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.countBounceRate(filter, Controller.GRAN_DAY);
        for(float a : result) System.out.print(a + ", ");
        assertArrayEquals(
                new float[] {(float) 0.014312384,(float)  0.013761468,(float)  0.015568863,(float)  0.013666073,(float)  0.010995052,(float)  0.013296399,(float)  0.011007154,(float)  0.016367476,(float)  0.012545739,(float)  0.012882448,(float)  0.013339921,(float)  0.015173068,(float)  0.01113924},
                result
        );
    }

    @Test
    void countBounceRateWithWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.countBounceRate(filter, Controller.GRAN_WEEK);
        for(float a : result) System.out.print(a + ", ");
        assertArrayEquals(
                new float[] {(float) 0.0131741455, (float) 0.013584649},
                result
        );
    }

    @Test
    void countBounceRateWithMonthTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.countBounceRate(filter, Controller.GRAN_MONTH);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 0.013376249},
                result
        );
    }

    @Test
    void countBounceRatePMWithTimeOfDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.countBounceRatePM(filter, Controller.PERFORMANCE_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 0.998614 ,(float) 1.0006744 ,(float) 1.0006739 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 0.9993127 ,(float) 1.0 ,(float) 1.0007937 ,(float) 0.999044 ,(float) 1.0013089 ,(float) 1.0},
                result
        );
    }

    @Test
    void countBounceRatePMWithDayOfWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.countBounceRatePM(filter, Controller.PERFORMANCE_WEEK);
        for(float a : result) System.out.print(a + " ,");

        assertArrayEquals(
                new float[] {(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0},
                result
        );
    }

    // Conversions Tests
    @Test
    void countConversionsWithDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countConversions(filter, Controller.GRAN_DAY);
        for(int a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new int[] {145 ,157 ,135 ,148 ,142 ,126 ,154 ,171 ,157 ,164 ,184 ,190 ,153},
                result
        );
    }

    @Test
    void countConversionsWithWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countConversions(filter, Controller.GRAN_WEEK);
        for(int a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new int[] {1007 ,1019},
                result
        );
    }

    @Test
    void countConversionsWithMonthTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countConversions(filter, Controller.GRAN_MONTH);
        for(int a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new int[] {2026},
                result
        );
    }

    @Test
    void countConversionsPMWithTimeOfDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countConversionsPM(filter, Controller.PERFORMANCE_DAY);
        for(int a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new int[] {16 ,5 ,6 ,6 ,12 ,44 ,56 ,77 ,111 ,116 ,120 ,124 ,122 ,117 ,139 ,131 ,124 ,111 ,139 ,122 ,109 ,108 ,75 ,36},
                result
        );
    }


    @Test
    void countConversionsPMWithDayOfWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countConversionsPM(filter, Controller.PERFORMANCE_WEEK);
        for(int a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new int[] {307 ,337 ,289 ,207 ,257 ,323 ,306},
                result
        );
    }


    // Uniques Tests
    @Test
    void countUniquesWithDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countUniques(filter, Controller.GRAN_DAY);
        assertArrayEquals(
                new int[] {1606 ,1738 ,1664 ,1672 ,1806 ,1799 ,1807 ,1885 ,1904 ,1851 ,2014 ,2098 ,1962},
                result
        );
    }

    @Test
    void countUniquesWithWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countUniques(filter, Controller.GRAN_WEEK);
        assertArrayEquals(
                new int[] {12092 ,11714},
                result
        );
    }

    @Test
    void countUniquesWithMonthTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countUniques(filter, Controller.GRAN_MONTH);
        assertArrayEquals(
                new int[] {23806},
                result
        );
    }

    @Test
    void countUniquesPMWithTimeOfDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countUniquesPM(filter, Controller.PERFORMANCE_DAY);
        assertArrayEquals(
                new int[] {212 ,66 ,75 ,59 ,192 ,468 ,772 ,1038 ,1295 ,1443 ,1483 ,1484 ,1429 ,1475 ,1465 ,1514 ,1445 ,1485 ,1454 ,1511 ,1260 ,1046 ,764 ,483},
                result
        );
    }

    @Test
    void countUniquesPMWithDayOfWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        int[] result = controller.countUniquesPM(filter, Controller.PERFORMANCE_WEEK);
        assertArrayEquals(
                new int[] {3671 ,3771 ,3884 ,2583 ,2876 ,3594 ,3536},
                result
        );
    }


    // Total Cost Tests
    @Test
    void countTotalCostWithDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeTotalCost(filter, Controller.GRAN_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 7820.5796 ,(float) 8483.418 ,(float) 8356.028 ,(float) 8397.711 ,(float) 8966.057 ,(float) 8775.406 ,(float) 9169.188 ,(float) 9093.673 ,(float) 9842.434 ,(float) 9150.713 ,(float) 10012.71 ,(float) 10374.721 ,(float) 9655.306},
                result
        );
    }

    @Test
    void countTotalCostWithWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeTotalCost(filter, Controller.GRAN_WEEK);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 59968.496 ,(float) 58129.59},
                result
        );
    }

    @Test
    void countTotalCostWithMonthTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeTotalCost(filter, Controller.GRAN_MONTH);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 118097.78},
                result
        );
    }

    @Test
    void countTotalCostPMWithTimeOfDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeTotalCostPM(filter, Controller.PERFORMANCE_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 1207.0 ,(float) 470.0 ,(float) 409.0 ,(float) 263.0 ,(float) 1201.0 ,(float) 2744.0 ,(float) 4452.0 ,(float) 6191.0 ,(float) 7748.0 ,(float) 8852.0 ,(float) 8487.0 ,(float) 8661.0 ,(float) 8663.0 ,(float) 8751.0 ,(float) 9057.0 ,(float) 9084.0 ,(float) 8797.0 ,(float) 8353.0 ,(float) 8448.0 ,(float) 9030.0 ,(float) 7864.0 ,(float) 6143.0 ,(float) 4347.0 ,(float) 2976.0},
                result
        );
    }

    @Test
    void countTotalCostPMWithDayOfWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeTotalCostPM(filter, Controller.PERFORMANCE_WEEK);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 21819.0 ,(float) 21881.0 ,(float) 21894.0 ,(float) 15019.0 ,(float) 17613.0 ,(float) 22208.0 ,(float) 21773.0},
                result
        );
    }


    //CountCostsTest
    @Test
    void countCostWithDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.clickCost(filter, Controller.GRAN_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 7787.353 ,(float) 8449.57 ,(float) 8322.43 ,(float) 8363.798 ,(float) 8929.262 ,(float) 8738.175 ,(float) 9130.491 ,(float) 9054.909 ,(float) 9804.517 ,(float) 9111.52 ,(float) 9969.973 ,(float) 10332.851 ,(float) 9616.036},
                result
        );
    }

    @Test
    void countCostWithWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.clickCost(filter, Controller.GRAN_WEEK);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 59721.176 ,(float) 57889.848},
                result
        );
    }

    @Test
    void countCostWithMonthTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.clickCost(filter, Controller.GRAN_MONTH);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 117610.766},
                result
        );
    }

    @Test
    void countCostPMWithTimeOfDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.clickCostPM(filter, Controller.PERFORMANCE_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 1037.6184 ,(float) 411.9956 ,(float) 319.40237 ,(float) 216.95825 ,(float) 988.579 ,(float) 2211.2927 ,(float) 3788.128 ,(float) 5215.7397 ,(float) 6464.052 ,(float) 7303.2993 ,(float) 7264.6816 ,(float) 7190.4775 ,(float) 6903.482 ,(float) 7110.5425 ,(float) 7366.76 ,(float) 7556.897 ,(float) 7360.9116 ,(float) 6993.2515 ,(float) 6821.4126 ,(float) 7347.4526 ,(float) 6419.1685 ,(float) 5163.9365 ,(float) 3580.5032 ,(float) 2574.3247},
                result
        );
    }

    @Test
    void countCostPMWithDayOfWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.clickCostPM(filter, Controller.PERFORMANCE_WEEK);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 18052.22 ,(float) 18593.586 ,(float) 18911.988 ,(float) 12803.608 ,(float) 13922.655 ,(float) 17749.08 ,(float) 17577.727},
                result
        );
    }
    //CPM Tests
    @Test
    void countCPMWithDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPM(filter, Controller.GRAN_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 234.71127 ,(float) 249.67384 ,(float) 247.57135 ,(float) 244.07693 ,(float) 244.77359 ,(float) 238.98816 ,(float) 238.76852 ,(float) 239.22534 ,(float) 260.12723 ,(float) 236.57478 ,(float) 235.7485 ,(float) 247.14073 ,(float) 241.89061},
                result
        );
    }

    @Test
    void countCPMWithWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPM(filter, Controller.GRAN_WEEK);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 242.58414 ,(float) 243.3249},
                result
        );
    }

    @Test
    void countCPMWithMonthTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPM(filter, Controller.GRAN_MONTH);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 242.94757},
                result
        );
    }

    @Test
    void countCPMPMWithTimeOfDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPMPM(filter, Controller.PERFORMANCE_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 4758.647 ,(float) 6107.8145 ,(float) 4431.244 ,(float) 3937.19 ,(float) 4944.8257 ,(float) 4762.65 ,(float) 4804.346 ,(float) 4998.6553 ,(float) 5000.6567 ,(float) 5031.6665 ,(float) 4895.5977 ,(float) 4856.7246 ,(float) 4957.82 ,(float) 4843.4004 ,(float) 5002.586 ,(float) 4966.1943 ,(float) 5024.5957 ,(float) 4670.5454 ,(float) 4658.566 ,(float) 4928.0874 ,(float) 4998.4443 ,(float) 4881.441 ,(float) 4733.2534 ,(float) 5135.9863},
                result
        );
    }


    @Test
    void countCPMPMWithDayOfWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPMPM(filter, Controller.PERFORMANCE_WEEK);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 4906.525 ,(float) 4925.757 ,(float) 4854.3447 ,(float) 4969.033 ,(float) 4768.459 ,(float) 4944.1416 ,(float) 4963.9062},
                result
        );
    }


    // CTR Tests
    @Test
    void countCTRWithDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCTR(filter, Controller.GRAN_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 20.734287 ,(float) 19.482798 ,(float) 20.21078 ,(float) 20.443256 ,(float) 20.137438 ,(float) 20.342936 ,(float) 21.134838 ,(float) 20.07022 ,(float) 19.778881 ,(float) 20.76221 ,(float) 20.98419 ,(float) 19.904694 ,(float) 20.210632},
                result
        );
    }

    @Test
    void countCTRWithWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCTR(filter, Controller.GRAN_WEEK);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 20.354631 ,(float) 20.283325},
                result
        );
    }

    @Test
    void countCTRWithMonthTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCTR(filter, Controller.GRAN_MONTH);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 20.319525},
                result
        );
    }

    @Test
    void countCTRPMWithTimeOfDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCTRPM(filter, Controller.PERFORMANCE_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0},
                result
        );
    }

    @Test
    void countCTRPMWithDayOfWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCTRPM(filter, Controller.PERFORMANCE_WEEK);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0 ,(float) 1.0},
                result
        );
    }

    // CPA Tests
    @Test
    void countCPAWithDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPA(filter, Controller.GRAN_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 53.93503 ,(float) 54.034508 ,(float) 61.896507 ,(float) 56.74129 ,(float) 63.141243 ,(float) 69.64608 ,(float) 59.540184 ,(float) 53.179375 ,(float) 62.69066 ,(float) 55.79703 ,(float) 54.4169 ,(float) 54.603794 ,(float) 63.10657},
                result
        );
    }

    @Test
    void countCPAWithWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPA(filter, Controller.GRAN_WEEK);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 59.551636 ,(float) 57.045723},
                result
        );
    }

    @Test
    void countCPAWithMonthTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPA(filter, Controller.GRAN_MONTH);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 58.291107},
                result
        );
    }

    @Test
    void countCPAPMWithTimeOfDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPAPM(filter, Controller.PERFORMANCE_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 4.4738064 ,(float) 8.441789 ,(float) 0.96432716 ,(float) 5.567343 ,(float) 4.9507465 ,(float) 6.104854 ,(float) 4.8024387 ,(float) 5.4604115 ,(float) 5.0336776 ,(float) 5.3168697 ,(float) 6.0736065 ,(float) 4.974112 ,(float) 5.4852924 ,(float) 4.7938175 ,(float) 5.097231 ,(float) 5.2833424 ,(float) 4.5061183 ,(float) 5.0222244 ,(float) 5.2433357 ,(float) 6.0053253 ,(float) 5.8787074 ,(float) 4.800624 ,(float) 4.2324443 ,(float) 5.2624288},
                result
        );
    }

    @Test
    void countCPAPMWithDayOfWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPAPM(filter, Controller.PERFORMANCE_WEEK);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 4.981866 ,(float) 5.4078307 ,(float) 5.809438 ,(float) 5.533542 ,(float) 5.1039066 ,(float) 5.44198 ,(float) 5.8203993},
                result
        );
    }

    // CPC Tests
    @Test
    void countCPCWithDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPC(filter, Controller.GRAN_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 4.866571 ,(float) 4.864345 ,(float) 5.0036097 ,(float) 4.989727 ,(float) 4.929113 ,(float) 4.861721 ,(float) 5.046334 ,(float) 4.801306 ,(float) 5.1450253 ,(float) 4.9118156 ,(float) 4.946991 ,(float) 4.9192605 ,(float) 4.8887625},
                result
        );
    }

    @Test
    void countCPCWithWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPC(filter, Controller.GRAN_WEEK);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 4.937711 ,(float) 4.935438},
                result
        );
    }

    @Test
    void countCPCWithMonthTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPC(filter, Controller.GRAN_MONTH);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 4.936579},
                result
        );
    }

    @Test
    void countCPCPMWithTimeOfDayTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPCPM(filter, Controller.PERFORMANCE_DAY);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 5.0070333 ,(float) 6.176385 ,(float) 4.540323 ,(float) 4.008715 ,(float) 5.1878953 ,(float) 5.386388 ,(float) 5.7489896 ,(float) 6.267545 ,(float) 6.58709 ,(float) 6.8037715 ,(float) 6.6759214 ,(float) 6.7939525 ,(float) 6.769595 ,(float) 6.7462873 ,(float) 6.8571105 ,(float) 6.787237 ,(float) 6.739658 ,(float) 6.5384216 ,(float) 6.5659833 ,(float) 6.761721 ,(float) 6.561099 ,(float) 6.1804905 ,(float) 5.6853433 ,(float) 5.700403},
                result
        );
    }

    @Test
    void countCPCPMWithDayOfWeekTest() throws SQLException {
        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
        float[] result = controller.computeCPCPM(filter, Controller.PERFORMANCE_WEEK);
        for(float a : result) System.out.print(a + " ,");
        assertArrayEquals(
                new float[] {(float) 9.465074 ,(float) 9.365397 ,(float) 9.480463 ,(float) 8.071176 ,(float) 8.636704 ,(float) 9.584349 ,(float) 9.475425},
                result
        );
    }
    // for(int a : result) System.out.print(a + " ,");
}