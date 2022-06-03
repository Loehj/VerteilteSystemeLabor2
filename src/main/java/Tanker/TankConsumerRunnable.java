package Tanker;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;

import java.util.concurrent.atomic.AtomicBoolean;

public class TankConsumerRunnable implements Runnable{

    private final TankConsumer consumer;
    private final TankerStat stats;
    private final TankConsumerStorage storage;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public TankConsumerRunnable(int i){
        consumer = new TankConsumer(i);
        storage = new TankConsumerStorage(i);
        stats = new TankerStat(storage);
    }

    @Override
    public void run() {
        try{
            consumer.subscribe();
            while (!closed.get()) {
                ConsumerRecords<String, String> records = consumer.poll();
//                int i = 0;
                for(ConsumerRecord<String, String> record : records){
//                    System.out.println(record.value());
                    storage.parseData(record.value());
//                    if(i % 1000 == 0){
//                    }
//                    i++;
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

    public void print(){
        stats.printStats();
    }
}
