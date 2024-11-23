package ru.gordeev.tests.web;

import org.testng.annotations.Test;
import ru.gordeev.api.models.Project;
import ru.gordeev.api.models.Properties;
import ru.gordeev.api.models.Step;
import ru.gordeev.api.models.Steps;
import ru.gordeev.api.requests.CheckedRequests;
import ru.gordeev.api.spec.Specifications;
import ru.gordeev.web.pages.ProjectPage;

import java.util.List;

import static com.codeborne.selenide.Condition.exactText;
import static ru.gordeev.api.enums.Endpoint.BUILD_TYPES;
import static ru.gordeev.api.enums.Endpoint.PROJECTS;

@Test(groups = {"Regression"})
public class RunBuildTest extends BaseUiTest {

    @Test(description = "User should be able to run build", groups = {"Positive"})
    public void userRunBuildTest() {
        // Set up environment
        loginAs(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.userAuth(testData.getUser()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        testData.getBuildType().setSteps(Steps.builder()
                .count(1)
                .step(List.of(Step.builder()
                        .name("Print Hello World")
                        .properties(new Properties()
                                .addProperty("use.custom.script", "false")
                                .addProperty("command.executable", "echo")
                                .addProperty("command.parameters", "Hello World")).build()))
                .build());

        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        // Interact with UI
        var latestBuild = ProjectPage.open(testData.getProject().getId())
                .titleShouldHave(exactText(testData.getProject().getName()))
                .findLatestBuild()
                .getLatestBuild();
        latestBuild.clickRunButton();

        // Verify UI build state
        softy.assertTrue(latestBuild.isBuildSuccessful());

        var buildConfigurationPage = latestBuild.clickSuccessLink();

        buildConfigurationPage.clickBuildLogAndSearchFor("Step 1/1: Print Hello World (Command Line)");
    }
}
