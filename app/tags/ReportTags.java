package tags;

import groovy.lang.Closure;
import models.FitnesRecord;
import models.User;
import play.db.jpa.JPABase;
import play.templates.FastTags;
import play.templates.GroovyTemplate;

import java.io.PrintWriter;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class ReportTags extends FastTags {
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/fitnes?characterEncoding=UTF-8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static Connection getDBConnection() {

        Connection dbConnection = null;

        try {

            Class.forName(DB_DRIVER);

        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());

        }

        try {

            dbConnection = DriverManager.getConnection(
                    DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return dbConnection;

    }

    private static void executeSQL(String query) {
        Connection dbConnection = null;
        Statement statement = null;
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            System.out.println(query);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static ResultSet selectQuery(String query) {
        Connection dbConnection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            System.out.println(query);
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultSet;
    }

    public static void _schedulerParse(Map<?, ?> args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {
        Connection dbConnection = null;
        Statement statement = null;
        String id = args.get("objectId").toString();
        if (id != null) {
            try {
                dbConnection = getDBConnection();
                statement = dbConnection.createStatement();


                // execute select SQL stetement
                ResultSet rs = statement.executeQuery("SELECT id, startDate, endDate, text, price, abonementNumber, type  FROM FitnesRecord WHERE who = '" + id + "' OR type ='" + id + "'");
                StringBuilder html = new StringBuilder();
                html.append("scheduler.parse([");
                while (rs.next()) {
                    Float price = rs.getFloat("price");
                    String abonementNumber = rs.getString("abonementNumber");
                    html.append("{");
                    html.append("id: \"" + rs.getString("id") + "\", ");
                    html.append("start_date: \"" + rs.getString("startDate") + "\", ");
                    html.append("end_date: \"" + rs.getString("endDate") + "\", ");
                    html.append("text: \"" + rs.getString("text") + "\", ");
                    html.append("combo_select_abonement: \"" + abonementNumber + "\", ");
                    html.append("combo_select_kort: \"" + rs.getString("type") + "\", ");
                    html.append("details: \"" + price + "\"");
                    if ((price == null || price == 0) && (abonementNumber == "")) {
                        html.append(",color: \"red\"");
                    }
                    html.append("}");
                    if (!rs.isLast()) {
                        html.append(",");
                    }
                }
                html.append("], \"json\");");
                out.print(html);
            } catch (Exception e) {
            } finally {

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (dbConnection != null) {
                    try {
                        dbConnection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void _abonementsParse(Map<?, ?> args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {
        Connection dbConnection = null;
        Statement statement = null;
        String id = args.get("objectId").toString();
        String type = args.get("type").toString();
        if (id != null) {
            try {
                dbConnection = getDBConnection();
                statement = dbConnection.createStatement();


                // execute select SQL stetement
                ResultSet rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = '" + type + "' AND ostatok != 0 " +
                        "AND( Month(startDate) = Month(NOW()) AND Month(endDate) = Month(NOW()) AND YEAR(startDate) = YEAR(NOW()) AND YEAR(endDate) = YEAR(NOW()))");
                StringBuilder html = new StringBuilder();
                html.append("var abonements = [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    if (!rs.isLast()) {
                        html.append(",");
                    }
                }
                html.append("];");
                out.print(html);
            } catch (Exception e) {
            } finally {

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (dbConnection != null) {
                    try {
                        dbConnection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
