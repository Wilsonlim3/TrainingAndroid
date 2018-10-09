package com.example.wijen.training;

import org.junit.Before;
import org.junit.Test;
import com.example.wijen.training.*;
//import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {
    int number=0;
    @Before
    public void ExampleNumber(){
        number = 5;
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(number,2+3);
    }
    @Test
    public void testingHelloWorld(){
        String notNull="'test";
        assertEquals("Hello World","Hello World");
        assertNotNull(notNull);
    }
    @Test
    public void testNew(){
        assertEquals("Test",Login.testing);
    }
}