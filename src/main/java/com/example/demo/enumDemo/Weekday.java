package com.example.demo.enumDemo;

/**
 * @Author: zhuwei
 * @Date:2018/12/7 17:04
 * @Description:
 */
public enum Weekday {
    SUN(0),MON(1),TUS(2),WED(3),THU(4),FRI(5),SAT(6);

    private int value;

    private Weekday(int value) {
        this.value = value;
    }

    public static Weekday getNextDay(Weekday nowDay) {
        int nextDayValue = nowDay.value;

        if(++nextDayValue==7) {
            nextDayValue=0;
        }

        return getWeekdayByValue(nextDayValue);
    }

    public static Weekday getWeekdayByValue(int value) {
        for(Weekday c : Weekday.values()) {
            if (c.value == value) {
                return c;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

