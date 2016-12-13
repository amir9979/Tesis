/**
 * Concurrent Benchmarks
 * 
 * Title:  all_to_all - with fault
 * 
 * Description:  This benchmarks has the objective of verify if testing 
 *               tools for concurrent programs are able to consider barriers. 
 *
 * Paradigm:     Shared Memory
 * 
 * Fault:       "Nonexistent wait"
 *               Line with fault: 114
 *               
 * Year:         2015
 * Company:      ICMC/USP - São Carlos
 *               University of São Paulo (USP)
 *               Institute of Mathematics and Computer Science (ICMC)
 *               
 * @authors      Raphael Negrisoli Batista
 *               George Gabriel Mendes Dourado
 * @version      1.0
 */

/* TEST 1
 * java Main
 * 
 * A POSSIBLE WRONG OUTPUT (the order of threads can change):
 * Thread-2 waiting at barrier 1
 * Thread-1 waiting at barrier 1
 * Thread-0 waiting at barrier 1
 * Thread-3 waiting at barrier 2
 * 
 * THE CORRECT OUTPUT (the order of threads can change): 
 * Thread-0 waiting at barrier 1
 * Thread-3 waiting at barrier 2
 * Thread-2 waiting at barrier 1
 * Thread-1 waiting at barrier 1
 * Thread-1 waiting at barrier 2
 * Thread-2 waiting at barrier 2
 */

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args) {
        CyclicBarrier barrier1 = new CyclicBarrier(3);
        CyclicBarrier barrier2 = new CyclicBarrier(3);

        Runner1 runner1 = new Runner1();
        runner1.setBarriers(barrier1, 1);
        Runner2 runner2 = new Runner2();
        runner2.setBarriers(barrier1, barrier2);
        Runner2 runner3 = new Runner2();
        runner3.setBarriers(barrier1, barrier2);
        Runner1 runner4 = new Runner1();
        runner4.setBarriers(barrier2, 2);

        runner1.start();
        runner2.start();
        runner3.start();
        runner4.start();
    }
}

class Runner2 extends Thread {
    CyclicBarrier barrier1;
    CyclicBarrier barrier2;

    public Runner2() {
    }

    public void setBarriers(CyclicBarrier barrier1, CyclicBarrier barrier2) {
        this.barrier1 = barrier1;
        this.barrier2 = barrier2;
    }

    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " waiting at barrier 1");

            barrier1.await();

            Thread.sleep((long) Math.random() * 1000);
            System.out.println(Thread.currentThread().getName() + " waiting at barrier 2");

            barrier2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

class Runner1 extends Thread {
    int i;
    CyclicBarrier barrier;

    public Runner1() {
    }

    public void setBarriers(CyclicBarrier barrier, int i) {
        this.barrier = barrier;
        this.i = i;
    }

    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " waiting at barrier " + i);

            //barrier.await();      Fault - removed await()
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}