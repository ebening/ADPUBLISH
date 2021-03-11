/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adinfi.formateador.view.administration;

import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author Guillermo Trejo
 */
public class JTabbedPane_withoutPaintedTabs extends JTabbedPane {  
  
    private static boolean showTabsHeader = false;  
  
    public JTabbedPane_withoutPaintedTabs() {  
        setUI(new MyTabbedPaneUI());  
    }  
  
    private class MyTabbedPaneUI extends BasicTabbedPaneUI {  
  
        @Override  
        protected int calculateTabAreaHeight(  
                    int tabPlacement, int horizRunCount, int maxTabHeight) {  
            if (showTabsHeader) {  
                return super.calculateTabAreaHeight(  
                    tabPlacement, horizRunCount, maxTabHeight);  
            } else {  
                return 0;  
            }  
        }  
  
        @Override  
        protected void paintTab(  
            Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex,  
                        Rectangle iconRect, Rectangle textRect) {  
            if (showTabsHeader) {  
                super.paintTab(  
                     g, tabPlacement, rects, tabIndex, iconRect, textRect);  
            }  
        }  
  
        @Override  
        protected void paintContentBorder(  
                   Graphics g, int tabPlacement, int selectedIndex) {  
            if (showTabsHeader) {  
                super.paintContentBorder(g, tabPlacement, selectedIndex);  
            }  
        }  
  
        @Override  
        public int tabForCoordinate(JTabbedPane pane, int x, int y) {  
            if (showTabsHeader) {  
                return super.tabForCoordinate(pane, x, y);  
            } else {  
                return -1;  
            }  
        }  
    }  
}  