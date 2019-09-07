package com.mini10.miniserver.annotation;

import java.lang.annotation.*;


/**
 * 自定义注解，用于拦截请求注释
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireSignature {
}
