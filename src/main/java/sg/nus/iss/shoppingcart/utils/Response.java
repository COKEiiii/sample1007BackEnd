package sg.nus.iss.shoppingcart.utils;

import sg.nus.iss.shoppingcart.enums.ResponseStatus;

/**
 * @ClassName Response
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2024/10/3
 * @Version 1.0
 */
public class Response<T> {
    // status code and status description
    private int statusCode;
    // respponse message
    private String message;
    private T data;

    // including the data
    public Response(ResponseStatus status, String message, T data) {
        this.statusCode = status.getCode();
        this.message = message;
        this.data = data;
    }

    // without data
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
