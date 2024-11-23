package ru.gordeev.teamcity.web.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.gordeev.teamcity.api.models.User;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class LoginPage extends BasePage {
    private static final String LOGIN_URL = "/login.html";

    private SelenideElement inputUsername = $("#username");
    private SelenideElement inputPassword = $("#password");
    private SelenideElement inputSubmitLogin = $(".loginButton");

    @Step("Open Login Page")
    public static LoginPage open () {
        return Selenide.open(LOGIN_URL, LoginPage.class);
    }

    @Step("Log in with username: {user.username}")
    public ProjectsPage login(User user) {
        inputUsername.val(user.getUsername());
        inputPassword.val(user.getPassword());
        inputSubmitLogin.click();

        return page(ProjectsPage.class);
    }
}
