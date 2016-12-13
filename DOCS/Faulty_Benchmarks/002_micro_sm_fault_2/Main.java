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
 * Fault:       "Non-atomic operations assumed to be atomic"
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
 * POSSIBLE OUTPUT:
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

		Runner runner1 = new Runner();
		runner1.setSharedVars(sharedInt);

		Runner runner2 = new Runner();
		runner2.setSharedVars(sharedInt);

		runner1.start();
		runner2.start();

		runner1.join();
		runner2.join();
	}
}

class Runner extends Thread {
	Shared sharedInt;

    public void setSharedVars(Shared sharedInt) {
    	this.sharedInt = sharedInt;
    }

	public void run() {		
        sharedInt.setShared(sharedInt.getShared() + 1);
        System.out.println("sharedInt = " + sharedInt.getShared());

        sharedInt.setShared(sharedInt.getShared() + 1);
        System.out.println("sharedInt = " + sharedInt.getShared());
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