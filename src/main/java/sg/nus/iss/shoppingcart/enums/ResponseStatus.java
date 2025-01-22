package sg.nus.iss.shoppingcart.enums;

/**
 * @ClassName ResponseStatus
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2024/10/3
 * @Version 1.0
 */
public enum ResponseStatus {
    SUCCESS(200, "Success"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    UNAUTHORIZED(401, "Session error" );

    private final int code;
    private final String description;

    ResponseStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    // get status code
    public int getCode() {
        return code;
    }

    // get status description
    public String getDescription() {
        return description;
    }
}
