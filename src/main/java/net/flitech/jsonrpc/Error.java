package net.flitech.jsonrpc;


@SuppressWarnings({"unused", "WeakerAccess"})
public class Error implements java.io.Serializable {

    private static final long SerialVersionUID = 1L;
    protected static final int SERVER_ERROR_CODE_MIN = -32099;
    protected static final int SERVER_ERROR_CODE_MAX = -32000;
    public static final int RESERVED_ERROR_CODE_MIN = -32768;
    public static final int RESERVED_ERROR_CODE_MAX = SERVER_ERROR_CODE_MAX;
    public static final int APPLICATION_EXCEPTION_ERROR_CODE = 1337;


    private final int code;
    private final String message;
    private final Throwable cause;


    public enum Predefined {
        ParseError(-32700, "Parse Error", "The request message could not be parsed as JSON"),
        InvalidRequest(-32600, "Invalid Request", "The JSON message sent was not a valid JSON-RPC Request object"),
        MethodNotFound(-32601, "Method Not Found", "The requested method does not exist or is not available"),
        InvalidParams(-32602, "Invalid Params", "Invalid method parameter(s)"),
        InternalError(-32603, "Internal Error", "JSON-RPC implementation internal error"),
//        ServerError(SERVER_ERROR_CODE_MAX, "Server Error", "Shit broke"),
        ;

        private final int code;
        private final String message;
        private final String detail;

        Predefined(int code, String message, String detail) {
            this.code = code;
            this.message = message;
            this.detail = detail;
        }

    }


    public Error(Predefined type, Throwable cause) {
        this.code = type.code;
        this.message = type.message;
        this.cause = cause;
    }

    public Error(Predefined type) {
        this(type, null);
    }

    private Error(int code, String message, Throwable cause) {
        if (code > RESERVED_ERROR_CODE_MAX || code < RESERVED_ERROR_CODE_MIN) {
            throw new IllegalArgumentException(
                    String.format("Error code %d is within the range (%d - %d) reserved by JSON-RPC 2.0",
                            code, RESERVED_ERROR_CODE_MIN, RESERVED_ERROR_CODE_MAX));
        }
        this.code = code;
        this.message = message;
        this.cause = cause;
    }

    public Error(int code, Throwable throwable) {
        this(code, throwable.getMessage(), throwable);
    }

    public Error(Throwable throwable) {
        this(APPLICATION_EXCEPTION_ERROR_CODE, throwable.getMessage(), throwable);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getCause() {
        return cause;
    }
}
