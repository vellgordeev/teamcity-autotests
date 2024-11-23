package ru.gordeev.teamcity.web;

import com.codeborne.selenide.Condition;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.gordeev.teamcity.api.enums.Endpoint;
import ru.gordeev.teamcity.api.models.Project;
import ru.gordeev.teamcity.web.pages.ProjectPage;
import ru.gordeev.teamcity.web.pages.ProjectsPage;
import ru.gordeev.teamcity.web.pages.create.CreateProjectPage;

import static com.codeborne.selenide.Condition.exactText;

@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUiTest {
    private static final String REPO_URL = "https://github.com/vellgordeev/vellgordeev";

    @Test(description = "User should be able to create project", groups = {"Positive"})
    public void userCreatesProject() {
        // Set up environment
        loginAs(testData.getUser());

        // Interact with UI
        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        // Verify API state (correct data sent from UI to API)
        var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());
        Assert.assertNotNull(createdProject);

        // Verify UI state (correct data retrieved and displayed on UI)
        ProjectPage.open(createdProject.getId())
                .titleShouldHave(exactText(testData.getProject().getName()));

        var foundProjects = ProjectsPage.open()
                .getProjects().stream()
                .anyMatch(project -> project.getName().text().equals(testData.getProject().getName()));

        softy.assertTrue(foundProjects);
    }

    @Test(description = "User should not be able to create project without name", groups = {"Negative"})
    public void userCreatesProjectWithoutName() {
        // Set up environment
        loginAs(testData.getUser());

        // Interact with UI
        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProjectWithoutName(testData.getBuildType().getName())
                .projectNameInputHasError(Condition.exactText("Project name must not be empty"));

        // Verify API state (incorrect data did not sent from UI to API)
        var createdProject = superUserUncheckRequests.getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());
        softy.assertEquals(createdProject.statusCode(), 404);
    }
}
