package lab04.hello;

public class TestHello {

	public static void main(String[] args) throws Exception {
		
		MyThread myThread = new MyThread("Pippo");
		myThread.start();		
		
		String myName = Thread.currentThread().getName();
		// Thread.currentThread().sleep(10000);
		System.out.println("Thread spawned - I'm "+myName);
		
	}
}
