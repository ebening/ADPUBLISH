import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
 
public class DragTest extends JPanel
{
    public DragTest()
    {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        String s = "";
        for(int j = 0; j < 4; j++)
        {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEtchedBorder());
            panel.add(new JLabel("panel " + (j+1), JLabel.CENTER));
            add(panel, gbc);
        }
    }
 
    public static void main(String[] args)
    {
        DragTest test = new DragTest();
        DragListener listener = new DragListener(test);
        test.addMouseListener(listener);
        test.addMouseMotionListener(listener);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(test);
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}
 
class DragListener extends MouseInputAdapter
{
    DragTest dragTest;
    Component selectedComponent;
    GridBagConstraints constraints;
    Point start;
    boolean dragging;
    final int MIN_DRAG_DISTANCE = 5;
 
    public DragListener(DragTest dt)
    {
        dragTest = dt;
        dragging = false;
    }
 
    public void mousePressed(MouseEvent e)
    {
        Point p = e.getPoint();
        Component[] c = dragTest.getComponents();
        for(int j = 0; j < c.length; j++)
        {
            Rectangle r = c[j].getBounds();
            if(r.contains(p))
            {
                selectedComponent = c[j];
                start = p;
                dragging = true;
                break;
            }
        }
    }
 
    public void mouseReleased(MouseEvent e)
    {
        if(dragging)  // no component has been removed
        {
            dragging = false;
            return;
        }
        Point p = e.getPoint();
        if(!dragTest.getBounds().contains(p))  // out of bounds
        {
            // failed drop - add back at index = 0
            dragTest.add(selectedComponent, constraints, 0);
            dragTest.revalidate();
            return;
        }
        Component comp = dragTest.getComponentAt(p);
        int index;
        if(comp == dragTest)
            index = getDropIndex(dragTest, p);
        else  // over a child component
        {
            Rectangle r = comp.getBounds();
            index = getComponentIndex(dragTest, comp);
            if(p.y - r.y > (r.y + r.height) - p.y)
                index += 1;
        }
        dragTest.add(selectedComponent, constraints, index);
        dragTest.revalidate();
    }
 
    private int getDropIndex(Container parent, Point p)
    {
        Component[] c = parent.getComponents();
        for(int j = 0; j < c.length; j++)
            if(c[j].getY() > p.y)
                return j;
        return -1;
    }
 
    private int getComponentIndex(Container parent, Component target)
    {
        Component[] c = parent.getComponents();
        for(int j = 0; j < c.length; j++)
            if(c[j] == target)
                return j;
        return -1;
    }
 
    public void mouseDragged(MouseEvent e)
    {
        if(dragging)
        {
            if(e.getPoint().distance(start) > MIN_DRAG_DISTANCE)
            {
                // save constraints for the drop
                GridBagLayout layout = (GridBagLayout)dragTest.getLayout();
                constraints = layout.getConstraints(selectedComponent);
                dragTest.remove(selectedComponent);
                dragging = false;
                dragTest.revalidate();
            }
        }
    }
}