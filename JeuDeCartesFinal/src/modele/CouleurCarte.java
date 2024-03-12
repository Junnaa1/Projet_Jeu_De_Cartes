package modele;

public enum CouleurCarte {
    TREFLE(1),
    PIQUE(2),
    CARREAU(3),
    COEUR(4),
    CACHEE(0);

    private final int points;

    CouleurCarte(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public static CouleurCarte getCouleurCarteFromString(String unNomDeCouleur) {
        for (CouleurCarte couleurCarte : CouleurCarte.values()) {
            if (couleurCarte.name().equals(unNomDeCouleur)) {
                return couleurCarte;
            }
        }
        return null;
    }
}

