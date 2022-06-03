package Tanker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TankMain {

    private static final int azThread = 7;

    public static void main(String... args) throws IOException {
        ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newFixedThreadPool(azThread);
        ArrayList<TankConsumerRunner> list = new ArrayList<>();
        for (int i = 0; i < azThread; i++){
            TankConsumerRunner runner = new TankConsumerRunner(i);
            list.add(runner);
            tpe.execute(runner);
        }
        tpe.shutdown();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        while((line = bufferedReader.readLine()) != null){
            if(line.equals("exit")){
                for(TankConsumerRunner runner : list){
                    runner.shutdown();
                }
                System.exit(0);
            }
        }
    }
}