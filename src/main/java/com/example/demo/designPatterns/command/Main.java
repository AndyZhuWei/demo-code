package com.example.demo.designPatterns.command;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/15-22:18
 */
public class Main {
    public static void main(String[] args) {
        Content c = new Content();

        Command insertCommand = new InsertCommand(c);
        insertCommand.doit();
        insertCommand.undo();

        Command copyCommand = new CopyCommand(c);
        copyCommand.doit();
        copyCommand.undo();

        Command deleteCommand = new DeleteCommand(c);
        deleteCommand.doit();
        deleteCommand.undo();

        List<Command> commandList = new ArrayList<>();
        commandList.add(insertCommand);
        commandList.add(copyCommand);
        commandList.add(deleteCommand);

        for(Command command : commandList) {
            command.doit();
        }
        System.out.println(c.msg);
        for(int i=commandList.size()-1;i>=0;i--) {
            commandList.get(i).undo();
        }


        System.out.println(c.msg);
    }
}
