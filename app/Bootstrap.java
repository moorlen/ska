import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/**
 * @author Moorlen
 */

@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        User user = User.find("byType", "superadmin").first();
        if (user == null) {
            Fixtures.loadModels("data.yml");
        }
    }
}