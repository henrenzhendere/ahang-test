package com.hang.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class OrderNumberGenerator {

    private static AtomicLong seq = new AtomicLong(0);
    private static final int MAX_SEQ = 9999;

    public static String generateOrderNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = sdf.format(new Date());
        long sequence = seq.getAndIncrement() % MAX_SEQ;
        String orderNumber = timestamp + String.format("%04d", sequence);
        return orderNumber;
    }
}