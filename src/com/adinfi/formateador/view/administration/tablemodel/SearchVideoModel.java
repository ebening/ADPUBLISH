package com.adinfi.formateador.view.administration.tablemodel;

import com.adinfi.formateador.util.Utilerias;
import com.adinfi.ws.vectormedia.TdsVectorMediaBusqueda;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Guillermo Trejo
 */
public class SearchVideoModel extends AbstractTableModel {

    public enum ColumNamesConstants {

        //Define las Columnas/Valores de cabecera de la tabla
        TOTALCOLUMS("6", null),
        THUMBNAIL("", ""),
        FECHA("FECHA", "Fecha"),
        TITULO("TITULO", "Título"),
        AUTOR("AUTOR", "Autor"),
        CATEGORIA("CATEGORIA", "Categoría"),
        TIPO("TIPO", "Tipo");

        ColumNamesConstants(final String value, final String caption) {
            this.value = value;
            this.caption = caption;
        }

        private final String value;
        private final String caption;

        /* Metodo sobre escrito para obetener el valor de las constantes.
         * @see java.lang.String toString()
         */
        @Override
        public String toString() {
            return value;
        }

        public String getCaption() {
            return caption;
        }

    }

    private final String[] columnNames = new String[]{
        ColumNamesConstants.THUMBNAIL.getCaption(),
        ColumNamesConstants.FECHA.getCaption(),
        ColumNamesConstants.TITULO.getCaption(),
        ColumNamesConstants.AUTOR.getCaption(),
        ColumNamesConstants.CATEGORIA.getCaption(),
        ColumNamesConstants.TIPO.getCaption()
    };
    private final Class[] classes = new Class[]{
        ImageIcon.class, String.class, String.class, String.class, String.class, String.class
    };
    public final Integer COLUMN_COUNT = Integer.parseInt(ColumNamesConstants.TOTALCOLUMS.toString());

    private TdsVectorMediaBusqueda[] data;
    private String imgDefault;

    private Object[] emptyRow = new Object[]{
        "", " ", "", "", "", ""
    };

    public SearchVideoModel() {
    }

    public SearchVideoModel(TdsVectorMediaBusqueda[] data) {
        this.data = data;
    }

    public SearchVideoModel(TdsVectorMediaBusqueda[] data, String imgDefault) {
        this.data = data;
        this.imgDefault = imgDefault;
    }

    @Override
    public int getRowCount() {
        return data != null ? data.length : 0;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(int column) {
        return column >= columnNames.length ? columnNames[columnNames.length - 1] : columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex >= classes.length ? classes[classes.length - 1] : classes[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (data == null || data.length == 0) {
            return Object.class;
        }

        Object value = null;
        try {
            switch (columnIndex) {
                case 0:
                    String thumbnail = data[rowIndex].getThumbnail();
                    if (!thumbnail.isEmpty()) {
                        value = loadThumbnail(thumbnail, imgDefault);
                    }
                    break;
                case 1:
                    value = data[rowIndex].getFechaPublicacion();
                    break;
                case 2:
                    value = data[rowIndex].getTitulo();
                    break;
                case 3:
                    value = data[rowIndex].getAutor();
                    break;
                case 4:
                    value = data[rowIndex].getCategoria();
                    break;
                case 5:
                    value = data[rowIndex].getTipoAnalisis();
                    break;
            }
        } catch (Exception e) {
            value = null;
        }
        return value;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        //Verificar si el elemento contiene -1 en idLanguage
//        if (data == null || data.length == 0) {
//            return false;
//        } else {
//            //Editar todas las columnas
//            if (col == 0) {
//                return true;
//            }
//            // habilitar edición en fila seleccionada
//            if (Boolean.parseBoolean(String.valueOf(data[row][0])) == true) {
//                return true;
//            } else {
//                return false;
//            }
//        }
        return false;
    }

//    /*
//     * Don't need to implement this method unless your table's
//     * data can change.
//     */
//    @Override
//    public void setValueAt(Object value, int row, int col) {
//        data[row][col] = value;
//        fireTableCellUpdated(row, col);
//    }
    public TdsVectorMediaBusqueda[] getData() {
        return data;
    }
//
//    public void setData(Object[][] data) {
//        this.data = data;
//        fireTableDataChanged();
//    }

    public Object[] getEmptyRow() {
        return emptyRow;
    }

    public void setEmptyRow(Object[] emptyRow) {
        this.emptyRow = emptyRow;
    }

    public static int getResponseCode(String urlString) throws MalformedURLException, IOException {
        URL u = new URL(urlString);
        HttpURLConnection huc = (HttpURLConnection) u.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        return huc.getResponseCode();
    }

    private ImageIcon loadThumbnail(String urlHttp, String defaultImg) {
        ImageIcon icon = null;
        BufferedImage resizedImage = null;
        try {
            if (getResponseCode(urlHttp) != 404) {
                URL url = new URL(urlHttp);
                BufferedImage img = ImageIO.read(url);
                resizedImage = resize(img, 60, 50);
                icon = new ImageIcon(resizedImage);
            } else {
                URL url = new URL(defaultImg);
                BufferedImage img = ImageIO.read(url);
                resizedImage = resize(img, 60, 50);
                icon = new ImageIcon(resizedImage);
            }

        } catch (IOException e) {
            Utilerias.logger(getClass()).info(e);
        }

        return icon;
    }


    /* IMAGE DATA */
    public BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = null;
        try {
            bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
            Graphics2D g2d = (Graphics2D) bi.createGraphics();
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            g2d.drawImage(image, 0, 0, width, height, null);
            g2d.dispose();

        } catch (Exception ex) {
            Utilerias.logger(getClass()).info(ex);
        }
        return bi;
    }

//  public Boolean isCheckedRow(int row) {
//        return (Boolean)((data == null || data.length == 0) ? false : data[row][0]);
//    }
//
//
    public Integer getUniqueRowIdentify(int row) {
        return (Integer) ((data == null || data.length == 0) ? -1 : data[row]);
    }
}
