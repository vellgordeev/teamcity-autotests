package ru.gordeev.teamcity.web;

import org.testng.annotations.Test;
import ru.gordeev.teamcity.api.models.Project;
import ru.gordeev.teamcity.api.models.Properties;
import ru.gordeev.teamcity.api.models.Step;
import ru.gordeev.teamcity.api.models.Steps;
import ru.gordeev.teamcity.api.requests.CheckedRequests;
import ru.gordeev.teamcity.api.spec.Specifications;
import ru.gordeev.teamcity.web.pages.ProjectPage;

import java.util.List;

import static com.codeborne.selenide.Condition.exactText;
import static io.qameta.allure.Allure.step;
import static ru.gordeev.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static ru.gordeev.teamcity.api.enums.Endpoint.PROJECTS;

@Test(groups = {"Regression"})
public class RunBuildTest extends BaseUiTest {

    @Test(description = "User should be able to run build", groups = {"Positive"})
    public void userRunBuildTest() {
        step("Log in as the test user", () -> {
            loginAs(testData.getUser());
        });

        step("Create a new project via API", () -> {
            var userCheckRequests = new CheckedRequests(Specifications.userAuth(testData.getUser()));
            userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        });

        step("Set up a build type with steps via API", () -> {
            testData.getBuildType().setSteps(Steps.builder()
                    .count(1)
                    .step(List.of(Step.builder()
                            .name("Print Hello World")
                            .properties(new Properties()
                                    .addProperty("use.custom.script", "false")
                                    .addProperty("command.executable", "echo")
                                    .addProperty("command.parameters", "Hello World")).build()))
                    .build());
            var userCheckRequests = new CheckedRequests(Specifications.userAuth(testData.getUser()));
            userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());
        });

        step("Open the project page and find the latest build", () -> {
            var latestBuild = ProjectPage.open(testData.getProject().getId())
                    .titleShouldHave(exactText(testData.getProject().getName()))
                    .findLatestBuild()
                    .getLatestBuild();
            step("Run the latest build", latestBuild::clickRunButton);
        });

        step("Verify the build status is successful", () -> {
            var latestBuild = ProjectPage.open(testData.getProject().getId())
                    .findLatestBuild()
                    .getLatestBuild();
            softy.assertTrue(latestBuild.isBuildSuccessful(), "Build should be successful");
        });

        step("Verify build log contains step information", () -> {
            var buildConfigurationPage = ProjectPage.open(testData.getProject().getId())
                    .findLatestBuild()
                    .getLatestBuild()
                    .clickSuccessLink();
            buildConfigurationPage.clickBuildLogAndSearchFor("Step 1/1: Print Hello World (Command Line)");
        });
    }
}
