package ru.gordeev.api.requests.checked;

import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import ru.gordeev.api.enums.Endpoint;
import ru.gordeev.api.models.BaseModel;
import ru.gordeev.api.requests.CrudInterface;
import ru.gordeev.api.requests.Request;
import ru.gordeev.api.requests.unchecked.UncheckedBase;
import ru.gordeev.api.utils.TestDataStorage;

@SuppressWarnings("unchecked")
public class CheckedBase<T extends BaseModel> extends Request implements CrudInterface {
    private final UncheckedBase uncheckedBase;

    public CheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);

        this.uncheckedBase = new UncheckedBase(spec, endpoint);
    }

    @Override
    public T create(BaseModel model) {
        var createdModel =  (T) uncheckedBase.create(model)
            .then()
            .assertThat().statusCode(HttpStatus.SC_OK)
            .extract().as(endpoint.getModelClass());

        TestDataStorage.getStorage().addCreatedEntity(endpoint, createdModel);
        return createdModel;
    }

    @Override
    public T read(String id) {
        return (T) uncheckedBase
            .read(id)
            .then().assertThat().statusCode(HttpStatus.SC_OK)
            .extract().as(endpoint.getModelClass());
    }

    @Override
    public T update(String id, BaseModel model) {
        return (T) uncheckedBase
            .update(id, model)
            .then().assertThat().statusCode(HttpStatus.SC_OK)
            .extract().as(endpoint.getModelClass());
    }

    @Override
    public Object delete(String id) {
        return uncheckedBase
            .delete(id)
            .then().assertThat().statusCode(HttpStatus.SC_OK)
            .extract().asString();
    }
}
