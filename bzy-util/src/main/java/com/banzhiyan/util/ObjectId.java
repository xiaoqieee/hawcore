package com.banzhiyan.util;

import com.banzhiyan.logging.Logger;
import com.banzhiyan.logging.LoggerFactory;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xn025665 on 2017/8/24.
 */

public final class ObjectId implements Comparable<ObjectId>, Serializable {
    private static final long serialVersionUID = -985349909068095474L;
    private static final Logger logger = LoggerFactory.getLogger(ObjectId.class);
    private static final int LOW_ORDER_THREE_BYTES = 16777215;
    private static final int MACHINE_IDENTIFIER;
    private static final short PROCESS_IDENTIFIER;
    private static final AtomicInteger NEXT_COUNTER = new AtomicInteger((new SecureRandom()).nextInt());
    private final int timestamp;
    private final int machineIdentifier;
    private final short processIdentifier;
    private final int counter;

    private static ObjectId get() {
        return new ObjectId();
    }

    public static String getIdentityId() {
        return DateUtils.format("yyMMdd", new Date()) + getId();
    }

    public static String getId() {
        return get().toHexString();
    }

    private static boolean isValid(String hexString) {
        if(hexString == null) {
            throw new IllegalArgumentException();
        } else {
            int len = hexString.length();
            if(len != 24) {
                return false;
            } else {
                for(int i = 0; i < len; ++i) {
                    char c = hexString.charAt(i);
                    if((c < 48 || c > 57) && (c < 97 || c > 102) && (c < 65 || c > 70)) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public static int getMachineIdentifier() {
        return MACHINE_IDENTIFIER;
    }

    public static int getProcessIdentifier() {
        return PROCESS_IDENTIFIER;
    }

    private static ObjectId createFromLegacyFormat(int time, int machine, int inc) {
        return new ObjectId(time, machine, inc);
    }

    private ObjectId() {
        this(new Date());
    }

    private ObjectId(Date date) {
        this(dateToTimestampSeconds(date), MACHINE_IDENTIFIER, PROCESS_IDENTIFIER, NEXT_COUNTER.getAndIncrement(), false);
    }

    private ObjectId(Date date, int counter) {
        this(date, MACHINE_IDENTIFIER, PROCESS_IDENTIFIER, counter);
    }

    private ObjectId(Date date, int machineIdentifier, short processIdentifier, int counter) {
        this(dateToTimestampSeconds(date), machineIdentifier, processIdentifier, counter);
    }

    private ObjectId(int timestamp, int machineIdentifier, short processIdentifier, int counter) {
        this(timestamp, machineIdentifier, processIdentifier, counter, true);
    }

    private ObjectId(int timestamp, int machineIdentifier, short processIdentifier, int counter, boolean checkCounter) {
        if((machineIdentifier & -16777216) != 0) {
            throw new IllegalArgumentException("The machine identifier must be between 0 and 16777215 (it must fit in three bytes).");
        } else if(checkCounter && (counter & -16777216) != 0) {
            throw new IllegalArgumentException("The counter must be between 0 and 16777215 (it must fit in three bytes).");
        } else {
            this.timestamp = timestamp;
            this.machineIdentifier = machineIdentifier;
            this.processIdentifier = processIdentifier;
            this.counter = counter & 16777215;
        }
    }

    private ObjectId(String hexString) {
        this(parseHexString(hexString));
    }

    private ObjectId(byte[] bytes) {
        if(bytes == null) {
            throw new IllegalArgumentException();
        } else if(bytes.length != 12) {
            throw new IllegalArgumentException("need 12 bytes");
        } else {
            this.timestamp = makeInt(bytes[0], bytes[1], bytes[2], bytes[3]);
            this.machineIdentifier = makeInt((byte)0, bytes[4], bytes[5], bytes[6]);
            this.processIdentifier = (short)makeInt((byte)0, (byte)0, bytes[7], bytes[8]);
            this.counter = makeInt((byte)0, bytes[9], bytes[10], bytes[11]);
        }
    }

    private ObjectId(int timestamp, int machineAndProcessIdentifier, int counter) {
        this(legacyToBytes(timestamp, machineAndProcessIdentifier, counter));
    }

    private static byte[] legacyToBytes(int timestamp, int machineAndProcessIdentifier, int counter) {
        byte[] bytes = new byte[]{int3(timestamp), int2(timestamp), int1(timestamp), int0(timestamp), int3(machineAndProcessIdentifier), int2(machineAndProcessIdentifier), int1(machineAndProcessIdentifier), int0(machineAndProcessIdentifier), int3(counter), int2(counter), int1(counter), int0(counter)};
        return bytes;
    }

    private byte[] toByteArray() {
        byte[] bytes = new byte[]{int3(this.timestamp), int2(this.timestamp), int1(this.timestamp), int0(this.timestamp), int2(this.machineIdentifier), int1(this.machineIdentifier), int0(this.machineIdentifier), short1(this.processIdentifier), short0(this.processIdentifier), int2(this.counter), int1(this.counter), int0(this.counter)};
        return bytes;
    }

    private String toHexString() {
        StringBuilder buf = new StringBuilder(24);
        byte[] arr$ = this.toByteArray();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            byte b = arr$[i$];
            buf.append(String.format("%02x", new Object[]{Integer.valueOf(b & 255)}));
        }

        return buf.toString();
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(o != null && this.getClass() == o.getClass()) {
            ObjectId objectId = (ObjectId)o;
            return this.counter != objectId.counter?false:(this.machineIdentifier != objectId.machineIdentifier?false:(this.processIdentifier != objectId.processIdentifier?false:this.timestamp == objectId.timestamp));
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.timestamp;
        result = 31 * result + this.machineIdentifier;
        result = 31 * result + this.processIdentifier;
        result = 31 * result + this.counter;
        return result;
    }

    public int compareTo(ObjectId other) {
        if(other == null) {
            throw new NullPointerException();
        } else {
            byte[] byteArray = this.toByteArray();
            byte[] otherByteArray = other.toByteArray();

            for(int i = 0; i < 12; ++i) {
                if(byteArray[i] != otherByteArray[i]) {
                    return (byteArray[i] & 255) < (otherByteArray[i] & 255)?-1:1;
                }
            }

            return 0;
        }
    }

    public String toString() {
        return this.toHexString();
    }

    private static int createMachineIdentifier() {
        int machinePiece;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration e = NetworkInterface.getNetworkInterfaces();

            while(e.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface)e.nextElement();
                sb.append(ni.toString());
                byte[] mac = ni.getHardwareAddress();
                if(mac != null) {
                    ByteBuffer bb = ByteBuffer.wrap(mac);

                    try {
                        sb.append(bb.getChar());
                        sb.append(bb.getChar());
                        sb.append(bb.getChar());
                    } catch (BufferUnderflowException var7) {
                        ;
                    }
                }
            }

            machinePiece = sb.toString().hashCode();
        } catch (Throwable var8) {
            machinePiece = (new SecureRandom()).nextInt();
            logger.error("Failed to get machine identifier from network interface, using random number instead", var8);
        }

        machinePiece &= 16777215;
        return machinePiece;
    }

    private static short createProcessIdentifier() {
        short processId;
        try {
            String processName = ManagementFactory.getRuntimeMXBean().getName();
            if(processName.contains("@")) {
                processId = (short)Integer.parseInt(processName.substring(0, processName.indexOf(64)));
            } else {
                processId = (short)ManagementFactory.getRuntimeMXBean().getName().hashCode();
            }
        } catch (Throwable var2) {
            processId = (short)(new SecureRandom()).nextInt();
            logger.error("Failed to get process identifier from JMX, using random number instead", var2);
        }

        return processId;
    }

    private static byte[] parseHexString(String s) {
        if(!isValid(s)) {
            throw new IllegalArgumentException("invalid hexadecimal representation of an ObjectId: [" + s + "]");
        } else {
            byte[] b = new byte[12];

            for(int i = 0; i < b.length; ++i) {
                b[i] = (byte)Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
            }

            return b;
        }
    }

    private static int dateToTimestampSeconds(Date time) {
        return (int)(time.getTime() / 1000L);
    }

    private static int makeInt(byte b3, byte b2, byte b1, byte b0) {
        return b3 << 24 | (b2 & 255) << 16 | (b1 & 255) << 8 | b0 & 255;
    }

    private static byte int3(int x) {
        return (byte)(x >> 24);
    }

    private static byte int2(int x) {
        return (byte)(x >> 16);
    }

    private static byte int1(int x) {
        return (byte)(x >> 8);
    }

    private static byte int0(int x) {
        return (byte)x;
    }

    private static byte short1(short x) {
        return (byte)(x >> 8);
    }

    private static byte short0(short x) {
        return (byte)x;
    }

    static {
        try {
            MACHINE_IDENTIFIER = createMachineIdentifier();
            PROCESS_IDENTIFIER = createProcessIdentifier();
        } catch (Exception var1) {
            throw new RuntimeException(var1);
        }
    }
}

