package ru.gordeev.web.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.$;

public class NavigationPanel extends BasePage {

    @Getter
    private static SelenideElement searchInput = $(byAttribute("data-test", "sidebar-search"));

    public static NavigationPanel enterSearchText(String projectName) {
        searchInput.shouldBe(visible).setValue(projectName);
        return new NavigationPanel();
    }

    public static SelenideElement findProjectInSearchResults(String projectName) {
        return $(byCssSelector(String.format("[class*='ProjectsTreeItem__name'][aria-label='%s']", projectName)))
                .shouldBe(visible);
    }
}
