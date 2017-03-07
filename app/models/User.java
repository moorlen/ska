package models;

import play.data.validation.Email;
import play.data.validation.Password;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;
import play.i18n.Messages;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public class User extends Model {
    static final String uniqueMessage = "validation.user.login.unique";
    static final String emailMessage = "validation.user.email";

    @Email(message = emailMessage)
    public String email;

    @Required
    @Password
    public String password;

    @Required
    public String firstName;

    @Required
    public String lastName;

    @Required
    public String thirdName;

    @Required
    @Unique(message = uniqueMessage)
    public String login;

    public String type;

    public Date birthDate;

    @OneToMany
    List<Abonement> abonements;

    public static User connect(String login, String password) {
        return find("byLoginAndPassword", login, password).first();
    }
}
