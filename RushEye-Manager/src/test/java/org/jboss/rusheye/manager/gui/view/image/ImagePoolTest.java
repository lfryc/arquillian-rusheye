/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.view.image;

import junit.framework.TestCase;

/**
 *
 * @author hcube
 */
public class ImagePoolTest extends TestCase {
    
    public ImagePoolTest(String testName) {
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

    public void testPut() {
        ImagePool pool = new ImagePool();
        pool.put(ImagePool.PATTERN, "src/test/resources/ActionParameterTestCase.testSelectingNames.png");
        
        assertNotNull(pool.get(ImagePool.PATTERN));
    }

}
