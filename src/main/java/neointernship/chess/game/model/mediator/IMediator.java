package neointernship.chess.game.model.mediator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.playmap.field.IField;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Mediator.class, name = "Mediator"),
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface IMediator {
    void updateMediator();

    IField getField(final Figure figure);

    Figure getFigure(final IField field);

    Collection<Figure> getFigures(final Color color);

    Collection<Figure> getFigures();

    Figure getKing(final Color color);

    void addNewConnection(final IField field, final Figure figure);

    void deleteConnection(final IField field);

    void clear();
}
