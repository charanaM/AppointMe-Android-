package com.example.acer.appointme;

/**
 * Charana Mayakaduwa
 * 2016139
 * w1626663
 */

import android.util.Xml;
import android.widget.Button;

import org.xmlpull.v1.XmlPullParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Thesaurus implements Runnable {

    //for the word
    private String word;
    //for the word type
    private ArrayList<String> type = new ArrayList<>();
    //for the synonyms
    private ArrayList<String> synonyms = new ArrayList<>();
    //thesaurus key for the web server
    public static final String ThesaurusKey = "r4rWl0un0EThlU3Ee0bq";

    public Thesaurus(String word) {
        this.word = word;
    }


    public void run() {

        findThesaurus();
    }


    private void findThesaurus() {
        String error = null;

        //connect to the web server
        HttpURLConnection httpURLConnection = null;
        try {

            if (Thread.interrupted())
                throw new InterruptedException();



            //get the synonyms from the web server
            URL url = new URL("http://thesaurus.altervista.org/thesaurus/v1?word=" + word +"&language=en_US&key=r4rWl0un0EThlU3Ee0bq&output=xml");

            //open the connection for the server to get the xml
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //wait for 10 seconds to read the downloaded xml file
            httpURLConnection.setReadTimeout(10000 /* milliseconds */);
            //timeout for data connection to the web server
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);

            // Start the query
            httpURLConnection.connect();
            // Check if task has been interrupted
            if (Thread.interrupted())
                throw new InterruptedException();
            // Read results from the query
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(httpURLConnection.getInputStream(), null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {


                if (eventType == XmlPullParser.START_TAG && parser.getName().equalsIgnoreCase("category")){
                    eventType = parser.next();
                    type.add(parser.getText());
                    System.out.println("Category: " + parser.getText());
                }

                if (eventType == XmlPullParser.START_TAG && parser.getName().equalsIgnoreCase("synonyms")){
                    eventType = parser.next();
                    synonyms.add(parser.getText());
                    System.out.println("TEXTTTTTTT: " + parser.getText());
                }

                if (XmlPullParser.TEXT == eventType){

                }

                eventType = parser.next();


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(type);
        System.out.println(synonyms);

    }

    public ArrayList<String> getType() {
        return type;
    }

    public ArrayList<String> getSynonyms() {
        return synonyms;
    }
}
