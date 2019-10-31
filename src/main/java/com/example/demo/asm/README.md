#ASM 库的介绍和使用
主要介绍 ASM 库的结构、主要的 API，并且通过两个示例说明如何通过 ASM 修改 .class 文件中的方法和属性。

![ASM库的介绍和使用](https://upload-images.jianshu.io/upload_images/4179925-e6b0a552b88874bd.png)
`使用相对路径是图片在idea中不显示 对应asm1.png`

## 一.ASM的结构
ASM库是一款基于Java字节码层面的代码分析和修改工具。ASM可以直接生产二进制的class文件，也可以在类被加载入
JVM之前动态修改类行为。
ASM库的结构如下所示
![ASM库的结构](https://upload-images.jianshu.io/upload_images/4179925-d4f950ec94a12cde.png)
`使用相对路径的图片在idea中不显示，对应asmConstruct.png`

* Core: 为其他包提供基础的读、写、转化Java字节码和定义API,并且可以生成Java字节码和实现大部分字节码
的转换，在[访问者模式和ASM](https://www.jianshu.com/p/e4b8cb0b3204)中介绍的几个中国要的类就在Core API中：
* Tree: 提供了Java字节码在内存中的表现
* Commons:提供了一些常用的简化字节码生成、转换的类和适配器
* Util：包含一些帮助类和简单的字节码修改类，有利于在开发或者测试中使用
* XML: 提供一个适配器将XML和SAX-comliant转换成字节码结构，可以允许使用XSLT去定义字节码转换

## 二.Core API介绍
### 2.1 ClassVisitor抽象类
如下所示，在ClassVisitor中提供了和类结构同名的一些方法，这些法规范会对类中相应的部分进行操作，而且是有顺序
的:`visit[visitSource][visitOuterClass](visitAnnotation|visitAttribute)*(visitInnerClass|
visitField|visitMethod)*visitEnd`

```java
public abstract class ClassVisitor {

        ......
    
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces);
    public void visitSource(String source, String debug);
    public void visitOuterClass(String owner, String name, String desc);
    public AnnotationVisitor visitAnnotation(String desc, boolean visible);
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible);
    public void visitAttribute(Attribute attr);
    public void visitInnerClass(String name, String outerName, String innerName, int access);
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value);
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions);
    public void visitEnd();
}
```

1. void visit(int version,int access,String name,String signature,String superName,String[] interfaces)
该方法是当扫描类时第一个调用的方法，主要用于类声明使用。下面是对方法中各个参数的示意：
visit(类版本,修饰符,类名,泛型信息,继承的父类,实现的接口)
2.AnntationVisitor visitAnnotation(String des,boolean visible)
该方法是当扫描器扫描到类注解声明时进行调用。下面是对方法中各个参数的示意：
visitAnnotation(注解类型，注解是否可以在JVM中可见)
3.FieldVisitor visitField(int access,String name,String desc,String signature,Object value)
该方法是当扫描器扫描到类中字段时进行调用。下面是对方法中各个参数示意：
visitField(修饰符，字段名，字段类型，泛型描述，默认值)
4.MethodVisitor visitMethod(int access,String name,String desc,String signature,String[] exceptions)
该方法是当扫描器扫描到类的方法时进行调用。下面是对方法中各个参数示意：
visitMethod(修饰符，方法名，方法签名，泛型信息，抛出的异常)
5.void visitEnd()
该方法是当扫描器完成类扫描时才会调用，如果想在类中追加某些方法

### 2.2 ClassReader类
这个类会将.class文件读入到ClassReader中的字节数组中，它的accept方法接受一个ClassVisitor实现类，
并按照顺序调用ClassVisitor中的方法

### 2.3 ClassWriter类
ClassWriter是一个ClassVisitor的子类，是和ClassReader对应的类，ClassReader是将.class文件读入
到一个字节数组中，ClassWriter是将修改后的类的字节码内容以字节数组的形式输出。

### 2.4 MethodVisitor & AdivceAdapter
MethodVisitor是一个抽象类，当ASM的ClassReader读取到Method时就转入MethodVisitor接口处理。
AdviceAdapter是MethodVisitor的子类，使用AdviceAdapter可以更方便的修改方法的字节码
AdviceAdapter的方法如下所示：
![AdviceAdapter](https://upload-images.jianshu.io/upload_images/4179925-f5a428b729962860.png)
`使用相对路径的图片在idea中不显示，对应AdviceAdapter.png`

其中比较重要的几个方法如下：
1.void visitCode():表示ASM开始扫描这个方法
2.void onMethodEnter():进入这个方法
3.void onMethodExit():即将从这个方法出去
4.void onVisitEnd():表示方法扫码完毕

### 2.5 FieldVisitor抽象类
FieldVisitor是一个抽象类，当ASM的ClassReader读取到Field时就传入FieldVisitor接口处理。
和分析MethodVisitor的方法一样，也可以查看源码注释进行学习。

### 2.6 操作流程
1.需要创建一个ClassReader对象，将.class文件的内容读入到一个字节数组中
2.然后需要一个ClassWriter的对象将操作之后的字节码的字节数组回写
3.需要事件过滤器ClassVisitor。在调用ClassVisitor的某些方法时会产生一个新的XXXVisitor
对象，当我们需要修改对于的内容时只需要实现自己的XXXVisitor并返回就可以了。

## 示例
### 3.1修改类中方法的字节码
假如现在我们有一个HelloWorld类
通过javac HelloWorld.java和javap -verbose HelloWorld.class可以查看到sayName()方法
的字节码
我们通过ASM修改HelloWrold.class字节码文件，实现统计方法执行时间的功能，详情见CostTime

### 3.2 修改类中属性的字节码
这一节中我们将展示一下如何使用Core API对类中的属性进行操纵
假如说，现在有一个Person.java类
```java
public class Person {
    private String name;
    private int sex;
}
```
我们想为这个类，添加一个`public int age`的属性该怎么添加呢？我们会面对两个问题：
1.该调用ASM的哪个API添加属性呢？
2.在何时写添加属性的代码？
接下来，我们就一一解决上面的两个问题？

###3.2.1 添加属性的API
按照我们分析的上述的2.6操作流程叙述，需要以下三个步骤：
1.需要创建一个ClassReader对象，将.class文件的内容读入到一个字节数组中
2.然后需要一个ClassWriter的对象将操作之后的字节码的字节数组回写
3.需要创建一个事件过滤器ClassVisitor.事件过滤器中的某些方法可以产生一个新的XXXVisitor
对象，当我们需要修改对应的内容时只要实现自己的XXXVisitor并返回就可以了。

在上面三个步骤中，可以操作的就是ClassVisitor了。ClassVisitor接口提供了和类结构同名的一些方法，
这些方法可以对相应的类结构进行操作。

在使用ClassVisitor添加类属性的时候，只需要添加一句话就可以了。
`classVisitor.visitField(Opcodes.ACC_PUBLIC,"age",Type.getDescriptor(int.class),null,null)`

### 3.2.2 添加属性的时机
我们先暂且在ClassVisitor的visitEnd()方法中写入上面的代码，详情见FieldPractice
那如果我们尝试在 ClassVisitor#visitField() 方法中添加属性可以吗？我们可以修改 Transform 测试一下：
详情见：Transform


## 3.3 ASMifier
可能有人会问，我刚开始学，上面例子中那些ASM的代码我还不会写，怎么办呢？ASM官方为我们提供了ASMifier,可以帮助我们生成这些晦涩难懂的ASM代码
比如，我想通过ASM实现统计一个方法的执行时间，该怎么做呢？一般会有如下的代码：
```java
public class Demo {

    public void costTime() {
        long startTime = System.currentTimeMillis();
        // ......
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("The cost time of this method is " + duration + " ms");
    }
}
```
那上面这段代码对应的 ASM 代码是什么呢？我们可以通过以下两个步骤，使用 ASMifier 自动生成：
1.通过 javac 编译该 Demo.java 文件生成对应的 Demo.class 文件，如下所示
`javac Demo.java`
2.通过 ASMifier 自动生成对应的 ASM 代码。首先需要在ASM官网 下载 asm-all.jar 库，我下载的是最新的 asm-all-5.2.jar，然后使用如下命令，即可生成
`java -classpath asm-all-5.2.jar org.objectweb.asm.util.ASMifier Demo.class `
截图如下：
![asmifier](https://upload-images.jianshu.io/upload_images/4179925-0cc8712718f08ea0.png)
`使用相对路径的图片在idea中不显示，对应asmifier.png`





























