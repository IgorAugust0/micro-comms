package com.igor.microcomms.products_api.config.util;

import java.util.List;
import java.util.function.Function;

import com.igor.microcomms.products_api.config.exception.ValidationException;

import static org.springframework.util.ObjectUtils.isEmpty;

public class ResponseUtil {

    /**
     * Converts a list of entities to a list of response objects using the provided
     * converter function.
     * Use with: ResponseConverter.convertToResponse(categoryRepository.findAll(),
     * CategoryResponse::of);
     *
     * @param <T>       the type of the entities in the input list
     * @param <R>       the type of the response objects in the output list
     * @param entities  the list of entities to be converted
     * @param converter the function that converts an entity to a response object
     * @return the list of response objects
     */
    public static <T, R> List<R> convertToResponse(List<T> entities, Function<T, R> converter) {
        return entities.stream().map(converter).toList();
    }

    /**
     * Validates that the given value is not empty.
     * Use with: ResponseConverter.validateNotEmpty(request.getDescription(),
     * "Description is required");
     *
     * @param value   the value to be validated
     * @param message the error message to be thrown if the value is empty
     * @throws ValidationException if the value is empty
     */
    public static void validateNotEmpty(Object value, String message) {
        if (isEmpty(value)) {
            throw new ValidationException(message);
        }
    }

}
