package net.flitech.jsonrpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;
import java.util.regex.Pattern;


@SuppressWarnings({"WeakerAccess", "unused"})
public class Request implements Serializable {

    private static final String VERSION = "2.0";
    private static final Pattern RESERVED_METHOD_NAME_PATTERN = Pattern.compile("^rpc\\..*$");
    private static final String RESERVED_METHOD_NAME_ERROR = String.format(
            "Method names matching '%s' are reserved for JSON-RPC internal use.", RESERVED_METHOD_NAME_PATTERN);

    private final String method;
    private final Object[] params;
    private final String id;


    private Request(String method, boolean isNotification, Object ... params) {
        if (RESERVED_METHOD_NAME_PATTERN.matcher(method).matches()) {
            throw new IllegalArgumentException(RESERVED_METHOD_NAME_ERROR);
        }
        this.method = method;
        this.id = isNotification ? null : UUID.randomUUID().toString();
        this.params = params[0] == null ? null : params;
    }

    public Request(String method, Object ... params) {
        this(method, false, params);
    }

    private boolean isNotification() {
        return id == null;
    }

    public String getMethod() {
        return method;
    }

    public Object[] getParams() {
        return params;
    }

    public String getId() {
        return id;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }

}
