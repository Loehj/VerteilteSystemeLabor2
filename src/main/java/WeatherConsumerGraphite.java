import org.apache.kafka.clients.consumer.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Properties;

import java.io.IOException;

import com.codahale.metrics.graphite.Graphite;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class WeatherConsumerGraphite {
    private final static String TOPIC = "weather";
    private final static String SERVER = "10.50.15.52:9092";
    private final static String CLIENT_ID = "Group8";
    private static Consumer<Long, String> consumer;

    public WeatherConsumerGraphite() {

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

    public void sendWeather() throws Exception {
        Graphite graphite = new Graphite("10.50.15.52", 2003);

        graphite.connect();
        if (graphite.isConnected()) {
            try {
                consumer.subscribe(Collections.singletonList(TOPIC));
                while (true) {
                    ConsumerRecords<Long, String> records = consumer.poll(Duration.of(60, ChronoUnit.SECONDS));
                    if (records.isEmpty()) {
                        Thread.sleep(10_000);
                    }
                    records.forEach(record -> {
                        try {
                            graphite.send("inf19b_group8_." + record.key(), record.value() + "" + record.offset() + "" + record.partition(), record.timestamp());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e){
                throw new Exception("help" + e);
            }
        }
        graphite.close();
    }

}
