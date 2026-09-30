package jeu;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import cartes.Carte;

public class Sabot implements Iterable<Carte> {

    private Carte[] cartes;
    private int nbCartes;
    private int nombreOperations = 0;


    public Sabot(Carte[] cartesInitiales) {
        this.cartes = new Carte[cartesInitiales.length];
        this.nbCartes = cartesInitiales.length;
        System.arraycopy(cartesInitiales, 0, this.cartes, 0, cartesInitiales.length);
    }


    public boolean estVide() {
        return nbCartes == 0;
    }


    public void ajouterCarte(Carte carte) {
        if (nbCartes >= cartes.length) {
            throw new IndexOutOfBoundsException("Dépassement de capacité du sabot.");
        }
        cartes[nbCartes] = carte;
        nbCartes++;
        nombreOperations++;
    }

    // 1.d : Pioche la première carte en utilisant l'itérateur
    public Carte piocher() {
        Iterator<Carte> it = iterator();
        Carte cartePiochee = it.next();
        it.remove();
        return cartePiochee;
    }


    @Override
    public Iterator<Carte> iterator() {
        return new Iterateur();
    }


    private class Iterateur implements Iterator<Carte> {
        private int indiceIterateur = 0;
        private boolean nextEffectue = false;
        private int nombreOperationsReference = nombreOperations;

        private void verificationConcurrence() {
            if (nombreOperations != nombreOperationsReference) {
                throw new ConcurrentModificationException("Modification concurrente détectée !");
            }
        }

        @Override
        public boolean hasNext() {
            return indiceIterateur < nbCartes;
        }

        @Override
        public Carte next() {
            verificationConcurrence();
            if (!hasNext()) {
                throw new NoSuchElementException("Plus de cartes dans le sabot.");
            }
            Carte c = cartes[indiceIterateur];
            indiceIterateur++;
            nextEffectue = true;
            return c;
        }

        @Override
        public void remove() {
            verificationConcurrence();
            if (!nextEffectue) {
                throw new IllegalStateException("next() doit être appelé avant remove().");
            }

            for (int i = indiceIterateur - 1; i < nbCartes - 1; i++) {
                cartes[i] = cartes[i + 1];
            }
            cartes[nbCartes - 1] = null;
            nbCartes--;
            indiceIterateur--;
            nextEffectue = false;

            nombreOperations++;
            nombreOperationsReference++;
        }
    }
}