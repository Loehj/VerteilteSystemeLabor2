
public class main {

    public static void main(String[] args){
        WeatherConsumer weatherConsumer = new WeatherConsumer();
        try {
            weatherConsumer.runConsumer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
