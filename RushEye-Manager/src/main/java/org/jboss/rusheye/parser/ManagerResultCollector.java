/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.parser;

import org.jboss.rusheye.internal.Instantiator;
import org.jboss.rusheye.manager.Main;
import org.jboss.rusheye.manager.project.TestCase;
import org.jboss.rusheye.result.ResultCollectorAdapter;
import org.jboss.rusheye.result.ResultEvaluator;
import org.jboss.rusheye.result.ResultStatistics;
import org.jboss.rusheye.result.ResultStorage;
import org.jboss.rusheye.result.writer.ResultWriter;
import org.jboss.rusheye.suite.*;

/**
 * Custom result collector used by Manager. It is not really different from
 * ResultCollectorImpl, but it takes into consideration changes from manager
 * project tree.
 *
 * @author Jakub D.
 */
public class ManagerResultCollector extends ResultCollectorAdapter {

    Properties properties;
    ResultStorage storage;
    ResultEvaluator evaluator;
    ResultWriter writer;
    ResultStatistics statistics;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void onConfigurationReady(VisualSuite visualSuite) {
        String storageClass = (String) properties.getProperty("result-storage");
        storage = new Instantiator<ResultStorage>().getInstance(storageClass);
        storage.setProperties(properties);

        String writerClass = (String) properties.getProperty("result-writer");
        writer = new Instantiator<ResultWriter>().getInstance(writerClass);
        writer.setProperties(properties);

        String statisticsClass = (String) properties.getProperty("result-statistics");
        statistics = new Instantiator<ResultStatistics>().getInstance(statisticsClass);
        statistics.setProperties(properties);

        evaluator = new ResultEvaluator();

    }

    @Override
    public void onPatternCompleted(Test test, Pattern pattern, ComparisonResult comparisonResult) {
        ResultConclusion conclusion = evaluator.evaluate(test.getPerception(), comparisonResult);

        if (conclusion == ResultConclusion.DIFFER || conclusion == ResultConclusion.PERCEPTUALLY_SAME) {
            String location = storage.store(test, pattern, comparisonResult.getDiffImage());
            pattern.setOutput(location);
        }

        if (comparisonResult.getDiffImage() != null) {
            comparisonResult.getDiffImage().flush();
        }

        pattern.setComparisonResult(comparisonResult);

        TestCase managerTest = Main.mainProject.findTest(test.getName(), pattern.getName());
        if (managerTest.getConclusion() != null)
            pattern.setConclusion(managerTest.getConclusion());
        else
            pattern.setConclusion(conclusion);

        statistics.onPatternCompleted(pattern);
    }

    @Override
    public void onTestCompleted(Test test) {
        writer.write(test);
        statistics.onTestCompleted(test);
    }

    @Override
    public void onSuiteCompleted(VisualSuite visualSuite) {
        writer.close();
        statistics.onSuiteCompleted();
        storage.end();
    }
}
