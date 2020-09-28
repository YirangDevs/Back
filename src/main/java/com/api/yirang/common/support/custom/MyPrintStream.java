package com.api.yirang.common.support.custom;

import com.api.yirang.common.support.time.MyLocalTime;

import java.io.OutputStream;
import java.io.PrintStream;

public class MyPrintStream extends PrintStream {
    public MyPrintStream(OutputStream out) {
        super(out);
    }
    @Override
    public void println(String string){
        String date = MyLocalTime.makeCurrentTimeString();
        super.println("[" + date + "] " + string);
    }
}
