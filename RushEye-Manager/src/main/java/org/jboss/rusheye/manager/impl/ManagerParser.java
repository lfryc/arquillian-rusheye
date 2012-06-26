/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.impl;

import java.io.File;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.ri.Stax2FilteredStreamReader;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.codehaus.stax2.validation.XMLValidationSchemaFactory;
import org.jboss.rusheye.manager.project.TestCase;
import org.jboss.rusheye.parser.Parser;
import org.jboss.rusheye.parser.UniqueIdentityChecker;
import org.jboss.rusheye.parser.UnmarshallerMultiListener;
import org.jboss.rusheye.suite.Pattern;
import org.jboss.rusheye.suite.Test;
import org.jboss.rusheye.suite.VisualSuite;

/**
 *
 * @author hcube
 */
public class ManagerParser extends Parser {
    
    public TestCase parseFileToManagerCases(File file){
        TestCase testCase = new TestCase();
        testCase.setName("Test Cases");
        try {
            XMLValidationSchemaFactory schemaFactory = XMLValidationSchemaFactory
                .newInstance(XMLValidationSchema.SCHEMA_ID_W3C_SCHEMA);
            URL schemaURL = getClass().getClassLoader().getResource("org/jboss/rusheye/visual-suite.xsd");
            XMLValidationSchema schema = schemaFactory.createSchema(schemaURL);

            XMLInputFactory2 factory = (XMLInputFactory2) XMLInputFactory.newInstance();

            StreamFilter filter = new StreamFilter() {
                @Override
                public boolean accept(XMLStreamReader reader) {
                    return reader.isStartElement();
                }
            };

            XMLStreamReader2 reader = factory.createXMLStreamReader(file);
            XMLStreamReader2 filteredReader = new Stax2FilteredStreamReader(reader, filter);

            reader.validateAgainst(schema);

            JAXBContext ctx = JAXBContext.newInstance(VisualSuite.class.getPackage().getName());
            Unmarshaller um = ctx.createUnmarshaller();

            UnmarshallerMultiListener listener = new UnmarshallerMultiListener();
            um.setListener(listener);

            // skip parsing of the first element - visual-suite
            filteredReader.nextTag();

            //listener.registerListener(new UniqueIdentityChecker(handler.getContext()));

            while (filteredReader.hasNext()) {
                    // go on the start of the next tag
                    filteredReader.nextTag();

                    Object o = um.unmarshal(reader);
                    if (o instanceof Test) {
                        Test test = (Test) o;
                        System.out.println(test.getName());
                        TestCase newCase = new TestCase();
                        newCase.setName(test.getName());
                        newCase.setParent(testCase);
                        newCase.setAllowsChildren(true);
                        testCase.addChild(newCase);
                        for (Pattern pattern : test.getPatterns()) {
                            System.out.println(pattern.getName());
                            TestCase patternCase = new TestCase();
                            patternCase.setName(pattern.getName());
                            patternCase.setFilename(pattern.getSource());
                            patternCase.setParent(newCase);
                            newCase.addChild(patternCase);
                        }
                    }
            }
        } catch (XMLStreamException e) {
            //throw handleParsingException(e, e);
        } catch (JAXBException e) {
            throw handleParsingException(e, e.getLinkedException());
        }
        
        return testCase;
    }
}
