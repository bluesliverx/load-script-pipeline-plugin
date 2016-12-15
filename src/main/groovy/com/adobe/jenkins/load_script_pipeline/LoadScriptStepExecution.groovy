package com.adobe.jenkins.load_script_pipeline;

import com.google.inject.Inject;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.cps.CpsFlowExecution;
import org.jenkinsci.plugins.workflow.cps.CpsStepContext;
import org.jenkinsci.plugins.workflow.cps.CpsThread;
import org.jenkinsci.plugins.workflow.cps.replay.ReplayAction;
import org.jenkinsci.plugins.workflow.steps.AbstractStepExecutionImpl;
import org.jenkinsci.plugins.workflow.steps.BodyExecutionCallback;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

/**
 * Loads another Groovy script file from a string and executes it.
 * Borrows heavily from LoadStepExecution in workflow-cps.
 *
 * @author Brian Saville
 */
public class LoadScriptStepExecution extends AbstractStepExecutionImpl {
    @Inject(optional=true)
    private transient LoadScriptStep step;

    @StepContextParameter
    private transient TaskListener listener;

    @Override
    public boolean start() throws Exception {
        CpsStepContext cps = (CpsStepContext) getContext();
        CpsThread t = CpsThread.current();

        CpsFlowExecution execution = t.getExecution();

        String text = step.getScript()
        String clazz = execution.getNextScriptName("");
        String newText = ReplayAction.replace(execution, clazz);
        if (newText != null) {
            listener.getLogger().println("Replacing Groovy text with edited version");
            text = newText;
        }

        Script script = execution.getShell().parse(text);

        // execute body as another thread that shares the same head as this thread
        // as the body can pause.
        cps.newBodyInvoker(t.getGroup().export(script))
                .withDisplayName(step.getScript())
                .withCallback(BodyExecutionCallback.wrap(cps))
                .start(); // when the body is done, the load step is done

        return false;
    }

    @Override
    public void stop(Throwable cause) throws Exception {
        // noop
        //
        // the head of the CPS thread that's executing the body should stop and that's all we need to do.
    }

    private static final long serialVersionUID = 1L;

}