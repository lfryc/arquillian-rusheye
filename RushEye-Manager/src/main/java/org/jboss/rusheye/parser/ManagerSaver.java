/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jboss.rusheye.RushEye;
import org.jboss.rusheye.parser.listener.CompareListener;
import org.jboss.rusheye.result.statistics.OverallStatistics;
import org.jboss.rusheye.result.storage.FileStorage;
import org.jboss.rusheye.result.writer.FileResultWriter;
import org.jboss.rusheye.retriever.mask.MaskFileRetriever;
import org.jboss.rusheye.retriever.pattern.PatternFileRetriever;
import org.jboss.rusheye.retriever.sample.FileSampleRetriever;
import org.jboss.rusheye.suite.Mask;
import org.jboss.rusheye.suite.Test;
import org.jboss.rusheye.suite.VisualSuite;

/**
 *
 * @author hcube
 */
public class ManagerSaver {
    private VisualSuite suite;
    

    private Document document;
    private Namespace ns;
    
    
    public ManagerSaver(VisualSuite s){
        this.suite = s;
    }
    
    public void save(){
        try{
        OutputStream os = new FileOutputStream("tmp.xml");
        crawl(os);
        os.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
   
    public void crawl(OutputStream os){
        document = DocumentHelper.createDocument();
        addDocumentRoot();
        writeDocument(os);
    }
    
    private void writeDocument(OutputStream os){
        OutputFormat format = OutputFormat.createPrettyPrint();

        try {
            XMLWriter writer = new XMLWriter(os, format);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDocumentRoot() {
        ns = Namespace.get(RushEye.NAMESPACE_VISUAL_SUITE);

        Element root = document.addElement(QName.get("visual-suite", ns));

        Namespace xsi = Namespace.get("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        QName schemaLocation = QName.get("schemaLocation", xsi);

        root.addNamespace("", ns.getURI());
        root.addNamespace(xsi.getPrefix(), xsi.getURI());
        root.addAttribute(schemaLocation, ns.getURI() + " " + RushEye.SCHEMA_LOCATION_VISUAL_SUITE);

        Element globalConfiguration = root.addElement(QName.get("global-configuration", ns));
        addSuiteListener(globalConfiguration);
        addRetrievers(globalConfiguration);
        addPerception(globalConfiguration);
        addMasks(globalConfiguration);
        addTests(root);
    }

    private void addSuiteListener(Element globalConfiguration) {
        Element suiteListener = globalConfiguration.addElement(QName.get("listener", ns));
        suiteListener.addAttribute("type", CompareListener.class.getName());

        suiteListener.addElement(QName.get("result-collector", ns)).addText(ManagerResultCollector.class.getName());
        suiteListener.addElement(QName.get("result-storage", ns)).addText(FileStorage.class.getName());
        suiteListener.addElement(QName.get("result-writer", ns)).addText(FileResultWriter.class.getName());
        suiteListener.addElement(QName.get("result-statistics", ns)).addText(OverallStatistics.class.getName());
    }

    private void addRetrievers(Element globalConfiguration) {
        globalConfiguration.addElement(QName.get("pattern-retriever", ns)).addAttribute("type", PatternFileRetriever.class.getName());
        globalConfiguration.addElement(QName.get("mask-retriever", ns)).addAttribute("type", MaskFileRetriever.class.getName());
        globalConfiguration.addElement(QName.get("sample-retriever", ns)).addAttribute("type",
                FileSampleRetriever.class.getName());
    }

    private void addPerception(Element base) {
        Element perception = base.addElement(QName.get("perception", ns));

        if (suite.getGlobalConfiguration().getPerception().getOnePixelTreshold() != null) {
            perception.addElement(QName.get("one-pixel-treshold", ns)).addText(String.valueOf(suite.getGlobalConfiguration().getPerception().getOnePixelTreshold()));
        }
        if (suite.getGlobalConfiguration().getPerception().getGlobalDifferenceTreshold() != null) {
            perception.addElement(QName.get("global-difference-treshold", ns))
                    .addText(String.valueOf(suite.getGlobalConfiguration().getPerception().getGlobalDifferenceTreshold()));
        }
        if (suite.getGlobalConfiguration().getPerception().getGlobalDifferenceAmount() != null) {
            perception.addElement(QName.get("global-difference-amount", ns)).addText(suite.getGlobalConfiguration().getPerception().getGlobalDifferenceAmount());
        }
    }

    private void addMasks( Element base) {
            for (Mask m : suite.getGlobalConfiguration().getMasks()) {

                Element mask = base.addElement(QName.get("mask", ns)).addAttribute("id", m.getId())
                        .addAttribute("type", m.getType().value()).addAttribute("source", m.getSource());

                    if(m.getVerticalAlign()!=null)
                    mask.addAttribute("vertical-align",m.getVerticalAlign().value());
                                        if(m.getHorizontalAlign()!=null)
                    mask.addAttribute("horizontal-align",m.getHorizontalAlign().value());
            }
        
    }

    private void addTests(Element root) {
        for(Test t : suite.getTests()){
                    Element test = root.addElement(QName.get("test", ns));
                    test.addAttribute("name", t.getName());

                    addPatterns(t,test);
            }
    }


    private void addPatterns(Test t, Element test) {
        for(org.jboss.rusheye.suite.Pattern p : t.getPatterns() ){
                    Element pattern = test.addElement(QName.get("pattern", ns));
                    pattern.addAttribute("name", p.getName());
                    pattern.addAttribute("source", p.getSource());
        }
    }

}
