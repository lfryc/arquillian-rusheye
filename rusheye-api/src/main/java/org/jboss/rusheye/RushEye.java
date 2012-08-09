/**
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.rusheye;

/**
 * Global definitions for RushEye project.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class RushEye {

    // note: 1.0 is not available yet, use 1.1 instead once going Final
    public static final String SCHEMA_VERSION_SUFFIX = "0_2";
    
    /** Namespace for Visual Suite descriptor */
    public static final String NAMESPACE_VISUAL_SUITE = "http://www.jboss.org/rusheye/visual-suite";

    /** Namespace for Visual Suite Result descriptor */
    public static final String NAMESPACE_VISUAL_SUITE_RESULT = "http://www.jboss.org/rusheye/visual-suite-result";

    /** Schema Location for Visual Suite descriptor */
    public static final String SCHEMA_LOCATION_VISUAL_SUITE = "http://www.jboss.org/schema/arquillian/rusheye/visual-suite_" + SCHEMA_VERSION_SUFFIX + ".xsd";

    /** Schema Location for Visual Suite Result descriptor */
    public static final String SCHEMA_LOCATION_VISUAL_SUITE_RESULT = "http://www.jboss.org/schema/arquillian/rusheye/visual-suite-result_" + SCHEMA_VERSION_SUFFIX + ".xsd";
    
    /** Identifies the class-path resource where Visual Suite descriptor can be found */
    public static final String RESOURCE_VISUAL_SUITE = "org/jboss/rusheye/visual-suite_" + SCHEMA_VERSION_SUFFIX + ".xsd";
    
    /** Identifies the class-path resource where Visual Suite Result descriptor can be found */
    public static final String RESOURCE_VISUAL_SUITE_RESULT = "org/jboss/rusheye/visual-suite-result_" + SCHEMA_VERSION_SUFFIX + ".xsd";

    private RushEye() {
    }
}
