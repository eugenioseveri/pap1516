package ass07.actors;

public class GenericMessage<T> {
	
	private final T data;
	
	public GenericMessage(T data) {
		this.data = data;
	}
	
	T getMessage() {
		return this.data;
	}
}
