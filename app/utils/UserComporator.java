package utils;

import java.util.Comparator;
import models.User;

/**
 *
 * @author moorlen
 */
public class UserComporator implements Comparator<User> {

    @Override
    public int compare(User o1, User o2) {
        return o1.firstName.compareTo(o2.firstName);
    }
}