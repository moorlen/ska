package dto;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author moorlen
 */
public class RecordToExcel {
    public int number;
    public Map<String, String> timeMap = new LinkedHashMap<String, String>();

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Map<String, String> getTimeMap() {
        return timeMap;
    }

    public void setTimeMap(Map<String, String> timeMap) {
        this.timeMap = timeMap;
    }
}
