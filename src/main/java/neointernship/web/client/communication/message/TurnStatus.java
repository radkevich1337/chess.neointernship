package neointernship.web.client.communication.message;

public enum TurnStatus {
    ATTACK("Аттака"),
    AISLE_TAKE("Взятие на проходе"),
    MOVE("Движение"),
    CASTLING("Рокировка"),
    TRANSFORMATION_BEFORE("Ход пешкой на конец доски"),
    TRANSFORMATION_AFTER("Превращение"),
    Check("Проверка"),
    ERROR("Ошибочка вышла");

    private String message;

    TurnStatus(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
