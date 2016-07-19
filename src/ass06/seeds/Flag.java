package ass06.seeds;

public class Flag {
	
	private boolean isSet;
	
	public Flag() {
		this.isSet = false;
	}
	
	public synchronized void set() {
		this.isSet = true;
	}
	
	public synchronized boolean isSet() {
		return this.isSet;
	}
	
	public synchronized void reset() {
		this.isSet = false;
	}

}
