package controllers;

import models.*;
import play.modules.paginate.ValuePaginator;
import services.UserPool;
import utils.UserComporator;

import java.util.*;


public class Administrator extends Application {
    public static void deleteEvent(String id) {
        FitnesRecord record = FitnesRecord.findById(new Long(id));
        record.delete();
        UserPool.get().say(connected().login, id);
        index();
    }

    public static void rooms() {
        UserPool.get().join(connected().login);
        session.put("currentDate", "");
        session.put("currentMode", "");
        render();
    }

    public static void clients() {
        session.put("currentDate", "");
        session.put("currentMode", "");
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

    public static void aboutClient(Long clientId) {
        User client = User.findById(clientId);
        render(client);
    }

    public static void saveEvent(String startDate, String endDate, String text, String id, String price, String currentDate, String currentMode, String abonement, String kort, String deleteTime) {
        FitnesRecord record = FitnesRecord.findById(new Long(id));
        if (record == null) {
            record = new FitnesRecord();
        }
        record.startDate = startDate;
        record.endDate = endDate;
        record.text = text;
        String clientLogin = session.get("clientLogin");
        String choiceObject = session.get("choiceObject");
        if (clientLogin == null) {
            record.who = choiceObject;
        } else {
            record.who = clientLogin;
        }
        if (!("".equals(price) || "null".equals(price))) {
            record.price = new Float(price);
        }
        if (!("".equals(abonement)) && (abonement != null)) {
            Abonement byNumber = Abonement.find("byNumber", abonement).first();
            byNumber.ostatok--;
            byNumber.save();
        }
        record.abonementNumber = abonement;
        record.type = kort;

        record.createDate = new Date();
        if ("".equals(deleteTime) || (deleteTime == null)) {
            record.deleteTime = 24;
        } else {
            record.deleteTime = new Integer(deleteTime);
        }
        record.save();
        UserPool.get().say(connected().login, id);
        session.put("currentDate", currentDate);
        session.put("currentMode", currentMode);
        if (clientLogin == null) {
            viewCalendar(choiceObject);
        } else {
            viewClient(Long.valueOf(session.get("clientId")));
        }
    }
}
