package jobs;

import play.jobs.Every;
import play.jobs.Job;
import services.DataBaseService;

import java.util.Date;

/**
 * @author moorlen
 */
@Every("10min")
public class CleanerJob extends Job {

    @Override
    public void doJob() throws Exception {
        Date date = new Date();
        System.out.println("Job is started" + date.toString());
        DataBaseService dataBaseService = new DataBaseService();
        dataBaseService.executeSQL("DELETE \n" +
                "FROM FitnesRecord\n" +
                "WHERE (TIMESTAMPDIFF(MINUTE,NOW(),CONCAT(SUBSTRING(startDate,7,4),'-',SUBSTRING(startDate,4,2),'-',SUBSTRING(startDate,1,2),' ',SUBSTRING(startDate,12))) - deleteTime*60 < 0) AND ((price is NULL) OR (price = 0)) AND (abonementNumber is NULL)");
        System.out.println("Job is stoped");
    }
}
