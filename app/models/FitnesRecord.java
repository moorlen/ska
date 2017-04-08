package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "FitnesRecord")
public class FitnesRecord extends Model {

    @Required
    public String startDate;

    @Required
    public String endDate;

    @Required
    public String text;

    @Required
    public String who;

    @Column(precision = 6, scale = 2)
    public Float price;

    public String type;

    public String abonementNumber;

    public Date createDate;

    @Required
    public int deleteTime;
}
