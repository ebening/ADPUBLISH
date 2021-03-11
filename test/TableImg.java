import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;

public class TableImg extends JFrame
{
    public TableImg()
    {
        
        ImageIcon icon = null;
        
        URL url;
        try {
            url = new URL("https://www.vector.com.mx/img/vectormedia/images/videos/0x0000000000514d8a.jpg");
        
                  BufferedImage img = ImageIO.read(url);
                  icon = new ImageIcon(img);
                  
                } catch (MalformedURLException ex) {
            Logger.getLogger(TableImg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TableImg.class.getName()).log(Level.SEVERE, null, ex);
        }  
        ImageIcon copyIcon = new ImageIcon("C:\\rightArrow.png");

        String[] columnNames = {"Picture", "Description"};
        Object[][] data =
        {
            {icon, "Add"},
            {copyIcon, "Copy"},
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable( model )
        {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        JScrollPane scrollPane = new JScrollPane( table );
        getContentPane().add( scrollPane );
    }

    public static void main(String[] args)
    {
        TableImg frame = new TableImg();
        frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
        frame.pack();
        frame.setVisible(true);
    }

}