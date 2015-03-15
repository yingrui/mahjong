package websiteschema.mpsegment.web.api.model;

public enum DomainType {
    core(0);

    private final int value;

    private DomainType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}