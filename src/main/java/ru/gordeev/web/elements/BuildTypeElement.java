package ru.gordeev.web.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import ru.gordeev.web.pages.BuildConfigurationPage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class BuildTypeElement extends BasePageElement {
    private SelenideElement buildStatusText;
    private SelenideElement buildName;
    private SelenideElement runButton;
    private SelenideElement successLink;

    public BuildTypeElement(SelenideElement element) {
        super(element);

        this.buildStatusText = $(byCssSelector("div[class*='Build__status']"));
        this.runButton = $(byCssSelector("[data-test='run-build']"));
        this.buildName = $(byCssSelector("span[class*='BuildTypeLine__caption']"));
        this.successLink = $(byCssSelector("a[class*='BuildStatusLink__success']"));
    }

    public String getBuildName() {
        return buildName.text();
    }

    public BuildTypeElement clickRunButton() {
        runButton.shouldBe(visible).click();
        return this;
    }

    public boolean isBuildSuccessful() {
        buildStatusText.shouldBe(visible).shouldHave(text("Success"));

        buildStatusText.$("[data-test-icon='finished_green']").should(exist);

        return true;
    }

    public BuildConfigurationPage clickSuccessLink() {
        successLink.shouldBe(visible).click();
        return new BuildConfigurationPage();
    }
}
