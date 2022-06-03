public class WeatherConsumerGraphite {
    private final static String TOPIC = "weather";
    private final static String SERVER = "10.50.15.52:9092";
    private final static String CLIENT_ID = "Group8";
    private static Consumer<Long, String> consumer;

    public WeatherConsumer() {

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,SERVER);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,CLIENT_ID);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<>(props);

    }

    public void sendWeather(){
        Graphite graphite = new Graphite("10.50.15.52", 2003);

        graphite.connect();
        if (graphite.isConnected()) {
            try {
                consumer.subscribe(Collections.singletonList(TOPIC));
                while (true) {
                    val records = consumer.poll(Duration.of(60, ChronoUnit.SECONDS));
                    if (records.isEmpty()) {
                        Thread.sleep(10_000);
                    }
                    records.forEach(rec -> {
                        try {
                            //graphite.send("inf19b_group8_." + rec.value().getCity().toLowerCase() + "_max", rec.value().getTempMax() + "", rec.timestamp() / 1000);
                            //graphite.send("inf19b_group8_." + rec.value().getCity().toLowerCase() + "_min", rec.value().getTempMin() + "", rec.timestamp() / 1000);
                            graphite.send("inf19b_group8_." + rec.value().getCity().toLowerCase() + "_currentTemp_", rec.value().getTempCurrent() + "", rec.timestamp() / 1000);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
        graphite.close();
    }

}
