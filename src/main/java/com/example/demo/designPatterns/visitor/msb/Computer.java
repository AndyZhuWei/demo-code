package com.example.demo.designPatterns.visitor.msb;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/13-18:24
 */
public class Computer {
    ComputerPart cpu = new CPU();
    ComputerPart memory = new Memory();
    ComputerPart board = new Board();

    public void accept(Visitor v) {
        this.cpu.accept(v);
        this.memory.accept(v);
        this.board.accept(v);
    }

    public static void main(String[] args) {
        PersonVisitor p = new PersonVisitor();
        new Computer().accept(p);
        System.out.println(p.totalPrice);
    }
}
abstract class ComputerPart {
    abstract void accept(Visitor v);
    abstract double getPrice();
}

class CPU extends ComputerPart {
    @Override
    void accept(Visitor v) {
        v.visitCpu(this);
    }

    @Override
    double getPrice() {
        return 500;
    }
}
class Memory extends ComputerPart {
    @Override
    void accept(Visitor v) {
        v.visitMemory(this);
    }

    @Override
    double getPrice() {
        return 300;
    }
}

class Board extends ComputerPart {
    @Override
    void accept(Visitor v) {
        v.visitBoard(this);
    }

    @Override
    double getPrice() {
        return 200;
    }
}

interface Visitor {
    void visitCpu(CPU cpu);
    void visitMemory(Memory memory);
    void visitBoard(Board board);
}

class PersonVisitor implements Visitor {
    double totalPrice = 0.0;
    @Override
    public void visitCpu(CPU cpu) {
        totalPrice += cpu.getPrice()*0.9;
    }

    @Override
    public void visitMemory(Memory memory) {
        totalPrice += memory.getPrice()*0.85;
    }

    @Override
    public void visitBoard(Board board) {
        totalPrice += board.getPrice()*0.95;
    }
}

class CorpVisitor implements Visitor {
    double totalPrice = 0.0;
    @Override
    public void visitCpu(CPU cpu) {
        totalPrice += cpu.getPrice()*0.6;
    }

    @Override
    public void visitMemory(Memory memory) {
        totalPrice += memory.getPrice()*0.75;
    }

    @Override
    public void visitBoard(Board board) {
        totalPrice += board.getPrice()*0.75;
    }
}