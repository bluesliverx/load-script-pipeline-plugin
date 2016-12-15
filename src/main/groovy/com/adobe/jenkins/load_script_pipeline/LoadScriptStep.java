package com.adobe.jenkins.load_script_pipeline;

import hudson.Extension;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Evaluate pipeline script file from a string.
 *
 * @author Brian Saville
 */
public class LoadScriptStep extends AbstractStepImpl {
    /**
     * The script as a non-empty string.
     */
    private final String script;

    @DataBoundConstructor
    public LoadScriptStep(String script) {
        this.script = script;
    }
    
    public String getScript() {
        return script;
    }

    @Extension
    public static class DescriptorImpl extends AbstractStepDescriptorImpl {
        public DescriptorImpl() {
            super(LoadScriptStepExecution.class);
        }

        @Override
        public String getFunctionName() {
            return "loadScript";
        }

        @Override
        public String getDisplayName() {
            return "Evaluate a Groovy script as a string into the Pipeline script";
        }
    }

}