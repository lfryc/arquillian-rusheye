/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project.testcase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.jboss.rusheye.parser.ManagerParser;
import org.jboss.rusheye.suite.ResultConclusion;
import org.jboss.rusheye.suite.VisualSuite;

/**
 *
 * @author hcube
 */
public class TestCaseTest extends TestCase {

    public TestCaseTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCaseFinding() {
        
        // given

        ManagerParser parser = new ManagerParser();
        VisualSuite suite = parser.loadSuite(new File("src/test/resources/suite.xml"));

        // when

        org.jboss.rusheye.manager.project.TestCase root = parser.parseSuiteToManagerCases(suite);
        
        // then

        //pattern
        org.jboss.rusheye.manager.project.TestCase testCase = (org.jboss.rusheye.manager.project.TestCase) root.getChildAt(0).getChildAt(0);

        //get path of leaf
        String path = testCase.getPath();

        //find the same leaf through root, using path
        org.jboss.rusheye.manager.project.TestCase testCase2 = root.findTest(path);

        assertEquals(testCase, testCase2);
    }

    public void testFileName() {
        // given

        ManagerParser parser = new ManagerParser();
        VisualSuite suite = parser.loadSuite(new File("src/test/resources/suite.xml"));

        // when

        org.jboss.rusheye.manager.project.TestCase root = parser.parseSuiteToManagerCases(suite);
        
        // then

        //pattern
        org.jboss.rusheye.manager.project.TestCase testCase = (org.jboss.rusheye.manager.project.TestCase) root.getChildAt(0).getChildAt(0);

        assertEquals(testCase.getFilename(), "ActionParameterTestCase.testSelectingNames.png");
    }
    
    public void testVisibility() {
        // given

        ManagerParser parser = new ManagerParser();
        VisualSuite suite = parser.loadSuite(new File("src/test/resources/suite.xml"));

        // when

        org.jboss.rusheye.manager.project.TestCase root = parser.parseSuiteToManagerCases(suite);
        
        // then

        //pattern
        org.jboss.rusheye.manager.project.TestCase testCase = (org.jboss.rusheye.manager.project.TestCase) root.getChildAt(0).getChildAt(0);

        testCase.setConclusion(ResultConclusion.DIFFER);
        
        //we filtered out all other cases
        List<ResultConclusion> list = new ArrayList<ResultConclusion>();
        list.add(ResultConclusion.DIFFER);
        root.setVisibility(list);
        assertEquals(root.getChildCount(), 1);
        
        root.setAllVisible();
        assertEquals(root.getChildCount(), 6);
    }
}
