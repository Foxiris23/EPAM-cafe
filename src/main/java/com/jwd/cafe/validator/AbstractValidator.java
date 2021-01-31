package com.jwd.cafe.validator;

/**
 * The abstract class of application validator that uses chain of responsibility pattern
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public abstract class AbstractValidator implements Validator {
    protected Validator next;

    public AbstractValidator(final Validator next) {
        this.next = next;
    }
}
