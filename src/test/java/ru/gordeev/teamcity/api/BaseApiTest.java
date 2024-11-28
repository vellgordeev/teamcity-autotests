package ru.gordeev.teamcity.api;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.gordeev.teamcity.BaseTest;
import ru.gordeev.teamcity.api.models.AuthModules;
import ru.gordeev.teamcity.api.models.ServerAuthSettings;
import ru.gordeev.teamcity.api.requests.non_crud.ServerAuthRequest;
import ru.gordeev.teamcity.api.spec.Specifications;

import static ru.gordeev.teamcity.api.generators.TestDataGenerator.generate;

public class BaseApiTest extends BaseTest {
    private final ServerAuthRequest serverAuthRequest = new ServerAuthRequest(Specifications.superUserAuth());
    private AuthModules authModules;
    private boolean perProjectPermissions;

    @BeforeSuite(alwaysRun = true)
    public void setUpServerAuthSettings() {
        perProjectPermissions = serverAuthRequest.read().getPerProjectPermissions();

        authModules = generate(AuthModules.class);

        serverAuthRequest.update(ServerAuthSettings.builder()
                .perProjectPermissions(true)
                .modules(authModules)
                .build());
    }

    @AfterSuite(alwaysRun = true)
    public void cleanUpServerAuthSettings() {
        serverAuthRequest.update(ServerAuthSettings.builder()
                .perProjectPermissions(perProjectPermissions)
                .modules(authModules)
                .build());
    }
}
