package org.jboss.lupic.parser.processor;

import org.jboss.lupic.parser.Processor;
import org.jboss.lupic.suite.GlobalConfiguration;

public class GlobalConfigurationProcessor extends Processor {

    {
        supportProcessor("image-retriever", RetrieverProcessor.class);
        supportProcessor("mask-retriever", RetrieverProcessor.class);
        supportProcessor("perception", PerceptionProcessor.class);
        supportProcessor("masks", MasksProcessor.class);
    }

    @Override
    public void start() {
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();

        getVisualSuite().setGlobalConfiguration(globalConfiguration);
        getContext().setCurrentConfiguration(globalConfiguration);
    }

    @Override
    public void end() {
        getContext().invokeListeners().configurationParsed(getVisualSuite());
    }
}
