package ru.gordeev.teamcity.web.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import lombok.Getter;
import ru.gordeev.teamcity.web.elements.BuildTypeElement;

import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.$;

public class ProjectPage extends BasePage {
    private static final String PROJECT_URL = "/project/%s";

    private final SelenideElement title = $("span[class*='ProjectPageHeader']");
    @Getter
    private BuildTypeElement latestBuild;

    public static ProjectPage open(String projectId) {
        return Selenide.open(PROJECT_URL.formatted(projectId), ProjectPage.class);
    }

    public ProjectPage titleShouldHave(WebElementCondition condition) {
        title.shouldHave(condition);
        return this;
    }

    public ProjectPage findLatestBuild() {
        SelenideElement latestBuildElement = $(byCssSelector(".BuildsByBuildType__container--YZ .BuildTypes__item--UX"));
        this.latestBuild = new BuildTypeElement(latestBuildElement);
        return this;
    }
}
