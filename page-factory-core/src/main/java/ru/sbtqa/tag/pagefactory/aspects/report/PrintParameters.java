package ru.sbtqa.tag.pagefactory.aspects.report;

import cucumber.api.TestStep;
import cucumber.api.event.Event;
import cucumber.api.event.TestStepFinished;
import cucumber.runner.PickleTestStep;
import gherkin.pickles.Argument;
import gherkin.pickles.PickleCell;
import gherkin.pickles.PickleRow;
import gherkin.pickles.PickleString;
import gherkin.pickles.PickleTable;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PrintParameters {

    private ThreadLocal<List<Argument>> testArguments = ThreadLocal.withInitial(ArrayList::new);

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepFinished(Event event) {
        return event instanceof TestStepFinished;
    }

    @Before("sendStepFinished(event)")
    public void beforeSendStepFinished(JoinPoint joinPoint, Event event) {
        TestStep testStep = ((TestStepFinished) event).testStep;

        if (testStep.getClass().equals(PickleTestStep.class)) {
            this.testArguments.set(testStep.getStepArgument());
            addAllureArguments();
        }
    }

    @After("execution(* cucumber.runtime.formatter.PrettyFormatter.printStep(..))")
    public void printStep(JoinPoint joinPoint) {
        for (Argument argument : this.testArguments.get()) {
            if (argument instanceof PickleString) {
                printPickleString((PickleString) argument);
            } else {
                printPickleTable((PickleTable) argument);
            }
        }
    }

    private void addAllureArguments() {
        Argument pickleString = this.testArguments.get().stream()
                .filter(argument -> argument instanceof PickleString).findFirst().orElse(null);

        if (pickleString != null) {
            Allure.getLifecycle().updateStep(stepResult ->
                    stepResult.withAttachments());
            this.textAttachment(((PickleString) pickleString).getContent());
        }
    }

    private void printPickleString(PickleString pickleString) {
        if (pickleString != null) {
            System.out.println("\"\"\"");
            System.out.println(pickleString.getContent());
            System.out.println("\"\"\"");
        }
    }

    private void printPickleTable(PickleTable pickleTable) {
        for (PickleRow row : pickleTable.getRows()) {
            System.out.print("      | ");
            for (PickleCell cell : row.getCells()) {
                System.out.print(cell.getValue() + " | ");
            }
            System.out.println();
        }
    }

    @Attachment(value = "{text}", type = "text/plain")
    private String textAttachment(String text) {
        return text;
    }
}
