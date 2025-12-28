package cn.qy.MomoTech.utils;

import cn.qy.MomoTech.Exceptions;
import cn.qy.MomoTech.MomoTech;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static cn.qy.MomoTech.MomoTech.playerNumber;
import static cn.qy.MomoTech.MomoTech.tps;

public class NumberCombinator {
    private static int dateCache;
    private static long lastTimeDateCacheUpdate;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.####");
    private static void checkDate(){
        long now = System.currentTimeMillis();
        if (now - lastTimeDateCacheUpdate > 1000){
            lastTimeDateCacheUpdate = now;
            dateCache = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        }
    }
    public static String Checker(double i)
             {
        int n;

        if (Maths.GetRandom((Math.min (20,(int) tps) * 16777216 * 6)) <= playerNumber * i)
            return "ExN5" ;//throw new Exceptions.NumberBugVException();
        if (i >= 16777216) return "ExN3";//throw new Exceptions.NumberBugIIIException();
        if (i <= -16777216) return "ExN4";//throw new Exceptions.NumberBugIVException();
        checkDate();
        if (i == ((double) dateCache / 100.00)) return "ExN2"; //throw new Exceptions.NumberBugIIException();
        if (hasMoreThan4DecimalPlaces(i)){
            return "ExN1";
        }

        return DECIMAL_FORMAT.format(i);
    }

    public static String Ordinary(double a, double b, String i) {
        if (Objects.equals(i, "/")) {
            if (b == 0) return "ExN0";
//            throw new Exceptions.NumberBugException();
            else return Checker(a / b);
        }
        if (Objects.equals(i, "+"))
            return Checker(a + b);
        if (Objects.equals(i, "-"))
            return Checker(a - b);
        if (Objects.equals(i, "*"))
            return Checker(a * b);
        return null;
    }

    private static boolean hasMoreThan4DecimalPlaces(double value) {
        double multiplied = value * 10000;
        long integerPart = (long) multiplied;
        double epsilon = 1e-10;
        double remainder = Math.abs(multiplied - integerPart);
        return remainder > epsilon;
    }


}
