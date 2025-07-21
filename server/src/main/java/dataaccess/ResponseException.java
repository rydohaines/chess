package dataaccess;

public class ResponseException extends Exception {
    public ResponseException(int errorCode,String message) {
        super(message);
    }
}
