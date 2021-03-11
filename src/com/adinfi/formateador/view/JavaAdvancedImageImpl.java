package com.adinfi.formateador.view;

import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.io.FileInputStream;
import javax.imageio.ImageIO;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedImageAdapter;
import javax.media.jai.RenderedOp;
import javax.media.jai.TileCache;
import javax.swing.Icon;
import com.adinfi.formateador.view.IconJAI;

/**
 *
 * @author guillermo
 */
 
    

public class JavaAdvancedImageImpl {

    private static final Integer SEPIA_INTENSITY = 30;
    private static final long cacheSize = 32 * 1024 * 1024L;
    private static JavaAdvancedImageImpl photoCaptureJAI = null;

    /**
     *
     */
    protected JavaAdvancedImageImpl() {
        TileCache cache = JAI.getDefaultInstance().getTileCache();
        cache.setMemoryCapacity(cacheSize);
    }

    /**
     *
     * @return
     */
    public synchronized static JavaAdvancedImageImpl getInstance() {
        if (photoCaptureJAI == null) {
            photoCaptureJAI = new JavaAdvancedImageImpl();
        }
        return photoCaptureJAI;
    }

    /**
     *
     * @param ipParam
     * @param pImage
     * @return
     */
    public PlanarImage adjustBrightness(int ipParam, PlanarImage pImage) {
        PlanarImage mImage = pImage;
        //Create parameter block
        ParameterBlock mPB = new ParameterBlock();
        //Add image source to parameter block
        mPB.addSource(mImage);

        byte lutData[];
        lutData = new byte[256];
        for (int j = 0; j < 256; j++) {
            lutData[j] = clampByte(j + ipParam);
        }

        LookupTableJAI mLookup = new LookupTableJAI(lutData);

        mPB.add(mLookup);

        RenderedOp mRenOp;

        // Process the brightness/ darkness
        mRenOp = JAI.create("lookup", mPB, null);
        //mRenOp = JAI.create("piecewise", mPB, null);
        return PlanarImage.wrapRenderedImage(mRenOp);//.getAsBufferedImage();
    }

    /**
     *
     * @param ipParam
     * @param image
     * @return
     */
    public BufferedImage adjustBrightness(int ipParam, BufferedImage image) {
        //PlanarImage mImage = pImage;
        //Create parameter block
        ParameterBlock mPB = new ParameterBlock();
        //Add image source to parameter block
        mPB.addSource(image);

        byte lutData[];
        lutData = new byte[256];
        for (int j = 0; j < 256; j++) {
            lutData[j] = clampByte(j + ipParam);
        }

        LookupTableJAI mLookup = new LookupTableJAI(lutData);

        mPB.add(mLookup);

        RenderedOp mRenOp;

        // Process the brightness/ darkness
        mRenOp = JAI.create("lookup", mPB, null);
        //mRenOp = JAI.create("piecewise", mPB, null);
        return mRenOp.getAsBufferedImage();//.getAsBufferedImage();
    }

    /**
     *
     * @param contrastValue
     * @param pImage
     * @return
     */
    public PlanarImage adjustContrast(int contrastValue, PlanarImage pImage) {
        int numberOfBands = pImage.getSampleModel().getNumBands();
        float[][][] bp = new float[numberOfBands][2][];
        for (int b = 0; b < numberOfBands; b++) {
            bp[b][0] = new float[]{0.0F, 32.0F, 64.0F, 255.0F};
            bp[b][1] = new float[]{0.0F, 0.0F, (float) contrastValue, 255.0F};
        }
        RenderedOp contrastRenOp = JAI.create("piecewise", pImage, bp);
        return PlanarImage.wrapRenderedImage(contrastRenOp);
    }

    /**
     *
     * @param contrastValue
     * @param image
     * @return
     */
    public BufferedImage adjustContrast(int contrastValue, BufferedImage image) {
        int numberOfBands = image.getSampleModel().getNumBands();
        float[][][] bp = new float[numberOfBands][2][];
        for (int b = 0; b < numberOfBands; b++) {
            bp[b][0] = new float[]{0.0F, 32.0F, 64.0F, 255.0F};
            bp[b][1] = new float[]{0.0F, 0.0F, (float) contrastValue, 255.0F};
        }
        RenderedOp contrastRenOp = JAI.create("piecewise", image, bp);
        return contrastRenOp.getAsBufferedImage();
    }

    /**
     *
     * @param image
     * @return
     */
    public PlanarImage colorToGray(PlanarImage image) {
        double matrix[][] = {{0.114D, 0.587D, 0.299D, 0.0D}};
        if (image.getSampleModel().getNumBands() != 3) {
            throw new IllegalArgumentException("Image # bands <> 3");
        }
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(matrix);
        return (PlanarImage) (JAI.create("bandcombine", pb, null));
    }

    /**
     *
     * @param image
     * @return
     */
    public BufferedImage colorToGray(BufferedImage image) {
        double matrix[][] = {{0.114D, 0.587D, 0.299D, 0.0D}};
        if (image.getSampleModel().getNumBands() != 3) {
            throw new IllegalArgumentException("Image # bands <> 3");
        }
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(matrix);
        return (JAI.create("bandcombine", pb, null)).getAsBufferedImage();
    }

    /**
     *
     * @param image
     * @return
     */
    public BufferedImage colorToSepia(BufferedImage image) {
        int sepiaDepth = 20;
        WritableRaster raster = image.getRaster();
        int pixels[] = new int[image.getWidth() * image.getHeight() * 3];
        raster.getPixels(0, 0, image.getWidth(), image.getHeight(), pixels);
        for (int i = 0, r, g, b, gry; i < pixels.length; i += 3) {
            r = pixels[i];
            g = pixels[i + 1];
            b = pixels[i + 2];
            gry = (r + g + b) / 3;
            r = g = b = gry;
            r += (sepiaDepth * 2);
            g += sepiaDepth;
            if (r > 255) {
                r = 255;
            }
            if (g > 255) {
                g = 255;
            }
            if (b > 255) {
                b = 255;
            }
            //Darken blue color to increase sepia effect
            b -= SEPIA_INTENSITY;
            if (b < 0) {
                b = 0;
            }
            if (b > 255) {
                b = 255;
            }
            pixels[i] = r;
            pixels[i + 1] = g;
            pixels[i + 2] = b;
        }
        raster.setPixels(0, 0, image.getWidth(), image.getHeight(), pixels);
        return image;
    }

    private byte clampByte(int i) {
        if (i > 255) {
            return -1;
        }
        if (i < 0) {
            return 0;
        } else {
            return (byte) i;
        }
    }

    /**
     *
     * @param source
     * @return
     */
    public Icon makeIcon(PlanarImage source) {
        float scale = 190.0F / Math.max(source.getWidth(),
                source.getHeight());
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        renderingHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        renderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        ParameterBlock pb = new ParameterBlock();
        pb.addSource(source);
        pb.add(scale); //x
        pb.add(scale); //y
        pb.add(0.0F); //x
        pb.add(0.0F); //y
        pb.add(Interpolation.getInstance(Interpolation.INTERP_BICUBIC_2)); //the interpolation
        PlanarImage scaled = JAI.create("scale", pb, renderingHints);
        return new IconJAI(scaled);
    }

    /**
     *
     * @param scale
     * @param source
     * @return
     */
    public PlanarImage resizeImage(float scale, PlanarImage source) {
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        renderingHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        renderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        ParameterBlock pb = new ParameterBlock();
        pb.addSource(source);
        pb.add(scale); //x
        pb.add(scale); //y
        pb.add(0.0F); //x
        pb.add(0.0F); //y
        pb.add(Interpolation.getInstance(Interpolation.INTERP_BICUBIC_2)); //the interpolation
        PlanarImage scaled = JAI.create("scale", pb, renderingHints);
        return scaled;
    }

    /**
     *
     * @param image
     * @return
     */
    public BufferedImage convertAWTImage(Image image) {
        RenderedImageAdapter renderedImageAdapter = null;
        try {
            RenderedImage renderedImage = JAI.create("AWTImage", image);
            ParameterBlock pb = new ParameterBlock();
            pb.addSource(renderedImage);
            pb.add(DataBuffer.TYPE_BYTE);
            renderedImage = JAI.create("format", pb);
            renderedImageAdapter = new RenderedImageAdapter(renderedImage);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return renderedImageAdapter == null ? null : renderedImageAdapter.getAsBufferedImage();
    }

    /**
     *
     * @param originalImage
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     * @throws IllegalArgumentException
     */
    public BufferedImage cropImage(BufferedImage originalImage, int x, int y, int w, int h)
            throws IllegalArgumentException {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(originalImage);
        pb.add((float) x);
        pb.add((float) y);
        pb.add((float) w);
        pb.add((float) h);
        RenderedImage ri = JAI.create("crop", pb);
        RenderedImageAdapter ria = new RenderedImageAdapter(ri);
        return ria.getAsBufferedImage();
    }

    /**
     *
     * @param fis
     * @return
     */
    public RenderedImageAdapter convertToRenderedImageAdapter(FileInputStream fis) {
        RenderedImageAdapter ria = null;
        RenderedImage ri = null;
        try {
            ri = ImageIO.read(fis);
            ria = new RenderedImageAdapter(ri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ria;
    }

}
