package com.ALGXeon.lingxiquiz;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RxjavaTest {
    @Test
    void rxJavaDemo() throws InterruptedException {
        // 创建一个流，每秒发射一个递增的整数（数据流变化）
        Flowable<Long> flowable = Flowable.interval(1, TimeUnit.SECONDS) // 每秒生成递增的数0开始
                .map(i -> i + 1)
                .subscribeOn(Schedulers.io()); // 指定创建流的线程池

        // 订阅 Flowable 流，并打印每个接受到的数字
        flowable.observeOn(Schedulers.io())
                .doOnNext(item -> System.out.println(item.toString()))
                .subscribe();

        // 让主线程睡眠，以便观察输出
        Thread.sleep(10000L);
    }

}
