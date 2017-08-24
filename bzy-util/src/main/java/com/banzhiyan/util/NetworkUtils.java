package com.banzhiyan.util;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * Created by xn025665 on 2017/8/24.
 */

public final class NetworkUtils {
    private static final String LOCALHOST = "127.0.0.1";
    private static final String ANYHOST = "0.0.0.0";
    public static final int MIN_PORT = 0;
    public static final int MAX_PORT = 65535;
    private static final InetAddress localhostAddress;
    private static final byte[] localMacAddressByteArray;
    private static final String localMacAddress;

    private NetworkUtils() {
    }

    public static long ipv4ToLong(String ipAddress) {
        long result = 0L;
        String[] ipAddressInArray = ipAddress.split("\\.");

        for(int i = 3; i >= 0; --i) {
            long ip = Long.parseLong(ipAddressInArray[3 - i]);
            result |= ip << i * 8;
        }

        return result;
    }

    public static String getLocalAddress() {
        Enumeration interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (Throwable var4) {
            return "127.0.0.1";
        }

        if(interfaces != null) {
            while(interfaces.hasMoreElements()) {
                NetworkInterface network = (NetworkInterface)interfaces.nextElement();
                Enumeration addresses = network.getInetAddresses();

                while(addresses.hasMoreElements()) {
                    InetAddress address = (InetAddress)addresses.nextElement();
                    if(!(address instanceof Inet6Address) && isValidAddress(address)) {
                        return address.getHostAddress();
                    }
                }
            }
        }

        return "127.0.0.1";
    }

    public static boolean isValidAddress(InetAddress address) {
        if(address != null && !address.isLoopbackAddress()) {
            String ip = address.getHostAddress();
            return ip != null && !"127.0.0.1".equals(ip) && !"0.0.0.0".equals(ip) && isValidIPv4(ip);
        } else {
            return false;
        }
    }

    public static boolean isValidIPv4(String address) {
        if(address != null && address.length() != 0) {
            int octets = 0;
            String temp = address + ".";
            int start = 0;

            while(true) {
                int pos;
                if(start < temp.length() && (pos = temp.indexOf(46, start)) > start) {
                    if(octets == 4) {
                        return false;
                    }

                    int octet;
                    try {
                        octet = Integer.parseInt(temp.substring(start, pos));
                    } catch (NumberFormatException var7) {
                        return false;
                    }

                    if(octet >= 0 && octet <= 255) {
                        start = pos + 1;
                        ++octets;
                        continue;
                    }

                    return false;
                }

                return octets == 4;
            }
        } else {
            return false;
        }
    }

    public static byte[] getLocalMacAddressByteArray() {
        return localMacAddressByteArray;
    }

    public static String getLocalMacAddress() {
        return localMacAddress;
    }

    public static InetAddress getLocalHost() {
        return localhostAddress;
    }

    public static String getLocalHostName() {
        return localhostAddress.getHostName();
    }

    public static String getLocalHostAddress() {
        return localhostAddress.getHostAddress();
    }

    public static boolean isAvailableLocalPort(int port) throws IllegalArgumentException {
        return isAvailablePort(getLocalHost(), port);
    }

    public static boolean isPortAvailable(String hostName, int port) {
        try {
            InetAddress hostAddress = InetAddress.getByName(hostName);
            return isAvailablePort(hostAddress, port);
        } catch (UnknownHostException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static boolean isAvailablePort(InetAddress hostAddress, int port) throws IllegalArgumentException {
        assertPort(port);
        boolean available = false;
        Socket socket = null;

        try {
            socket = new Socket(hostAddress, port);
            available = isAvailablePort(socket);
        } catch (IOException var8) {
            available = true;
        } finally {
            close(socket);
        }

        return available;
    }

    private static void close(Socket socket) {
        if(socket != null) {
            try {
                if(!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException var2) {
                ;
            }
        }

    }

    private static boolean isAvailablePort(Socket socket) {
        return socket != null && !socket.isBound();
    }

    public static void assertPort(int port) throws IllegalArgumentException {
        if(port < 0 || port > '\uffff') {
            throw new IllegalArgumentException("The range of argument value of port must between 0 AND 65535");
        }
    }

    static {
        try {
            localhostAddress = InetAddress.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localhostAddress);
            byte[] macAddressByteArray = null;
            if(networkInterface == null) {
                Enumeration enumeration = NetworkInterface.getNetworkInterfaces();

                while(enumeration.hasMoreElements()) {
                    networkInterface = (NetworkInterface)enumeration.nextElement();
                    if(networkInterface != null) {
                        macAddressByteArray = networkInterface.getHardwareAddress();
                        if(macAddressByteArray != null) {
                            break;
                        }
                    }
                }
            }

            localMacAddressByteArray = macAddressByteArray == null?new byte[0]:macAddressByteArray;
            StringBuilder macBuilder = new StringBuilder();
            if(localMacAddressByteArray.length >= 1) {
                for(int i = 0; i < localMacAddressByteArray.length; ++i) {
                    macBuilder.append(String.format("%02X%s", new Object[]{Byte.valueOf(localMacAddressByteArray[i]), i < localMacAddressByteArray.length - 1?"-":""}));
                }
            }

            localMacAddress = macBuilder.toString();
        } catch (Exception var4) {
            throw new InstantiationError(var4.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println(getLocalAddress());
    }
}

