package Tanker;

//import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class TankConsumer {

    private final static String SERVER = "10.50.15.52:9092";
    private final static String TOPIC = "tankerkoenig";
    public final KafkaConsumer<String, String> consumer;
    public final int partition;

    public TankConsumer(int partition){
        this.partition = partition;
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", SERVER);
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer = new KafkaConsumer<>(props);
    }

    public void subscribe(){
        //consumer.assign(Arrays.asList(new TopicPartition(TOPIC, partition)));
        consumer.subscribe(Arrays.asList(TOPIC));
    }

    public ConsumerRecords<String, String> poll(){
        //ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        //consumer.commitSync();
        //return records;
        return consumer.poll(Duration.ofMillis(100));
    }

    public void close(){
        consumer.close();
    }

    public void wakeup() {
        consumer.wakeup();
    }
}