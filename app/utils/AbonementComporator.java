package utils;

import java.util.Comparator;
import models.Abonement;

/**
 *
 * @author moorlen
 */
public class AbonementComporator implements Comparator<Abonement> {

    @Override
    public int compare(Abonement o1, Abonement o2) {
        return o1.startDate.compareTo(o2.startDate);
    }
}
