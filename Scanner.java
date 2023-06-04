

// package parserexemple;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*La ArrayListclasse est un tableau redimensionnable , qui peut être trouvé dans le java.utilpackage.

La différence entre un tableau intégré et un tableau ArrayListen Java,
est que la taille d'un tableau ne peut pas être modifiée (si vous voulez ajouter ou supprimer des éléments dans / d'un tableau,
 vous devez en créer un nouveau).
Alors que des éléments peuvent être ajoutés et supprimés d'un ArrayListfichier quand vous le souhaitez.
*/

public class Scanner {
    ArrayList<Character> fluxCaracteres;
    private int indiceCourant;
    private char caractereCourant;
    private boolean eof;

    public Scanner() {
        this("");
    }

    public Scanner(String nomFich) {
        BufferedReader f = null;
        int car = 0;
        fluxCaracteres = new ArrayList<Character>();
        indiceCourant = 0;
        eof = false;
        try {
            f = new BufferedReader(new FileReader(nomFich));
        } catch (IOException e) {
            System.out.println("taper votre texte ci-dessous (ctrl+z pour finir)");
            f = new BufferedReader(new InputStreamReader(System.in));
        }

        try {
            while ((car = f.read()) != -1)
                fluxCaracteres.add((char) car);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void caractereSuivant() {
        if (indiceCourant < fluxCaracteres.size())
            caractereCourant = fluxCaracteres.get(indiceCourant++);
        else
            eof = true;
    }

    public void reculer() {
        if (indiceCourant > 0)
            indiceCourant--;
    }

    public UniteLexicale lexemeSuivant() {
        caractereSuivant();

        while (eof || Character.isWhitespace(caractereCourant)) {
            if (eof)
                return new UniteLexicale(Categorie.EOF, "");
            caractereSuivant();
        }

        if (caractereCourant == 's')
            return getSINON();

        if (caractereCourant == 'e')
            return getENTIER();

        if (caractereCourant == 'b')
            return getBOOLEEN();

        if (caractereCourant == 'd')
            return getDEBUT();

        if (caractereCourant == 't')
            return getTABLEAU();

        if (caractereCourant == 'f')
            return FF_();

        if (caractereCourant == 'a')
            return getAFFICHER();

        if (caractereCourant == 'l')
            return getLIRE();

        if (caractereCourant == 'r')
            return getREEL();

        if (caractereCourant == 'p')
            return getPOUR();

        if (Character.isLetter(caractereCourant))
            return getID();

        if (Character.isDigit(caractereCourant))
            return getNombre();

        if (caractereCourant == ':')
            return getOPPAff();

        if (caractereCourant == ';')
            return new UniteLexicale(Categorie.PV, ";");

        if (caractereCourant == '<' || caractereCourant == '>' || caractereCourant == '=')
            return getOPPRel();

        if (caractereCourant == '+')
            return new UniteLexicale(Categorie.OPP, "+");

        if (caractereCourant == '*')
            return new UniteLexicale(Categorie.OPP, "*");
        if (caractereCourant == '-')
            return new UniteLexicale(Categorie.OPP, "-");

        if (caractereCourant == '/')
            return new UniteLexicale(Categorie.OPP, "/");

        if (caractereCourant == '"')
            return getLITTERAL();

        if (caractereCourant == '(')
            return new UniteLexicale(Categorie.PAROUV, "(");

        if (caractereCourant == ')')
            return new UniteLexicale(Categorie.PARFER, ")");

        if (caractereCourant == '[')
            return new UniteLexicale(Categorie.BAROUV, "[");

        if (caractereCourant == ']')
            return new UniteLexicale(Categorie.BARFER, "]");

        return null;
    }

    // id
    public UniteLexicale getID() {
        int etat = 0;
        StringBuffer sb = new StringBuffer();
        while (true) {
            switch (etat) {
                case 0:
                    etat = 1;
                    sb.append(caractereCourant);
                    break;
                case 1:
                    caractereSuivant();
                    if (eof)
                        etat = 3;
                    else if (Character.isLetterOrDigit(caractereCourant))
                        sb.append(caractereCourant);
                    else
                        etat = 2;
                    break;
                case 2:
                    reculer();
                    return new UniteLexicale(Categorie.ID, sb.toString());
                case 3:
                    return new UniteLexicale(Categorie.ID, sb.toString());
            }
        }
    }

    // nombre
    public UniteLexicale getNombre() {
        int etat = 0;
        StringBuffer sb = new StringBuffer();
        while (true) {
            switch (etat) {
                case 0:
                    etat = 1;
                    sb.append(caractereCourant);
                    break;
                case 1:
                    caractereSuivant();
                    if (eof)
                        etat = 3;
                    else if (Character.isDigit(caractereCourant))
                        sb.append(caractereCourant);
                    else
                        etat = 2;
                    break;
                case 2:
                    reculer();
                    return new UniteLexicale(Categorie.NB, sb.toString());
                case 3:
                    return new UniteLexicale(Categorie.NB, sb.toString());
            }
        }

    }

    // oppaff
    public UniteLexicale getOPPAff() {
        int etat = 0;
        StringBuffer sb = new StringBuffer();
        while (true) {
            switch (etat) {
                case 0:
                    if (eof)
                        break;
                    else if (caractereCourant == ':') {
                        sb.append(caractereCourant);
                        caractereSuivant();
                        etat = 1;

                    } else
                        break;

                case 1:
                    if (eof)
                        break;
                    else if (caractereCourant == '=') {
                        sb.append(caractereCourant);
                        caractereSuivant();
                        etat = 2;
                    } else if (eof || caractereCourant == ' ')
                        return new UniteLexicale(Categorie.DP, sb.toString());

                    else
                        break;

                case 2:
                    if (eof)
                        etat = 3;
                    else
                        etat = 4;
                case 3:

                    return new UniteLexicale(Categorie.OPPAff, sb.toString());
                case 4:
                    reculer();
                    return new UniteLexicale(Categorie.OPPAff, sb.toString());

            }

        }
    }

    // opprel
    public UniteLexicale getOPPRel() {
        int etat = 0;
        StringBuffer sb = new StringBuffer();
        while (true) {
            switch (etat) {
                case 0:

                    if (caractereCourant == '=') {
                        sb.append(caractereCourant);
                        caractereSuivant();
                        etat = 1;

                    } else if (caractereCourant == '>') {
                        sb.append(caractereCourant);
                        caractereSuivant();
                        etat = 2;

                    } else if (caractereCourant == '<') {
                        sb.append(caractereCourant);
                        caractereSuivant();
                        etat = 3;
                    }

                    break;

                case 1: {
                    if (eof)
                        return new UniteLexicale(Categorie.OPPRel, "EGAA");

                    else {
                        reculer();

                        return new UniteLexicale(Categorie.OPPRel, "EGAAA");
                    }
                }

                case 2:
                    // caractereSuivant();
                    if (eof) {
                        etat = 5;

                    } else if (caractereCourant == '=') {
                        sb.append(caractereCourant);
                        caractereSuivant();
                        etat = 4;

                    } else
                        etat = 5;
                    break;
                case 3:
                    if (eof)

                        etat = 8;
                    else if (caractereCourant == '=') {
                        sb.append(caractereCourant);
                        caractereSuivant();
                        etat = 6;

                    } else if (caractereCourant == '>') {
                        sb.append(caractereCourant);
                        caractereSuivant();
                        etat = 7;

                    } else
                        etat = 8;
                    break;
                case 4:
                    if (eof)
                        return new UniteLexicale(Categorie.OPPRel, "PGE");

                    else
                        reculer();
                    return new UniteLexicale(Categorie.OPPRel, "PGE");
                case 5:
                    if (eof)
                        return new UniteLexicale(Categorie.OPPRel, "PGQ");

                    else
                        reculer();
                    return new UniteLexicale(Categorie.OPPRel, "PGQ");

                case 6:
                    if (eof)
                        return new UniteLexicale(Categorie.OPPRel, "PPE");

                    else
                        reculer();
                    return new UniteLexicale(Categorie.OPPRel, "PPE");
                case 7:
                    if (eof)
                        return new UniteLexicale(Categorie.OPPRel, "DIF");

                    else
                        reculer();
                    return new UniteLexicale(Categorie.OPPRel, "DIF");
                case 8:
                    if (eof)
                        return new UniteLexicale(Categorie.OPPRel, "PPQ");

                    else
                        reculer();
                    return new UniteLexicale(Categorie.OPPRel, "PPQ");

            }

        }
    }

    // litteral
    public UniteLexicale getLITTERAL() {

        int etat = 0;
        StringBuffer sb = new StringBuffer();

        while (true) {
            switch (etat) {
                case 0: {
                    etat = 1;
                    sb.append(caractereCourant);
                }
                break;
                case 1:
                    caractereSuivant();
                    if (eof)
                        etat = 2;
                    else {
                        while (!eof && caractereCourant != '"') {

                            sb.append(caractereCourant);
                            caractereSuivant();
                        }
                        if (caractereCourant == '"') {

                            sb.append(caractereCourant);
                            return new UniteLexicale(Categorie.Litteral, sb.toString());
                        }
                    }

                    break;

                case 2:
                    return new UniteLexicale(Categorie.ERREUR, sb.toString());

            }

        }

    }

    // si sinon
    public UniteLexicale getSINON() {
        StringBuffer sb = new StringBuffer();
        int etat = 0;
        while (true) {
            switch (etat) {
                case 0: {
                    sb.append(caractereCourant);
                    etat = 1;
                    break;
                }
                case 1: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'i') {
                            sb.append(caractereCourant);
                            etat = 2;
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }

                    }
                }
                break;
                case 2: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'n') {
                            sb.append(caractereCourant);
                            etat = 3;
                        } else if (eof || caractereCourant == ' ') {
                            return new UniteLexicale(Categorie.SI, sb);
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 3: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'o') {
                            sb.append(caractereCourant);
                            etat = 4;
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 4: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'n') {
                            etat = 5;
                            sb.append(caractereCourant);
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 5: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ' || caractereCourant == ',' || caractereCourant == ';') {
                        return new UniteLexicale(Categorie.SINON, sb);
                    } else {
                        etat = 6;
                        sb.append(caractereCourant);
                    }
                }
                break;
                case 6: {
                    while (!eof && Character.isLetterOrDigit(caractereCourant)) {
                        caractereSuivant();
                        sb.append(caractereCourant);
                    }
                    return new UniteLexicale(Categorie.ID, sb);

                }

                case 7: {
                    return new UniteLexicale(Categorie.ID, sb);

                }

            }
        }
    }

    // entier
    public UniteLexicale getENTIER() {
        StringBuffer sb = new StringBuffer();
        int etat = 0;
        while (true) {
            switch (etat) {
                case 0: {
                    sb.append(caractereCourant);
                    etat = 1;
                    break;
                }
                case 1: {
                    caractereSuivant();
                    if (eof) {
                        etat = 8;
                    } else {
                        if (caractereCourant == 'n') {
                            sb.append(caractereCourant);
                            etat = 2;
                        } else {
                            etat = 7;
                            sb.append(caractereCourant);
                        }

                    }
                }
                break;
                case 2: {
                    caractereSuivant();
                    if (eof) {
                        etat = 8;
                    } else {
                        if (caractereCourant == 't') {
                            sb.append(caractereCourant);
                            etat = 3;
                        } else {
                            etat = 7;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 3: {
                    caractereSuivant();
                    if (eof) {
                        etat = 8;
                    } else {
                        if (caractereCourant == 'i') {
                            sb.append(caractereCourant);
                            etat = 4;
                        } else {
                            etat = 7;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 4: {
                    caractereSuivant();
                    if (eof) {
                        etat = 8;
                    } else {
                        if (caractereCourant == 'e') {
                            etat = 5;
                            sb.append(caractereCourant);
                        } else {
                            etat = 7;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 5: {
                    caractereSuivant();
                    if (eof) {
                        etat = 8;
                    } else {
                        if (caractereCourant == 'r') {
                            etat = 6;
                            sb.append(caractereCourant);
                        } else {
                            etat = 7;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 6: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ' || caractereCourant == ':') {
                        return new UniteLexicale(Categorie.ENTIER, sb);
                    } else {
                        etat = 7;
                        sb.append(caractereCourant);
                    }
                }
                break;
                case 7: {
                    while (!eof && Character.isLetterOrDigit(caractereCourant)) {
                        caractereSuivant();
                        sb.append(caractereCourant);
                    }
                    return new UniteLexicale(Categorie.ID, sb);

                }

                case 8: {
                    return new UniteLexicale(Categorie.ID, sb);

                }

            }
        }
    }

    // booleen
    public UniteLexicale getBOOLEEN() {
        StringBuffer sb = new StringBuffer();
        int etat = 0;
        while (true) {
            switch (etat) {
                case 0: {
                    sb.append(caractereCourant);
                    etat = 1;
                    break;
                }
                case 1: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'o') {
                            sb.append(caractereCourant);
                            etat = 2;
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }

                    }
                }
                break;
                case 2: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'o') {
                            sb.append(caractereCourant);
                            etat = 3;
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 3: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'l') {
                            sb.append(caractereCourant);
                            etat = 4;
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 4: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'e') {
                            etat = 5;
                            sb.append(caractereCourant);
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 5: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'e') {
                            etat = 6;
                            sb.append(caractereCourant);
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 6: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'n') {
                            etat = 7;
                            sb.append(caractereCourant);
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 7: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ' || caractereCourant == ':') {
                        return new UniteLexicale(Categorie.BOOLEEN, sb);
                    } else {
                        etat = 8;
                        sb.append(caractereCourant);
                    }
                }
                break;
                case 8: {
                    while (!eof && Character.isLetterOrDigit(caractereCourant)) {
                        caractereSuivant();
                        sb.append(caractereCourant);
                    }
                    return new UniteLexicale(Categorie.ID, sb);

                }

                case 9: {
                    return new UniteLexicale(Categorie.ID, sb);

                }

            }
        }
    }

    // debut de
    public UniteLexicale getDEBUT() {
        StringBuffer sb = new StringBuffer();
        int etat = 0;
        while (true) {
            switch (etat) {
                case 0: {
                    sb.append(caractereCourant);
                    etat = 1;
                    break;
                }
                case 1: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'e') {
                            sb.append(caractereCourant);
                            etat = 2;
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }

                    }
                }
                break;
                case 2: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'b') {
                            sb.append(caractereCourant);
                            etat = 3;
                        } else if (eof || caractereCourant == ' ') {
                            return new UniteLexicale(Categorie.DE, sb);
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 3: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'u') {
                            sb.append(caractereCourant);
                            etat = 4;
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 4: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 't') {
                            etat = 5;
                            sb.append(caractereCourant);
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 5: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ') {
                        return new UniteLexicale(Categorie.DEBUT, sb);
                    } else {
                        etat = 6;
                        sb.append(caractereCourant);
                    }
                }
                break;
                case 6: {
                    while (!eof && Character.isLetterOrDigit(caractereCourant)) {
                        caractereSuivant();
                        sb.append(caractereCourant);
                    }
                    return new UniteLexicale(Categorie.ID, sb);

                }

                case 7: {
                    return new UniteLexicale(Categorie.ID, sb);

                }

            }
        }
    }

    // tableau true
    public UniteLexicale getTABLEAU() {
        StringBuffer sb = new StringBuffer();
        int etat = 0;
        while (true) {
            switch (etat) {
                case 0: {
                    sb.append(caractereCourant);
                    etat = 1;
                    break;
                }
                case 1: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'a') {
                            sb.append(caractereCourant);
                            etat = 2;
                        } else if (caractereCourant == 'r') {
                            sb.append(caractereCourant);
                            etat = 10;
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }

                    }
                }
                break;
                case 2: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'b') {
                            sb.append(caractereCourant);
                            etat = 3;
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 3: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'l') {
                            sb.append(caractereCourant);
                            etat = 4;
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 4: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'e') {
                            etat = 5;
                            sb.append(caractereCourant);
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 5: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'a') {
                            etat = 6;
                            sb.append(caractereCourant);
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 6: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'u') {
                            etat = 7;
                            sb.append(caractereCourant);
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 7: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ') {
                        return new UniteLexicale(Categorie.TABLEAU, sb);
                    } else {
                        etat = 8;
                        sb.append(caractereCourant);
                    }
                }
                break;
                case 8: {
                    while (!eof && Character.isLetterOrDigit(caractereCourant)) {
                        caractereSuivant();
                        sb.append(caractereCourant);
                    }
                    return new UniteLexicale(Categorie.ID, sb);

                }

                case 9: {
                    return new UniteLexicale(Categorie.ID, sb);

                }

                case 10: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'u') {
                            etat = 11;
                            sb.append(caractereCourant);
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 11: {
                    caractereSuivant();
                    if (eof) {
                        etat = 9;
                    } else {
                        if (caractereCourant == 'e') {
                            etat = 12;
                            sb.append(caractereCourant);
                        } else {
                            etat = 8;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 12: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ') {
                        return new UniteLexicale(Categorie.TRUE, sb);
                    } else {
                        etat = 8;
                        sb.append(caractereCourant);
                    }
                }
                break;

            }
        }
    }

    // false faire fin finpour finsi
    public UniteLexicale FF_() {
        StringBuffer sb = new StringBuffer();
        int etat = 0;
        while (true) {
            switch (etat) {
                case 0: {
                    sb.append(caractereCourant);
                    etat = 1;
                    break;
                }
                case 1: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'i') {
                            sb.append(caractereCourant);
                            etat = 2;
                        } else if (caractereCourant == 'a') {
                            sb.append(caractereCourant);
                            etat = 13;
                        }

                        else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }

                    }
                }
                break;
                case 2: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'n') {
                            sb.append(caractereCourant);
                            etat = 3;
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 3: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ' || caractereCourant == ',' || caractereCourant == ';') {
                        return new UniteLexicale(Categorie.FIN, sb);
                    } else {
                        etat = 4;
                        sb.append(caractereCourant);

                    }
                }
                break;
                case 4: {
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 's') {
                            etat = 5;
                        } else if (caractereCourant == 'p') {
                            etat = 9;

                        }

                        else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 5: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'i') {
                            sb.append(caractereCourant);
                            etat = 8;
                            System.out.println("h2");

                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }

                break;
                case 6: {
                    while (!eof && Character.isLetterOrDigit(caractereCourant)) {
                        caractereSuivant();
                        sb.append(caractereCourant);
                    }
                    return new UniteLexicale(Categorie.ID, sb);
                }

                case 7: {
                    return new UniteLexicale(Categorie.ID, sb);
                }
                case 8: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ' || caractereCourant == ',' || caractereCourant == ';') {
                        System.out.println("h3");
                        return new UniteLexicale(Categorie.FINSI, sb);
                    } else {
                        etat = 6;
                        sb.append(caractereCourant);
                    }
                }
                break;
                case 9: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'o') {
                            sb.append(caractereCourant);
                            etat = 10;
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 10: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'u') {
                            sb.append(caractereCourant);
                            etat = 11;
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 11:

                {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'r') {
                            sb.append(caractereCourant);
                            etat = 12;
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 12: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ' || caractereCourant == ',' || caractereCourant == ';') {
                        return new UniteLexicale(Categorie.FINPOUR, sb);
                    } else {
                        etat = 6;
                        sb.append(caractereCourant);
                    }
                }
                break;

                case 13: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'i') {
                            sb.append(caractereCourant);
                            etat = 14;
                        } else if (caractereCourant == 'l') {
                            sb.append(caractereCourant);
                            etat = 17;
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 14: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'r') {
                            sb.append(caractereCourant);
                            etat = 15;
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 15: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'e') {
                            sb.append(caractereCourant);
                            etat = 16;
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }

                }
                break;

                case 16: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ' || caractereCourant == ',' || caractereCourant == ';') {
                        return new UniteLexicale(Categorie.FAIRE, sb);
                    } else {
                        etat = 6;
                        sb.append(caractereCourant);
                    }
                }
                break;

                case 17: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 's') {
                            sb.append(caractereCourant);
                            etat = 18;
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 18: {
                    caractereSuivant();
                    if (eof) {
                        etat = 7;
                    } else {
                        if (caractereCourant == 'e') {
                            sb.append(caractereCourant);
                            etat = 19;
                        } else {
                            etat = 6;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 19: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ' || caractereCourant == ',' || caractereCourant == ';') {
                        return new UniteLexicale(Categorie.FALSE, sb);
                    } else {
                        etat = 6;
                        sb.append(caractereCourant);
                    }
                }
                break;

            }
        }
    }

    // alors afficher
    public UniteLexicale getAFFICHER() {
        StringBuffer sb = new StringBuffer();
        int etat = 0;
        while (true) {
            switch (etat) {
                case 0: {
                    sb.append(caractereCourant);
                    etat = 1;
                    break;
                }
                case 1: {
                    caractereSuivant();
                    if (eof) {
                        etat = 10;
                    } else {
                        if (caractereCourant == 'l') {
                            etat = 11;

                        } else if (caractereCourant == 'f') {
                            sb.append(caractereCourant);
                            etat = 2;

                        } else {
                            etat = 9;
                            sb.append(caractereCourant);
                        }

                    }
                }
                break;
                case 2: {
                    caractereSuivant();
                    if (eof) {
                        etat = 10;
                    } else {
                        if (caractereCourant == 'f') {
                            sb.append(caractereCourant);
                            etat = 3;
                        } else {
                            etat = 9;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 3: {
                    caractereSuivant();
                    if (eof) {
                        etat = 10;
                    } else {
                        if (caractereCourant == 'i') {
                            sb.append(caractereCourant);
                            etat = 4;
                        } else {
                            etat = 9;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 4: {
                    caractereSuivant();
                    if (eof) {
                        etat = 10;
                    } else {
                        if (caractereCourant == 'c') {
                            etat = 5;
                            sb.append(caractereCourant);
                        } else {
                            etat = 9;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 5: {
                    caractereSuivant();
                    if (eof) {
                        etat = 10;
                    } else {
                        if (caractereCourant == 'h') {
                            etat = 6;
                            sb.append(caractereCourant);
                        } else {
                            etat = 9;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 6: {
                    caractereSuivant();
                    if (eof) {
                        etat = 10;
                    } else {
                        if (caractereCourant == 'e') {
                            etat = 7;
                            sb.append(caractereCourant);
                        } else {
                            etat = 9;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 7: {
                    caractereSuivant();
                    if (eof) {
                        etat = 10;
                    } else {
                        if (caractereCourant == 'r') {
                            etat = 8;
                            sb.append(caractereCourant);
                        } else {
                            etat = 9;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 8: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ' || caractereCourant == ':') {
                        return new UniteLexicale(Categorie.AFFICHER, sb);
                    } else {
                        etat = 9;
                        sb.append(caractereCourant);
                    }
                }
                break;
                case 9: {
                    while (!eof && Character.isLetterOrDigit(caractereCourant)) {
                        caractereSuivant();
                        sb.append(caractereCourant);
                    }
                    return new UniteLexicale(Categorie.ID, sb);

                }

                case 10: {
                    return new UniteLexicale(Categorie.ID, sb);

                }
                case 11: {
                    int etat1 = 1;
                    while (true) {
                        switch (etat1) {
                            case 1: {
                                etat1 = 2;
                                sb.append(caractereCourant);

                            }
                            break;
                            case 2: {
                                if (eof) {
                                    etat1 = 8;
                                } else {
                                    if (caractereCourant == 'l') {
                                        etat1 = 3;

                                    } else {

                                        sb.append(caractereCourant);
                                        etat1 = 7;
                                    }

                                }
                            }
                            break;
                            case 3: {
                                caractereSuivant();
                                if (eof) {
                                    etat1 = 8;
                                } else {
                                    if (caractereCourant == 'o') {

                                        sb.append(caractereCourant);
                                        etat1 = 4;
                                    } else {

                                        sb.append(caractereCourant);
                                        etat1 = 7;
                                    }

                                }
                            }
                            break;

                            case 4: {
                                caractereSuivant();
                                if (eof) {
                                    etat1 = 8;
                                } else {
                                    if (caractereCourant == 'r') {

                                        sb.append(caractereCourant);
                                        etat1 = 5;
                                    } else {

                                        sb.append(caractereCourant);
                                        etat1 = 7;
                                    }

                                }
                            }
                            break;
                            case 5: {
                                caractereSuivant();
                                if (eof) {
                                    etat1 = 8;
                                } else {
                                    if (caractereCourant == 's') {
                                        sb.append(caractereCourant);
                                        etat1 = 6;

                                    } else {

                                        sb.append(caractereCourant);
                                        etat1 = 7;
                                    }
                                }
                            }
                            break;
                            case 6: {
                                caractereSuivant();
                                if (eof || caractereCourant == ' ' || caractereCourant == ','
                                        || caractereCourant == ';') {
                                    return new UniteLexicale(Categorie.ALORS, sb);
                                } else {

                                    sb.append(caractereCourant);
                                    etat1 = 7;
                                }
                            }
                            break;
                            case 7: {
                                while (!eof && Character.isLetterOrDigit(caractereCourant)) {
                                    caractereSuivant();
                                    sb.append(caractereCourant);
                                }
                                return new UniteLexicale(Categorie.ID, sb);

                            }

                            case 8: {
                                return new UniteLexicale(Categorie.ID, sb);

                            }

                        }
                    }

                }
            }
        }
    }

    // pour
    public UniteLexicale getPOUR() {
        StringBuffer sb = new StringBuffer();
        int etat = 0;
        while (true) {
            switch (etat) {
                case 0: {
                    sb.append(caractereCourant);
                    etat = 1;
                    break;
                }
                case 1: {
                    caractereSuivant();
                    if (eof) {
                        etat = 6;
                    } else {
                        if (caractereCourant == 'o') {
                            sb.append(caractereCourant);
                            etat = 2;
                        } else {
                            etat = 5;
                            sb.append(caractereCourant);
                        }

                    }
                }
                break;
                case 2: {
                    caractereSuivant();
                    if (eof) {
                        etat = 6;
                    } else {
                        if (caractereCourant == 'u') {
                            sb.append(caractereCourant);
                            etat = 3;
                        } else {
                            etat = 5;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 3: {
                    caractereSuivant();
                    if (eof) {
                        etat = 6;
                    } else {
                        if (caractereCourant == 'r') {
                            sb.append(caractereCourant);
                            etat = 4;
                        } else {
                            etat = 5;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 4: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ' || caractereCourant == ',' || caractereCourant == ';') {
                        return new UniteLexicale(Categorie.POUR, sb);
                    } else {
                        etat = 5;
                        sb.append(caractereCourant);
                    }
                }
                break;
                case 5: {
                    while (!eof && Character.isLetterOrDigit(caractereCourant)) {
                        caractereSuivant();
                        sb.append(caractereCourant);
                    }
                    return new UniteLexicale(Categorie.ID, sb);

                }

                case 6: {
                    return new UniteLexicale(Categorie.ID, sb);

                }

            }
        }
    }

    // reel
    public UniteLexicale getREEL() {
        StringBuffer sb = new StringBuffer();
        int etat = 0;
        while (true) {
            switch (etat) {
                case 0: {
                    sb.append(caractereCourant);
                    etat = 1;
                    break;
                }
                case 1: {
                    caractereSuivant();
                    if (eof) {
                        etat = 6;
                    } else {
                        if (caractereCourant == 'e') {
                            sb.append(caractereCourant);
                            etat = 2;
                        } else {
                            etat = 5;
                            sb.append(caractereCourant);
                        }

                    }
                }
                break;
                case 2: {
                    caractereSuivant();
                    if (eof) {
                        etat = 6;
                    } else {
                        if (caractereCourant == 'e') {
                            sb.append(caractereCourant);
                            etat = 3;
                        } else {
                            etat = 5;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 3: {
                    caractereSuivant();
                    if (eof) {
                        etat = 6;
                    } else {
                        if (caractereCourant == 'l') {
                            sb.append(caractereCourant);
                            etat = 4;
                        } else {
                            etat = 5;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 4: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ' || caractereCourant == ',' || caractereCourant == ';') {
                        return new UniteLexicale(Categorie.REEL, sb);
                    } else {
                        etat = 5;
                        sb.append(caractereCourant);
                    }
                }
                break;
                case 5: {
                    while (!eof && Character.isLetterOrDigit(caractereCourant)) {
                        caractereSuivant();
                        sb.append(caractereCourant);
                    }
                    return new UniteLexicale(Categorie.ID, sb);

                }

                case 6: {
                    return new UniteLexicale(Categorie.ID, sb);

                }

            }
        }
    }

    // lire
    public UniteLexicale getLIRE() {
        StringBuffer sb = new StringBuffer();
        int etat = 0;
        while (true) {
            switch (etat) {
                case 0: {
                    sb.append(caractereCourant);
                    etat = 1;
                    break;
                }
                case 1: {
                    caractereSuivant();
                    if (eof) {
                        etat = 6;
                    } else {
                        if (caractereCourant == 'i') {
                            sb.append(caractereCourant);
                            etat = 2;
                        } else {
                            etat = 5;
                            sb.append(caractereCourant);
                        }

                    }
                }
                break;
                case 2: {
                    caractereSuivant();
                    if (eof) {
                        etat = 6;
                    } else {
                        if (caractereCourant == 'r') {
                            sb.append(caractereCourant);
                            etat = 3;
                        } else {
                            etat = 5;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;
                case 3: {
                    caractereSuivant();
                    if (eof) {
                        etat = 6;
                    } else {
                        if (caractereCourant == 'e') {
                            sb.append(caractereCourant);
                            etat = 4;
                        } else {
                            etat = 5;
                            sb.append(caractereCourant);
                        }
                    }
                }
                break;

                case 4: {
                    caractereSuivant();
                    if (eof || caractereCourant == ' ' || caractereCourant == ',' || caractereCourant == ';') {
                        return new UniteLexicale(Categorie.LIRE, sb);
                    } else {
                        etat = 5;
                        sb.append(caractereCourant);
                    }
                }
                break;
                case 5: {
                    while (!eof && Character.isLetterOrDigit(caractereCourant)) {
                        caractereSuivant();
                        sb.append(caractereCourant);
                    }
                    return new UniteLexicale(Categorie.ID, sb);

                }

                case 6: {
                    return new UniteLexicale(Categorie.ID, sb);

                }

            }
        }
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return fluxCaracteres.toString();
    }

}
