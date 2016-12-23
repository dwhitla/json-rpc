package net.flitech.jsonrpc;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;

/**
 */
public class RequestTest {
    @Test
    public void write() throws Exception {
        Request request = new Request("testMethod", "foo", "bar", 1);
        OutputStream out = new ByteArrayOutputStream();

        assertEquals("{\"method:\"}", "");
        /**
         * { "method": "testMethod", "params": [ "foo", "bar", 1 ], "id": "" }
         */
    }

}
