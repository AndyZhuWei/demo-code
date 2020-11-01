package com.example.demo.designPatterns.command;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/15-22:27
 */
public class InsertCommand extends Command{

    Content c;
    String strToInsert = "http://www.mashibing.com";
    public InsertCommand(Content c) {
        this.c = c;
    }

    @Override
    public void doit() {
        c.msg=c.msg+strToInsert;
    }

    @Override
    public void undo() {
        c.msg = c.msg.substring(0,c.msg.length()-strToInsert.length());

    }
}
