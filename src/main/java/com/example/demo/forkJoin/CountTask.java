package com.example.demo.forkJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @Author: zhuwei
 * @Date:2018/12/7 16:34
 * @Description: 使用Fork/Join并行框架
 *
 * 这个程序是将1+2+3+4+5+6拆分成1+2；3+4；5+6三个部分进行子程序进行计算后合并
 *
 *
 */
public class CountTask extends RecursiveTask<Integer> {

    private static final long serialVersionUID = 1L;

    //阈值
    private static final int THRESHOLD = 2;
    private int start;
    private int end;

    public CountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }



    @Override
    protected Integer compute() {
        int sum = 0;
        //判断任务是否足够小
        boolean canCompute = (end-start)<=THRESHOLD;
        if(canCompute) {
            //如果小于阈值，就进行计算
            for(int i=start;i<=end;i++) {
                sum += i;
            }
        } else {
            //如果大于阈值，就再进行任务拆分
            int middle = (start+end)/2;
            CountTask leftTask = new CountTask(start,middle);
            CountTask rightTask = new CountTask(middle+1,end);
            //执行子任务
            leftTask.fork();
            rightTask.fork();
            //等待子任务执行完，并得到执行结果
            int leftResult = leftTask.join();
            int rightResult = rightTask.join();
            //合并子任务
            sum = leftResult+rightResult;
        }
        return sum;
    }


    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CountTask task = new CountTask(1,5);
        //执行一个任务
        Future<Integer> result = forkJoinPool.submit(task);
        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
