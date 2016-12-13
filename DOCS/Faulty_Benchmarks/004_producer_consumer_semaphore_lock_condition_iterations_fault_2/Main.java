/**
 * Concurrent Benchmarks
 * 
 * Title:  Producer-Consumer with Semaphore and Locks/Condition     
 * 
 * Description:  This benchmark implements the producer-consumer program
 *               using locks, conditions variables and semaphore. 
 * 
 * Paradigm:     Shared Memory               
 * 
 * Fault:        "A blocking critical section"
 *                Lines with fault: 19-21, setMultiplySharedValue() method - Buffer_Semaphore class
 *               
 * Year:         2015
 * Company:      ICMC/USP - São Carlos
 *               University of São Paulo (USP)
 *               Institute of Mathematics and Computer Science (ICMC)
 *               
 * @author       George Gabriel Mendes Dourado
 * @version      1.0
 */

/* TEST 1
 * java Main 4 0 2 0
 * 
 * OUTPUT:
 * writes semaphore = 3.0
 * reads semaphore = 3.0
 * writes semaphore = 4.0
 * reads semaphore = 4.0
 * writes semaphore = 5.0
 * reads semaphore = 5.0
 * writes semaphore = 6.0
 * reads semaphore = 6.0
 */

/* TEST 2
 * java Main 4 0 2 0
 * 
 * Reveal the fault. A Blocking critical section.
 */

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int numberOfThreads = 4; // total number of threads
        int operation = 0; // if 0 increments, if 1 multiply
        int numberOfIterations = 1; // amount of iterations that each thread make in the shared variable
        int path = 0; // if 0 -> use buffer with semaphore, if 1 -> use buffer with locks and conditions
        boolean flag = true;

        if (args.length == 4) {
            numberOfThreads = Integer.parseInt(args[0]);
            operation = Integer.parseInt(args[1]);
            numberOfIterations = Integer.parseInt(args[2]);
            path = Integer.parseInt(args[3]);
        } else {
            System.out.println("Wrong number of arguments.");
            System.out.println("Using default values.");
        }
        
        Buffer_Semaphore sharedObject1 = new Buffer_Semaphore();
        Buffer_With_Lock_Condition sharedObject2 = new Buffer_With_Lock_Condition();
        sharedObject2.acessLock = new ReentrantLock();
        sharedObject2.canWrite = sharedObject2.acessLock.newCondition();
        sharedObject2.canRead = sharedObject2.acessLock.newCondition();
        
        Semaphore tokenToProducer = new Semaphore(0);
        tokenToProducer.release();
        Semaphore tokenToConsumer = new Semaphore(0);
        
        Producer producer0 = new Producer();
        producer0.setSharedObject(operation, numberOfIterations, path);
        producer0.setBuffer_Semaphore(sharedObject1);
        producer0.setBuffer_With_Lock_Condition(sharedObject2);
        producer0.setSemaphoreTokenToConsumer(tokenToConsumer);
        producer0.setSemaphoreTokenToProducer(tokenToProducer);
        producer0.start();
                    
        Producer producer1 = new Producer();
        producer1.setSharedObject(operation, numberOfIterations, path);
        producer1.setBuffer_Semaphore(sharedObject1);
        producer1.setBuffer_With_Lock_Condition(sharedObject2);
        producer1.setSemaphoreTokenToConsumer(tokenToConsumer);
        producer1.setSemaphoreTokenToProducer(tokenToProducer);
        producer1.start();
        
        
        Consumer consumer0 = new Consumer();
        consumer0.setSharedObject(numberOfIterations, path);
        consumer0.setBuffer_Semaphore(sharedObject1);
        consumer0.setBuffer_With_Lock_Condition(sharedObject2);
        consumer0.setSemaphoreTokenToConsumer(tokenToConsumer);
        consumer0.setSemaphoreTokenToProducer(tokenToProducer);
        consumer0.start();
        
        Consumer consumer1 = new Consumer();
        consumer1.setSharedObject(numberOfIterations, path);
        consumer1.setBuffer_Semaphore(sharedObject1);
        consumer1.setBuffer_With_Lock_Condition(sharedObject2);
        consumer1.setSemaphoreTokenToConsumer(tokenToConsumer);
        consumer1.setSemaphoreTokenToProducer(tokenToProducer);
        consumer1.start();
        
        
        producer0.join();
        consumer0.join();
        
        producer1.join();
        consumer1.join();
    }
}