package com.banzhiyan.util;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class DeepCopier {
    public DeepCopier() {
    }

    public static BigInteger checksum(Object sourceObject) {
        if(sourceObject == null) {
            return BigInteger.ZERO;
        } else {
            ResourceTracker tracker = new ResourceTracker("Deep Copier");

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                tracker.attach(oos);
                oos.writeObject(sourceObject);
                MessageDigest m = MessageDigest.getInstance("SHA-1");
                m.update(baos.toByteArray());
                BigInteger var5 = new BigInteger(1, m.digest());
                return var5;
            } catch (IOException var10) {
                throw new RuntimeException(var10);
            } catch (NoSuchAlgorithmException var11) {
                ;
            } finally {
                tracker.clear();
            }

            return BigInteger.ZERO;
        }
    }

    public static <T> T copy(T original) {
        T o = null;
        ResourceTracker tracker = new ResourceTracker("Deep Copier");

        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(byteOut);
            tracker.attach(oos);
            oos.writeObject(original);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(byteIn);
            tracker.attach(ois);

            try {
                o = (T)ois.readObject();
            } catch (ClassNotFoundException var12) {
                ;
            }
        } catch (IOException var13) {
            throw new RuntimeException(var13);
        } finally {
            tracker.clear();
        }

        return o;
    }

    public static <T> T copyParallel(T original) {
        try {
            PipedOutputStream outputStream = new PipedOutputStream();
            PipedInputStream inputStream = new PipedInputStream(outputStream);
            ObjectOutputStream ois = new ObjectOutputStream(outputStream);
            DeepCopier.Receiver receiver = new DeepCopier.Receiver(inputStream);

            try {
                ois.writeObject(original);
            } finally {
                ois.close();
            }

            return (T)receiver.getResult();
        } catch (IOException var9) {
            throw new RuntimeException(var9);
        }
    }

    private static class Receiver<T> extends Thread {
        private final InputStream inputStream;
        private volatile T result;
        private volatile Throwable throwable;

        public Receiver(InputStream inputStream) {
            this.inputStream = inputStream;
            this.start();
        }

        public void run() {
            try {
                ObjectInputStream ois = new ObjectInputStream(this.inputStream);

                try {
                    this.result = (T)ois.readObject();

                    try {
                        while(true) {
                            if(this.inputStream.read() != -1) {
                                continue;
                            }
                        }
                    } catch (IOException var7) {
                        ;
                    }
                } finally {
                    ois.close();
                }
            } catch (Throwable var9) {
                this.throwable = var9;
            }

        }

        public T getResult() throws IOException {
            try {
                this.join();
            } catch (InterruptedException var2) {
                throw new RuntimeException("Unexpected InterruptedException", var2);
            }

            if(this.throwable != null) {
                if(this.throwable instanceof ClassNotFoundException) {
                    ;
                }

                throw new RuntimeException(this.throwable);
            } else {
                return this.result;
            }
        }
    }
}

