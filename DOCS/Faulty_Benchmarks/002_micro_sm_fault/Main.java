/**
 * Concurrent Benchmarks
 * 
 * Title:  micro_sm - with fault
 * 
 * Description:  This benchmarks has the objective of verify if testing 
 *               tools for concurrent programs are able to find incorrect 
 *               initialization of the number of permits in a semaphore.
 *
 * Paradigm:     Shared Memory
 * 
 * Fault:       "Incorrect count initialization"
 *               Line with fault: 48
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
 * OUTPUT:
 * Thread-1 shared integer = 2
 * Thread-0 shared integer = 2
 * Thread-1 shared integer = 3
 * Thread-0 shared integer = 4
 * 
 * THE CORRECT OUTPUT:
 * Thread-0 shared integer = 1
 * Thread-0 shared integer = 2
 * Thread-1 shared integer = 3
 * Thread-1 shared integer = 4
 */
 
import java.util.concurrent.Semaphore;

public class Main {
	public static void main(String[] args) throws Exception {
		Shared sharedInt = new Shared();
		Semaphore mutex = new Semaphore(0);
		mutex.release();
		mutex.release(); // Fault - extra release

		Runner runner1 = new Runner();
		runner1.setSharedVars(mutex, sharedInt);

		Runner runner2 = new Runner();
		runner2.setSharedVars(mutex, sharedInt);

		runner1.start();
		runner2.start();

		runner1.join();
		runner2.join();
	}
}

class Runner extends Thread {
	Shared sharedInt;
	Semaphore mutex;

    public void setSharedVars(Semaphore mutex, Shared sharedInt) {
    	this.mutex = mutex;
    	this.sharedInt = sharedInt;
    }

	public void run() {		
		mutex.acquireUninterruptibly();
        sharedInt.setShared(sharedInt.getShared() + 1);
        System.out.println("sharedInt = " + sharedInt.getShared());
        mutex.release();

        mutex.acquireUninterruptibly();
        sharedInt.setShared(sharedInt.getShared() + 1);
        System.out.println("sharedInt = " + sharedInt.getShared());
        mutex.release();
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