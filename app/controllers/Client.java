package controllers;

import models.User;
import play.mvc.Controller;

public class Client extends Application {
    public static void index() {
        render();
    }

    public static void viewCalendar() {
        User client = connected();
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
        render(currentDate, currentMode, client);
    }
}
