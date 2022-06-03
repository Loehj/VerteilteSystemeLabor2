import java.io.IOException;

public class main {

    public static void main(String[] args) {
        WeatherConsumer weatherConsumer = new WeatherConsumer();
        KProducer producer = new KProducer();
        WeatherConsumerGraphite weatherConsumerGraphite = new WeatherConsumerGraphite();
        try {
            //producer.runProducer(5);
            //weatherConsumer.runConsumer();
            weatherConsumerGraphite.sendWeather();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
