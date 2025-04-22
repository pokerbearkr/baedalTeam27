package org.example.baedalteam27.global.jwt;

import java.lang.annotation.*;

/**
 * LoginUser Annotation 정의
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {
}
