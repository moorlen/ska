package controllers;

import static controllers.Administrator.index;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.Abonement;
import models.FitnesRecord;
import models.User;
import play.data.validation.Valid;
import play.modules.paginate.ValuePaginator;
import play.mvc.Controller;
import utils.UserComporator;

public class Trainer extends Application {
    public static void index() {
        session.put("currentDate", "");
        session.put("currentMode", "");
        session.put("calledController", "Trainer");
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

    public static void saveEvent(String startDate, String endDate, String text, String id, String price, String currentDate, String currentMode, String abonement) {
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
        record.save();
        session.put("currentDate", currentDate);
        session.put("currentMode", currentMode);
        viewClient(Long.valueOf(session.get("clientId")));
    }

    public static void deleteEvent(String id) {
        FitnesRecord record = FitnesRecord.findById(new Long(id));
        record.delete();
        viewClient(Long.valueOf(session.get("clientId")));
    }

    public static void newClient() {
        render();
    }

    public static void saveClient(@Valid User client, String verifyPassword) {
        validation.required(verifyPassword);
        validation.equals(verifyPassword, client.password).message("Пароли не совпадают");
        if (validation.hasErrors()) {
            render("@newClient", client, verifyPassword);
        }
        client.type = "client";
        client.create();
        index();
    }
}