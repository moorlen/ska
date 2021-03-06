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

    public static void register() {
        Map<String, String> types = new LinkedHashMap<String, String>();
        types.put("administrator", "Администратор");
        types.put("client", "Клиент");
        types.put("trainer", "Тренер");
        render();
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
        flash.error("Неверный пароль");
        index();
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
}