package publisher;

public class Publisher {
    private Subscriber subscriber;

    public void add(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void publish(Message message) {
        subscriber.receive(message);
    }
}
