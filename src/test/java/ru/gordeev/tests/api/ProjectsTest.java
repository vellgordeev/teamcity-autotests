package ru.gordeev.tests.api;

import org.testng.annotations.Test;
import ru.gordeev.api.models.Project;
import ru.gordeev.api.requests.CheckedRequests;
import ru.gordeev.api.requests.crud.CheckedBase;
import ru.gordeev.api.spec.Specifications;
import ru.gordeev.api.utils.TestDataStorage;

import java.util.HashMap;
import java.util.Map;

import static ru.gordeev.api.enums.Endpoint.PROJECTS;
import static ru.gordeev.api.enums.Endpoint.USERS;

public class ProjectsTest extends BaseApiTest {

    @Test(description = "User should be able to find project by their name", groups = {"Positive"})
    public void userFindsProjectByTheirName() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.userAuth(testData.getUser()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        Map<String, String> searchParams = new HashMap<>();
        searchParams.put("locator", "name:" + testData.getProject().getName());

        Project project = userCheckRequests.getRequest(PROJECTS, CheckedBase.class)
            .searchProject(searchParams)
            .getProject()
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No project found"));

        TestDataStorage.getStorage().addCreatedEntity(PROJECTS, project);

        softy.assertEquals(project.getName(), testData.getProject().getName());
    }
}
