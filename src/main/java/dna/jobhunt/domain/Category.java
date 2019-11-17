package dna.jobhunt.domain;

/**
 * Category of {@link JobOffer}
 */
public enum Category {
    IT("IT"),
    Food_And_Drink("Food & Drink"),
    Drinks("Drinks"),
    Office("Office"),
    Courier("Courier"),
    Shop_Assistant("Shop Assistant");

    String name;

    Category(String name) {
        this.name = name;
    }
}
