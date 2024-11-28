package ru.gordeev.teamcity.web;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Test;
import ru.gordeev.teamcity.api.enums.Endpoint;
import ru.gordeev.teamcity.api.models.Project;
import ru.gordeev.teamcity.web.pages.ProjectPage;
import ru.gordeev.teamcity.web.pages.ProjectsPage;
import ru.gordeev.teamcity.web.pages.create.CreateProjectPage;

import static com.codeborne.selenide.Condition.exactText;
import static io.qameta.allure.Allure.step;
import static org.testng.Assert.assertNotNull;

@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUiTest {
    private static final String REPO_URL = "https://github.com/vellgordeev/vellgordeev";

    @Test(description = "User should be able to create project", groups = {"Positive"})
    public void userCreatesProject() {
        step("Log in as the test user", () -> {
            loginAs(testData.getUser());
        });

        step("Create a project using the UI", () -> {
            CreateProjectPage.open("_Root")
                    .createForm(REPO_URL)
                    .setupProject(testData.getProject().getName(), testData.getBuildType().getName());
        });

        step("Verify the project was created via API", () -> {
            var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS)
                    .read("name:" + testData.getProject().getName());
            assertNotNull(createdProject.getId());

            testData.setProject(createdProject);
        });

        step("Verify the project appears correctly on the UI", () -> {
            ProjectPage.open(testData.getProject().getId())
                    .titleShouldHave(exactText(testData.getProject().getName()));

            var foundProjects = ProjectsPage.open()
                    .getProjects().stream()
                    .anyMatch(project -> project.getName().text().equals(testData.getProject().getName()));

            softy.assertTrue(foundProjects);
        });
    }

    @Test(description = "User should not be able to create project without name", groups = {"Negative"})
    public void userCreatesProjectWithoutName() {
        step("Log in as the test user", () -> {
            loginAs(testData.getUser());
        });

        step("Attempt to create a project without a name", () -> {
            CreateProjectPage.open("_Root")
                    .createForm(REPO_URL)
                    .setupProjectWithoutName(testData.getBuildType().getName())
                    .projectNameInputHasError(Condition.exactText("Project name must not be empty"));
        });

        step("Verify the project was not created via API", () -> {
            var createdProject = superUserUncheckRequests.getRequest(Endpoint.PROJECTS)
                    .read("name:" + testData.getProject().getName());
            softy.assertEquals(createdProject.statusCode(), 404);
        });
    }
}
