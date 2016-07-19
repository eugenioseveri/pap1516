package ass07.tasks;

public class GenericSynchronizedFlag<T> {
	
	private T value;

    public GenericSynchronizedFlag(T initialValue) {
        this.value = initialValue;
    }
    
    public synchronized T getValue() {
        return this.value;
    }

    public synchronized void setValue(T value) {
        this.value = value;
    }
}
