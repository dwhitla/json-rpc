package net.flitech.jsonrpc.io;

/**
 */
public class ClassDescriptor {

//    private static final long serialVersionUID = 1L;
//    private static final ObjectStreamField[] serialPersistentFields = NO_FIELDS;
//
//    private final String name;
//    private final boolean isProxy;
//    private final boolean isEnum;
//    private final boolean isSerializable;
//    private final boolean isExternalizable;
//    private final Long suid;
//
//    private final ObjectStreamField[] fields;
//    private final Constructor ctor;
//    private Class<?> clazz;
//    private ClassDescriptor superclassDescriptor;
//
//
//    public Class<?> getDescribedClass() {
//        return clazz;
//    }
//
//    public void writeNonProxy(JSONObjectOutputStream jsonObjectOutputStream) {
//
//    }
//
//    /**
//     * Placeholder used in lookup tables for an entry in the process of being initialized.
//     * Callers which receive an EntryFuture belonging to another thread as the result of a lookup should call the get()
//     * method of the EntryFuture; this will return the actual entry once it is ready for use and has been set().
//     */
//    private static class EntryFuture {
//
//        private static final Object UNSET = new Object();
//        private final Thread owner = Thread.currentThread();
//        private Object entry = UNSET;
//
//        /**
//         * Attempts to set the value contained by this EntryFuture.
//         * If the EntryFuture's value has not been set already, then the value is saved,
//         * any callers blocked in the get() method are notified, and true is returned.
//         * If the value has already been set, then no saving or notification occurs, and false is returned.
//         */
//        synchronized boolean set(Object entry) {
//            if (this.entry == UNSET) {
//                this.entry = entry;
//                notifyAll();
//                return true;
//            }
//            return false;
//        }
//
//        /**
//         * Returns the value contained by this EntryFuture, blocking if necessary until a value is set.
//         */
//        synchronized Object get() {
//            boolean interrupted = false;
//            while (entry == UNSET) {
//                try {
//                    wait();
//                } catch (InterruptedException ex) {
//                    interrupted = true;
//                }
//            }
//            if (interrupted) {
//                AccessController.doPrivileged(
//                        (PrivilegedAction<Void>) () -> {
//                            Thread.currentThread().interrupt();
//                            return null;
//                        }
//                );
//            }
//            return entry;
//        }
//
//        /**
//         * Returns the thread that created this EntryFuture.
//         */
//        Thread getOwner() {
//            return owner;
//        }
//    }
//
//    /**
//     *  Weak key for Class objects.
//     *
//     **/
//    private static class WeakClassKey extends WeakReference<Class<?>> {
//
//        /**
//         * saved value of the referent's identity hash code, to maintain
//         * a consistent hash code after the referent has been cleared
//         */
//        private final int hash;
//
//        /**
//         * Create a new WeakClassKey to the given Class, registered with a queue.
//         */
//        WeakClassKey(Class<?> clazz, ReferenceQueue<Class<?>> refQueue) {
//            super(clazz, refQueue);
//            hash = System.identityHashCode(clazz);
//        }
//
//        /**
//         * Returns the identity hash code of the original referent.
//         */
//        public int hashCode() {
//            return hash;
//        }
//
//        /**
//         * Returns true if the given object is this identical WeakClassKey instance,
//         * or, if this object's referent has not been cleared, if the given object is another WeakClassKey
//         * instance with the identical non-null referent as this one.
//         */
//        public boolean equals(Object obj) {
//            if (obj == this) {
//                return true;
//            }
//
//            if (obj instanceof WeakClassKey) {
//                Object referent = get();
//                return (referent != null) && (referent == ((WeakClassKey) obj).get());
//            } else {
//                return false;
//            }
//        }
//    }
//
//
//    /**
//     * FieldReflector cache lookup key.
//     * Keys are considered equal if they refer to the same class and equivalent field formats.
//     */
//    private static class FieldReflectorKey extends WeakReference<Class<?>> {
//
//        private final String signature;
//        private final int hash;
//        private final boolean nullClass;
//
//        FieldReflectorKey(Class<?> clazz, ObjectStreamField[] fields, ReferenceQueue<Class<?>> queue) {
//            super(clazz, queue);
//            nullClass = (clazz == null);
//            StringBuilder builder = new StringBuilder();
//            for (ObjectStreamField field : fields) {
//                builder.append(field.getName()).append(field.getSignature());
//            }
//            signature = builder.toString();
//            hash = System.identityHashCode(clazz) + signature.hashCode();
//        }
//
//        public int hashCode() {
//            return hash;
//        }
//
//        public boolean equals(Object obj) {
//            if (obj == this) {
//                return true;
//            }
//
//            if (obj instanceof FieldReflectorKey) {
//                FieldReflectorKey other = (FieldReflectorKey) obj;
//                Class<?> referent;
//                return (nullClass ? other.nullClass
//                        : ((referent = get()) != null) &&
//                        (referent == other.get())) &&
//                        signature.equals(other.signature);
//            } else {
//                return false;
//            }
//        }
//    }
//    /** cache mapping local classes -> descriptors */
//    private static final ConcurrentMap<WeakClassKey, Reference<?>> CLASS_DESCRIPTORS = new ConcurrentHashMap<>();
//    /** cache mapping field group/local desc pairs -> field reflectors */
//    private static final ConcurrentMap<FieldReflectorKey, Reference<?>> FIELD_REFLECTORS = new ConcurrentHashMap<>();
//    /** queue for WeakReferences to local classes */
//    private static final ReferenceQueue<Class<?>> DESCRIPTOR_REFERENCE_QUEUE = new ReferenceQueue<>();
//    /** queue for WeakReferences to field reflectors keys */
//    private static final ReferenceQueue<Class<?>> REFLECTOR_REFERENCE_QUEUE = new ReferenceQueue<>();
//
//
//    /**
//     * Creates local class descriptor representing given class.
//     */
//    private ClassDescriptor(final Class<?> clazz) {
//        this.clazz = clazz;
//        name = clazz.getName();
//        isProxy = Proxy.isProxyClass(clazz);
//        isEnum = Enum.class.isAssignableFrom(clazz);
//        isSerializable = Serializable.class.isAssignableFrom(clazz);
//        isExternalizable = Externalizable.class.isAssignableFrom(clazz);
//
//        Class<?> superclass = clazz.getSuperclass();
//        superclassDescriptor = (superclass == null) ? null : lookup(superclass, false);
////        localDesc = this;
//
//        if (isSerializable) {
//            AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
//
//                if (isEnum) {
//                    suid = 0L;
//                    fields = NO_FIELDS;
//                    return null;
//                }
//                if (clazz.isArray()) {
//                    fields = NO_FIELDS;
//                    return null;
//                }
//
//                suid = getDeclaredSUID(clazz);
//                try {
//                    fields = getSerialFields(clazz);
//                    computeFieldOffsets();
//                } catch (InvalidClassException e) {
//                    serializeEx = deserializeEx =
//                            new ObjectStreamClass.ExceptionInfo(e.classname, e.getMessage());
//                    fields = NO_FIELDS;
//                }
//
//                if (externalizable) {
//                    ctor = getExternalizableConstructor(clazz);
//                } else {
//                    ctor = getSerializableConstructor(clazz);
//                    writeObjectMethod = getPrivateMethod(clazz, "writeObject", new Class<?>[] { ObjectOutputStream.class }, Void.TYPE);
//                    readObjectMethod = getPrivateMethod(clazz, "readObject", new Class<?>[] { ObjectInputStream.class }, Void.TYPE);
//                    readObjectNoDataMethod = getPrivateMethod(clazz, "readObjectNoData", null, Void.TYPE);
//                    hasWriteObjectData = (writeObjectMethod != null);
//                }
//                writeReplaceMethod = getInheritableMethod(clazz, "writeReplace", null, Object.class);
//                readResolveMethod = getInheritableMethod(clazz, "readResolve", null, Object.class);
//                return null;
//            });
//        } else {
//            suid = Long.valueOf(0);
//            fields = NO_FIELDS;
//        }
//
//        try {
//            fieldRefl = getReflector(fields, this);
//        } catch (InvalidClassException ex) {
//            // field mismatches impossible when matching local fields vs. self
//            throw new InternalError(ex);
//        }
//
//        if (deserializeEx == null) {
//            if (isEnum) {
//                deserializeEx = new ObjectStreamClass.ExceptionInfo(name, "enum type");
//            } else if (ctor == null) {
//                deserializeEx = new ObjectStreamClass.ExceptionInfo(name, "no valid constructor");
//            }
//        }
//        for (int i = 0; i < fields.length; i++) {
//            if (fields[i].getField() == null) {
//                defaultSerializeEx = new ObjectStreamClass.ExceptionInfo(
//                        name, "unmatched serializable field(s) declared");
//            }
//        }
//        initialized = true;
//    }
//
//
//    /**
//     * Returns serializable fields of given class as defined explicitly by a "serialPersistentFields" field,
//     * or null if no appropriate "serialPersistentFields" field is defined.  Serializable fields backed
//     * by an actual field of the class are represented by ObjectStreamFields with corresponding non-null
//     * Field objects.
//     * Throws InvalidClassException if the declared serializable fields are invalid
//     * e.g., if multiple fields share the same name.
//     */
//    private static ObjectStreamField[] getDeclaredSerialFields(Class<?> clazz) throws InvalidClassException {
//        ObjectStreamField[] serialPersistentFields = null;
//        try {
//            Field declaredField = clazz.getDeclaredField("serialPersistentFields");
//            int mask = Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL;
//            if ((declaredField.getModifiers() & mask) == mask) {
//                declaredField.setAccessible(true);
//                serialPersistentFields = (ObjectStreamField[]) declaredField.get(null);
//            }
//        } catch (Exception ex) {
//        }
//        if (serialPersistentFields == null) {
//            return null;
//        } else if (serialPersistentFields.length == 0) {
//            return NO_FIELDS;
//        }
//
//        ObjectStreamField[] boundFields =
//                new ObjectStreamField[serialPersistentFields.length];
//        Set<String> fieldNames = new HashSet<>(serialPersistentFields.length);
//
//        for (int i = 0; i < serialPersistentFields.length; i++) {
//            ObjectStreamField spf = serialPersistentFields[i];
//
//            String fname = spf.getName();
//            if (fieldNames.contains(fname)) {
//                throw new InvalidClassException(
//                        "multiple serializable fields named " + fname);
//            }
//            fieldNames.add(fname);
//
//            try {
//                Field f = clazz.getDeclaredField(fname);
//                if ((f.getType() == spf.getType()) &&
//                        ((f.getModifiers() & Modifier.STATIC) == 0))
//                {
//                    boundFields[i] =
//                            new ObjectStreamField(f, spf.isUnshared(), true);
//                }
//            } catch (NoSuchFieldException ex) {
//            }
//            if (boundFields[i] == null) {
//                boundFields[i] = new ObjectStreamField(
//                        fname, spf.getType(), spf.isUnshared());
//            }
//        }
//        return boundFields;
//    }
//
//    /**
//     * Returns array of ObjectStreamFields corresponding to all non-static
//     * non-transient fields declared by given class.  Each ObjectStreamField
//     * contains a Field object for the field it represents.  If no default
//     * serializable fields exist, NO_FIELDS is returned.
//     */
//    private static ObjectStreamField[] getDefaultSerialFields(Class<?> cl) {
//        Field[] clFields = cl.getDeclaredFields();
//        ArrayList<ObjectStreamField> list = new ArrayList<>();
//        int mask = Modifier.STATIC | Modifier.TRANSIENT;
//
//        for (int i = 0; i < clFields.length; i++) {
//            if ((clFields[i].getModifiers() & mask) == 0) {
//                list.add(new ObjectStreamField(clFields[i], false, true));
//            }
//        }
//        int size = list.size();
//        return (size == 0) ? NO_FIELDS :
//                list.toArray(new ObjectStreamField[size]);
//    }
//
//    /**
//     * Returns explicit serial version UID value declared by given class, or
//     * null if none.
//     */
//    private static Long getDeclaredSUID(Class<?> cl) {
//        try {
//            Field f = cl.getDeclaredField("serialVersionUID");
//            int mask = Modifier.STATIC | Modifier.FINAL;
//            if ((f.getModifiers() & mask) == mask) {
//                f.setAccessible(true);
//                return Long.valueOf(f.getLong(null));
//            }
//        } catch (Exception ex) {
//        }
//        return null;
//    }
//
//    /**
//     * Computes the default serial version UID value for the given class.
//     */
//    private static long computeDefaultSUID(Class<?> cl) {
//        if (!Serializable.class.isAssignableFrom(cl) || Proxy.isProxyClass(cl))
//        {
//            return 0L;
//        }
//
//        try {
//            ByteArrayOutputStream bout = new ByteArrayOutputStream();
//            DataOutputStream dout = new DataOutputStream(bout);
//
//            dout.writeUTF(cl.getName());
//
//            int classMods = cl.getModifiers() &
//                    (Modifier.PUBLIC | Modifier.FINAL |
//                            Modifier.INTERFACE | Modifier.ABSTRACT);
//
//            /*
//             * compensate for javac bug in which ABSTRACT bit was set for an
//             * interface only if the interface declared methods
//             */
//            Method[] methods = cl.getDeclaredMethods();
//            if ((classMods & Modifier.INTERFACE) != 0) {
//                classMods = (methods.length > 0) ?
//                        (classMods | Modifier.ABSTRACT) :
//                        (classMods & ~Modifier.ABSTRACT);
//            }
//            dout.writeInt(classMods);
//
//            if (!cl.isArray()) {
//                /*
//                 * compensate for change in 1.2FCS in which
//                 * Class.getInterfaces() was modified to return Cloneable and
//                 * Serializable for array classes.
//                 */
//                Class<?>[] interfaces = cl.getInterfaces();
//                String[] ifaceNames = new String[interfaces.length];
//                for (int i = 0; i < interfaces.length; i++) {
//                    ifaceNames[i] = interfaces[i].getName();
//                }
//                Arrays.sort(ifaceNames);
//                for (int i = 0; i < ifaceNames.length; i++) {
//                    dout.writeUTF(ifaceNames[i]);
//                }
//            }
//
//            Field[] fields = cl.getDeclaredFields();
//            ObjectStreamClass.MemberSignature[] fieldSigs = new ObjectStreamClass.MemberSignature[fields.length];
//            for (int i = 0; i < fields.length; i++) {
//                fieldSigs[i] = new ObjectStreamClass.MemberSignature(fields[i]);
//            }
//            Arrays.sort(fieldSigs, new Comparator<ObjectStreamClass.MemberSignature>() {
//                public int compare(ObjectStreamClass.MemberSignature ms1, ObjectStreamClass.MemberSignature ms2) {
//                    return ms1.name.compareTo(ms2.name);
//                }
//            });
//            for (int i = 0; i < fieldSigs.length; i++) {
//                ObjectStreamClass.MemberSignature sig = fieldSigs[i];
//                int mods = sig.member.getModifiers() &
//                        (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED |
//                                Modifier.STATIC | Modifier.FINAL | Modifier.VOLATILE |
//                                Modifier.TRANSIENT);
//                if (((mods & Modifier.PRIVATE) == 0) ||
//                        ((mods & (Modifier.STATIC | Modifier.TRANSIENT)) == 0))
//                {
//                    dout.writeUTF(sig.name);
//                    dout.writeInt(mods);
//                    dout.writeUTF(sig.signature);
//                }
//            }
//
//            if (hasStaticInitializer(cl)) {
//                dout.writeUTF("<clinit>");
//                dout.writeInt(Modifier.STATIC);
//                dout.writeUTF("()V");
//            }
//
//            Constructor<?>[] cons = cl.getDeclaredConstructors();
//            ObjectStreamClass.MemberSignature[] consSigs = new ObjectStreamClass.MemberSignature[cons.length];
//            for (int i = 0; i < cons.length; i++) {
//                consSigs[i] = new ObjectStreamClass.MemberSignature(cons[i]);
//            }
//            Arrays.sort(consSigs, new Comparator<ObjectStreamClass.MemberSignature>() {
//                public int compare(ObjectStreamClass.MemberSignature ms1, ObjectStreamClass.MemberSignature ms2) {
//                    return ms1.signature.compareTo(ms2.signature);
//                }
//            });
//            for (int i = 0; i < consSigs.length; i++) {
//                ObjectStreamClass.MemberSignature sig = consSigs[i];
//                int mods = sig.member.getModifiers() &
//                        (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED |
//                                Modifier.STATIC | Modifier.FINAL |
//                                Modifier.SYNCHRONIZED | Modifier.NATIVE |
//                                Modifier.ABSTRACT | Modifier.STRICT);
//                if ((mods & Modifier.PRIVATE) == 0) {
//                    dout.writeUTF("<init>");
//                    dout.writeInt(mods);
//                    dout.writeUTF(sig.signature.replace('/', '.'));
//                }
//            }
//
//            ObjectStreamClass.MemberSignature[] methSigs = new ObjectStreamClass.MemberSignature[methods.length];
//            for (int i = 0; i < methods.length; i++) {
//                methSigs[i] = new ObjectStreamClass.MemberSignature(methods[i]);
//            }
//            Arrays.sort(methSigs, new Comparator<ObjectStreamClass.MemberSignature>() {
//                public int compare(ObjectStreamClass.MemberSignature ms1, ObjectStreamClass.MemberSignature ms2) {
//                    int comp = ms1.name.compareTo(ms2.name);
//                    if (comp == 0) {
//                        comp = ms1.signature.compareTo(ms2.signature);
//                    }
//                    return comp;
//                }
//            });
//            for (int i = 0; i < methSigs.length; i++) {
//                ObjectStreamClass.MemberSignature sig = methSigs[i];
//                int mods = sig.member.getModifiers() &
//                        (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED |
//                                Modifier.STATIC | Modifier.FINAL |
//                                Modifier.SYNCHRONIZED | Modifier.NATIVE |
//                                Modifier.ABSTRACT | Modifier.STRICT);
//                if ((mods & Modifier.PRIVATE) == 0) {
//                    dout.writeUTF(sig.name);
//                    dout.writeInt(mods);
//                    dout.writeUTF(sig.signature.replace('/', '.'));
//                }
//            }
//
//            dout.flush();
//
//            MessageDigest md = MessageDigest.getInstance("SHA");
//            byte[] hashBytes = md.digest(bout.toByteArray());
//            long hash = 0;
//            for (int i = Math.min(hashBytes.length, 8) - 1; i >= 0; i--) {
//                hash = (hash << 8) | (hashBytes[i] & 0xFF);
//            }
//            return hash;
//        } catch (IOException ex) {
//            throw new InternalError(ex);
//        } catch (NoSuchAlgorithmException ex) {
//            throw new SecurityException(ex.getMessage());
//        }
//    }
//
//
//    /**
//     * Returns public no-arg constructor of given class, or null if none found.
//     * Access checks are disabled on the returned constructor (if any), since
//     * the defining class may still be non-public.
//     */
//    private static Constructor<?> getExternalizableConstructor(Class<?> cl) {
//        try {
//            Constructor<?> cons = cl.getDeclaredConstructor((Class<?>[]) null);
//            cons.setAccessible(true);
//            return ((cons.getModifiers() & Modifier.PUBLIC) != 0) ?
//                    cons : null;
//        } catch (NoSuchMethodException ex) {
//            return null;
//        }
//    }
//
//    /**
//     * Returns subclass-accessible no-arg constructor of first non-serializable
//     * superclass, or null if none found.  Access checks are disabled on the
//     * returned constructor (if any).
//     */
//    private static Constructor<?> getSerializableConstructor(Class<?> cl) {
//        Class<?> initCl = cl;
//        while (Serializable.class.isAssignableFrom(initCl)) {
//            if ((initCl = initCl.getSuperclass()) == null) {
//                return null;
//            }
//        }
//        try {
//            Constructor<?> cons = initCl.getDeclaredConstructor((Class<?>[]) null);
//            int mods = cons.getModifiers();
//            if ((mods & Modifier.PRIVATE) != 0 ||
//                    ((mods & (Modifier.PUBLIC | Modifier.PROTECTED)) == 0 &&
//                            !packageEquals(cl, initCl)))
//            {
//                return null;
//            }
//            cons = reflFactory.newConstructorForSerialization(cl, cons);
//            cons.setAccessible(true);
//            return cons;
//        } catch (NoSuchMethodException ex) {
//            return null;
//        }
//    }
//
//
//
//
//    public ObjectStreamField[] getFields() {
//        return new ObjectStreamField[0];
//    }
//
//    /**
//     * Looks up and returns class descriptor for given class, or null if class is non-serializable and "onlySerializable" is set to
//     * false.
//     *
//     * @param   cl class to look up
//     * @param   onlySerializable if true, only return descriptors for serializable classes;
//     *                           else, return descriptors for any class
//     */
//    static ClassDescriptor lookup(Class<?> cl, boolean onlySerializable) {
//        if (onlySerializable && !Serializable.class.isAssignableFrom(cl)) {
//            return null;
//        }
//        Reference<?> ref = Caches.CLASS_DESCRIPTORS.get(key);
//        Object entry = null;
//        if (ref != null) {
//            entry = ref.get();
//        }
//        EntryFuture future = null;
//        if (entry == null) {
//            EntryFuture newEntry = new EntryFuture();
//            Reference<?> newRef = new SoftReference<>(newEntry);
//            do {
//                if (ref != null) {
//                    Caches.CLASS_DESCRIPTORS.remove(key, ref);
//                }
//                ref = Caches.CLASS_DESCRIPTORS.putIfAbsent(key, newRef);
//                if (ref != null) {
//                    entry = ref.get();
//                }
//            } while (ref != null && entry == null);
//            if (entry == null) {
//                future = newEntry;
//            }
//        }
//
//        if (entry instanceof ClassDescriptor) {  // check common case first
//            return (ClassDescriptor) entry;
//        }
//        if (entry instanceof EntryFuture) {
//            future = (EntryFuture) entry;
//            if (future.getOwner() == Thread.currentThread()) {
//                /*
//                 * Handle nested call situation described by 4803747: waiting
//                 * for future value to be set by a lookup() call further up the
//                 * stack will result in deadlock, so calculate and set the
//                 * future value here instead.
//                 */
//                entry = null;
//            } else {
//                entry = future.get();
//            }
//        }
//        if (entry == null) {
//            try {
//                entry = new ClassDescriptor(cl);
//            } catch (Throwable th) {
//                entry = th;
//            }
//            if (future.set(entry)) {
//                Caches.CLASS_DESCRIPTORS.put(key, new SoftReference<Object>(entry));
//            } else {
//                // nested lookup call already set future
//                entry = future.get();
//            }
//        }
//
//        if (entry instanceof ClassDescriptor) {
//            return (ClassDescriptor) entry;
//        } else if (entry instanceof RuntimeException) {
//            throw (RuntimeException) entry;
//        } else if (entry instanceof Error) {
//            throw (Error) entry;
//        } else {
//            throw new InternalError("unexpected entry: " + entry);
//        }
//    }
//
//    /**
//     * Find the descriptor for a class that can be serialized.  Creates an
//     * instance if one does not exist yet for class.
//     *
//     * @param   cl class for which to get the descriptor
//     * @return  the class descriptor for the specified class; or
//     *          null if the specified class does not implement java.io.Serializable
//     */
//    public static ClassDescriptor lookup(Class<?> cl) {
//        return lookup(cl, true);
//    }
//
//    /**
//     * Returns the descriptor for any class, regardless of whether it
//     * implements {@link Serializable}.
//     *
//     * @param        cl class for which to get the descriptor
//     * @return       the class descriptor for the specified class
//     * @since 1.6
//     */
//    public static ClassDescriptor lookupAny(Class<?> cl) {
//        return lookup(cl, false);
//    }
//
//    /**
//     * Returns the name of the class described by this descriptor.
//     * This method returns the name of the class in the format that
//     * is used by the {@link Class#getName} method.
//     *
//     * @return a string representing the name of the class
//     */
//    public String getName() {
//        return name;
//    }
//
//    public boolean hasWriteReplaceMethod() {
//        return false;
//    }
//
//    public Object invokeWriteReplace(Object obj) {
//        return null;
//    }
}
