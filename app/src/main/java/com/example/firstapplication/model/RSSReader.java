package com.example.firstapplication.model;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Parser un flux RSS
 * @author Fobec 2010
 */

public class RSSReader {

    /**
     * Parser le fichier XML
     * @param feedurl URL du flux RSS
     */
    public NodeList parse(String feedurl) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            URL url = new URL(feedurl);
            Document doc = builder.parse(url.openStream());
            NodeList nodes = null;
            Element element = null;
            /**
             * Titre et date du flux
             */
            nodes = doc.getElementsByTagName("title");
            Node node = doc.getDocumentElement();
            System.out.println("Flux RSS: " + this.readNode(node, "channel|title"));
            System.out.println("Date de publication: " + GMTDateToFrench(this.readNode(node, "channel|lastBuildDate")));
            System.out.println();
            /**
             * Elements du flux RSS
             **/
            nodes = doc.getElementsByTagName("item");
            for (int i = 0; i < nodes.getLength(); i++) {
                element = (Element) nodes.item(i);
                System.out.println("Titre: " + readNode(element, "title"));
                System.out.println("Lien: " + readNode(element, "link"));
                System.out.println("Date: " + GMTDateToFrench(readNode(element, "pubDate")));
                System.out.println("Description: " + readNode(element, "description"));
                System.out.println();
            } //for
            //for
            return nodes;

        } catch (SAXException ex) {
            Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Méthode permettant de retourner ce que contient d'un noeud
     * @param _node le noeud principal
     * @param _path suite des noms des noeud sans espace séparer par des "|"
     * @return un string contenant le valeur du noeud voulut
     */
    public String readNode(Node _node, String _path) {

        String[] paths = _path.split("|");
        Node node = null;

        if (paths != null && paths.length > 0) {
            node = _node;

            for (int i = 0; i < paths.length; i++) {
                node = getChildByName(node, paths[i].trim());
            }
        }

        if (node != null) {
            return node.getTextContent();
        } else {
            return "";
        }
    }

    /**
     * renvoye le nom d'un noeud fils a partir de son nom
     * @param _node noeud pricipal
     * @param _name nom du noeud fils
     * @return le noeud fils
     */
    public Node getChildByName(Node _node, String _name) {
        if (_node == null) {
            return null;
        }
        NodeList listChild = _node.getChildNodes();

        if (listChild != null) {
            for (int i = 0; i < listChild.getLength(); i++) {
                Node child = listChild.item(i);
                if (child != null) {
                    if ((child.getNodeName() != null && (_name.equals(child.getNodeName()))) || (child.getLocalName() != null && (_name.equals(child.getLocalName())))) {
                        return child;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Afficher une Date GML au format francais
     * @param gmtDate
     * @return
     */
    public String GMTDateToFrench(String gmtDate) {
        try {
            SimpleDateFormat dfGMT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            dfGMT.parse(gmtDate);
            SimpleDateFormat dfFrench = new SimpleDateFormat("EEEE, d MMMM yyyy HH:mm:ss", Locale.FRANCE);
            return dfFrench.format(dfGMT.getCalendar().getTime());
        } catch (ParseException ex) {
            Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * Exemple
     * @param args
     */
    public static void main(String[] args) {
        RSSReader reader = new RSSReader();
        reader.parse("http://fobec.com/CMS/fobec.xml");
    }
}