/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package designer;

import com.adinfi.formateador.view.administration.JCheckBoxTree;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.tree.TreePath;
import tree.examples.JCheckBoxTree2;

/**
 *
 * @author USUARIO
 */
public class Main extends JFrame {

    private static final long serialVersionUID = 4648172894076113183L;

    public Main() {
        super();
        setSize(500, 500);
        this.getContentPane().setLayout(new BorderLayout());
        final JCheckBoxTree2 cbt = new JCheckBoxTree2();
        this.getContentPane().add(cbt);
//        cbt.addCheckChangeEventListener(new JCheckBoxTree.CheckChangeEventListener() {
//            public void checkStateChanged(JCheckBoxTree.CheckChangeEvent event) {
//                System.out.println("event");
//                TreePath[] paths = cbt.getCheckedPaths();
//                for (TreePath tp : paths) {
//                    for (Object pathPart : tp.getPath()) {
//                        System.out.print(pathPart + ",");
//                    }                   
//                    System.out.println();
//                }
//            }           
//        });         
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        
        cbt.setModel(null);
    }

    public static void main(String args[]) {
        Main m = new Main();
        m.setVisible(true);
    }
}