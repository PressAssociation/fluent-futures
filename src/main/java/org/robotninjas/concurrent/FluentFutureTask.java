/*
 * (C) Copyright 2014 David Rusek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.robotninjas.concurrent;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;

import java.util.concurrent.*;

/**
 * A {FutureTask<V>} which implements {FluentFuture<V>}
 *
 * @param <V> The result type returned by this FluentFutureTask's <tt>get</tt> method
 * @see java.util.concurrent.FutureTask
 * @see org.robotninjas.concurrent.FluentFuture
 */
public class FluentFutureTask<V> extends FutureTask<V> implements FluentFuture<V>, RunnableFuture<V> {

    private final ExecutionList executionList = new ExecutionList();

    FluentFutureTask(Callable<V> callable) {
        super(callable);
    }

    FluentFutureTask(Runnable runnable, V result) {
        super(runnable, result);
    }

    public static <V> FluentFutureTask<V> create(Callable<V> callable) {
        return new FluentFutureTask<V>(callable);
    }

    public static <V> FluentFutureTask<V> create(Runnable runnable, V result) {
        return new FluentFutureTask<V>(runnable, result);
    }

    @Override
    public <Y> FluentFuture<Y> transform(Function<? super V, ? extends Y> func) {
        return new FluentDecorator<Y>(Futures.transform(this, func));
    }

    @Override
    public <Y> FluentFuture<Y> transform(Executor executor, Function<? super V, ? extends Y> func) {
        return new FluentDecorator<Y>(Futures.transform(this, func, executor));
    }

    @Override
    public <Y> FluentFuture<Y> transform(AsyncFunction<? super V, ? extends Y> func) {
        return new FluentDecorator<Y>(Futures.transform(this, func));
    }

    @Override
    public <Y> FluentFuture<Y> transform(Executor executor, AsyncFunction<? super V, ? extends Y> func) {
        return new FluentDecorator<Y>(Futures.transform(this, func, executor));
    }

    @Override
    public FluentFuture<V> withFallback(FutureFallback<V> fallback) {
        return new FluentDecorator<V>(Futures.withFallback(this, fallback));
    }

    @Override
    public FluentFuture<V> withFallback(Executor executor, FutureFallback<V> fallback) {
        return new FluentDecorator<V>(Futures.withFallback(this, fallback, executor));
    }

    @Override
    public FluentFuture<V> addCallback(FutureCallback<V> callback) {
        Futures.addCallback(this, callback);
        return this;
    }

    @Override
    public FluentFuture<V> addCallback(Executor executor, FutureCallback<V> callback) {
        Futures.addCallback(this, callback, executor);
        return this;
    }

    @Override
    public FluentFuture<V> onSuccess(Executor executor, final Consumer<V> callback) {
        return addCallback(executor, ConsumerDecorator.success(callback));
    }

    @Override
    public FluentFuture<V> onSuccess(final Consumer<V> callback) {
        return onSuccess(MoreExecutors.sameThreadExecutor(), callback);
    }

    @Override
    public FluentFuture<V> onFailure(Executor executor, final Consumer<Throwable> callback) {
        return addCallback(executor, ConsumerDecorator.<V>failure(callback));
    }

    @Override
    public FluentFuture<V> onFailure(final Consumer<Throwable> callback) {
        return onFailure(MoreExecutors.sameThreadExecutor(), callback);
    }

    @Override
    public <E extends Exception> FluentCheckedFuture<V, E> makeChecked(Function<Exception, E> func) {
        return new CheckedDecorator<V, E>(Futures.makeChecked(this, func));
    }

    @Override
    public V get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.get(l, timeUnit);
    }

    @Override
    public <X extends Exception> V get(long l, TimeUnit timeUnit, Class<X> exceptionClass) throws X {
        return Futures.get(this, l, timeUnit, exceptionClass);
    }

    @Override
    public <Y extends Exception> V get(Class<Y> exceptionClass) throws Y {
        return Futures.get(this, exceptionClass);
    }

    @Override
    public void addListener(Runnable listener, Executor executor) {
        executionList.add(listener, executor);
    }

    @Override
    protected void done() {
        executionList.execute();
    }

}
