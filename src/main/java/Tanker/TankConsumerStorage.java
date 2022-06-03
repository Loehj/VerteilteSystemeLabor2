package Tanker;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Queue;

public class TankConsumerStorage {
    public int getPLZ() {
        return plzArea;
    }

    public enum priceType{
        diesel,
        e5,
        e10
    }
    private Queue<TankerKoenigData> storage;
    private final int plzArea; //ex plzArea = 0 --> Plz with leading 0
    public TankConsumerStorage(int plzArea){
        this.plzArea = plzArea;
        storage = new ArrayDeque<>();
    }

    public void parseData(String value){
        JSONObject object = new JSONObject(value);
        if (object.has("postCode") && !object.isNull("postCode")){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
            LocalDateTime date = LocalDateTime.from(formatter.parse(object.getString("date")));
            storage.add(new TankerKoenigData(date, object.getString("station"), object.getString("postCode"), object.getFloat("pDiesel"), object.getFloat("pE5"), object.getFloat("pE10")));
        }
    }

    public void outPutData(){
        String prefix = String.valueOf(plzArea);
        int count = 0;
        for (TankerKoenigData data:storage) {
            if(data.postcode().startsWith(prefix)){
                count++;
            }
        }

        System.out.println("Output Storage " + plzArea + "\n" +
                "count: " + storage.size() + "count with plz: " + count);

    }

    public TankerStatData getAveragePerTimeArea(LocalDateTime startingDate, LocalDateTime endingDate){
        double[] totals = new double[3];
        int count = 0;

        boolean timeCondition;

        for (TankerKoenigData data:storage) {
            timeCondition = data.date().isBefore(endingDate) && (data.date().isAfter(startingDate) || data.date().isEqual(startingDate));

            if (timeCondition){
                totals[0] += data.pDiesel();
                totals[1] += data.pE5();
                totals[2] += data.pE10();
                count++;
            }
        }

        if (count == 0){
            return null;
        }

        return new TankerStatData(totals[0] / count, totals[1] / count, totals[2] / count, startingDate, endingDate);
    }
}
