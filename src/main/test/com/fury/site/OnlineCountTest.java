package com.fury.site;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class OnlineCountTest {
    public static void main(String[] args) {
        try {
            URLConnection url = new URL("http://furyps.com/updatepc.php?p=LCwrRkRxLV(!)_:&c=200").openConnection();
            url.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            url.connect();
            InputStream in = url.getInputStream();
            if(!convertStreamToString(in).equalsIgnoreCase("updated."))
                System.err.println("Error updating player count.");
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
