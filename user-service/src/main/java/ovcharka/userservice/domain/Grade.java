package ovcharka.userservice.domain;

public enum Grade {
    A("A", 4.0),
    B("B", 3.0),
    C("C", 2.0),
    D("D", 1.0),
    F("F", 0.0);

    private final String symbol;
    private final Double value;

    Grade(String symbol, Double value) {
        this.symbol = symbol;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getValue() {
        return value;
    }
}
