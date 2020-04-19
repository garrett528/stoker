package kafdrop.exception;

public final class KafkaAdminClientException extends RuntimeException {
  public KafkaAdminClientException(Throwable cause) {
    super(cause);
  }
}
