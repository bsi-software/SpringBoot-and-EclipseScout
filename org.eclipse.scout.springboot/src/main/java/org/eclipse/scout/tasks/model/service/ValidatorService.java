package org.eclipse.scout.tasks.model.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public interface ValidatorService<T> {

  /**
   * Returns the a default validator with JSR-303 support. Overwrite this method for custom validation.
   */
  default Validator getValidator() {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();

    return validator;
  }

  /**
   * Validates the provided object using the validator provided by {@link getValidator}. If the provided object does not
   * validate a {@link ConstraintViolationException} is thrown.
   */
  default void validate(T object) throws ConstraintViolationException {
    Set<ConstraintViolation<T>> violations = getValidator().validate(object);

    if (violations.size() > 0) {
      throw new ConstraintViolationException(violations);
    }
  }
}
