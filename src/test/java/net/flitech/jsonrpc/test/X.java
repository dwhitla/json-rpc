package net.flitech.jsonrpc.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;


public class X implements Serializable {
    public final int i;

    public X(int i) {
        this.i = i;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }

    private void readObjectNoData() throws ObjectStreamException {
    }

    /*
    Object writeReplace() throws ObjectStreamException {
    }

    Object readResolve() throws ObjectStreamException {
    }
    */

}
