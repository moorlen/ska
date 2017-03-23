package controllers;

import java.util.Collections;
import java.util.List;

import models.Abonement;
import models.FitnesRecord;
import models.User;
import play.modules.paginate.ValuePaginator;
import utils.UserComporator;

public class Trainer extends Application {
    public static void index() {
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

    public static void saveEvent(String startDate, String endDate, String text, String id, String price, String currentDate, String currentMode, String abonement) {
        FitnesRecord record = FitnesRecord.findById(new Long(id));
        String oldAbonement = null;
        if (record == null) {
            record = new FitnesRecord();
        } else {
            oldAbonement = record.abonementNumber;
        }
        record.startDate = startDate;
        record.endDate = endDate;
        record.text = text;
        record.who = session.get("clientLogin");
        if (!("".equals(price) || "null".equals(price))) {
            record.price = new Float(price);
        }
        if ((abonement != null) && (!"".equals(abonement))) {
            Abonement byNumber = Abonement.find("byNumber", abonement).first();
            byNumber.ostatok--;
            byNumber.save();
        }
        if ((oldAbonement != null) && (!"".equals(oldAbonement))) {
            Abonement byNumber = Abonement.find("byNumber", oldAbonement).first();
            byNumber.ostatok++;
            byNumber.save();
        }
        record.abonementNumber = abonement;
        record.save();
        session.put("currentDate", currentDate);
        session.put("currentMode", currentMode);
        viewClient(Long.valueOf(session.get("clientId")));
    }
}