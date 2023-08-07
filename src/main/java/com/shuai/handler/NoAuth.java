package com.shuai.handler;

import java.lang.annotation.*;

/**
 * 自定义注解：此注解标记的接口不需要token也能访问
 */
@Target({ElementType.METHOD})//METHOD:表明此注解只能放在方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuth {

}
