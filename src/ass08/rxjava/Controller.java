package ass08.rxjava;

import javax.swing.SwingUtilities;

import pap.ass08.pos.P2d;

public class Controller implements InputListener {

	private Model model;
	private View view;
	private int threshold, k;
	private int heartbeatValuesSum, heartbeatUpdatesCount;
	private long alarmInitialTime;
	private int maxHeartbeatValue;
	private P2d lastPosition;
	private long lastPositionTime;
	
	public Controller(Model model, View view, int threshold, int k) {
		this.model = model;
		this.view = view;
		this.threshold = threshold;
		this.k = k;
	}
	
	@Override
	public void started() {
		this.alarmInitialTime = System.currentTimeMillis(); // Prima inizializzazione timer
		this.model.getMergedStreams().subscribe(p -> {
			if(p instanceof Integer) {
				SwingUtilities.invokeLater(() -> {
					this.view.updateCurrentHeartbeatValue(p.toString());
					this.heartbeatValuesSum += (Integer)p;
					this.heartbeatUpdatesCount++;
					this.view.updateAverageHeartbeatValue(Integer.toString(this.heartbeatValuesSum/this.heartbeatUpdatesCount));
					if((Integer)p>this.threshold) { // Allarme (soglia superata)
						if((System.currentTimeMillis()-this.alarmInitialTime)>this.k*1000) {
							this.view.updateAlarmNotification("True");
						}
					} else { // Allarme rientrato
						this.alarmInitialTime = System.currentTimeMillis();
						this.view.updateAlarmNotification("False");
					}
					if((Integer)p>this.maxHeartbeatValue) { // Memorizza il valore massimo di battito
						this.maxHeartbeatValue = (Integer)p;
						if(this.lastPosition!= null) {
							this.view.updatePositionWithMaxHeartbeatValue(this.lastPosition.toString());
						}
					}
				});
				
			} else if(p instanceof P2d) {
				if(this.lastPosition==null) {
					this.lastPosition = (P2d)p; // Prima inizializzazione posizione
				}
				SwingUtilities.invokeLater(() -> {
					double distance = P2d.distance((P2d)p, this.lastPosition);
					this.view.updateCurrentSpeed(Double.toString(distance/(System.currentTimeMillis()-this.lastPositionTime)));
					this.lastPositionTime = System.currentTimeMillis();
					this.lastPosition = (P2d)p;
					this.view.updateGridPosition((P2d)p);
				});
				
			}
		});
	}

	@Override
	public void stopped() {
		this.model.getFlag().set();
	}
	
}
