
synchronized的底层实现
JDK早期 重量级都是用os底层的锁
后来改进
锁升级的概念：
  我就是厕所所长（一二）

sync (object)
markword记录这个线程id(偏向锁)
如果线程争用：升级为 自旋锁
自旋10次后升级为重量级锁 需要通过OS加锁，进而进入等待队列，不占用CPU资源了

在用户态执行效率高，在系统内核态执行效率就低了，所以通过OS加锁性能就不好

执行时间端（加锁代码），线程数少，用自选
执行时间长，线程多，用系统锁


synchronized不能用String常量,Integer,Long

如果设置一个Object的锁时最好加上final,担心在加锁代码中改变了锁对象导致错误

synchronized优化
细化和粗化
CAS(无锁优化 自旋)
CAS的ABA问题对引用类型可能有影响

Unsafe相当于C C++的指针，在jdk10中可以直接得到这个单例类，在此之前只能通过反射使用
但是在JDK1.9之后内部api应该被关闭了，可以编译，但是运行是不行的


Condition的本质就是不同的等待队列