package ru.gordeev.tests.api;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import ru.gordeev.teamcity.api.enums.BuildState;
import ru.gordeev.teamcity.api.enums.BuildStatus;
import ru.gordeev.teamcity.api.enums.Endpoint;
import ru.gordeev.teamcity.api.generators.TestDataGenerator;
import ru.gordeev.teamcity.api.models.*;
import ru.gordeev.teamcity.api.requests.CheckedRequests;
import ru.gordeev.teamcity.api.requests.UncheckedRequests;
import ru.gordeev.teamcity.api.requests.non_crud.BuildQueueInterface;
import ru.gordeev.teamcity.api.requests.non_crud.checked.CheckedBuildQueueRequest;
import ru.gordeev.teamcity.api.requests.non_crud.unchecked.UncheckedBuildQueueRequest;
import ru.gordeev.teamcity.api.spec.Specifications;

import java.util.Arrays;
import java.util.List;

import static ru.gordeev.teamcity.api.enums.Endpoint.*;
import static ru.gordeev.teamcity.api.utils.PollingUtils.waitForCondition;

@Test(groups = {"Regression"})
public class BuildQueueTest extends BaseApiTest {

    @Test(description = "User should be able to start build", groups = {"Positive"})
    public void triggerBuildTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.userAuth(testData.getUser()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        Build buildQueueBody = TestDataGenerator.generate(Arrays.asList(testData.getProject(), testData.getBuildType()), Build.class, testData.getBuildType());

        testData.getBuildType().setSteps(Steps.builder()
            .count(1)
            .step(List.of(Step.builder()
                .name("Print Hello World")
                .properties(new Properties()
                    .addProperty("use.custom.script", "false")
                    .addProperty("command.executable", "echo")
                    .addProperty("command.parameters", "Hello World")).build()))
            .build());

        buildQueueBody.setBuildType(testData.getBuildType());

        BuildQueueInterface<Build> request = userCheckRequests.getRequest(Endpoint.BUILD_QUEUE, CheckedBuildQueueRequest.class);
        var buildId = request.triggerBuild(buildQueueBody).getId();

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

        Build invalidBuild = new Build();
        invalidBuild.setBuildType(null);

        uncheckedRequests.getRequest(Endpoint.BUILD_QUEUE, UncheckedBuildQueueRequest.class)
            .triggerBuild(invalidBuild)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body(Matchers.containsString("No 'buildType' element in the posted entry"));
    }
}
