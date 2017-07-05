package com.lib.tools;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    //    private final static String REG = "[android\\.*]|[com.android\\.*]|[java\\.*]|[javax\\.*]|[dalvik\\.*]|[junit\\.*]|[org\\.*]";
    private final static String REG = "(java|android|com\\.android|javax|dalvik|junit|org)\\..+";

    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);

//        String[] pkgNames = new String[]{"javax.xml.transform.Source",
//                "android.text.style.AlignmentSpan", "org.json.JSONArray"};
//        Pattern pattern = Pattern.compile(REG);
//        for (String pkgName : pkgNames) {
//            Matcher matcher = pattern.matcher(pkgName);
//            Assert.assertTrue(pkgName, matcher.matches());
//        }
//
//        pkgNames = new String[]{"xjavax.xml.transform.Source",
//                "asndroid.text.style.AlignmentSpan", "orgc.json.JSONArray","com.androis"};
//        for (String pkgName : pkgNames) {
//            Matcher matcher = pattern.matcher(pkgName);
//            Assert.assertTrue(pkgName, !matcher.matches());
//        }


        String data = "ff0000aa";

        long i = Long.parseLong(data,16);
        int k = (int) i;
        System.out.print(i);

        String m = Integer.toHexString(k);
        System.out.print(m);
    }
}