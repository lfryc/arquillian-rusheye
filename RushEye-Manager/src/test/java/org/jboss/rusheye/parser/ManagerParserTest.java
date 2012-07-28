/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.parser;

import java.io.File;
import java.util.List;
import junit.framework.TestCase;

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

        ManagerParser parser = new ManagerParser();
        org.jboss.rusheye.manager.project.TestCase root = parser.parseFileToManagerCases(new File("src/test/resources/suite.xml"));

        assertEquals(root.getChildCount(), 6);
        for (int i = 0; i < root.getChildCount(); ++i) {
            org.jboss.rusheye.manager.project.TestCase test = (org.jboss.rusheye.manager.project.TestCase) root.getChildAt(i);

            //test has patterns as leafs
            assertEquals(test.isLeaf(), false);

            //one pattern per test it this suite
            assertEquals(test.getChildCount(), 1);
            
            //pattern should be leaf
            assertEquals(test.getChildAt(0).isLeaf(), true);
        }
    }
}
