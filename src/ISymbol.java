import javafx.scene.image.ImageView;

public interface ISymbol {

    //Declare the abstract methods
    void setImage(ImageView i);

    ImageView getImage();

    void setValue(int value);

    int getValue();

}
