package modele;

public enum NomCarte {
	AS(1),
    DEUX(2),
    TROIS(3),
    QUATRE(4),
    CINQ(5),
    SIX(6),
    SEPT(7),
    HUIT(8),
    NEUF(9),
    DIX(10),
    VALET(11),
    DAME(12),
    ROI(13);
    private final int points;

    NomCarte(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
