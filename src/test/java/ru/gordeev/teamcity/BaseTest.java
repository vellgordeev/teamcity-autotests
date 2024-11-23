package ru.gordeev.teamcity;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;
import ru.gordeev.teamcity.api.models.TestData;
import ru.gordeev.teamcity.api.requests.CheckedRequests;
import ru.gordeev.teamcity.api.requests.UncheckedRequests;
import ru.gordeev.teamcity.api.spec.Specifications;
import ru.gordeev.teamcity.api.utils.TestDataFactory;
import ru.gordeev.teamcity.api.utils.TestDataStorage;

public class BaseTest {
    protected SoftAssert softy;
    protected TestData testData;
    protected CheckedRequests superUserCheckRequests = new CheckedRequests(Specifications.superUserAuth());
    protected UncheckedRequests superUserUncheckRequests = new UncheckedRequests(Specifications.superUserAuth());

    @BeforeSuite
    public void beforeSuite() {

    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        softy = new SoftAssert();
        testData = TestDataFactory.createTestData();
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        softy.assertAll();
        TestDataStorage.getStorage().deleteCreatedEntities();
    }
}
