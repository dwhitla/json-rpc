package net.flitech.jsonrpc.io;


import javax.json.Json;
import javax.json.stream.JsonGenerator;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.NotActiveException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.Field;

public class JSONObjectOutputStream extends ObjectOutputStream {

    /**
     * Context during upcalls to class-defined writeObject methods; holds
     * object currently being serialized and descriptor for current class.
     * Null when not during writeObject upcall.
     */
    private SerialCallbackContext context;
    private JsonGenerator gen;
    private int depth;

    /**
     * Creates a JSONObjectOutputStream that writes to the specified OutputStream.
     * This constructor writes the serialization stream header to the
     * underlying stream; callers may wish to flush the stream immediately to
     * ensure that constructors for receiving JSONObjectOutputStreams will not block
     * when reading the header.
     *
     * @param   out output stream to write to
     * @throws  IOException if an I/O error occurs while writing stream header
     * @throws  SecurityException if untrusted subclass illegally overrides
     *          security-sensitive methods
     * @throws  NullPointerException if <code>out</code> is <code>null</code>
     * @since   1.4
     * @see     ObjectOutputStream#ObjectOutputStream()
     * @see     ObjectOutputStream#putFields()
     * @see     ObjectInputStream#ObjectInputStream(InputStream)
     */
    public JSONObjectOutputStream(OutputStream out) throws IOException, SecurityException {
        super();
        gen = Json.createGenerator(out);
    }

    @Override
    public void useProtocolVersion(int version) throws IOException {
        switch (version) {
            case PROTOCOL_VERSION_1:
            case PROTOCOL_VERSION_2:
                break;
            default:
                throw new IllegalArgumentException("unknown version: " + version);
        }
    }

//    protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
//        gen.write("type", desc.getName());
//        gen.write("suid", desc.getSerialVersionUID());
//    }

//    /**
//     * Write the specified object to the JSONObjectOutputStream.  The class of the
//     * object, the signature of the class, and the values of the non-transient
//     * and non-static fields of the class and all of its supertypes are
//     * written.  Predefined serialization for a class can be overridden using the
//     * writeObject and the readObject methods.  Objects referenced by this
//     * object are written transitively so that a complete equivalent graph of
//     * objects can be reconstructed by a JSONObjectInputStream.
//     *
//     * <p>Exceptions are thrown for problems with the OutputStream and for
//     * classes that should not be serialized.  All exceptions are fatal to the
//     * OutputStream, which is left in an indeterminate state, and it is up to
//     * the caller to ignore or recover the stream state.
//     *
//     * @throws InvalidClassException Something is wrong with a class used by serialization.
//     * @throws NotSerializableException Some object to be serialized does not implement the
//     *      java.io.Serializable interface.
//     * @throws IOException Any exception thrown by the underlying OutputStream.
//     */
//    @Override
//    protected void writeObjectOverride(Object obj) throws IOException {
//        try {
//            writeObject0(obj, false);
//        } catch (IOException ex) {
//            if (depth == 0) {
//                writeFatalException(ex);
//            }
//            throw ex;
//        }
//    }

//    private void writeObject0(Object object, boolean unshared) throws IOException {
//        depth++;
//        try {
//            // handle previously written and non-replaceable objects
//            if (writeMetaObjects(object, unshared)) return;
//
//            // check for replacement object
//            Object orig = object;
//            Class<?> objClass = object.getClass();
//            Class<?> replClass = null;
//            ObjectStreamClass desc = ObjectStreamClass.lookupAny(objClass);
//            while (replClass != objClass) {
//                // REMIND: skip this check for strings/arrays?
//                desc = ObjectStreamClass.lookup(objClass);
//                if (desc.hasWriteReplaceMethod()) {
//                    object = desc.invokeWriteReplace(object);
//                    replClass = object.getClass();
//                }
//            }
//
//            Object rep = replaceObject(object);
//            if (rep != object && rep != null) {
//                objClass = rep.getClass();
//                desc = ObjectStreamClass.lookupAny(objClass);
//            }
//            object = rep;
//
//            // if object replaced, run through original checks a second time
//            if (object != orig) {
//                int handle;
//                subs.assign(orig, object);
//                if (object == null) {
//                    writeNull();
//                    return;
//                } else if (!unshared && (handle = handles.lookup(object)) != -1) {
//                    writeHandle(handle);
//                    return;
//                } else {
//                    if (writeMetaObjects(object, unshared)) return;
//                }
//            }
//
//            // remaining cases
//            if (object instanceof String) {
//                writeString((String) object, unshared);
//            } else if (objClass.isArray()) {
//                writeArray(object, desc, unshared);
//            } else if (object instanceof Enum) {
//                writeEnum((Enum<?>) object, desc, unshared);
//            } else if (object instanceof Serializable) {
//                writeOrdinaryObject(object, desc, unshared);
//            } else {
//                if (extendedDebugInfo) {
//                    throw new NotSerializableException(
//                            objClass.getName() + "\n" + debugInfoStack.toString());
//                } else {
//                    throw new NotSerializableException(objClass.getName());
//                }
//            }
//        } finally {
//            depth--;
//        }
//    }

//    private boolean writeMetaObjects(Object object, boolean unshared) throws IOException {
//        if (object instanceof Class) {
//            writeClass((Class) object, unshared);
//            return true;
//        } else if (object instanceof ClassDescriptor) {
//            writeClassDesc((ClassDescriptor) object, unshared);
//            return true;
//        }
//        return false;
//    }

//    /**
//     * Write the non-static and non-transient fields of the current class to
//     * this stream.  This may only be called from the writeObject method of the
//     * class being serialized. It will throw the NotActiveException if it is
//     * called otherwise.
//     *
//     * @throws  IOException if I/O errors occur while writing to the underlying <code>OutputStream</code>
//     */
//    @Override
//    public void defaultWriteObject() throws IOException {
//        SerialCallbackContext ctx = context;
//        if (ctx == null) {
//            throw new NotActiveException("not in call to writeObject");
//        }
//        Object curObj = ctx.getObj();
//        ClassDescriptor curDesc = ctx.getDesc();
//        defaultWriteFields(curObj, curDesc);
//    }

//    /**
//     * Writes an "unshared" object to the ObjectOutputStream.  This method is
//     * identical to writeObject, except that it always writes the given object
//     * as a new, unique object in the stream (as opposed to a back-reference
//     * pointing to a previously serialized instance).  Specifically:
//     * <ul>
//     *   <li>An object written via writeUnshared is always serialized in the
//     *       same manner as a newly appearing object (an object that has not
//     *       been written to the stream yet), regardless of whether or not the
//     *       object has been written previously.
//     *
//     *   <li>If writeObject is used to write an object that has been previously
//     *       written with writeUnshared, the previous writeUnshared operation
//     *       is treated as if it were a write of a separate object.  In other
//     *       words, ObjectOutputStream will never generate back-references to
//     *       object data written by calls to writeUnshared.
//     * </ul>
//     * While writing an object via writeUnshared does not in itself guarantee a
//     * unique reference to the object when it is deserialized, it allows a
//     * single object to be defined multiple times in a stream, so that multiple
//     * calls to readUnshared by the receiver will not conflict.  Note that the
//     * rules described above only apply to the base-level object written with
//     * writeUnshared, and not to any transitively referenced sub-objects in the
//     * object graph to be serialized.
//     *
//     * <p>ObjectOutputStream subclasses which override this method can only be
//     * constructed in security contexts possessing the
//     * "enableSubclassImplementation" SerializablePermission; any attempt to
//     * instantiate such a subclass without this permission will cause a
//     * SecurityException to be thrown.
//     *
//     * @param   obj object to write to stream
//     * @throws  NotSerializableException if an object in the graph to be
//     *          serialized does not implement the Serializable interface
//     * @throws  InvalidClassException if a problem exists with the class of an
//     *          object to be serialized
//     * @throws  IOException if an I/O error occurs during serialization
//     * @since 1.4
//     */
//    @Override
//    public void writeUnshared(Object obj) throws IOException {
//        try {
//            writeObject0(obj, true);
//        } catch (IOException ex) {
//            if (depth == 0) {
//                writeFatalException(ex);
//            }
//            throw ex;
//        }
//    }

    /**
     * Reset will disregard the state of any objects already written to the
     * stream.  The state is reset to be the same as a new ObjectOutputStream.
     * The current point in the stream is marked as reset so the corresponding
     * ObjectInputStream will be reset at the same point.  Objects previously
     * written to the stream will not be referred to as already being in the
     * stream.  They will be written to the stream again.
     *
     * @throws  IOException if reset() is invoked while serializing an object.
     */
    @Override
    public void reset() throws IOException {
        if (depth != 0) {
            throw new IOException("stream active");
        }
        clear();
    }

//    /**
//     * Writes representation of given class to stream.
//     */
//    private void writeClass(Class<?> clazz) throws IOException {
//        writeClassDesc(ClassDescriptor.lookup(clazz, true));
//    }

//    /**
//     * Writes representation of given class descriptor to stream.
//     */
//    private void writeClassDesc(ClassDescriptor desc) throws IOException {
//        if (desc == null) {
//            writeNull();
//        } else if (desc.isProxy()) {
//            writeProxyDesc(desc);
//        } else {
//            writeNonProxyDesc(desc);
//        }
//    }

//    /**
//     * Writes class descriptor representing a dynamic proxy class to stream.
//     */
//    private void writeProxyDesc(ClassDescriptor desc) throws IOException {
//        gen.write("type", "DynamicProxy");
//
//        Class<?> clazz = desc.getDescribedClass();
//        gen.writeStartArray("interfaces");
//        for (Class<?> iface : clazz.getInterfaces()) {
//            gen.write(iface.getName());
//        }
//        annotateProxyClass(clazz);
//        writeClassDesc(desc.getSuperDesc());
//    }

//    /**
//     * Writes class descriptor representing a standard (i.e. not a dynamic proxy) class to stream.
//     */
//    private void writeNonProxyDesc(ClassDescriptor desc) throws IOException {
//        gen.write("type", "java.lang.Class");
//        writeClassDescriptor(desc);
//        annotateClass(desc.getDescribedClass());
//        writeClassDesc(desc.getSuperDesc());
//    }

//    /**
//     * Writes given string to stream, using standard or long UTF format
//     * depending on string length.
//     */
//    private void writeString(String str, boolean unshared) throws IOException {
//        handles.assign(unshared ? null : str);
//        long utflen = bout.getUTFLength(str);
//        if (utflen <= 0xFFFF) {
//            bout.writeByte(TC_STRING);
//            bout.writeUTF(str, utflen);
//        } else {
//            bout.writeByte(TC_LONGSTRING);
//            bout.writeLongUTF(str, utflen);
//        }
//    }

//    /**
//     * Writes given array object to stream.
//     */
//    private void writeArray(Object array, ClassDescriptor desc) throws IOException {
//        bout.writeByte(TC_ARRAY);
//        writeClassDesc(desc, false);
//        handles.assign(unshared ? null : array);
//
//        Class<?> ccl = desc.forClass().getComponentType();
//        if (ccl.isPrimitive()) {
//            if (ccl == Integer.TYPE) {
//                int[] ia = (int[]) array;
//                bout.writeInt(ia.length);
//                bout.writeInts(ia, 0, ia.length);
//            } else if (ccl == Byte.TYPE) {
//                byte[] ba = (byte[]) array;
//                bout.writeInt(ba.length);
//                bout.write(ba, 0, ba.length, true);
//            } else if (ccl == Long.TYPE) {
//                long[] ja = (long[]) array;
//                bout.writeInt(ja.length);
//                bout.writeLongs(ja, 0, ja.length);
//            } else if (ccl == Float.TYPE) {
//                float[] fa = (float[]) array;
//                bout.writeInt(fa.length);
//                bout.writeFloats(fa, 0, fa.length);
//            } else if (ccl == Double.TYPE) {
//                double[] da = (double[]) array;
//                bout.writeInt(da.length);
//                bout.writeDoubles(da, 0, da.length);
//            } else if (ccl == Short.TYPE) {
//                short[] sa = (short[]) array;
//                bout.writeInt(sa.length);
//                bout.writeShorts(sa, 0, sa.length);
//            } else if (ccl == Character.TYPE) {
//                char[] ca = (char[]) array;
//                bout.writeInt(ca.length);
//                bout.writeChars(ca, 0, ca.length);
//            } else if (ccl == Boolean.TYPE) {
//                boolean[] za = (boolean[]) array;
//                bout.writeInt(za.length);
//                bout.writeBooleans(za, 0, za.length);
//            } else {
//                throw new InternalError();
//            }
//        } else {
//            Object[] objs = (Object[]) array;
//            int len = objs.length;
//            bout.writeInt(len);
//            if (extendedDebugInfo) {
//                debugInfoStack.push(
//                        "array (class \"" + array.getClass().getName() +
//                                "\", size: " + len  + ")");
//            }
//            try {
//                for (int i = 0; i < len; i++) {
//                    if (extendedDebugInfo) {
//                        debugInfoStack.push(
//                                "element of array (index: " + i + ")");
//                    }
//                    try {
//                        writeObject0(objs[i], false);
//                    } finally {
//                        if (extendedDebugInfo) {
//                            debugInfoStack.pop();
//                        }
//                    }
//                }
//            } finally {
//                if (extendedDebugInfo) {
//                    debugInfoStack.pop();
//                }
//            }
//        }
//    }

//    /**
//     * Writes given enum constant to stream.
//     */
//    private void writeEnum(Enum<?> en, ObjectStreamClass desc, boolean unshared) throws IOException {
//        gen.write("desc", TC_ENUM);
//        ObjectStreamClass sdesc = desc.getSuperDesc();
//        writeClassDesc((sdesc.forClass() == Enum.class) ? desc : sdesc, false);
//        handles.assign(unshared ? null : en);
//        writeString(en.name(), false);
//    }

//    /**
//     * Writes representation of a "ordinary" (i.e., not a String, Class,
//     * ObjectStreamClass, array, or enum constant) serializable object to the
//     * stream.
//     */
//    private void writeOrdinaryObject(Object obj,
//                                     ObjectStreamClass desc,
//                                     boolean unshared)
//            throws IOException
//    {
//        if (extendedDebugInfo) {
//            debugInfoStack.push(
//                    (depth == 1 ? "root " : "") + "object (class \"" +
//                            obj.getClass().getName() + "\", " + obj.toString() + ")");
//        }
//        try {
//            desc.checkSerialize();
//
//            bout.writeByte(TC_OBJECT);
//            writeClassDesc(desc, false);
//            handles.assign(unshared ? null : obj);
//            if (desc.isExternalizable() && !desc.isProxy()) {
//                writeExternalData((Externalizable) obj);
//            } else {
//                writeSerialData(obj, desc);
//            }
//        } finally {
//            if (extendedDebugInfo) {
//                debugInfoStack.pop();
//            }
//        }
//    }

//    /**
//     * Writes externalizable data of given object by invoking its
//     * writeExternal() method.
//     */
//    private void writeExternalData(Externalizable obj) throws IOException {
//        PutFieldImpl oldPut = curPut;
//        curPut = null;
//
//        if (extendedDebugInfo) {
//            debugInfoStack.push("writeExternal data");
//        }
//        java.io.SerialCallbackContext oldContext = curContext;
//        try {
//            curContext = null;
//            if (protocol == PROTOCOL_VERSION_1) {
//                obj.writeExternal(this);
//            } else {
//                bout.setBlockDataMode(true);
//                obj.writeExternal(this);
//                bout.setBlockDataMode(false);
//                bout.writeByte(TC_ENDBLOCKDATA);
//            }
//        } finally {
//            curContext = oldContext;
//            if (extendedDebugInfo) {
//                debugInfoStack.pop();
//            }
//        }
//
//        curPut = oldPut;
//    }

//    /**
//     * Fetches and writes values of serializable fields of given object to
//     * stream.  The given class descriptor specifies which field values to
//     * write, and in which order they should be written.
//     */
//    private void defaultWriteFields(Object object, ClassDescriptor desc) throws IOException {
//        Class<?> descriptorClass = desc.getDescribedClass();
//        if (descriptorClass != null && object != null && !descriptorClass.isInstance(object)) {
//            throw new ClassCastException();
//        }
//
//        desc.checkDefaultSerialize();
//
//        int primDataSize = desc.getPrimDataSize();
//        if (primVals == null || primVals.length < primDataSize) {
//            primVals = new byte[primDataSize];
//        }
//        desc.getPrimFieldValues(object, primVals);
//        bout.write(primVals, 0, primDataSize, false);
//
//        ObjectStreamField[] fields = desc.getFields(false);
//        Object[] objVals = new Object[desc.getNumObjFields()];
//        int numPrimFields = fields.length - objVals.length;
//        desc.getObjFieldValues(object, objVals);
//        for (int i = 0; i < objVals.length; i++) {
//            if (extendedDebugInfo) {
//                debugInfoStack.push(
//                        "field (class \"" + desc.getName() + "\", name: \"" +
//                                fields[numPrimFields + i].getName() + "\", type: \"" +
//                                fields[numPrimFields + i].getType() + "\")");
//            }
//            try {
//                writeObject0(objVals[i],
//                        fields[numPrimFields + i].isUnshared());
//            } finally {
//                if (extendedDebugInfo) {
//                    debugInfoStack.pop();
//                }
//            }
//        }
//    }

//    /**
//     * Attempts to write to stream fatal IOException that has caused
//     * serialization to abort.
//     */
//    private void writeFatalException(IOException ex) throws IOException {
//        /*
//         * Note: the serialization specification states that if a second
//         * IOException occurs while attempting to serialize the original fatal
//         * exception to the stream, then a StreamCorruptedException should be
//         * thrown (section 2.1).
//         */
//        clear();
//        try {
//            writeObject0(ex, false);
//            clear();
//        } catch (IOException e) {
//            throw new StreamCorruptedException("IOException while attempting to serialize exception");
//        }
//    }

//    /**
//     * A value container object for holding the field values defined by the supplied class descriptor.
//     * This separates the marshalling of serializable instance variables from the serializability
//     * discovery process allowing caching of the latter's output.
//     * The JSONObjectOutputStream can then just use the information cached in the class descriptor to build
//     * an InstanceFields container each time a matching class instance is encountered while traversing the object graph.
//     */
//    private class InstanceFields {
//
//        /** class descriptor describing serializable fields */
//        private final ClassDescriptor desc;
//        /** primitive field values */
//        private final Field[] primitives;
//        /** object field values */
//        private final Field[] objects;
//
//        InstanceFields(ClassDescriptor desc) {
//            this.desc = desc;
//            primitives = new Field[0];
//            objects = new Field[0];
//        }
//
//        @Deprecated
//        public void write(ObjectOutput out) throws IOException {
//            throw new UnsupportedOperationException();
//        }
//
//        /**
//         * Writes buffered primitive data and object fields to stream.
//         */
//        void writeFields() throws IOException {
//            for (Field field : primitives) {
//
//            }
//
//            JSONObjectStreamField[] fields = desc.getFields();
//            int numPrimFields = fields.length - objects.length;
//            for (int i = 0; i < objects.length; i++) {
//                writeObject0(objects[i], fields[numPrimFields + i].isUnshared());
//            }
//        }
//
//    }

//    /**
//     * Clears internal data structures.
//     */
//    private void clear() {
//        subs.clear();
//        handles.clear();
//    }


    @Override
    public void writeUnshared(Object obj) throws IOException {
        super.writeUnshared(obj);
    }

    @Override
    public void defaultWriteObject() throws IOException {
        super.defaultWriteObject();
    }

    @Override
    public PutField putFields() throws IOException {
        return super.putFields();
    }

    @Override
    public void writeFields() throws IOException {
        super.writeFields();
    }

    /**
     * Writes a byte. This method will block until the byte is actually
     * written.
     *
     * @param   val the byte to be written to the stream
     * @throws  IOException If an I/O error has occurred.
     */
    @Override
    public void write(int val) throws IOException {
        super.write(val);
    }

    /**
     * Writes an array of bytes. This method will block until the bytes are
     * actually written.
     *
     * @param   buf the data to be written
     * @throws  IOException If an I/O error has occurred.
     */
    @Override
    public void write(byte[] buf) throws IOException {
        super.write(buf);
    }

    /**
     * Writes a sub array of bytes.
     *
     * @param   buf the data to be written
     * @param   off the start offset in the data
     * @param   len the number of bytes that are written
     * @throws  IOException If an I/O error has occurred.
     */
    @Override
    public void write(byte[] buf, int off, int len) throws IOException {
        super.write(buf, off, len);
    }

    /**
     * Flushes the stream. This will write any buffered output bytes and flush
     * through to the underlying stream.
     *
     * @throws  IOException If an I/O error has occurred.
     */
    @Override
    public void flush() throws IOException {
        super.flush();
    }

    /**
     * Closes the stream. This method must be called to release any resources
     * associated with the stream.
     *
     * @throws  IOException If an I/O error has occurred.
     */
    @Override
    public void close() throws IOException {
        super.close();
    }

    /**
     * Writes a boolean.
     *
     * @param   val the boolean to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    @Override
    public void writeBoolean(boolean val) throws IOException {
        super.writeBoolean(val);
    }

    /**
     * Writes an 8 bit byte.
     *
     * @param   val the byte value to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    @Override
    public void writeByte(int val) throws IOException {
        super.writeByte(val);
    }

    /**
     * Writes a 16 bit short.
     *
     * @param   val the short value to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    @Override
    public void writeShort(int val) throws IOException {
        super.writeShort(val);
    }

    /**
     * Writes a 16 bit char.
     *
     * @param   val the char value to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    @Override
    public void writeChar(int val) throws IOException {
        super.writeChar(val);
    }

    /**
     * Writes a 32 bit int.
     *
     * @param   val the integer value to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    @Override
    public void writeInt(int val) throws IOException {
        super.writeInt(val);
    }

    /**
     * Writes a 32 bit float.
     *
     * @param   val the float value to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    @Override
    public void writeLong(long val) throws IOException {
        super.writeLong(val);
    }

    /**
     * Writes a 32 bit float.
     *
     * @param   val the float value to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    @Override
    public void writeFloat(float val) throws IOException {
        super.writeFloat(val);
    }

    /**
     * Writes a 64 bit double.
     *
     * @param   val the double value to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    @Override
    public void writeDouble(double val) throws IOException {
        super.writeDouble(val);
    }

    /**
     * Writes a String as a sequence of bytes.
     *
     * @param   str the String of bytes to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    @Override
    public void writeBytes(String str) throws IOException {
        super.writeBytes(str);
    }

    /**
     * Writes a String as a sequence of chars.
     *
     * @param   str the String of chars to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    @Override
    public void writeChars(String str) throws IOException {
        super.writeChars(str);
    }

    /**
     * Primitive data write of this String in
     * <a href="DataInput.html#modified-utf-8">modified UTF-8</a>
     * format.  Note that there is a
     * significant difference between writing a String into the stream as
     * primitive data or as an Object. A String instance written by writeObject
     * is written into the stream as a String initially. Future writeObject()
     * calls write references to the string into the stream.
     *
     * @param   str the String to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    @Override
    public void writeUTF(String str) throws IOException {
        super.writeUTF(str);
    }
}
