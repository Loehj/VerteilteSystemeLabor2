package Tanker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;

public class TankerStat {
    private Queue<TankerStatData> statData;

    private final TankConsumerStorage storage;


    public TankerStat(TankConsumerStorage storage){
        this.storage = storage;
        statData = new ArrayDeque<>();
    }

    public void printStats(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH");
        LocalDateTime startingDate = LocalDateTime.from(formatter.parse("2021-12-15T00:00:00.000+00:00"));
        LocalDateTime endDate = startingDate.plusHours(1);

        double currentAverage = 0;
        LocalDateTime stopDate = LocalDateTime.from(formatter.parse("2022-07-01T00:00:00.000+00:00"));
        while(startingDate.isBefore(stopDate)){
            TankerStatData data = storage.getAveragePerTimeArea(startingDate, endDate);
            if(data != null){
                statData.add(data);
            }
            startingDate = endDate;
            endDate = startingDate.plusHours(1);
        }

        int plz = storage.getPLZ();

        for (TankerStatData data:statData) {
            System.out.println("Averages in routingNu starting with " + plz + ":" + data.startingTime().format(formatter2) + " - " + data.averageDiesel() +";"+ data.averageE5()+";"+ data.averageE10());
        }
    }
}
