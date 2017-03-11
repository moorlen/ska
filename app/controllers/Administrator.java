package controllers;

import models.*;
import play.modules.paginate.ValuePaginator;
import utils.UserComporator;

import java.util.*;


public class Administrator extends Application {
    public static void index() {
        User user = Application.connected();
        String currentDate = session.get("currentDate");
        String currentMode = session.get("currentMode");
        session.put("calledController", "Administrator");
        if (currentDate == null || "".equals(currentDate)) {
            currentDate = "new Date()";
        } else {
            String[] split = currentDate.substring(0, 10).split("-");
            int i = Integer.parseInt(split[1]) - 1;
            currentDate = "new Date(" + split[2] + "," + i + "," + split[0] + ")";
        }
        if (currentMode == null || "".equals(currentMode)) {
            currentMode = "week";
        }
        Map<String, String> months = new LinkedHashMap<String, String>();
        months.put("universal", "Универсальный зал");
        months.put("handbool", "Гандбольная площадка");
        months.put("play1", "Игровая площадка. Корт 1");
        months.put("play2", "Игровая площадка. Корт 2");
        months.put("play3", "Игровая площадка. Корт 3");
        months.put("play4", "Игровая площадка. Корт 4");
        months.put("sauna", "Сауна");
        String choiceObject = months.get(session.get("choiceObject"));
        render(currentDate, currentMode, choiceObject);
    }

    public static void saveEvent(String startDate, String endDate, String text, String id, String price, String currentDate, String currentMode) {
        FitnesRecord record = FitnesRecord.findById(new Long(id));
        String currentAbonement;
        if (record == null) {
            record = new FitnesRecord();
        } else {
            currentAbonement = record.abonementNumber;
        }
        record.startDate = startDate;
        record.endDate = endDate;
        record.text = text;
        record.who = session.get("choiceObject");
        if (!("".equals(price) || "null".equals(price))) {
            record.price = new Float(price);
        }
        record.save();
        session.put("currentDate", currentDate);
        session.put("currentMode", currentMode);
        index();
    }

    public static void deleteEvent(String id) {
        FitnesRecord record = FitnesRecord.findById(new Long(id));
        record.delete();
        index();
    }

    public static void korts() {
        session.put("currentDate", "");
        session.put("currentMode", "");
        render();
    }

    public static void rooms() {
        session.put("currentDate", "");
        session.put("currentMode", "");
        render();
    }

    public static void menu() {
        session.put("currentDate", "");
        session.put("currentMode", "");
        render();
    }

    public static void clients() {
        session.put("currentDate", "");
        session.put("currentMode", "");
        session.put("calledController", "Administrator");
        List<User> users = User.find("byType", "client").fetch();
        Collections.sort(users, new UserComporator());
        ValuePaginator allUser = new ValuePaginator(users);
        allUser.setPageSize(10);
        render(allUser);
    }

    public static void viewClient(Long clientId) {
        User client = User.findById(clientId);
        String currentDate = session.get("currentDate");
        String currentMode = session.get("currentMode");
        if (currentDate == null || "".equals(currentDate)) {
            currentDate = "new Date()";
        } else {
            String[] split = currentDate.substring(0, 10).split("-");
            int i = Integer.parseInt(split[1]) - 1;
            currentDate = "new Date(" + split[2] + "," + i + "," + split[0] + ")";
        }
        if (currentMode == null || "".equals(currentMode)) {
            currentMode = "week";
        }
        String choiceObject = client.login;
        session.put("clientLogin", client.login);
        session.put("clientId", client.id);
        render(currentDate, currentMode, choiceObject, client);
    }

    public static void saveEvent(String startDate, String endDate, String text, String id, String price, String currentDate, String currentMode, String abonement, String kort) {
        FitnesRecord record = FitnesRecord.findById(new Long(id));
        if (record == null) {
            record = new FitnesRecord();
        }
        record.startDate = startDate;
        record.endDate = endDate;
        record.text = text;
        record.who = session.get("clientLogin");
        if (!("".equals(price) || "null".equals(price))) {
            record.price = new Float(price);
        }
        if (abonement != null) {
            Abonement byNumber = Abonement.find("byNumber", abonement).first();
            byNumber.ostatok--;
            byNumber.save();
        }
        record.abonementNumber = abonement;
        record.type = kort;
        record.save();
        session.put("currentDate", currentDate);
        session.put("currentMode", currentMode);
        viewClient(Long.valueOf(session.get("clientId")));
    }

    public static void choiceObject(String object) {
        session.put("choiceObject", object);
        index();
    }
}
