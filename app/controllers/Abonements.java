package controllers;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.Abonement;
import models.User;
import play.data.validation.Valid;
import play.modules.paginate.ValuePaginator;
import play.mvc.Controller;
import utils.AbonementComporator;

/**
 * @author moorlen
 */
public class Abonements extends Controller {

    private static SecureRandom random = new SecureRandom();

    public static void index(Long clientId) {
        User client = User.findById(clientId);
        List<Abonement> abonements = Abonement.find("byClient_Id", clientId).fetch();
        Collections.sort(abonements, new AbonementComporator());
        ValuePaginator allAbonemets = new ValuePaginator(abonements);
        allAbonemets.setPageSize(10);
        render(client, allAbonemets);
    }

    public static void newAbonement(Long clientId) {
        Map<String, String> targets = new LinkedHashMap<String, String>();
        targets.put("kort", "Игровой зал");
        targets.put("fitnes", "Тренажерный зал");
        String number = new BigInteger(20, random).toString(8);
        render(clientId, targets, number);
    }

    public static void saveAbonement(@Valid Abonement abonement, Long clientId) {
        if (validation.hasErrors()) {
            Map<String, String> targets = new LinkedHashMap<String, String>();
            targets.put("kort", "Игровой зал");
            targets.put("fitnes", "Тренажерный зал");
            render("@newAbonement", abonement, targets, clientId);
        }
        User client = User.findById(clientId);
        abonement.ostatok = abonement.count;
        abonement.client = client;
        abonement.create();
        index(clientId);
    }
}
