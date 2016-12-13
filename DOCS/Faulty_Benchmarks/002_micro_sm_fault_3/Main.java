 /**
 * Concurrent Benchmarks
 * 
 * Title:  micro_sm - with fault    
 * 
 * Description:  This benchmarks has the objective of verify if testing 
 *               tools for concurrent programs are able to find Non-atomic 
 *               operations assumed to be atomic.
 *
 * Paradigm:     Shared Memory
 * 
 * Fault:        Wrong Lock or No Lock
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

/* TEST
 * java Main
 * 
 * A POSSIBLE WRONG OUTPUT:
 * Thread-0 shared integer = 2
 * Thread-1 shared integer = 2
 * 
 * THE CORRECT OUTPUT:
 * Thread-0 shared integer = 1
 * Thread-1 shared integer = 2
 */

 
import java.util.concurrent.Semaphore;

public class Main {
	public static void main(String[] args) throws Exception {
		Shared sharedInt = new Shared();
        
        Semaphore mutex1 = new Semaphore(0);
        mutex1.release();
        
        Semaphore mutex2 = new Semaphore(0);
        mutex2.release();

        Runner1 runner1 = new Runner1();
        runner1.setSharedVars(mutex1, sharedInt);

        Runner2 runner2 = new Runner2();
        runner2.setSharedVars(mutex2, sharedInt);

        runner1.start();
        runner2.start();

        runner1.join();
        runner2.join();
	}
}

class Runner1 extends Thread {
    Shared sharedInt;
    Semaphore mutex1;

    public void setSharedVars(Semaphore mutex1, Shared sharedInt) {
        this.mutex1 = mutex1;
        this.sharedInt = sharedInt;
    }

    public void run() {
        mutex1.acquireUninterruptibly();
        sharedInt.setShared(sharedInt.getShared() + 1);
        System.out.println("shared integer = " + sharedInt.getShared());
        mutex1.release();
    }
}

class Runner2 extends Thread {
    Shared sharedInt;
    Semaphore mutex2;

    public void setSharedVars(Semaphore mutex2, Shared sharedInt) {
        this.mutex2 = mutex2;
        this.sharedInt = sharedInt;
    }

    public void run() {
        mutex2.acquireUninterruptibly();
        sharedInt.setShared(sharedInt.getShared() + 1);
        System.out.println("shared integer = " + sharedInt.getShared());
        mutex2.release();
    }
}

class Shared {
    int shared = 0;

    public void setShared(int shared) {
        this.shared = shared;
    }

    public int getShared() {
        return shared;
    }
}