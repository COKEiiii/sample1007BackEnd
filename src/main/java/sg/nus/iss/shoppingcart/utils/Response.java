package sg.nus.iss.shoppingcart.utils;

import sg.nus.iss.shoppingcart.enums.ResponseStatus;

public class Response<T> {
    private int statusCode;

    private String message;
    private T data;

    public Response(ResponseStatus status, String message, T data) {
        this.statusCode = status.getCode();
        this.message = message;
        this.data = data;
    }

    public Response(ResponseStatus status, String message) {
        this.statusCode = status.getCode();
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatus(int status) {
        this.statusCode = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
