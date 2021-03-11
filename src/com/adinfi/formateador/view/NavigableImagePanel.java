package com.adinfi.formateador.view;

import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.Utilerias;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;



/**
 * <p>
 * <code>NavigableImagePanel</code> is a lightweight container displaying an
 * image that can be zoomed in and out and panned with ease and simplicity,
 * using an adaptive rendering for high quality display and satisfactory
 * performance. </p> <p> <h3>Image</h3> <p>An image is loaded either via a
 * constructor:</p>
 * <pre>
 * NavigableImagePanel panel = new NavigableImagePanel(image);
 * </pre> or using a setter:
 * <pre>
 * NavigableImagePanel panel = new NavigableImagePanel();
 * panel.setImage(image);
 * </pre> When an image is set, it is initially painted centered in the
 * component, at the largest possible size, fully visible, with its aspect ratio
 * is preserved. This is defined as 100% of the image size and its corresponding
 * zoom level is 1.0. </p> <h3>Zooming</h3> <p> Zooming can be controlled
 * interactively, using either the mouse scroll wheel (default) or the mouse two
 * buttons, or programmatically, allowing the programmer to implement other
 * custom zooming methods. If the mouse does not have a scroll wheel, set the
 * zooming device to mouse buttons:
 * <pre>
 * panel.setZoomDevice(ZoomDevice.MOUSE_BUTTON);
 * </pre> The left mouse button works as a toggle switch between zooming in and
 * zooming out modes, and the right button zooms an image by one increment
 * (default is 20%). You can change the zoom increment value by:
 * <pre>
 * panel.setZoomIncrement(newZoomIncrement);
 * </pre> If you intend to provide programmatic zoom control, set the zoom
 * device to none to disable both the mouse wheel and buttons for zooming
 * purposes:
 * <pre>
 * panel.setZoomDevice(ZoomDevice.NONE);
 * </pre> and use
 * <code>setZoom()</code> to change the zoom level. </p> <p> Zooming is always
 * around the point the mouse pointer is currently at, so that this point
 * (called a zooming center) remains stationary ensuring that the area of an
 * image we are zooming into does not disappear off the screen. The zooming
 * center stays at the same location on the screen and all other points move
 * radially away from it (when zooming in), or towards it (when zooming out).
 * For programmatically controlled zooming the zooming center is either
 * specified when
 * <code>setZoom()</code> is called:
 * <pre>
 * panel.setZoom(newZoomLevel, newZoomingCenter);
 * </pre> or assumed to be the point of an image which is the closest to the
 * center of the panel, if no zooming center is specified:
 * <pre>
 * panel.setZoom(newZoomLevel);
 * </pre> </p> <p> There are no lower or upper zoom level limits. </p>
 * <h3>Navigation</h3> <p><code>NavigableImagePanel</code> does not use scroll
 * bars for navigation, but relies on a navigation image located in the upper
 * left corner of the panel. The navigation image is a small replica of the
 * image displayed in the panel. When you click on any point of the navigation
 * image that part of the image is displayed in the panel, centered. The
 * navigation image can also be zoomed in the same way as the main image.</p>
 * <p>In order to adjust the position of an image in the panel, it can be
 * dragged with the mouse, using the left button. </p> <p>For programmatic image
 * navigation, disable the navigation image:
 * <pre>
 * panel.setNavigationImageEnabled(false)
 * </pre> and use
 * <code>getImageOrigin()</code> and
 * <code>setImageOrigin()</code> to move the image around the panel. </p>
 * <h3>Rendering</h3> <p><code>NavigableImagePanel</code> uses the Nearest
 * Neighbor interpolation for image rendering (default in Java). When the scaled
 * image becomes larger than the original image, the Bilinear interpolation is
 * applied, but only to the part of the image which is displayed in the panel.
 * This interpolation change threshold can be controlled by adjusting the value
 * of
 * <code>HIGH_QUALITY_RENDERING_SCALE_THRESHOLD</code>.</p>
 */
public final class NavigableImagePanel extends JPanel {

    /**
     * <p>Identifies a change to the zoom level.</p>
     */
    public static final String ZOOM_LEVEL_CHANGED_PROPERTY = "zoomLevel";
    /**
     * <p>Identifies a change to the zoom increment.</p>
     */
    public static final String ZOOM_INCREMENT_CHANGED_PROPERTY = "zoomIncrement";
    /**
     * <p>Identifies that the image in the panel has changed.</p>
     */
    public static final String IMAGE_CHANGED_PROPERTY = "image";
    private static final double SCREEN_NAV_IMAGE_FACTOR = 0.15; // 15% of panel's width
    private static final double NAV_IMAGE_FACTOR = 0.3; // 30% of panel's width
    private static final double HIGH_QUALITY_RENDERING_SCALE_THRESHOLD = 1.0;
    private static final Object INTERPOLATION_TYPE =
            RenderingHints.VALUE_INTERPOLATION_BILINEAR;
    private static final long serialVersionUID = 6493735558120797461L;
    private double zoomIncrement = 0.2;
    private double zoomFactor = 1.0 + zoomIncrement;
    private double navZoomFactor = 1.0 + zoomIncrement;
    private BufferedImage image;
    private BufferedImage subimage;
    private BufferedImage navigationImage;
    private int navImageWidth;
    private int navImageHeight;
    private double initialScale = 0.0;
    private double scale = 0.0;
    private double navScale = 0.0;
    private int originX = 0;
    private int originY = 0;
    private Point mousePosition;
    private Dimension previousPanelSize;
    private boolean navigationImageEnabled = true;
    private boolean highQualityRenderingEnabled = true;
    private WheelZoomDevice wheelZoomDevice = null;
    private ButtonZoomDevice buttonZoomDevice = null;
    private Rectangle clip = new Rectangle(150, 150);
    private int last_x = 0;
    private int last_y = 0;
    //private ImageClipper imageClipper = null;
    private Boolean pressOut = Boolean.FALSE;
    private boolean firstime = true;

    /**
     * <p>Defines zoom devices.</p>
     */
    public static class ZoomDevice {

        /**
         * <p>Identifies that the panel does not implement zooming, but the
         * component using the panel does (programmatic zooming method).</p>
         */
        public static final ZoomDevice NONE = new ZoomDevice("none");
        /**
         * <p>Identifies the left and right mouse buttons as the zooming
         * device.</p>
         */
        public static final ZoomDevice MOUSE_BUTTON = new ZoomDevice("mouseButton");
        /**
         * <p>Identifies the mouse scroll wheel as the zooming device.</p>
         */
        public static final ZoomDevice MOUSE_WHEEL = new ZoomDevice("mouseWheel");
        private String zoomDevice;

        private ZoomDevice(String zoomDevice) {
            this.zoomDevice = zoomDevice;
        }

        @Override
        public String toString() {
            return zoomDevice;
        }
    }

    //This class is required for high precision image coordinates translation.
    private static class Coords {

        private double x;
        private double y;

        private Coords(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public int getIntX() {
            return (int) Math.round(x);
        }

        public int getIntY() {
            return (int) Math.round(y);
        }

        @Override
        public String toString() {
            return "[Coords: x=" + x + ",y=" + y + "]";
        }
    }

    private class WheelZoomDevice implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            Point p = e.getPoint();
            boolean zoomIn = (e.getWheelRotation() < 0);

            if (isInNavigationImage(p)) {
                if (zoomIn) {
                    navZoomFactor = 1.0 + zoomIncrement;
                } else {
                    navZoomFactor = 1.0 - zoomIncrement;
                }
                zoomNavigationImage();
            } else if (isInImage(p)) {
                if (zoomIn) {
                    zoomFactor = 1.0 + zoomIncrement;
                } else {
                    zoomFactor = 1.0 - zoomIncrement;
                }
                zoomImage();
            }
        }
    }

    private class ButtonZoomDevice extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            Point p = e.getPoint();
            if (SwingUtilities.isRightMouseButton(e)) {
                if (isInNavigationImage(p)) {
                    navZoomFactor = 1.0 - zoomIncrement;
                    zoomNavigationImage();
                } else if (isInImage(p)) {
                    zoomFactor = 1.0 - zoomIncrement;
                    zoomImage();
                }
            } else {
                if (isInNavigationImage(p)) {
                    navZoomFactor = 1.0 + zoomIncrement;
                    zoomNavigationImage();
                } else if (isInImage(p)) {
                    zoomFactor = 1.0 + zoomIncrement;
                    zoomImage();
                }
            }
        }
    }

    /**
     * <p>Creates a new navigable image panel with the specified image and the
     * mouse scroll wheel as the zooming device.</p>
     */
    public NavigableImagePanel(BufferedImage image) throws IOException {
        this();
        setImage(image);
    }

    /**
     * <p>Creates a new navigable image panel with no default image and the
     * mouse scroll wheel as the zooming device.</p>
     */
    public NavigableImagePanel() {
        setOpaque(false);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (scale > 0.0) {
                    if (isFullImageInPanel()) {
                        centerImage();
                    } else if (isImageEdgeInPanel()) {
                        scaleOrigin();
                    }
                    if (isNavigationImageEnabled()) {
                        createNavigationImage();
                    }
                    repaint();
                }
                previousPanelSize = getSize();
            }
        });


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //se usara para clip
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (getClip() == null) return;
                    last_x = (getClip().x - e.getX());
                    last_y = (getClip().y - e.getY());

                    if (getClip().contains(e.getX(), e.getY())) {
                        updateLocation(e);
                    } else {
                        pressOut = Boolean.TRUE;

                        boolean b = isInNavigationImage(e.getPoint());
                        if (b) {
                            Point p = e.getPoint();
                            displayImageAt(p);
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (getClip() == null) return;
                //se usara para clip
                if (getClip().contains(e.getX(), e.getY())) {
                    updateLocation(e);
                } else {
                    pressOut = false;
                }
            }

            /*@Override
             public void mouseWheelMoved(MouseWheelEvent e) {
             //se usara para clip
             if (getClip().contains(e.getX(), e.getY())) {
             int notches = e.getWheelRotation();
             if (notches < 0) {
             //up incrementar tamano
             if ((getClip().width <= getImage().getWidth())
             & getClip().height <= getImage().getHeight()) {
             plus();
             }
             } else {
             //down disminuir tamano
             sustract();
             }
             }
             }*/
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //se usara para clip
                if (SwingUtilities.isLeftMouseButton(e)
                        && !isInNavigationImage(e.getPoint())) {
                    if (!pressOut) {
                        updateLocation(e);
                    } else {
                        Point p = e.getPoint();
                        moveImage(p);
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                //we need the mouse position so that after zooming
                //that position of the image is maintained
                mousePosition = e.getPoint();
            }
        });
        setZoomDevice(ZoomDevice.NONE);
        ImageClipperNavigablePanel.getInstance().setNavigableImagePanel(this);

    }

    public void updateLocation(MouseEvent e) {
        //agregar validacion para que no se mueva fuera de los limites
        int x = last_x + e.getX();
        int y = last_y + e.getY();
        //tolerancia -5
        if ((x > -5) && (y > -5)) {
            setLocationClip(x, y);
        }
    }

    /*public void plus() {
     Point p = getClip().getLocation();
     int x = p.x;
     int y = p.y;
     Dimension dim2 = getClip().getSize();
     int w2 = dim2.width + 20;

     int h2 = dim2.height + 20;

     setLocationClip(x - 10, y - 10);
     setSizeClip(w2, h2);
     }

     public void sustract() {
     Point p = getClip().getLocation();
     int x = p.x;
     int y = p.y;
     Dimension dim = getClip().getSize();
     int w2 = dim.width - 20;
     int h2 = dim.height - 20;

     setLocationClip(x + 10, y + 10);
     setSizeClip(w2, h2);
     }*/
    /**
     * @return the subimage
     */
    public BufferedImage getSubimage() {
        return subimage;
    }

    /**
     * @param subimage the subimage to set
     */
    public void setSubimage(BufferedImage subimage) {
        this.subimage = subimage;
    }

    public void cropImage() throws OutOfMemoryError {
        getClip();
    }

    /**
     * @return the scale
     */
    public double getScale() {
        return scale;
    }

    /**
     * @param scale the scale to set
     */
    public void setScale(double scale) {
        this.scale = scale;
        repaint();
    }

    public Rectangle getClip() {
        return clip;
    }

    public void setClip(Point p1, Point p2) {
        clip.setFrameFromDiagonal(p1, p2);
        repaint();
    }

    
    
    public void setClipSize(int w, int h) {
        
        if (w == -1 & h == -1) {
            clip = null;
            return;
        }
        
        int wmedium = 0;
        int hmedium = 0;
        if (getSubimage() != null) {
            wmedium = (getSubimage().getWidth() / 2);
            hmedium = (getSubimage().getHeight() / 2);
        } else {
            Rectangle rect = getImageClipBounds();
            if (rect == null || rect.width == 0 || rect.height == 0) { // no part of image is displayed in the panel
                wmedium = (getImage().getWidth() / 2);
                hmedium = (getImage().getHeight() / 2);
            } else {
                wmedium = getImage().getSubimage(rect.x, rect.y, rect.width, rect.height).getWidth() / 2;
                hmedium = getImage().getSubimage(rect.x, rect.y, rect.width, rect.height).getHeight() / 2;
            }
        }
        
        clip.setSize(w, h);
        clip.setLocation((wmedium - ((int) w / 2)),
                (hmedium - ((int) h / 2)));
        repaint();
    }
    
    public void setSizeClipByScale(int width, int height) {
        if (width <= 0 || height <= 0) {
            clip = null;
            return;
        }
        final int panel_width = this.getWidth();
        final int panel_height = this.getHeight();
        clip.setSize(width, height);
        clip.setLocation(((panel_width / 2) - ((int) width / 2)), ((panel_height / 2) - ((int) height / 2)));
        repaint();
    }

    public void setSizeClip(int width, int height) {
        clip.setSize(width, height);
        repaint();
    }

    public void setLocationClip(int x, int y) {
        if (getClip() == null) return;
        clip.setLocation(x, y);
        repaint();
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @return the firstime
     */
    public boolean isFirstime() {
        return firstime;
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    /**
     * @param firstime the firstime to set
     */
    public void setFirstime(boolean firstime) {
        this.firstime = firstime;
    }

    private void addWheelZoomDevice() {
        if (wheelZoomDevice == null) {
            wheelZoomDevice = new WheelZoomDevice();
            addMouseWheelListener(wheelZoomDevice);
        }
    }

    private void addButtonZoomDevice() {
        if (buttonZoomDevice == null) {
            buttonZoomDevice = new ButtonZoomDevice();
            addMouseListener(buttonZoomDevice);
        }
    }

    private void removeWheelZoomDevice() {
        if (wheelZoomDevice != null) {
            removeMouseWheelListener(wheelZoomDevice);
            wheelZoomDevice = null;
        }
    }

    private void removeButtonZoomDevice() {
        if (buttonZoomDevice != null) {
            removeMouseListener(buttonZoomDevice);
            buttonZoomDevice = null;
        }
    }

    /**
     * <p>Sets a new zoom device.</p>
     *
     * @param newZoomDevice specifies the type of a new zoom device.
     */
    public void setZoomDevice(ZoomDevice newZoomDevice) {
        if (newZoomDevice == ZoomDevice.NONE) {
            removeWheelZoomDevice();
            removeButtonZoomDevice();
        } else if (newZoomDevice == ZoomDevice.MOUSE_BUTTON) {
            removeWheelZoomDevice();
            addButtonZoomDevice();
        } else if (newZoomDevice == ZoomDevice.MOUSE_WHEEL) {
            removeButtonZoomDevice();
            addWheelZoomDevice();
        }
    }

    /**
     * <p>Gets the current zoom device.</p>
     */
    public ZoomDevice getZoomDevice() {
        if (buttonZoomDevice != null) {
            return ZoomDevice.MOUSE_BUTTON;
        } else if (wheelZoomDevice != null) {
            return ZoomDevice.MOUSE_WHEEL;
        } else {
            return ZoomDevice.NONE;
        }
    }

    //Called from paintComponent() when a new image is set.
    private void initializeParams() {
        double xScale = (double) getWidth() / getImage().getWidth();
        double yScale = (double) getHeight() / getImage().getHeight();
        initialScale = Math.min(xScale, yScale);
        scale = initialScale;

        //An image is initially centered
        centerImage();
        if (isNavigationImageEnabled()) {
            createNavigationImage();
        }
    }

    //Centers the current image in the panel.
    private void centerImage() {
        originX = (int) (getWidth() - getScreenImageWidth()) / 2;
        originY = (int) (getHeight() - getScreenImageHeight()) / 2;
    }

    //Creates and renders the navigation image in the upper let corner of the panel.
    private void createNavigationImage() {
        //We keep the original navigation image larger than initially
        //displayed to allow for zooming into it without pixellation effect.
        navImageWidth = (int) (getWidth() * NAV_IMAGE_FACTOR);
        navImageHeight = navImageWidth * getImage().getHeight() / getImage().getWidth();
        if (navImageWidth == 0 || navImageHeight == 0) {
            return;
        }
        int scrNavImageWidth = (int) (getWidth() * SCREEN_NAV_IMAGE_FACTOR);
        navScale = (double) scrNavImageWidth / navImageWidth;
        navigationImage = new BufferedImage(navImageWidth, navImageHeight,
                getImage().getType());
        Graphics g = navigationImage.getGraphics();
        g.drawImage(getImage(), 0, 0, navImageWidth, navImageHeight, null);
    }

    /**
     * <p>Sets an image for display in the panel.</p>
     *
     * @param image an image to be set in the panel
     */
    public void setImage(BufferedImage image) {
        BufferedImage oldImage = this.getImage();
        this.image = image;
        
        //Reset scale so that initializeParameters() is called in paintComponent()
        //for the new image.
        scale = 0.0;
        firePropertyChange(IMAGE_CHANGED_PROPERTY, (Image) oldImage, (Image) this.image);
        repaint();
    }

    /**
     * <p>Tests whether an image uses the standard RGB color space.</p>
     */
    public static boolean isStandardRGBImage(BufferedImage bImage) {
        return bImage.getColorModel().getColorSpace().isCS_sRGB();
    }

    //Converts this panel's coordinates into the original image coordinates
    private Coords panelToImageCoords(Point p) {
        return new Coords((p.x - originX) / scale, (p.y - originY) / scale);
    }

    //Converts the original image coordinates into this panel's coordinates
    private Coords imageToPanelCoords(Coords p) {
        return new Coords((p.x * scale) + originX, (p.y * scale) + originY);
    }

    //Converts the navigation image coordinates into the zoomed image coordinates
    private Point navToZoomedImageCoords(Point p) {
        int x = p.x * getScreenImageWidth() / getScreenNavImageWidth();
        int y = p.y * getScreenImageHeight() / getScreenNavImageHeight();
        return new Point(x, y);
    }

    //The user clicked within the navigation image and this part of the image
    //is displayed in the panel.
    //The clicked point of the image is centered in the panel.
    private void displayImageAt(Point p) {
        Point scrImagePoint = navToZoomedImageCoords(p);
        originX = -(scrImagePoint.x - getWidth() / 2);
        originY = -(scrImagePoint.y - getHeight() / 2);
        repaint();
    }

    //Tests whether a given point in the panel falls within the image boundaries.
    private boolean isInImage(Point p) {
        Coords coords = panelToImageCoords(p);
        int x = coords.getIntX();
        int y = coords.getIntY();
        return (x >= 0 && x < getImage().getWidth() && y >= 0 && y < getImage().getHeight());
    }

    //Tests whether a given point in the panel falls within the navigation image
    //boundaries.
    private boolean isInNavigationImage(Point p) {
        return (isNavigationImageEnabled() && p.x < getScreenNavImageWidth()
                && p.y < getScreenNavImageHeight());
    }

    /*private void isInClip(Point p) {
        
     }*/
    //Used when the image is resized.
    private boolean isImageEdgeInPanel() {
        if (previousPanelSize == null) {
            return false;
        }

        return (originX > 0 && originX < previousPanelSize.width
                || originY > 0 && originY < previousPanelSize.height);
    }

    //Tests whether the image is displayed in its entirety in the panel.
    private boolean isFullImageInPanel() {
        return (originX >= 0 && (originX + getScreenImageWidth()) < getWidth()
                && originY >= 0 && (originY + getScreenImageHeight()) < getHeight());
    }

    /**
     * <p>Indicates whether the high quality rendering feature is enabled.</p>
     *
     * @return true if high quality rendering is enabled, false otherwise.
     */
    public boolean isHighQualityRenderingEnabled() {
        return highQualityRenderingEnabled;
    }

    /**
     * <p>Enables/disables high quality rendering.</p>
     *
     * @param enabled enables/disables high quality rendering
     */
    public void setHighQualityRenderingEnabled(boolean enabled) {
        highQualityRenderingEnabled = enabled;
    }

    //High quality rendering kicks in when when a scaled image is larger
    //than the original image. In other words,
    //when image decimation stops and interpolation starts.
    private boolean isHighQualityRendering() {
        return (highQualityRenderingEnabled
                && scale > HIGH_QUALITY_RENDERING_SCALE_THRESHOLD);
    }

    /**
     * <p>Indicates whether navigation image is enabled.<p>
     *
     * @return true when navigation image is enabled, false otherwise.
     */
    public boolean isNavigationImageEnabled() {
        return navigationImageEnabled;
    }

    /**
     * <p>Enables/disables navigation with the navigation image.</p>
     * <p>Navigation image should be disabled when custom, programmatic
     * navigation is implemented.</p>
     *
     * @param enabled true when navigation image is enabled, false otherwise.
     */
    public void setNavigationImageEnabled(boolean enabled) {
        navigationImageEnabled = enabled;
        repaint();
    }

    //Used when the panel is resized
    private void scaleOrigin() {
        originX = originX * getWidth() / previousPanelSize.width;
        originY = originY * getHeight() / previousPanelSize.height;
        repaint();
    }

    //Converts the specified zoom level	to scale.
    private double zoomToScale(double zoom) {
        return initialScale * zoom;
    }

    /**
     * <p>Gets the current zoom level.</p>
     *
     * @return the current zoom level
     */
    public double getZoom() {
        return scale / initialScale;
    }

    /**
     * <p>Sets the zoom level used to display the image.</p> <p>This method is
     * used in programmatic zooming. The zooming center is the point of the
     * image closest to the center of the panel. After a new zoom level is set
     * the image is repainted.</p>
     *
     * @param newZoom the zoom level used to display this panel's image.
     */
    public void setZoom(double newZoom) {
        Point zoomingCenter = new Point(getWidth() / 2, getHeight() / 2);
        setZoom(newZoom, zoomingCenter);
    }

    /**
     * <p>Sets the zoom level used to display the image, and the zooming center,
     * around which zooming is done.</p> <p>This method is used in programmatic
     * zooming. After a new zoom level is set the image is repainted.</p>
     *
     * @param newZoom the zoom level used to display this panel's image.
     */
    public void setZoom(double newZoom, Point zoomingCenter) {
        Coords imageP = panelToImageCoords(zoomingCenter);
        if (imageP.x < 0.0) {
            imageP.x = 0.0;
        }
        if (imageP.y < 0.0) {
            imageP.y = 0.0;
        }
        if (imageP.x >= getImage().getWidth()) {
            imageP.x = getImage().getWidth() - 1.0;
        }
        if (imageP.y >= getImage().getHeight()) {
            imageP.y = getImage().getHeight() - 1.0;
        }

        Coords correctedP = imageToPanelCoords(imageP);
        double oldZoom = getZoom();
        scale = zoomToScale(newZoom);
        Coords panelP = imageToPanelCoords(imageP);

        originX += (correctedP.getIntX() - (int) panelP.x);
        originY += (correctedP.getIntY() - (int) panelP.y);

        firePropertyChange(ZOOM_LEVEL_CHANGED_PROPERTY, Double.valueOf(oldZoom),
                Double.valueOf(getZoom()));

        repaint();
    }

    /**
     * <p>Gets the current zoom increment.</p>
     *
     * @return the current zoom increment
     */
    public double getZoomIncrement() {
        return zoomIncrement;
    }

    /**
     * <p>Sets a new zoom increment value.</p>
     *
     * @param newZoomIncrement new zoom increment value
     */
    public void setZoomIncrement(double newZoomIncrement) {
        double oldZoomIncrement = zoomIncrement;
        zoomIncrement = newZoomIncrement;
        firePropertyChange(ZOOM_INCREMENT_CHANGED_PROPERTY,
                Double.valueOf(oldZoomIncrement), Double.valueOf(zoomIncrement));
    }

    //Zooms an image in the panel by repainting it at the new zoom level.
    //The current mouse position is the zooming center.
    private void zoomImage() {
        Coords imageP = panelToImageCoords(mousePosition);
        double oldZoom = getZoom();
        scale *= zoomFactor;
        Coords panelP = imageToPanelCoords(imageP);

        originX += (mousePosition.x - (int) panelP.x);
        originY += (mousePosition.y - (int) panelP.y);

        firePropertyChange(ZOOM_LEVEL_CHANGED_PROPERTY, Double.valueOf(oldZoom),
                Double.valueOf(getZoom()));

        repaint();
    }

    //Zooms the navigation image
    private void zoomNavigationImage() {
        navScale *= navZoomFactor;
        repaint();
    }

    /**
     * <p>Gets the image origin.</p> <p>Image origin is defined as the upper,
     * left corner of the image in the panel's coordinate system.</p>
     *
     * @return the point of the upper, left corner of the image in the panel's
     * coordinates system.
     */
    public Point getImageOrigin() {
        return new Point(originX, originY);
    }

    /**
     * <p>Sets the image origin.</p> <p>Image origin is defined as the upper,
     * left corner of the image in the panel's coordinate system. After a new
     * origin is set, the image is repainted. This method is used for
     * programmatic image navigation.</p>
     *
     * @param x the x coordinate of the new image origin
     * @param y the y coordinate of the new image origin
     */
    public void setImageOrigin(int x, int y) {
        setImageOrigin(new Point(x, y));
    }

    /**
     * <p>Sets the image origin.</p> <p>Image origin is defined as the upper,
     * left corner of the image in the panel's coordinate system. After a new
     * origin is set, the image is repainted. This method is used for
     * programmatic image navigation.</p>
     *
     * @param newOrigin the value of a new image origin
     */
    public void setImageOrigin(Point newOrigin) {
        originX = newOrigin.x;
        originY = newOrigin.y;
        repaint();
    }

    //Moves te image (by dragging with the mouse) to a new mouse position p.
    private void moveImage(Point p) {
        int xDelta = p.x - mousePosition.x;
        int yDelta = p.y - mousePosition.y;
        originX += xDelta;
        originY += yDelta;
        mousePosition = p;
        repaint();
    }

    //Gets the bounds of the image area currently displayed in the panel (in image
    //coordinates).
    private Rectangle getImageClipBounds() {
        Coords startCoords = panelToImageCoords(new Point(0, 0));
        Coords endCoords = panelToImageCoords(new Point(getWidth() - 1, getHeight() - 1));
        int panelX1 = startCoords.getIntX();
        int panelY1 = startCoords.getIntY();
        int panelX2 = endCoords.getIntX();
        int panelY2 = endCoords.getIntY();
        //No intersection?
        if (panelX1 >= getImage().getWidth() || panelX2 < 0 || panelY1 >= getImage().getHeight() || panelY2 < 0) {
            return null;
        }

        int x1 = (panelX1 < 0) ? 0 : panelX1;
        int y1 = (panelY1 < 0) ? 0 : panelY1;
        int x2 = (panelX2 >= getImage().getWidth()) ? getImage().getWidth() - 1 : panelX2;
        int y2 = (panelY2 >= getImage().getHeight()) ? getImage().getHeight() - 1 : panelY2;
        return new Rectangle(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
    }

    /**
     * Paints the panel and its image at the current zoom level, location, and
     * interpolation method dependent on the image scale.</p>
     *
     * @param g the <code>Graphics</code> context for painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Paints the background

        if (getImage() == null) {
            return;
        }

        if (scale == 0.0) {
            initializeParams();
        }

        if (isHighQualityRendering()) {
            Rectangle rect = getImageClipBounds();
            if (rect == null || rect.width == 0 || rect.height == 0) { // no part of image is displayed in the panel
                return;
            }
            setSubimage(getImage().getSubimage(rect.x, rect.y, rect.width, rect.height));
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, INTERPOLATION_TYPE);
            g2.drawImage(getSubimage(), Math.max(0, originX),
                    Math.max(0, originY),
                    Math.min((int) (getSubimage().getWidth() * scale), getWidth()), Math.min((int) (getSubimage().getHeight() * scale), getHeight()), null);
        } else {

            g.drawImage(getImage(), originX, originY, getScreenImageWidth(),
                    getScreenImageHeight(), null);
        }

        //Draw navigation image
        if (isNavigationImageEnabled() & !isFullImageInPanel()) {
            g.drawImage(navigationImage, 0, 0, getScreenNavImageWidth(),
                    getScreenNavImageHeight(), null);
            drawZoomAreaOutline(g);
        }
        drawCropAreaOutline(g);
    }

    //Paints a white outline over the navigation image indicating
    //the area of the image currently displayed in the panel.
    private void drawZoomAreaOutline(Graphics g) {
        if (isFullImageInPanel()) {
            return;
        }

        int x = -originX * getScreenNavImageWidth() / getScreenImageWidth();
        int y = -originY * getScreenNavImageHeight() / getScreenImageHeight();
        int width = getWidth() * getScreenNavImageWidth() / getScreenImageWidth();
        int height = getHeight() * getScreenNavImageHeight() / getScreenImageHeight();
        g.setColor(Color.white);
        g.drawRect(x, y, width, height);
    }

    private void drawCropAreaOutline(Graphics g) {
        if (getClip() == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g;
        if (isFirstime()) {
            //cambiado para que no se desorbite la medida del cuadro de corte
            //int widthImage = getImage().getWidth();
            //int heightImage = getImage().getHeight();
            int widthImage = this.getWidth();
            int heightImage = this.getHeight();

            if (widthImage > clip.getWidth() & heightImage > clip.getHeight()) {
                //cambiado para que no se desorbite la medida del cuadro de corte
                //int wmedium = (getImage().getWidth() / 2);
                //int hmedium = (getImage().getHeight() / 2);
                int wmedium = (this.getWidth() / 2);
                int hmedium = (this.getHeight() / 2);
                //clip = new Rectangle(300, 500);
                getClip().setLocation((wmedium - 150), (hmedium - 150));
                setFirstime(false);
            } else {
                //TODO crear un cuadro de texto de acuerdo a las medidas minimas
                //cambiado para que no se desorbite la medida del cuadro de corte
                //int wmedium = (getImage().getWidth() / 2);
                //int hmedium = (getImage().getHeight() / 2);
                int wmedium = (this.getWidth() / 2);
                int hmedium = (this.getHeight() / 2);
                clip = new Rectangle(widthImage, heightImage);
                getClip().setLocation((wmedium - ((int) widthImage / 2)), (hmedium - ((int) heightImage / 2)));
                setFirstime(false);
            }
        }

        g2.setPaint(Color.RED);
        g2.draw(getClip());
    }

    int getScreenImageWidth() {
        return (int) (scale * getImage().getWidth());
    }

    int getScreenImageHeight() {
        return (int) (scale * getImage().getHeight());
    }

    private int getScreenNavImageWidth() {
        return (int) (navScale * navImageWidth);
    }

    private int getScreenNavImageHeight() {
        return (int) (navScale * navImageHeight);
    }

    private static String[] getImageFormatExtensions() {
        String[] names = ImageIO.getReaderFormatNames();
        for (int i = 0; i < names.length; i++) {
            names[i] = names[i].toLowerCase(Locale.getDefault());
        }
        Arrays.sort(names);
        return names;
    }

}

class ImageClipperNavigablePanel extends MouseInputAdapter {

    private static ImageClipperNavigablePanel imageClipper = null;
    private NavigableImagePanel navigableImagePanel;

    static synchronized ImageClipperNavigablePanel getInstance() {
        if (imageClipper == null) {
            imageClipper = new ImageClipperNavigablePanel();
        }
        return imageClipper;
    }

    protected ImageClipperNavigablePanel() {
    }

    void setNavigableImagePanel(NavigableImagePanel navigableImagePanel) {
        this.navigableImagePanel = navigableImagePanel;
        if (navigableImagePanel.getMouseWheelListeners().length == 0) {
            this.navigableImagePanel.addMouseWheelListener(this);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (navigableImagePanel.getClip() == null) return;
        if (navigableImagePanel.getClip().contains(e.getX(), e.getY())) {
            int notches = e.getWheelRotation();
            if (notches < 0) {
                //up incrementar tamano
                if ((navigableImagePanel.getClip().width <= navigableImagePanel.getImage().getWidth())
                        && navigableImagePanel.getClip().height <= navigableImagePanel.getImage().getHeight()) {
                    plus();
                }
            } else {
                //down disminuir tamano
                sustract();
            }
        }
    }
    
    public void zoomInOut(boolean isOut){
        if (navigableImagePanel.getClip() == null) return;
        
        if(isOut){
            plus();
        }else{
            sustract();
        }
        
    }

    public void plus() {
        //Point p = navigableImagePanel;
        int x = navigableImagePanel.getWidth();
        int y = navigableImagePanel.getHeight();
        Dimension dim2 = navigableImagePanel.getClip().getSize();
        int w2 = (int) (dim2.width * 1.1); //+ 20;
        int h2 = (int) (dim2.height * 1.1); //+ 20;
        
        if(w2 > x || h2 > y)
            return;

        navigableImagePanel.setLocationClip((x / 2) - (w2 / 2), (y / 2) - (h2 / 2));
        navigableImagePanel.setSizeClip(w2, h2);
    }

    public void sustract() {
        //Point p = navigableImagePanel.getClip().getLocation();
        int x = navigableImagePanel.getWidth();
        int y = navigableImagePanel.getHeight();
        Dimension dim = navigableImagePanel.getClip().getSize();
        int w2 = (int) (dim.width * 0.9);// - 20;
        int h2 = (int) (dim.height * 0.9);// - 20;
        
        if(w2 < 10 || h2 < 10)
            return;

        navigableImagePanel.setLocationClip((x / 2) - (w2 / 2), (y / 2) - (h2 / 2));
        navigableImagePanel.setSizeClip(w2, h2);
    }

    public BufferedImage getCutImage() throws IllegalArgumentException {
        Point p = navigableImagePanel.getClip().getLocation();
        int x = p.x;
        int y = p.y;
        int x1 = navigableImagePanel.getOriginX();
        int y1 = navigableImagePanel.getOriginY();
        
        if(y < y1){
            JOptionPane.showMessageDialog(null, "No se pudo generar el corte de la imagen, asegurese que el margen \n"
                                              + "de corte en color rojo este dentro de los bordes de la imagen.", Utilerias.getProperty(ApplicationProperties.APP_TITLE), JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        //cortar la imagen
        //aqui hay que validar que no se desborde el cuadro de corte
        int x2 = Math.abs(x - x1);
        int y2 = Math.abs(y - y1);
        double T = navigableImagePanel.getImage().getWidth();
        double U = navigableImagePanel.getScreenImageWidth();
        double scalaAlrevez = T / U;
        int w = (int) (navigableImagePanel.getClip().getWidth() * scalaAlrevez);
        int h = (int) (navigableImagePanel.getClip().getHeight() * scalaAlrevez);
        return JavaAdvancedImageImpl.getInstance().cropImage(navigableImagePanel.getImage(), (int) (x2 * scalaAlrevez), (int) (y2 * scalaAlrevez), w, h);

    }

    public BufferedImage selectAllPicture() {
        return navigableImagePanel.getImage();
        //navigableImagePanel.getClip().setLocation(navigableImagePanel.getScreenImageWidth(), navigableImagePanel.getScreenImageHeight());

    }
}