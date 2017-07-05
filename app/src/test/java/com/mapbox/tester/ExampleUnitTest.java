package com.mapbox.tester;


import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);

        String data = "1,1    2 3";
        String[] coordinates = data.split(" +");
        for (String coord : coordinates) {
            System.out.print(coord);
        }
    }
}