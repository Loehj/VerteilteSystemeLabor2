package Tanker;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.util.concurrent.atomic.AtomicBoolean;

public class TankConsumerRunner implements Runnable{

    private final TankConsumer consumer;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public TankConsumerRunner(int i){
        consumer = new TankConsumer(i);
    }

    @Override
    public void run() {
        try{
            consumer.subscribe();
            while (!closed.get()) {
                ConsumerRecords<String, String> records = consumer.poll();
                for(ConsumerRecord<String, String> record : records){
                    System.out.println(record.value());
                }
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) throw e;
        } finally {
            consumer.close();
        }
    }

    public void shutdown(){
        closed.set(true);
        consumer.wakeup();
    }
}
