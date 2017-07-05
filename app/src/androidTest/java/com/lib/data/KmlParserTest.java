package com.lib.data;


/**
 * Created by zhanghong on 17-5-27.
 */
//@RunWith(AndroidJUnit4.class)
public class KmlParserTest {

//    Locker locker = new Locker();
//
//    @Test
//    public void testKmlParser() {
//        SpKmlParser parser = new SpKmlParser(mKmlSource, this);
//        parser.parser();
//        locker.lock(1000 * 1000);
//        Assert.assertTrue(locker.isUnLock());
//    }
//
//    private void onParserOver(SpKmlParser parser) {
//        locker.unlock();
//    }
//
//
//    private FileSource mKmlSource = new FileSource() {
//        private PipedInputStream is = new PipedInputStream();
//        private PipedOutputStream os = new PipedOutputStream();
//
//        private String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<kml xmlns=\"http://earth.google.com/kml/2.1\">\n" +
//                " <Placemark>   \n" +
//                "<name>Simple placemark</name>   \n" +
//                "<description>Attached to the ground. Intelligently places itself\n" +
//                "       at the height of the underlying terrain.\n" +
//                "</description>   \n" +
//                "<Point>     \n" +
//                "<coordinates>-122.0822035425683,37.42228990140251,0</coordinates>   \n" +
//                "</Point> \n" +
//                "</Placemark>\n" +
//                "</kml>";
//
//        private String data2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//                "<kml xmlns=\"http://earth.google.com/kml/2.1\">\n" +
//                " <Document>\n" +
//                "    <Placemark>\n" +
//                "      <name>CDATA example</name>\n" +
//                "      <description>\n" +
//                "        <![CDATA[\n" +
//                "          <h1>CDATA Tags are useful!</h1>\n" +
//                "          <p><font color=\"red\">Text is <i>more readable</i> and\n" +
//                "          <b>easier to write</b> when you can avoid using entity\n" +
//                "          references.</font></p>\n" +
//                "        ]]>\n" +
//                "      </description>\n" +
//                "      <Point>\n" +
//                "        <coordinates>102.595626,14.996729</coordinates>\n" +
//                "      </Point>\n" +
//                "    </Placemark>\n" +
//                " </Document>\n" +
//                "</kml>";
//
//        @Override
//        public InputStream getInputStream() throws Exception {
//            os.connect(is);
//            os.write(data2.getBytes());
//            os.flush();
//            os.close();
//            return is;
//        }
//
//        @Override
//        public void init() {
//        }
//
//        @Override
//        public void recycle() {
//
//        }
//    };
}