package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

import java.util.Date;

/**
 * @author moorlen
 */
@Entity
public class Abonement extends Model {
    static final String mes = "validation.abonement.number.unique";

    @Required
    @Unique(message = mes)
    public String number;

    @Required
    public Date startDate;

    @Required
    public Date endDate;

    @Required
    @Column(precision = 6, scale = 2)
    public Float price;

    @Required
    public Long count;

    public Long ostatok;

    @ManyToOne
    public User client;

    @Required
    public String target;
}
