package ass08.rxjava;

import rx.Observable;

import ass08.HeartbeatSensor;
import pap.ass08.pos.Flag;

public class HeartbeatStream extends HeartbeatSensor {

	private int period;
	
	public HeartbeatStream(int period) {
		super();
		this.period = period;
	}

	public Observable<Integer> makeObservable(Flag flag) {
		return Observable.create(s -> {
			new Thread(() -> {
				while(!flag.isSet()) {
					s.onNext(this.getCurrentValue());
					try {
						Thread.sleep(this.period);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				s.onCompleted();
			}).start();
		});
	}
	
}
