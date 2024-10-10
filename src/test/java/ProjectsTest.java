import org.testng.annotations.Test;
import ru.gordeev.api.models.Project;
import ru.gordeev.api.requests.CheckedRequests;
import ru.gordeev.api.requests.non_crud.checked.CheckedSearchProjectsRequest;
import ru.gordeev.api.spec.Specifications;

import java.util.HashMap;
import java.util.Map;

import static ru.gordeev.api.enums.Endpoint.PROJECTS;
import static ru.gordeev.api.enums.Endpoint.SEARCH_PROJECTS;
import static ru.gordeev.api.enums.Endpoint.USERS;

public class ProjectsTest extends BaseApiTest {

    @Test(description = "User should be able to find project by their name", groups = {"Positive"})
    public void userFindsProjectByTheirName() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.userAuth(testData.getUser()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        Map<String, String> searchParams = new HashMap<>();
        searchParams.put("locator", "name:" + testData.getProject().getName());

        Project project = userCheckRequests.getRequest(SEARCH_PROJECTS, CheckedSearchProjectsRequest.class)
            .searchProject(searchParams)
            .getProject()
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No project found"));

        softy.assertEquals(project.getName(), testData.getProject().getName());
    }
}
