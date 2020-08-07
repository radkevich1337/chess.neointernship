package neointernship.chess.game.model.figure.piece;

import com.fasterxml.jackson.annotation.JsonValue;
import neointernship.chess.game.model.enums.Color;

/**
 * Интерфейс (абстрактный класс) для описания шахматных фигур.
 */
public abstract class Figure {

    private final String name;
    private final char gameSymbol;
    private final Color color;
    private final int price;

    /**
     * Конструктор фигуры
     *
     * @param name       название фигуры
     * @param gameSymbol символ фигуры в игре
     * @param color      цвет
     * @param price      ценность
     */
    public Figure(final String name, final Character gameSymbol, final Color color, final int price) {
        this.name = name;
        this.gameSymbol = gameSymbol;
        this.color = color;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public char getGameSymbol() {
        return gameSymbol;
    }

    public Color getColor() {
        return color;
    }

    public int getPrice() {
        return price;
    }

    @Override
    @JsonValue
    public String toString() {
        return name + ":" + gameSymbol + ":" + color + ":" + price;
    }

    @Override
    public boolean equals(Object o) {
        return (this == o);
    }
}
