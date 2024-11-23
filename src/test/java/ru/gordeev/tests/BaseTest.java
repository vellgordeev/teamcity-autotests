package ru.gordeev.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;
import ru.gordeev.api.models.TestData;
import ru.gordeev.api.requests.CheckedRequests;
import ru.gordeev.api.requests.UncheckedRequests;
import ru.gordeev.api.spec.Specifications;
import ru.gordeev.api.utils.TestDataFactory;
import ru.gordeev.api.utils.TestDataStorage;

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
