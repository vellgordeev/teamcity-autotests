package ru.gordeev.teamcity.api.generators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gordeev.teamcity.api.annotations.Random;
import ru.gordeev.teamcity.api.models.BaseModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TestDataGenerator {
    private static final Logger logger = LogManager.getLogger(TestDataGenerator.class);

    private TestDataGenerator() {
    }

    /**
     * Generates an instance of the specified class and populates its fields based on provided parameters or default rules.
     *
     * Fields are populated in the following order:
     * 1. If parameters are provided, they are assigned to fields in the order they appear.
     * 2. If no parameters are provided, fields may be populated with random values (if annotated) or recursively generated.
     *
     * @param generatorClass the class of the object to be generated
     * @param parameters optional parameters to set for the fields; if provided, they will be assigned to fields in order
     * @param <T> the type of the object to be generated
     * @return an instance of the specified class with populated fields
     * @throws IllegalStateException if the instance cannot be created or if an error occurs during instantiation
     */
    public static <T extends BaseModel> T generate(Class<T> generatorClass, Object... parameters) {
        return generate(Collections.emptyList(), generatorClass, parameters);
    }

    /**
     * Generates an instance of the specified class and populates its fields based on provided parameters, existing generated models, or default rules.
     *
     * Fields are populated in the following order:
     * 1. If parameters are provided, they are assigned to fields in order.
     * 2. Fields annotated with {@code @Random} are populated with random values if no parameter is provided.
     * 3. Fields of type {@code BaseModel} are recursively generated using the provided list of previously generated models, if applicable.
     * 4. Fields of type {@code List<BaseModel>} are populated with generated models or generated recursively.
     *
     * If fields have multiple applicable rules (e.g., parameters and annotations), parameters will take priority.
     *
     * @param generatedModels a list of previously generated models to reuse, if applicable
     * @param generatorClass the class of the object to be generated
     * @param parameters optional parameters to set for the fields; if provided, they will be assigned to fields in order
     * @param <T> the type of the object to be generated
     * @return an instance of the specified class with populated fields
     * @throws IllegalStateException if the instance cannot be created or if any errors occur during field assignment
     */
    public static <T extends BaseModel> T generate(List<BaseModel> generatedModels, Class<T> generatorClass, Object... parameters) {
        try {
            T instance = generatorClass.getDeclaredConstructor().newInstance();
            int paramIndex = 0;

            for (Field field : generatorClass.getDeclaredFields()) {
                field.setAccessible(true);

                if (paramIndex < parameters.length) {
                    assignParameterToField(instance, field, parameters[paramIndex]);
                    paramIndex++;
                } else if (field.isAnnotationPresent(Random.class)) {
                    assignRandomValueToField(instance, field);
                } else if (BaseModel.class.isAssignableFrom(field.getType())) {
                    assignGeneratedBaseModel(instance, field, generatedModels);
                } else if (List.class.isAssignableFrom(field.getType())) {
                    assignGeneratedList(instance, field, generatedModels);
                }

                field.setAccessible(false);
            }

            return instance;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("Failed to generate test data for class: {}", generatorClass.getName(), e);
            throw new IllegalStateException(e);
        }
    }

    private static void assignParameterToField(Object instance, Field field, Object parameter) throws IllegalAccessException {
        if (parameter != null && !field.getType().isAssignableFrom(parameter.getClass())) {
            String errorMessage = String.format("Incompatible parameter type: expected %s, but got %s", field.getType().getName(), parameter.getClass().getName());
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        field.set(instance, parameter);
    }

    private static void assignRandomValueToField(Object instance, Field field) throws IllegalAccessException {
        if (String.class.equals(field.getType())) {
            field.set(instance, RandomData.getString());
        }
    }

    private static void assignGeneratedBaseModel(Object instance, Field field, List<BaseModel> generatedModels) throws IllegalAccessException {
        BaseModel generatedInstance = generatedModels.stream()
            .filter(m -> m.getClass().equals(field.getType()))
            .findFirst()
            .orElseGet(() -> generate(generatedModels, field.getType().asSubclass(BaseModel.class)));
        field.set(instance, generatedInstance);
    }

    private static void assignGeneratedList(Object instance, Field field, List<BaseModel> generatedModels) throws IllegalAccessException {
        if (!(field.getGenericType() instanceof ParameterizedType pt)) {
            String errorMessage = String.format("Field %s must have a parameterized type", field.getName());
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Class<?> typeClass = (Class<?>) pt.getActualTypeArguments()[0];
        if (!BaseModel.class.isAssignableFrom(typeClass)) {
            String errorMessage = String.format("Field %s must be a list of objects that are subclasses of BaseModel", field.getName());
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        BaseModel generatedInstance = generatedModels.stream()
            .filter(m -> m.getClass().equals(typeClass))
            .findFirst()
            .orElseGet(() -> generate(generatedModels, typeClass.asSubclass(BaseModel.class)));

        field.set(instance, new ArrayList<>(List.of(generatedInstance)));
    }
}
