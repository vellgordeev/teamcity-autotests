package ru.gordeev.teamcity.web;

import org.testng.annotations.Test;
import ru.gordeev.teamcity.web.pages.FirstStartPage;

public class SetupServerTest extends BaseUiTest {

    @Test(groups = {"Setup"})
    public void setupTeamCityServerTest() {
        FirstStartPage.open().setupFirstStart();
    }
}
