package ass08.rxjava;

import pap.ass08.pos.Flag;
import pap.ass08.pos.P2d;
import pap.ass08.pos.PosSensor;
import rx.Observable;

public class Model {
	private HeartbeatStream hbSensor;
	private PosSensor posSensor;
	private Flag flag;
	
	public Model(HeartbeatStream hbSensor, PosSensor posSensor) {
		this.hbSensor = hbSensor;
		this.posSensor = posSensor;
	}
	
	public void resetFlag() {
		this.flag = new Flag();
	}
	
	public Flag getFlag() {
		return this.flag;
	}
	
	public Observable<Object> getMergedStreams() { 
		resetFlag();
		Observable<Integer> hbSensorStream = this.hbSensor.makeObservable(this.flag);;
		Observable<P2d> posSensorStream = this.posSensor.makeObservable(1000, this.flag);
		return Observable.merge(hbSensorStream, posSensorStream);
	}

}
