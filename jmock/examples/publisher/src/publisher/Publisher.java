package publisher;

public class Publisher {
    private Subscriber subscriber;

    public void add(Subscriber newSubscriber) {
        this.subscriber = newSubscriber;
    }

    public void publish(Message message) {
        subscriber.receive(message);
    }
}
