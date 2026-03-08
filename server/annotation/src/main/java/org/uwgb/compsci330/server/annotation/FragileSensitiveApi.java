package org.uwgb.compsci330.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This method uses provides a <b>sensitive internal API</b>, possibly bypassing traditional verification checks for performance.
 *
 * <p>
 * <b>Please take care when using this method.</b>
 * </p>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface FragileSensitiveApi {
}
