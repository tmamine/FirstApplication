package com.example.firstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.firstapplication.model.RSSReader;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RSSReader rssr = new RSSReader();
        NodeList nodes = null;
        Element element = null;

        nodes = rssr.parse("http://fobec.com/CMS/fobec.xml");

        for (int i = 0; i < nodes.getLength(); i++) {
            element = (Element) nodes.item(i);
            System.out.println("Titre: " + rssr.readNode(element, "title"));
            System.out.println("Lien: " + rssr.readNode(element, "link"));
            System.out.println("Date: " + rssr.GMTDateToFrench(rssr.readNode(element, "pubDate")));
            System.out.println("Description: " + rssr.readNode(element, "description"));
            System.out.println();
        } //for
    }
}
