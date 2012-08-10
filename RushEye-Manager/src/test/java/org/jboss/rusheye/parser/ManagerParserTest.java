/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.parser;

import java.io.File;
import junit.framework.TestCase;
import org.jboss.rusheye.suite.VisualSuite;

/**
 *
 * @author hcube
 */
public class ManagerParserTest extends TestCase {

    public ManagerParserTest(String testName) {
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

    /**
     * Test of parseFileToManagerCases method, of class ManagerParser.
     */
    public void testParseFileToManagerCases() {

        // given

        //ManagerParser parser = new ManagerParser();
        //VisualSuite suite = parser.loadSuite(new File("src/test/resources/suite.xml"));

        // when

        //org.jboss.rusheye.manager.project.TestCase root = parser.parseSuiteToManagerCases(suite);

        // then

        
        //assertEquals(root.getChildCount(), 2);

        //for (int i = 0; i < root.getChildCount(); ++i) {
           // org.jboss.rusheye.manager.project.TestCase test = (org.jboss.rusheye.manager.project.TestCase) root.getChildAt(i);

            //test has patterns as leafs
           // assertEquals(false, test.isLeaf());

            //one pattern per test it this suite
           // assertEquals(1, test.getChildCount());

            //pattern should be leaf
            //assertEquals(true, test.getChildAt(0).isLeaf());
        //}
    }
    

    /**
     * Test of loadSuite method, of class ManagerParser.
     */
    /*
    public void testLoadSuite() {

        //given

        ManagerParser parser = new ManagerParser();

        //when

        VisualSuite suite = parser.loadSuite(new File("src/test/resources/suite.xml"));

        //then

        //assertEquals(3, suite.getGlobalConfiguration().getMasks().size());
        //assertEquals(1.0f, suite.getGlobalConfiguration().getPerception().getGlobalDifferenceTreshold());
        //assertEquals(6, suite.getTests().size());
        //assertEquals(1, suite.getTests().get(0).getPatterns().size());
    }*/
}
