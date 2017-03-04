package utils;

import models.User;

import java.util.Comparator;

/**
 *
 * @author moorlen
 */
public class LoginComporator implements Comparator<User> {

    @Override
    public int compare(User o1, User o2) {
        return o1.login.compareTo(o2.login);
    }
}