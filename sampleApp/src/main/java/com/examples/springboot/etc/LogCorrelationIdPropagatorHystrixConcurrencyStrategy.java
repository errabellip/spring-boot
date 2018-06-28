package com.examples.springboot.etc;

import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

/**
 * 
 * @author Kingsly Theodar Rajasekar
 *
 */
@Configuration
public class LogCorrelationIdPropagatorHystrixConcurrencyStrategy {

	private static final String CORRELATION_ID = "correlationid";

	@PostConstruct
	public void setupLogCorrelationForHystrixThreadsStrategy() {
		HystrixPlugins.getInstance().registerConcurrencyStrategy(new HystrixConcurrencyStrategy() {
			@Override
			public <T> Callable<T> wrapCallable(final Callable<T> callable) {
				return new LogCorrelationIdPropagatorCallableWrapper<T>(callable);
			}
		});
	}

	private class LogCorrelationIdPropagatorCallableWrapper<K> implements Callable<K> {

		private final Callable<K> actual;
		// This is how state is transferred from Web to Hystrix thread
		private final String parentCorrelationId;

		// This will be run by web thread
		public LogCorrelationIdPropagatorCallableWrapper(Callable<K> actual) {
			this.actual = actual;
			this.parentCorrelationId = MDC.get(CORRELATION_ID);
		}

		@Override
		// This will be run by Hystrix thread
		public K call() throws Exception {
			try {
				MDC.put(CORRELATION_ID, parentCorrelationId);
				return actual.call();
			} finally {
				MDC.remove(CORRELATION_ID);
			}
		}

	}

}