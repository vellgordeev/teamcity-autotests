package ru.gordeev.teamcity.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.gordeev.teamcity.web.elements.ProjectElement;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProjectsPage extends BasePage {
    private static final String PROJECTS_URL = "/favorite/projects";

    private ElementsCollection projectElements = $$("div[class*='Subproject__container']");
    private SelenideElement spanFavoriteProjects = $("span[class='ProjectPageHeader__title--ih']");
    private SelenideElement header = $(".MainPanel__router--gF > div");

    public ProjectsPage() {
        header.shouldBe(Condition.visible, BASE_WAITING);
    }

    @Step("Open the Projects Page")
    public static ProjectsPage open() {
        return Selenide.open(PROJECTS_URL, ProjectsPage.class);
    }

    @Step("Get the list of projects on the Projects Page")
    public List<ProjectElement> getProjects() {
        projectElements.shouldBe(sizeGreaterThan(0));
        return generatePageElements(projectElements, ProjectElement::new);
    }
}
