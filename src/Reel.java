import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Reel {

    static Symbol[] symbols = new Symbol[6];

    public Reel() throws FileNotFoundException {

        Symbol bell = new Symbol(); //Create a symbol instance
        bell.setImage(new ImageView(new Image(new FileInputStream("D:\\Practice\\src\\S\\bell.png")))); //Set the image for the symbol
        bell.setValue(6); //Set the credit value for the symbol
        symbols[0] = bell;

        Symbol cherry = new Symbol(); //Create a symbol instance
        cherry.setImage(new ImageView(new Image(new FileInputStream("D:\\Practice\\src\\S\\cherry.png")))); //Set the image for the symbol
        cherry.setValue(2); //Set the credit value for the symbol
        symbols[1] = cherry;

        Symbol lemon = new Symbol(); //Create a symbol instance
        lemon.setImage(new ImageView(new Image(new FileInputStream("D:\\Practice\\src\\S\\lemon.png")))); //Set the image for the symbol
        lemon.setValue(3); //Set the credit value for the symbol
        symbols[2] = lemon;

        Symbol plum = new Symbol(); //Create a symbol instance
        plum.setImage(new ImageView(new Image(new FileInputStream("D:\\Practice\\src\\S\\plum.png")))); //Set the image for the symbol
        plum.setValue(4); //Set the credit value for the symbol
        symbols[3] = plum;

        Symbol redseven = new Symbol(); //Create a symbol instance
        redseven.setImage(new ImageView(new Image(new FileInputStream("D:\\Practice\\src\\S\\redseven.png")))); //Set the image for the symbol
        redseven.setValue(7); //Set the credit value for the symbol
        symbols[4] = redseven;

        Symbol watermelon = new Symbol(); //Create a symbol instance
        watermelon.setImage(new ImageView(new Image(new FileInputStream("D:\\Practice\\src\\S\\watermelon.png")))); //Set the image for the symbol
        watermelon.setValue(5); //Set the credit value for the symbol
        symbols[5] = watermelon;


    }

    public static Symbol[] spin() {
        return symbols;
    }


}
