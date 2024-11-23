package ru.gordeev.teamcity.web.pages.create;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;

import static com.codeborne.selenide.Selenide.$;

public class CreateProjectPage extends CreateBasePage {
    private static final String PROJECT_SHOW_MODE = "createProjectMenu";

    private SelenideElement projectNameInput = $("#projectName");
    private SelenideElement projectNameError = $("#error_projectName");

    public static CreateProjectPage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, PROJECT_SHOW_MODE), CreateProjectPage.class);
    }

    public CreateProjectPage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    public void setupProject(String projectName, String buildTypeName) {
        projectNameInput.val(projectName);
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
    }

    public CreateProjectPage setupProjectWithoutName(String buildTypeName) {
        projectNameInput.clear();
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
        return this;
    }

    public void projectNameInputHasError(WebElementCondition condition) {
        projectNameError.shouldHave(condition);
    }
}
