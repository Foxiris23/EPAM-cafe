package com.jwd.cafe.validator;

public abstract class AbstractValidator implements Validator {
    protected Validator next;

    public AbstractValidator(final Validator next) {
        this.next = next;
    }
}
