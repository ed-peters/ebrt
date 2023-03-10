package ebrt.media;

public record MediumInterface(Medium inside, Medium outside) {

    public MediumInterface(Medium medium) {
        this(medium, medium);
    }

    public boolean isTransition() {
        return inside != outside;
    }
}
