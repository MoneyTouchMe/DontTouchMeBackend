package com.example.donttouchme.common.aop.querycounter;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class ConnectionHandler implements InvocationHandler {

    private final Object target;
    private final QueryCounter counter;

    public ConnectionHandler(Object target, QueryCounter counter) {
        this.target = target;
        this.counter = counter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        countPrepareStatement(method);
        logQueryCount(method);
        return method.invoke(target, args);
    }

    private void logQueryCount(Method method) {
        if (method.getName().equals("close")) {
            warnTooManyQuery();
            log.info("====== 발생한 쿼리 수 : {} =======\n", counter.getCount());
        }
    }

    private void countPrepareStatement(Method method) {
        if (method.getName().equals("prepareStatement")) {
            counter.increase();
        }
    }

    private void warnTooManyQuery() {
        if (counter.isWarn()) {
            log.warn("======= Too Many Query !!!! =======");
        }
    }
}
