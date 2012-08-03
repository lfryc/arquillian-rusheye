/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.frames;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.gui.CustomTreeRenderer;
import org.jboss.rusheye.manager.gui.view.DoubleView;
import org.jboss.rusheye.manager.gui.view.MaskView;
import org.jboss.rusheye.manager.gui.view.SingleView;
import org.jboss.rusheye.manager.project.Project;
import org.jboss.rusheye.manager.project.observable.Observed;
import org.jboss.rusheye.manager.project.observable.Observer;
import org.jboss.rusheye.manager.project.TestCase;
import org.jboss.rusheye.manager.project.tree.NodeList;
import org.jboss.rusheye.manager.project.tree.TreeNodeImpl;
import org.jboss.rusheye.parser.ManagerParser;
import org.jboss.rusheye.parser.ParserThread;
import org.jboss.rusheye.suite.Properties;
import org.jboss.rusheye.suite.ResultConclusion;

/**
 * Project Manager Frame. One of 2 main frames for manager. Here we display tree
 * of test cases and do manual changing. Also allows filtering and other useful
 * stuff.
 *
 * @author Jakub D.
 */
public class ProjectManagerFrame extends javax.swing.JFrame implements Observer {

    private JViewport viewport;

    public ProjectManagerFrame() {
        initComponents();

        viewport = jScrollPane2.getViewport();

        //Custom document listener for filtering purposes
        filterField.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent de) {
                String regexp = filterField.getText();
                if (regexp.equals("")) {
                    Main.mainProject.getRoot().setAllVisible();
                    updateTreeModel();
                } else {
                    Main.mainProject.getRoot().setVisibility(filterField.getText());
                    updateTreeModel();
                }
            }

            public void removeUpdate(DocumentEvent de) {
                String regexp = filterField.getText();
                if (regexp.equals("")) {
                    Main.mainProject.getRoot().setAllVisible();
                    updateTreeModel();
                } else {
                    Main.mainProject.getRoot().setVisibility(filterField.getText());
                    updateTreeModel();
                }
            }

            public void changedUpdate(DocumentEvent de) {
            }
        });

        projectTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent tse) {
                putTestIntoView();
                if (Main.mainProject.getCurrentCase() != null && Main.mainProject.getCurrentCase().isLeaf()) {
                    String info = "";
                    info += ((TestCase) Main.mainProject.getCurrentCase().getParent()).getName() + "\n";
                    info += Main.mainProject.getCurrentCase().getName() + "\n";
                    info += Main.mainProject.getCurrentCase().getConclusion().toString();
                    infoTextArea.setText(info);
                }
            }
        });
    }

    public void update(Project project) {
        this.patternsPathField.setText(project.getPatternPath());
        this.samplesPathField.setText(project.getSamplesPath());
    }

    public javax.swing.JTextField getPatternsPathField() {
        return patternsPathField;
    }

    public javax.swing.JTextField getSamplesPathField() {
        return samplesPathField;
    }

    public JTree getTree() {
        return projectTree;
    }

    public void createTree() {
        Main.projectFrame.setVisible(true);
        projectTree.setCellRenderer(new CustomTreeRenderer());
        updateTreeModel();
    }

    public void updateTreeModel() {
        DefaultTreeModel model = new DefaultTreeModel(Main.mainProject.getRoot());
        projectTree.setModel(model);
        projectTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        Main.projectFrame.validate();
    }

    /**
     * Generate Double/Single view based on current test. Fired mainly when we
     * click on test in tree.
     *
     */
    public void putTestIntoView() {
        TestCase node = (TestCase) projectTree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }

        if (node.isLeaf()) {
            Main.mainProject.setCurrentCase(Main.mainProject.findTest(node.getPath()));

            JPanel panel = Main.interfaceFrame.getMainPanel();
            panel.removeAll();

            TestCase current = Main.mainProject.getCurrentCase();

            switch (Main.interfaceFrame.getView()) {
                case InterfaceFrame.SINGLE:
                    panel.add(new SingleView(current));
                    break;
                case InterfaceFrame.DOUBLE:
                    panel.add(new DoubleView(current));
                    break;
                case InterfaceFrame.MASK:
                    panel.add(new MaskView(current));
                    break;
            }

            panel.validate();
        }
    }

    /**
     * Updates icons for JTree.
     */
    public void updateIcons() {
        String info = "";
        info += ((TestCase) Main.mainProject.getCurrentCase().getParent()).getName() + "\n";
        info += Main.mainProject.getCurrentCase().getName() + "\n";
        info += Main.mainProject.getCurrentCase().getConclusion().toString();
        infoTextArea.setText(info);
        Main.mainProject.getCurrentCase().setChecked(true);
        TreePath path = projectTree.getSelectionPath();
        this.updateTreeModel();
        projectTree.setSelectionPath(path);
        projectTree.scrollPathToVisible(path);
    }

    /**
     * Allows us to travel between cases in tree.
     *
     * @param offset how far we search next case.
     */
    private void findNeighbour(int offset) {
        TreeNodeImpl node = (TreeNodeImpl) projectTree.getLastSelectedPathComponent();
        TreePath parentPath = projectTree.getSelectionPath().getParentPath();

        NodeList list = (NodeList) node.getParent().children();
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).equals(node)) {
                if (offset > 0 && i < list.size() - offset) {
                    projectTree.setSelectionPath(parentPath.pathByAddingChild(list.get(i + offset)));
                    break;
                }
                if (offset < 0 && i >= offset) {
                    projectTree.setSelectionPath(parentPath.pathByAddingChild(list.get(i + offset)));
                    break;
                }
            }
        }
    }

    /**
     * Observer pattern implementation.
     *
     * @param o project where paths have changed
     */
    public void update(Observed o) {
        if (o instanceof Project) {
            Project p = (Project) o;
            this.patternsPathField.setText(p.getPatternPath());
            this.samplesPathField.setText(p.getSamplesPath());
        }
    }
    
    private void filter(){
            List<ResultConclusion> filter = new ArrayList<ResultConclusion>();
        if (sameCheckBox.isSelected())
            filter.add(ResultConclusion.SAME);
        if (pSameCheckBox.isSelected())
            filter.add(ResultConclusion.PERCEPTUALLY_SAME);
        if (diffCheckBox.isSelected())
            filter.add(ResultConclusion.DIFFER);
        if (notCheckBox.isSelected())
            filter.add(ResultConclusion.NOT_TESTED);
        if (errorCheckBox.isSelected())
            filter.add(ResultConclusion.ERROR);


        Main.mainProject.getRoot().setVisibility(filter);
        this.updateTreeModel();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        patternsPathField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        samplesPathField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        projectTree = new javax.swing.JTree();
        posButton = new javax.swing.JButton();
        negButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        prevButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        filterField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoTextArea = new javax.swing.JTextArea();
        jSeparator2 = new javax.swing.JSeparator();
        runAllButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        menuBar = new javax.swing.JMenuBar();
        filtersMenu = new javax.swing.JMenu();
        filterAllMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        sameCheckBox = new javax.swing.JCheckBoxMenuItem();
        pSameCheckBox = new javax.swing.JCheckBoxMenuItem();
        diffCheckBox = new javax.swing.JCheckBoxMenuItem();
        notCheckBox = new javax.swing.JCheckBoxMenuItem();
        errorCheckBox = new javax.swing.JCheckBoxMenuItem();

        setTitle("ProjectManager");

        jLabel1.setText("Patterns :");

        patternsPathField.setText("path...");
        patternsPathField.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        patternsPathField.setEnabled(false);

        jLabel2.setText("Samples :");

        samplesPathField.setBackground(new java.awt.Color(255, 255, 255));
        samplesPathField.setText("path...");
        samplesPathField.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        samplesPathField.setEnabled(false);

        jLabel3.setText("Filter :");

        jScrollPane2.setMaximumSize(null);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Test Cases");
        projectTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane2.setViewportView(projectTree);

        posButton.setText("Positive");
        posButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                posButtonActionPerformed(evt);
            }
        });

        negButton.setText("Negative");
        negButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                negButtonActionPerformed(evt);
            }
        });

        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        prevButton.setText("Previous");
        prevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevButtonActionPerformed(evt);
            }
        });

        filterField.setFont(new java.awt.Font("Ubuntu", 2, 15)); // NOI18N

        infoTextArea.setColumns(20);
        infoTextArea.setEditable(false);
        infoTextArea.setLineWrap(true);
        infoTextArea.setRows(5);
        jScrollPane1.setViewportView(infoTextArea);

        runAllButton.setText("Run all");
        runAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAllButtonActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(jTree1);

        filtersMenu.setText("Filters");

        filterAllMenuItem.setText("Show all");
        filterAllMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterAllMenuItemActionPerformed(evt);
            }
        });
        filtersMenu.add(filterAllMenuItem);
        filtersMenu.add(jSeparator3);

        sameCheckBox.setSelected(true);
        sameCheckBox.setText("Same");
        sameCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sameCheckBoxActionPerformed(evt);
            }
        });
        filtersMenu.add(sameCheckBox);

        pSameCheckBox.setSelected(true);
        pSameCheckBox.setText("Perceptually same");
        pSameCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pSameCheckBoxActionPerformed(evt);
            }
        });
        filtersMenu.add(pSameCheckBox);

        diffCheckBox.setSelected(true);
        diffCheckBox.setText("Differ");
        diffCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diffCheckBoxActionPerformed(evt);
            }
        });
        filtersMenu.add(diffCheckBox);

        notCheckBox.setSelected(true);
        notCheckBox.setText("Not tested");
        notCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notCheckBoxActionPerformed(evt);
            }
        });
        filtersMenu.add(notCheckBox);

        errorCheckBox.setSelected(true);
        errorCheckBox.setText("Error");
        errorCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errorCheckBoxActionPerformed(evt);
            }
        });
        filtersMenu.add(errorCheckBox);

        menuBar.add(filtersMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(filterField, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(samplesPathField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                .addComponent(patternsPathField, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(prevButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jSeparator1)
                                .addComponent(jSeparator2)
                                .addComponent(runAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(negButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(posButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(patternsPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(samplesPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(negButton)
                    .addComponent(posButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextButton)
                    .addComponent(prevButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runAllButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Sets current pattern result as PERCEPTUALLY_SAME. Also changes result xml
     * file if available
     *
     * @param evt event triggering method
     */
    private void posButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_posButtonActionPerformed
        ResultConclusion last = Main.mainProject.getCurrentCase().getConclusion();
        Main.mainProject.getStatistics().addValue(last, -1);

        Main.mainProject.getCurrentCase().setConclusion(ResultConclusion.PERCEPTUALLY_SAME);
        Main.mainProject.getStatistics().addValue(ResultConclusion.PERCEPTUALLY_SAME, 1);
        Main.statFrame.update(Main.mainProject);

        String result = Main.mainProject.getResult();
        // TODO It's a hack
        if (result != null) {
            String regexp = Main.mainProject.getCurrentCase().getFilename() + "\" result=\"" + last;
            String newString = Main.mainProject.getCurrentCase().getFilename() + "\" result=\"" + Main.mainProject.getCurrentCase().getConclusion();

            result = result.replace(regexp, newString);
            try {
                PrintWriter out = new PrintWriter(Main.mainProject.getResultDescriptor());
                out.println(result);
                out.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        updateIcons();
    }//GEN-LAST:event_posButtonActionPerformed
    /**
     * Sets current pattern result as DIFFER.
     *
     * @param evt event triggering method
     */
    private void negButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_negButtonActionPerformed
        ResultConclusion last = Main.mainProject.getCurrentCase().getConclusion();
        Main.mainProject.getStatistics().addValue(last, -1);

        Main.mainProject.getCurrentCase().setConclusion(ResultConclusion.DIFFER);
        Main.mainProject.getStatistics().addValue(ResultConclusion.DIFFER, 1);

        Main.statFrame.update(Main.mainProject);

        updateIcons();
    }//GEN-LAST:event_negButtonActionPerformed
    /**
     * Shows all images in tree.
     *
     * @param evt event triggering method
     */
    private void filterAllMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterAllMenuItemActionPerformed
        Main.mainProject.getRoot().setAllVisible();
        this.updateTreeModel();
    }//GEN-LAST:event_filterAllMenuItemActionPerformed

   /**
     * Sets next test/pattern as main.
     *
     * @param evt event triggering method
     */
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        findNeighbour(1);
    }//GEN-LAST:event_nextButtonActionPerformed
    /**
     * Sets previous test/pattern as main.
     *
     * @param evt event triggering method
     */
    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevButtonActionPerformed
        findNeighbour(-1);
    }//GEN-LAST:event_prevButtonActionPerformed

    private void runAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runAllButtonActionPerformed
        Properties props = new Properties();

        if (!Main.mainProject.getSamplesPath().equals(""))
            props.setProperty("samples-directory", Main.mainProject.getSamplesPath());
        else {
            JOptionPane.showMessageDialog(this, "No samples path selected", "Parse", JOptionPane.WARNING_MESSAGE);
        }
        if (!Main.mainProject.getPatternPath().equals("")) {
            props.setProperty("patterns-directory", Main.mainProject.getPatternPath());
        } else {
            JOptionPane.showMessageDialog(this, "No patterns path selected", "Parse", JOptionPane.WARNING_MESSAGE);
        }
        props.setProperty("file-storage-directory", "tmp");
        props.setProperty("result-output-file", "result.xml");

        if (!Main.mainProject.getMaskPath().equals(""))
            props.setProperty("masks-directory", Main.mainProject.getMaskPath());

        Main.statFrame.setVisible(true);
        ManagerParser parser = Main.mainProject.getParser();
        parser.setProperties(props);

        new Thread(new ParserThread(parser)).start();

        Main.mainProject.setResultDescriptor(new File("result.xml"));
    }//GEN-LAST:event_runAllButtonActionPerformed

    private void sameCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sameCheckBoxActionPerformed
        filter();
    }//GEN-LAST:event_sameCheckBoxActionPerformed

    private void pSameCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pSameCheckBoxActionPerformed
        filter();
    }//GEN-LAST:event_pSameCheckBoxActionPerformed

    private void diffCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diffCheckBoxActionPerformed
        filter();
    }//GEN-LAST:event_diffCheckBoxActionPerformed

    private void notCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notCheckBoxActionPerformed
        filter();
    }//GEN-LAST:event_notCheckBoxActionPerformed

    private void errorCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_errorCheckBoxActionPerformed
        filter();
    }//GEN-LAST:event_errorCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem diffCheckBox;
    private javax.swing.JCheckBoxMenuItem errorCheckBox;
    private javax.swing.JMenuItem filterAllMenuItem;
    private javax.swing.JTextField filterField;
    private javax.swing.JMenu filtersMenu;
    private javax.swing.JTextArea infoTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JTree jTree1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton negButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JCheckBoxMenuItem notCheckBox;
    private javax.swing.JCheckBoxMenuItem pSameCheckBox;
    private javax.swing.JTextField patternsPathField;
    private javax.swing.JButton posButton;
    private javax.swing.JButton prevButton;
    private javax.swing.JTree projectTree;
    private javax.swing.JButton runAllButton;
    private javax.swing.JCheckBoxMenuItem sameCheckBox;
    private javax.swing.JTextField samplesPathField;
    // End of variables declaration//GEN-END:variables
}
