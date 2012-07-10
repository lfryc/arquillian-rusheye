/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.gui.view.MenuView;
import org.jboss.rusheye.manager.project.ProjectFactory;
import org.jboss.rusheye.manager.utils.FileChooserUtils;

/**
 *
 * @author hcube
 */
public class InterfaceFrame extends javax.swing.JFrame {

    public static final int SINGLE = 1;
    public static final int DOUBLE = 2;
    private int view = InterfaceFrame.DOUBLE;
    private MenuView menuView;

    /**
     * Creates new form InterfaceFrame
     */
    public InterfaceFrame() {
        initComponents();

        this.validate();
    }
    
    public void setMenu(){
        menuView = new MenuView();
        Main.mainProject.addObserver(menuView);
        mainPanel.removeAll();
        mainPanel.add(menuView);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public void clean() {
        Main.projectFrame.getPatternsPathField().setText("path...");
        Main.projectFrame.getSamplesPathField().setText("path...");
        mainPanel.removeAll();
    }

    public void setPatternsAction() {
        File dir = FileChooserUtils.openDir("Open Pattern Dir", this);
        if (dir != null)
            Main.mainProject.setPatternPath(dir.getAbsolutePath());
        Main.projectFrame.getPatternsPathField().setText(dir.getAbsolutePath());
        
        Main.mainProject.getRoot().removeDiffRecursive();
        Main.projectFrame.putTestIntoView();
    }

    public void setSamplesAction() {
        File dir = FileChooserUtils.openDir("Open Samples Dir", this);
        if (dir != null)
            Main.mainProject.setSamplesPath(dir.getAbsolutePath());
        Main.projectFrame.getSamplesPathField().setText(dir.getAbsolutePath());
        
        Main.mainProject.getRoot().removeDiffRecursive();
        Main.projectFrame.putTestIntoView();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        projectMenu = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        viewsMenu = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        doubleViewMenuItem = new javax.swing.JMenuItem();
        singleViewMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RushEye  - Manager");

        mainPanel.setLayout(new java.awt.BorderLayout());

        fileMenu.setText("File");

        jMenuItem7.setText("New empty project");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem7);

        jMenuItem6.setText("New project from descriptor");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem6);

        jMenuItem5.setText("New project from directories");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem5);
        fileMenu.add(jSeparator6);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        jMenuBar1.add(fileMenu);

        projectMenu.setText("Project");

        jMenuItem14.setText("Generate Suite Descriptor");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        projectMenu.add(jMenuItem14);

        jMenuItem13.setText("Generate Result Descriptor");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        projectMenu.add(jMenuItem13);
        projectMenu.add(jSeparator2);

        jMenuItem1.setText("Set patterns path");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        projectMenu.add(jMenuItem1);

        jMenuItem2.setText("Set samples path");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        projectMenu.add(jMenuItem2);

        jMenuItem4.setText("Set result descriptor");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        projectMenu.add(jMenuItem4);

        jMenuBar1.add(projectMenu);

        viewsMenu.setText("Views");

        jMenuItem3.setText("Project Manager");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        viewsMenu.add(jMenuItem3);

        jMenuItem8.setText("Main Menu");
        viewsMenu.add(jMenuItem8);
        viewsMenu.add(jSeparator3);

        doubleViewMenuItem.setText("Double View");
        doubleViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doubleViewMenuItemActionPerformed(evt);
            }
        });
        viewsMenu.add(doubleViewMenuItem);

        singleViewMenuItem.setText("Single View");
        singleViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleViewMenuItemActionPerformed(evt);
            }
        });
        viewsMenu.add(singleViewMenuItem);

        jMenuBar1.add(viewsMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        setSamplesAction();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        setPatternsAction();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Main.projectFrame.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        JFileChooser fc = FileChooserUtils.dirChooser();
        fc.setDialogTitle("Open Pattern Dir");
        String path1 = FileChooserUtils.chooseFile(fc, this).getAbsolutePath();
        fc.setDialogTitle("Open Samples Dir");
        String path2 = FileChooserUtils.chooseFile(fc, this).getAbsolutePath();
        if (path1 != null && path2 != null) {
            Main.mainProject = ProjectFactory.projectFromDirs(path1, path2);
            Main.projectFrame.getPatternsPathField().setText(path1);
            Main.projectFrame.getSamplesPathField().setText(path2);
            Main.projectFrame.createTree();
        }
        clean();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void doubleViewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doubleViewMenuItemActionPerformed
        view = InterfaceFrame.DOUBLE;
        Main.projectFrame.putTestIntoView();
    }//GEN-LAST:event_doubleViewMenuItemActionPerformed

    private void singleViewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleViewMenuItemActionPerformed
        view = InterfaceFrame.SINGLE;
        Main.projectFrame.putTestIntoView();
    }//GEN-LAST:event_singleViewMenuItemActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        CrawlFrame crawlFrame = new CrawlFrame();
        crawlFrame.setVisible(true);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        ParseFrame parseFrame = new ParseFrame();
        parseFrame.setVisible(true);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        JFileChooser fc = FileChooserUtils.fileChooser();
        File tmp = FileChooserUtils.chooseFile(fc, this);
        if (tmp != null) {
            Main.mainProject = ProjectFactory.projectFromDescriptor(tmp);
            Main.projectFrame.createTree();
            clean();
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        Main.mainProject = ProjectFactory.emptyProject();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        JFileChooser fc = FileChooserUtils.fileChooser();
        File tmp = FileChooserUtils.chooseFile(fc, this);
        if (tmp != null)
            Main.mainProject.setResultDescriptor(tmp);
    }//GEN-LAST:event_jMenuItem4ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem doubleViewMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenu projectMenu;
    private javax.swing.JMenuItem singleViewMenuItem;
    private javax.swing.JMenu viewsMenu;
    // End of variables declaration//GEN-END:variables
}
