package ass04.sol;

import ass04.SecureSystem;

public class AttackerOptimized extends Thread {
	
	private int id;
	private long from;
	private long to;
	private PassGenWithState pgen;
	private SecureSystem sys;
	private long startTime;
	
	public AttackerOptimized(int id, long from, long to, int nDigits, SecureSystem sys, long startTime){
		this.id = id;
		this.from = from;
		this.to = to;
		this.sys = sys;
		this.pgen = new PassGenWithState(nDigits,from);
		this.startTime = startTime;
	}
	
	public void run(){
		log("working from "+from+" to "+(to-1));
		for (long v = from; v < to; v++){
			char[] pass = pgen.getCurrent();
			String pwd = new String(pass);
			// log("trying "+ PassGen.toStringVal(pass) + " - "+ pwd);
			boolean ok = sys.login(pwd);
			if (ok){
				log("FOUND "+pwd);
				log("Time elapsed: "+(System.currentTimeMillis()-startTime)+"ms.");
				System.exit(0);
			} else {
				pgen.next();
			}
		}
		
	}
	
	protected void log(String msg){
		synchronized(System.out) {
			System.out.println("[worker-"+id+"] "+msg);
		}
	}
	
}
