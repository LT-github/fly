package com.lt.fly.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
    //1、
    public static String getTrace(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }

    //2、
    public static String getExceptionAllinformation(Exception ex){
        String sOut = "";
        StackTraceElement[] trace = ex.getStackTrace();
        for (StackTraceElement s : trace) {
            sOut += "\tat " + s + "\r\n";
        }
        return sOut;
    }

    //3、
    public static String getExceptionAllinformation_01(Exception ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        ex.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        pout.close();
        try {
            out.close();
        } catch (Exception e) {
        }
        return ret;
    }

    //4、
    private static String toString_02(Throwable e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
//
//    public static void main(String[] args) {
//        try {
//            Integer a = null;
//            System.out.println(a.toString());
////        }catch (Exception e){
////            System.out.println("第二种方式 :");
////            System.err.println(getExceptionAllinformation(e));
////            System.out.println("第三种方式 :");
////            System.err.println(getExceptionAllinformation_01(e));
////            System.out.println("第四种方式 :");
////            System.err.println(toString_02(e));
//
//        }catch (Throwable throwable){
//            System.out.println("第一种方式 :");
//            System.err.println(getTrace(throwable));
//
//        }
//
//    }
}
