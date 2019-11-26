package Donnees;

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
                Point myPoint = new Point(lg, lat);
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
        for(int i=0; i<nbRootNodes; i++){
            Element myElement = (Element) rootNodes.item(i);
            List
            if (myElement.getNodeName().equals("entrepot")){
               /* Date today = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("H:m:s");
                System.out.println(formatter.format(aujourdhui));*/

                //Date myDate = SimpleDateFormat.parse(myElement.getAttribute("heureDepart"));
                int idEntrepot = Integer.parseInt(myElement.getAttribute("adresse"));
                Intersection entrepot = Graphe.shared.getIntersectionMap().get(idEntrepot);

                /*

<entrepot adresse="342873658" heureDepart="8:0:0"/>
<livraison adresseEnlevement="208769039" adresseLivraison="25173820" dureeEnlevement="180" dureeLivraison="240"/>

                 */
            }
        }
    }

}
