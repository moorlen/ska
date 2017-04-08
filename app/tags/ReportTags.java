package tags;

import groovy.lang.Closure;
import play.templates.FastTags;
import play.templates.GroovyTemplate;
import services.DataBaseService;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportTags extends FastTags {
    private static DataBaseService dataBaseService = new DataBaseService();

    public static void _schedulerParse(Map<?, ?> args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {
        Connection dbConnection = null;
        Statement statement = null;
        String id = args.get("objectId").toString();
        Object userId = args.get("userId");
        List<String> objects = new ArrayList<String>();
        objects.add("universal");
        objects.add("handbool");
        objects.add("play1");
        objects.add("play2");
        objects.add("play3");
        objects.add("play4");
        objects.add("sauna");
        objects.add("fitnes");
        if (id != null) {
            try {
                dbConnection = dataBaseService.getDBConnection();
                statement = dbConnection.createStatement();
                // execute select SQL stetement
                ResultSet rs = statement.executeQuery("SELECT id, startDate, endDate, text, price, abonementNumber, type, deleteTime, who  FROM FitnesRecord WHERE who = '" + id + "' OR type ='" + id + "'");
                StringBuilder html = new StringBuilder();
                html.append("scheduler.parse([");
                while (rs.next()) {
                    Float price = rs.getFloat("price");
                    String abonementNumber = rs.getString("abonementNumber");
                    html.append("{");
                    html.append("id: \"" + rs.getString("id") + "\", ");
                    html.append("start_date: \"" + rs.getString("startDate") + "\", ");
                    html.append("end_date: \"" + rs.getString("endDate") + "\", ");
                    html.append("combo_select_abonement: \"" + abonementNumber + "\", ");
                    html.append("combo_select_kort: \"" + rs.getString("type") + "\", ");
                    String text = rs.getString("text");
                    text = text.replace("\"", "'");
                    if (userId != null) {
                        if (("".equals(abonementNumber)) || (abonementNumber == null)) {
                            html.append("text: \"" + text + "\", ");
                        } else {
                            html.append("text: \"" + text + ". Оплачено по абонименту " + abonementNumber + "\", ");
                        }
                    } else {
                        html.append("text: \"" + text + "\", ");
                    }
                    html.append("combo_select_abonement: \"" + abonementNumber + "\", ");
                    html.append("combo_select_targets: \"" + rs.getString("type") + "\", ");
                    html.append("deleteTime: \"" + rs.getString("deleteTime") + "\", ");
                    if (!objects.contains(rs.getString("who"))) {
                        html.append("readonly: true, ");
                    }
                    html.append("details: \"" + price + "\"");
                    if ((price == null || price == 0) && (("".equals(abonementNumber)) || (abonementNumber == null))) {
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
        if (id != null) {
            try {
                dbConnection = dataBaseService.getDBConnection();
                statement = dbConnection.createStatement();


                // execute select SQL stetement
                StringBuilder html = new StringBuilder();
                html.append("var abonements = {");
                ResultSet rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'universal' AND ostatok != 0 " +
                        "AND(NOW() BETWEEN startDate AND endDate)");
                html.append("universal: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'handbool' AND ostatok != 0 " +
                        "AND(NOW() BETWEEN startDate AND endDate)");
                html.append("handbool: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'sauna' AND ostatok != 0 " +
                        "AND(NOW() BETWEEN startDate AND endDate)");
                html.append("sauna: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'fitnes' AND ostatok != 0 " +
                        "AND(NOW() BETWEEN startDate AND endDate)");
                html.append("fitnes: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'kort' AND ostatok != 0 " +
                        "AND(NOW() BETWEEN startDate AND endDate)");
                html.append("play1: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'kort' AND ostatok != 0 " +
                        "AND(NOW() BETWEEN startDate AND endDate)");
                html.append("play2: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'kort' AND ostatok != 0 " +
                        "AND(NOW() BETWEEN startDate AND endDate)");
                html.append("play3: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'kort' AND ostatok != 0 " +
                        "AND(NOW() BETWEEN startDate AND endDate)");
                html.append("play4: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("]");
                html.append("};");
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

    public static void _abonementParse(Map<?, ?> args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {
        Connection dbConnection = null;
        Statement statement = null;
        String id = args.get("objectId").toString();
        if (id != null) {
            try {
                dbConnection = dataBaseService.getDBConnection();
                statement = dbConnection.createStatement();


                // execute select SQL stetement
                StringBuilder html = new StringBuilder();
                html.append("var abonements = {");
                ResultSet rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'universal' AND ostatok != 0 " +
                        "AND( Month(startDate) = Month(NOW()) AND Month(endDate) = Month(NOW()) AND YEAR(startDate) = YEAR(NOW()) AND YEAR(endDate) = YEAR(NOW()))");
                html.append("universal: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'handbool' AND ostatok != 0 " +
                        "AND( Month(startDate) = Month(NOW()) AND Month(endDate) = Month(NOW()) AND YEAR(startDate) = YEAR(NOW()) AND YEAR(endDate) = YEAR(NOW()))");
                html.append("handbool: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'sauna' AND ostatok != 0 " +
                        "AND( Month(startDate) = Month(NOW()) AND Month(endDate) = Month(NOW()) AND YEAR(startDate) = YEAR(NOW()) AND YEAR(endDate) = YEAR(NOW()))");
                html.append("sauna: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'fitnes' AND ostatok != 0 " +
                        "AND( Month(startDate) = Month(NOW()) AND Month(endDate) = Month(NOW()) AND YEAR(startDate) = YEAR(NOW()) AND YEAR(endDate) = YEAR(NOW()))");
                html.append("fitnes: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'kort' AND ostatok != 0 " +
                        "AND( Month(startDate) = Month(NOW()) AND Month(endDate) = Month(NOW()) AND YEAR(startDate) = YEAR(NOW()) AND YEAR(endDate) = YEAR(NOW()))");
                html.append("play1: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'kort' AND ostatok != 0 " +
                        "AND( Month(startDate) = Month(NOW()) AND Month(endDate) = Month(NOW()) AND YEAR(startDate) = YEAR(NOW()) AND YEAR(endDate) = YEAR(NOW()))");
                html.append("play2: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'kort' AND ostatok != 0 " +
                        "AND( Month(startDate) = Month(NOW()) AND Month(endDate) = Month(NOW()) AND YEAR(startDate) = YEAR(NOW()) AND YEAR(endDate) = YEAR(NOW()))");
                html.append("play3: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("],");
                rs = statement.executeQuery("SELECT number FROM Abonement " +
                        "WHERE client_id = '" + id + "' AND target = 'kort' AND ostatok != 0 " +
                        "AND( Month(startDate) = Month(NOW()) AND Month(endDate) = Month(NOW()) AND YEAR(startDate) = YEAR(NOW()) AND YEAR(endDate) = YEAR(NOW()))");
                html.append("play4: [");
                while (rs.next()) {
                    html.append("{");
                    html.append("key: \"" + rs.getString("number") + "\", ");
                    html.append("label: \"" + rs.getString("number") + "\"");
                    html.append("}");
                    html.append(",");
                }
                html.append("{");
                html.append("key: \"null\", ");
                html.append("label: \"\"");
                html.append("}");
                html.append("]");
                html.append("};");
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
