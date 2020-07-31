package com.java.ping;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Simple test
     */
    @Test
    public void testPing()
    {
        JavaPing ping=new JavaPing();
        Map<String,String> params=new HashMap<String,String>();
        params.put("hostname","www.google.com");
        params.put("timeout","20");
        ping.execute(params);
    }
}
