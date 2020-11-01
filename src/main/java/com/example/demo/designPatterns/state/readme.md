状态模式
根据状态决定行为

如果一个类中的operation需要根据不同的state实现的话，那么我们可以把state抽象出来，然后再state中实现这个方法

如果一个类中的operation不再扩展的时候，state需要扩展的，则使用state模式比较方便。
如果一个类中operation扩展则时候state模式不方便。
如果operation和state都不扩展，则使用switch就很方便了，也不用创建那么多子类

有限状态机(FSM)
状态是有限的，状态与状态之间是通过action来迁移的。例如：线程状态迁移

首先，线程的状态是有限的，简单说有 new、ready、running、blocked、waiting、time waiting、terminated
我们new出一个线程时，线程的状态就是new。
当我们调用线程的start方法时就会被操作系统的线程调度器拿过去执行，此时的状态可以称为执行态（执行态有两种：
    ready:线程处在一个就绪队列中。running(线程调度器选中就绪队列中的线程后，线程才开始执行，此时的状态就是running)），再running状态下调用线程的yield
    方法线程就会进入就绪队列，过一会可能就会进入running状态。
线程执行完就会进入terminated状态
如果再运行中执Thead.sleep(time),o.wait(time),t.join(time),LockSupport.parkNanos(),LockSupport.parkUntil()方法就会进行time waiting状态,在等待
   时间过后就会又被操作系统进行调度
正在执行的线程执行o.wait(),t.join(),LockSupport.park()等没有时间的方法就会进入waiting状态，无限等待，直到主动调用o.notify(),o.notifyAll(),
  LockSupport.unpark()为止，才会重新回到执行态
如果在执行态的时候执行到了syncronized块的时候，就会进入阻塞的状态blocked。知道获取锁然后在进入到执行态


状态模式和有限状态机不是一个东西




