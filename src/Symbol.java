

import javafx.scene.image.ImageView;


public class Symbol implements ISymbol {
    //Declaring the instance variables
    private ImageView symbol;
    private int creditsGiven;

    public Symbol() {
        symbol = null;
        creditsGiven = 0;
    }

    @Override
    public void setImage(ImageView i) {
        this.symbol = i;
    }

    @Override
    public ImageView getImage() {
        return symbol;
    }

    @Override
    public void setValue(int value) {
        this.creditsGiven = value;
    }

    @Override
    public int getValue() {
        return this.creditsGiven;
    }

}
