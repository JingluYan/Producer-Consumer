/**
 * There are three secretarys to type letters with different speeds and there is
 * one manager to sign letters. One tray is able to hold at most five letters.
 * Once the tray is empty, manager waits. While the tray is full, secretarys
 * wait.
 * 
 * @author Jinglu Yan 200839014
 */
public class Office {

	/**
	 * Create secratary objects, manager object and tray object. Create and
	 * start threads for secretarys and manager.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Create tray object.
		Tray tray = new Tray();
		// Create three secretary objects with different name and speed.
		Secretary secretaryA = new Secretary(tray, 'A', 1000);
		Secretary secretaryB = new Secretary(tray, 'B', 2000);
		Secretary secretaryC = new Secretary(tray, 'C', 4000);
		// Create manager object.
		Manager manager = new Manager(tray, secretaryA, 2000);

		// Create three threads for different secretarys.
		Thread Athread = new Thread(secretaryA);
		Thread Bthread = new Thread(secretaryB);
		Thread Cthread = new Thread(secretaryC);
		// Create manager thread.
		Thread mthread = new Thread(manager);

		// Start these four threads (Three secretarys and manager).
		Athread.start();
		Bthread.start();
		Cthread.start();
		mthread.start();
	}
}

/**
 * The tray can hold a maximum of 5 letters at a time. One letter is added to
 * tray when secretary finish typing. Once manager signs a letter, a letter is
 * removed from tray.
 * 
 * @author Jinglu Yan 200839014
 * 
 */
class Tray {
	/**
	 * The number of items in tray.
	 */
	private int numItems = 0;
	/**
	 * Binary semaphore for judging if the tray is full.
	 */
	private boolean full = (numItems == 5);
	/**
	 * Binary semaphore for judging if the tray is empty.
	 */
	private boolean empty = (numItems == 0);

	/**
	 * Judge if the tray is empty currently.It is used for other classes.
	 * 
	 * @return true or false
	 */
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * Add a letter in tray.
	 * 
	 * @param i
	 *            letter
	 */
	public synchronized void add(int i) {
		// If the tray is full
		while (full) {
			try {
				// Wait and not add
				wait();
			} catch (InterruptedException e) {
			}
		}
		// If the tray is not full, add the number of items in tray
		numItems++;
		// Resite the binary semaphore full
		full = (numItems == 5);
		// Tray is not empty
		empty = false;
		// Wakeup a single thread in waiting pool
		notify();
		System.out.println("A letter has successfully been added to the tray.");
		System.out.println("Tray = " + numItems);
	}

	/**
	 * Remove a letter in tray.
	 */
	public synchronized void remove() {
		// If the tray is empty
		while (empty) {
			try {
				// Not remove and wait
				wait();
			} catch (InterruptedException e) {
			}
		}
		// If the tray is not empty, remove a letter in tray and decrease the
		// number of items in tray.
		numItems--;
		// Resite binary semaphore emty
		empty = (numItems == 0);
		// Tray is not full
		full = false;
		// Wakeup a thread in waiting pool
		notify();
		System.out.println("A letter has been removes form the tray.");
		System.out.println("Tray = " + numItems);
	}
}

/**
 * Secretary types letters and add letters in tray.
 * 
 * @author Jinglu Yan 200839014
 * 
 */
class Secretary implements Runnable {
	/**
	 * Each secretary types seven letters.
	 */
	private static final int letters = 7;
	/**
	 * Initial finished task number is zero.
	 */
	private int tasksDone = 0;
	/**
	 * Object tray.
	 */
	private final Tray tray;
	/**
	 * Name of secretary.
	 */
	private char name;
	/**
	 * Secretary type speed.
	 */
	private int speed;

	/**
	 * Constructor for secretary.
	 * 
	 * @param t
	 *            tray object
	 * @param n
	 *            name of secretary
	 * @param s
	 *            type speed
	 */
	Secretary(Tray t, char n, int s) {
		tray = t;
		name = n;
		speed = s;
	}

	/**
	 * Judging is there are more task left.
	 * 
	 * @return true or false
	 */
	public boolean moreTasks() {
		// If the number of finished task is smaller than the number of total
		// letters, return true.
		return tasksDone < letters;
	}

	/**
	 * Secretary type letter and add in tray.
	 */
	public void run() {
		while (moreTasks()) {
			// Print out the number of finished task number
			System.out.println("Secretary " + name + " has typed letter = "
					+ (tasksDone + 1));
			// Add letter in tray and increase the finished task number
			tray.add(tasksDone++);
			System.out.println("Secretary " + name
					+ " has added letter to the tray.");
			System.out.println("Secretary " + name
					+ " is ready to type a new letter.");
			try {
				// Make secretary sleep
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

/**
 * Manager removes letter and signs letter with particular speed.
 * 
 * @author Jinglu Yan 200839014
 * 
 */
class Manager implements Runnable {
	/**
	 * Object tray.
	 */
	private final Tray tray;
	/**
	 * Object secretary.
	 */
	private final Secretary secre;
	/**
	 * Manager sign speed.
	 */
	private final int speed;

	Manager(Tray t, Secretary s, int ss) {
		tray = t;
		secre = s;
		speed = ss;
	}

	/**
	 * Manager removes letter and signs letter.
	 */
	public void run() {
		// Store the total signed letters
		int total = 0;
		// If secretary has more tasks and tray is not empty
		while (secre.moreTasks() || !tray.isEmpty()) {
			// remove letter in tray
			tray.remove();
			System.out
					.println("The manager has taken a letter form the tray to sign;");
			System.out.println("Signed = " + ++total);
			try {
				// Make manager sleep
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("The manager is ready to sign a letter.");
		}
	}
}