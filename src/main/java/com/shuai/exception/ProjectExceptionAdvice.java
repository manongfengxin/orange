package com.shuai.exception;

import com.shuai.util.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*作为springMVC的异常处理器*/
//@ControllerAdvice
@RestControllerAdvice//包含上面的注解，还包含有@ResponseBody
public class ProjectExceptionAdvice {
    // 拦截所有的异常信息
    @ExceptionHandler
    public Result doException(Exception ex) {
        // 记录日志
        // 发送信息给运维
        // 发送邮件给开发人员，ex对象发送给开发人员
        // 控制台会显示异常信息
        ex.printStackTrace();
        return Result.fail("服务器故障，请稍后重试");
    }
}
