package Donnees;

import com.sothawo.mapjfx.Coordinate;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

import Modeles.*;
import org.w3c.dom.Document;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
        }

        final Element root = document.getDocumentElement();

        if(!root.getNodeName().equals("reseau")){
            throw new Exception("Le fichier ne représente pas un plan...");
        }

        NodeList rootNodes = root.getChildNodes();
        int nbRootNodes = rootNodes.getLength();

        int countNodes=0;
        for(int i=0; i<nbRootNodes; i++) {
            Element myElement = (Element) rootNodes.item(i);
            Intersection myIntersection;
            if (myElement.getNodeName().equals("noeud")) {
                double lat = Double.parseDouble(myElement.getAttribute("latitude"));
                double lg = Double.parseDouble(myElement.getAttribute("longitude"));
                int idIntersection = Integer.parseInt(myElement.getAttribute("id"));
                Coordinate myPoint = new Coordinate(lg, lat);
                myIntersection = new Intersection(idIntersection, myPoint);
                countNodes++;
                Graphe.shared.addIntersection(myIntersection);
            }

            if(myElement.getNodeName().equals("troncon")){
                if(countNodes==0){
                    throw new Exception("Aucune intersection n'est présente dans le plan. Veuillez charger les noeuds.");
                }
                String nomRue = myElement.getAttribute("nomRue");
                int idOrigine = Integer.parseInt(myElement.getAttribute("origine"));
                Intersection origine = Graphe.shared.getIntersectionMap().get(idOrigine);
                int idDestination = Integer.parseInt(myElement.getAttribute("destination"));
                Intersection destination = Graphe.shared.getIntersectionMap().get(idDestination);
                Double longueur=Double.parseDouble(myElement.getAttribute("longueur"));

                Troncon myTroncon = new Troncon(destination,nomRue,longueur);
                Graphe.shared.addTroncon(myTroncon,origine);
            }
        }
    }

    public void chargerDemande(String cheminFichier) throws Exception {

        Document document = null;

        try {
            document = parser.parse(new File(cheminFichier));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Element root = document.getDocumentElement();

        if(!root.getNodeName().equals("demandeDeLivraisons")){
            throw new Exception("Le fichier ne représente pas une demande de livraison...");
        }

        NodeList rootNodes = root.getChildNodes();
        int nbRootNodes = rootNodes.getLength();

        int countNodes=0;
        Intersection entrepot;
        List<Livraison> deliveries;
        Date myDate = new Date(); //Date du jour
        SimpleDateFormat formatter = new SimpleDateFormat("H:m:s");

        for(int i=0; i<nbRootNodes; i++){
            Element myElement = (Element) rootNodes.item(i);
            if (myElement.getNodeName().equals("entrepot")){
                myDate = formatter.parse(myElement.getAttribute("heureDepart"));
                int idEntrepot = Integer.parseInt(myElement.getAttribute("adresse"));
                entrepot = Graphe.shared.getIntersectionMap().get(idEntrepot);
                setEntrepot(entrepot); // pour l'IHM
            }

            if(myElement.getNodeName().equals("livraison")){
                int idEnlevement = Integer.parseInt(myElement.getAttribute("adresseEnlevement"));
                Intersection enlevement = Graphe.shared.getIntersectionMap().get(idEnlevement);
                int idLivraison = Integer.parseInt(myElement.getAttribute("adresseLivraison"));
                Intersection livraison = Graphe.shared.getIntersectionMap().get(idLivraison);
                int dureeEnlevement = Integer.parseInt(myElement.getAttribute("dureeEnlevement"));
                int dureeLivraison = Integer.parseInt(myElement.getAttribute("dureeLivraison"));
                Livraison myDelivery = new Livraison(enlevement,livraison,dureeEnlevement,dureeLivraison);
                deliveries.add(myDelivery);
            }
            //Demande myDemande = new Demande();
        }
    }

}
