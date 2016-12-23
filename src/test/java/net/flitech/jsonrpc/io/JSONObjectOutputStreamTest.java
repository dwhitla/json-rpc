package net.flitech.jsonrpc.io;

import net.flitech.jsonrpc.test.X;
import net.flitech.jsonrpc.test.Y;
import net.flitech.jsonrpc.test.Z;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 */
public class JSONObjectOutputStreamTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void objectStreamClass() {
        ObjectStreamClass xDesc = ObjectStreamClass.lookup(X.class);
        ObjectStreamClass yDesc = ObjectStreamClass.lookupAny(Y.class);
        ObjectStreamClass zDesc = ObjectStreamClass.lookupAny(Z.class);
    }

    @Test
    public void writeObjectOverride() throws Exception {
        OutputStream f = new ByteArrayOutputStream();
        ObjectOutput s = new JSONObjectOutputStream(f);
        s.writeObject("Today");
        s.writeObject(new Date());
        s.flush();
        assertEquals("{\"type\":\"java.util.String\",\"value\":}{}", f.toString());
    }

}
