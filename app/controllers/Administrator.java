package controllers;

import models.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


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
        if (record == null) {
            record = new FitnesRecord();
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

    public static void menu() {
        session.put("currentDate", "");
        session.put("currentMode", "");
        render();
    }

    public static void choiceObject(String object) {
        session.put("choiceObject", object);
        index();
    }
}
