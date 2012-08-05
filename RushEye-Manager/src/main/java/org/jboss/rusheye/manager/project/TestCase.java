/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project;

import java.awt.image.BufferedImage;
import java.util.List;
import org.jboss.rusheye.core.DefaultImageComparator;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.gui.view.image.ImagePool;
import org.jboss.rusheye.manager.project.tree.TreeNodeImpl;
import org.jboss.rusheye.parser.DefaultConfiguration;
import org.jboss.rusheye.result.ResultEvaluator;
import org.jboss.rusheye.suite.ComparisonResult;
import org.jboss.rusheye.suite.Configuration;
import org.jboss.rusheye.suite.ResultConclusion;

/**
 * Extension of TreeNodeImpl. It contains data regarding tests, like result
 * conclusion, pattern filename, and images.
 *
 * @author Jakub D.
 */
public class TestCase extends TreeNodeImpl {

    private ResultConclusion conclusion;
    private boolean checked;
    private String filename;
    private ImagePool pool;

    public TestCase() {
        super();
        pool = new ImagePool();
    }

    public ResultConclusion getConclusion() {
        return conclusion;
    }

    public void setConclusion(ResultConclusion conclusion) {
        this.conclusion = conclusion;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public ImagePool getPool() {
        return pool;
    }

    /**
     * Performs comparison of pattern and sample. Sets diff image and
     * conclusion.
     */
    public void loadDiff() {
        Configuration configuration = Main.mainProject.getSuiteDescriptor().getGlobalConfiguration();
        ComparisonResult result = new DefaultImageComparator().compare(getImage(ImagePool.PATTERN), getImage(ImagePool.SAMPLE), configuration.getPerception(),
                configuration.getMasks());

        if (conclusion == null || conclusion == ResultConclusion.NOT_TESTED) {
            conclusion = new ResultEvaluator().evaluate(configuration.getPerception(), result);
            Main.mainProject.getStatistics().addValue(conclusion, 1);
            Main.mainProject.getStatistics().addValue(ResultConclusion.NOT_TESTED, -1);

            Main.interfaceFrame.getStatFrame().update(Main.mainProject);
            Main.interfaceFrame.getProjectFrame().updateCheckBoxes(Main.mainProject.getStatistics());
        }
        BufferedImage diff = result.getDiffImage();

        pool.put(ImagePool.DIFF, diff);
    }

    /**
     * Removes all diff images from TestCases. used when we change path to
     * patterns/samples and diff images become invalid.
     */
    public void removeDiffRecursive() {
        if (this.isLeaf())
            pool.remove(ImagePool.DIFF);
        for (int i = 0; i < this.getChildCount(); ++i)
            ((TestCase) this.getChildAt(i)).removeDiffRecursive();
    }

    public BufferedImage getImage(String key) {
        if (pool.get(key) == null) {
            if (key.equals(ImagePool.PATTERN)) {
                pool.put(key, Main.mainProject.getPatternPath() + "/" + filename);
                if (pool.get(key) != null) {
                    return pool.get(key);
                } else {
                    if (pool.get(ImagePool.FAKE) == null) {
                        pool.put(ImagePool.FAKE, "empty.png");
                    }
                    return pool.get(ImagePool.FAKE);
                }
            } else if (key.equals(ImagePool.SAMPLE)) {
                pool.put(key, Main.mainProject.getSamplesPath() + "/" + filename);
                if (pool.get(key) != null) {
                    return pool.get(key);
                } else {
                    if (pool.get(ImagePool.FAKE) == null) {
                        pool.put(ImagePool.FAKE, "empty.png");
                    }
                    return pool.get(ImagePool.FAKE);
                }
            } else if (key.equals(ImagePool.DIFF)) {
                loadDiff();
                return pool.get(key);
            } else
                return null;
        } else {
            return pool.get(key);
        }
    }

    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * Finds test recursively in node and its children.
     */
    public TestCase findTest(String path) {
        if (this.getPath().equals(path)) {
            return this;
        } else {
            for (int i = 0; i < this.getChildCount(); ++i) {
                TestCase child = (TestCase) this.getChildAt(i);
                TestCase result = child.findTest(path);
                if (result != null)
                    return result;

            }
        }

        return null;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Sets visibility of nodes,based on ResultConclusion of the test.
     */
    public void setVisibility(List<ResultConclusion> con) {
        boolean condition = false;
        for (ResultConclusion c : con) {
            if (conclusion == c) {
                condition = true;
                break;
            }
        }

        if (conclusion == null || condition)
            this.setVisible(true);
        else
            this.setVisible(false);

        for (int i = 0; i < this.getAllChildren().size(); ++i)
            ((TestCase) this.getAllChildren().get(i)).setVisibility(con);

        collapseInvalidLeafs();
    }

    /**
     * Sets visibility of nodes,based on String pattern matching.
     */
    public void setVisibility(String regexp) {
        if (conclusion == null || this.getName().toLowerCase().contains(regexp.toLowerCase()))
            this.setVisible(true);
        else
            this.setVisible(false);

        for (int i = 0; i < this.getAllChildren().size(); ++i)
            ((TestCase) this.getAllChildren().get(i)).setVisibility(regexp);

        collapseInvalidLeafs();
    }

    /**
     * Hides test nodes, where all patterns have been filtered out.
     */
    private void collapseInvalidLeafs() {
        if (conclusion == null && this.getChildCount() == 0)
            this.setVisible(false);

        for (int i = 0; i < this.getAllChildren().size(); ++i)
            ((TestCase) this.getAllChildren().get(i)).collapseInvalidLeafs();
    }

    /**
     * Recursively changes visibility of node and children to true.
     */
    public void setAllVisible() {
        this.setVisible(true);

        for (int i = 0; i < this.getAllChildren().size(); ++i)
            ((TestCase) this.getAllChildren().get(i)).setAllVisible();
    }
}
