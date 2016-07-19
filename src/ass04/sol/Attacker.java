package ass04.sol;

import ass04.SecureSystem;

public class Attacker extends Thread {
	
	private int id;
	private long from;
	private long to;
	private PassGen pgen;
	private SecureSystem sys;
	private long startTime;
	
	public Attacker(int id, long from, long to, PassGen pgen, SecureSystem sys, long startTime){
		this.id = id;
		this.from = from;
		this.to = to;
		this.pgen = pgen;
		this.sys = sys;
		this.startTime = startTime;
	}
	
	public void run(){
		log("working from "+from+" to "+(to-1));
		for (long v = from; v < to; v++){
			char[] pass = pgen.getPass(v);
			String pwd = new String(pass);
			// log("trying "+ PassGen.toStringVal(pass) + " - "+ pwd);
			boolean ok = sys.login(pwd);
			if (ok){
				log("FOUND: "+pwd);
				log("Time elapsed: "+(System.currentTimeMillis()-startTime)+"ms.");
				System.exit(0);
			}
		}
		
	}
	
	protected void log(String msg){
		synchronized(System.out) {
			System.out.println("[worker-"+id+"] "+msg);
		}
	}
	
}
