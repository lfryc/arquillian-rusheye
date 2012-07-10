/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.parser;

import com.ctc.wstx.exc.WstxParsingException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.io.FileUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.ri.Stax2FilteredStreamReader;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.codehaus.stax2.validation.XMLValidationSchemaFactory;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.project.testcase.TestCase;
import org.jboss.rusheye.suite.*;

/**
 *
 * @author hcube
 */
public class ManagerParser extends Parser {

    private List<Test> parsedTests = new ArrayList<Test>();

    @Override
    protected void parseFile(File file, boolean tmpfile) {
        VisualSuite visualSuite = null;
        try {
            XMLValidationSchemaFactory schemaFactory = XMLValidationSchemaFactory.newInstance(XMLValidationSchema.SCHEMA_ID_W3C_SCHEMA);
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

            visualSuite = new VisualSuite();
            handler.setVisualSuite(visualSuite);
            handler.getContext().invokeListeners().onSuiteStarted(visualSuite);

            listener.registerListener(new UniqueIdentityChecker(handler.getContext()));

            while (filteredReader.hasNext()) {
                try {
                    // go on the start of the next tag
                    filteredReader.nextTag();

                    Object o = um.unmarshal(reader);
                    if (o instanceof GlobalConfiguration) {
                        GlobalConfiguration globalConfiguration = (GlobalConfiguration) o;
                        handler.getContext().setCurrentConfiguration(globalConfiguration);
                        visualSuite.setGlobalConfiguration(globalConfiguration);
                        handler.getContext().invokeListeners().onConfigurationReady(visualSuite);

                        RetriverInjector retriverInjector = new RetriverInjector(this);
                        for (Mask mask : globalConfiguration.getMasks()) {
                            retriverInjector.afterUnmarshal(mask, null);
                        }
                        listener.registerListener(retriverInjector);
                    }
                    if (o instanceof Test) {
                        Test test = (Test) o;
                        handler.getContext().setCurrentConfiguration(test);
                        handler.getContext().setCurrentTest(test);
                        for (Pattern pattern : test.getPatterns()) {
                            handler.getContext().invokeListeners().onPatternReady(test, pattern);
                        }
                        Test testWrapped = ConfigurationCompiler.wrap(test, visualSuite.getGlobalConfiguration());
                        handler.getContext().invokeListeners().onTestReady(testWrapped);
                        
                        for(Pattern pattern : testWrapped.getPatterns()){
                            TestCase managerTest = Main.mainProject.findTest(testWrapped.getName(), pattern.getName());
                            managerTest.setConclusion(pattern.getConclusion());
                            Main.mainProject.setCurrentCase(managerTest);
                            Main.projectFrame.updateIcons();
                        }
                        
                        parsedTests.add(testWrapped);
                    }
                } catch (WstxParsingException e) {
                    // intentionally blank - wrong end of document detection
                }

            }
        } catch (XMLStreamException e) {
            throw handleParsingException(e, e);
        } catch (JAXBException e) {
            throw handleParsingException(e, e.getLinkedException());
        } finally {
            if (visualSuite != null && handler.getContext() != null) {
                handler.getContext().invokeListeners().onSuiteReady(visualSuite);
            }
            if (tmpfile) {
                FileUtils.deleteQuietly(file);
            }
        }
    }

    public TestCase parseFileToManagerCases(File file) {
        TestCase testCase = new TestCase();
        testCase.setName("Test Cases");
        try {
            XMLValidationSchemaFactory schemaFactory = XMLValidationSchemaFactory.newInstance(XMLValidationSchema.SCHEMA_ID_W3C_SCHEMA);
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
    
    public List<Test> getParsedTests(){
        return parsedTests;
    }
}
