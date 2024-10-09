import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import ru.gordeev.api.enums.BuildState;
import ru.gordeev.api.enums.BuildStatus;
import ru.gordeev.api.enums.Endpoint;
import ru.gordeev.api.models.Build;
import ru.gordeev.api.models.Project;
import ru.gordeev.api.models.Properties;
import ru.gordeev.api.models.Step;
import ru.gordeev.api.models.Steps;
import ru.gordeev.api.requests.CheckedRequests;
import ru.gordeev.api.requests.UncheckedRequests;
import ru.gordeev.api.requests.non_crud.BuildInterface;
import ru.gordeev.api.requests.non_crud.unchecked.UncheckedBuildRequests;
import ru.gordeev.api.spec.Specifications;

import java.util.Arrays;
import java.util.List;

import static ru.gordeev.api.enums.Endpoint.BUILD_TYPES;
import static ru.gordeev.api.enums.Endpoint.PROJECTS;
import static ru.gordeev.api.enums.Endpoint.USERS;
import static ru.gordeev.api.generators.TestDataGenerator.generate;
import static ru.gordeev.api.utils.PollingUtils.waitForCondition;

public class BuildTest extends BaseApiTest {

    @Test(description = "User should be able to start build", groups = {"Positive"})
    public void triggerBuildTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.userAuth(testData.getUser()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        BuildInterface<Build> request = userCheckRequests.getRequest(Endpoint.BUILD_QUEUE, BuildInterface.class);

        Build build = generate(Arrays.asList(testData.getProject(), testData.getBuildType()), Build.class, testData.getBuildType());

        Step buildStep = new Step();
        buildStep.setName("Print Hello World");
        Properties stepProperties = new Properties();
        stepProperties.addProperty("use.custom.script", "false");
        stepProperties.addProperty("command.executable", "echo");
        stepProperties.addProperty("command.parameters", "Hello World");
        buildStep.setProperties(stepProperties);

        testData.getBuildType().setSteps(Steps.builder().count(1).step(List.of(buildStep)).build());
        build.setBuildType(testData.getBuildType());

        var buildId = request.triggerBuild(build).getId();

        Build finishedBuild = waitForCondition(
            () -> request.getBuildById(buildId),
            runningBuild -> runningBuild.getState().equals(BuildState.FINISHED)
        );

        softy.assertEquals(finishedBuild.getState(), BuildState.FINISHED);
        softy.assertEquals(finishedBuild.getStatus(), BuildStatus.SUCCESS);
    }

    @Test(description = "User should not be able to start build without buildType", groups = {"Negative"})
    public void triggerBuildNegativeTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        UncheckedRequests uncheckedRequests = new UncheckedRequests(Specifications.userAuth(testData.getUser()));

        var request = uncheckedRequests.getRequest(Endpoint.BUILD_QUEUE, UncheckedBuildRequests.class);

        Build invalidBuild = new Build();
        invalidBuild.setBuildType(null);

        request.triggerBuild(invalidBuild)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body(Matchers.containsString("No 'buildType' element in the posted entry"));
    }
}
