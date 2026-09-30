package testsFonctionnels;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import cartes.Botte;
import cartes.Carte;
import cartes.JeuDeCartes;
import cartes.Type;
import jeu.Sabot;

public class TestSabot {

    public static void main(String[] args) {
        JeuDeCartes jeu = new JeuDeCartes();


        System.out.println("=== 2.a : Test avec piocher() ===");
        Sabot sabotA = new Sabot(jeu.donnerCartes());
        while (!sabotA.estVide()) {
            Carte c = sabotA.piocher();
            System.out.println("je pioche " + c);
        }

        System.out.println("\n=== 2.b : Test avec Iterator et remove() ===");
        Sabot sabotB = new Sabot(jeu.donnerCartes());
        for (Iterator<Carte> it = sabotB.iterator(); it.hasNext(); ) {
            Carte c = it.next();
            System.out.println("je pioche " + c);
            it.remove();
        }

        System.out.println("\n=== 2.c : Test des exceptions ConcurrentModification ===");
        

        Sabot sabotC1 = new Sabot(jeu.donnerCartes());
        try {
            Iterator<Carte> it = sabotC1.iterator();
            while (it.hasNext()) {
                it.next();
                sabotC1.piocher();
            }
            System.out.println("Échec test 1 : pas d'exception levée.");
        } catch (ConcurrentModificationException e) {
            System.out.println("Succès test 1 (piocher) : " + e.getClass().getSimpleName() + " bien levée.");
        }


        Sabot sabotC2 = new Sabot(jeu.donnerCartes());
        sabotC2.piocher();
        try {
            Iterator<Carte> it = sabotC2.iterator();
            while (it.hasNext()) {
                it.next();
                sabotC2.ajouterCarte(new Botte(Type.ACCIDENT));
            }
            System.out.println("Échec test 2 : pas d'exception levée.");
        } catch (ConcurrentModificationException e) {
            System.out.println("Succès test 2 (ajouterCarte) : " + e.getClass().getSimpleName() + " bien levée.");
        }
    }
}