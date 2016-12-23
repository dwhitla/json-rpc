package net.flitech.jsonrpc.io;

import java.io.NotActiveException;


/**
 * Context during upcalls from object stream to class-defined readObject/writeObject methods.
 * Holds object currently being deserialized and descriptor for current class.
 *
 * This context keeps track of the thread it was constructed on, and allows only a single call of defaultReadObject,
 * readFields, defaultWriteObject or writeFields which must be invoked on the same thread before the class's
 * readObject/writeObject method has returned.
 * If not set to the current thread, the getObj method throws NotActiveException.
 */
final class SerialCallbackContext {
    private final Object obj;
    private final ClassDescriptor desc;
    private Thread thread;

    public SerialCallbackContext(Object obj, ClassDescriptor desc) {
        this.obj = obj;
        this.desc = desc;
        this.thread = Thread.currentThread();
    }

    public Object getObj() throws NotActiveException {
        checkAndSetUsed();
        return obj;
    }

    public ClassDescriptor getDesc() {
        return desc;
    }

    public void check() throws NotActiveException {
        if (thread != null && thread != Thread.currentThread()) {
            throw new NotActiveException(
                    "expected thread: " + thread + ", but got: " + Thread.currentThread());
        }
    }

    private void checkAndSetUsed() throws NotActiveException {
        if (thread != Thread.currentThread()) {
            throw new NotActiveException(
                    "not in readObject invocation or fields already read");
        }
        thread = null;
    }

    public void setUsed() {
        thread = null;
    }
}
