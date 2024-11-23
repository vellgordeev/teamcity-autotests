package ru.gordeev.teamcity.web.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.$;

public class BuildConfigurationPage extends BasePage {
    private static final String BUILD_CONFIGURATION_URL = "/buildConfiguration/%s";

    private SelenideElement buildLog = $("span[data-test='tab'][data-tab-title='Build Log']");
    private SelenideElement buildLogMessages = $(byCssSelector("div[class*='FullBuildLog__messagesWrapper']"));


    @Step("Open Build Configuration Page with ID: {id}")
    public static BuildConfigurationPage open(String id) {
        return Selenide.open(BUILD_CONFIGURATION_URL.formatted(id), BuildConfigurationPage.class);
    }

    @Step("Click 'Build Log' tab and search for text: {text}")
    public SelenideElement clickBuildLogAndSearchFor(String text) {
        buildLog.click();
        return buildLogMessages.shouldHave(text(text));
    }
}
