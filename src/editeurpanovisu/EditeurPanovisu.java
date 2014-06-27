/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editeurpanovisu;

import impl.org.controlsfx.i18n.Localization;
import java.awt.Dimension;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.STYLESHEET_MODENA;
import static javafx.application.Application.launch;
import static javafx.application.Application.setUserAgentStylesheet;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.ToolTipManager;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author LANG Laurent
 */
public class EditeurPanovisu extends Application {

    /**
     * Définition de la langue locale par défaut fr_FR
     */
    public static final String[] codesLanguesTraduction = {"fr_FR", "en_EN", "de_DE"};
    public static final String[] languesTraduction = {"Francais", "English", "Deutsch"};
    public static Locale locale = new Locale("fr", "FR");
    private static ResourceBundle rb;
    static private PopUpDialogController popUp;
    static private ImageView imagePanoramique;
    private Image image2;
    static private Label lblLong, lblLat;
    static private VBox panneau2;
    static private HBox coordonnees;
    static private String currentDir;
    static private int numPoints = 0;
    static public Panoramique[] panoramiquesProjet = new Panoramique[100];
    static public int nombrePanoramiques = 0;
    static private int panoActuel = 0;
    static private File fichProjet;
    static private String panoListeVignette;
    static private Pane pano;
    static private VBox paneChoixPanoramique;
    static private VBox outils;
    static private Tab tabInterface;
    static private File file;
    static private Scene scene;
    static private ScrollPane vuePanoramique;
    static private ScrollPane panneauOutils;
    static private double largeurMax;
    static private boolean estCharge = false;
    static private boolean repertSauveChoisi = false;
    static private String panoEntree = "";
    static public String systemeExploitation;
    /**
     * Répertoire de l'application
     */
    static public String repertAppli;
    static private String repertTemp;
    static private String repertPanos;
    /**
     * Répertoire de sauvegarde du projet
     */
    static public String repertoireProjet;
    /**
     * Répertoire du fichier de configuration
     */
    static public File repertConfig;

    final static private ComboBox listeChoixPanoramique = new ComboBox();
    final static private ComboBox listeChoixPanoramiqueEntree = new ComboBox();
    static private Label lblChoixPanoramique;
    static private boolean panoCharge = false;
    static private String panoAffiche = "";
    static private boolean dejaSauve = true;
    static private Stage stPrincipal;
    static private String[] histoFichiers = new String[10];
    static private String txtRepertConfig;
    static private int nombreHistoFichiers = 0;
    static private File fichHistoFichiers;
    private String texteHisto;
    private static String numVersion;

    static private Button valideChargeDerniersFichiers;
    static private GestionnaireInterfaceController gestionnaireInterface = new GestionnaireInterfaceController();

    static private Menu derniersProjets;
    private Menu menuPanoramique;
    private Menu menuTransformation;
    private MenuItem cube2EquiTransformation;
    private MenuItem equi2CubeTransformation;
    private MenuItem configTransformation;
    private Menu menuModeles;
    private MenuItem chargerModele;
    private MenuItem sauverModele;

    private MenuItem aPropos;
    private MenuItem aide;
    private ImageView imgNouveauProjet;
    private ImageView imgSauveProjet;
    private ImageView imgChargeProjet;
    private ImageView imgVisiteGenere;
    private ImageView imgAjouterPano;
    private ImageView imgCube2Equi;
    private ImageView imgEqui2Cube;

    private MenuItem nouveauProjet;
    private MenuItem sauveProjet;
    private MenuItem ajouterPano;

    private MenuItem fermerProjet;

    private MenuItem sauveSousProjet;
    private MenuItem visiteGenere;
    private MenuItem chargeProjet;

    private void genereVisite() throws IOException {
        if (!repertSauveChoisi) {
            repertoireProjet = currentDir;
        }
        if (!dejaSauve) {
            projetSauve();
        }
        if (dejaSauve) {
            deleteDirectory(repertTemp + "/panovisu/images");
            File imagesRepert = new File(repertTemp + "/panovisu/images");
            if (!imagesRepert.exists()) {
                imagesRepert.mkdirs();
            }
            File boutonRepert = new File(repertTemp + "/panovisu/images/navigation");
            if (!boutonRepert.exists()) {
                boutonRepert.mkdirs();
            }
            File boussoleRepert = new File(repertTemp + "/panovisu/images/boussoles");
            if (!boussoleRepert.exists()) {
                boussoleRepert.mkdirs();
            }
            copieDirectory(repertAppli + File.separator + "panovisu/images/boussoles", boussoleRepert.getAbsolutePath());
            File reseauRepert = new File(repertTemp + "/panovisu/images/reseaux");
            if (!reseauRepert.exists()) {
                reseauRepert.mkdirs();
            }
            copieDirectory(repertAppli + File.separator + "panovisu/images/reseaux", reseauRepert.getAbsolutePath());
            File interfaceRepert = new File(repertTemp + "/panovisu/images/interface");
            if (!interfaceRepert.exists()) {
                interfaceRepert.mkdirs();
            }
            copieDirectory(repertAppli + File.separator + "panovisu/images/interface", interfaceRepert.getAbsolutePath());
            File MARepert = new File(repertTemp + "/panovisu/images/MA");
            if (!MARepert.exists()) {
                MARepert.mkdirs();
            }
            File hotspotsRepert = new File(repertTemp + "/panovisu/images/hotspots");
            if (!hotspotsRepert.exists()) {
                hotspotsRepert.mkdirs();
            }
            copieFichierRepertoire(repertAppli + File.separator + "panovisu" + File.separator + "images" + File.separator + "aide_souris.png",
                    repertTemp + "/panovisu/images");
            copieFichierRepertoire(repertAppli + File.separator + "panovisu" + File.separator + "images" + File.separator + "fermer.png",
                    repertTemp + "/panovisu/images");
            copieFichierRepertoire(repertAppli + File.separator + "panovisu" + File.separator + "images" + File.separator + "precedent.png",
                    repertTemp + "/panovisu/images");
            copieFichierRepertoire(repertAppli + File.separator + "panovisu" + File.separator + "images" + File.separator + "suivant.png",
                    repertTemp + "/panovisu/images");
            for (int i = 0; i < gestionnaireInterface.nombreImagesBouton; i++) {
                ReadWriteImage.writePng(gestionnaireInterface.nouveauxBoutons[i],
                        boutonRepert.getAbsolutePath() + File.separator + gestionnaireInterface.nomImagesBoutons[i],
                        false, 0.f);
            }
            ReadWriteImage.writePng(gestionnaireInterface.nouveauxBoutons[gestionnaireInterface.nombreImagesBouton],
                    hotspotsRepert.getAbsolutePath() + File.separator + "hotspot.png",
                    false, 0.f);
            ReadWriteImage.writePng(gestionnaireInterface.nouveauxMasque,
                    MARepert.getAbsolutePath() + File.separator + "MA.png",
                    false, 0.f);

            File xmlRepert = new File(repertTemp + File.separator + "xml");
            if (!xmlRepert.exists()) {
                xmlRepert.mkdirs();
            }
            File cssRepert = new File(repertTemp + File.separator + "css");
            if (!cssRepert.exists()) {
                cssRepert.mkdirs();
            }
            File jsRepert = new File(repertTemp + File.separator + "js");
            if (!jsRepert.exists()) {
                jsRepert.mkdirs();
            }
            String contenuFichier;
            File xmlFile;
            String chargeImages = "";

            for (int i = 0; i < nombrePanoramiques; i++) {
                String fPano = "panos/" + panoramiquesProjet[i].getNomFichier().substring(panoramiquesProjet[i].getNomFichier().lastIndexOf(File.separator) + 1, panoramiquesProjet[i].getNomFichier().length()).split("\\.")[0];
                if (panoramiquesProjet[i].getTypePanoramique().equals(Panoramique.CUBE)) {
                    fPano = fPano.substring(0, fPano.length() - 2);
                    chargeImages += "                images[" + i + "]=\"" + fPano + "_f.jpg\"\n";
                    chargeImages += "                images[" + i + "]=\"" + fPano + "_b.jpg\"\n";
                    chargeImages += "                images[" + i + "]=\"" + fPano + "_u.jpg\"\n";
                    chargeImages += "                images[" + i + "]=\"" + fPano + "_d.jpg\"\n";
                    chargeImages += "                images[" + i + "]=\"" + fPano + "_r.jpg\"\n";
                    chargeImages += "                images[" + i + "]=\"" + fPano + "_l.jpg\"\n";
                } else {
                    chargeImages += "                images[" + i + "]=\"" + fPano + ".jpg\"\n";
                }
                String affInfo = (panoramiquesProjet[i].isAfficheInfo()) ? "oui" : "non";
                String affTitre = (panoramiquesProjet[i].isAfficheTitre()) ? "oui" : "non";
                double regX;
                double zN;
                if (panoramiquesProjet[i].getTypePanoramique().equals(Panoramique.SPHERE)) {
                    regX = Math.round(((panoramiquesProjet[i].getLookAtX() - 180) % 360) * 10) / 10;
                    zN = Math.round(((panoramiquesProjet[i].getZeroNord() - 180) % 360) * 10) / 10;
                } else {
                    regX = Math.round(((panoramiquesProjet[i].getLookAtX() + 90) % 360) * 10) / 10;
                    zN = Math.round(((panoramiquesProjet[i].getZeroNord() + 90) % 360) * 10) / 10;
                }

                contenuFichier = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<!--\n"
                        + "     Visite générée par l'éditeur panoVisu \n"
                        + "\n"
                        + "             Création L.LANG      le monde à 360°  : http://lemondea360.fr\n"
                        + "-->\n"
                        + "\n"
                        + "\n"
                        + "<scene>\n"
                        + "   <pano \n"
                        + "      image=\"" + fPano + "\"\n"
                        + "      titre=\"" + panoramiquesProjet[i].getTitrePanoramique() + "\"\n"
                        + "      titrePolice=\"" + gestionnaireInterface.titrePoliceNom + "\"\n"
                        + "      titreTaillePolice=\"" + Math.round(Double.parseDouble(gestionnaireInterface.titrePoliceTaille)) + "pt\"\n"
                        + "      titreTaille=\"" + Math.round(gestionnaireInterface.titreTaille) + "%\"\n"
                        + "      titreFond=\"" + gestionnaireInterface.couleurFondTitre + "\"\n"
                        + "      titreCouleur=\"" + gestionnaireInterface.couleurTitre + "\"\n"
                        + "      titreOpacite=\"" + gestionnaireInterface.titreOpacite + "\"\n"
                        + "      type=\"" + panoramiquesProjet[i].getTypePanoramique() + "\"\n"
                        + "      multiReso=\"oui\"\n"
                        + "      nombreNiveaux=\"" + panoramiquesProjet[i].getNombreNiveaux() + "\"\n"
                        + "      zeroNord=\"" + zN + "\"\n"
                        + "      regardX=\"" + regX + "\"\n"
                        + "      regardY=\"" + Math.round(panoramiquesProjet[i].getLookAtY() * 10) / 10 + "\"\n"
                        + "      affinfo=\"" + affInfo + "\"\n"
                        + "      afftitre=\"" + affTitre + "\"\n"
                        + "   />\n"
                        + "   <!--Définition de la Barre de navigation-->\n"
                        + "   <boutons \n"
                        + "      styleBoutons=\"navigation\"\n"
                        + "      couleur=\"rgba(255,255,255,0)\"\n"
                        + "      bordure=\"rgba(255,255,255,0)\"\n"
                        + "      deplacements=\"" + gestionnaireInterface.toggleBarreDeplacements + "\" \n"
                        + "      zoom=\"" + gestionnaireInterface.toggleBarreZoom + "\" \n"
                        + "      outils=\"" + gestionnaireInterface.toggleBarreOutils + "\"\n"
                        + "      fs=\"" + gestionnaireInterface.toggleBoutonFS + "\" \n"
                        + "      souris=\"" + gestionnaireInterface.toggleBoutonSouris + "\" \n"
                        + "      rotation=\"" + gestionnaireInterface.toggleBoutonRotation + "\" \n"
                        + "      positionX=\"" + gestionnaireInterface.positionBarre.split(":")[1] + "\"\n"
                        + "      positionY=\"" + gestionnaireInterface.positionBarre.split(":")[0] + "\" \n"
                        + "      dX=\"" + gestionnaireInterface.dXBarre + "\" \n"
                        + "      dY=\"" + gestionnaireInterface.dYBarre + "\"\n"
                        + "      visible=\"" + gestionnaireInterface.toggleBarreVisibilite + "\"\n"
                        + "   />\n";
                if (gestionnaireInterface.bAfficheBoussole) {
                    String SAiguille = (gestionnaireInterface.bAiguilleMobileBoussole) ? "oui" : "non";
                    contenuFichier += "<!--  Boussole -->\n"
                            + "    <boussole \n"
                            + "        affiche=\"oui\"\n"
                            + "        image=\"" + gestionnaireInterface.imageBoussole + "\"\n"
                            + "        taille=\"" + gestionnaireInterface.tailleBoussole + "\"\n"
                            + "        positionY=\"" + gestionnaireInterface.positionBoussole.split(":")[0] + "\"\n"
                            + "        positionX=\"" + gestionnaireInterface.positionBoussole.split(":")[1] + "\"\n"
                            + "        opacite=\"" + gestionnaireInterface.opaciteBoussole + "\"\n"
                            + "        dX=\"" + gestionnaireInterface.dXBoussole + "\"\n"
                            + "        dY=\"" + gestionnaireInterface.dYBoussole + "\"\n"
                            + "        aiguille=\"" + SAiguille + "\"\n"
                            + "    />\n";
                }
                if (gestionnaireInterface.bSuivantPrecedent) {

                    int panoPrecedent = (i == nombrePanoramiques - 1) ? 0 : i + 1;
                    int panoSuivant = (i == 0) ? nombrePanoramiques - 1 : i - 1;
                    String nomPano = panoramiquesProjet[panoSuivant].getNomFichier();
                    String strPanoSuivant = "xml/" + nomPano.substring(nomPano.lastIndexOf(File.separator) + 1, nomPano.lastIndexOf(".")) + ".xml";
                    nomPano = panoramiquesProjet[panoPrecedent].getNomFichier();
                    String strPanoPrecedent = "xml/" + nomPano.substring(nomPano.lastIndexOf(File.separator) + 1, nomPano.lastIndexOf(".")) + ".xml";
                    System.out.println(strPanoSuivant + " : " + strPanoPrecedent);
                    contenuFichier += "<!--  Bouton Suivant Precedent -->\n"
                            + "    <suivantPrecedent\n"
                            + "        suivant=\"" + strPanoSuivant + "\"            \n"
                            + "        precedent=\"" + strPanoPrecedent + "\"            \n"
                            + "    />    \n"
                            + "";
                }
                if (gestionnaireInterface.bAfficheMasque) {
                    String SNavigation = (gestionnaireInterface.bMasqueNavigation) ? "oui" : "non";
                    String SBoussole = (gestionnaireInterface.bMasqueBoussole) ? "oui" : "non";
                    String STitre = (gestionnaireInterface.bMasqueTitre) ? "oui" : "non";
                    String SPlan = (gestionnaireInterface.bMasquePlan) ? "oui" : "non";
                    String SReseaux = (gestionnaireInterface.bMasqueReseaux) ? "oui" : "non";
                    String SVignettes = (gestionnaireInterface.bMasqueVignettes) ? "oui" : "non";
                    contenuFichier += "<!--  Bouton de Masquage -->\n"
                            + "    <marcheArret \n"
                            + "        affiche=\"oui\"\n"
                            + "        image=\"MA.png\"\n"
                            + "        taille=\"" + gestionnaireInterface.tailleMasque + "\"\n"
                            + "        positionY=\"" + gestionnaireInterface.positionMasque.split(":")[0] + "\"\n"
                            + "        positionX=\"" + gestionnaireInterface.positionMasque.split(":")[1] + "\"\n"
                            + "        opacite=\"" + gestionnaireInterface.opaciteMasque + "\"\n"
                            + "        dX=\"" + gestionnaireInterface.dXMasque + "\"\n"
                            + "        dy=\"" + gestionnaireInterface.dYMasque + "\"\n"
                            + "        navigation=\"" + SNavigation + "\"\n"
                            + "        boussole=\"" + SBoussole + "\"\n"
                            + "        titre=\"" + STitre + "\"\n"
                            + "        plan=\"" + SPlan + "\"\n"
                            + "        reseaux=\"" + SReseaux + "\"\n"
                            + "        vignettes=\"" + SVignettes + "\"\n"
                            + "    />\n";
                }

                if (gestionnaireInterface.bAfficheReseauxSociaux) {
                    String STwitter = (gestionnaireInterface.bReseauxSociauxTwitter) ? "oui" : "non";
                    String SGoogle = (gestionnaireInterface.bReseauxSociauxGoogle) ? "oui" : "non";
                    String SFacebook = (gestionnaireInterface.bReseauxSociauxFacebook) ? "oui" : "non";
                    String SEmail = (gestionnaireInterface.bReseauxSociauxEmail) ? "oui" : "non";
                    contenuFichier += "<!--  Bouton de Masquage -->\n"
                            + "    <reseauxSociaux \n"
                            + "        affiche=\"oui\"\n"
                            + "        taille=\"" + gestionnaireInterface.tailleReseauxSociaux + "\"\n"
                            + "        positionY=\"" + gestionnaireInterface.positionReseauxSociaux.split(":")[0] + "\"\n"
                            + "        positionX=\"" + gestionnaireInterface.positionReseauxSociaux.split(":")[1] + "\"\n"
                            + "        opacite=\"" + gestionnaireInterface.opaciteReseauxSociaux + "\"\n"
                            + "        dX=\"" + gestionnaireInterface.dXReseauxSociaux + "\"\n"
                            + "        dy=\"" + gestionnaireInterface.dYReseauxSociaux + "\"\n"
                            + "        twitter=\"" + STwitter + "\"\n"
                            + "        google=\"" + SGoogle + "\"\n"
                            + "        facebook=\"" + SFacebook + "\"\n"
                            + "        email=\"" + SEmail + "\"\n"
                            + "    />\n";
                }
                if (gestionnaireInterface.bAfficheVignettes) {
                    String SAfficheVignettes = (gestionnaireInterface.bAfficheVignettes) ? "oui" : "non";
                    contenuFichier += "<!-- Barre des vignettes -->"
                            + "    <vignettes \n"
                            + "        affiche=\"" + SAfficheVignettes + "\"\n"
                            + "        tailleImage=\"" + gestionnaireInterface.tailleImageVignettes + "\"\n"
                            + "        fondCouleur=\"" + gestionnaireInterface.couleurFondVignettes + "\"\n"
                            + "        opacite=\"" + gestionnaireInterface.opaciteVignettes + "\"\n"
                            + "        position=\"" + gestionnaireInterface.positionVignettes + "\"\n"
                            + "    >\n";
                    for (int j = 0; j < nombrePanoramiques; j++) {
                        String nomPano = panoramiquesProjet[j].getNomFichier();
                        String nFichier = nomPano.substring(nomPano.lastIndexOf(File.separator) + 1, nomPano.lastIndexOf(".")) + "Vignette.jpg";
                        String nXML = nomPano.substring(nomPano.lastIndexOf(File.separator) + 1, nomPano.lastIndexOf(".")) + ".xml";
                        ReadWriteImage.writeJpeg(panoramiquesProjet[j].getVignettePanoramique(),
                                repertTemp + "/panos/" + nFichier, 1.0f, false, 0.0f);
                        //System.out.println(nFichier + " : " + nXML);
                        contenuFichier
                                += "        <imageVignette \n"
                                + "            image=\"panos/" + nFichier + "\"\n"
                                + "            xml=\"xml/" + nXML + "\"\n"
                                + "        />\n";
                    }
                    contenuFichier
                            += "    </vignettes>       \n"
                            + "";

                }

                contenuFichier += "    <!--Définition des hotspots-->  \n"
                        + "   <hotspots>\n";
                for (int j = 0; j < panoramiquesProjet[i].getNombreHotspots(); j++) {
                    HotSpot HS = panoramiquesProjet[i].getHotspot(j);
                    double longit;
                    if (panoramiquesProjet[i].getTypePanoramique().equals(Panoramique.SPHERE)) {
                        longit = HS.getLongitude() - 180;
                    } else {
                        longit = HS.getLongitude() + 90;
                    }
                    String txtAnime = (HS.isAnime()) ? "true" : "false";
                    contenuFichier
                            += "      <point \n"
                            + "           long=\"" + longit + "\"\n"
                            + "           lat=\"" + HS.getLatitude() + "\"\n"
                            + "           image=\"panovisu/images/hotspots/hotspot.png\"\n"
                            + "           xml=\"xml/" + HS.getFichierXML() + "\"\n"
                            + "           info=\"" + HS.getInfo() + "\"\n"
                            + "           anime=\"" + txtAnime + "\"\n"
                            + "      />\n";
                }
                contenuFichier += "   </hotspots>\n"
                        + "</scene>\n";
                String fichierPano = panoramiquesProjet[i].getNomFichier();
                String nomXMLFile = fichierPano.substring(fichierPano.lastIndexOf(File.separator) + 1, fichierPano.length()).split("\\.")[0] + ".xml";
                xmlFile = new File(xmlRepert + File.separator + nomXMLFile);
                xmlFile.setWritable(true);
                FileWriter fw = new FileWriter(xmlFile);
                try (BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write(contenuFichier);
                }
            }
            Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            int hauteur = (int) tailleEcran.getHeight() - 200;
            String fichierHTML = "<!DOCTYPE html>\n"
                    + "<html lang=\"fr\">\n"
                    + "    <head>\n"
                    + "        <title>Panovisu - visualiseur 100% html5 (three.js)</title>\n"
                    + "        <meta charset=\"utf-8\">\n"
                    + "        <meta name=\"viewport\" content=\"width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0\">\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "        <header>\n"
                    + "\n"
                    + "        </header>\n"
                    + "        <article style=\"height : " + hauteur + "px;\">\n"
                    + "            <div id=\"pano\">\n"
                    + "            </div>\n"
                    + "        </article>\n"
                    + "        <script src=\"panovisu/panovisuInit.js\"></script>\n"
                    + "        <script src=\"panovisu/panovisu.js\"></script>\n"
                    + "        <script>\n"
                    + "\n"
                    + "            $(function() {\n"
                    + "                $(window).resize(function(){\n"
                    + "                    $(\"#article\").height($(window).height()-10);\n"
                    + "                    $(\"#pano\").height($(window).height()-10);\n"
                    + "                })\n"
                    + "                $(\"article\").height($(window).height()-10);\n"
                    + "                $(\"#pano\").height($(window).height()-10);\n"
                    + "                ajoutePano({\n"
                    + "                    panoramique: \"pano\",\n"
                    + "                    fenX: \"100%\",\n"
                    + "                    fenY: \"100%\",\n"
                    + "                    xml: \"xml/PANO.xml\"\n"
                    + "                });\n"
                    + "                $(\".reseauSocial-twitter\").on(\"click\", function() {\n"
                    + "                    window.open(\n"
                    + "                            \"https://twitter.com/share?url=\" + document.location.href\n"
                    + "                            );\n"
                    + "                    return false;\n"
                    + "                });\n"
                    + "                $(\".reseauSocial-fb\").on(\"click\", function() {\n"
                    + "                    window.open(\n"
                    + "                            \"http://www.facebook.com/share.php?u=\" + document.location.href\n"
                    + "                            );\n"
                    + "                    return false;\n"
                    + "                });\n"
                    + "                $(\".reseauSocial-google\").on(\"click\", function() {\n"
                    + "                    window.open(\n"
                    + "                            \"https://plus.google.com/share?url=\" + document.location.href + \"&amp;hl=fr\"\n"
                    + "                            );\n"
                    + "                    return false;\n"
                    + "                });\n"
                    + "                $(\".reseauSocial-email\").attr(\"href\",\"mailto:?body=\" + document.location.href + \"&amp;hl=fr\");\n"
                    + "                images=new Array();\n"
                    + chargeImages
                    + "                prechargeImages(images); \n \n"
                    + "            });\n"
                    + "        </script>\n"
                    + "    </body>\n"
                    + "</html>\n";
            if (panoEntree.equals("")) {
                fichierHTML = fichierHTML.replace("PANO",
                        panoramiquesProjet[0].getNomFichier().substring(panoramiquesProjet[0].getNomFichier().lastIndexOf(File.separator) + 1, panoramiquesProjet[0].getNomFichier().length()).split("\\.")[0]);
            } else {
                fichierHTML = fichierHTML.replace("PANO", panoEntree);
            }
            File fichIndexHTML = new File(repertTemp + File.separator + "index.html");
            fichIndexHTML.setWritable(true);
            FileWriter fw1 = new FileWriter(fichIndexHTML);
            try (BufferedWriter bw1 = new BufferedWriter(fw1)) {
                bw1.write(fichierHTML);
            }

            File repertVisite = new File(repertoireProjet + File.separator + "visite");
            if (!repertVisite.exists()) {
                repertVisite.mkdirs();
            }

            String nomRepertVisite = repertVisite.getAbsolutePath();
            copieDirectory(repertTemp, nomRepertVisite);
            Dialogs.create().title("Editeur PanoVisu")
                    .masthead("Génération de la visite")
                    .message("Votre visite a bien été généré dans le répertoire : " + nomRepertVisite)
                    .showInformation();
        } else {
            Dialogs.create().title("Editeur PanoVisu")
                    .masthead("Génération de la visite")
                    .message("Votre visite n'a pu être générée, votre fichier n'étant pas sauvegardé")
                    .showError();

        }
    }

    private int creeNiveauxImageEqui(String fichierImage, String repertoire) {

        double tailleNiv0 = 2048.d;
        Image img = new Image("file:" + fichierImage);
        if (img.getWidth() > 10000) {
            img = new Image("file:" + fichierImage, 10000, 5000, true, true);
        }
        String nomPano = fichierImage.substring(fichierImage.lastIndexOf(File.separator) + 1, fichierImage.length());
        String ficImage = repertoire + File.separator + nomPano;
        System.out.println("image : " + ficImage);
        try {
            ReadWriteImage.writeJpeg(img, ficImage, 0.9f, true, 0.2f);
        } catch (IOException ex) {
            Logger.getLogger(EditeurPanovisu.class.getName()).log(Level.SEVERE, null, ex);
        }
        double tailleImage = img.getWidth();
        int nombreNiveaux = (int) (Math.log(tailleImage / tailleNiv0) / Math.log(2.d)) + 1;
        System.out.println("Taille de l'image : " + tailleImage + " Nombre de niveaux" + nombreNiveaux);
        for (int i = 0; i < nombreNiveaux; i++) {
            try {
                double tailleNiveau = tailleImage * Math.pow(2.d, i) / Math.pow(2.d, nombreNiveaux);
                img = new Image("file:" + fichierImage, Math.round(tailleNiveau * 2.d) / 2.d, Math.round(tailleNiveau / 2.d), true, true);
                String repNiveau = repertoire + File.separator + "niveau" + i;
                File fRepert = new File(repNiveau);
                if (!fRepert.exists()) {
                    fRepert.mkdirs();
                }
                nomPano = fichierImage.substring(fichierImage.lastIndexOf(File.separator) + 1, fichierImage.length());
                ficImage = repNiveau + File.separator + nomPano;
                System.out.println("fichier " + ficImage);
                ReadWriteImage.writeJpeg(img, ficImage, 0.9f, true, 0.2f);
            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return nombreNiveaux;
    }

    private int creeNiveauxImageCube(String fichierImage, String repertoire) {
        double tailleNiv0 = 512.d;
        Image img = new Image("file:" + fichierImage);
        if (img.getWidth() > 3000) {
            img = new Image("file:" + fichierImage, 3000, 3000, true, true);
        }
        String nomPano = fichierImage.substring(fichierImage.lastIndexOf(File.separator) + 1, fichierImage.length());
        String ficImage = repertoire + File.separator + nomPano;
        System.out.println("image : " + ficImage);
        try {
            ReadWriteImage.writeJpeg(img, ficImage, 0.9f, true, 0.2f);
        } catch (IOException ex) {
            Logger.getLogger(EditeurPanovisu.class.getName()).log(Level.SEVERE, null, ex);
        }
        double tailleImage = img.getWidth();
        int nombreNiveaux = (int) (Math.log(tailleImage / tailleNiv0) / Math.log(2.d)) + 1;
        System.out.println("Taille de l'image : " + tailleImage + " Nombre de niveaux" + nombreNiveaux);
        for (int i = 0; i < nombreNiveaux; i++) {
            try {
                double tailleNiveau = tailleImage * Math.pow(2.d, i) / Math.pow(2.d, nombreNiveaux);
                img = new Image("file:" + fichierImage, tailleNiveau, tailleNiveau, true, true);
                String repNiveau = repertoire + File.separator + "niveau" + i;
                File fRepert = new File(repNiveau);
                if (!fRepert.exists()) {
                    fRepert.mkdirs();
                }
                nomPano = fichierImage.substring(fichierImage.lastIndexOf(File.separator) + 1, fichierImage.length());
                ficImage = repNiveau + File.separator + nomPano;
                System.out.println("fichier " + ficImage);
                ReadWriteImage.writeJpeg(img, ficImage, 0.9f, true, 0.2f);
            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return nombreNiveaux;
    }

    /**
     *
     * @param event
     */
    private void panoramiquesAjouter() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJpeg = new FileChooser.ExtensionFilter("Fichiers JPEG (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterBmp = new FileChooser.ExtensionFilter("Fichiers BMP (*.bmp)", "*.bmp");
        File repert = new File(currentDir + File.separator);
        fileChooser.setInitialDirectory(repert);
        fileChooser.getExtensionFilters().addAll(extFilterJpeg, extFilterBmp);

        List<File> list = fileChooser.showOpenMultipleDialog(null);
        if (list != null) {
            int i = 0;
            File[] lstFich1 = new File[list.size()];
            String[] typeFich1 = new String[list.size()];
            for (File file : list) {
                Image img = new Image("file:" + file.getAbsolutePath());
                if (img.getWidth() == 2 * img.getHeight()) {
                    lstFich1[i] = file;
                    typeFich1[i] = Panoramique.SPHERE;
                    i++;
                }
                if (img.getWidth() == img.getHeight()) {
                    String nom = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 6);
                    boolean trouve = false;
                    for (int j = 0; j < i; j++) {
                        String nom1 = lstFich1[j].getAbsolutePath().substring(0, file.getAbsolutePath().length() - 6);
                        if (nom.equals(nom1)) {
                            trouve = true;
                        }
                    }
                    if (!trouve) {
                        lstFich1[i] = file;
                        typeFich1[i] = Panoramique.CUBE;
                        i++;
                    }
                }

            }
            File[] lstFich = new File[i];
            System.arraycopy(lstFich1, 0, lstFich, 0, i);

            for (int ii = 0; ii < lstFich.length; ii++) {
                File file1 = lstFich[ii];
                dejaSauve = false;
                stPrincipal.setTitle(stPrincipal.getTitle().replace(" *", "") + " *");
                sauveProjet.setDisable(false);
                currentDir = file1.getParent();
                File imageRepert = new File(repertTemp + File.separator + "panos");

                if (!imageRepert.exists()) {

                    imageRepert.mkdirs();
                }
                repertPanos = imageRepert.getAbsolutePath();
//                try {
//                    if (typeFich1[ii].equals(Panoramique.SPHERE)) {
//                        copieFichierRepertoire(file1.getPath(), repertPanos);
//                    } else {
//                        String nom = file1.getAbsolutePath().substring(0, file1.getAbsolutePath().length() - 6);
//                        copieFichierRepertoire(nom + "_u.jpg", repertPanos);
//                        copieFichierRepertoire(nom + "_d.jpg", repertPanos);
//                        copieFichierRepertoire(nom + "_f.jpg", repertPanos);
//                        copieFichierRepertoire(nom + "_b.jpg", repertPanos);
//                        copieFichierRepertoire(nom + "_r.jpg", repertPanos);
//                        copieFichierRepertoire(nom + "_l.jpg", repertPanos);
//                    }
//                } catch (IOException ex) {
//                    Logger.getLogger(EditeurPanovisu.class.getName()).log(Level.SEVERE, null, ex);
//                }
                ajoutePanoramiqueProjet(file1.getPath(), typeFich1[ii]);
            }

            installeEvenements();

            imgVisiteGenere.setOpacity(
                    1.0);
            imgVisiteGenere.setDisable(
                    false);

        }

    }

    /**
     *
     */
    private void clickBtnValidePano() {
        TextArea txtTitrePano = (TextArea) scene.lookup("#txttitrepano");
        CheckBox chkAfficheInfo = (CheckBox) scene.lookup("#chkafficheinfo");
        CheckBox chkAfficheTitre = (CheckBox) scene.lookup("#chkaffichetitre");
        RadioButton radSphere = (RadioButton) scene.lookup("#radsphere");
        panoramiquesProjet[panoActuel].setTitrePanoramique(txtTitrePano.getText());
        panoramiquesProjet[panoActuel].setAfficheInfo(chkAfficheInfo.isSelected());
        panoramiquesProjet[panoActuel].setAfficheTitre(chkAfficheTitre.isSelected());
        if (radSphere.isSelected()) {
            panoramiquesProjet[panoActuel].setTypePanoramique(Panoramique.SPHERE);

        } else {
            panoramiquesProjet[panoActuel].setTypePanoramique(Panoramique.CUBE);
        }
    }

    /**
     *
     */
    private void projetCharge() throws IOException {
        if (!repertSauveChoisi) {
            repertoireProjet = currentDir;
        }
        Action reponse = null;
        Localization.setLocale(locale);
        if (!dejaSauve) {
            reponse = Dialogs.create()
                    .owner(null)
                    .title("Charge un Projet")
                    .masthead("vous n'avez pas sauvegardé votre projet")
                    .message("Voulez vous le sauver ?")
                    .showConfirm();

        }
        if (reponse == Dialog.Actions.YES) {
            try {
                projetSauve();

            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        if ((reponse == Dialog.Actions.YES) || (reponse == Dialog.Actions.NO) || (reponse == null)) {
            dejaSauve = true;
            FileChooser repertChoix = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("fichier panoVisu (*.pvu)", "*.pvu");
            repertChoix.getExtensionFilters().add(extFilter);
            File repert = new File(repertoireProjet + File.separator);
            repertChoix.setInitialDirectory(repert);
            fichProjet = null;
            fichProjet = repertChoix.showOpenDialog(stPrincipal);
            stPrincipal.setTitle("Panovisu v" + numVersion + " : " + fichProjet.getAbsolutePath());
            if (fichProjet != null) {
                repertoireProjet = fichProjet.getParent();
                ajouteFichierHisto(fichProjet.getAbsolutePath());
                repertSauveChoisi = true;
                deleteDirectory(repertTemp);
                String repertPanovisu = repertTemp + File.separator + "panovisu";
                File rptPanovisu = new File(repertPanovisu);
                rptPanovisu.mkdirs();
                copieDirectory(repertAppli + File.separator + "panovisu", repertPanovisu);
                menuPanoramique.setDisable(false);
                imgAjouterPano.setDisable(false);
                imgAjouterPano.setOpacity(1.0);
                imgSauveProjet.setDisable(false);
                imgSauveProjet.setOpacity(1.0);
                imgVisiteGenere.setDisable(false);
                imgVisiteGenere.setOpacity(1.0);

                paneChoixPanoramique.setVisible(false);

                sauveProjet.setDisable(false);
                sauveSousProjet.setDisable(false);
                visiteGenere.setDisable(false);
                numPoints = 0;
                imagePanoramique.setImage(null);
                listeChoixPanoramique.getItems().clear();
                listeChoixPanoramiqueEntree.getItems().clear();
                FileReader fr;
                try {
                    fr = new FileReader(fichProjet);
                    String texte;
                    try (BufferedReader br = new BufferedReader(fr)) {
                        texte = "";
                        String ligneTexte;
                        while ((ligneTexte = br.readLine()) != null) {
                            texte += ligneTexte + "\n";
                        }
                    }
                    analyseLigne(texte);

                    panoActuel = 0;
                    affichePanoChoisit(panoActuel);
                    panoCharge = true;
                    paneChoixPanoramique.setVisible(true);
                    listeChoixPanoramique.setValue(listeChoixPanoramique.getItems().get(0));

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(EditeurPanovisu.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    private void ajouteFichierHisto(String nomFich) {
        if (nombreHistoFichiers > 10) {
            nombreHistoFichiers = 10;
        }
        int trouve = -1;
        for (int i = 0; i < nombreHistoFichiers; i++) {
            if (histoFichiers[i].equals(nomFich)) {
                trouve = i;
            }
        }
        if (trouve == -1) {
            for (int i = nombreHistoFichiers; i >= 0; i--) {
                if (i < 9) {
                    histoFichiers[i + 1] = histoFichiers[i];
                }
            }
            histoFichiers[0] = nomFich;
            if (nombreHistoFichiers < 10) {
                nombreHistoFichiers++;
            }
        } else {
            for (int i = trouve - 1; i >= 0; i--) {
                if (i < 9) {
                    histoFichiers[i + 1] = histoFichiers[i];
                }
            }
            histoFichiers[0] = nomFich;

        }
        derniersProjets.getItems().clear();
        for (int i = 0; i < nombreHistoFichiers; i++) {
            //System.out.println("histo fichier (" + i + ") = " + histoFichiers[i]);
            if (histoFichiers[i] != null) {
                MenuItem menuDerniersFichiers = new MenuItem(histoFichiers[i]);
                derniersProjets.getItems().add(menuDerniersFichiers);
                menuDerniersFichiers.setOnAction((ActionEvent e) -> {
                    MenuItem mnu = (MenuItem) e.getSource();

                    try {
                        projetChargeNom(mnu.getText());
                    } catch (IOException ex) {
                        Logger.getLogger(EditeurPanovisu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        }

    }

    /**
     *
     */
    private void projetChargeNom(String nomFich) throws IOException {
        File fichProjet1 = new File(nomFich);
        if (fichProjet1.exists()) {
            Action reponse = null;
            Localization.setLocale(locale);
            if (!dejaSauve) {
                reponse = Dialogs.create()
                        .owner(null)
                        .title("Charge un Projet")
                        .masthead("vous n'avez pas sauvegardé votre projet")
                        .message("Voulez vous le sauver ?")
                        .showConfirm();

            }
            if (reponse == Dialog.Actions.YES) {
                try {
                    projetSauve();

                } catch (IOException ex) {
                    Logger.getLogger(EditeurPanovisu.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if ((reponse == Dialog.Actions.YES) || (reponse == Dialog.Actions.NO) || (reponse == null)) {
                dejaSauve = true;
                fichProjet = fichProjet1;
                ajouteFichierHisto(fichProjet.getAbsolutePath());
                stPrincipal.setTitle("Panovisu  v" + numVersion + " : " + fichProjet.getAbsolutePath());
                repertoireProjet = fichProjet.getParent();
                repertSauveChoisi = true;
                deleteDirectory(repertTemp);
                String repertPanovisu = repertTemp + File.separator + "panovisu";
                File rptPanovisu = new File(repertPanovisu);
                rptPanovisu.mkdirs();
                copieDirectory(repertAppli + File.separator + "panovisu", repertPanovisu);
                menuPanoramique.setDisable(false);
                imgAjouterPano.setDisable(false);
                imgAjouterPano.setOpacity(1.0);
                imgSauveProjet.setDisable(false);
                imgSauveProjet.setOpacity(1.0);
                imgVisiteGenere.setDisable(false);
                imgVisiteGenere.setOpacity(1.0);

                paneChoixPanoramique.setVisible(false);

                sauveProjet.setDisable(false);
                sauveSousProjet.setDisable(false);
                visiteGenere.setDisable(false);
                numPoints = 0;
                imagePanoramique.setImage(null);
                listeChoixPanoramique.getItems().clear();
                listeChoixPanoramiqueEntree.getItems().clear();
                FileReader fr;
                try {
                    fr = new FileReader(fichProjet);
                    String texte;
                    try (BufferedReader br = new BufferedReader(fr)) {
                        texte = "";
                        String ligneTexte;
                        while ((ligneTexte = br.readLine()) != null) {
                            texte += ligneTexte + "\n";
                        }
                    }
                    analyseLigne(texte);

                    panoActuel = 0;
                    affichePanoChoisit(panoActuel);
                    panoCharge = true;
                    paneChoixPanoramique.setVisible(true);
                    listeChoixPanoramique.setValue(listeChoixPanoramique.getItems().get(0));

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(EditeurPanovisu.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void sauveFichierProjet() throws IOException {
        repertoireProjet = fichProjet.getParent();
        repertSauveChoisi = true;
        File fichierProjet = new File(fichProjet.getAbsolutePath());
        if (!fichierProjet.exists()) {
            fichierProjet.createNewFile();
        }
        dejaSauve = true;
        stPrincipal.setTitle("Panovisu v" + numVersion + " : " + fichProjet.getAbsolutePath());

        String contenuFichier = "";
        if (!panoEntree.equals("")) {
            contenuFichier += "[PanoEntree=>" + panoEntree + "]\n";
        }
        for (int i = 0; i < nombrePanoramiques; i++) {
            contenuFichier += "[Panoramique=>"
                    + "fichier:" + panoramiquesProjet[i].getNomFichier()
                    + ";nb:" + panoramiquesProjet[i].getNombreHotspots()
                    + ";titre:" + panoramiquesProjet[i].getTitrePanoramique() + ""
                    + ";type:" + panoramiquesProjet[i].getTypePanoramique()
                    + ";afficheInfo:" + panoramiquesProjet[i].isAfficheInfo()
                    + ";afficheTitre:" + panoramiquesProjet[i].isAfficheTitre()
                    + ";regardX:" + panoramiquesProjet[i].getLookAtX()
                    + ";regardY:" + panoramiquesProjet[i].getLookAtY()
                    + ";zeroNord:" + panoramiquesProjet[i].getZeroNord()
                    + "]\n";
            for (int j = 0; j < panoramiquesProjet[i].getNombreHotspots(); j++) {
                HotSpot HS = panoramiquesProjet[i].getHotspot(j);
                contenuFichier += "   [hotspot==>"
                        + "longitude:" + HS.getLongitude()
                        + ";latitude:" + HS.getLatitude()
                        + ";image:" + HS.getFichierImage()
                        + ";xml:" + HS.getFichierXML()
                        + ";info:" + HS.getInfo()
                        + ";anime:" + HS.isAnime()
                        + "]\n";
            }
        }
        contenuFichier += "[Interface=>\n" + gestionnaireInterface.getTemplate() + "]\n";
        fichProjet.setWritable(true);
        FileWriter fw = new FileWriter(fichProjet);
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(contenuFichier);
        }
        Dialogs.create().title("Editeur PanoVisu")
                .masthead("Sauvegarde de fichier")
                .message("Votre fichier à bien été sauvegardé")
                .showInformation();

    }

    private void sauveHistoFichiers() throws IOException {
        File fichConfig = new File(EditeurPanovisu.repertConfig.getAbsolutePath() + File.separator + "derniersprojets.cfg");
        if (!fichConfig.exists()) {
            fichConfig.createNewFile();
        }
        fichConfig.setWritable(true);
        String contenuFichier = "";
        for (int i = 0; i < nombreHistoFichiers; i++) {
            contenuFichier += histoFichiers[i] + "\n";
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(fichConfig);
        } catch (IOException ex) {
            Logger.getLogger(ConfigDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write(contenuFichier);
        } catch (IOException ex) {
            Logger.getLogger(ConfigDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(ConfigDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @throws IOException
     */
    private void projetSauve() throws IOException {
        if (!repertSauveChoisi) {
            repertoireProjet = currentDir;
        }
        if (fichProjet == null) {
            FileChooser repertChoix = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("fichier panoVisu (*.pvu)", "*.pvu");
            repertChoix.getExtensionFilters().add(extFilter);
            File repert = new File(repertoireProjet + File.separator);
            repertChoix.setInitialDirectory(repert);
            fichProjet = repertChoix.showSaveDialog(null);

        }
        if (fichProjet != null) {
            sauveFichierProjet();
            ajouteFichierHisto(fichProjet.getAbsolutePath());
        }
    }

    /**
     *
     * @throws IOException
     */
    private void projetSauveSous() throws IOException {
        if (!repertSauveChoisi) {
            repertoireProjet = currentDir;
        }
        FileChooser repertChoix = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("fichier panoVisu (*.pvu)", "*.pvu");
        repertChoix.getExtensionFilters().add(extFilter);
        File repert = new File(repertoireProjet + File.separator);
        repertChoix.setInitialDirectory(repert);
        fichProjet = repertChoix.showSaveDialog(null);
        if (fichProjet != null) {
            sauveFichierProjet();
            ajouteFichierHisto(fichProjet.getAbsolutePath());

        }

    }

    /**
     *
     */
    private void aideAPropos() {
        try {
            popUp.affichePopup();

        } catch (Exception ex) {
            Logger.getLogger(EditeurPanovisu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     */
    private void projetsFermer() throws IOException {
        Action reponse = null;
        Localization.setLocale(locale);
        if (!dejaSauve) {
            reponse = Dialogs.create()
                    .owner(null)
                    .title("Quitter l'application")
                    .masthead("vous n'avez pas sauvegardé votre projet")
                    .message("Voulez vous le sauver ?")
                    .showConfirm();

        }
        if (reponse == Dialog.Actions.YES) {
            try {
                projetSauve();

            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        if ((reponse == Dialog.Actions.YES) || (reponse == Dialog.Actions.NO) || (reponse == null)) {
            sauveHistoFichiers();
            deleteDirectory(repertTemp);
            File ftemp = new File(repertTemp);
            ftemp.delete();
            Platform.exit();
        }
    }

    /**
     *
     */
    private void projetsNouveau() {
        Action reponse = null;
        Localization.setLocale(locale);
        if (!dejaSauve) {
            reponse = Dialogs.create()
                    .owner(null)
                    .title("Nouveau Projet")
                    .masthead("vous n'avez pas sauvegardé votre projet")
                    .message("Voulez vous le sauver ?")
                    .showConfirm();

        }
        if (reponse == Dialog.Actions.YES) {
            try {
                projetSauve();

            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        if ((reponse == Dialog.Actions.YES) || (reponse == Dialog.Actions.NO) || (reponse == null)) {
            panoEntree = "";
            deleteDirectory(repertTemp);
            String repertPanovisu = repertTemp + File.separator + "panovisu";
            File rptPanovisu = new File(repertPanovisu);
            rptPanovisu.mkdirs();
            copieDirectory(repertAppli + File.separator + "panovisu", repertPanovisu);
            menuPanoramique.setDisable(false);

            imgAjouterPano.setDisable(false);
            imgAjouterPano.setOpacity(1.0);
            imgSauveProjet.setDisable(false);
            imgSauveProjet.setOpacity(1.0);
            sauveProjet.setDisable(false);
            sauveSousProjet.setDisable(false);
            visiteGenere.setDisable(false);
            fichProjet = null;
            paneChoixPanoramique.setVisible(false);
            panoramiquesProjet = new Panoramique[50];
            nombrePanoramiques = 0;
            retireAffichageLigne();
            retireAffichageHotSpots();
            retireAffichagePointsHotSpots();
            numPoints = 0;
            imagePanoramique.setImage(null);
            listeChoixPanoramique.getItems().clear();
            listeChoixPanoramiqueEntree.getItems().clear();

            scene.setOnDragOver((DragEvent event) -> {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.ANY);
                } else {
                    event.consume();
                }
            });

            // Dropping over surface
            scene.setOnDragDropped((DragEvent event) -> {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    File[] list = new File[100];
                    int i = 0;
                    for (File file1 : db.getFiles()) {
                        filePath = file1.getAbsolutePath();
                        list[i] = file1;
                        i++;
                    }
                    int nb = i;
                    if (list != null) {
                        i = 0;
                        File[] lstFich1 = new File[list.length];
                        String[] typeFich1 = new String[list.length];
                        for (int jj = 0; jj < nb; jj++) {
                            File file = list[jj];
                            String nomfich = file.getAbsolutePath();
                            String extension = nomfich.substring(nomfich.lastIndexOf(".") + 1, nomfich.length()).toLowerCase();
                            if (extension.equals("bmp") || extension.equals("jpg")) {

                                Image img = new Image("file:" + file.getAbsolutePath());
                                if (img.getWidth() == 2 * img.getHeight()) {
                                    lstFich1[i] = file;
                                    typeFich1[i] = Panoramique.SPHERE;
                                    i++;
                                }
                                if (img.getWidth() == img.getHeight()) {
                                    String nom = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 6);
                                    boolean trouve = false;
                                    for (int j = 0; j < i; j++) {
                                        String nom1 = lstFich1[j].getAbsolutePath().substring(0, file.getAbsolutePath().length() - 6);
                                        if (nom.equals(nom1)) {
                                            trouve = true;
                                        }
                                    }
                                    if (!trouve) {
                                        lstFich1[i] = file;
                                        typeFich1[i] = Panoramique.CUBE;
                                        i++;
                                    }
                                }

                            }
                        }
                        File[] lstFich = new File[i];
                        System.arraycopy(lstFich1, 0, lstFich, 0, i);

                        for (int ii = 0; ii < lstFich.length; ii++) {
                            File file1 = lstFich[ii];
                            dejaSauve = false;
                            stPrincipal.setTitle(stPrincipal.getTitle().replace(" *", "") + " *");
                            sauveProjet.setDisable(false);
                            currentDir = file1.getParent();
                            File imageRepert = new File(repertTemp + File.separator + "panos");

                            if (!imageRepert.exists()) {

                                imageRepert.mkdirs();
                            }
                            repertPanos = imageRepert.getAbsolutePath();
//                            try {
//                                if (typeFich1[ii].equals(Panoramique.SPHERE)) {
//                                    copieFichierRepertoire(file1.getPath(), repertPanos);
//                                } else {
//                                    String nom = file1.getAbsolutePath().substring(0, file1.getAbsolutePath().length() - 6);
//                                    copieFichierRepertoire(nom + "_u.jpg", repertPanos);
//                                    copieFichierRepertoire(nom + "_d.jpg", repertPanos);
//                                    copieFichierRepertoire(nom + "_f.jpg", repertPanos);
//                                    copieFichierRepertoire(nom + "_b.jpg", repertPanos);
//                                    copieFichierRepertoire(nom + "_r.jpg", repertPanos);
//                                    copieFichierRepertoire(nom + "_l.jpg", repertPanos);
//                                }
//                            } catch (IOException ex) {
//                                Logger.getLogger(EditeurPanovisu.class.getName()).log(Level.SEVERE, null, ex);
//                            }
                            ajoutePanoramiqueProjet(file1.getPath(), typeFich1[ii]);
                        }

                        installeEvenements();

                        imgVisiteGenere.setOpacity(
                                1.0);
                        imgVisiteGenere.setDisable(
                                false);
                    }

                }
                event.setDropCompleted(success);
                event.consume();
            });

        }
    }

    /**
     *
     * @param nb
     * @return chaine de caractères aléatoire
     */
    private String genereChaineAleatoire(Number nb) {
        int nombre = (int) nb;
        String chaine = "";
        String chaine1 = "abcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < nombre; i++) {
            int numChar = (int) (Math.random() * 36.0f);
            chaine += chaine1.substring(numChar, numChar + 1);
        }
        return chaine;
    }

    /**
     *
     * @param emplacement répertoire à effacer
     */
    static public void deleteDirectory(String emplacement) {
        File path = new File(emplacement);
        if (path.exists()) {
            File[] files = path.listFiles();
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    deleteDirectory(file1.getAbsolutePath());
                }
                file1.delete();
            }
        }
    }

    private void analyseLigne(String ligneComplete) {
        panoEntree = "";
        nombrePanoramiques = 0;
        ligneComplete = ligneComplete.replace("[", "|");
        String lignes[] = ligneComplete.split("\\|", 500);
        String ligne;
        String[] elementsLigne;
        String[] typeElement;
        int nbHS = 0;
        for (int kk = 1; kk < lignes.length; kk++) {
            ligne = lignes[kk];
            elementsLigne = ligne.split(";", 10);
            typeElement = elementsLigne[0].split(">", 2);
            typeElement[0] = typeElement[0].replace(" ", "").replace("=", "").replace("[", "");
            elementsLigne[0] = typeElement[1];
            if ("Panoramique".equals(typeElement[0])) {
                for (int i = 0; i < elementsLigne.length; i++) {
                    elementsLigne[i] = elementsLigne[i].replace("]", "");
                    String[] valeur = elementsLigne[i].split(":", 2);
                    switch (valeur[0]) {
                        case "fichier":
                            String nmFich = valeur[1];
                            File imgPano = new File(nmFich);
                            File imageRepert = new File(repertTemp + File.separator + "panos");

                            if (!imageRepert.exists()) {
                                imageRepert.mkdirs();
                            }
                            repertPanos = imageRepert.getAbsolutePath();
                            Image img = new Image("file:" + imgPano.getPath());
                            try {
                                copieFichierRepertoire(imgPano.getPath(), repertPanos);

                            } catch (IOException ex) {
                                Logger.getLogger(EditeurPanovisu.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            if (img.getWidth() == img.getHeight()) {
                                ajoutePanoramiqueProjet(valeur[1], Panoramique.CUBE);
//                                try {
//                                    String nom = imgPano.getAbsolutePath().substring(0, imgPano.getAbsolutePath().length() - 6);
//                                    copieFichierRepertoire(nom + "_u.jpg", repertPanos);
//                                    copieFichierRepertoire(nom + "_d.jpg", repertPanos);
//                                    copieFichierRepertoire(nom + "_f.jpg", repertPanos);
//                                    copieFichierRepertoire(nom + "_b.jpg", repertPanos);
//                                    copieFichierRepertoire(nom + "_r.jpg", repertPanos);
//                                    copieFichierRepertoire(nom + "_l.jpg", repertPanos);
//                                } catch (IOException ex) {
//                                    Logger.getLogger(EditeurPanovisu.class
//                                            .getName()).log(Level.SEVERE, null, ex);
//                                }

                            } else {
                                ajoutePanoramiqueProjet(valeur[1], Panoramique.SPHERE);
//                                try {
//                                    copieFichierRepertoire(imgPano.getPath(), repertPanos);
//                                } catch (IOException ex) {
//                                    Logger.getLogger(EditeurPanovisu.class
//                                            .getName()).log(Level.SEVERE, null, ex);
//                                }
                            }

                            break;
                        case "titre":
                            if (!valeur[1].equals("'null'")) {
                                panoramiquesProjet[panoActuel].setTitrePanoramique(valeur[1]);
                            } else {
                                panoramiquesProjet[panoActuel].setTitrePanoramique("");
                            }
                            break;
                        case "type":
                            panoramiquesProjet[panoActuel].setTypePanoramique(valeur[1]);
                            break;
                        case "afficheInfo":
                            if (valeur[1].equals("true")) {
                                panoramiquesProjet[panoActuel].setAfficheInfo(true);
                            } else {
                                panoramiquesProjet[panoActuel].setAfficheInfo(false);
                            }
                            break;
                        case "afficheTitre":
                            if (valeur[1].equals("true")) {
                                panoramiquesProjet[panoActuel].setAfficheTitre(true);
                            } else {
                                panoramiquesProjet[panoActuel].setAfficheTitre(false);
                            }
                            break;
                        case "nb":
                            nbHS = Integer.parseInt(valeur[1]);
                            break;
                        case "regardX":
                            panoramiquesProjet[panoActuel].setLookAtX(Double.parseDouble(valeur[1]));
                            break;
                        case "regardY":
                            panoramiquesProjet[panoActuel].setLookAtY(Double.parseDouble(valeur[1]));
                            break;
                        case "zeroNord":
                            panoramiquesProjet[panoActuel].setZeroNord(Double.parseDouble(valeur[1]));
                            break;
                        default:
                            break;
                    }
                }
                for (int jj = 0; jj < nbHS; jj++) {
                    kk++;
                    ligne = lignes[kk];
                    elementsLigne = ligne.split(";", 10);
                    typeElement = elementsLigne[0].split(">", 2);
                    typeElement[0] = typeElement[0].replace(" ", "").replace("=", "").replace("[", "");
                    elementsLigne[0] = typeElement[1];
                    HotSpot HS = new HotSpot();
                    for (int i = 0; i < elementsLigne.length; i++) {
                        elementsLigne[i] = elementsLigne[i].replace("]", "");
                        String[] valeur = elementsLigne[i].split(":", 2);
                        switch (valeur[0]) {
                            case "longitude":
                                HS.setLongitude(Double.parseDouble(valeur[1]));
                                break;
                            case "latitude":
                                HS.setLatitude(Double.parseDouble(valeur[1]));
                                break;
                            case "image":
                                if ("null".equals(valeur[1])) {
                                    HS.setFichierImage(null);
                                } else {
                                    HS.setFichierImage(valeur[1]);
                                }
                                break;
                            case "info":
                                if ("null".equals(valeur[1])) {
                                    HS.setInfo(null);
                                } else {
                                    HS.setInfo(valeur[1]);
                                }
                                break;
                            case "xml":
                                if ("null".equals(valeur[1])) {
                                    HS.setFichierXML(null);
                                } else {
                                    HS.setFichierXML(valeur[1]);
                                }
                                break;
                            case "anime":
                                if (valeur[1].equals("true")) {
                                    HS.setAnime(true);
                                } else {
                                    HS.setAnime(false);
                                }
                                break;
                        }
                    }
                    panoramiquesProjet[panoActuel].addHotspot(HS);
                }
            }
            if ("Interface".equals(typeElement[0])) {
                String[] elts = elementsLigne[0].replace("]", "").split("\n");
                List<String> listeTemplate = new ArrayList<>();
                for (String texte : elts) {
                    if (!texte.equals("")) {
                        listeTemplate.add(texte);
                    }
                }
                gestionnaireInterface.setTemplate(listeTemplate);
            }
            if ("PanoEntree".equals(typeElement[0])) {
                panoEntree = elementsLigne[0].split("]")[0];
            }

        }
        if (!panoEntree.equals("")) {
            listeChoixPanoramiqueEntree.setValue(panoEntree + ".jpg");
        }
        for (int ii = 0; ii < nombrePanoramiques; ii++) {
            Panoramique pano1 = panoramiquesProjet[ii];
        }
    }

    /**
     *
     */
    private void lisFichierConfig() throws IOException {
//        MenuItem menuItem=new MenuItem("test");
//        menuItem.setOnAction(null);
//        derniersProjets.getItems().addAll(menuItem);
//        derniersProjets.setDisable(false);
        File fichConfig = new File(repertConfig.getAbsolutePath() + File.separator + "panovisu.cfg");
        if (fichConfig.exists()) {
            FileReader fr;
            try {
                fr = new FileReader(fichConfig);
                String texte;
                try (BufferedReader br = new BufferedReader(fr)) {
                    texte = "";
                    String ligneTexte;
                    String langue = "fr";
                    String pays = "FR";
                    String repert = repertAppli;
                    while ((texte = br.readLine()) != null) {
                        String tpe = texte.split("=")[0];
                        String valeur = texte.split("=")[1];
                        switch (tpe) {
                            case "langue":
                                langue = valeur;
                                break;
                            case "pays":
                                pays = valeur;
                                break;
                            case "repert":
                                repert = valeur;
                                break;

                        }

                    }
                    locale = new Locale(langue, pays);
                    repertoireProjet = repert;
                    currentDir = repert;
                    repertSauveChoisi = true;
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            fichConfig.createNewFile();
            locale = new Locale("fr", "FR");
            repertoireProjet = repertAppli;
        }

    }

    /**
     *
     * @param emplacement répertoire origine
     * @param repertoire répertoire cible
     */
    static public void copieDirectory(String emplacement, String repertoire) {
        File repert2 = new File(repertoire);
        if (!repert2.exists()) {
            repert2.mkdirs();
        }
        File path = new File(emplacement);
        if (path.exists()) {
            File[] files = path.listFiles();
            for (File file1 : files) {
                try {
                    if (file1.isDirectory()) {
                        String rep1 = file1.getAbsolutePath().substring(file1.getAbsolutePath().lastIndexOf(File.separator) + 1);
                        rep1 = repertoire + File.separator + rep1;
                        copieDirectory(file1.getAbsolutePath(), rep1);
                    } else {
                        copieFichierRepertoire(file1.getAbsolutePath(), repertoire);

                    }
                } catch (IOException ex) {
                    Logger.getLogger(EditeurPanovisu.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

//    /**
//     *
//     * @param emplacement répertoire origine
//     * @param repertoire répertoire cible
//     */
//    static public void copieDirectorysaufImage(String emplacement, String repertoire) {
//        File repert2 = new File(repertoire);
//        if (!repert2.exists()) {
//            repert2.mkdirs();
//        }
//        File path = new File(emplacement);
//        if (path.exists()) {
//            File[] files = path.listFiles();
//            for (File file1 : files) {
//                try {
//                    if (file1.isDirectory()) {
//                        if (!file1.getName().equals("images")) {
//                            String rep1 = file1.getAbsolutePath().substring(file1.getAbsolutePath().lastIndexOf(File.separator) + 1);
//                            rep1 = repertoire + File.separator + rep1;
//                            copieDirectorysaufImage(file1.getAbsolutePath(), rep1);
//                        }
//                    } else {
//                        copieFichierRepertoire(file1.getAbsolutePath(), repertoire);
//
//                    }
//                } catch (IOException ex) {
//                    Logger.getLogger(EditeurPanovisu.class
//                            .getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//    }
    /**
     *
     * @param fichier
     * @param repertoire
     * @throws FileNotFoundException
     * @throws IOException
     */
    static private void copieFichierRepertoire(String fichier, String repertoire) throws FileNotFoundException, IOException {
        String fichier1 = fichier.substring(fichier.lastIndexOf(File.separator) + 1);
        InputStream in = new FileInputStream(fichier);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(repertoire + File.separator + fichier1));
        byte[] buf = new byte[2048 * 1024];
        int n;
        while ((n = in.read(buf, 0, buf.length)) > 0) {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
    }

    /**
     *
     */
    private void retireAffichageHotSpots() {
        Pane lbl = (Pane) outils.lookup("#labels");
        outils.getChildren().remove(lbl);
    }

    /**
     *
     */
    private void retireAffichagePointsHotSpots() {
        for (int i = 0; i < numPoints; i++) {
            Node pt = (Node) pano.lookup("#point" + i);
            pano.getChildren().remove(pt);
        }
    }

    /**
     *
     * @param panCourant
     * @return
     */
    private String listePano(int panCourant) {
        String liste = "";
        if (nombrePanoramiques == 0) {
            return null;
        } else {
            for (int i = 0; i < nombrePanoramiques; i++) {
                if (i != panCourant) {
                    String fichierPano = panoramiquesProjet[i].getNomFichier();
                    String nomPano = fichierPano.substring(fichierPano.lastIndexOf(File.separator) + 1, fichierPano.length());
                    String[] nPano = nomPano.split("\\.");
                    liste += nPano[0];
                    if (i < nombrePanoramiques - 1) {
                        liste += ";";
                    }
                }
            }
            return liste;
        }
    }

    private AnchorPane afficherListePanosVignettes(int numHS) {

        pano.setOnMouseClicked(
                (MouseEvent me) -> {
                }
        );
        pano.setCursor(Cursor.DEFAULT);
        AnchorPane APlistePano = new AnchorPane();
        APlistePano.setOpacity(1);
        Pane fond = new Pane();
        fond.setStyle("-fx-background-color : #bbb;");
        fond.setPrefWidth(540);
        fond.setPrefHeight(((nombrePanoramiques - 2) / 4 + 1) * 65 + 10);
        fond.setMinWidth(540);
        fond.setMinHeight(70);
        APlistePano.getChildren().add(fond);
        APlistePano.setStyle("-fx-backgroung-color : #bbb;");
        int j = 0;
        ImageView[] IVPano;
        IVPano = new ImageView[nombrePanoramiques - 1];
        double xPos;
        double yPos;

        for (int i = 0; i < nombrePanoramiques; i++) {
            if (i != panoActuel) {
                int numeroPano = i;
                IVPano[j] = new ImageView(panoramiquesProjet[i].getVignettePanoramique());
                IVPano[j].setFitWidth(120);
                IVPano[j].setFitHeight(60);
                IVPano[j].setSmooth(true);
                String nomPano = panoramiquesProjet[i].getNomFichier();
                int col = j % 4;
                int row = j / 4;
                xPos = col * 130 + 25;
                yPos = row * 65 + 5;
                IVPano[j].setLayoutX(xPos);
                IVPano[j].setLayoutY(yPos);
                IVPano[j].setCursor(Cursor.HAND);
                IVPano[j].setStyle("-fx-background-color : #ccc;");
                Tooltip.install(IVPano[j], new Tooltip(nomPano.substring(nomPano.lastIndexOf(File.separator) + 1, nomPano.lastIndexOf("."))));
                IVPano[j].setOnMouseClicked((MouseEvent me) -> {
                    pano.setCursor(Cursor.CROSSHAIR);
                    pano.setOnMouseClicked(
                            (MouseEvent me1) -> {
                                gereSourisPanoramique(me1);
                            }
                    );
                    panoListeVignette = nomPano;
                    if (panoramiquesProjet[numeroPano].getTitrePanoramique() != null) {
                        String texteHS = panoramiquesProjet[numeroPano].getTitrePanoramique();
                        TextArea txtHS = (TextArea) scene.lookup("#txtHS" + numHS);
                        txtHS.setText(texteHS);
                    }
                    ComboBox cbx = (ComboBox) scene.lookup("#cbpano" + numHS);
                    cbx.setValue(nomPano.substring(nomPano.lastIndexOf(File.separator) + 1, nomPano.lastIndexOf(".")));
                    APlistePano.setVisible(false);
                    me.consume();
                });
                APlistePano.getChildren().add(IVPano[j]);
                j++;
            }

        }
        int taille = ((j + 1) / 4 + 1) * 65 + 5;
        APlistePano.setPrefWidth(540);
        APlistePano.setPrefHeight(taille);
        APlistePano.setMinWidth(540);
        APlistePano.setMinHeight(taille);
        //System.out.println(j + "=>" + taille);
        ImageView IVClose = new ImageView(new Image("file:" + repertAppli + File.separator + "images/ferme.png", 20, 20, true, true));
        IVClose.setLayoutX(2);
        IVClose.setLayoutY(5);
        IVClose.setCursor(Cursor.HAND);
        APlistePano.getChildren().add(IVClose);
        IVClose.setOnMouseClicked((MouseEvent me) -> {
            pano.setCursor(Cursor.CROSSHAIR);
            pano.setOnMouseClicked(
                    (MouseEvent me1) -> {
                        gereSourisPanoramique(me1);
                    }
            );

            panoListeVignette = "";
            APlistePano.setVisible(false);
            me.consume();
        });
        return APlistePano;
    }

    /**
     *
     */
    private void valideHS() {
        for (int i = 0; i < panoramiquesProjet[panoActuel].getNombreHotspots(); i++) {
            ComboBox cbx = (ComboBox) scene.lookup("#cbpano" + i);
            TextArea txtTexteHS = (TextArea) scene.lookup("#txtHS" + i);
            panoramiquesProjet[panoActuel].getHotspot(i).setInfo(txtTexteHS.getText());
            panoramiquesProjet[panoActuel].getHotspot(i).setFichierXML(cbx.getValue() + ".xml");
            CheckBox cbAnime = (CheckBox) scene.lookup("#anime" + i);
            if (cbAnime.isSelected()) {
                panoramiquesProjet[panoActuel].getHotspot(i).setAnime(true);
            } else {
                panoramiquesProjet[panoActuel].getHotspot(i).setAnime(false);
            }
        }

    }

    /**
     *
     * @param lstPano
     * @param numPano
     * @return
     */
    public Pane affichageHS(String lstPano, int numPano) {

        Pane panneauHotSpots = new Pane();
        panneauHotSpots.setTranslateY(10);
        VBox vb1 = new VBox(5);
        panneauHotSpots.getChildren().add(vb1);
        Label lbl;
        Label lblPoint;
        Label sep = new Label(" ");
        Label sep1 = new Label(" ");
        for (int o = 0; o < panoramiquesProjet[numPano].getNombreHotspots(); o++) {
            VBox vbPanneauHS = new VBox();
            Pane pannneauHS = new Pane(vbPanneauHS);
            panneauHotSpots.setId("HS" + o);
            String chLong1, chLat1;

            chLong1 = "Long : " + String.format("%.1f", panoramiquesProjet[numPano].getHotspot(o).getLongitude());
            chLat1 = "Lat : " + String.format("%.1f", panoramiquesProjet[numPano].getHotspot(o).getLatitude());
            lbl = new Label(chLong1 + "        " + chLat1);
            lbl.setPadding(new Insets(5, 10, 5, 5));
            lblPoint = new Label("Point n°" + o);
            lblPoint.setPadding(new Insets(5, 10, 5, 5));
            lblPoint.setStyle("-fx-background-color : #333;");
            lblPoint.setTextFill(Color.WHITE);
            Separator sp = new Separator(Orientation.HORIZONTAL);
            sp.setStyle("-fx-border-color : #777;");
            sp.setPrefWidth(300);
            pannneauHS.setStyle("-fx-border-color :#777;-fx-border-radius : 3px;-fx-background-color : #ccc;");

            pannneauHS.setPrefWidth(300);
            pannneauHS.setTranslateX(5);
            vbPanneauHS.getChildren().addAll(lblPoint, sp, lbl);
            if (lstPano != null) {
                Label lblLien = new Label("Panoramique de destination");
                ComboBox cbDestPano = new ComboBox();
                String[] liste = lstPano.split(";");
                cbDestPano.getItems().addAll(Arrays.asList(liste));
                cbDestPano.setTranslateX(60);
                cbDestPano.setId("cbpano" + o);
                String f1XML = panoramiquesProjet[numPano].getHotspot(o).getFichierXML();
                if (f1XML != null) {
                    cbDestPano.setValue(f1XML.split("\\.")[0]);
                }
                vbPanneauHS.getChildren().addAll(lblLien, cbDestPano, sep);
            }
            Label lblTexteHS = new Label("Texte du Hotspot");
            TextArea txtTexteHS = new TextArea();
            if (panoramiquesProjet[numPano].getHotspot(o).getInfo() != null) {
                txtTexteHS.setText(panoramiquesProjet[numPano].getHotspot(o).getInfo());
            }
            txtTexteHS.setId("txtHS" + o);
            txtTexteHS.setPrefSize(200, 25);
            txtTexteHS.setMaxSize(200, 20);
            txtTexteHS.setTranslateX(60);
            CheckBox cbAnime = new CheckBox("HostSpot Animé");
            cbAnime.setId("anime" + o);
            if (panoramiquesProjet[numPano].getHotspot(o).isAnime()) {
                cbAnime.setSelected(true);
            }
            cbAnime.setPadding(new Insets(5));
            cbAnime.setTranslateX(60);
            vbPanneauHS.getChildren().addAll(lblTexteHS, txtTexteHS, cbAnime, sep1);
            vb1.getChildren().addAll(pannneauHS, sep);
        }
        Button btnValider = new Button("Valide HotSpots");
        btnValider.setTranslateX(200);
        btnValider.setTranslateY(5);
        btnValider.setPadding(new Insets(7));
        btnValider.setOnAction((ActionEvent e) -> {
            valideHS();
        });
        vb1.getChildren().addAll(btnValider, sep1);
        return panneauHotSpots;
    }

    /**
     *
     */
    private void ajouteAffichageHotspots() {
        Pane lbl = affichageHS(listePano(panoActuel), panoActuel);
        lbl.setId("labels");
        outils.getChildren().add(lbl);
        numPoints = panoramiquesProjet[panoActuel].getNombreHotspots();
    }

    private AnchorPane afficheLegende() {
        AnchorPane APLegende = new AnchorPane();
        APLegende.setStyle("-fx-background-color : #ccc;-fx-border-color : #777;");
        APLegende.setMinWidth(400);
        APLegende.setMinHeight(150);
        APLegende.setPrefWidth(400);
        APLegende.setPrefHeight(150);
        APLegende.setMaxWidth(400);
        APLegende.setMaxHeight(150);
        APLegende.setLayoutY(imagePanoramique.getFitHeight() + 20);
        APLegende.setLayoutX(50);
        Circle point = new Circle(30, 20, 5);
        point.setFill(Color.YELLOW);
        point.setStroke(Color.RED);
        point.setCursor(Cursor.DEFAULT);
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
            15.0, 2.0,
            2.0, 2.0,
            2.0, 15.0,
            -2.0, 15.0,
            -2.0, 2.0,
            -15.0, 2.0,
            -15.0, -2.0,
            -2.0, -2.0,
            -2.0, -15.0,
            2.0, -15.0,
            2.0, -2.0,
            15.0, -2.0
        });
        polygon.setStrokeLineJoin(StrokeLineJoin.MITER);
        polygon.setFill(Color.BLUEVIOLET);
        polygon.setStroke(Color.YELLOW);
        polygon.setId("PoV");
        polygon.setLayoutX(30);
        polygon.setLayoutY(65);
        Label lblHS = new Label(rb.getString("main.legendeHS"));
        Label lblPoV = new Label(rb.getString("main.legendePoV"));
        Label lblNord = new Label(rb.getString("main.legendeNord"));
        Line ligneNord = new Line(30, 90, 30, 110);
        ligneNord.setStroke(Color.RED);
        ligneNord.setStrokeWidth(3);
        lblHS.setLayoutX(50);
        lblHS.setLayoutY(10);
        lblPoV.setLayoutX(50);
        lblPoV.setLayoutY(55);
        lblNord.setLayoutX(50);
        lblNord.setLayoutY(92);
        APLegende.getChildren().addAll(lblHS, point, lblPoV, polygon, lblNord, ligneNord);
        APLegende.setId("legende");
        APLegende.setVisible(false);
        APLegende.setTranslateY(10);
        APLegende.setTranslateX(30);
        return APLegende;
    }

    /**
     *
     * @param i
     * @param longitude
     * @param latitude
     */
    private void afficheHS(int i, double longitude, double latitude) {
        double largeur = imagePanoramique.getFitWidth();
        double X = (longitude + 180.0d) * largeur / 360.0d;
        double Y = (90.0d - latitude) * largeur / 360.0d;
        Circle point = new Circle(X, Y, 5);
        point.setFill(Color.YELLOW);
        point.setStroke(Color.RED);
        point.setId("point" + i);
        point.setCursor(Cursor.DEFAULT);
        pano.getChildren().add(point);
        Tooltip.install(point, new Tooltip("point n° " + i));

        point.setOnMouseClicked((MouseEvent me1) -> {
            double mouseX = me1.getSceneX();
            double mouseY = me1.getSceneY() - pano.getLayoutY() - 115;
            String chPoint = point.getId();
            chPoint = chPoint.substring(5, chPoint.length());
            int numeroPoint = Integer.parseInt(chPoint);
            Node pt;
            pt = (Node) pano.lookup("#point" + chPoint);

            if (me1.isControlDown()) {
                valideHS();
                dejaSauve = false;
                stPrincipal.setTitle(stPrincipal.getTitle().replace(" *", "") + " *");
                pano.getChildren().remove(pt);

                for (int o = numeroPoint + 1; o < numPoints; o++) {
                    pt = (Node) pano.lookup("#point" + Integer.toString(o));
                    pt.setId("point" + Integer.toString(o - 1));
                }
                /**
                 * on retire les anciennes indication de HS
                 */
                retireAffichageHotSpots();
                numPoints--;
                panoramiquesProjet[panoActuel].removeHotspot(numeroPoint);
                /**
                 * On les crée les nouvelles
                 */
                ajouteAffichageHotspots();
                me1.consume();
            } else {
                valideHS();
                if (nombrePanoramiques > 1) {
                    AnchorPane listePanoVig = afficherListePanosVignettes(numeroPoint);
                    if (mouseX + (nombrePanoramiques - 1) * 130 > pano.getWidth()) {
                        listePanoVig.setLayoutX(pano.getWidth() - (nombrePanoramiques - 1) * 130);
                    } else {
                        listePanoVig.setLayoutX(mouseX);
                    }
                    listePanoVig.setLayoutY(mouseY);
                    pano.getChildren().add(listePanoVig);
                }
                me1.consume();

            }

        });
    }

    /**
     *
     */
    private void ajouteAffichagePointsHotspots() {
        for (int i = 0; i < panoramiquesProjet[panoActuel].getNombreHotspots(); i++) {
            double longitude = panoramiquesProjet[panoActuel].getHotspot(i).getLongitude();
            double latitude = panoramiquesProjet[panoActuel].getHotspot(i).getLatitude();
            afficheHS(i, longitude, latitude);
        }

    }

    /**
     *
     */
    private void sauveFichiers() throws IOException {
        if (!dejaSauve) {
            projetSauve();
        }
    }

    private void panoChoixRegard(double X, double Y) {
        double mouseX = X;
        double mouseY = Y - pano.getLayoutY() - 115;
        double largeur = imagePanoramique.getFitWidth();
        double regardX = 360.0f * mouseX / largeur - 180;
        double regardY = 90.0d - 2.0f * mouseY / largeur * 180.0f;
        panoramiquesProjet[panoActuel].setLookAtX(regardX);
        panoramiquesProjet[panoActuel].setLookAtY(regardY);
        affichePoV(regardX, regardY);
    }

    private void panoChoixNord(double X) {
        double mouseX = X;
        double largeur = imagePanoramique.getFitWidth();
        double regardX = 360.0f * mouseX / largeur - 180;
        panoramiquesProjet[panoActuel].setZeroNord(regardX);
        afficheNord(regardX);
    }

    private void afficheNord(double longitude) {
        double largeur = imagePanoramique.getFitWidth();
        double X = (longitude + 180.0d) * largeur / 360.0d;
        Node ancPoV = (Node) pano.lookup("#Nord");
        if (ancPoV != null) {
            pano.getChildren().remove(ancPoV);
        }
        Line ligne = new Line(X, 0, X, imagePanoramique.getFitHeight());
        ligne.setStroke(Color.RED);
        ligne.setStrokeWidth(3);
        ligne.setId("Nord");
        pano.getChildren().add(ligne);
    }

    private void affichePoV(double longitude, double latitude) {
        double largeur = imagePanoramique.getFitWidth();
        double X = (longitude + 180.0d) * largeur / 360.0d;
        double Y = (90.0d - latitude) * largeur / 360.0d;
        Node ancPoV = (Node) pano.lookup("#PoV");
        if (ancPoV != null) {
            pano.getChildren().remove(ancPoV);
        }
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
            20.0, 2.0,
            2.0, 2.0,
            2.0, 20.0,
            -2.0, 20.0,
            -2.0, 2.0,
            -20.0, 2.0,
            -20.0, -2.0,
            -2.0, -2.0,
            -2.0, -20.0,
            2.0, -20.0,
            2.0, -2.0,
            20.0, -2.0
        });
        polygon.setStrokeLineJoin(StrokeLineJoin.MITER);
        polygon.setFill(Color.BLUEVIOLET);
        polygon.setStroke(Color.YELLOW);
        polygon.setId("PoV");
        polygon.setLayoutX(X);
        polygon.setLayoutY(Y);
        pano.getChildren().add(polygon);
    }

    private void panoMouseClic(double X, double Y) {

        valideHS();
        dejaSauve = false;
        stPrincipal.setTitle(stPrincipal.getTitle().replace(" *", "") + " *");
        double mouseX = X;
        double mouseY = Y - pano.getLayoutY() - 115;
        double longitude, latitude;
        double largeur = imagePanoramique.getFitWidth();
        String chLong, chLat;
        longitude = 360.0f * mouseX / largeur - 180;
        latitude = 90.0d - 2.0f * mouseY / largeur * 180.0f;
        Circle point = new Circle(mouseX, mouseY, 5);
        point.setFill(Color.YELLOW);
        point.setStroke(Color.RED);
        point.setId("point" + numPoints);
        point.setCursor(Cursor.DEFAULT);
        pano.getChildren().add(point);
        Tooltip.install(point, new Tooltip("point n°" + numPoints));
        HotSpot HS = new HotSpot();
        HS.setLongitude(longitude);
        HS.setLatitude(latitude);
        panoramiquesProjet[panoActuel].addHotspot(HS);
        retireAffichageHotSpots();
        Pane affHS1 = affichageHS(listePano(panoActuel), panoActuel);
        affHS1.setId("labels");
        outils.getChildren().add(affHS1);

        numPoints++;
        if (nombrePanoramiques > 1) {
            AnchorPane listePanoVig = afficherListePanosVignettes(panoramiquesProjet[panoActuel].getNombreHotspots() - 1);
            if (mouseX + (nombrePanoramiques - 1) * 130 > pano.getWidth()) {
                listePanoVig.setLayoutX(mouseX);
                listePanoVig.setLayoutX(pano.getWidth() - (nombrePanoramiques - 1) * 130);
            } else {
                listePanoVig.setLayoutX(mouseX);
            }
            listePanoVig.setLayoutY(mouseY);
            pano.getChildren().add(listePanoVig);
        }
        point.setOnMouseClicked((MouseEvent me1) -> {
            if (me1.isControlDown()) {
                valideHS();
                dejaSauve = false;
                stPrincipal.setTitle(stPrincipal.getTitle().replace(" *", "") + " *");
                String chPoint = point.getId();
                chPoint = chPoint.substring(5, chPoint.length());
                int numeroPoint = Integer.parseInt(chPoint);
                Node pt;
                pt = (Node) pano.lookup("#point" + chPoint);
                pano.getChildren().remove(pt);

                for (int o = numeroPoint + 1; o < numPoints; o++) {
                    pt = (Node) pano.lookup("#point" + Integer.toString(o));
                    pt.setId("point" + Integer.toString(o - 1));
                }
                /**
                 * on retire les anciennes indication de HS
                 */
                retireAffichageHotSpots();
                numPoints--;
                panoramiquesProjet[panoActuel].removeHotspot(numeroPoint);
                /**
                 * On les crée les nouvelles
                 */
                ajouteAffichageHotspots();
                me1.consume();
            } else {
                valideHS();
                String chPoint = point.getId();
                chPoint = chPoint.substring(5, chPoint.length());
                int numeroPoint = Integer.parseInt(chPoint);
                if (nombrePanoramiques > 1) {
                    AnchorPane listePanoVig = afficherListePanosVignettes(numeroPoint);
                    if (mouseX + (nombrePanoramiques - 1) * 130 > pano.getWidth()) {
                        listePanoVig.setLayoutX(mouseX);
                        listePanoVig.setLayoutX(pano.getWidth() - (nombrePanoramiques - 1) * 130);
                    } else {
                        listePanoVig.setLayoutX(mouseX);
                    }
                    listePanoVig.setLayoutY(mouseY);
                    pano.getChildren().add(listePanoVig);
                }
                me1.consume();

            }
        });

    }

    private void gereSourisPanoramique(MouseEvent me) {
        if (me.getButton() == MouseButton.SECONDARY) {
            if (me.isShiftDown()) {
            } else if (me.isControlDown()) {
            } else {
                panoChoixRegard(me.getSceneX(), me.getSceneY());
                me.consume();
            }
        }
        if (me.getButton() == MouseButton.PRIMARY) {
            if (me.isShiftDown()) {
                panoChoixNord(me.getSceneX());
                me.consume();
            } else if (!(me.isControlDown()) && estCharge) {
                panoMouseClic(me.getSceneX(), me.getSceneY());
            }
        }
    }

    /**
     *
     */
    private void installeEvenements() {
        /**
         *
         */
        scene.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {
            double largeur = (double) newSceneWidth - 320.0d;
            vuePanoramique.setPrefWidth(largeur);
        });
        /**
         *
         */
        scene.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) -> {
            vuePanoramique.setPrefHeight((double) newSceneHeight - 70.0d);
            panneauOutils.setPrefHeight((double) newSceneHeight - 70.0d);
        });

        /**
         *
         */
        pano.setOnMouseClicked(
                (MouseEvent me) -> {
                    gereSourisPanoramique(me);
                }
        );
        /**
         *
         */
        pano.setOnMouseMoved(
                (MouseEvent me) -> {
                    if (estCharge) {
                        double mouseX = me.getSceneX();
                        double mouseY = me.getSceneY() - pano.getLayoutY() - 115;
                        double longitude, latitude;
                        double largeur = imagePanoramique.getFitWidth() * pano.getScaleX();
                        longitude = 360.0f * mouseX / largeur - 180;
                        latitude = 90.0d - 2.0f * mouseY / largeur * 180.0f;
                        String chLong = "Long : " + String.format("%.1f", longitude);
                        String chLat = "Lat : " + String.format("%.1f", latitude);
                        lblLong.setText(chLong);
                        lblLat.setText(chLat);
                    }
                }
        );
        /*
        
         */
        listeChoixPanoramiqueEntree.valueProperty()
                .addListener(new ChangeListenerImplEntree());
        listeChoixPanoramique.valueProperty()
                .addListener(new ChangeListenerImpl());

    }

    private class ChangeListenerImpl implements ChangeListener<String> {

        public ChangeListenerImpl() {
        }

        @Override
        public void changed(ObservableValue ov, String ancienneValeur, String nouvelleValeur) {
            if (nouvelleValeur != null) {
                if (panoCharge) {
                    panoCharge = false;
                    panoAffiche = nouvelleValeur;
                } else {
                    if (!(nouvelleValeur.equals(panoAffiche))) {
                        clickBtnValidePano();
                        valideHS();

                        panoAffiche = nouvelleValeur;
                        int numPanoChoisit = listeChoixPanoramique.getSelectionModel().getSelectedIndex();
                        affichePanoChoisit(numPanoChoisit);
                    }
                }
            }

        }
    }

    private class ChangeListenerImplEntree implements ChangeListener<String> {

        public ChangeListenerImplEntree() {
        }

        @Override
        public void changed(ObservableValue ov, String ancienneValeur, String nouvelleValeur) {
            if ((nouvelleValeur != null) && (nouvelleValeur != "")) {
                panoEntree = nouvelleValeur.split("\\.")[0];
            }

        }
    }

    private void ajouteAffichageLignes() {
        double largeur = imagePanoramique.getFitWidth();
        double hauteur = largeur / 2.0d;
        Line ligne;
        int x, y;
        int nl = 0;
        for (int i = -180; i < 180; i += 10) {
            x = (int) (largeur / 2.0f + largeur / 360.0f * (float) i);
            ligne = new Line(x, 0, x, hauteur);
            ligne.setId("ligne" + nl);
            nl++;
            ligne.setStroke(Color.ORANGE);
            if (i == 0) {
                ligne.setStroke(Color.WHITE);
                ligne.setStrokeWidth(0.5);
            } else {
                if ((i % 20) == 0) {
                    ligne.setStroke(Color.WHITE);
                    ligne.setStrokeWidth(0.25);
                } else {
                    ligne.setStroke(Color.GRAY);
                    ligne.setStrokeWidth(0.25);
                }
            }
            pano.getChildren().add(ligne);
        }
        for (int i = -90; i < 90; i += 10) {
            y = (int) (hauteur / 2.0f + hauteur / 180.0f * (float) i);
            ligne = new Line(0, y, largeur, y);
            ligne.setId("ligne" + nl);
            nl++;
            if (i == 0) {
                ligne.setStroke(Color.WHITE);
                ligne.setStrokeWidth(0.5);
            } else {
                if ((i % 20) == 0) {
                    ligne.setStroke(Color.WHITE);
                    ligne.setStrokeWidth(0.25);
                } else {
                    ligne.setStroke(Color.GRAY);
                    ligne.setStrokeWidth(0.25);
                }
            }

            pano.getChildren().add(ligne);
        }

    }

    /**
     *
     */
    private void retireAffichageLigne() {
        int i = 0;
        Node lg;
        do {
            lg = (Node) pano.lookup("#ligne" + i);
            if (lg != null) {
                pano.getChildren().remove(lg);
            }
            i++;
        } while (lg != null);
    }

    /**
     *
     * @param numPanochoisi
     */
    @SuppressWarnings("empty-statement")
    private void affichePanoChoisit(int numPanochoisi) {
        imagePanoramique.setImage(panoramiquesProjet[numPanochoisi].getImagePanoramique());
        Node legende = (Node) panneau2.lookup("#legende");
        legende.setVisible(true);
        retireAffichagePointsHotSpots();
        retireAffichageHotSpots();
        retireAffichageLigne();
        numPoints = 0;

        panoActuel = numPanochoisi;
        ajouteAffichageHotspots();
        affichePoV(panoramiquesProjet[numPanochoisi].getLookAtX(), panoramiquesProjet[numPanochoisi].getLookAtY());
        afficheNord(panoramiquesProjet[numPanochoisi].getZeroNord());
        ajouteAffichagePointsHotspots();
        ajouteAffichageLignes();
        afficheInfoPano();
    }

    private void afficheInfoPano() {
        TextArea txtTitrePano = (TextArea) scene.lookup("#txttitrepano");
        CheckBox chkAfficheInfo = (CheckBox) scene.lookup("#chkafficheinfo");
        CheckBox chkAfficheTitre = (CheckBox) scene.lookup("#chkaffichetitre");
        RadioButton radSphere = (RadioButton) scene.lookup("#radsphere");
        RadioButton radCube = (RadioButton) scene.lookup("#radcube");

        if (panoramiquesProjet[panoActuel].getTitrePanoramique() != null) {
            txtTitrePano.setText(panoramiquesProjet[panoActuel].getTitrePanoramique());
        } else {
            txtTitrePano.setText("");
        }
        chkAfficheInfo.setSelected(panoramiquesProjet[panoActuel].isAfficheInfo());
        chkAfficheTitre.setSelected(panoramiquesProjet[panoActuel].isAfficheTitre());
        if (panoramiquesProjet[panoActuel].getTypePanoramique().equals(Panoramique.SPHERE)) {
            radSphere.setSelected(true);
            radCube.setSelected(false);
        } else {
            radCube.setSelected(true);
            radSphere.setSelected(false);
        }

    }

    /**
     *
     * @param fichierPano
     */
    @SuppressWarnings("empty-statement")
    private void ajoutePanoramiqueProjet(String fichierPano, String typePano) {
        String nomPano = fichierPano.substring(fichierPano.lastIndexOf(File.separator) + 1, fichierPano.length());
        panoCharge = true;
        listeChoixPanoramique.getItems().add(nomPano);
        listeChoixPanoramiqueEntree.getItems().add(nomPano);
        paneChoixPanoramique.setVisible(true);
        estCharge = true;
        Panoramique panoCree = new Panoramique();
        panoCree.setNomFichier(fichierPano);
        if (typePano.equals(Panoramique.SPHERE)) {
            int nombreNiveaux = creeNiveauxImageEqui(fichierPano, repertPanos);
            image2 = new Image("file:" + fichierPano, 1200, 600, true, true, true);
            Image image3 = new Image("file:" + fichierPano, 300, 150, true, true, true);
            panoCree.setNombreNiveaux(nombreNiveaux);
            panoCree.setVignettePanoramique(image3);
            panoCree.setImagePanoramique(image2);
            panoCree.setTypePanoramique(Panoramique.SPHERE);
        } else {
            String nom = fichierPano.substring(0, fichierPano.length() - 6);
            String extension = fichierPano.substring(fichierPano.length() - 3, fichierPano.length());
            Image top;
            Image bottom;
            Image left;
            Image right;
            Image front;
            Image behind;
            if (extension.equals("bmp")) {
                top = new Image("file:" + nom + "_u.bmp");
                bottom = new Image("file:" + nom + "_d.bmp");
                left = new Image("file:" + nom + "_l.bmp");
                right = new Image("file:" + nom + "_r.bmp");
                front = new Image("file:" + nom + "_f.bmp");
                behind = new Image("file:" + nom + "_b.bmp");
            } else {
                top = new Image("file:" + nom + "_u.jpg");
                bottom = new Image("file:" + nom + "_d.jpg");
                left = new Image("file:" + nom + "_l.jpg");
                right = new Image("file:" + nom + "_r.jpg");
                front = new Image("file:" + nom + "_f.jpg");
                behind = new Image("file:" + nom + "_b.jpg");
            }
            int nombreNiveaux = creeNiveauxImageCube(nom + "_u.jpg", repertPanos);
            creeNiveauxImageCube(nom + "_d.jpg", repertPanos);
            creeNiveauxImageCube(nom + "_l.jpg", repertPanos);
            creeNiveauxImageCube(nom + "_r.jpg", repertPanos);
            creeNiveauxImageCube(nom + "_f.jpg", repertPanos);
            creeNiveauxImageCube(nom + "_b.jpg", repertPanos);
            panoCree.setNombreNiveaux(nombreNiveaux);

            Image equiRectangulaire = TransformationsPanoramique.cube2rect(front, left, right, behind, top, bottom, 1200);
            Image vignetteEquiRectangulaire = TransformationsPanoramique.cube2rect(front, left, right, behind, top, bottom, 300);
            panoCree.setImagePanoramique(equiRectangulaire);
            panoCree.setVignettePanoramique(vignetteEquiRectangulaire);
            panoCree.setTypePanoramique(Panoramique.CUBE);
        }

        panoCree.setLookAtX(0.0d);
        panoCree.setLookAtY(0.0d);
        panoCree.setAfficheInfo(true);
        panoCree.setAfficheTitre(true);
        imagePanoramique.setImage(panoCree.getImagePanoramique());
        CheckBox chkAfficheInfo = (CheckBox) scene.lookup("#chkafficheinfo");
        CheckBox chkAfficheTitre = (CheckBox) scene.lookup("#chkaffichetitre");
        RadioButton radSphere = (RadioButton) scene.lookup("#radsphere");
        RadioButton radCube = (RadioButton) scene.lookup("#radcube");

        imagePanoramique.setSmooth(true);
        retireAffichageLigne();
        retireAffichageHotSpots();
        retireAffichagePointsHotSpots();
        numPoints = 0;
        ajouteAffichageLignes();
        panoramiquesProjet[nombrePanoramiques] = panoCree;
        panoActuel = nombrePanoramiques;
        chkAfficheInfo.setSelected(panoramiquesProjet[panoActuel].isAfficheInfo());
        chkAfficheTitre.setSelected(panoramiquesProjet[panoActuel].isAfficheTitre());
        if (panoramiquesProjet[panoActuel].getTypePanoramique().equals(Panoramique.SPHERE)) {
            radSphere.setSelected(true);
            radCube.setSelected(false);
        } else {
            radCube.setSelected(true);
            radSphere.setSelected(false);
        }

        listeChoixPanoramique.setValue(listeChoixPanoramique.getItems().get(nombrePanoramiques));
        //listeChoixPanoramiqueEntree.setValue(listeChoixPanoramiqueEntree.getItems().get(0));
        nombrePanoramiques++;

    }

    /**
     *
     */
    private void transformationCube2Equi() {
        EquiCubeDialogController C2EController = new EquiCubeDialogController();
        try {
            C2EController.afficheFenetre(EquiCubeDialogController.CUBE2QUI);

        } catch (Exception ex) {
            Logger.getLogger(EditeurPanovisu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     */
    private void transformationEqui2Cube() {
        EquiCubeDialogController E2CController = new EquiCubeDialogController();
        try {
            E2CController.afficheFenetre(EquiCubeDialogController.EQUI2CUBE);

        } catch (Exception ex) {
            Logger.getLogger(EditeurPanovisu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     */
    private void modeleSauver() throws IOException {
        File fichTemplate;
        FileChooser repertChoix = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("fichier Modèle panoVisu (*.tpl)", "*.tpl");
        repertChoix.getExtensionFilters().add(extFilter);
        File repert = new File(repertAppli + File.separator + "templates");
        if (!repert.exists()) {
            repert.mkdirs();
        }
        repertChoix.setInitialDirectory(repert);
        fichTemplate = repertChoix.showSaveDialog(null);

        if (fichTemplate != null) {
            String contenuFichier = gestionnaireInterface.getTemplate();
            fichTemplate.setWritable(true);
            FileWriter fw = new FileWriter(fichTemplate);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(contenuFichier);
            }
            Dialogs.create().title("Editeur PanoVisu")
                    .masthead("Sauvegarde du fichier de Modèle")
                    .message("Votre modèle à bien été sauvegardé")
                    .showInformation();
        }

    }

    /**
     *
     */
    private void modeleCharger() throws IOException {
        FileChooser repertChoix = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("fichier panoVisu (*.tpl)", "*.tpl");
        repertChoix.getExtensionFilters().add(extFilter);
        File repert = new File(repertAppli + File.separator + "templates");
        repertChoix.setInitialDirectory(repert);
        File fichtemplate = repertChoix.showOpenDialog(stPrincipal);
        if (fichtemplate != null) {
            FileReader fr;
            try {
                fr = new FileReader(fichtemplate);
                List<String> texte = new ArrayList<>();
                BufferedReader br = new BufferedReader(fr);

                String ligneTexte;
                while ((ligneTexte = br.readLine()) != null) {
                    texte.add(ligneTexte);
                }
                gestionnaireInterface.setTemplate(texte);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     *
     * @param primaryStage
     * @throws Exception
     */
    private void creeEnvironnement(Stage primaryStage) throws Exception {
        creeEnvironnement(primaryStage, 800, 600);
    }

    /**
     *
     * @param primaryStage
     * @param racine
     * @param taille
     * @throws Exception
     */
    private void creeMenu(Stage primaryStage, VBox racine, int taille) throws Exception {
        //Pane myPane = (Pane) FXMLLoader.load(getClass().getResource("menuPrincipal.fxml"));
        VBox myPane = new VBox();
        myPane.setPrefHeight(80);
        myPane.setPrefWidth(3000);
        MenuBar menuPrincipal = new MenuBar();
        menuPrincipal.setMinHeight(21);
        menuPrincipal.setPrefHeight(29);
        menuPrincipal.setPrefWidth(3000);
        /*
         Menu projets
         */
        Menu menuProjet = new Menu(rb.getString("projets"));
        menuPrincipal.getMenus().add(menuProjet);
        nouveauProjet = new MenuItem(rb.getString("nouveauProjet"));
        nouveauProjet.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        menuProjet.getItems().add(nouveauProjet);
        chargeProjet = new MenuItem(rb.getString("ouvrirProjet"));
        chargeProjet.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        menuProjet.getItems().add(chargeProjet);
        sauveProjet = new MenuItem(rb.getString("sauverProjet"));
        sauveProjet.setDisable(true);
        sauveProjet.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        menuProjet.getItems().add(sauveProjet);
        sauveSousProjet = new MenuItem(rb.getString("sauverProjetSous"));
        sauveSousProjet.setDisable(true);
        sauveSousProjet.setAccelerator(KeyCombination.keyCombination("Shift+Ctrl+S"));
        menuProjet.getItems().add(sauveSousProjet);
        derniersProjets = new Menu(rb.getString("derniersProjets"));
//        derniersProjets.setDisable(true);
        menuProjet.getItems().add(derniersProjets);
        fichHistoFichiers = new File(repertConfig.getAbsolutePath() + File.separator + "derniersprojets.cfg");
        nombreHistoFichiers = 0;
        if (fichHistoFichiers.exists()) {
            FileReader fr;
            fr = new FileReader(fichHistoFichiers);
            try (BufferedReader br = new BufferedReader(fr)) {
                while ((texteHisto = br.readLine()) != null) {
                    MenuItem menuDerniersFichiers = new MenuItem(texteHisto);
                    derniersProjets.getItems().add(menuDerniersFichiers);
                    histoFichiers[nombreHistoFichiers] = texteHisto;
                    nombreHistoFichiers++;
                    menuDerniersFichiers.setOnAction((ActionEvent e) -> {
                        MenuItem mnu = (MenuItem) e.getSource();

                        try {
                            projetChargeNom(mnu.getText());
                        } catch (IOException ex) {
                            Logger.getLogger(EditeurPanovisu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });

                }
            }
        }

        SeparatorMenuItem sep1 = new SeparatorMenuItem();
        menuProjet.getItems().add(sep1);
        visiteGenere = new MenuItem(rb.getString("genererVisite"));
        visiteGenere.setDisable(true);
        visiteGenere.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
        menuProjet.getItems().add(visiteGenere);
        SeparatorMenuItem sep2 = new SeparatorMenuItem();
        menuProjet.getItems().add(sep2);
        fermerProjet = new MenuItem(rb.getString("quitterApplication"));
        fermerProjet.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        menuProjet.getItems().add(fermerProjet);
        /*
         Menu panoramiques
         */
        menuPanoramique = new Menu(rb.getString("panoramiques"));
        menuPanoramique.setDisable(true);
        menuPrincipal.getMenus().add(menuPanoramique);
        ajouterPano = new MenuItem(rb.getString("ajouterPanoramiques"));
        ajouterPano.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        menuPanoramique.getItems().add(ajouterPano);
        /*
         Menu Modèles 
         */
        menuModeles = new Menu(rb.getString("menuModele"));
        menuPrincipal.getMenus().add(menuModeles);

        chargerModele = new MenuItem(rb.getString("modeleCharger"));
        menuModeles.getItems().add(chargerModele);

        sauverModele = new MenuItem(rb.getString("modeleSauver"));
        menuModeles.getItems().add(sauverModele);

        /*
         Menu transformations 
         */
        menuTransformation = new Menu(rb.getString("outils"));
        menuPrincipal.getMenus().add(menuTransformation);

        cube2EquiTransformation = new MenuItem(rb.getString("outilsCube2Equi"));
        menuTransformation.getItems().add(cube2EquiTransformation);
        equi2CubeTransformation = new MenuItem(rb.getString("outilsEqui2Cube"));
        menuTransformation.getItems().add(equi2CubeTransformation);
        SeparatorMenuItem sep3 = new SeparatorMenuItem();
        menuTransformation.getItems().add(sep3);
        configTransformation = new MenuItem(rb.getString("outilsConfiguration"));
        menuTransformation.getItems().add(configTransformation);

        /*
         Menu Aide
         */
        Menu menuAide = new Menu(rb.getString("aide"));
        menuPrincipal.getMenus().add(menuAide);
        aide = new MenuItem(rb.getString("aideAide"));
        aide.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
        menuAide.getItems().add(aide);
        SeparatorMenuItem sep4 = new SeparatorMenuItem();
        menuAide.getItems().add(sep4);
        aPropos = new MenuItem(rb.getString("aideAPropos"));
        menuAide.getItems().add(aPropos);
        /*
         barre de boutons 
         */
        String couleurBouton = "-fx-background-color:#f7f7f7;";
        HBox barreBouton = new HBox();
        barreBouton.setPrefHeight(50);
        barreBouton.setPrefWidth(3000);
        /*
         Bouton nouveau Projet
         */
        ScrollPane SPBtnNouvprojet = new ScrollPane();
        SPBtnNouvprojet.setStyle(couleurBouton);
        SPBtnNouvprojet.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SPBtnNouvprojet.setPrefHeight(35);
        SPBtnNouvprojet.setPrefWidth(35);

        HBox.setMargin(SPBtnNouvprojet, new Insets(5, 15, 0, 15));
        imgNouveauProjet = new ImageView(new Image("file:" + repertAppli + File.separator + "images/nouveauProjet.png"));
        SPBtnNouvprojet.setContent(imgNouveauProjet);
        SPBtnNouvprojet.setTooltip(new Tooltip(rb.getString("nouveauProjet")));
        barreBouton.getChildren().add(SPBtnNouvprojet);
        /*
         Bouton ouvrir Projet
         */
        ScrollPane SPBtnOuvrirProjet = new ScrollPane();
        SPBtnOuvrirProjet.setStyle(couleurBouton);
        SPBtnOuvrirProjet.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SPBtnOuvrirProjet.setPrefHeight(35);
        SPBtnOuvrirProjet.setPrefWidth(35);

        HBox.setMargin(SPBtnOuvrirProjet, new Insets(5, 15, 0, 0));
        imgChargeProjet = new ImageView(new Image("file:" + repertAppli + File.separator + "images/ouvrirProjet.png"));
        SPBtnOuvrirProjet.setContent(imgChargeProjet);
        SPBtnOuvrirProjet.setTooltip(new Tooltip(rb.getString("ouvrirProjet")));
        barreBouton.getChildren().add(SPBtnOuvrirProjet);

        /*
         Bouton sauve Projet
         */
        ScrollPane SPBtnSauveProjet = new ScrollPane();
        SPBtnSauveProjet.setStyle(couleurBouton);
        SPBtnSauveProjet.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SPBtnSauveProjet.setPrefHeight(35);
        SPBtnSauveProjet.setPrefWidth(35);

        HBox.setMargin(SPBtnSauveProjet, new Insets(5, 15, 0, 0));
        imgSauveProjet = new ImageView(new Image("file:" + repertAppli + File.separator + "images/sauveProjet.png"));
        SPBtnSauveProjet.setContent(imgSauveProjet);
        SPBtnSauveProjet.setTooltip(new Tooltip(rb.getString("sauverProjet")));
        barreBouton.getChildren().add(SPBtnSauveProjet);
        Separator sepImages = new Separator(Orientation.VERTICAL);
        sepImages.prefHeight(200);
        barreBouton.getChildren().add(sepImages);
        imgSauveProjet.setDisable(true);
        imgSauveProjet.setOpacity(0.3);
        /*
         Bouton Ajoute Panoramique
         */
        ScrollPane SPBtnAjoutePano = new ScrollPane();
        SPBtnAjoutePano.setStyle(couleurBouton);
        SPBtnAjoutePano.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SPBtnAjoutePano.setPrefHeight(35);
        SPBtnAjoutePano.setPrefWidth(35);

        HBox.setMargin(SPBtnAjoutePano, new Insets(5, 15, 0, 15));
        imgAjouterPano = new ImageView(new Image("file:" + repertAppli + File.separator + "images/ajoutePanoramique.png"));
        SPBtnAjoutePano.setContent(imgAjouterPano);
        SPBtnAjoutePano.setTooltip(new Tooltip(rb.getString("ajouterPanoramiques")));
        barreBouton.getChildren().add(SPBtnAjoutePano);
        imgAjouterPano.setDisable(true);
        imgAjouterPano.setOpacity(0.3);


        /*
         Bouton Génère
         */
        ScrollPane SPBtnGenereVisite = new ScrollPane();
        SPBtnGenereVisite.setStyle(couleurBouton);
        SPBtnGenereVisite.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SPBtnGenereVisite.setPrefHeight(35);
        SPBtnGenereVisite.setPrefWidth(70);

        HBox.setMargin(SPBtnGenereVisite, new Insets(5, 15, 0, 0));
        imgVisiteGenere = new ImageView(new Image("file:" + repertAppli + File.separator + "images/genereVisite.png"));
        SPBtnGenereVisite.setContent(imgVisiteGenere);
        SPBtnGenereVisite.setTooltip(new Tooltip(rb.getString("genererVisite")));
        barreBouton.getChildren().add(SPBtnGenereVisite);
        imgVisiteGenere.setDisable(true);
        imgVisiteGenere.setOpacity(0.3);
        Separator sepImages1 = new Separator(Orientation.VERTICAL);
        sepImages1.prefHeight(200);
        barreBouton.getChildren().add(sepImages1);

        /*
         Bouton faces de cube -> equi
         */
        ScrollPane SPBtnCube2Equi = new ScrollPane();
        SPBtnCube2Equi.setStyle(couleurBouton);
        SPBtnCube2Equi.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SPBtnCube2Equi.setPrefHeight(35);
        SPBtnCube2Equi.setPrefWidth(109);

        HBox.setMargin(SPBtnCube2Equi, new Insets(5, 25, 0, 250));
        imgCube2Equi = new ImageView(new Image("file:" + repertAppli + File.separator + "images/cube2equi.png"));
        SPBtnCube2Equi.setContent(imgCube2Equi);
        SPBtnCube2Equi.setTooltip(new Tooltip(rb.getString("outilsCube2Equi")));
        barreBouton.getChildren().add(SPBtnCube2Equi);
        /*
         Bouton equi -> faces de  Cube
         */
        ScrollPane SPBtnEqui2Cube = new ScrollPane();
        SPBtnEqui2Cube.setStyle(couleurBouton);
        SPBtnEqui2Cube.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SPBtnEqui2Cube.setPrefHeight(35);
        SPBtnEqui2Cube.setPrefWidth(109);

        HBox.setMargin(SPBtnEqui2Cube, new Insets(5, 15, 0, 0));
        imgEqui2Cube = new ImageView(new Image("file:" + repertAppli + File.separator + "images/equi2cube.png"));
        SPBtnEqui2Cube.setContent(imgEqui2Cube);
        SPBtnEqui2Cube.setTooltip(new Tooltip(rb.getString("outilsEqui2Cube")));
        barreBouton.getChildren().add(SPBtnEqui2Cube);

        myPane.getChildren().addAll(menuPrincipal, barreBouton);
        racine.getChildren().add(myPane);
        nouveauProjet.setOnAction((ActionEvent e) -> {
            projetsNouveau();
        });
        chargeProjet.setOnAction((ActionEvent e) -> {
            try {
                projetCharge();

            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
        sauveProjet.setOnAction((ActionEvent e) -> {
            try {
                projetSauve();

            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
        sauveSousProjet.setOnAction((ActionEvent e) -> {
            try {
                projetSauveSous();

            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
        visiteGenere.setOnAction((ActionEvent e) -> {
            try {
                genereVisite();

            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
        fermerProjet.setOnAction((ActionEvent e) -> {
            try {
                projetsFermer();
            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        ajouterPano.setOnAction((ActionEvent e) -> {
            panoramiquesAjouter();
        });
        aPropos.setOnAction((ActionEvent e) -> {
            aideAPropos();
        });
        aide.setOnAction((ActionEvent e) -> {
            AideDialogController.affiche();
        });

        chargerModele.setOnAction((ActionEvent e) -> {
            try {
                modeleCharger();

            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });

        sauverModele.setOnAction((ActionEvent e) -> {
            try {
                modeleSauver();

            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });

        cube2EquiTransformation.setOnAction((ActionEvent e) -> {
            transformationCube2Equi();
        });
        configTransformation.setOnAction((ActionEvent e) -> {
            try {
                ConfigDialogController cfg = new ConfigDialogController();
                cfg.afficheFenetre();

            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });

        imgNouveauProjet.setOnMouseClicked((MouseEvent t) -> {
            projetsNouveau();
        });
        imgChargeProjet.setOnMouseClicked((MouseEvent t) -> {
            try {
                projetCharge();

            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
        imgSauveProjet.setOnMouseClicked((MouseEvent t) -> {
            try {
                projetSauve();

            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
        imgAjouterPano.setOnMouseClicked((MouseEvent t) -> {
            panoramiquesAjouter();
        });
        imgVisiteGenere.setOnMouseClicked((MouseEvent t) -> {
            try {
                genereVisite();

            } catch (IOException ex) {
                Logger.getLogger(EditeurPanovisu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
        imgEqui2Cube.setOnMouseClicked((MouseEvent t) -> {
            transformationEqui2Cube();
        });
        imgCube2Equi.setOnMouseClicked((MouseEvent t) -> {
            transformationCube2Equi();
        });

    }

    private void creeTabInterface() {

    }

    /**
     *
     * @param primaryStage
     * @param width
     * @param height
     * @throws Exception
     */
    private void creeEnvironnement(Stage primaryStage, int width, int height) throws Exception {
        popUp = new PopUpDialogController();
        primaryStage.setMaximized(true);

        /**
         * Création des éléments constitutifs de l'écran
         */
        VBox root = new VBox();
        creeMenu(primaryStage, root, width);
        TabPane tabPaneEnvironnement = new TabPane();
//        tabPaneEnvironnement.setTranslateZ(5);
        tabPaneEnvironnement.setMinHeight(height - 140);
        tabPaneEnvironnement.setMaxHeight(height - 140);
        ScrollPane lastFiles = new ScrollPane();
        lastFiles.setPrefSize(310, 200);
        VBox lstDerniersFichiers = new VBox();
        Label lblDerniersFichiers = new Label("liste des derniers Projets");
        ListView listDerniersFichiers = new ListView();
        listDerniersFichiers.setPrefSize(300, 140);
        listDerniersFichiers.setId("listderniersfichiers");
        valideChargeDerniersFichiers = new Button("Charge");
        valideChargeDerniersFichiers.setDisable(true);
        valideChargeDerniersFichiers.setPadding(new Insets(7));
        valideChargeDerniersFichiers.setTranslateX(200);
        valideChargeDerniersFichiers.setId("validechargederniersfichiers");
        valideChargeDerniersFichiers.setOnAction((ActionEvent e) -> {
            ListView lv = (ListView) scene.lookup("#listderniersfichiers");
            String nomFich;
            nomFich = (String) lv.getSelectionModel().getSelectedItem();
        });
        listDerniersFichiers.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> ov,
                            String old_val, String new_val) {
                        if (new_val != null) {
                            Button valide = (Button) scene.lookup("#validechargederniersfichiers");
                            valide.setDisable(false);
                        }
                    }
                });

        lstDerniersFichiers.getChildren().addAll(lblDerniersFichiers, listDerniersFichiers, valideChargeDerniersFichiers);
        lastFiles.setContent(lstDerniersFichiers);
        Pane barreStatus = new Pane();
        barreStatus.setPrefSize(width + 20, 30);
        barreStatus.setTranslateY(25);
        barreStatus.setStyle("-fx-background-color:#c00;-fx-border-color:#aaa");
        Tab tabVisite = new Tab();
        Pane visualiseur;
        tabInterface = new Tab();
        gestionnaireInterface.creeInterface(width, height - 140);
        visualiseur = gestionnaireInterface.tabInterface;
        tabInterface.setContent(visualiseur);

        HBox hbEnvironnement = new HBox();
        TextArea txtTitrePano;
        RadioButton radSphere;
        RadioButton radCube;
        CheckBox chkAfficheTitre;
        CheckBox chkAfficheInfo;
        Button btnValidePano;

        tabPaneEnvironnement.getTabs().addAll(tabVisite, tabInterface);
        //tabPaneEnvironnement.setTranslateY(80);
        tabPaneEnvironnement.setSide(Side.TOP);
        tabVisite.setText(rb.getString("main.creationVisite"));
        tabVisite.setClosable(false);
        tabInterface.setText(rb.getString("main.creationInterface"));
        tabInterface.setClosable(false);
        tabVisite.setContent(hbEnvironnement);
        double largeur;
        String labelStyle = "-fx-color : white;-fx-background-color : #fff;-fx-padding : 5px;  -fx-border : 1px solid #777;-fx-width : 100px;-fx-margin : 5px; ";

        scene = new Scene(root, width, height, Color.rgb(221, 221, 221));
//        if (systemeExploitation.indexOf("inux") != -1) {
//            root.setStyle("-fx-font-size : 7pt;-fx-font-family: sans-serif;");
//        } else {
        root.setStyle("-fx-font-size : 8pt;-fx-font-family: Arial;");
//        }
        panneauOutils = new ScrollPane();
        panneauOutils.setStyle("-fx-background-color : #ccc;");
        outils = new VBox();
        paneChoixPanoramique = new VBox();
        paneChoixPanoramique.setId("choixPanoramique");
        Label lblChoixPanoramiqueEntree = new Label("Choix du panoramique d'entrée de la visite");
        lblChoixPanoramiqueEntree.setPadding(new Insets(5));
        lblChoixPanoramiqueEntree.setStyle("-fx-background-color : #000;");
        lblChoixPanoramiqueEntree.setTextFill(Color.WHITE);

        lblChoixPanoramique = new Label("Affichage du panoramique");
        lblChoixPanoramique.setPadding(new Insets(5));
        lblChoixPanoramique.setStyle("-fx-background-color : #000;");
        lblChoixPanoramique.setTextFill(Color.WHITE);
        Separator sepPano = new Separator(Orientation.HORIZONTAL);
        sepPano.setMinHeight(30);
        listeChoixPanoramique.setVisibleRowCount(10);
        paneChoixPanoramique.getChildren().addAll(lblChoixPanoramiqueEntree, listeChoixPanoramiqueEntree, sepPano, lblChoixPanoramique, listeChoixPanoramique);
        paneChoixPanoramique.setSpacing(10);
        /*
         à modifier pour afficher le panneau des derniers fichiers;        
         */
        //outils.getChildren().addAll(lastFiles, paneChoixPanoramique);

        outils.getChildren().addAll(paneChoixPanoramique);

        paneChoixPanoramique.setVisible(false);
        /*
         Création du panneau d'info du panoramique
         */
        VBox vbInfoPano = new VBox(5);
        vbInfoPano.setPrefSize(310, 270);
        Pane myAnchorPane = new Pane(vbInfoPano);
        Separator sepInfo = new Separator(Orientation.HORIZONTAL);
        sepInfo.setTranslateY(5);
        Separator sepInfo1 = new Separator(Orientation.HORIZONTAL);
        sepInfo1.setTranslateY(10);
        Separator sepInfo2 = new Separator(Orientation.HORIZONTAL);
        sepInfo2.setTranslateY(20);
        Label lblTitrePano = new Label("Titre du panoramique");
        lblTitrePano.setPadding(new Insets(30, 5, 5, 5));
        txtTitrePano = new TextArea();
        txtTitrePano.setId("txttitrepano");
        txtTitrePano.setPrefSize(200, 25);
        Label lblTypePano = new Label("Type de panoramique");
        lblTypePano.setPadding(new Insets(10, 5, 5, 5));
        radSphere = new RadioButton(Panoramique.SPHERE);
        radSphere.setSelected(true);
        radSphere.setId("radsphere");
        radCube = new RadioButton(Panoramique.CUBE);
        radCube.setSelected(false);
        radCube.setId("radcube");
        radCube.setOnMouseClicked((MouseEvent me) -> {
            RadioButton radSphere1 = (RadioButton) scene.lookup("#radsphere");
            radSphere1.setSelected(false);
        });
        radSphere.setOnMouseClicked((MouseEvent me) -> {
            RadioButton radCube1 = (RadioButton) scene.lookup("#radcube");
            radCube1.setSelected(false);
        });
        HBox hbTypePano = new HBox(radSphere, radCube);
        hbTypePano.setTranslateX(60);
        chkAfficheTitre = new CheckBox("Affiche Titre");
        chkAfficheTitre.setId("chkaffichetitre");
        chkAfficheInfo = new CheckBox("Affiche écran d'information");
        chkAfficheInfo.setId("chkafficheinfo");
        chkAfficheInfo.setTranslateX(60);
        chkAfficheTitre.setTranslateX(60);
        chkAfficheTitre.setTranslateY(10);
        chkAfficheInfo.setTranslateY(15);

        btnValidePano = new Button("Valide Infos");
        btnValidePano.setTranslateY(25);
        btnValidePano.setTranslateX(200);
        btnValidePano.setPadding(new Insets(7));
        btnValidePano.setOnAction((ActionEvent e) -> {
            clickBtnValidePano();
        });

        vbInfoPano.getChildren().addAll(sepInfo, lblTitrePano, txtTitrePano, sepInfo1, lblTypePano, hbTypePano, chkAfficheTitre, chkAfficheInfo, sepInfo2, btnValidePano);

        paneChoixPanoramique.getChildren().add(myAnchorPane);

        vuePanoramique = new ScrollPane();

        coordonnees = new HBox();
        pano = new Pane();
        panneau2 = new VBox();
        lblLong = new Label("");
        lblLat = new Label("");
        imagePanoramique = new ImageView();

        primaryStage.setScene(scene);

        /**
         *
         */
        vuePanoramique.setPrefSize(width - 330, height - 130);
        vuePanoramique.setMaxSize(width - 330, height - 130);
        vuePanoramique.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        vuePanoramique.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        vuePanoramique.setTranslateY(5);
        //vuePanoramique.setStyle("-fx-background-color : #c00;");
        /**
         *
         */
        panneauOutils.setContent(outils);
        panneauOutils.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        panneauOutils.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        panneauOutils.setPrefSize(320, height - 130);
        panneauOutils.setMaxSize(320, height - 130);
        panneauOutils.setTranslateY(5);
        panneauOutils.setStyle("-fx-background-color : #ccc;");
        /**
         *
         */
        pano.setCursor(Cursor.CROSSHAIR);
        outils.setPrefWidth(310);
        outils.setStyle("-fx-background-color : #ccc;");
        outils.minHeight(height - 130);
        lblLong.setStyle(labelStyle);
        lblLat.setStyle(labelStyle);
        lblLong.setPrefSize(100, 15);
        lblLat.setPrefSize(100, 15);
        lblLat.setTranslateX(50);
        panneau2.setStyle("-fx-background-color : #ddd;");
        panneau2.setPrefSize(width - 330, height - 140);

        imagePanoramique.setCache(true);
        if (largeurMax < 1200) {
            largeur = largeurMax;
        } else {
            largeur = 1200;
        }
        imagePanoramique.setFitWidth(largeur);
        imagePanoramique.setFitHeight(largeur / 2.0d);
        pano.getChildren().add(imagePanoramique);
        pano.setPrefSize(imagePanoramique.getFitWidth(), imagePanoramique.getFitHeight());
        pano.setMaxSize(imagePanoramique.getFitWidth(), imagePanoramique.getFitHeight());
        coordonnees.getChildren().setAll(lblLong, lblLat);
        vuePanoramique.setContent(panneau2);
        hbEnvironnement.getChildren().setAll(vuePanoramique, panneauOutils);
        root.getChildren().addAll(tabPaneEnvironnement, barreStatus);
        panneau2.getChildren().setAll(coordonnees, pano, afficheLegende());
        primaryStage.show();
        popUp.affichePopup();
        ToolTipManager ttManager;
        ttManager = ToolTipManager.sharedInstance();
        ttManager.setInitialDelay(10);
        ttManager.setReshowDelay(10);
        ttManager.setDismissDelay(10);
    }

    /**
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        File rep = new File("");
        currentDir = rep.getAbsolutePath();
        repertAppli = rep.getAbsolutePath();
        repertConfig = new File(repertAppli + File.separator + "configPV");
        txtRepertConfig = repertConfig.getAbsolutePath();
        if (!repertConfig.exists()) {
            repertConfig.mkdirs();
            locale = new Locale("fr", "FR");
            repertoireProjet = repertAppli;
        } else {
            lisFichierConfig();
        }
        rb = ResourceBundle.getBundle("editeurpanovisu.i18n.PanoVisu", locale);
        stPrincipal = primaryStage;
        stPrincipal.setTitle("PanoVisu v" + numVersion);
        setUserAgentStylesheet(STYLESHEET_MODENA);
        //setUserAgentStylesheet(repertAppli + File.separator + "test.css");
        primaryStage.setMaximized(true);
        Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int hauteur = (int) tailleEcran.getHeight() - 20;
        int largeur = (int) tailleEcran.getWidth() - 20;
        largeurMax = tailleEcran.getWidth() - 320.0d;
        creeEnvironnement(primaryStage, largeur, hauteur);

        File repertTempFile = new File(repertAppli + File.separator + "temp");
        repertTemp = repertTempFile.getAbsolutePath();

        if (!repertTempFile.exists()) {
            repertTempFile.mkdirs();
        } else {
            deleteDirectory(repertTemp);
        }
        String extTemp = genereChaineAleatoire(20);
        repertTemp = repertTemp + File.separator + "temp" + extTemp;
        repertTempFile = new File(repertTemp);
        repertTempFile.mkdirs();
        installeEvenements();
        projetsNouveau();
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            Action reponse = null;
            Localization.setLocale(locale);
            if (!dejaSauve) {
                reponse = Dialogs.create()
                        .owner(null)
                        .title("Quitter l'éditeur")
                        .masthead("ATTENTION !!! vous n'avez pas sauvegardé votre Projet")
                        .message("Voulez vous le sauver ?")
                        .showConfirm();
            }
            if (reponse == Dialog.Actions.YES) {
                try {
                    projetSauve();

                } catch (IOException ex) {
                    Logger.getLogger(EditeurPanovisu.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if ((reponse == Dialog.Actions.YES) || (reponse == Dialog.Actions.NO) || (reponse == null)) {
                try {
                    sauveHistoFichiers();
                } catch (IOException ex) {
                    Logger.getLogger(EditeurPanovisu.class.getName()).log(Level.SEVERE, null, ex);
                }

                deleteDirectory(repertTemp);
                File ftemp = new File(repertTemp);
                ftemp.delete();
            } else {
                event.consume();
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Package pack = Package.getPackage("editeurpanovisu");
        System.setProperty("file.encoding", "UTF-8");
        numVersion = pack.getImplementationVersion();
        systemeExploitation = System.getProperty("os.name");
        //System.out.println("Systeme : " + systemeExploitation);
        Properties properties = System.getProperties();
        properties.list(System.out);
        //System.out.println(numVersion);
        launch(args);
    }
}
