package ass06.ex1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BlackboardImpl implements Blackboard {
	
	private Map<String,List<Msg>> messages;

	public BlackboardImpl() {
		this.messages = new HashMap<>();
	}

	@Override
	public synchronized void post(String tag, Msg msg) {
		List<Msg> tagList = this.messages.get(tag);
		if(tagList==null) {
			tagList = new LinkedList<>(); // LinkedList invece di ArrayList perché le liste vengono spesso create con pochi elementi ed eliminate velocemente
		}
		tagList.add(msg); // Inserisce il messaggio nella lista associata al tag
		this.messages.put(tag, tagList);
		notifyAll(); // Risveglia gli altri
	}

	@Override
	public synchronized Msg take(String tag) {
		while(!this.messages.containsKey(tag)) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Msg takenMessage = this.messages.get(tag).remove(0); // Per comodità si prende il primo elemento della lista (anche in seguito)
		if(this.messages.get(tag).isEmpty()) { // Se la lista associata ad un tag si svuota, si elimina il tag
			this.messages.remove(tag);
		}
		return takenMessage;
	}

	@Override
	public synchronized Optional<Msg> takeIfPresent(String tag) {
		if(!this.messages.containsKey(tag)) {
			return Optional.empty();
		}
		Msg takenMessage = this.messages.get(tag).remove(0);
		if(this.messages.get(tag).isEmpty()) { // Se la lista associata ad un tag si svuota, si elimina il tag
			this.messages.remove(tag);
		}
		return Optional.of(takenMessage);
	}

	@Override
	public synchronized Msg read(String tag) {
		while(!this.messages.containsKey(tag)) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return this.messages.get(tag).get(0);
	}

	@Override
	public synchronized Optional<Msg> readIfPresent(String tag) {
		if(!this.messages.containsKey(tag)) {
			return Optional.empty();
		}
		return Optional.of(this.messages.get(tag).get(0));
	}

}
