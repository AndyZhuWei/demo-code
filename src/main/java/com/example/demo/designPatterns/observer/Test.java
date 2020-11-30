package com.example.demo.designPatterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HP
 * @Description 模拟TestFrame程序
 * @date 2020/9/12-21:36
 */
public class Test {
    public static void main(String[] args) {
        Button b = new Button();
        b.addActionListener(new Button.MyActionListener());
        b.addActionListener(new Button.MyActionListener2());
        b.buttonPressed();

    }

    static class Button {
        private List<ActionListener> actionListeners = new ArrayList<>();

        public void buttonPressed() {
            ActionEvent e = new ActionEvent(System.currentTimeMillis(),this);
            for(int i=0;i<actionListeners.size();i++) {
                ActionListener l = actionListeners.get(i);
                l.actionPerformed(e);
            }
        }

        public void addActionListener(ActionListener l) {
            actionListeners.add(l);
        }


        interface ActionListener {
            public void actionPerformed(ActionEvent e);
        }

        static class MyActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("button pressed!");
            }
        }

        static class MyActionListener2 implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("button pressed 2!");
            }
        }

        class ActionEvent {
            long when;
            Object source;

            public ActionEvent(long when, Object source) {
                super();
                this.when = when;
                this.source = source;
            }

            public long getWhen() {
                return when;
            }

            public void setWhen(long when) {
                this.when = when;
            }

            public Object getSource() {
                return source;
            }

            public void setSource(Object source) {
                this.source = source;
            }
        }
    }
}


