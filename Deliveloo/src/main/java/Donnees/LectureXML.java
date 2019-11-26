package Donnees;

import com.sothawo.mapjfx.Coordinate;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

import Modeles.*;
import Vue.Elements;
import org.w3c.dom.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xml.sax.SAXException;

public class LectureXML {
    //map(idIntersection,Intersection);

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser;

    public LectureXML(){
        try{
            parser = factory.newDocumentBuilder();
        } catch(final ParserConfigurationException e){
            e.printStackTrace();
        }
    }

/*    public void testest() throws IOException, SAXException {
        final Document document= parser.parse(new File("./datas/petitPlan.xml"));

        // Parcourir et charger le petitPlan
        final Element racine = document.getDocumentElement();
        System.out.println(racine.getNodeName());
    }
*/

    public void chargerPlan(String cheminFichier) throws Exception {

        Document document = null;

        try {
            document = parser.parse(new File(cheminFichier));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        final Element root = document.getDocumentElement();

        if(!root.getNodeName().equals("reseau")){
            throw new Exception("Le fichier ne représente pas un plan...");
        }

        NodeList rootNodes = root.getChildNodes();
        int nbRootNodes = rootNodes.getLength();

        int countNodes=0;
        for(int i=0; i<nbRootNodes; i++) {
            Node node = rootNodes.item(i);
            NamedNodeMap attributes = node.getAttributes();

            if (node.getNodeName().equals("noeud")) {

                double lat = Double.parseDouble(attributes.getNamedItem("latitude").getNodeValue());

                double lg = Double.parseDouble(attributes.getNamedItem("longitude").getNodeValue());

                long idIntersection = Long.parseLong(attributes.getNamedItem("id").getNodeValue());

                Coordinate myPoint = new Coordinate(lg, lat);
                Intersection myIntersection = new Intersection(idIntersection, myPoint);
                countNodes++;
                Graphe.shared.addIntersection(myIntersection);
            }

            if(rootNodes.item(i).getNodeName().equals("troncon")){
                if(countNodes==0){
                    throw new Exception("Aucune intersection n'est présente dans le plan. Veuillez charger les noeuds.");
                }
                String nomRue = attributes.getNamedItem("nomRue").getNodeValue();
                long idOrigine = Long.parseLong(attributes.getNamedItem("origine").getNodeValue());

                long idDestination = Long.parseLong(attributes.getNamedItem("destination").getNodeValue());
                Intersection destination = Graphe.shared.getIntersectionMap().get(idDestination);

                Double longueur=Double.parseDouble(attributes.getNamedItem("longueur").getNodeValue());

                Troncon myTroncon = new Troncon(destination,nomRue,longueur);
                Graphe.shared.addTroncon(myTroncon,idOrigine);
            }
        }
    }

    public Demande chargerDemande(String cheminFichier) throws Exception {

        Document document = null;

        try {
            document = parser.parse(new File(cheminFichier));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        final Element root = document.getDocumentElement();

        if(!root.getNodeName().equals("demandeDeLivraisons")){
            throw new Exception("Le fichier ne représente pas une demande de livraison...");
        }

        NodeList rootNodes = root.getChildNodes();
        int nbRootNodes = rootNodes.getLength();
        ArrayList<Livraison> deliveries= new ArrayList<>();
        Date myDate = null;
        Intersection entrepot = null;

        for(int i=0; i<nbRootNodes; i++){
            Node node = rootNodes.item(i);
            NamedNodeMap attributes = node.getAttributes();

            if(node.getNodeName().equals("livraison")){
                long idEnlevement = Long.parseLong(attributes.getNamedItem("adresseEnlevement").getNodeValue());
                Intersection enlevement = Graphe.shared.getIntersectionMap().get(idEnlevement);
                long idLivraison = Long.parseLong(attributes.getNamedItem("adresseLivraison").getNodeValue());
                Intersection livraison = Graphe.shared.getIntersectionMap().get(idLivraison);
                int dureeEnlevement = Integer.parseInt(attributes.getNamedItem("dureeEnlevement").getNodeValue());
                int dureeLivraison = Integer.parseInt(attributes.getNamedItem("dureeLivraison").getNodeValue());
                Livraison myDelivery = new Livraison(enlevement,livraison,dureeEnlevement,dureeLivraison);
                deliveries.add(myDelivery);
            } else if(node.getNodeName().equals("entrepot")){
                long idEntrepot = Long.parseLong(attributes.getNamedItem("adresse").getNodeValue());
                entrepot = Graphe.shared.getIntersectionMap().get(idEntrepot);

                SimpleDateFormat formatter = new SimpleDateFormat("H:m:s");
                myDate = formatter.parse(attributes.getNamedItem("heureDepart").getNodeValue());
            }
        }
        if (entrepot == null|| myDate==null){
            throw new Exception("L'entrepot n'est pas defini'...");
        }
        Demande demande = new Demande(deliveries,entrepot, myDate);
        return demande;
    }

    public ArrayList<Coordinate> getLimitesPlan(){
        ArrayList<Coordinate> myList = new ArrayList<>();

        return myList;
    }
}
