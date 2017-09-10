package org.lzbruby.netty.core;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * 功能描述：Service方法性能日志
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/1/11 time:17:30.
 */
public class CommonDigestInterceptor implements InitializingBean, MethodInterceptor {


    /**
     * 摘要类型，不能为空
     */
    private String digestType;

    /**
     * 日志：以摘要类型初始化
     */
    private Log logger;

    /**
     * 需要忽略的方法名
     */
    private String[] ignoreMethodNames = new String[]{
            "java.lang.Object.toString",
            "org.springframework.dispatch.ApplicationListener.onApplicationEvent"};

    /**
     * @see MethodInterceptor#invoke(MethodInvocation)
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String className = invocation.getMethod().getDeclaringClass().getName();
        String methodName = invocation.getMethod().getName();

        String totalMethodName = className + "." + methodName;
        if (ArrayUtils.contains(ignoreMethodNames, totalMethodName)) {
            return invocation.proceed();
        }

        if (logger.isInfoEnabled()) {
            // 是否处理成功标识
            boolean isSuccess = false;
            long startTime = System.currentTimeMillis();
            try {
                Object result = invocation.proceed();
                isSuccess = true;
                return result;
            } finally {
                long elapseTime = System.currentTimeMillis() - startTime;
                logger.info(totalMethodName + ", " + (isSuccess ? "Y" : "N") + ", " + elapseTime + "ms");
            }
        }
        return invocation.proceed();
    }

    /**
     * @see InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isBlank(digestType)) {
            throw new Exception("digestType属性不能为空");
        }
        // 初始化日志
        logger = LogFactory.getLog(digestType);
    }

    public void setDigestType(String digestType) {
        this.digestType = digestType;
    }

    public void setIgnoreMethodNames(String[] ignoreMethodNames) {
        this.ignoreMethodNames = ignoreMethodNames;
    }
}
