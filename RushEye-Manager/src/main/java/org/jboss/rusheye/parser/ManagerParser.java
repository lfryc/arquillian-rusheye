/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.parser;

import com.ctc.wstx.exc.WstxParsingException;
import java.io.File;
import java.io.InputStream;
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
import org.jboss.rusheye.RushEye;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.gui.charts.RushEyeStatistics;
import org.jboss.rusheye.manager.project.TestCase;
import org.jboss.rusheye.manager.project.observable.Observed;
import org.jboss.rusheye.manager.project.observable.Observer;
import org.jboss.rusheye.suite.*;

/**
 * Custom parser used by Manager. Besides normal parsing it creates tree of
 * tests for manager project and updates them during parsing.
 *
 * @author Jakub D.
 */
public class ManagerParser extends Parser implements Observed {

    private RushEyeStatistics statistics;
    private List<Observer> list;
    private volatile boolean valid = true;

    public ManagerParser() {
        super();
        statistics = new RushEyeStatistics();
        list = new ArrayList<Observer>();
    }

    protected void parseSuiteFile(File file, boolean tmpfile) {
        Main.mainProject.setParsing(true);
        statistics = new RushEyeStatistics();
        VisualSuite visualSuite = null;
        try {
            XMLValidationSchemaFactory schemaFactory = XMLValidationSchemaFactory.newInstance(XMLValidationSchema.SCHEMA_ID_W3C_SCHEMA);
            URL schemaURL = getClass().getClassLoader().getResource(RushEye.RESOURCE_VISUAL_SUITE);
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
                if (!isValid())
                    break;
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
                    if (o instanceof Case) {
                        Case case1 = (Case) o;
                        Case caseWrapped = ConfigurationCompiler.wrap(case1, visualSuite.getGlobalConfiguration());
                        handler.getContext().setCurrentConfiguration(caseWrapped);
                        handler.getContext().setCurrentCase(caseWrapped);
                        for (Test test : caseWrapped.getTests()) {
                            Test testWrapped = ConfigurationCompiler.wrap(test, caseWrapped);
                            handler.getContext().setCurrentConfiguration(testWrapped);
                            handler.getContext().setCurrentTest(testWrapped);
                            for (Pattern pattern : testWrapped.getPatterns()) {
                                handler.getContext().invokeListeners().onPatternReady(testWrapped, pattern);
                            }
                            handler.getContext().invokeListeners().onTestReady(testWrapped);

                            for (Pattern pattern : testWrapped.getPatterns()) {
                                TestCase managerTest = Main.mainProject.findTest(testWrapped.getName(), pattern.getName());
                                managerTest.setConclusion(pattern.getConclusion());
                                statistics.addValue(pattern.getConclusion(), 1);

                                Main.mainProject.setCurrentCase(managerTest);
                                Main.interfaceFrame.getProjectFrame().updateIcons();

                                this.notifyObservers();
                            }
                        }
                        handler.getContext().invokeListeners().onCaseReady(caseWrapped);

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

        Main.mainProject.setParsing(false);
        valid = true;
        Main.interfaceFrame.getProjectFrame().toggleRunAll();

    }

    public VisualSuite loadSuite(File file) {
        VisualSuite visualSuite = null;
        try {
            XMLValidationSchemaFactory schemaFactory = XMLValidationSchemaFactory.newInstance(XMLValidationSchema.SCHEMA_ID_W3C_SCHEMA);
            URL schemaURL = getClass().getClassLoader().getResource(RushEye.RESOURCE_VISUAL_SUITE);
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

            while (filteredReader.hasNext()) {
                try {
                    // go on the start of the next tag
                    filteredReader.nextTag();

                    Object o = um.unmarshal(reader);
                    if (o instanceof GlobalConfiguration) {
                        GlobalConfiguration globalConfiguration = (GlobalConfiguration) o;
                        visualSuite.setGlobalConfiguration(globalConfiguration);
                    }
                    if (o instanceof Case) {
                        Case case1 = (Case) o;
                        visualSuite.getCases().add(case1);
                    }
                } catch (WstxParsingException e) {
                    // intentionally blank - wrong end of document detection
                }

            }
        } catch (XMLStreamException e) {
            throw handleParsingException(e, e);
        } catch (JAXBException e) {
            throw handleParsingException(e, e.getLinkedException());
        }

        return visualSuite;
    }

    public TestCase parseSuiteToManagerCases(VisualSuite suite) {
        statistics = new RushEyeStatistics();
        TestCase root = new TestCase();
        root.setName("Test Cases");
        for (Case case1 : suite.getCases()) {
            TestCase caseCase = new TestCase();
            caseCase.setName(case1.getName());
            caseCase.setAllowsChildren(true);
            for (Test test : case1.getTests()) {
                TestCase testCase = new TestCase();
                testCase.setName(test.getName());
                testCase.setAllowsChildren(true);

                for (Pattern pattern : test.getPatterns()) {
                    statistics.addValue(ResultConclusion.NOT_TESTED, 1);
                    TestCase patternCase = new TestCase();
                    patternCase.setName(pattern.getName());
                    patternCase.setFilename(pattern.getSource());
                    patternCase.setConclusion(ResultConclusion.NOT_TESTED);
                    testCase.addChild(patternCase);
                }
                caseCase.addChild(testCase);
            }
            root.addChild(caseCase);
        }
        notifyObservers();

        return root;
    }

    @Override
    public void addObserver(Observer o) {
        list.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        list.remove(o);
    }

    private void notifyObservers() {
        for (Observer o : list) {
            o.update(this);
        }
    }

    public RushEyeStatistics getStatistics() {
        return statistics;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
