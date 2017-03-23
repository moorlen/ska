package controllers;

import models.Abonement;
import models.FitnesRecord;
import models.User;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.LinkedHashMap;
import java.util.Map;

public class Application extends Controller {

    @Before
    static void addUser() {
        //Lang.change("ru");
        User user = connected();
        if (user != null) {
            renderArgs.put("user", user);
        }
    }

    static User connected() {
        if (renderArgs.get("user") != null) {
            return renderArgs.get("user", User.class);
        }
        String login = session.get("login");
        String password = session.get("password");
        if (login != null) {
            return User.connect(login, password);
        }
        return null;
    }

    public static void index() {
        User user = connected();
        if (user != null) {
            switch (user.type.charAt(0)) {
                case 'a':
                    Administrator.rooms();
                    break;
                case 'c':
                    Client.index();
                    break;
                case 't':
                    Trainer.index();
                    break;
                case 's':
                    Admin.index();
                    break;
            }
        }
        render();
    }

    public static void menu() {
        session.put("currentDate", "");
        session.put("currentMode", "");
        render("@Application.menu");
    }

    public static void korts() {
        session.put("currentDate", "");
        session.put("currentMode", "");
        render("@Application.korts");
    }

    public static void viewCalendar(String object) {
        User user = connected();
        String fromAdministrator = "false";
        if (user != null) {
            switch (user.type.charAt(0)) {
                case 'a':
                    fromAdministrator = "true";
                    break;
            }
        }
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
        session.put("choiceObject", object);
        Map<String, String> months = new LinkedHashMap<String, String>();
        months.put("universal", "Универсальный зал");
        months.put("handbool", "Гандбольная площадка");
        months.put("play1", "Игровая площадка. Корт 1");
        months.put("play2", "Игровая площадка. Корт 2");
        months.put("play3", "Игровая площадка. Корт 3");
        months.put("play4", "Игровая площадка. Корт 4");
        months.put("sauna", "Сауна");
        String choiceObject = months.get(object);
        render("@Application.viewCalendar", fromAdministrator, currentDate, currentMode, choiceObject);
    }

    public static void roomsOrIndex() {
        User user = connected();
        if (user != null) {
            switch (user.type.charAt(0)) {
                case 'a':
                    Administrator.rooms();
                    break;
            }
        }
        index();
    }

    public static void register() {
        Map<String, String> types = new LinkedHashMap<String, String>();
        types.put("administrator", "Администратор");
        types.put("client", "Клиент");
        types.put("trainer", "Тренер");
        render(types);
    }

    public static void saveUser(@Valid User user, String verifyPassword) {
        validation.required(verifyPassword);
        validation.equals(verifyPassword, user.password).message("Пароли не совпадают");
        if (validation.hasErrors()) {
            Map<String, String> types = new LinkedHashMap<String, String>();
            types.put("administrator", "Администратор");
            types.put("client", "Клиент");
            types.put("trainer", "Тренер");
            render("@register", user, verifyPassword, types);
        }
        user.create();
        session.put("login", user.login);
        session.put("password", user.password);
        flash.success("Welcome, " + user.firstName);
        switch (user.type.charAt(0)) {
            case 'a':
                Administrator.rooms();
                break;
            case 'c':
                Client.index();
                break;
            case 't':
                Trainer.index();
                break;
            case 's':
                Admin.index();
                break;
        }
    }

    public static void loginForm() {
        render();
    }

    public static void login(String login, String password) {
        User user = User.find("byLoginAndPassword", login, password).first();
        if (user != null) {
            session.put("login", user.login);
            session.put("password", user.password);
            flash.success("Welcome, " + user.firstName);
            switch (user.type.charAt(0)) {
                case 'a':
                    Administrator.rooms();
                    break;
                case 'c':
                    Client.index();
                    break;
                case 't':
                    Trainer.index();
                    break;
            }
        }
        // Oops
        session.put("login", login);
        flash.error("Неверный пароль или логин");
        loginForm();
    }

    public static void logout() {
        session.clear();
        index();
    }

    public static void changePassword() {
        render();
    }

    public static void updatePassword(String oldPassword, String newPassword, String verifyPassword) {
        validation.required(oldPassword);
        validation.required(newPassword);
        validation.required(verifyPassword);
        User user = connected();
        validation.equals(oldPassword, user.password).message("Неверный пароль");
        validation.equals(verifyPassword, newPassword).message("Пароли не совпадают");
        if (validation.hasErrors()) {
            Map<String, String> types = new LinkedHashMap<String, String>();
            types.put("administrator", "Администратор");
            types.put("client", "Клиент");
            types.put("trainer", "Тренер");
            render("@changePassword", oldPassword, newPassword, verifyPassword);
        }

        user.password = newPassword;
        session.put("password", user.password);
        user.save();
        index();
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

    public static void deleteEvent(String id) {
        FitnesRecord record = FitnesRecord.findById(new Long(id));
        if (record.abonementNumber != null) {
            Abonement byNumber = Abonement.find("byNumber", record.abonementNumber).first();
            byNumber.ostatok++;
            byNumber.save();
        }
        record.delete();
        User user = connected();
        if (user != null) {
            session.put("login", user.login);
            session.put("password", user.password);
            switch (user.type.charAt(0)) {
                case 'a':
                    Administrator.viewClient(Long.valueOf(session.get("clientId")));
                    break;
                case 't':
                    Trainer.viewClient(Long.valueOf(session.get("clientId")));
                    break;
            }
        }
        index();
    }

    public static void clients() {
        User user = connected();
        if (user != null) {
            switch (user.type.charAt(0)) {
                case 'a':
                    Administrator.clients();
                    break;
                case 't':
                    Trainer.index();
                    break;
            }
        }
    }
}