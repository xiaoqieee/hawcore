package com.banzhiyan.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class ImageScale {
    public ImageScale() {
    }

    public static File scale(File sourceImageFile, int width, int height) throws IOException {
        String sourceImageFilePath = sourceImageFile.getCanonicalPath();
        String extension = FilenameUtils.getExtension(sourceImageFilePath);
        String targetImageFilePath = sourceImageFilePath + "_" + width + "x" + height + "." + extension;
        File targetImageFile = new File(targetImageFilePath);
        scale(sourceImageFile, width, height, targetImageFile);
        return targetImageFile;
    }

    public static void scale(File sourceImageFile, int width, int height, File targetImageFile) throws IOException {
        String extension = FilenameUtils.getExtension(sourceImageFile.getName());
        BufferedImage scaledImage = scaleImage(sourceImageFile, width, height);
        ImageIO.write(scaledImage, extension, targetImageFile);
    }

    private static BufferedImage scaleImage(File sourceImageFile, int width, int height) throws IOException {
        byte[] sourceImageFileContent = readAsBytes(sourceImageFile);
        BufferedImage originalImage = null;
        if(ArrayUtils.EMPTY_BYTE_ARRAY == sourceImageFileContent) {
            originalImage = ImageIO.read(sourceImageFile);
        } else {
            ByteArrayInputStream sourceImageFileInputStream = new ByteArrayInputStream(sourceImageFileContent);
            originalImage = ImageIO.read(sourceImageFileInputStream);
        }

        int type = originalImage.getType() == 0?4:originalImage.getType();
        BufferedImage scaledImage = new BufferedImage(width, height, type);
        Graphics2D g = scaledImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, (ImageObserver)null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return scaledImage;
    }

    private static byte[] readAsBytes(File sourceFile) {
        try {
            return FileUtils.readFileToByteArray(sourceFile);
        } catch (IOException var2) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
    }
}

