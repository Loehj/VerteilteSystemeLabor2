package Tanker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainTanker {

    private static final int PARTITION_COUNT = 10;

    public static void main(String... args) throws IOException {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(PARTITION_COUNT);
        ArrayList<TankConsumerRunnable> tankConsumerRunnables = new ArrayList<>();
        for (int i = 0; i < PARTITION_COUNT; i++){
            TankConsumerRunnable runner = new TankConsumerRunnable(i);
            tankConsumerRunnables.add(runner);
            threadPoolExecutor.execute(runner);
        }
        threadPoolExecutor.shutdown();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        while((line = bufferedReader.readLine()) != null){
            if(line.equals("exit") || line.equals("s")){
                for(TankConsumerRunnable runner : tankConsumerRunnables){
                    runner.shutdown();
                    runner.print();
                }
                System.exit(0);
            }
        }
    }
}