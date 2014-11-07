package org.robotninjas.concurrent;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Checks to make sure we can compile certain generic combinations of {@code FluentFutures}.
 *
 * @author Matt Nathan
 */
@SuppressWarnings("unused")
public class FluentFuturesCompilerChecks {
    private Iterable<ListenableFuture<String>> guavaArg = null;
    private Iterable<FluentFuture<String>> fluentArg = null;
    private Executor executor;

    public void checkFromIterableFutures() {
        ListenableFuture<List<String>> guavaResult = Futures.allAsList(guavaArg);
        FluentFuture<List<String>> fluentResult = FluentFutures.from(guavaArg);
        FluentFuture<List<String>> fluentExecutorResult = FluentFutures.from(guavaArg, executor);
    }

    public void checkFromIterableFuturesResultSuper() {
        ListenableFuture<List<CharSequence>> guavaResult = Futures.<CharSequence>allAsList(guavaArg);
        FluentFuture<List<CharSequence>> fluentResult = FluentFutures.<CharSequence>from(guavaArg);
        FluentFuture<List<CharSequence>> fluentExecutorResult = FluentFutures.<CharSequence>from(guavaArg, executor);
    }

    public void checkFromIterableFuturesFutureExtension() {
        ListenableFuture<List<String>> guavaResult = Futures.allAsList(fluentArg);
        FluentFuture<List<String>> fluentResult = FluentFutures.from(fluentArg);
    }

    public void checkFromIterableFuturesFutureExtensionResultSuper() {
        ListenableFuture<List<CharSequence>> guavaResult = Futures.<CharSequence>allAsList(fluentArg);
        FluentFuture<List<CharSequence>> fluentResult = FluentFutures.<CharSequence>from(fluentArg);
        FluentFuture<List<CharSequence>> fluentExecutorResult = FluentFutures.<CharSequence>from(fluentArg, executor);
    }
}
