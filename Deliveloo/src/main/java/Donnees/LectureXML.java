package Donnees;

import com.sothawo.mapjfx.Coordinate;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

import Modeles.*;
import org.w3c.dom.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import static java.lang.Math.*;

/**
 * Classe se chargeant de la lecture des fichiers XML
 *
 * @author H4132
 */
public class LectureXML {
    /**
     * Crée une instance de l'API DocumentBuilderFactory à partir de laquelle on peut avoir un parseur
     * qui va produire des arbres d'objets DOM a partir du fichier XML chargé.
     */
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    /**
     * DocumentBuilder est une API pour avoir un parseur qui produit des
     * arbres d'objets DOM a partir d'un objet XML.
     */
    DocumentBuilder parser;

    public LectureXML() {
        try {
            parser = factory.newDocumentBuilder();
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge le plan de la ville récupéré d'un fichier XML
     *
     * @param cheminFichier est le chemin d'accès sur le disque du fichier XML contenant les données du plan à charger
     * @throws Exception
     */
    public void chargerPlan(String cheminFichier) throws Exception {
        if (!cheminFichier.substring(cheminFichier.lastIndexOf('.') + 1).equals("xml")) {
            throw new Exception("Le fichier n'est pas un fichier xml. Veuillez charger un fichier d'extension .xml");
        }

        Document document = null;
        try {
            document = parser.parse(new File(cheminFichier));
        } catch (IOException e) {
            throw new Exception(e.getMessage(), e);
        } catch (SAXException e) {
            throw new Exception(e.getMessage(), e);
        }

        final Element root = document.getDocumentElement();

        if (!root.getNodeName().equals("reseau")) {
            throw new Exception("Le fichier ne représente pas un plan... Veuillez charger un fichier .xml correspondant à un plan.");
        }

        NodeList rootNodes = root.getChildNodes();
        int nbRootNodes = rootNodes.getLength();

        if (nbRootNodes == 0) throw new Exception("Le fichier ne contient aucune information.");

        int countInter = 0, countTroncon = 0;
        for (int i = 0; i < nbRootNodes; i++) {
            Node node = rootNodes.item(i);
            NamedNodeMap attributes = node.getAttributes();

            Double lat, lg;
            long idIntersection;
            if (node.getNodeName().equals("noeud")) {
                try {
                    lat = Double.parseDouble(attributes.getNamedItem("latitude").getNodeValue());
                    lg = Double.parseDouble(attributes.getNamedItem("longitude").getNodeValue());
                    idIntersection = Long.parseLong(attributes.getNamedItem("id").getNodeValue());
                } catch (Exception e) {
                    throw new Exception("Les attributs du noeud correspondant à l'intersection " + ++countInter + "sont mal renseignés. " +
                            "Veuillez respecter le format des attributs suivant : \n" +
                            " latitude et longitude sont des Double et id est un long.", e);
                }

                Coordinate myPoint = new Coordinate(lat, lg);
                Intersection myIntersection = new Intersection(idIntersection, myPoint);
                ++countInter;
                Graphe.shared.addIntersection(myIntersection);
            }


            String nomRue = null;
            long idOrigine = 0, idDestination = 0;
            Double longueur = null;
            Intersection destination;
            if (rootNodes.item(i).getNodeName().equals("troncon")) {
                if (countInter == 0) {
                    throw new Exception("Aucune intersection n'est présente dans le plan. Veuillez charger les noeuds d'intersection avant.");
                }
                try {
                    nomRue = attributes.getNamedItem("nomRue").getNodeValue();
                    idOrigine = Long.parseLong(attributes.getNamedItem("origine").getNodeValue());
                    idDestination = Long.parseLong(attributes.getNamedItem("destination").getNodeValue());
                    longueur = Double.parseDouble(attributes.getNamedItem("longueur").getNodeValue());
                    if (longueur < 0.0) {
                        System.out.println(longueur);
                        throw new IllegalArgumentException();
                    }
                } catch (NullPointerException npe) {
                    throw new NullPointerException("Informations manquantes au tronçon " + countTroncon++ + "\n");
                } catch (NumberFormatException nfe) {
                    throw new Exception("Les attributs du noeud correspondant au tronçon " + countTroncon++ + " sont mal renseignés. " +
                            "Veuillez respecter le format des attributs suivant : \n" +
                            " origine et destination sont des long, nomRue est un String et longueur est un Double.", nfe);
                } catch (IllegalArgumentException iae) {
                    throw new IllegalArgumentException("La longueur du tronçon ne peut pas être négative. \n" +
                            " Veuillez rectifier cette longueur erronée.");
                } catch (Exception e) {
                    throw new Exception("Problème dans le chargement du plan", e);
                }
                destination = Graphe.shared.getIntersectionMap().get(idDestination);
                if (destination == null) {
                    throw new Exception("L'intersection d'origine du troncon " + countTroncon++ + " n'existe pas dans le graphe du plan.");
                }
                Troncon myTroncon = new Troncon(destination, nomRue, longueur);
                countTroncon++;
                Graphe.shared.addTroncon(myTroncon, idOrigine);
            }
        }
    }

    /**
     * Charge une demande de livraison(s) à partir d'un fichier XML
     *
     * @param cheminFichier est le chemin d'accès sur le disque du fichier XML
     *                      contenant les données de la demande de livraison(s)
     * @return la demande de livraison créée dans notre structure de données
     * à l'aboutissement du chargement de la demande
     * @throws Exception
     */
    public Demande chargerDemande(String cheminFichier) throws Exception {
        if (!cheminFichier.substring(cheminFichier.lastIndexOf('.') + 1).equals("xml")) {
            throw new Exception("Le fichier n'est pas un fichier xml. Veuillez charger un fichier d'extension .xml");
        }

        Document document = null;
        try {
            document = parser.parse(new File(cheminFichier));
        } catch (IOException e) {
            throw new Exception(e.getMessage(), e);
        } catch (SAXException e) {
            throw new Exception(e.getMessage(), e);
        }

        final Element root = document.getDocumentElement();

        if (!root.getNodeName().equals("demandeDeLivraisons")) {
            throw new Exception("Le fichier ne représente pas une demande de livraison...");
        }

        NodeList rootNodes = root.getChildNodes();
        int nbRootNodes = rootNodes.getLength();

        if (nbRootNodes == 0) throw new Exception("Le fichier ne contient aucune information.");

        Date myDate = new Date();
        Intersection entrepot = new Intersection();

        Long countDeliveries = new Long(0);
        String nomDemande = cheminFichier.substring(cheminFichier.replace("\\", "/").lastIndexOf("/") + 1, cheminFichier.lastIndexOf("."));
        Demande demande = new Demande(entrepot, myDate, nomDemande);
        for (int i = 0; i < nbRootNodes; i++) {
            Node node = rootNodes.item(i);
            NamedNodeMap attributes = node.getAttributes();

            long idEnlevement, idLivraison;
            Intersection enlevement, livraison;
            int dureeEnlevement, dureeLivraison;
            long idEntrepot;

            if (node.getNodeName().equals("livraison")) {
                try {
                    idEnlevement = Long.parseLong(attributes.getNamedItem("adresseEnlevement").getNodeValue());
                    idLivraison = Long.parseLong(attributes.getNamedItem("adresseLivraison").getNodeValue());
                    dureeEnlevement = Integer.parseInt(attributes.getNamedItem("dureeEnlevement").getNodeValue());
                    dureeLivraison = Integer.parseInt(attributes.getNamedItem("dureeLivraison").getNodeValue());
                    if (dureeEnlevement < 0 || dureeLivraison < 0) {
                        throw new Exception("Attention les durées ne peuvent pas être négatives.");
                    }
                } catch (Exception e) {
                    throw new Exception("Les attributs du noeud correspondant à une livraison " + ++countDeliveries + "sont mal renseignés. " +
                            "Veuillez respecter le format des attributs suivant : \n" +
                            " adresseEnlevement et adresseLivraison sont des long, et dureeEnlevement et dureeLivraison sont des int.", e);
                }
                enlevement = Graphe.shared.getIntersectionMap().get(idEnlevement);
                if (enlevement == null) {
                    throw new Exception("L'intersection d'enlevement " + countDeliveries++ + " n'existe pas dans le graphe du plan.");
                }
                livraison = Graphe.shared.getIntersectionMap().get(idLivraison);
                if (livraison == null) {
                    throw new Exception("L'intersection de livraison" + countDeliveries++ + " n'existe pas dans le graphe du plan");
                }
                countDeliveries++;
                demande.addLivraison(enlevement, livraison, dureeEnlevement, dureeLivraison);

            } else if (node.getNodeName().equals("entrepot")) {
                try {
                    idEntrepot = Long.parseLong(attributes.getNamedItem("adresse").getNodeValue());
                    SimpleDateFormat formatter = new SimpleDateFormat("H:m:s");
                    myDate = formatter.parse(attributes.getNamedItem("heureDepart").getNodeValue());
                    demande.setHeureDepart(myDate);
                } catch (Exception e) {
                    throw new Exception("Les attributs de l'entrepot sont mal renseignés. \n" +
                            "Veuillez respecter le format des attributs suivant : \n" +
                            "adresse est un long et heureDepart est une String de syntaxe \"H:m:s\"");
                }
                entrepot = Graphe.shared.getIntersectionMap().get(idEntrepot);
                if (entrepot == null || myDate == null) {
                    String message = "";
                    if (myDate == null) {
                        message = "La date de la demande de livraisons n'est pas définie.";
                    }
                    if (entrepot == null) {
                        message = "L'entrepot de la demande de livraisons n'est pas défini.";
                    }
                    throw new Exception(message);
                }
                demande.setEntrepot(entrepot);
            }
        }
        if (countDeliveries == 0) throw new Exception("La demande ne contient aucune livraison.");
        return demande;
    }

    /**
     * Calcule les coordonnées des extrémités du plan
     *
     * @return la liste des coordonnées des quatre extrémités
     * du plan calculées
     * @throws Exception
     */
    public ArrayList<Coordinate> getLimitesPlan() throws Exception {
        ArrayList<Coordinate> myList = new ArrayList<>();

        double minLatitude = 100;
        double minLongitude = 100;
        double maxLatitude = -100;
        double maxLongitude = -100;

        for (HashMap.Entry mapValue : Graphe.shared.getIntersectionMap().entrySet()) {
            Intersection myIntersection = (Intersection) mapValue.getValue();
            double latitude = myIntersection.getCoordinate().getLatitude();
            double longitude = myIntersection.getCoordinate().getLongitude();
            if (latitude < minLatitude) {
                minLatitude = latitude;
            } else if (latitude > maxLatitude) {
                maxLatitude = latitude;
            }
            if (longitude < minLongitude) {
                minLongitude = longitude;
            } else if (longitude > maxLongitude) {
                maxLongitude = longitude;
            }
        }

        Coordinate minLatminLong = new Coordinate(minLatitude, minLongitude);
        Coordinate minLatmaxLong = new Coordinate(minLatitude, maxLongitude);
        Coordinate maxLatminLong = new Coordinate(maxLatitude, minLongitude);
        Coordinate maxLatmaxLong = new Coordinate(maxLatitude, maxLongitude);

        myList.add(minLatminLong);
        myList.add(minLatmaxLong);
        myList.add(maxLatminLong);
        myList.add(maxLatmaxLong);

        if (myList.size() < 4) {
            throw new Exception("La liste des points d'extrémités est vide");
        } else if (myList.size() > 4) {
            throw new Exception("La liste des points d'extrémités contient plus de 4 points");
        }
        return myList;
    }

    /**
     * Calcule l'intersection du graphe la plus proche d'un point donné
     *
     * @param c représente les coordonnées du point
     *          dont on veut l'intersection la plus proche
     * @return l'intersection la plus proche du point donné
     */
    public Intersection getIntersectionPlusProche(Coordinate c) {

        Intersection res = new Intersection();
        Double distanceMin = Double.MAX_VALUE;
        Collection<Intersection> inter = Graphe.shared.getIntersectionMap().values();
        Iterator it = inter.iterator();

        while (it.hasNext()) {
            Intersection i = (Intersection) it.next();

            Double diffLongitude = abs(i.getCoordinate().getLongitude() - c.getLongitude());
            Double difflatitude = abs(i.getCoordinate().getLatitude() - c.getLatitude());

            Double distance = sqrt(pow(diffLongitude, 2) + pow(difflatitude, 2));

            if (distance < distanceMin) {
                distanceMin = distance;
                res = i;
            }
        }
        if (distanceMin > 0.002)
            return null;
        return res;
    }
}
