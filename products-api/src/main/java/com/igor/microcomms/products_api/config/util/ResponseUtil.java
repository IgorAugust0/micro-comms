package com.igor.microcomms.products_api.config.util;

import java.util.List;
import java.util.function.Function;

import com.igor.microcomms.products_api.config.exception.AuthenticationException;
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

    public enum ExceptionType {
        VALIDATION, AUTHENTICATION, AUTHORIZATION, NOT_FOUND, INTERNAL_SERVER_ERROR
    }

    public static void validateNotEmpty(Object value, String message) {
        validateNotEmpty(value, message, ExceptionType.VALIDATION);
    }

    /**
     * Validates that the given value is not empty.
     *
     * @param value   the value to be validated
     * @param message the error message to be thrown if the value is empty
     * @param type    the type of exception to be thrown (ValidationException or
     *                AuthenticationException)
     * @throws ValidationException      if the value is empty and the type is
     *                                  ValidationException
     * @throws AuthenticationException  if the value is empty and the type is
     *                                  AuthenticationException
     * @throws IllegalArgumentException if the type is neither ValidationException
     *                                  nor AuthenticationException
     */
    public static void validateNotEmpty(Object value, String message, ExceptionType type) {
        if (isEmpty(value)) {
            switch (type) {
                case VALIDATION -> throw new ValidationException(message);
                case AUTHENTICATION -> throw new AuthenticationException(message);
                default -> throw new IllegalArgumentException("Invalid exception type");
            }
        }
    }

}
