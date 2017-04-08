package controllers;

import java.util.Collections;

import play.data.validation.Valid;
import models.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import play.modules.paginate.ValuePaginator;
import services.UserPool;
import utils.LoginComporator;

public class Admin extends Application {
    public static void index() {
        UserPool.get().join(connected().login);
        List<User> users = User.find("select distinct u from User u  where not u.type ='superadmin'").fetch();
        Collections.sort(users, new LoginComporator());
        ValuePaginator allUser = new ValuePaginator(users);
        allUser.setPageSize(10);
        render(allUser);
    }

    public static void newUser() {
        Map<String, String> types = new LinkedHashMap<String, String>();
        types.put("administrator", "Администратор");
        types.put("client", "Клиент");
        types.put("trainer", "Тренер");
        render(types);
    }

    public static void saveUser(@Valid User client, String verifyPassword) {
        validation.required(verifyPassword);
        validation.equals(verifyPassword, client.password).message("Пароли не совпадают");
        if (validation.hasErrors()) {
            Map<String, String> types = new LinkedHashMap<String, String>();
            types.put("administrator", "Администратор");
            types.put("client", "Клиент");
            types.put("trainer", "Тренер");
            render("@newUser", client, verifyPassword, types);
        }
        client.create();
        index();
    }

    public static void deleteUser(Long clientId) {
        User user = User.findById(clientId);
        List<FitnesRecord> fitnesRecords = FitnesRecord.find("byWho", user.login).fetch();
        for (FitnesRecord record : fitnesRecords) {
            record.delete();
        }
        List<Abonement> abonements = Abonement.find("byClient_Id", user.id).fetch();
        for (Abonement record : abonements) {
            record.delete();
        }
        user.delete();
        index();
    }
}

