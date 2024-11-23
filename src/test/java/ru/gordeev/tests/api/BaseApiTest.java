package ru.gordeev.tests.api;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.gordeev.api.models.AuthModules;
import ru.gordeev.api.models.ServerAuthSettings;
import ru.gordeev.api.requests.non_crud.ServerAuthRequest;
import ru.gordeev.api.spec.Specifications;
import ru.gordeev.tests.BaseTest;

import static ru.gordeev.api.generators.TestDataGenerator.generate;

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
