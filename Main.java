// package compilateurnewversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
//import TPIF3PARSER.parserIF3;
//import TPIF3PARSER.anallex;

public class Main {
    public static void main(String[] args) {

        // anallex anaLex=new anallex();
        // anaLex.lecture();

        /*
         * System.out.println("remplissage du tableaullll");
         * for( int i=0; i<anaLex.fluxCaracteres.size();i++)
         *
         * System.out.println(" " +
         * anaLex.fluxCaracteres.get(i));
         */

        System.out.println("***********************Analyse lexical*************************");

        ArrayList<String> tab = new ArrayList<String>();

        Scanner anaLex = new Scanner();
        //System.out.println("alex:" + anaLex);

        UniteLexicale ul = null;
        do {
            ul = anaLex.lexemeSuivant();
            tab.add(ul.toString());
            System.out.println("ul: " + ul);
            System.out.println("tab: " + tab);

        } while (ul.getCategorie() != Categorie.EOF);

        for (int i = 0; i < tab.size(); i++)

            System.out.println(" " +
                    tab.get(i));

        int taille = anaLex.fluxCaracteres.size();
        String tab1[] = new String[taille];
        // String ss = " ";
        for (int i = 0; i < anaLex.fluxCaracteres.size(); i++) {
            // // if (!tab[i].equals(ss)) {
           // System.out.println(anaLex.fluxCaracteres);

            tab1[i] = String.valueOf(anaLex.fluxCaracteres.get(i));
            // System.out.println("kii");
            // // }
        }

        // for (int i = 0; i < taille; i++)

        // {
        // System.out.println(tab1[i]);

        // }
        System.out.println("***********************Analyse Syntaxique*************************");

        parsernew test22 = new parsernew();

        test22.analyzeSLnew();
        // test22.Action("id", "id");
    }
}
