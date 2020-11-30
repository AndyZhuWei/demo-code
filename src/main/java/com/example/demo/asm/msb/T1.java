package com.example.demo.asm.msb;

/**
 * @author HP
 * @Description 光标必须位于类体内，view-show bytecode 这是idea自带的工具可以查看一个class文件的二进制汇编码。
 * 如果想要显示更好，可以自己下载一个插件jclasslib bytecode viewer
 * @date 2020/9/13-21:15
 */
public class T1 {
    int i = 0;
    public void m() {
        int j = 1;
    }
}
