import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import ru.gordeev.api.models.User;
import ru.gordeev.api.spec.Specifications;

public class BuildConfigurationTest extends BaseApiTest {

    @Test
    public void buildConfigurationTest() {
        var user = User.builder()
            .username("admin")
            .password("admin")
            .build();

        var token = RestAssured
            .given()
            .spec(Specifications.getSpec().authSpec(user))
            .get("/authenticationTest.html?crsf")
            .then()
            .assertThat().statusCode(HttpStatus.SC_OK)
            .extract().asString();

        System.out.println(token);
    }
}
