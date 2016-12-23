package net.flitech.jsonrpc.io;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.StreamCorruptedException;

/**
 */
public class JSONObjectInputStream extends ObjectInputStream {

    private JsonParser parser;

    /**
     * Creates a JSONObjectInputStream that reads from the specified InputStream.
     * A serialization stream header is read from the stream and verified.
     * This constructor will block until the corresponding JSONObjectOutputStream
     * has written and flushed the header.
     *
     * <p>If a security manager is installed, this constructor will check for
     * the "enableSubclassImplementation" SerializablePermission when invoked
     * directly or indirectly by the constructor of a subclass which overrides
     * the ObjectInputStream.readFields or ObjectInputStream.readUnshared
     * methods.
     *
     * @param   in input stream to read from
     * @throws StreamCorruptedException if the stream header is incorrect
     * @throws  IOException if an I/O error occurs while reading stream header
     * @throws  SecurityException if untrusted subclass illegally overrides
     *          security-sensitive methods
     * @throws  NullPointerException if <code>in</code> is <code>null</code>
     * @see     ObjectInputStream#ObjectInputStream()
     * @see     ObjectInputStream#readFields()
     * @see     ObjectOutputStream#ObjectOutputStream(OutputStream)
     */
    public JSONObjectInputStream(InputStream in) throws IOException {
        super();
        parser = Json.createParser(in);
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        return super.readClassDescriptor();
    }

    @Override
    protected Object readObjectOverride() throws IOException, ClassNotFoundException {
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            switch(event) {
                case START_ARRAY:
                case END_ARRAY:
                case START_OBJECT:
                case END_OBJECT:
                case VALUE_FALSE:
                case VALUE_NULL:
                case VALUE_TRUE:
                    System.out.println(event.toString());
                    break;
                case KEY_NAME:
                    System.out.print(event.toString() + " " +
                            parser.getString() + " - ");
                    break;
                case VALUE_STRING:
                case VALUE_NUMBER:
                    System.out.println(event.toString() + " " +
                            parser.getString());
                    break;
            }
        }
        return null;
    }
}
