package com.banzhiyan.util;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;


/**
 * Created by xn025665 on 2017/8/24.
 */

public final class ZipUtils {
    private static final int BUFFER = 8192;

    public ZipUtils() {
    }

    public static byte[] doCompress(byte[] srcData, String zipFileName) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(8192);
        Throwable var3 = null;

        byte[] var7;
        try {
            ZipArchiveOutputStream out = new ZipArchiveOutputStream(bout);
            Throwable var5 = null;

            try {
                ZipArchiveEntry entry = new ZipArchiveEntry(zipFileName);
                entry.setSize((long)srcData.length);
                out.putArchiveEntry(entry);
                IOUtils.write(srcData, out);
                out.closeArchiveEntry();
                out.finish();
                var7 = bout.toByteArray();
            } catch (Throwable var30) {
                var5 = var30;
                throw var30;
            } finally {
                if(out != null) {
                    if(var5 != null) {
                        try {
                            out.close();
                        } catch (Throwable var29) {
                            var5.addSuppressed(var29);
                        }
                    } else {
                        out.close();
                    }
                }

            }
        } catch (Throwable var32) {
            var3 = var32;
            throw var32;
        } finally {
            if(bout != null) {
                if(var3 != null) {
                    try {
                        bout.close();
                    } catch (Throwable var28) {
                        var3.addSuppressed(var28);
                    }
                } else {
                    bout.close();
                }
            }

        }

        return var7;
    }

    public static byte[] doCompress(File srcFile, String zipFileName) throws IOException {
        if(srcFile != null && srcFile.exists() && !srcFile.isDirectory()) {
            InputStream in = new BufferedInputStream(new FileInputStream(srcFile), 8192);
            Throwable var3 = null;

            byte[] var9;
            try {
                ByteArrayOutputStream bout = new ByteArrayOutputStream(8192);
                Throwable var5 = null;

                try {
                    ZipArchiveOutputStream out = new ZipArchiveOutputStream(bout);
                    Throwable var7 = null;

                    try {
                        ZipArchiveEntry entry = new ZipArchiveEntry(zipFileName);
                        entry.setSize(srcFile.length());
                        out.putArchiveEntry(entry);
                        IOUtils.copy(in, out);
                        out.closeArchiveEntry();
                        out.finish();
                        var9 = bout.toByteArray();
                    } catch (Throwable var53) {
                        var7 = var53;
                        throw var53;
                    } finally {
                        if(out != null) {
                            if(var7 != null) {
                                try {
                                    out.close();
                                } catch (Throwable var52) {
                                    var7.addSuppressed(var52);
                                }
                            } else {
                                out.close();
                            }
                        }

                    }
                } catch (Throwable var55) {
                    var5 = var55;
                    throw var55;
                } finally {
                    if(bout != null) {
                        if(var5 != null) {
                            try {
                                bout.close();
                            } catch (Throwable var51) {
                                var5.addSuppressed(var51);
                            }
                        } else {
                            bout.close();
                        }
                    }

                }
            } catch (Throwable var57) {
                var3 = var57;
                throw var57;
            } finally {
                if(in != null) {
                    if(var3 != null) {
                        try {
                            in.close();
                        } catch (Throwable var50) {
                            var3.addSuppressed(var50);
                        }
                    } else {
                        in.close();
                    }
                }

            }

            return var9;
        } else {
            throw new IllegalArgumentException("srcFile[" + srcFile + "] must exist and cannot be a directory.");
        }
    }

    public static void doCompress(File srcFile, File destFile) throws IOException {
        if(srcFile != null && srcFile.exists() && !srcFile.isDirectory()) {
            if(destFile != null && (!destFile.exists() || !destFile.isDirectory())) {
                InputStream in = new BufferedInputStream(new FileInputStream(srcFile), 8192);
                Throwable var3 = null;

                try {
                    ZipArchiveOutputStream out = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(destFile), 8192));
                    Throwable var5 = null;

                    try {
                        if(!destFile.exists()) {
                            destFile.createNewFile();
                        }

                        ZipArchiveEntry entry = new ZipArchiveEntry(srcFile.getName());
                        entry.setSize(srcFile.length());
                        out.putArchiveEntry(entry);
                        IOUtils.copy(in, out);
                        out.closeArchiveEntry();
                        out.finish();
                    } catch (Throwable var28) {
                        var5 = var28;
                        throw var28;
                    } finally {
                        if(out != null) {
                            if(var5 != null) {
                                try {
                                    out.close();
                                } catch (Throwable var27) {
                                    var5.addSuppressed(var27);
                                }
                            } else {
                                out.close();
                            }
                        }

                    }
                } catch (Throwable var30) {
                    var3 = var30;
                    throw var30;
                } finally {
                    if(in != null) {
                        if(var3 != null) {
                            try {
                                in.close();
                            } catch (Throwable var26) {
                                var3.addSuppressed(var26);
                            }
                        } else {
                            in.close();
                        }
                    }

                }

            } else {
                throw new IllegalArgumentException("destFile[" + destFile == null?"null":destFile.getAbsolutePath() + "] cannot be empty and cannot be a directory.");
            }
        } else {
            throw new IllegalArgumentException("srcFile[" + srcFile + "] must exist and cannot be a directory.");
        }
    }

    public static void doDecompressToFile(InputStream src, File destFile) throws IOException {
        ZipArchiveInputStream in = new ZipArchiveInputStream(new BufferedInputStream(src, 8192));
        Throwable var3 = null;

        try {
            while(in.getNextZipEntry() != null) {
                OutputStream os = new BufferedOutputStream(new FileOutputStream(destFile), 8192);
                Throwable var5 = null;

                try {
                    IOUtils.copy(in, os);
                } catch (Throwable var28) {
                    var5 = var28;
                    throw var28;
                } finally {
                    if(os != null) {
                        if(var5 != null) {
                            try {
                                os.close();
                            } catch (Throwable var27) {
                                var5.addSuppressed(var27);
                            }
                        } else {
                            os.close();
                        }
                    }

                }
            }
        } catch (Throwable var30) {
            var3 = var30;
            throw var30;
        } finally {
            if(in != null) {
                if(var3 != null) {
                    try {
                        in.close();
                    } catch (Throwable var26) {
                        var3.addSuppressed(var26);
                    }
                } else {
                    in.close();
                }
            }

        }

    }

    public static void doDecompress(File srcFile, File destDir) throws IOException {
        if(srcFile != null && srcFile.exists() && !srcFile.isDirectory()) {
            ZipArchiveInputStream is = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(srcFile), 8192));
            Throwable var3 = null;

            try {
                ZipArchiveEntry entry = null;

                while((entry = is.getNextZipEntry()) != null) {
                    if(entry.isDirectory()) {
                        File directory = new File(destDir, entry.getName());
                        directory.mkdirs();
                    } else {
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(destDir, entry.getName())), 8192);
                        Throwable var6 = null;

                        try {
                            IOUtils.copy(is, os);
                        } catch (Throwable var29) {
                            var6 = var29;
                            throw var29;
                        } finally {
                            if(os != null) {
                                if(var6 != null) {
                                    try {
                                        os.close();
                                    } catch (Throwable var28) {
                                        var6.addSuppressed(var28);
                                    }
                                } else {
                                    os.close();
                                }
                            }

                        }
                    }
                }
            } catch (Throwable var31) {
                var3 = var31;
                throw var31;
            } finally {
                if(is != null) {
                    if(var3 != null) {
                        try {
                            is.close();
                        } catch (Throwable var27) {
                            var3.addSuppressed(var27);
                        }
                    } else {
                        is.close();
                    }
                }

            }

        } else {
            throw new IllegalArgumentException("srcFile[" + srcFile + "] must exist and cannot be a directory.");
        }
    }
}

