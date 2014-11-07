package org.robotninjas.concurrent;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;

/**
 * Tests for compilation of {@code FluentFuture} to ensure that we can compile certain generic combinations.
 *
 * @author Matt Nathan
 */
@SuppressWarnings("unused")
public class FluentFutureCompilerChecks {
    // NB: the checks in here will _never_ run, this is used to prove that what compiles with Guava compiles with
    // FluentFuture

    private FluentFuture<FluentFutureCompilerChecks> subject1 = null;
    private FluentFuture<Object> subject2 = null;
    private Function<Object, String> func = Functions.toStringFunction();
    private AsyncFunction<Object, String> async = new AsyncFunction<Object, String>() {
        @Override
        public ListenableFuture<String> apply(Object input) throws Exception {
            return FluentFutures.from(String.valueOf(input));
        }
    };
    private Executor executor = null;

    public void checkFluentTransformFunctionInputWildcard() {
        // check both guava and FluentFuture, they should both compile
        ListenableFuture<String> guavaResult = Futures.transform(subject1, func);
        FluentFuture<String> output = subject1.transform(func);
    }

    public void checkFluentTransformFunctionOutputWildcard() {
        // check both guava and FluentFuture, they should both compile
        ListenableFuture<CharSequence> guavaResult = Futures.<Object, CharSequence>transform(subject2, func);
        FluentFuture<CharSequence> output = subject2.<CharSequence>transform(func);
    }

    public void checkFluentExecutorTransformFunctionInputWildcard() {
        // check both guava and FluentFuture, they should both compile
        ListenableFuture<String> guavaResult = Futures.transform(subject1, func, executor);
        FluentFuture<String> output = subject1.transform(executor, func);
    }

    public void checkFluentExecutorTransformFunctionOutputWildcard() {
        // check both guava and FluentFuture, they should both compile
        ListenableFuture<CharSequence> guavaResult = Futures.<Object, CharSequence>transform(subject2, func, executor);
        FluentFuture<CharSequence> output = subject2.<CharSequence>transform(executor, func);
    }

    public void checkFluentAsyncTransformFunctionInputWildcard() {
        // check both guava and FluentFuture, they should both compile
        ListenableFuture<String> guavaResult = Futures.transform(subject1, async);
        FluentFuture<String> output = subject1.transform(async);
    }

    public void checkFluentAsyncTransformFunctionOutputWildcard() {
        // check both guava and FluentFuture, they should both compile
        ListenableFuture<CharSequence> guavaResult = Futures.<Object, CharSequence>transform(subject2, async);
        FluentFuture<CharSequence> output = subject2.<CharSequence>transform(async);
    }

    public void checkFluentExecutorAsyncTransformFunctionInputWildcard() {
        // check both guava and FluentFuture, they should both compile
        ListenableFuture<String> guavaResult = Futures.transform(subject1, async, executor);
        FluentFuture<String> output = subject1.transform(executor, async);
    }

    public void checkFluentExecutorAsyncTransformFunctionOutputWildcard() {
        // check both guava and FluentFuture, they should both compile
        ListenableFuture<CharSequence> guavaResult = Futures.<Object, CharSequence>transform(subject2, async, executor);
        FluentFuture<CharSequence> output = subject2.<CharSequence>transform(executor, async);
    }
}
