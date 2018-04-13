package ru.sbtqa.tag.pagefactory.stepdefs;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.context.ScenarioContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.tasks.AttachScreenshotToReportTask;
import ru.sbtqa.tag.pagefactory.tasks.ConnectToLogTask;
import ru.sbtqa.tag.pagefactory.tasks.KillProcessesTask;
import ru.sbtqa.tag.pagefactory.tasks.StartVideoTask;
import ru.sbtqa.tag.pagefactory.tasks.StopVideoTask;
import ru.sbtqa.tag.pagefactory.tasks.TaskHandler;

public class SetupStepDefs {

    @Before(order = 10001)
    public void setUp(Scenario scenario) {
//        Environment.setProperties(ConfigFactory.create(Configuration.class));
        Environment.getDriverService().mountDriver();
        ScenarioContext.setScenario(scenario);

        TaskHandler.addTask(new ConnectToLogTask());
        TaskHandler.addTask(new KillProcessesTask());
        TaskHandler.addTask(new StartVideoTask());
        TaskHandler.handleTasks();

        PageManager.getInstance().cachePages();
        PageContext.resetContext();
    }

    @After
    public void tearDown() {
        TaskHandler.addTask(new AttachScreenshotToReportTask());
        TaskHandler.addTask(new StopVideoTask());
        TaskHandler.handleTasks();

        Environment.getDriverService().demountDriver();
    }

}
