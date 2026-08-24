package io.github.octopus123.datamasking.aspect;

import io.github.octopus123.datamasking.processor.MaskProcessor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 脱敏时机
 */
@Aspect
public class MaskAspect {

    private final MaskProcessor maskProcessor;

    public MaskAspect(MaskProcessor maskProcessor) {
        this.maskProcessor = maskProcessor;
    }


    @Around("execution(* *..controller..*(..))")
    public Object mask(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("========== MaskAspect 执行 ==========");
        // 先执行Controller
        Object result = joinPoint.proceed();
        System.out.println("返回对象类型：" + result.getClass());
        // 对返回结果进行脱敏
        maskProcessor.process(result);
        return result;
    }


}
