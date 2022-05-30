
public class main {

    public static void main(String[] args){
        WeatherConsumer weatherConsumer = new WeatherConsumer();
        KProducer producer = new KProducer();
        try {
            producer.runProducer(5);
            weatherConsumer.runConsumer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
