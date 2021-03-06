/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editeurpanovisu;

import static editeurpanovisu.EditeurPanovisu.getPanoramiquesProjet;
import static editeurpanovisu.EditeurPanovisu.getiNombrePanoramiques;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author LANG Laurent
 */
public class OrdrePanoramique {

    private static ObservableList<String> strPanos = FXCollections.observableArrayList();
    public static final ObservableList<PanoramiqueCellule> cellulesPanoramiques = FXCollections.observableArrayList();
    private final AnchorPane apListePanoramiques = new AnchorPane();
    private ListView<String> lstStrPanos;

    public void creeListe() {
        cellulesPanoramiques.clear();
        strPanos.clear();
        for (int i = 0; i < getiNombrePanoramiques(); i++) {
            PanoramiqueCellule cellPano = new PanoramiqueCellule();
            cellPano.setImgPanoramique(getPanoramiquesProjet()[i].getImgPanoRectListe());
            cellPano.setStrTitrePanoramique(getPanoramiquesProjet()[i].getStrTitrePanoramique());
            cellPano.setStrTitrePanoramiqueLigne2(getPanoramiquesProjet()[i].getStrNomFichier().substring(getPanoramiquesProjet()[i].getStrNomFichier().lastIndexOf(File.separator) + 1, getPanoramiquesProjet()[i].getStrNomFichier().length()));
            cellPano.setiNumPano(i);
            strPanos.add(Integer.toString(i));
            cellulesPanoramiques.add(cellPano);
        }
        lstStrPanos = new ListView<>(strPanos);
        lstStrPanos.setCellFactory(param -> new ListePanoramiqueCellule());
        int iTaille = 46 * (getiNombrePanoramiques()) + 5;
        iTaille = (iTaille > 465) ? 465 : iTaille;
        lstStrPanos.setMaxSize(300, iTaille);
        lstStrPanos.setMinSize(300, iTaille);
        lstStrPanos.setPrefSize(300, iTaille);
        apListePanoramiques.setMaxSize(300, iTaille);
        apListePanoramiques.setMinSize(300, iTaille);
        apListePanoramiques.setPrefSize(300, iTaille);
        apListePanoramiques.getChildren().add(lstStrPanos);
        lstStrPanos.setLayoutX(0);
        lstStrPanos.setLayoutY(0);
    }

    public void creeListe(String strOrdre) {
        String[] strOrd1 = strOrdre.split(",");
        cellulesPanoramiques.clear();
        strPanos.clear();
        for (String strOrd11 : strOrd1) {
            int iPano = Integer.parseInt(strOrd11);
            PanoramiqueCellule cellPano = new PanoramiqueCellule();
            cellPano.setImgPanoramique(getPanoramiquesProjet()[iPano].getImgPanoRectListe());
            cellPano.setStrTitrePanoramique(getPanoramiquesProjet()[iPano].getStrTitrePanoramique());
            cellPano.setStrTitrePanoramiqueLigne2(getPanoramiquesProjet()[iPano].getStrNomFichier().substring(getPanoramiquesProjet()[iPano].getStrNomFichier().lastIndexOf(File.separator) + 1, getPanoramiquesProjet()[iPano].getStrNomFichier().length()));
            cellPano.setiNumPano(iPano);
            strPanos.add(strOrd11);
            cellulesPanoramiques.add(cellPano);
        }
        lstStrPanos = new ListView<>(strPanos);
        lstStrPanos.setCellFactory(param -> new ListePanoramiqueCellule());
        int iTaille = 46 * (getiNombrePanoramiques()) + 5;
        iTaille = (iTaille > 465) ? 465 : iTaille;
        lstStrPanos.setMaxSize(300, iTaille);
        lstStrPanos.setMinSize(300, iTaille);
        lstStrPanos.setPrefSize(300, iTaille);
        apListePanoramiques.setMaxSize(300, iTaille);
        apListePanoramiques.setMinSize(300, iTaille);
        apListePanoramiques.setPrefSize(300, iTaille);
        apListePanoramiques.getChildren().add(lstStrPanos);
        lstStrPanos.setLayoutX(0);
        lstStrPanos.setLayoutY(0);
    }

    public void supprimerElement(int iElement) {
        int iTrouve = -1;
        for (int i = 0; i < strPanos.size(); i++) {
            if (strPanos.get(i).equals(Integer.toString(iElement))) {
                iTrouve = i;
            }
        }
        if (iTrouve != -1) {
            strPanos.remove(iTrouve);
            ObservableList<String> strPanos1 = strPanos;
            cellulesPanoramiques.clear();
            apListePanoramiques.getChildren().remove(lstStrPanos);
            lstStrPanos = null;
            strPanos = FXCollections.observableArrayList();
            for (int i = 0; i < strPanos1.size(); i++) {
                int iPano = Integer.parseInt(strPanos1.get(i));
                if (iPano > iElement) {
                    iPano--;
                }
                strPanos.add(Integer.toString(iPano));
                PanoramiqueCellule cellPano = new PanoramiqueCellule();
                cellPano.setImgPanoramique(getPanoramiquesProjet()[iPano].getImgPanoRectListe());
                cellPano.setStrTitrePanoramique(getPanoramiquesProjet()[iPano].getStrTitrePanoramique());
                cellPano.setStrTitrePanoramiqueLigne2(getPanoramiquesProjet()[iPano].getStrNomFichier().substring(getPanoramiquesProjet()[iPano].getStrNomFichier().lastIndexOf(File.separator) + 1, getPanoramiquesProjet()[iPano].getStrNomFichier().length()));
                cellPano.setiNumPano(iPano);
                cellulesPanoramiques.add(cellPano);
            }
            lstStrPanos = new ListView<>(strPanos);
            lstStrPanos.setCellFactory(param -> new ListePanoramiqueCellule());
            int iTaille = 46 * (strPanos.size()) + 5;
            iTaille = (iTaille > 465) ? 465 : iTaille;
            lstStrPanos.setMaxSize(300, iTaille);
            lstStrPanos.setMinSize(300, iTaille);
            lstStrPanos.setPrefSize(300, iTaille);
            apListePanoramiques.setMaxSize(300, iTaille);
            apListePanoramiques.setMinSize(300, iTaille);
            apListePanoramiques.setPrefSize(300, iTaille);
            apListePanoramiques.getChildren().add(lstStrPanos);
        }
    }

    public void rafraichitListe() {
        ObservableList<String> strPanos1 = strPanos;
        cellulesPanoramiques.clear();
        apListePanoramiques.getChildren().remove(lstStrPanos);
        lstStrPanos = null;
        strPanos = FXCollections.observableArrayList();
        for (int i = 0; i < strPanos1.size(); i++) {
            int iPano = Integer.parseInt(strPanos1.get(i));
            strPanos.add(strPanos1.get(i));
            PanoramiqueCellule cellPano = new PanoramiqueCellule();
            cellPano.setImgPanoramique(getPanoramiquesProjet()[iPano].getImgPanoRectListe());
            cellPano.setStrTitrePanoramique(getPanoramiquesProjet()[iPano].getStrTitrePanoramique());
            cellPano.setStrTitrePanoramiqueLigne2(getPanoramiquesProjet()[iPano].getStrNomFichier().substring(getPanoramiquesProjet()[iPano].getStrNomFichier().lastIndexOf(File.separator) + 1, getPanoramiquesProjet()[iPano].getStrNomFichier().length()));
            cellPano.setiNumPano(iPano);

            cellulesPanoramiques.add(cellPano);
        }
        lstStrPanos = new ListView<>(strPanos);
        lstStrPanos.setCellFactory(param -> new ListePanoramiqueCellule());
        int iTaille = 46 * (strPanos.size()) + 5;
        iTaille = (iTaille > 465) ? 465 : iTaille;
        lstStrPanos.setMaxSize(300, iTaille);
        lstStrPanos.setMinSize(300, iTaille);
        lstStrPanos.setPrefSize(300, iTaille);
        apListePanoramiques.setMaxSize(300, iTaille);
        apListePanoramiques.setMinSize(300, iTaille);
        apListePanoramiques.setPrefSize(300, iTaille);
        apListePanoramiques.getChildren().add(lstStrPanos);

    }

    public void ajouteNouveauxPanos() {
        ObservableList<String> strPanos1 = FXCollections.observableArrayList();
        int iTaillePano = 0;
        if (strPanos == null) {
            iTaillePano = 0;
            strPanos = FXCollections.observableArrayList();
        } else {
            iTaillePano = strPanos.size();
            strPanos1 = strPanos;
            strPanos = FXCollections.observableArrayList();
            for (String strPano1 : strPanos1) {
                strPanos.add(strPano1);
            }
        }
        for (int i = iTaillePano; i < getiNombrePanoramiques(); i++) {
            PanoramiqueCellule cellPano = new PanoramiqueCellule();
            cellPano.setImgPanoramique(getPanoramiquesProjet()[i].getImgPanoRectListe());
            cellPano.setStrTitrePanoramique(getPanoramiquesProjet()[i].getStrTitrePanoramique());
            cellPano.setStrTitrePanoramiqueLigne2(getPanoramiquesProjet()[i].getStrNomFichier().substring(getPanoramiquesProjet()[i].getStrNomFichier().lastIndexOf(File.separator) + 1, getPanoramiquesProjet()[i].getStrNomFichier().length()));
            cellPano.setiNumPano(i);
            strPanos.add(Integer.toString(i));
            cellulesPanoramiques.add(cellPano);
        }
        ListView lstStrPanos1 = new ListView<>(strPanos);
        lstStrPanos1.setCellFactory(param -> new ListePanoramiqueCellule());
        int iTaille = 46 * (getiNombrePanoramiques()) + 5;
        iTaille = (iTaille > 465) ? 465 : iTaille;
        lstStrPanos1.setMaxSize(300, iTaille);
        lstStrPanos1.setMinSize(300, iTaille);
        lstStrPanos1.setPrefSize(300, iTaille);
        apListePanoramiques.getChildren().clear();
        apListePanoramiques.setMaxSize(300, iTaille);
        apListePanoramiques.setMinSize(300, iTaille);
        apListePanoramiques.setPrefSize(300, iTaille);
        apListePanoramiques.getChildren().add(lstStrPanos1);
    }

    /**
     * @return the strPanos
     */
    public ObservableList<String> getStrPanos() {
        return strPanos;
    }

    /**
     * @return the cellulesPanoramiques
     */
    public ObservableList<PanoramiqueCellule> getCellulesPanoramiques() {
        return cellulesPanoramiques;
    }

    /**
     * @return the apListePanoramiques
     */
    public AnchorPane getApListePanoramiques() {
        return apListePanoramiques;
    }

}
