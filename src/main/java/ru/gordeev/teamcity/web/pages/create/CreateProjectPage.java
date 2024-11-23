package ru.gordeev.teamcity.web.pages.create;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class CreateProjectPage extends CreateBasePage {
    private static final String PROJECT_SHOW_MODE = "createProjectMenu";

    private SelenideElement projectNameInput = $("#projectName");
    private SelenideElement projectNameError = $("#error_projectName");

    @Step("Open Create Project Page with project ID: {projectId}")
    public static CreateProjectPage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, PROJECT_SHOW_MODE), CreateProjectPage.class);
    }

    @Step("Navigate to the create form with URL: {url}")
    public CreateProjectPage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    @Step("Set up project with name: {projectName} and build type: {buildTypeName}")
    public void setupProject(String projectName, String buildTypeName) {
        projectNameInput.val(projectName);
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
    }

    @Step("Set up project without a name, using build type: {buildTypeName}")
    public CreateProjectPage setupProjectWithoutName(String buildTypeName) {
        projectNameInput.clear();
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
        return this;
    }

    @Step("Verify that project name input has the error condition: {condition}")
    public void projectNameInputHasError(WebElementCondition condition) {
        projectNameError.shouldHave(condition);
    }
}
