package ru.gordeev.api.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gordeev.api.generators.TestDataGenerator;
import ru.gordeev.api.models.BaseModel;
import ru.gordeev.api.models.TestData;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public final class TestDataFactory {
    private static final Logger logger = LogManager.getLogger(TestDataFactory.class);

    private TestDataFactory() {}

    public static TestData createTestData() {
        return createForClass(TestData.class);
    }

    private static <T> T createForClass(Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            var generatedModels = new ArrayList<BaseModel>();

            for (var field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (BaseModel.class.isAssignableFrom(field.getType())) {
                    var generatedModel = TestDataGenerator.generate(generatedModels, field.getType().asSubclass(BaseModel.class));
                    field.set(instance, generatedModel);
                    generatedModels.add(generatedModel);
                }
                field.setAccessible(false);
            }

            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("Failed to generate test data for class: {}", clazz.getName(), e);
            throw new IllegalStateException("Cannot generate test data for " + clazz.getName(), e);
        }
    }
}
