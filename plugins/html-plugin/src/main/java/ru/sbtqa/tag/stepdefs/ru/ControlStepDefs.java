package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.Transform;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import ru.sbtqa.tag.pagefactory.html.junit.ControlSteps;
import ru.sbtqa.tag.pagefactory.transformer.ConditionTransformer;
import ru.sbtqa.tag.pagefactory.transformer.enums.Condition;

public class ControlStepDefs {

    private final ControlSteps controlSteps = ControlSteps.getInstance();

    @Когда("^(?:пользователь |он )?отмечает признак \"([^\"]*)\"$")
    public void select(String flagName) {
        controlSteps.select(flagName);
    }

    @Когда("^признак \"([^\"]*)\" (не )?отмечен$")
    public void isSelected(String flagName, @Transform(ConditionTransformer.class) Condition negation) {
        controlSteps.isSelected(flagName, negation);
    }

    @Когда("^(?:пользователь |он )?отмечает признак \"([^\"]*)\" по значению \"([^\"]*)\"$")
    public void selectByValue(String flagName, String value) {
        controlSteps.selectByValue(flagName, value);
    }

    @И("^в радио группе \"([^\"]*)\" (не )?отмечено значение \"([^\"]*)\"$")
    public void isRadiobuttonSelected(String groupName, @Transform(ConditionTransformer.class) Condition negation, String value) {
        controlSteps.isRadiobuttonSelected(groupName, negation, value);
    }

    @И("^в радио группе \"([^\"]*)\" не выбрано ни одно значение$")
    public void checkRadioGroupValueNotSelected(String radioGroupName) {
        controlSteps.checkRadioGroupValueNotSelected(radioGroupName);
    }
}
