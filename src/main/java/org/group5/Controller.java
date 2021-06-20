package org.group5;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Performs data processing and mediates communication between view and model.
 */
public class Controller {

    private static final int MILLIS_PER_SEC = 1000;
    private static final int DEFAULT_TIME_SPENT = 1;
    private static final int DEFAULT_PAGES_VIEWED = 1;
    private static final BounceType DEFAULT_BOUNCE_TYPE = BounceType.TimeSpent;

    private static final int MILLIS_IN_HOUR = 1000 * 60 * 60;

    private Model model;

    private int bouncePagesViewedThreshold;
    private int bounceTimeSpentThreshold;
    private BounceType bounceType;

    public int countBounces() throws SQLException {

        String sql = switch (bounceType) {
            case TimeSpent -> "SELECT COUNT(*) FROM servers "
                    + "WHERE (exitDate - entryDate <= " + bounceTimeSpentThreshold / 1000 + ") "
                    + ";";
            case PagesViewed -> "SELECT COUNT(*) FROM servers "
                    + "WHERE (pagesViewed >= " + bouncePagesViewedThreshold + ") "
                    + ";";
        };

        return model.executeQuery(sql).getInt(1);
    }

    public int countConversions() throws SQLException {
        String sql = "SELECT COUNT(*) FROM servers "
                + "WHERE  Conversion = 'Yes'"
                + ";";

        return model.executeQuery(sql).getInt(1);
    }

    public int countClicks() throws SQLException {
        return model.executeQuery("SELECT COUNT(*) FROM clicks;").getInt(1);
    }

    public enum BounceType {
        TimeSpent,
        PagesViewed
    }

    public Controller(Model model) {
        this.model = model;
        bounceType = DEFAULT_BOUNCE_TYPE;
        bouncePagesViewedThreshold = DEFAULT_PAGES_VIEWED;
        bounceTimeSpentThreshold = DEFAULT_TIME_SPENT;
    }

    public int countImpressions() throws SQLException {
        return model.executeQuery("SELECT COUNT(*) FROM impressions;").getInt(1);
    }

    public Date getFirstDate() throws SQLException {
        long time = model.executeQuery("SELECT MIN(entryDate) FROM impressions;").getInt(1);
        return new Date(time * MILLIS_PER_SEC);
    }

    public Date getLastDate() throws SQLException {
        long time = model.executeQuery("SELECT MAX(entryDate) FROM impressions;").getInt(1);
        return new Date(time * MILLIS_PER_SEC);
    }

    // REQ3-4: One data point on chart corresponds to metric computed over specific time interval (hour/day/week) - GRANULARITY
    // REQ6: User should be able to filter metrics and charts by Date Range / Audience Segments (gender,age,income) / Context
    // EXT2: Chart displaying performance metrics per time of day / per time of week

    // Constants in seconds for granularity
    public static final int GRAN_DAY = 86400;
    public static final int GRAN_WEEK = 604800;
    public static final int GRAN_MONTH = 2592000;

    public int[] countImpressions(Filter filter, int granularity) throws SQLException {
        int start = (int) (filter.getStart().getTime() / 1000);
        int end = (int) (filter.getEnd().getTime() / 1000);

        String whereClause = buildWhereClause(filter, "impressions");

        String sql = "SELECT entryDate FROM impressions INNER JOIN users ON impressions.id=users.id " + whereClause + ";";

        ResultSet result = model.executeQuery(sql);

        int size = axisSize(start, end, granularity);
        int[] data = new int[size];

        while (result.next()) {
            int time = result.getInt(1);
            int idx = index(time, start, granularity);
            if (idx < size)
                data[idx]++;
        }

        result.close();

        return data;
    }

    public String buildGroupByClause(int granularity) {
        String grouping = switch (granularity) {
            case GRAN_DAY -> "%j";
            case GRAN_WEEK -> "%W";
            case GRAN_MONTH -> "%m";
            default -> throw new IllegalStateException("Unexpected value: " + granularity);
        };
        return " GROUP BY strftime('" + grouping + "', entryDate, 'unixepoch') ";
    }

    public static final int PERFORMANCE_DAY = 86400;
    public static final int PERFORMANCE_WEEK = 604800;

    // Performance metrics
    public int[] countImpressionsPM(Filter filter, int performanceMetric) throws SQLException {

        String grouping = (performanceMetric == PERFORMANCE_DAY) ? "%H" : "%w";
        int size = (performanceMetric == PERFORMANCE_DAY) ? 24 : 7;

        String whereClause = buildWhereClause(filter, "impressions");

        String sql = "SELECT COUNT(*) FROM impressions INNER JOIN users ON impressions.id=users.id " + whereClause
                + " GROUP BY strftime('" + grouping + "', entryDate, 'unixepoch') "
                + " ORDER BY strftime('" + grouping + "', entryDate, 'unixepoch');";

        ResultSet result = model.executeQuery(sql);

        int[] data = new int[size];

        int i = 0;
        while (result.next()) {
            data[i] = result.getInt(1);
            i++;
        }

//        System.out.println(Arrays.toString(data));

        result.close();

        return data;

    }

    private String buildWhereClause(Filter filter, String entryTable) throws SQLException {

        List<String> conditions = new ArrayList<>();

        int start = (int) (filter.getStart().getTime() / 1000);
        int end = (int) (filter.getEnd().getTime() / 1000);

        if (end < start) throw new SQLException("End date is earlier than start date!", "");

        conditions.add(" (" + entryTable + ".entryDate BETWEEN " + start + " AND " + end + ") ");

        if (filter.hasGender()) {
            conditions.add(" (gender = '" + filter.getGender() + "') ");
        }

        if (filter.hasAge()) {
            conditions.add(" (age = '" + filter.getAge() + "') ");
        }

        if (filter.hasIncome()) {
            conditions.add(" (income = '" + filter.getIncome() + "') ");
        }

        if (filter.hasContext()) {
            conditions.add(" (context = '" + filter.getContext() + "') ");
        }

        StringBuilder whereClause = new StringBuilder("WHERE");
        for (String condition : conditions) {
            whereClause.append(condition);
            whereClause.append(" AND ");
        }
        whereClause.setLength(whereClause.length() - 5); // Remove trailing AND

        return whereClause.toString();
    }

    private int axisSize(int start, int end, int granularity) {
        int diff = end - start;
        return diff / granularity + 1;
    }

    private int index(int time, int start, int granularity) {
        return (time - start) / granularity;
    }

    public int countUniques() throws SQLException {
        String sql = "SELECT COUNT(DISTINCT id) FROM clicks;";
        return model.executeQuery(sql).getInt(1);

    }

    public int[] countUniques(Filter filter, int granularity) throws SQLException {

        int start = (int) (filter.getStart().getTime() / 1000);
        int end = (int) (filter.getEnd().getTime() / 1000);

        String whereClause = buildWhereClause(filter, "clicks");

        String sql = "SELECT clicks.entryDate FROM clicks INNER JOIN users ON clicks.id=users.id INNER JOIN impressions ON clicks.id=impressions.id "
                + whereClause
                + " GROUP BY clicks.id "
                + ";";

        ResultSet result = model.executeQuery(sql);

        int size = axisSize(start, end, granularity);
        int[] data = new int[size];

        while (result.next()) {
            int time = result.getInt(1);
            int idx = index(time, start, granularity);
            if (idx < size)
                data[idx]++;
        }

        result.close();

        return data;

    }

    public int[] countUniquesPM(Filter filter, int performanceMetric) throws SQLException {

        String grouping = (performanceMetric == PERFORMANCE_DAY) ? "%H" : "%w";
        int size = (performanceMetric == PERFORMANCE_DAY) ? 24 : 7;

        String whereClause = buildWhereClause(filter, "clicks");

        String sql = "SELECT COUNT(DISTINCT clicks.id) FROM clicks INNER JOIN users ON clicks.id=users.id " + whereClause
                + " GROUP BY strftime('" + grouping + "', entryDate, 'unixepoch') "
                + " ORDER BY strftime('" + grouping + "', entryDate, 'unixepoch');";

        ResultSet result = model.executeQuery(sql);

        int[] data = new int[size];

        int i = 0;
        while (result.next()) {
            data[i] = result.getInt(1);
            i++;
        }

//        System.out.println(Arrays.toString(data));

        result.close();

        return data;

    }

    public int[] countClicks(Filter filter, int granularity) throws SQLException {

        int start = (int) (filter.getStart().getTime() / 1000);
        int end = (int) (filter.getEnd().getTime() / 1000);

        String whereClause = buildWhereClause(filter, "clicks");

        String sql = "SELECT clicks.entryDate FROM clicks INNER JOIN users ON clicks.id=users.id INNER JOIN impressions ON clicks.id=impressions.id "
                + whereClause
                + ";";

        ResultSet result = model.executeQuery(sql);

        int size = axisSize(start, end, granularity);
        int[] data = new int[size];

        while (result.next()) {
            int time = result.getInt(1);
            int idx = index(time, start, granularity);
            if (idx < size)
                data[idx]++;
        }

        result.close();

        return data;
    }

    public int[] countClicksPM(Filter filter, int performanceMetric) throws SQLException {

        String grouping = (performanceMetric == PERFORMANCE_DAY) ? "%H" : "%w";
        int size = (performanceMetric == PERFORMANCE_DAY) ? 24 : 7;

        String whereClause = buildWhereClause(filter, "clicks");

        String sql = "SELECT COUNT(*) FROM clicks INNER JOIN users ON clicks.id=users.id " + whereClause
                + " GROUP BY strftime('" + grouping + "', entryDate, 'unixepoch') "
                + " ORDER BY strftime('" + grouping + "', entryDate, 'unixepoch');";

        ResultSet result = model.executeQuery(sql);

        int[] data = new int[size];

        int i = 0;
        while (result.next()) {
            data[i] = result.getInt(1);
            i++;
        }

        result.close();
//        System.out.println(Arrays.toString(data));

        return data;

    }

    private int[] countBouncesByTimeSpent(Filter filter, int granularity) throws SQLException {

        int start = (int) (filter.getStart().getTime() / 1000);
        int end = (int) (filter.getEnd().getTime() / 1000);

        String whereClause = buildWhereClause(filter, "servers");

        String sql = "SELECT servers.entryDate, servers.exitDate FROM servers INNER JOIN users ON servers.id=users.id INNER JOIN impressions ON servers.id=impressions.id "
                + whereClause + " AND servers.exitDate - servers.entryDate <= " + bounceTimeSpentThreshold / 1000
                + ";";

        ResultSet result = model.executeQuery(sql);

        int size = axisSize(start, end, granularity);
        int[] data = new int[size];

        while (result.next()) {
            int time = result.getInt(1);
            int idx = index(time, start, granularity);
            data[idx]++;
        }

        result.close();

        return data;
    }

    private int[] countBouncesByPagesViewed(Filter filter, int granularity) throws SQLException {

        int start = (int) (filter.getStart().getTime() / 1000);
        int end = (int) (filter.getEnd().getTime() / 1000);

        String whereClause = buildWhereClause(filter, "servers");

        String sql = "SELECT servers.entryDate, servers.pagesViewed FROM servers INNER JOIN users ON servers.id=users.id INNER JOIN impressions ON servers.id=impressions.id "
                + whereClause + " AND pagesViewed <= " + bouncePagesViewedThreshold
                + ";";

        ResultSet result = model.executeQuery(sql);

        int size = axisSize(start, end, granularity);
        int[] data = new int[size];

        while (result.next()) {
            int time = result.getInt(1);
            int idx = index(time, start, granularity);
            data[idx]++;
        }

        result.close();

        return data;
    }

    public int[] countBounces(Filter filter, int granularity) throws SQLException {
        return switch (bounceType) {
            case TimeSpent -> countBouncesByTimeSpent(filter, granularity);
            case PagesViewed -> countBouncesByPagesViewed(filter, granularity);
        };
    }

    //TODO:
    // THIS MIGHT NEED A FIX
    public int[] countBouncesPM(Filter filter, int performanceMetric) throws SQLException {

        String grouping = (performanceMetric == PERFORMANCE_DAY) ? "%H" : "%w";
        int size = (performanceMetric == PERFORMANCE_DAY) ? 24 : 7;

        String whereClause = buildWhereClause(filter, "servers");

        String sql = "SELECT COUNT(*) FROM servers INNER JOIN users ON servers.id=users.id " + whereClause
                + " GROUP BY strftime('" + grouping + "', entryDate, 'unixepoch') "
                + " ORDER BY strftime('" + grouping + "', entryDate, 'unixepoch');";

        ResultSet result = model.executeQuery(sql);

        int[] data = new int[size];

        int i = 0;
        while (result.next()) {
            data[i] = result.getInt(1);
            i++;
        }

//        System.out.println(Arrays.toString(data));

        result.close();

        return data;

    }

    public float[] countBounceRate(Filter filter, int granularity) throws SQLException {

        int[] bounces = countBounces(filter, granularity);
        int[] clicks = countClicks(filter, granularity);

        float[] bounceRate = new float[bounces.length];
        for (int i = 0; i < bounces.length; i++) {
            bounceRate[i] = bounces[i] / (float) clicks[i];
        }

        return bounceRate;
    }

    public float[] countBounceRatePM(Filter filter, int perfMetric) throws SQLException {
        int[] bouncesPM = countBouncesPM(filter, perfMetric);
        int[] clicksPM = countClicksPM(filter, perfMetric);

        float[] bounceRatePM = new float[bouncesPM.length];
        for (int i = 0; i < bouncesPM.length; i++) {
            bounceRatePM[i] = bouncesPM[i] / (float) clicksPM[i];
        }

        return bounceRatePM;
    }

    public HistogramData computeClickCostHistogram(Date start, Date end) throws SQLException {

        long timeStartSec = start.getTime() / 1000;
        long timeEndSec = end.getTime() / 1000;

        // Get the maximum and minimum click costs in the range
        String maxMinSql = "SELECT MAX(cost), MIN(cost) FROM clicks "
                + "WHERE (entryDate BETWEEN " + timeStartSec + " AND " + timeEndSec + ");";
        ResultSet maxMinResults = model.executeQuery(maxMinSql);
        float max = maxMinResults.getFloat(1);
        float min = maxMinResults.getFloat(2);
        maxMinResults.close();

        // Calculate an appropriate division to get N bars
        int N = 11;
        float interval = (max - min) / 11;

        // Get data
        String dataSql = "SELECT cost FROM clicks "
                + "WHERE (entryDate BETWEEN " + timeStartSec + " AND " + timeEndSec + ");";
        ResultSet dataResults = model.executeQuery(dataSql);

        int[] data = new int[N + 1];

        // Construct histogram
        while (dataResults.next()) {
            float cost = dataResults.getFloat(1);
            int idx = (cost - min == 0) ? 0 : (int) ((cost - min) / interval);
            // Increment frequency for this cost
            data[idx]++;
        }

        dataResults.close();

        return new HistogramData(data, interval, min, max);
    }

    public int[] countConversions(Filter filter, int granularity) throws SQLException {

        int start = (int) (filter.getStart().getTime() / 1000);
        int end = (int) (filter.getEnd().getTime() / 1000);

        String sql = "SELECT servers.entryDate, conversion FROM servers INNER JOIN users ON servers.id=users.id INNER JOIN impressions ON servers.id=impressions.id "
                + buildWhereClause(filter, "servers") + " AND (conversion = 'Yes' ) "
                + ";";

        ResultSet result = model.executeQuery(sql);

        int size = axisSize(start, end, granularity);
        int[] data = new int[size];

        while (result.next()) {
            int time = result.getInt(1);
            int idx = index(time, start, granularity);
            data[idx]++;
        }

        result.close();

        return data;
    }

    public int[] countConversionsPM(Filter filter, int performanceMetric) throws SQLException {

        String grouping = (performanceMetric == PERFORMANCE_DAY) ? "%H" : "%w";
        int size = (performanceMetric == PERFORMANCE_DAY) ? 24 : 7;

        String whereClause = buildWhereClause(filter, "servers");

        String sql = "SELECT COUNT(conversion) FROM servers INNER JOIN users ON servers.id=users.id " + whereClause + " AND (conversion = 'Yes' ) "
                + " GROUP BY strftime('" + grouping + "', entryDate, 'unixepoch') "
                + " ORDER BY strftime('" + grouping + "', entryDate, 'unixepoch');";

        ResultSet result = model.executeQuery(sql);

        int[] data = new int[size];

        int i = 0;
        while (result.next()) {
            data[i] = result.getInt(1);
            i++;
        }

        result.close();
        return data;
    }

    public float computeTotalCost() throws SQLException {
        String sql = "SELECT SUM(impressions.cost + clicks.cost) FROM impressions INNER JOIN clicks ON impressions.id=clicks.id";
        return model.executeQuery(sql).getFloat(1);
    }

    public float[] computeTotalCost(Filter filter, int granularity) throws SQLException {

        int start = (int) (filter.getStart().getTime() / 1000);
        int end = (int) (filter.getEnd().getTime() / 1000);

        String sql = "SELECT entryDate, cost FROM impressions INNER JOIN users ON impressions.id=users.id "
                + buildWhereClause(filter, "impressions")
                + ";";

        ResultSet result = model.executeQuery(sql);
        int size = axisSize(start, end, granularity);
        float[] data = new float[size];

        while (result.next()) {
            int time = result.getInt(1);
            int idx = index(time, start, granularity);
            float cost = result.getFloat(2);
            data[idx] += cost;
        }

        result.close();

        String sql1 = "SELECT clicks.entryDate,clicks.cost FROM clicks INNER JOIN impressions ON clicks.id=impressions.id "
                + buildWhereClause(filter, "clicks")
                + ";";

        ResultSet result1 = model.executeQuery(sql1);
        while (result1.next()) {
            int time = result1.getInt(1);
            int idx = index(time, start, granularity);
            float cost = result1.getFloat(2);
            data[idx] += cost;
        }

        result1.close();

        return data;
    }

    public float[] computeTotalCostPM(Filter filter, int performanceMetric) throws SQLException {

        String grouping = (performanceMetric == PERFORMANCE_DAY) ? "%H" : "%w";
        int size = (performanceMetric == PERFORMANCE_DAY) ? 24 : 7;

        String whereClause = buildWhereClause(filter, "impressions");

        String sql = "SELECT ( SUM(impressions.cost) + SUM(clicks.cost) ) FROM clicks INNER JOIN users ON clicks.id=users.id INNER JOIN impressions ON impressions.id=clicks.id " + whereClause
                + " GROUP BY strftime('" + grouping + "', impressions.entryDate, 'unixepoch') "
                + " ORDER BY strftime('" + grouping + "', impressions.entryDate, 'unixepoch');";

        ResultSet result = model.executeQuery(sql);

        float[] data = new float[size];

        int i = 0;
        while (result.next()) {
            data[i] = result.getInt(1);
            i++;
        }

        result.close();

        return data;

    }

    public float[] clickCost(Filter filter, int granularity) throws SQLException {

        int start = (int) (filter.getStart().getTime() / 1000);
        int end = (int) (filter.getEnd().getTime() / 1000);

        String sql1 = "SELECT clicks.entryDate,clicks.cost FROM clicks INNER JOIN impressions ON clicks.id=impressions.id "
                + buildWhereClause(filter, "clicks")
                + ";";

        int size = axisSize(start, end, granularity);
        float[] data = new float[size];

        ResultSet result1 = model.executeQuery(sql1);
        while (result1.next()) {
            int time = result1.getInt(1);
            int idx = index(time, start, granularity);
            float cost = result1.getFloat(2);
            data[idx] += cost;
        }

        result1.close();

        return data;
    }

    public float[] clickCostPM(Filter filter, int performanceMetric) throws SQLException {

        String grouping = (performanceMetric == PERFORMANCE_DAY) ? "%H" : "%w";
        int size = (performanceMetric == PERFORMANCE_DAY) ? 24 : 7;

        String whereClause = buildWhereClause(filter, "clicks");

        String sql = "SELECT SUM(clicks.cost) FROM clicks INNER JOIN users ON clicks.id=users.id " + whereClause
                + " GROUP BY strftime('" + grouping + "', entryDate, 'unixepoch') "
                + " ORDER BY strftime('" + grouping + "', entryDate, 'unixepoch');";

        ResultSet result = model.executeQuery(sql);

        float[] data = new float[size];

        int i = 0;
        while (result.next()) {
            data[i] = result.getFloat(1);
            i++;
        }

        result.close();
        return data;

    }


    public float[] computeCTR(Filter filter, int granularity) throws SQLException {

        int start = (int) (filter.getStart().getTime() / 1000);
        int end = (int) (filter.getEnd().getTime() / 1000);

        String sql = "SELECT entryDate FROM impressions INNER JOIN users ON impressions.id = users.id "
                + buildWhereClause(filter, "impressions")
                + ";";

        ResultSet result = model.executeQuery(sql);

        int size = axisSize(start, end, granularity);
        float[] data = new float[size];

        while (result.next()) {
            int time = result.getInt(1);
            int idx = index(time, start, granularity);
            data[idx]++;
        }

        result.close();

        String sql1 = "SELECT clicks.entryDate FROM clicks INNER JOIN users ON clicks.id=users.id INNER JOIN impressions ON clicks.id=impressions.id "
                + buildWhereClause(filter, "clicks")
                + ";";

        ResultSet result1 = model.executeQuery(sql1);

        int[] data1 = new int[size];
        while (result1.next()) {
            int time = result1.getInt(1);
            int idx = index(time, start, granularity);
            data1[idx]++;
        }


        float[] data2 = new float[size];
        for (int i = 0; i < size; i++) {
            data2[i] = data[i] / (float) data1[i];
        }

        result1.close();

        return data2;
    }

    public float[] computeCTRPM(Filter filter, int performanceMetric) throws SQLException {

        String grouping = (performanceMetric == PERFORMANCE_DAY) ? "%H" : "%w";
        int size = (performanceMetric == PERFORMANCE_DAY) ? 24 : 7;

        String whereClause = buildWhereClause(filter, "impressions");

        String sql = "SELECT ( CAST ( COUNT(clicks.entryDate) as REAL ) / CAST ( COUNT(impressions.entryDate) as REAL ) ) FROM clicks INNER JOIN users ON clicks.id=users.id INNER JOIN impressions ON impressions.id=clicks.id " + whereClause
                + " GROUP BY strftime('" + grouping + "', impressions.entryDate, 'unixepoch') "
                + " ORDER BY strftime('" + grouping + "', impressions.entryDate, 'unixepoch');";

        ResultSet result = model.executeQuery(sql);

        float[] data = new float[size];

        int i = 0;
        while (result.next()) {
            data[i] = result.getFloat(1);
            i++;
        }

        result.close();

        return data;

    }

    public float[] computeCPA(Filter filter, int granularity) throws SQLException {

        int start = (int) (filter.getStart().getTime() / 1000);
        int end = (int) (filter.getEnd().getTime() / 1000);

        int size = axisSize(start, end, granularity);
        float[] data;
        int[] data1;
        float[] data2 = new float[size];

        data = computeTotalCost(filter, granularity);
        data1 = countConversions(filter, granularity);

        for (int i = 0; i < size; i++) {
            if (data1[i] != 0) data2[i] = data[i] / (float) data1[i];
            else data2[i] = 0;
        }

        return data2;
    }

    public float[] computeCPAPM(Filter filter, int performanceMetric) throws SQLException {

        String grouping = (performanceMetric == PERFORMANCE_DAY) ? "%H" : "%w";
        int size = (performanceMetric == PERFORMANCE_DAY) ? 24 : 7;

        String whereClause = buildWhereClause(filter, "impressions");

        String sql = "SELECT ( SUM(impressions.cost) + SUM(clicks.cost) / COUNT(conversion) ) FROM clicks INNER JOIN users ON clicks.id=users.id INNER JOIN impressions ON impressions.id=clicks.id INNER JOIN servers ON servers.id = clicks.id " + whereClause + " AND ( conversion = 'Yes' ) "
                + " GROUP BY strftime('" + grouping + "', impressions.entryDate, 'unixepoch') "
                + " ORDER BY strftime('" + grouping + "', impressions.entryDate, 'unixepoch');";

        ResultSet result = model.executeQuery(sql);

        float[] data = new float[size];

        int i = 0;
        while (result.next()) {
            data[i] = result.getFloat(1);
            i++;
        }

        result.close();

        return data;

    }

    public float[] computeCPC(Filter filter, int granularity) throws SQLException {

        int start = (int) (filter.getStart().getTime() / 1000);
        int end = (int) (filter.getEnd().getTime() / 1000);


        int size = axisSize(start, end, granularity);
        float[] data;
        int[] data1;
        float[] data2 = new float[size];

        data = computeTotalCost(filter, granularity);
        data1 = countClicks(filter, granularity);

        for (int i = 0; i < size; i++) {
            if (data1[i] != 0) data2[i] = data[i] / (float) data1[i];
            else data2[i] = 0;
        }

        return data2;
    }

    public float[] computeCPCPM(Filter filter, int performanceMetric) throws SQLException {

        String grouping = (performanceMetric == PERFORMANCE_DAY) ? "%H" : "%w";
        int size = (performanceMetric == PERFORMANCE_DAY) ? 24 : 7;

        String whereClause = buildWhereClause(filter, "impressions");

        String sql = "SELECT ( SUM(impressions.cost) + SUM(clicks.cost) / COUNT(clicks.entryDate) ) FROM clicks INNER JOIN users ON clicks.id=users.id INNER JOIN impressions ON impressions.id=clicks.id INNER JOIN servers ON servers.id = clicks.id " + whereClause
                + " GROUP BY strftime('" + grouping + "', impressions.entryDate, 'unixepoch') "
                + " ORDER BY strftime('" + grouping + "', impressions.entryDate, 'unixepoch');";

        ResultSet result = model.executeQuery(sql);

        float[] data = new float[size];

        int i = 0;
        while (result.next()) {
            data[i] = result.getFloat(1);
            i++;
        }

        result.close();

        return data;

    }

    public float[] computeCPM(Filter filter, int granularity) throws SQLException {

        int start = (int) (filter.getStart().getTime() / 1000);
        int end = (int) (filter.getEnd().getTime() / 1000);

        int size = axisSize(start, end, granularity);

        float[] data;
        int[] data1;
        float[] data2 = new float[size];

        data = computeTotalCost(filter, granularity);
        data1 = countImpressions(filter, granularity);

        for (int i = 0; i < size; i++) {
            if (data1[i] != 0) data2[i] = data[i] / (float) data1[i] * 1000;
            else data2[i] = 0;
        }

        return data2;
    }

    public float[] computeCPMPM(Filter filter, int performanceMetric) throws SQLException {

        String grouping = (performanceMetric == PERFORMANCE_DAY) ? "%H" : "%w";
        int size = (performanceMetric == PERFORMANCE_DAY) ? 24 : 7;

        String whereClause = buildWhereClause(filter, "impressions");

        String sql = "SELECT ( SUM(impressions.cost) + SUM(clicks.cost) / COUNT(impressions.entryDate) * 1000 ) FROM clicks INNER JOIN users ON clicks.id=users.id INNER JOIN impressions ON impressions.id=clicks.id INNER JOIN servers ON servers.id = clicks.id " + whereClause
                + " GROUP BY strftime('" + grouping + "', impressions.entryDate, 'unixepoch') "
                + " ORDER BY strftime('" + grouping + "', impressions.entryDate, 'unixepoch');";

        ResultSet result = model.executeQuery(sql);

        float[] data = new float[size];

        int i = 0;
        while (result.next()) {
            data[i] = result.getFloat(1);
            i++;
        }

        result.close();

        return data;

    }


    public void loadFiles(String impressionFilePath, String clickFilePath, String serverFilePath)
            throws IOException, SQLException {
//        model.closeConn();
        model.recreate();
        model.loadImpressions(impressionFilePath);
        model.loadClicks(clickFilePath);
        model.loadServers(serverFilePath);
    }


    public void setPagesViewedThreshold(int threshold) {
        bouncePagesViewedThreshold = threshold;
    }

    public void setBounceTimeSpentThreshold(int threshold) {
        bounceTimeSpentThreshold = threshold;
    }

    public int getBouncePagesViewedThreshold() {
        return bouncePagesViewedThreshold;
    }

    public int getBounceTimeSpentThreshold() {
        return bounceTimeSpentThreshold;
    }

    public void setBounceType(BounceType bounceType) {
        this.bounceType = bounceType;
    }

    public BounceType getBounceType() {
        return bounceType;
    }

    // Calculates number of buckets in a time period given a granularity
    private int numBuckets(long startTime, long endTime, int granularity) {
        long difference = endTime - startTime;
        long buckets = difference / granularity;
        if (difference % granularity != 0) buckets++;
        return (int) buckets;
    }

    // Calculate bucket index for a particular data point
    private int bucketIndex(long dataPointTime, long dataStartTime, int granularity) {
        return (int) ((dataPointTime - dataStartTime) / granularity);
    }

}
