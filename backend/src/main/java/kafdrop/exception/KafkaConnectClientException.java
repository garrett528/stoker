package kafdrop.exception;

public final class KafkaConnectClientException extends RuntimeException {
    public KafkaConnectClientException(Throwable cause) {
        super(cause);
    }
}
