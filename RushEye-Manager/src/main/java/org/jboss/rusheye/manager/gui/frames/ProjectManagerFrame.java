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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.exception.ManagerException;
import org.jboss.rusheye.manager.gui.CustomTreeRenderer;
import org.jboss.rusheye.manager.gui.charts.RushEyeStatistics;
import org.jboss.rusheye.manager.gui.view.DoubleView;
import org.jboss.rusheye.manager.gui.view.MaskView;
import org.jboss.rusheye.manager.gui.view.SingleView;
import org.jboss.rusheye.manager.gui.view.mask.MaskCase;
import org.jboss.rusheye.manager.gui.view.mask.MaskType;
import org.jboss.rusheye.manager.gui.view.mask.converters.MaskConverter;
import org.jboss.rusheye.manager.gui.view.mask.converters.MaskToImageConverter;
import org.jboss.rusheye.manager.project.Project;
import org.jboss.rusheye.manager.project.TestCase;
import org.jboss.rusheye.manager.project.tree.NodeList;
import org.jboss.rusheye.manager.project.tree.TreeNodeImpl;
import org.jboss.rusheye.manager.utils.FileChooserUtils;
import org.jboss.rusheye.parser.ManagerParser;
import org.jboss.rusheye.parser.ParserThread;
import org.jboss.rusheye.suite.Perception;
import org.jboss.rusheye.suite.Properties;
import org.jboss.rusheye.suite.ResultConclusion;

/**
 * Project Manager Frame. One of 2 main frames for manager. Here we display tree
 * of test cases and do manual changing. Also allows filtering and other useful
 * stuff.
 *
 * @author Jakub D.
 */
public class ProjectManagerFrame extends javax.swing.JFrame {

    public ProjectManagerFrame() {
        initComponents();

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
        this.masksPathField.setText(project.getMaskPath());

        if (project.getSuiteDescriptor() != null) {
            Perception perception = project.getSuiteDescriptor().getGlobalConfiguration().getPerception();
            if (perception.getOnePixelTreshold() != null)
                this.pixelTresField.setText("" + perception.getOnePixelTreshold());
            if (perception.getGlobalDifferenceTreshold() != null)
                this.diffTresField.setText("" + perception.getGlobalDifferenceTreshold());
            if (perception.getGlobalDifferenceAmount() != null)
                this.diffAmountField.setText("" + perception.getGlobalDifferenceAmount());
        }
    }

    public void createTree() {
        this.setVisible(true);
        projectTree.setCellRenderer(new CustomTreeRenderer());
        updateTreeModel();
    }

    public void updateTreeModel() {
        DefaultTreeModel model = new DefaultTreeModel(Main.mainProject.getRoot());
        projectTree.setModel(model);
        projectTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.validate();
        for (int i = 0; i < projectTree.getRowCount(); i++)
            projectTree.expandRow(i);
    }

    /**
     * Generate Double/Single view based on current test. Fired mainly when we
     * click on test in tree.
     *
     */
    public void putTestIntoView() {
        TestCase node = (TestCase) projectTree.getLastSelectedPathComponent();
        if (node == null)
            return;

        if (node.isLeaf()) {
            TestCase current = Main.mainProject.findTest(node.getPath());
            Main.mainProject.setCurrentCase(current);

            Main.interfaceFrame.clean();
            JPanel panel = Main.interfaceFrame.getMainPanel();

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

        if (node == null)
            return;

        if (node.getName().startsWith("Mask ")) {
            Main.mainProject.getMaskManager().setCurrentMask(node);
            infoTextArea.setText(node.getInfo());
            Main.interfaceFrame.repaint();
        }
        if (node.getName().startsWith("Rect ")) {
            infoTextArea.setText(node.getShape().toString());
        }
    }

    private void setCheckBoxesTo(boolean value) {
        sameCheckBox.setSelected(value);
        pSameCheckBox.setSelected(value);
        diffCheckBox.setSelected(value);
        notCheckBox.setSelected(value);
        errorCheckBox.setSelected(value);
    }

    public void updateCheckBoxes(RushEyeStatistics stats) {
        sameCheckBox.setText("Same (" + stats.getValue(ResultConclusion.SAME) + ")");
        pSameCheckBox.setText("Perceptually same (" + stats.getValue(ResultConclusion.PERCEPTUALLY_SAME) + ")");
        diffCheckBox.setText("Differ (" + stats.getValue(ResultConclusion.DIFFER) + ")");
        notCheckBox.setText("Not tested (" + stats.getValue(ResultConclusion.NOT_TESTED) + ")");
        errorCheckBox.setText("Error (" + stats.getValue(ResultConclusion.ERROR) + ")");

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

    public void toggleRunAll() {
        if (runAllButton.getText().equals("Run all"))
            runAllButton.setText("Stop");
        else
            runAllButton.setText("Run all");
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
        managerPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoTextArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        projectTree = new javax.swing.JTree();
        filterField = new javax.swing.JTextField();
        runAllButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        prevButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        posButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
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
        jProgressBar1 = new javax.swing.JProgressBar();
        masksPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        maskTree = new javax.swing.JTree();
        addMaskButton = new javax.swing.JButton();
        removeMaskButton = new javax.swing.JButton();
        saveMaskButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        removeSuiteMaskButton = new javax.swing.JButton();
        addSuiteMaskButton = new javax.swing.JButton();
        addToSuiteMaskButton = new javax.swing.JButton();
        configPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        patternsPathField = new javax.swing.JTextField();
        samplesPathField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        masksPathField = new javax.swing.JTextField();
        patternsButton = new javax.swing.JButton();
        samplesButton = new javax.swing.JButton();
        masksButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        pixelTresField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        diffTresField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        diffAmountField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setTitle("ProjectManager");

        infoTextArea.setColumns(20);
        infoTextArea.setEditable(false);
        infoTextArea.setLineWrap(true);
        infoTextArea.setRows(5);
        jScrollPane1.setViewportView(infoTextArea);

        jScrollPane2.setMaximumSize(null);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Test Cases");
        projectTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        projectTree.setRootVisible(false);
        jScrollPane2.setViewportView(projectTree);

        filterField.setFont(new java.awt.Font("Ubuntu", 2, 15)); // NOI18N

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

        posButton.setText("Accept");
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

        javax.swing.GroupLayout managerPanelLayout = new javax.swing.GroupLayout(managerPanel);
        managerPanel.setLayout(managerPanelLayout);
        managerPanelLayout.setHorizontalGroup(
            managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(managerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .addGroup(managerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(51, 51, 51)
                        .addComponent(filterField))
                    .addComponent(jSeparator4)
                    .addGroup(managerPanelLayout.createSequentialGroup()
                        .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(showSameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(showDiffButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(showAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(showNotButton, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(notCheckBox)
                            .addComponent(pSameCheckBox)
                            .addComponent(diffCheckBox)
                            .addComponent(errorCheckBox)
                            .addComponent(sameCheckBox))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(managerPanelLayout.createSequentialGroup()
                        .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, managerPanelLayout.createSequentialGroup()
                                .addComponent(prevButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(runAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(posButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        managerPanelLayout.setVerticalGroup(
            managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, managerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(managerPanelLayout.createSequentialGroup()
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
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, managerPanelLayout.createSequentialGroup()
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(managerPanelLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(runAllButton))
                    .addGroup(managerPanelLayout.createSequentialGroup()
                        .addGroup(managerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nextButton)
                            .addComponent(prevButton)
                            .addComponent(posButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Manager", managerPanel);

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        maskTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane3.setViewportView(maskTree);

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

        saveMaskButton.setText("Save image");
        saveMaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMaskButtonActionPerformed(evt);
            }
        });

        jLabel9.setText("Masks in manager :");

        jLabel10.setText("Masks in suite :");

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList1);

        removeSuiteMaskButton.setText("Remove");
        removeSuiteMaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSuiteMaskButtonActionPerformed(evt);
            }
        });

        addSuiteMaskButton.setText("Add");
        addSuiteMaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSuiteMaskButtonActionPerformed(evt);
            }
        });

        addToSuiteMaskButton.setText("Add to suite");
        addToSuiteMaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToSuiteMaskButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout masksPanelLayout = new javax.swing.GroupLayout(masksPanel);
        masksPanel.setLayout(masksPanelLayout);
        masksPanelLayout.setHorizontalGroup(
            masksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(masksPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(masksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3)
                    .addGroup(masksPanelLayout.createSequentialGroup()
                        .addGroup(masksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(masksPanelLayout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(masksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(addMaskButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(removeMaskButton, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                                    .addComponent(saveMaskButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addToSuiteMaskButton, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)))
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addGroup(masksPanelLayout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(masksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(addSuiteMaskButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(removeSuiteMaskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        masksPanelLayout.setVerticalGroup(
            masksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(masksPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(masksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(masksPanelLayout.createSequentialGroup()
                        .addComponent(addMaskButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeMaskButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveMaskButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addToSuiteMaskButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(masksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(masksPanelLayout.createSequentialGroup()
                        .addComponent(addSuiteMaskButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeSuiteMaskButton)))
                .addContainerGap(204, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Masks", masksPanel);

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

        patternsButton.setText("Set");
        patternsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patternsButtonActionPerformed(evt);
            }
        });

        samplesButton.setText("Set");
        samplesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                samplesButtonActionPerformed(evt);
            }
        });

        masksButton.setText("Set");
        masksButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masksButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("One pixel treshold :");

        jLabel6.setText("Global difference treshold :");

        jLabel7.setText("Global difference amount :");

        jLabel8.setText("%");

        javax.swing.GroupLayout configPanelLayout = new javax.swing.GroupLayout(configPanel);
        configPanel.setLayout(configPanelLayout);
        configPanelLayout.setHorizontalGroup(
            configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(configPanelLayout.createSequentialGroup()
                        .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(patternsPathField)
                            .addComponent(samplesPathField)
                            .addComponent(masksPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(masksButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(samplesButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(patternsButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(configPanelLayout.createSequentialGroup()
                        .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(pixelTresField, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(diffTresField, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addGroup(configPanelLayout.createSequentialGroup()
                                .addComponent(diffAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        configPanelLayout.setVerticalGroup(
            configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(patternsPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(patternsButton))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(samplesPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(samplesButton))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(masksPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(masksButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pixelTresField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diffTresField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(diffAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap(256, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Configuration", configPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 712, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
        Main.interfaceFrame.getStatFrame().update(Main.mainProject);

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
        updateCheckBoxes(Main.mainProject.getStatistics());
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
        if (Main.mainProject.isParsing()) {
            Main.mainProject.getParserThread().getParser().setValid(false);
        } else {
            try {
                Main.mainProject.parse();
            } catch (ManagerException ex) {
                JOptionPane.showMessageDialog(Main.interfaceFrame, ex.getMessage(), "Parser Exception", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_runAllButtonActionPerformed

    private void showAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAllButtonActionPerformed
        setCheckBoxesTo(true);

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
        setCheckBoxesTo(false);
        sameCheckBox.setSelected(true);
        pSameCheckBox.setSelected(true);
        filter();
    }//GEN-LAST:event_showSameButtonActionPerformed

    private void showNotButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showNotButtonActionPerformed
        setCheckBoxesTo(false);
        notCheckBox.setSelected(true);
        filter();
    }//GEN-LAST:event_showNotButtonActionPerformed

    private void showDiffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showDiffButtonActionPerformed
        setCheckBoxesTo(false);
        diffCheckBox.setSelected(true);
        filter();
    }//GEN-LAST:event_showDiffButtonActionPerformed

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
        filter();
    }//GEN-LAST:event_pSameCheckBoxActionPerformed

    private void patternsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patternsButtonActionPerformed
        Main.interfaceFrame.setPatternsAction();
    }//GEN-LAST:event_patternsButtonActionPerformed

    private void samplesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_samplesButtonActionPerformed
        Main.interfaceFrame.setSamplesAction();
    }//GEN-LAST:event_samplesButtonActionPerformed

    private void masksButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masksButtonActionPerformed
        Main.interfaceFrame.setMasksAction();
    }//GEN-LAST:event_masksButtonActionPerformed

    private void removeSuiteMaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSuiteMaskButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_removeSuiteMaskButtonActionPerformed

    private void addSuiteMaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSuiteMaskButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addSuiteMaskButtonActionPerformed

    private void addToSuiteMaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToSuiteMaskButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addToSuiteMaskButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addMaskButton;
    private javax.swing.JButton addSuiteMaskButton;
    private javax.swing.JButton addToSuiteMaskButton;
    private javax.swing.JPanel configPanel;
    private javax.swing.JTextField diffAmountField;
    private javax.swing.JCheckBox diffCheckBox;
    private javax.swing.JTextField diffTresField;
    private javax.swing.JCheckBox errorCheckBox;
    private javax.swing.JTextField filterField;
    private javax.swing.JTextArea infoTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel managerPanel;
    private javax.swing.JTree maskTree;
    private javax.swing.JButton masksButton;
    private javax.swing.JPanel masksPanel;
    private javax.swing.JTextField masksPathField;
    private javax.swing.JButton nextButton;
    private javax.swing.JCheckBox notCheckBox;
    private javax.swing.JCheckBox pSameCheckBox;
    private javax.swing.JButton patternsButton;
    private javax.swing.JTextField patternsPathField;
    private javax.swing.JTextField pixelTresField;
    private javax.swing.JButton posButton;
    private javax.swing.JButton prevButton;
    private javax.swing.JTree projectTree;
    private javax.swing.JButton removeMaskButton;
    private javax.swing.JButton removeSuiteMaskButton;
    private javax.swing.JButton runAllButton;
    private javax.swing.JCheckBox sameCheckBox;
    private javax.swing.JButton samplesButton;
    private javax.swing.JTextField samplesPathField;
    private javax.swing.JButton saveMaskButton;
    private javax.swing.JButton showAllButton;
    private javax.swing.JButton showDiffButton;
    private javax.swing.JButton showNotButton;
    private javax.swing.JButton showSameButton;
    // End of variables declaration//GEN-END:variables
}
