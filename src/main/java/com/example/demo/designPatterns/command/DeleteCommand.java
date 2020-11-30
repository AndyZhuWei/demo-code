package com.example.demo.designPatterns.command;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/15-22:27
 */
public class DeleteCommand extends Command{

    Content c;
    String deleted;
    public DeleteCommand(Content c) {
        this.c = c;
    }

    @Override
    public void doit() {
        deleted = c.msg.substring(0,5);
        c.msg=c.msg.substring(5,c.msg.length());
    }

    @Override
    public void undo() {
        c.msg = deleted+c.msg;

    }
}
