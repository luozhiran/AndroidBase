package com.plugin.androidbase.aop;

import com.itg.lib_log.L;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class Test {

    final String TAG = Test.class.getSimpleName();

    @Before("execution(* *..Activity.on*(..))")
    public void logLifeCycle(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getThis().getClass().getSimpleName();
        L.e( "class:" + className+"   method:" + methodSignature.getName());
    }
}
