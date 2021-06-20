package org.group5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class Model {

    Connection database;

    static int fileinc = 1;

    public Model() throws SQLException {
        recreate();
    }

    public void recreate() throws SQLException {
//        // Delete database if it already exists
//        File file = new File("./auction_data.db");
//        if (file.exists()) file.delete();
        createDB();
        deleteTables();
        createTables();
    }

    public void deleteTables() throws SQLException {
        boolean prev = database.getAutoCommit();
        database.setAutoCommit(false);

        String sql = "DROP TABLE IF EXISTS impressions";
        executeUpdate(sql);

        sql = "DROP TABLE IF EXISTS users";
        executeUpdate(sql);

        sql = "DROP TABLE IF EXISTS clicks";
        executeUpdate(sql);

        sql = "DROP TABLE IF EXISTS servers";
        executeUpdate(sql);

        database.setAutoCommit(prev);
    }

//    public void closeConn() throws SQLException {
//        database.close();
//    }

    private void createDB() throws SQLException {

        fileinc++;
        String fn = fileinc + ".db";
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + fn);
        if (connection != null) {
            database = connection;
        }

    }

    private void createTables() throws SQLException {

        String sqlCreateUserTable =
                "CREATE TABLE IF NOT EXISTS users (\n"
                        + " id text PRIMARY KEY,\n"
                        + " gender text,\n"
                        + " age text,\n"
                        + " income text\n"
                        + ");";

        String sqlCreateImpressionTable =
                "CREATE TABLE IF NOT EXISTS impressions (\n"
                        + " integer PRIMARY KEY,\n"
                        + " entryDate integer,\n"
                        + " id text,\n"
                        + " context text,\n"
                        + " cost real,\n"
                        + " FOREIGN KEY(id) REFERENCES users(id)\n"
                        + ");";

        String sqlCreateServerTable =
                "CREATE TABLE IF NOT EXISTS servers (\n"
                        + " entryDate integer,\n"
                        + " id text,\n"
                        + " exitDate integer,\n"
                        + " pagesViewed integer,\n"
                        + " conversion text\n,"
                        + " PRIMARY KEY (entryDate, id),\n"
                        + " FOREIGN KEY(id) REFERENCES users(id)\n"
                        + ");";

        String sqlCreateClickTable =
                "CREATE TABLE IF NOT EXISTS clicks (\n"
                        + " entryDate integer,\n"
                        + " id text,\n"
                        + " cost real,\n"
                        + " PRIMARY KEY (entryDate, id),\n"
                        + " FOREIGN KEY(id) REFERENCES users(id)\n"
                        + ");";

        Statement createUserTable = database.createStatement();
        createUserTable.execute(sqlCreateUserTable);
        createUserTable.close();

        Statement createImpressionTable = database.createStatement();
        createImpressionTable.execute(sqlCreateImpressionTable);
        createImpressionTable.close();

        Statement createServerTable = database.createStatement();
        createServerTable.execute(sqlCreateServerTable);
        createServerTable.close();

        Statement createClickTable = database.createStatement();
        createClickTable.execute(sqlCreateClickTable);
        createClickTable.close();

    }

    public void loadImpressions(String filePath) throws IOException, SQLException {

        boolean firstLine = true;
        String DELIMITER = ",";

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        String sqlInsertImpression =
                "INSERT INTO impressions\n"
                        + "VALUES ("
                        + "null" + ","
                        + "CAST(strftime('%s', ?) as integer)" + ","
                        + "?" + ","
                        + "?" + ","
                        + "?"
                        + ");";

        String sqlInsertUser =
                "INSERT OR IGNORE INTO users VALUES (?,?,?,?);";

        PreparedStatement preparedImpressionStatement = database.prepareStatement(sqlInsertImpression);
        PreparedStatement preparedUserStatement = database.prepareStatement(sqlInsertUser);

        database.setAutoCommit(false);

        int i = 0;
        while ((line = br.readLine()) != null) {
            if (firstLine) firstLine = false;
            else {
                String[] values = line.split(DELIMITER);

                preparedUserStatement.setString(1, values[1]);
                preparedUserStatement.setString(2, values[2]);
                preparedUserStatement.setString(3, values[3]);
                preparedUserStatement.setString(4, values[4]);
                preparedUserStatement.addBatch();

                preparedImpressionStatement.setString(1, values[0]);
                preparedImpressionStatement.setString(2, values[1]);
                preparedImpressionStatement.setString(3, values[5]);
                preparedImpressionStatement.setFloat(4, Float.parseFloat(values[6]));
                preparedImpressionStatement.addBatch();

                if (i % 10000 == 0) {
                    preparedUserStatement.executeBatch();
                    preparedImpressionStatement.executeBatch();
                    database.commit();
                }

            }

            i++;

        }

        preparedUserStatement.executeBatch();
        preparedImpressionStatement.executeBatch();
        database.commit();

        preparedUserStatement.close();
        preparedImpressionStatement.close();

    }

    public void loadClicks(String filePath) throws IOException, SQLException {

        boolean firstLine = true;
        String DELIMITER = ",";

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        String sql =
                "INSERT INTO clicks VALUES(CAST(strftime('%s', ?) as integer),?,?);";


        PreparedStatement preparedStatement = database.prepareStatement(sql);

        database.setAutoCommit(false);

        int i = 0;
        while ((line = br.readLine()) != null) {
            if (firstLine) firstLine = false;
            else {
                String[] values = line.split(DELIMITER);

                preparedStatement.setString(1, values[0]);
                preparedStatement.setString(2, values[1]);
                preparedStatement.setFloat(3, Float.parseFloat(values[2]));
                preparedStatement.addBatch();

                if (i % 10000 == 0) {
                    preparedStatement.executeBatch();
                    database.commit();
                }

            }

            i++;

        }

        preparedStatement.executeBatch();
        database.commit();

        preparedStatement.close();

    }

    public void loadServers(String filePath) throws IOException, SQLException {

        boolean firstLine = true;
        String DELIMITER = ",";

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        String sql =
                "INSERT INTO servers VALUES(CAST(strftime('%s', ?) as integer),?,CAST(strftime('%s', ?) as integer),?,?);";


        PreparedStatement preparedStatement = database.prepareStatement(sql);

        database.setAutoCommit(false);

        int i = 0;
        while ((line = br.readLine()) != null) {
            if (firstLine) firstLine = false;
            else {
                String[] values = line.split(DELIMITER);

                preparedStatement.setString(1, values[0]);
                preparedStatement.setString(2, values[1]);
                preparedStatement.setString(3, values[2]);
                preparedStatement.setInt(4, Integer.parseInt(values[3]));
                preparedStatement.setString(5, values[4]);
                preparedStatement.addBatch();

                if (i % 10000 == 0) {
                    preparedStatement.executeBatch();
                    database.commit();
                }

            }

            i++;
        }

        preparedStatement.executeBatch();
        database.commit();

        preparedStatement.close();

    }

    /**
     * Execute an arbitrary query on the database
     *
     * @param sql SQL Query
     * @return SQL ResultSet
     * @throws SQLException Query error
     */
    public ResultSet executeQuery(String sql) throws SQLException {

        Statement statement = database.createStatement();
        ResultSet result = statement.executeQuery(sql);
        database.commit();
//        statement.close();

        return result;

    }

    public void executeUpdate(String sql) throws SQLException {

        Statement statement = database.createStatement();
        statement.executeUpdate(sql);
        database.commit();
        statement.close();

    }


}
