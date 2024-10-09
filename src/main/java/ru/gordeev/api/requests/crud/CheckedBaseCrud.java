package ru.gordeev.api.requests.crud;

import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import ru.gordeev.api.enums.Endpoint;
import ru.gordeev.api.models.BaseModel;
import ru.gordeev.api.requests.EndpointActions;
import ru.gordeev.api.requests.Request;
import ru.gordeev.api.utils.TestDataStorage;

@SuppressWarnings("unchecked")
public class CheckedBaseCrud<T extends BaseModel> extends Request implements CrudInterface, EndpointActions {
    private final UncheckedBaseCrud uncheckedBaseCrud;

    public CheckedBaseCrud(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);

        this.uncheckedBaseCrud = new UncheckedBaseCrud(spec, endpoint);
    }

    @Override
    public T create(BaseModel model) {
        var createdModel =  (T) uncheckedBaseCrud.create(model)
            .then()
            .assertThat().statusCode(HttpStatus.SC_OK)
            .extract().as(endpoint.getModelClass());

        TestDataStorage.getStorage().addCreatedEntity(endpoint, createdModel);
        return createdModel;
    }

    @Override
    public T read(String id) {
        return (T) uncheckedBaseCrud
            .read(id)
            .then().assertThat().statusCode(HttpStatus.SC_OK)
            .extract().as(endpoint.getModelClass());
    }

    @Override
    public T update(String id, BaseModel model) {
        return (T) uncheckedBaseCrud
            .update(id, model)
            .then().assertThat().statusCode(HttpStatus.SC_OK)
            .extract().as(endpoint.getModelClass());
    }

    @Override
    public Object delete(String id) {
        return uncheckedBaseCrud
            .delete(id)
            .then().assertThat().statusCode(HttpStatus.SC_OK)
            .extract().asString();
    }
}
