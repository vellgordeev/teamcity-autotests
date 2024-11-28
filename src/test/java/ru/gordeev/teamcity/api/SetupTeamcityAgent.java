package ru.gordeev.teamcity.api;

import org.testng.annotations.Test;
import ru.gordeev.teamcity.api.enums.Endpoint;
import ru.gordeev.teamcity.api.requests.non_crud.checked.CheckedAgentAuthorizeRequest;

public class SetupTeamcityAgent extends BaseApiTest {

    @Test(groups = {"Setup"})
    public void setupTeamCityAgentTest() {
        superUserCheckRequests.getRequest(Endpoint.AGENT_AUTHORIZE, CheckedAgentAuthorizeRequest.class)
                .authorizeAgent("teamcityAgentFirst", true);
    }
}
