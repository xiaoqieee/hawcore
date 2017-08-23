package com.banzhiyan.util;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by xn025665 on 2017/8/23.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static final int INDEX_NOT_FOUND = -1;
    public static final String SPACE = " ";
    public static final String EMPTY = "";

    public StringUtils() {
    }

    public static byte[] getBytesIso8859_1(String string) {
        return getBytesUnchecked(string, "ISO-8859-1");
    }

    public static byte[] getBytesUsAscii(String string) {
        return getBytesUnchecked(string, "US-ASCII");
    }

    public static byte[] getBytesUtf16(String string) {
        return getBytesUnchecked(string, "UTF-16");
    }

    public static byte[] getBytesUtf16Be(String string) {
        return getBytesUnchecked(string, "UTF-16BE");
    }

    public static byte[] getBytesUtf16Le(String string) {
        return getBytesUnchecked(string, "UTF-16LE");
    }

    public static byte[] getBytesUtf8(String string) {
        return getBytesUnchecked(string, "UTF-8");
    }

    public static byte[] getBytesUnchecked(String string, String charsetName) {
        if(string == null) {
            return null;
        } else {
            try {
                return string.getBytes(charsetName);
            } catch (UnsupportedEncodingException var3) {
                throw newIllegalStateException(charsetName, var3);
            }
        }
    }

    private static IllegalStateException newIllegalStateException(String charsetName, UnsupportedEncodingException e) {
        return new IllegalStateException(charsetName + ": " + e);
    }

    public static String newString(byte[] bytes, String charsetName) {
        if(bytes == null) {
            return null;
        } else {
            try {
                return new String(bytes, charsetName);
            } catch (UnsupportedEncodingException var3) {
                throw newIllegalStateException(charsetName, var3);
            }
        }
    }

    public static String newStringIso8859_1(byte[] bytes) {
        return newString(bytes, "ISO-8859-1");
    }

    public static String newStringUsAscii(byte[] bytes) {
        return newString(bytes, "US-ASCII");
    }

    public static String newStringUtf16(byte[] bytes) {
        return newString(bytes, "UTF-16");
    }

    public static String newStringUtf16Be(byte[] bytes) {
        return newString(bytes, "UTF-16BE");
    }

    public static String newStringUtf16Le(byte[] bytes) {
        return newString(bytes, "UTF-16LE");
    }

    public static String newStringUtf8(byte[] bytes) {
        return newString(bytes, "UTF-8");
    }

    public static String appendWithOSLineSeparator(List<String> str) {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        Iterator i$ = str.iterator();

        while(i$.hasNext()) {
            String s = (String)i$.next();
            sb.append(s);
            sb.append(lineSeparator);
        }

        return sb.toString();
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if(cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if(!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static String substringBetween(String str, String open, String close) {
        if(str != null && open != null && close != null) {
            int start = str.indexOf(open);
            if(start != -1) {
                int end = str.indexOf(close, start + open.length());
                if(end != -1) {
                    return str.substring(start + open.length(), end);
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static String trimWhitespace(String str) {
        return StringUtils.trimWhitespace(str);
    }

    public static String trimAllWhitespace(String str) {
        return StringUtils.trimAllWhitespace(str);
    }

    public static String trimLeadingWhitespace(String str) {
        return StringUtils.trimLeadingWhitespace(str);
    }

    public static String trimTrailingWhitespace(String str) {
        return StringUtils.trimTrailingWhitespace(str);
    }

    public static String trimLeadingCharacter(String str, char leadingCharacter) {
        return StringUtils.trimLeadingCharacter(str, leadingCharacter);
    }

    public static String trimTrailingCharacter(String str, char trailingCharacter) {
        return StringUtils.trimTrailingCharacter(str, trailingCharacter);
    }

    public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        return StringUtils.substringMatch(str, index, substring);
    }

    public static int countOccurrencesOf(String str, String sub) {
        return StringUtils.countOccurrencesOf(str, sub);
    }

    public static String delete(String inString, String pattern) {
        return StringUtils.delete(inString, pattern);
    }

    public static String deleteAny(String inString, String charsToDelete) {
        return StringUtils.deleteAny(inString, charsToDelete);
    }

    public static String quote(String str) {
        return StringUtils.quote(str);
    }

    public static Object quoteIfString(Object obj) {
        return StringUtils.quoteIfString(obj);
    }

    public static String unqualify(String qualifiedName) {
        return StringUtils.unqualify(qualifiedName);
    }

    public static String unqualify(String qualifiedName, char separator) {
        return StringUtils.unqualify(qualifiedName, separator);
    }

    public static String getFilename(String path) {
        return StringUtils.getFilename(path);
    }

    public static String getFilenameExtension(String path) {
        return StringUtils.getFilenameExtension(path);
    }

    public static String stripFilenameExtension(String path) {
        return StringUtils.stripFilenameExtension(path);
    }

    public static String applyRelativePath(String path, String relativePath) {
        return StringUtils.applyRelativePath(path, relativePath);
    }

    public static String cleanPath(String path) {
        return StringUtils.cleanPath(path);
    }

    public static boolean pathEquals(String path1, String path2) {
        return StringUtils.pathEquals(path1, path2);
    }

    public static Locale parseLocaleString(String localeString) {
        return StringUtils.parseLocaleString(localeString);
    }

    public static String toLanguageTag(Locale locale) {
        return StringUtils.toLanguageTag(locale);
    }

    public static TimeZone parseTimeZoneString(String timeZoneString) {
        return StringUtils.parseTimeZoneString(timeZoneString);
    }

    public static String[] addStringToArray(String[] array, String str) {
        return StringUtils.addStringToArray(array, str);
    }

    public static String[] concatenateStringArrays(String[] array1, String[] array2) {
        return StringUtils.concatenateStringArrays(array1, array2);
    }

    public static String[] mergeStringArrays(String[] array1, String[] array2) {
        return StringUtils.mergeStringArrays(array1, array2);
    }

    public static String[] sortStringArray(String[] array) {
        return StringUtils.sortStringArray(array);
    }

    public static String[] toStringArray(Collection<String> collection) {
        return StringUtils.toStringArray(collection);
    }

    public static String[] toStringArray(Enumeration<String> enumeration) {
        return StringUtils.toStringArray(enumeration);
    }

    public static String[] trimArrayElements(String[] array) {
        return StringUtils.trimArrayElements(array);
    }

    public static String[] removeDuplicateStrings(String[] array) {
        return StringUtils.removeDuplicateStrings(array);
    }

    public static String[] split(String toSplit, String delimiter) {
        return StringUtils.split(toSplit, delimiter);
    }

    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
        return StringUtils.splitArrayElementsIntoProperties(array, delimiter);
    }

    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {
        return StringUtils.splitArrayElementsIntoProperties(array, delimiter, charsToDelete);
    }

    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return StringUtils.tokenizeToStringArray(str, delimiters);
    }

    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        return StringUtils.tokenizeToStringArray(str, delimiters, trimTokens, ignoreEmptyTokens);
    }

    public static String[] delimitedListToStringArray(String str, String delimiter) {
        return StringUtils.delimitedListToStringArray(str, delimiter);
    }

    public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
        return StringUtils.delimitedListToStringArray(str, delimiter, charsToDelete);
    }

    public static String[] commaDelimitedListToStringArray(String str) {
        return StringUtils.commaDelimitedListToStringArray(str);
    }

    public static Set<String> commaDelimitedListToSet(String str) {
        return StringUtils.commaDelimitedListToSet(str);
    }

    public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
        return StringUtils.collectionToDelimitedString(coll, delim, prefix, suffix);
    }

    public static String collectionToDelimitedString(Collection<?> coll, String delim) {
        return StringUtils.collectionToDelimitedString(coll, delim);
    }

    public static String collectionToCommaDelimitedString(Collection<?> coll) {
        return StringUtils.collectionToCommaDelimitedString(coll);
    }

    public static String arrayToDelimitedString(Object[] arr, String delim) {
        return StringUtils.arrayToDelimitedString(arr, delim);
    }

    public static String arrayToCommaDelimitedString(Object[] arr) {
        return StringUtils.arrayToCommaDelimitedString(arr);
    }
}
