package com.example.peter.budgetti;

import android.util.Log;

import com.example.peter.budgetti.Classes.Moment;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testMoment(){
        Moment moment = new Moment();
        System.out.println( "Tag: "+String.valueOf(moment.getDate()));
        System.out.println( "Zeit: "+String.valueOf(moment.getTime()));


    }
}