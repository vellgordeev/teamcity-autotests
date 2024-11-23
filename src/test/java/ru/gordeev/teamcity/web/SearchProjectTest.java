package ru.gordeev.teamcity.web;

import org.testng.annotations.Test;
import ru.gordeev.teamcity.api.models.Project;
import ru.gordeev.teamcity.api.requests.CheckedRequests;
import ru.gordeev.teamcity.api.spec.Specifications;
import ru.gordeev.teamcity.web.pages.NavigationPanel;
import ru.gordeev.teamcity.web.pages.ProjectsPage;

import static io.qameta.allure.Allure.step;
import static ru.gordeev.teamcity.api.enums.Endpoint.PROJECTS;

@Test(groups = {"Regression"})
public class SearchProjectTest extends BaseUiTest {

    @Test(description = "User should be able to search for a project", groups = {"Positive"})
    public void userSearchProjectTest() {
        step("Log in as the test user", () -> {
            loginAs(testData.getUser());
        });

        step("Create a project via API", () -> {
            var userCheckRequests = new CheckedRequests(Specifications.userAuth(testData.getUser()));
            userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        });

        step("Open the Projects Page", () -> {
            ProjectsPage.open();
        });

        step("Search for the project in the navigation panel", () -> {
            NavigationPanel.enterSearchText(testData.getProject().getName());
        });

        step("Verify the project appears in search results", () -> {
            softy.assertTrue(
                    NavigationPanel.findProjectInSearchResults(testData.getProject().getName()).isDisplayed(),
                    "Project should appear in the search results"
            );
        });
    }
}
