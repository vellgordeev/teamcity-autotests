package ru.gordeev.tests.web;

import org.testng.annotations.Test;
import ru.gordeev.api.models.Project;
import ru.gordeev.api.requests.CheckedRequests;
import ru.gordeev.api.spec.Specifications;
import ru.gordeev.web.pages.NavigationPanel;
import ru.gordeev.web.pages.ProjectsPage;

import static ru.gordeev.api.enums.Endpoint.PROJECTS;

@Test(groups = {"Regression"})
public class SearchProjectTest extends BaseUiTest {

    @Test(description = "User should be able to search for a project", groups = {"Positive"})
    public void userSearchProjectTest() {
        // Set up environment
        loginAs(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.userAuth(testData.getUser()));

        // Create project via API
        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        // Interact with UI
        ProjectsPage.open();
        NavigationPanel.enterSearchText(testData.getProject().getName());

        // Verify project appears in search results
        softy.assertTrue(NavigationPanel
                .findProjectInSearchResults(testData.getProject().getName())
                .isDisplayed(), "Project should appear in the search results");
    }
}
