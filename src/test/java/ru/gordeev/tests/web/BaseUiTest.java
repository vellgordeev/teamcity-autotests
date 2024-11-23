package ru.gordeev.tests.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import ru.gordeev.teamcity.api.config.Config;
import ru.gordeev.teamcity.api.enums.Endpoint;
import ru.gordeev.teamcity.api.models.User;
import ru.gordeev.teamcity.web.pages.LoginPage;
import ru.gordeev.tests.BaseTest;

import java.util.Map;

import static java.lang.Long.parseLong;

public class BaseUiTest extends BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void setupUiTest() {
        Configuration.timeout = parseLong(Config.getProperty("timeout"));
        Configuration.browser = Config.getProperty("browser");
        Configuration.baseUrl = "http://" + Config.getProperty("host");
        Configuration.remote = Config.getProperty("remote");
        Configuration.browserSize = Config.getProperty("browserSize");

        Configuration.browserCapabilities.setCapability(
                "selenoid:options", Map.of("enableVNC", true, "enableLog", true)
        );
    }

    @AfterMethod(alwaysRun = true)
    public void closeWebDriver() {
        Selenide.closeWebDriver();
    }

    protected void loginAs(User user) {
        superUserCheckRequests.getRequest(Endpoint.USERS).create(user);
        LoginPage.open().login(user);
    }
}
