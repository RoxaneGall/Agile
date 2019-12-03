package Donnees;

import com.sothawo.mapjfx.Coordinate;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

import Modeles.*;
import Vue.Controller;
import org.w3c.dom.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class LectureXML {
    //map(idIntersection,Intersection);

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser;

    public LectureXML() throws Exception {
        try{
            parser = factory.newDocumentBuilder();
        } catch(final ParserConfigurationException e){
            throw new Exception(e.getMessage());
        }
    }

    public void chargerPlan(String cheminFichier) throws Exception {
        if(!cheminFichier.substring(cheminFichier.lastIndexOf('.')+1).equals("xml")){
            throw new Exception("Le fichier n'est pas un fichier xml. Veuillez charger un fichier d'extension .xml");
        }

        Document document = null;
        try {
            document = parser.parse(new File(cheminFichier));
        } catch (IOException e) {
            throw new Exception(e.getMessage(),e);
        } catch (SAXException e){
            throw new Exception(e.getMessage(),e);
        }

        final Element root = document.getDocumentElement();

        if(!root.getNodeName().equals("reseau")){
            throw new Exception("Le fichier ne représente pas un plan... Veuillez charger un fichier .xml correspondant à un plan.");
        }

        NodeList rootNodes = root.getChildNodes();
        int nbRootNodes = rootNodes.getLength();

        if(nbRootNodes==0){
            throw new Exception("Le fichier ne contient aucune information.");
        }

        int countInter=0, countTroncon=0;
        for(int i=0; i<nbRootNodes; i++) {
            Node node = rootNodes.item(i);
            NamedNodeMap attributes = node.getAttributes();

            Double lat,lg;
            long idIntersection;
            if (node.getNodeName().equals("noeud")) {
                try {
                    lat = Double.parseDouble(attributes.getNamedItem("latitude").getNodeValue());
                    lg = Double.parseDouble(attributes.getNamedItem("longitude").getNodeValue());
                    idIntersection = Long.parseLong(attributes.getNamedItem("id").getNodeValue());
                }catch (Exception e){
                    throw new Exception("Les attributs du noeud correspondant à l'intersection "+ ++countInter + "sont mals renseignés. " +
                            "Veuillez respecter le format des attributs suivant : \n" +
                                    " latitude et longitude sont des Double et id est un long.",e);
                }

                Coordinate myPoint = new Coordinate(lat, lg);
                Intersection myIntersection = new Intersection(idIntersection, myPoint);
                ++countInter;
                Graphe.shared.addIntersection(myIntersection);
            }

            String nomRue;
            long idOrigine, idDestination;
            Double longueur;
            Intersection destination;
            if(rootNodes.item(i).getNodeName().equals("troncon")){
                if(countInter==0){
                    throw new Exception("Aucune intersection n'est présente dans le plan. Veuillez charger les noeuds d'intersection avant.");
                }
                try {
                    nomRue = attributes.getNamedItem("nomRue").getNodeValue();
                    idOrigine = Long.parseLong(attributes.getNamedItem("origine").getNodeValue());
                    idDestination = Long.parseLong(attributes.getNamedItem("destination").getNodeValue());
                    longueur=Double.parseDouble(attributes.getNamedItem("longueur").getNodeValue());
                    if(longueur<0){
                        throw new Exception("La longueur du tronçon ne peut pas être négative. \n" +
                                " Veuillez rectifier cette longueur erronée.");
                    }
                }catch(Exception e){
                    throw new Exception("Les attributs du noeud correspondant au tronçon "+ countTroncon++ + " sont mals renseignés. " +
                            "Veuillez respecter le format des attributs suivant : \n" +
                            " origine et destination sont des long, nomRue est un String et longueur est un Double.",e);
                }
                destination = Graphe.shared.getIntersectionMap().get(idDestination);
                if (destination == null) {
                    throw new Exception("L'intersection d'origine du " + countTroncon++ + "eme troncon n'existe pas....");
                }
                Troncon myTroncon = new Troncon(destination,nomRue,longueur);
                countTroncon++;
                Graphe.shared.addTroncon(myTroncon,idOrigine);
            }
        }
    }

    /*Charger plan :
- Le fichier chargé n’est pas un .xml
- Le fichier chargé est vide ou ne correspond pas au format requis
- Impossible de lire le fichier (fichier protégé en lecture) ou autre erreur
Faudrait que tu renvoies à l'IHM des trucs différents selon l'erreur ou qu'on affiche un message d'erreur à l'utilisateur*/

    /*Charger demande :
- Le fichier chargé n’est pas un .xml
- Le fichier chargé est vide ou ne correspond pas au format requis
- Un point d’intersection n’est pas compris dans la zone du plan de Lyon et ne peut donc pas être pris en compte
- Impossible de lire le fichier (fichier protégé en lecture) ou autre erreur*/

    public Demande chargerDemande(String cheminFichier) throws Exception {
        System.out.println("Lecture du XML ");
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
        Date myDate = new Date();
        Intersection entrepot = new Intersection();

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
            String message ="";
            if(myDate == null){
                message = "La date de la demande de livraisons n'est pas définie.";
            }
            if (entrepot == null){
                message = "L'entrepot de la demande de livraisons n'est pas défini.";
            }
            throw new Exception(message);
        }

        Demande demande = new Demande(deliveries,entrepot, myDate);
        return demande;
    }

    public ArrayList<Coordinate> getLimitesPlan(){
        ArrayList<Coordinate> myList = new ArrayList<>();

        double minLatitude = 100;
        double minLongitude = 100;
        double maxLatitude = -100;
        double maxLongitude = -100;

       for(HashMap.Entry mapValue : Graphe.shared.getIntersectionMap().entrySet()){
           Intersection myIntersection = (Intersection) mapValue.getValue();
           double latitude = myIntersection.getCoordinate().getLatitude();
           double longitude = myIntersection.getCoordinate().getLongitude();
           if(latitude<minLatitude){ minLatitude = latitude; }
           else if(latitude>maxLatitude){ maxLatitude = latitude;}
           if(longitude<minLongitude){ minLongitude = longitude;}
           else if(longitude>minLongitude){ maxLongitude = longitude; }
       }

       Coordinate minLatminLong = new Coordinate(minLatitude,minLongitude);
       Coordinate minLatmaxLong = new Coordinate(minLatitude,maxLongitude);
       Coordinate maxLatminLong = new Coordinate(maxLatitude,minLongitude);
       Coordinate maxLatmaxLong = new Coordinate(maxLatitude,maxLongitude);

       myList.add(minLatminLong);
       myList.add(minLatmaxLong);
       myList.add(maxLatminLong);
       myList.add(maxLatmaxLong);

       if(myList.isEmpty()){
           System.out.println("test list empty");
           //exception ?
       }
       return myList;
    }
}
