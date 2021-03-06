
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RxJavaWork {

    public static void main(String[] args) {

        testObservable();
        testZipObservable();
        testMultiCast();

    }

    private static void testObservable() {
        String[] numberStrings = new String[] {"one", "two", "three"};
        Observable.fromArray(numberStrings)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(String numberString) {
                        System.out.println("onNext - " + numberString);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });

        List<String> numberStringList = Arrays.asList("one1", "two1", "three1", "four1", "five1");
        Observable.fromArray(numberStringList, numberStringList)
                .subscribe(new Observer<List>() {
                               @Override
                               public void onSubscribe(Disposable disposable) {
                                   System.out.println("onSubscribe1");
                               }

                               @Override
                               public void onNext(List list) {
                                   System.out.println("onNext1 - " + list);
                               }

                               @Override
                               public void onError(Throwable throwable) {
                                   System.out.println("onError1");
                               }

                               @Override
                               public void onComplete() {
                                   System.out.println("onComplete1");
                               }
                           }
                );

        Observable.fromIterable(numberStringList)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        System.out.println("onSubscribeIterable");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext Iterable - " + s);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("onErrorIterable");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onCompleteIterable");
                    }
                });

        Observable.fromIterable(numberStringList)
                .buffer(2)
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        System.out.println("onCompleteIterableBuffer");
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        System.out.println("onNext Iterable Buffer - " + strings);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("onCompleteIterableBuffer");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onCompleteIterableBuffer");
                    }
                });

    }

    private static void testZipObservable() {
        String[] numberStrings = new String[] {"one", "two", "three"};
        Integer[] numbers = new Integer[] {1,2,3,4,5};
        int[] numbers1 = new int[] {1,2,3,4,5};
        Observable<Integer> arrayObservable1 = Observable.fromArray(numbers);
        Observable<Integer> arrayObservable2 = Observable.fromArray(numbers);
        Observable zipObservables = Observable.zip(arrayObservable1, arrayObservable2, (o, o2) -> o + o2);

        zipObservables.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                System.out.println("onSubcribezipObservables");
            }

            @Override
            public void onNext(Integer o) {
                System.out.println("onNextzipObservables: " + o);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onErrorzipObservables");
            }

            @Override
            public void onComplete() {
                System.out.println("onCompletezipObservables");
            }
        });
        List<String> numberStringList = Arrays.asList("one1", "two1", "three1", "four1", "five1");
        Observable listObservable = Observable.fromIterable(numberStringList);
        Observable zipObservablesCollection = Observable.zip(arrayObservable1, listObservable, (o, o2) -> {
//             String s = o.toString() + "----" +o2.toString();
//             throw new Exception("test exception");
            return o2;
        });
        zipObservablesCollection.subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable disposable) {
                System.out.println("onSubcribezipObservablesCollection");
            }

            @Override
            public void onNext(Object o) {
                System.out.println("onNextzipObservablesCollection: " + o);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onErrorzipObservablesCollection: " + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onCompletezipObservablesCollection");
            }
        });
//fromCallable
        //fromFuture
    }


    private static void testMultiCast() {
        List<String> numberStringList = Arrays.asList("one1", "two1", "three1", "four1", "five1");
        Observable listObservable = Observable.range(1,10);
        listObservable
                .doOnNext(s -> System.out.println(Thread.currentThread().getName() + ":     " + s))
                .observeOn(Schedulers.newThread())
                //.subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        System.out.println(Thread.currentThread().getName() + ":     " + "onSubcribeObservables1");
                    }

                    @Override
                    public void onNext(Integer s) {
                        try {
                            System.out.println(Thread.currentThread().getName() + ":     " + "onNextObservables1: " + s);
//                    System.out.println("onNextObservables1: " + s);
                            Thread.sleep(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("onErrorObservables1: " + throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println(Thread.currentThread().getName() + ":     " + "onCompleteObservables1");
                    }
                });

        System.out.println("------------------------------------------------------------------------------");
        listObservable
                .observeOn(Schedulers.newThread())
                .doOnNext(s -> System.out.println(Thread.currentThread().getName() + ":     second" + s))

                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        System.out.println(Thread.currentThread().getName() + ":     " + "onSubcribezipObservables2");
                    }

                    @Override
                    public void onNext(Integer s) {
                        System.out.println(Thread.currentThread().getName() + ":     " + "onNextzipObservables2: " + s);
//                System.out.println("onNextzipObservables2: " + s);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("onErrorzipObservables2: " + throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println(Thread.currentThread().getName() + ":     " + "onCompletezipObservables2");
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
