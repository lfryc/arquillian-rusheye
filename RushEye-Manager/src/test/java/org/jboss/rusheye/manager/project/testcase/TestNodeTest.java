/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.project.testcase;

import junit.framework.TestCase;

/**
 *
 * @author hcube
 */
public class TestNodeTest extends TestCase {

    public TestNodeTest(String testName) {
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

    public void testEmptyNode() {
        TestNode node = new TestNode();
        node.setName("Parent");

        // empty node - no children
        assertTrue(node.isLeaf());
    }

    public void testNodeWithChildren() {
        TestNode node = new TestNode();
        node.setName("Parent");

        TestNode child1 = new TestNode();
        child1.setName("1");
        TestNode child2 = new TestNode();
        child2.setName("2");

        node.addChild(child1);
        node.addChild(child2);

        assertFalse(node.isLeaf());
        //node with 2 children
        assertEquals(node.getChildCount(), 2);

        child1.setVisible(false);
        //only 1 child is visible now, child count returns only visible
        assertEquals(node.getChildCount(), 1);

        //path
        assertEquals(child1.getPath(), "Parent.1");

        //comparable
        assertEquals(child1.equals(child2), false);

        child2.setName(child1.getName());

        assertEquals(child1.equals(child2), true);
    }

    public void testCompare() {
        TestNode child1 = new TestNode();
        child1.setName("1");
        TestNode child2 = new TestNode();
        child2.setName("2");

        //comparable
        assertEquals(child1.equals(child2), false);

        child2.setName(child1.getName());

        assertEquals(child1.equals(child2), true);
    }
}
