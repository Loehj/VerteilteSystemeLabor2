package Tanker;

import java.time.LocalDateTime;

public record TankerStatData(double averageDiesel, double averageE5, double averageE10, LocalDateTime startingTime, LocalDateTime endTime) {

}
