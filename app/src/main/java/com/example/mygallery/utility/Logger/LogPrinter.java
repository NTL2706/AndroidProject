package com.example.mygallery.utility.Logger;

public interface LogPrinter {

    void v(String tag, String msg, Throwable tr);

    void d(String tag, String msg, Throwable tr);

    void i(String tag, String msg, Throwable tr);

    void w(String tag, String msg, Throwable tr);

    void e(String tag, String msg, Throwable tr);

    void wtf(String tag, String msg, Throwable tr);
}
