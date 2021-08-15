package scripts.api.util.numbers;

import scripts.api.Core;

public class Hourly {

    public static String getHourly(int total){

        return " (" + (Math.round(total/((System.currentTimeMillis() - Core.getStartTime())/3600000.0D))) + ")";

    }

}
