package com.jwd.cafe.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * The inherited class of all application entities
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Data
public abstract class AbstractEntity implements Serializable, Cloneable {
}