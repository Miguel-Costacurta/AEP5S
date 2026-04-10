package enums;

public enum Prioridade {
    BAIXA(10),
    MEDIA(5),
    ALTA(2);

    private final int diasSLA;

    Prioridade(int diasSLA){
        this.diasSLA = diasSLA;
    }

    public int getDiasSLA(){
        return diasSLA;
    }

}
