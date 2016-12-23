package net.flitech.jsonrpc;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 */
public class Response implements Serializable {

    private static final long SerialVersionUID = 1L;
    private static final String JSONRPC_VERSION = "2.0";

    private final String jsonrpc = JSONRPC_VERSION;
    private final Object result;
    private final Error error;
    private final Object id;


    public Response(Object result, String id) {
        this.id = id;
        if (Error.class.isAssignableFrom(result.getClass())) {
            this.result = null;
            this.error = ((Error) result);
        } else {
            this.result = result;
            this.error = null;
        }
    }

    private Response(Error error, Object id) {
        this.id = id;
        this.result = null;
        this.error = error;
    }

    public Response(Error error, Number id) {
        this(error, (Object)id);
    }

    public Response(Error error, String id) {
        this(error, (Object)id);
    }

    public boolean isError() {
        return error != null;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(jsonrpc);
        if (isError()) {
            out.writeObject(error);
        } else {
            out.writeObject(result);
        }
        out.writeObject(id);
    }

}
