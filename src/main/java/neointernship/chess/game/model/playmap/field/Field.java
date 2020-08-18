package neointernship.chess.game.model.playmap.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import neointernship.chess.game.model.answer.RepositiryChar;
import neointernship.chess.game.model.enums.Color;

import java.util.Objects;

public class Field implements IField {

    private final byte x;
    private final byte y;
    private final Color color;

    /**
     * отвечает за отображение координат клетки
     */
    private static final RepositiryChar repositiryChar = new RepositiryChar();

    public Field(int x, int y) {
        this.x = (byte) x;
        this.y = (byte) y;
        color = initColor();
    }

    @JsonCreator
    public Field(final String field) {
        final String[] params = field.split(":");
        this.x = Byte.parseByte(params[0].trim());
        this.y = Byte.parseByte(params[1].trim());
        this.color = Color.parseColor(params[2].trim());
    }

    public int getXCoord() {
        return x;
    }

    public int getYCoord() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Определяет какой цвет у клетки с координатами (x,y)
     *
     * @return
     */
    private Color initColor() {
        return (x + y) % 2 == 0 ? Color.WHITE : Color.BLACK;
    }

    @Override
    public String showField() {
        final StringBuffer sb = new StringBuffer("(");
        sb.append(repositiryChar.getY(getYCoord()));
        sb.append(repositiryChar.getX(getXCoord()));
        sb.append(')');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return x == field.x &&
                y == field.y &&
                color == field.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, color);
    }

    @Override
    @JsonValue
    public String toString() {
        return x + ":" + y + ":" + color;
    }
}
