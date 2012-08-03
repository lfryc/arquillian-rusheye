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
import javax.swing.*;
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
import org.jboss.rusheye.manager.gui.view.mask.MaskCase;
import org.jboss.rusheye.manager.gui.view.mask.MaskType;
import org.jboss.rusheye.manager.gui.view.mask.converters.MaskConverter;
import org.jboss.rusheye.manager.gui.view.mask.converters.MaskToImageConverter;
import org.jboss.rusheye.manager.project.Project;
import org.jboss.rusheye.manager.project.TestCase;
import org.jboss.rusheye.manager.project.observable.Observed;
import org.jboss.rusheye.manager.project.observable.Observer;
import org.jboss.rusheye.manager.project.tree.NodeList;
import org.jboss.rusheye.manager.project.tree.TreeNodeImpl;
import org.jboss.rusheye.manager.utils.FileChooserUtils;
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


    public ProjectManagerFrame() {
        initComponents();

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
        
        maskTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent tse) {
                putMaskIntoView();

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

    private void filter() {
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
    
    public void createMaskTree() {
        updateMaskTreeModel();
    }

    public void updateMaskTreeModel() {
        DefaultTreeModel model = new DefaultTreeModel(Main.mainProject.getMaskManager().getRoot());
        maskTree.setModel(model);
        maskTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        maskTree.setExpandsSelectedPaths(true);
        for (int i = 0; i < maskTree.getRowCount(); i++) {
            maskTree.expandRow(i);
        }
        this.validate();
    }

    public void putMaskIntoView() {
        MaskCase node = (MaskCase) maskTree.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        if (node.getName().startsWith("Mask ")) {
            Main.mainProject.getMaskManager().setCurrentMask(node);
            infoTextArea.setText(node.getInfo());
            Main.interfaceFrame.repaint();
        }
        if (node.getName().startsWith("Rect ")) {
            infoTextArea.setText(node.getShape().toString());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoTextArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        projectTree = new javax.swing.JTree();
        filterField = new javax.swing.JTextField();
        negButton = new javax.swing.JButton();
        runAllButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        prevButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        posButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        sameCheckBox = new javax.swing.JCheckBox();
        showAllButton = new javax.swing.JButton();
        showDiffButton = new javax.swing.JButton();
        showSameButton = new javax.swing.JButton();
        pSameCheckBox = new javax.swing.JCheckBox();
        diffCheckBox = new javax.swing.JCheckBox();
        notCheckBox = new javax.swing.JCheckBox();
        errorCheckBox = new javax.swing.JCheckBox();
        jSeparator4 = new javax.swing.JSeparator();
        showNotButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        maskTree = new javax.swing.JTree();
        jScrollPane4 = new javax.swing.JScrollPane();
        infoTextArea1 = new javax.swing.JTextArea();
        addMaskButton = new javax.swing.JButton();
        removeMaskButton = new javax.swing.JButton();
        saveMaskButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        patternsPathField = new javax.swing.JTextField();
        samplesPathField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        masksPathField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setTitle("ProjectManager");

        infoTextArea.setColumns(20);
        infoTextArea.setEditable(false);
        infoTextArea.setLineWrap(true);
        infoTextArea.setRows(5);
        jScrollPane1.setViewportView(infoTextArea);

        jScrollPane2.setMaximumSize(null);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Test Cases");
        projectTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane2.setViewportView(projectTree);

        filterField.setFont(new java.awt.Font("Ubuntu", 2, 15)); // NOI18N

        negButton.setText("Negative");
        negButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                negButtonActionPerformed(evt);
            }
        });

        runAllButton.setText("Run all");
        runAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAllButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Filter :");

        prevButton.setText("Previous");
        prevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevButtonActionPerformed(evt);
            }
        });

        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        posButton.setText("Positive");
        posButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                posButtonActionPerformed(evt);
            }
        });

        sameCheckBox.setSelected(true);
        sameCheckBox.setText("Same");
        sameCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sameCheckBoxActionPerformed(evt);
            }
        });

        showAllButton.setText("Show all");
        showAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAllButtonActionPerformed(evt);
            }
        });

        showDiffButton.setText("Show differ");
        showDiffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showDiffButtonActionPerformed(evt);
            }
        });

        showSameButton.setText("Show same");
        showSameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showSameButtonActionPerformed(evt);
            }
        });

        pSameCheckBox.setSelected(true);
        pSameCheckBox.setText("Perceptually same");
        pSameCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pSameCheckBoxActionPerformed(evt);
            }
        });

        diffCheckBox.setSelected(true);
        diffCheckBox.setText("Differ");
        diffCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diffCheckBoxActionPerformed(evt);
            }
        });

        notCheckBox.setSelected(true);
        notCheckBox.setText("Not tested");
        notCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notCheckBoxActionPerformed(evt);
            }
        });

        errorCheckBox.setSelected(true);
        errorCheckBox.setText("Error");
        errorCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errorCheckBoxActionPerformed(evt);
            }
        });

        showNotButton.setText("Show not tested");
        showNotButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showNotButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(51, 51, 51)
                        .addComponent(filterField))
                    .addComponent(jSeparator4)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(prevButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jSeparator1)
                                .addComponent(jSeparator2)
                                .addComponent(runAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(negButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(posButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(showSameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(showDiffButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(showAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(showNotButton, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(notCheckBox)
                                    .addComponent(pSameCheckBox)
                                    .addComponent(diffCheckBox)
                                    .addComponent(errorCheckBox)
                                    .addComponent(sameCheckBox))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(sameCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pSameCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(diffCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(notCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(errorCheckBox))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(showAllButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(showDiffButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(showSameButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(showNotButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(negButton)
                    .addComponent(posButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextButton)
                    .addComponent(prevButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runAllButton)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Manager", jPanel2);

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        maskTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane3.setViewportView(maskTree);

        infoTextArea1.setColumns(20);
        infoTextArea1.setRows(5);
        jScrollPane4.setViewportView(infoTextArea1);

        addMaskButton.setText("Add");
        addMaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMaskButtonActionPerformed(evt);
            }
        });

        removeMaskButton.setText("Remove");
        removeMaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeMaskButtonActionPerformed(evt);
            }
        });

        saveMaskButton.setText("Save");
        saveMaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMaskButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addMaskButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeMaskButton, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                            .addComponent(saveMaskButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(addMaskButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeMaskButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveMaskButton)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(342, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Masks", jPanel1);

        jLabel1.setText("Patterns :");

        patternsPathField.setText("path...");
        patternsPathField.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        patternsPathField.setEnabled(false);

        samplesPathField.setBackground(new java.awt.Color(255, 255, 255));
        samplesPathField.setText("path...");
        samplesPathField.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        samplesPathField.setEnabled(false);

        jLabel2.setText("Samples :");

        jLabel4.setText("Masks :");

        masksPathField.setBackground(new java.awt.Color(255, 255, 255));
        masksPathField.setText("path...");
        masksPathField.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        masksPathField.setEnabled(false);

        jButton1.setText("Set");

        jButton2.setText("Set");

        jButton3.setText("Set");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(patternsPathField)
                            .addComponent(samplesPathField)
                            .addComponent(masksPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(patternsPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(samplesPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(masksPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addContainerGap(512, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Configuration", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void showAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAllButtonActionPerformed
        Main.mainProject.getRoot().setAllVisible();
        this.updateTreeModel();
    }//GEN-LAST:event_showAllButtonActionPerformed

    private void sameCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sameCheckBoxActionPerformed
        filter();
    }//GEN-LAST:event_sameCheckBoxActionPerformed

    private void diffCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diffCheckBoxActionPerformed
        filter();
    }//GEN-LAST:event_diffCheckBoxActionPerformed

    private void notCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notCheckBoxActionPerformed
        filter();
    }//GEN-LAST:event_notCheckBoxActionPerformed

    private void errorCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_errorCheckBoxActionPerformed
        filter();
    }//GEN-LAST:event_errorCheckBoxActionPerformed

    private void showSameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showSameButtonActionPerformed
        sameCheckBox.setSelected(false);
        pSameCheckBox.setSelected(false);
        diffCheckBox.setSelected(false);
        notCheckBox.setSelected(false);
        errorCheckBox.setSelected(false);
        
        sameCheckBox.setSelected(true);
        pSameCheckBox.setSelected(true);
        filter();
    }//GEN-LAST:event_showSameButtonActionPerformed

    private void showNotButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showNotButtonActionPerformed
        sameCheckBox.setSelected(false);
        pSameCheckBox.setSelected(false);
        diffCheckBox.setSelected(false);
        notCheckBox.setSelected(false);
        errorCheckBox.setSelected(false);
        
        notCheckBox.setSelected(true);
        filter();
    }//GEN-LAST:event_showNotButtonActionPerformed

    private void showDiffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showDiffButtonActionPerformed
        sameCheckBox.setSelected(false);
        pSameCheckBox.setSelected(false);
        diffCheckBox.setSelected(false);
        notCheckBox.setSelected(false);
        errorCheckBox.setSelected(false);
        
        diffCheckBox.setSelected(true);
        filter();
    }//GEN-LAST:event_showDiffButtonActionPerformed

    private void negButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_negButtonActionPerformed
        ResultConclusion last = Main.mainProject.getCurrentCase().getConclusion();
        Main.mainProject.getStatistics().addValue(last, -1);

        Main.mainProject.getCurrentCase().setConclusion(ResultConclusion.DIFFER);
        Main.mainProject.getStatistics().addValue(ResultConclusion.DIFFER, 1);

        Main.statFrame.update(Main.mainProject);

        updateIcons();
    }//GEN-LAST:event_negButtonActionPerformed

    private void addMaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMaskButtonActionPerformed
        MaskCase root = Main.mainProject.getMaskManager().getRoot();
        MaskCase newCase = new MaskCase();
        newCase.setName("Mask " + (root.getChildCount() + 1));
        newCase.setType(MaskType.SELECTIVE_ALPHA);

        root.addChild(newCase);

        this.updateTreeModel();

        Main.mainProject.getMaskManager().setCurrentMask(newCase);
    }//GEN-LAST:event_addMaskButtonActionPerformed

    private void removeMaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeMaskButtonActionPerformed
        MaskCase root = Main.mainProject.getMaskManager().getRoot();

        MaskCase node = (MaskCase) maskTree.getLastSelectedPathComponent();
        TreePath path = maskTree.getSelectionPath();

        System.out.println("Try to remove ");
        if (!root.getAllChildren().remove(node)) {
            for (int i = 0; i < root.getChildCount(); ++i) {
                root.getAllChildren().get(i).getAllChildren().remove(node);
            }
        }

        this.updateTreeModel();

        maskTree.setSelectionPath(path.getParentPath());
        maskTree.scrollPathToVisible(path);
    }//GEN-LAST:event_removeMaskButtonActionPerformed

    private void saveMaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMaskButtonActionPerformed

        JFileChooser fc = FileChooserUtils.saveChooser();

        int returnVal = fc.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            MaskCase node = (MaskCase) maskTree.getLastSelectedPathComponent();
            MaskConverter converter = new MaskToImageConverter(node);
            converter.save(file);
        }

    }//GEN-LAST:event_saveMaskButtonActionPerformed

    private void pSameCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pSameCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pSameCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addMaskButton;
    private javax.swing.JCheckBox diffCheckBox;
    private javax.swing.JCheckBox errorCheckBox;
    private javax.swing.JTextField filterField;
    private javax.swing.JTextArea infoTextArea;
    private javax.swing.JTextArea infoTextArea1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTree maskTree;
    private javax.swing.JTextField masksPathField;
    private javax.swing.JButton negButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JCheckBox notCheckBox;
    private javax.swing.JCheckBox pSameCheckBox;
    private javax.swing.JTextField patternsPathField;
    private javax.swing.JButton posButton;
    private javax.swing.JButton prevButton;
    private javax.swing.JTree projectTree;
    private javax.swing.JButton removeMaskButton;
    private javax.swing.JButton runAllButton;
    private javax.swing.JCheckBox sameCheckBox;
    private javax.swing.JTextField samplesPathField;
    private javax.swing.JButton saveMaskButton;
    private javax.swing.JButton showAllButton;
    private javax.swing.JButton showDiffButton;
    private javax.swing.JButton showNotButton;
    private javax.swing.JButton showSameButton;
    // End of variables declaration//GEN-END:variables
}
