package modele;

import java.util.Objects;

public class Carte implements Comparable<Carte> {
    private final NomCarte nom;
    private final CouleurCarte couleur;

    public Carte(NomCarte nom, CouleurCarte couleur) {
        this.nom = nom;
        this.couleur = couleur;
    }

    public NomCarte getNom() {
        return nom;
    }

    public CouleurCarte getCouleur() {
        return couleur;
    }

    public int getValeur() {
        return this.nom.getPoints();
    }

    public int getValCouleur() {
        return this.couleur.getPoints();
    }

    public int compareTo(Carte other) {
        if (this.getValeur() == other.getValeur()) {
            return Integer.compare(this.getValCouleur(), other.getValCouleur());
        }
        return Integer.compare(this.getValeur(), other.getValeur());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carte)) return false;
        Carte carte = (Carte) o;
        return nom == carte.nom && couleur == carte.couleur;
    }

    public int hashCode() {
        return Objects.hash(nom, couleur);
    }

    public String toString() {
        return "Carte " + nom.name() + " de " + couleur.name() + " (valeur : " + getValeur() + ")";
    }
    


    }
    
    
    
    
    
    
    
    
    
    
    
    
}
