
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main extends Application {

    ImageView[] v;
    static Button btnSpin;
    int[] credits = {6, 2, 3, 4, 7, 5};
    final static int INITIAL_CREDITS = 10;
    static int currentCreditBalance = INITIAL_CREDITS;
    private static int numberOfWins = 0;
    private static int numberOfLosses = 0;
    private static int numberOfGames = 0;
    private int averageNoOfCredits = 0;
    static int sum = 0;
    static int currentBet = 0;
    Stage window;
    static String fileName = null;
        boolean isLbl1Clicked, isLbl2Clicked, isLbl3Clicked;

    Scene scene, secondScene, thirdScene;
    BorderPane thirdPane;
    Label lblText1, lblText2, lblText3;
    Label t, tBet;
    static int index1, index2, index3;
    static boolean isBetMaxClicked = false;
    VBox vBox;
    List<Integer> numberOfBetsPerGame = new ArrayList<>();
    static int displayGivenCredits = 0;
    static int getBetMaxClickCount = 0;
    MediaPlayer mediaPlayer;

    //Display an alert when the player tries to play the game when credits is zero
    public void gameOverAlert() {
        FileInputStream gameOver = null;
        ImageView v = null;

        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            gameOver = new FileInputStream("D:\\Practice\\src\\S\\game over animation.gif");
            v = new ImageView(new Image(gameOver));
            v.setFitWidth(400);
            v.setFitHeight(300);
            alert.setTitle("WARNING"); //Specify the title of the alert window
            alert.setHeaderText(null); //Specify there is no content in the header section
            alert.setGraphic(v); //Set a graphic to the alert window
            alert.setContentText("Your credits are over! Start a new game."); //Set the content of the alert box
            alert.showAndWait();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //Display an alert when the player wins the game
    public void gameWonAlert() {
        String soundFile = "D:\\Practice\\src\\S\\Clapping Sound Effects.mp4";
        Media sound = new Media(new File(soundFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog"); //Set the title for the alert box
        alert.setHeaderText(null); //Specify there is no content in the header section
        alert.setContentText("You win " + displayGivenCredits + " credits!"); //Set the content of the alert box
        alert.setHeight(200);
        alert.setWidth(200);
        FileInputStream win = null;
        try {
            win = new FileInputStream("D:\\Practice\\src\\S\\congratulations.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView win1 = new ImageView(new Image(win));
        alert.setGraphic(win1);
        alert.showAndWait();
    }


    //Display an alert when the player loses the game
    public void gameLoseAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");   //Set the title for the alert box
        alert.setHeaderText(null);  //Specify there is no content in the header section
        alert.setContentText("You lose!");  //Set the content of the alert box
        alert.showAndWait();
    }


    //Add one coin to the current credit balance when the addCoin button is clicked
    public void addCoin() {
        if (currentCreditBalance == 0)
            gameOverAlert(); //Display an alert if the player tries to continue the game when the credit balance is zero
        else {
            currentCreditBalance += 1; //Add one credit to the current credit balance
        }
    }


    //Bet one coin from the current credit balance when the betOne button is clicked
    public void betOneCoin() {
        if (currentCreditBalance == 0)
            gameOverAlert(); //Display an alert if the player tries to continue the game when the credit balance is zero
        else {
            currentCreditBalance -= 1; //Bet one credit from the current credit balance
            numberOfBetsPerGame.add(1);
        }
    }


    //Bet three coins at once from the current credit balance when the betMax button is clicked
    public void betMaxCoins() {
        if (currentCreditBalance == 0) {
            gameOverAlert(); //Display an alert if the player tries to continue the game when the credit balance is zero
        } else if (currentCreditBalance < 3) {
            //Display an alert if the player clicks the betMax button when there is no sufficient credits
            Alert alertBetMax = new Alert(Alert.AlertType.WARNING);
            alertBetMax.setTitle("INSUFFICIENT CREDITS");   //Set the title for the alert box
            alertBetMax.setHeaderText(null);  //Specify there is no content in the header section
            alertBetMax.setContentText("No sufficient credits to bet!");  //Set the content of the alert box
            alertBetMax.showAndWait();
        } else {
            currentCreditBalance -= 3; //Deduct three credits from the current credit balance
            numberOfBetsPerGame.add(3);
        }
    }


    //Reset the Credit Area & the Bet Area when the reset button is clicked
    public void reset() {
        getBetMaxClickCount = 0; //To allow the player to click the bet max button again when it has been reset
        if (currentCreditBalance == 0)
            gameOverAlert(); //Display an alert if the player tries to continue the game when the credit balance is zero
        else {
            t.setText(" "); //Clear the Credit Area
            tBet.setText(" "); // Clear the Bet Atea
            updateCreditBetAreasWhenReset(); //Update the Credit & Bet Area
            currentCreditBalance = INITIAL_CREDITS; //Update the currentCreditBalance to the INITIAL_CREDITS given at the begining of the game when the reset button is clicked
            numberOfBetsPerGame.clear(); //Clear the amount of coins that has been bet and set it to zero
        }

    }


    //Find the average number of credits netted per game
    public double calculateAvgNoOfCreditsNettedPerGame() {
        double averageNoOfCreditsNettedPerGame = (double) (currentCreditBalance) / numberOfGames;
        return averageNoOfCreditsNettedPerGame;
    }


    //Display the statistics
    public void displayStatistics() {

        thirdPane = new BorderPane();
        thirdPane.setStyle("-fx-background-color: black");
        VBox buttons = new VBox();
        window.setTitle("Statistics Window");

        VBox chartArea = new VBox();
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Number Of Wins", numberOfWins),
                        new PieChart.Data("Number Of Losses", numberOfLosses),
                        new PieChart.Data("Credits netted per game", calculateAvgNoOfCreditsNettedPerGame()));

        final PieChart chart = new PieChart(pieChartData);
        Text text = new Text("STATISTICS"); //Create a text that displays the name of the game
        text.setFont(Font.font("Curlz mt", FontWeight.EXTRA_BOLD, 75));
        text.setFill(Color.CRIMSON); //Set the font color

        Label name = new Label("Average status");
        name.setFont(Font.font("Comic Sans ms", FontWeight.EXTRA_BOLD, 20));
        name.setTextFill(Color.CRIMSON);

        HBox h = new HBox();
        RadioButton a = new RadioButton("Winning more credits than losing");
        a.setFont(Font.font("Comic Sans ms", FontWeight.EXTRA_BOLD, 20));
        a.setTextFill(Color.CRIMSON);
        a.setDisable(true); //Make the radio button a read only property


        RadioButton b = new RadioButton("Losing more credits than winning");
        b.setFont(Font.font("Comic Sans ms", FontWeight.EXTRA_BOLD, 20));
        b.setTextFill(Color.CRIMSON);
        b.setDisable(true); //Make the radio button a read only property

        RadioButton c = new RadioButton("Number of wins = Number of losses");
        c.setFont(Font.font("Comic Sans ms", FontWeight.EXTRA_BOLD, 20));
        c.setTextFill(Color.CRIMSON);
        c.setDisable(true); //Make the radio button a read only property


        Label l = new Label();
        l.setText("Average number of credits netted per game "  +calculateAvgNoOfCreditsNettedPerGame());
        l.setFont(Font.font("Comic Sans ms", FontWeight.EXTRA_BOLD, 20));
        l.setTextFill(Color.CRIMSON);


        if (numberOfWins > 0 && numberOfWins > numberOfLosses) {
            a.setSelected(true);
        }
        else if(numberOfWins == numberOfLosses)
            c.setSelected(true);
        else
            b.setSelected(true);

        h.getChildren().addAll(a, b, c);
        h.setSpacing(40);
        Button saveStatistics = new Button("Save Statistics");
        saveStatistics.setFont(Font.font("Comic Sans ms", FontWeight.EXTRA_BOLD, 20));
        saveStatistics.setTextFill(Color.CRIMSON);
        saveStatistics.setPrefHeight(70);
        saveStatistics.setPrefWidth(180);
        saveStatistics.setStyle("-fx-base: coral; -fx-background-radius: 40;"); //Set the css styles to the button
        saveStatistics.setOnAction(e -> {
            if(numberOfGames == 0) {
                Alert saveAlert = new Alert(Alert.AlertType.WARNING);
                saveAlert.setHeaderText(null);
                saveAlert.setContentText("Sry! Your trying to save an empty file. Play the game to check statistics");
                saveAlert.showAndWait();
            }
            else {
                saveData();
                //Display an alert when the statistics have been saved
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Your statistics have been saved successfully!\nFile Name: " + fileName);
                alert.showAndWait();
            }

        });

        chartArea.getChildren().addAll(chart, name, h, l, saveStatistics);
        chartArea.setSpacing(10);
        chartArea.setAlignment(Pos.CENTER);


        //Defining the file path of the image
        FileInputStream home = null;
        try {
            home = new FileInputStream("D:\\Practice\\src\\S\\Home.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Create an image button
        ImageView homeView = new ImageView(new Image(home));
        homeView.setFitWidth(120);
        homeView.setFitHeight(120);

        //Creating the button
        Button btHome = new Button(" ", homeView);
        btHome.setContentDisplay(ContentDisplay.CENTER);
        btHome.setPrefHeight(170);
        btHome.setPrefWidth(170);
        btHome.setStyle("-fx-base: coral; -fx-background-radius: 60");


        btHome.setOnAction(e -> {
            numberOfGames = 0;
            numberOfWins = 0;
            numberOfLosses = 0;
            pieChartData.clear();
            getBetMaxClickCount = 0; //Let the player, start a new game so that the Bet Max button can be clicked again
            currentCreditBalance = 10; //Set the current credit balance to zero to start a new game
            numberOfBetsPerGame.clear(); //Clear all the bets to start a new game
            updateCreditBetAreasWhenReset(); //Update the credit area to the INITIAL_CREDITS & bet area to zero so the player can start a new game
            window.setScene(scene); //Go back to the home page when the home button is clicked
            window.setTitle("Slot Machine"); //Set the title of the window when the window changes

        });


        //Defining the file path of the image
        FileInputStream exit = null;
        try {
            exit = new FileInputStream("D:\\Practice\\src\\S\\exit.png"); //Giving the path of the image
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Create an image button
        ImageView exitView = new ImageView(new Image(exit));
        exitView.setFitWidth(120);
        exitView.setFitHeight(120);


        //Creating the button
        Button btExit = new Button(" ", exitView);
        btExit.setContentDisplay(ContentDisplay.CENTER);
        btExit.setPrefHeight(170);
        btExit.setPrefWidth(170);
        btExit.setStyle("-fx-base: coral;-fx-background-radius: 60;");
        btExit.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog"); //Set the title for the alert box
            alert.setHeaderText(null); //Specify there is no content in the header section
            alert.setContentText("Are you sure you want to exit?"); //Set the content of the alert box

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                System.exit(1); //Terminate the programme

        });


        //Defining the file path of the image
        FileInputStream back = null;
        try {
            back = new FileInputStream("D:\\Practice\\src\\S\\back.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Create an image button
        ImageView backView = new ImageView(new Image(back));
        backView.setFitWidth(120);
        backView.setFitHeight(120);

        //Creating the button
        Button btBack = new Button(" ", backView);
        btBack.setContentDisplay(ContentDisplay.CENTER);
        btBack.setPrefWidth(170);
        btBack.setPrefHeight(170);
        btBack.setStyle("-fx-base: coral; -fx-background-radius: 60");

        btBack.setOnAction(e -> {
            window.setScene(secondScene); //Go back to the previous page when the back button is clicked
            window.setTitle("Slot Machine"); //Set the title of the winodw when the window changes

        });


        buttons.getChildren().addAll(btHome, btExit, btBack);
        buttons.setPadding(new Insets(5));
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(50);

        thirdPane.setTop(text);
        BorderPane.setAlignment(text,Pos.TOP_CENTER);
        thirdPane.setCenter(chartArea);
        thirdPane.setRight(buttons);
        BorderPane.setAlignment(buttons, Pos.CENTER_RIGHT);
        window.setTitle("Statistics Window");
        thirdScene = new Scene(thirdPane, 1500, 1500);


    }


    //Writting statistics data to a txt file
    public void saveData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH-mm-ss");
        Date date = new Date();
        fileName = dateFormat.format(date) + ".txt";
        File file = new File(fileName);
        FileWriter fileWriter;
        try {
            try {
                if (!file.exists()) {
                    file.createNewFile(); //Create a new file if the file doesn't exist
                    try {
                        fileWriter = new FileWriter(file);
                        PrintWriter printData = new PrintWriter(fileWriter);


                        //Writting data to the file
                        printData.println("*************************STATISTICS*************************");
                        printData.println();
                        printData.println("Number Of Games                           : " + numberOfGames);
                        printData.println();
                        printData.println("Number Of Wins                            : " + numberOfWins);
                        printData.println();
                        printData.println("Number Of Losses                          : " + numberOfLosses);
                        printData.println();
                        if(numberOfWins > 0 && numberOfWins > numberOfLosses) {
                            printData.println("Average Status                            : Winning more credits than losing");
                            printData.println();
                        }
                        else if( numberOfWins == numberOfLosses) {
                            printData.println("Average Status                            : Number of wins = Number of losses");
                            printData.println();
                        }
                        else{
                            printData.println("Average Status                            : Losing more credits than winning");
                            printData.println();
                        }

                        printData.println("Total Credits Won                         : " + currentCreditBalance);
                        printData.println();
                        printData.println("Average Number Of Credits Netted Per Game : " + calculateAvgNoOfCreditsNettedPerGame());
                        printData.println();
                        printData.println("************************************************************");
                        printData.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception f) {
            f.printStackTrace();
        }
    }


    // Update the credit area when add or bet credits
    public void updateCreditAreaWhenAddOrBetCoins() {
        String updateCreditArea = currentCreditBalance + " ";
        t.setText(" "); // Clear the value in the text field before displaying the next value
        t.setText(updateCreditArea); //Update the credit area when add or bet coins
    }


    // Update the Bet Area when credits are bet
    public void updateBetAreaWhenBetOneOrMaxCoins() {
        int sum = 0;
        String updateBetArea = null;
        for (int i = 0; i < numberOfBetsPerGame.size(); i++) {
            sum += numberOfBetsPerGame.get(i);
            updateBetArea = sum + " ";
        }

        tBet.setText(" "); // Clear the value in the text field before displaying the next value
        tBet.setText(updateBetArea); //Update the credit area when bet coins
    }


    // Update the Credit Area & the Bet Area when the Reset button is being pressed
    public void updateCreditBetAreasWhenReset() {
        String resetCreditArea = INITIAL_CREDITS + " ";
        String resetBetArea = 0 + " ";
        t.setText(resetCreditArea); //Update the Credit Area with the INITIAL_CREDITS
        tBet.setText(resetBetArea); //Update the Bet Area with zero
    }


    //Give free credits when the player wins tha game
    public void addCreditsWhenWon() {
        int matchingCredit = 0;
        int credit1 = Reel.symbols[index1].getValue();
        int credit2 = Reel.symbols[index2].getValue();
        int credit3 = Reel.symbols[index3].getValue();

        if (credit1 == credit2)
            matchingCredit = credit1;
        else if (credit2 == credit3)
            matchingCredit = credit2;
        else if (credit1 == credit3)
            matchingCredit = credit1;
        else if (credit1 == credit2 && credit2 == credit3 && credit1 == credit3)
            matchingCredit = credit1;

        int bets = sum;


        currentCreditBalance += numberOfBetsPerGame.get(numberOfBetsPerGame.size() - 1) * matchingCredit;
        displayGivenCredits = numberOfBetsPerGame.get(numberOfBetsPerGame.size() - 1) * matchingCredit;
        updateCreditAreaWhenAddOrBetCoins();
    }


    //Apply styles to the buttons
    public void setStyles(Button object) {
        object.setFont(Font.font("Comic Sans ms", FontWeight.EXTRA_BOLD, 20));
        object.setTextFill(Color.CRIMSON);
        object.setPrefWidth(130);
        object.setPrefHeight(50);
        object.setStyle("-fx-base: coral; -fx-background-radius: 40;"); //Set the css styles to the button
    }





    @Override //Overriding the start methods in the Application class
    public void start(Stage primaryStage) {

        //Create the home page
        window = primaryStage;
        BorderPane root = new BorderPane(); //Create a Border pane layout
        ImageView background = null;
        try {
            background = new ImageView(new Image(new FileInputStream("D:\\Practice\\src\\S\\free coins.png")));
            background.setFitHeight(500);
            background.setFitWidth(600);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        root.setStyle("-fx-background-color:black;");

        VBox vBox = new VBox(); //Create a VBox layout

        Text text = new Text("SLOT MACHINE"); //Create a text that displays the name of the game
        text.setFont(Font.font("Curlz mt", FontWeight.EXTRA_BOLD, 120));
        text.setFill(Color.CRIMSON); //Set the font color


        //Create a fade transition to the text
        FadeTransition transition = new FadeTransition(Duration.millis(2000), text);
        transition.setCycleCount(Timeline.INDEFINITE); //Define the number of cycles the transition will be repeated
        transition.setFromValue(1.5);
        transition.setToValue(0.1);
        transition.setAutoReverse(true); //Define whether the transition is repeated or not
        transition.play(); //Start playing the transition


        //Defining the file path of the image
        FileInputStream next = null;
        try {
            next = new FileInputStream("D:\\Practice\\src\\S\\next10.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Create an image button
        ImageView nextView = new ImageView(new Image(next));
        nextView.setFitWidth(150);
        nextView.setFitHeight(150);


        //Creating the button
        Button btNext = new Button(" ", nextView);
        btNext.setContentDisplay(ContentDisplay.CENTER);
        btNext.setPrefHeight(200);
        btNext.setPrefWidth(200);
        btNext.setStyle("-fx-base: coral; -fx-background-radius: 60");

        //Moving to the second window when the Next button is clicked
        btNext.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.setTitle("Slot Machine"); //Set the title of the window when the window changes
                window.setScene(secondScene);

            }
        });


        //Defining the file path of the image
        FileInputStream exit = null;
        try {
            exit = new FileInputStream("D:\\Practice\\src\\S\\exit.png"); //Giving the path of the image
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Create an image button
        ImageView exitView = new ImageView(new Image(exit));
        exitView.setFitWidth(150);
        exitView.setFitHeight(150);


        //Creating the button
        Button btExit = new Button(" ", exitView);
        btExit.setContentDisplay(ContentDisplay.CENTER);
        btExit.setPrefWidth(210);
        btExit.setPrefHeight(210);
        btExit.setStyle("-fx-base: coral;-fx-background-radius: 60;");
        btExit.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog"); //Set the title for the alert box
            alert.setHeaderText(null); //Specify there is no content in the header section
            alert.setContentText("Are you sure you want to exit?"); //Set the content of the alert box

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.exit(1); //Terminate the programme
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        });


        vBox.getChildren().addAll(btNext, btExit); //Add the elements to the VBox layout
        vBox.setSpacing(50); //Specify the gap between two nodes in the Vbox
        vBox.setPadding(new Insets(10));
        root.setTop(text);
        root.setCenter(background);
        BorderPane.setAlignment(text, Pos.CENTER);
        root.setRight(vBox);
        BorderPane.setAlignment(vBox, Pos.CENTER_RIGHT);
        //End of the home page




        //Create the second window
        BorderPane secondaryLayout = new BorderPane();
        secondaryLayout.setStyle("-fx-background-color: black");


        HBox labels = new HBox();

        //Create three labels to hold the images
        Label lblText1 = new Label(" ");
        try {
            ImageView picture = new ImageView(new Image(new FileInputStream("D:\\Practice\\src\\S\\bell.png")));
            picture.setFitWidth(280); //Set the image width
            picture.setFitHeight(350); //Set the image height
            lblText1.setGraphic(picture); //Set the image to the label
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Label lblText2 = new Label(" ");
        try {
            ImageView picture = new ImageView(new Image(new FileInputStream("D:\\Practice\\src\\S\\redseven.png")));
            picture.setFitWidth(280);//Set the image width
            picture.setFitHeight(350); //Set the image height
            lblText2.setGraphic(picture); //Set the image to the label
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        Label lblText3 = new Label(" ");
        try {
            ImageView picture = new ImageView(new Image(new FileInputStream("D:\\Practice\\src\\S\\plum.png")));
            picture.setFitWidth(280); //Set the image width
            picture.setFitHeight(400); //Set the image height
            lblText3.setGraphic(picture); //Set the image to the label
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        labels.getChildren().addAll(lblText1, lblText2, lblText3); //Add the three labels to a hbox layout
        labels.setPadding(new Insets(50));
        labels.setSpacing(10);
        secondaryLayout.setCenter(labels);
        //BorderPane.setAlignment(labels, Pos.CENTER);


        // Display the credit area
        VBox vBox2 = new VBox();
        Label creditArea = new Label("Credit Area");
        creditArea.setFont(Font.font("Chiller", FontWeight.EXTRA_BOLD, 80));
        creditArea.setTextFill(Color.CRIMSON);
        t = new Label();
        t.setStyle("-fx-background-color:black;-fx-font-size: 50");
        t.setTextFill(Color.CORAL);
        t.setText("10"); //Display the initial credits given, in the Credit Area when the game starts

        class EditCreditAreaWhenAddOrBetCoins implements Runnable {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        updateCreditAreaWhenAddOrBetCoins();

                    }
                });
            }


        }


        // Display the Bet Area

        Label betArea = new Label("Bet Area");
        betArea.setFont(Font.font("Chiller", FontWeight.EXTRA_BOLD, 80));
        betArea.setTextFill(Color.CRIMSON);
        tBet = new Label();
        tBet.setText("0");
        tBet.setStyle("-fx-background-color: black; -fx-font-size: 50");
        tBet.setTextFill(Color.CORAL);

        class EditBetAreaWhenBetCoins implements Runnable {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        updateBetAreaWhenBetOneOrMaxCoins();
                    }
                });
            }


        }

        vBox2.getChildren().addAll(creditArea, t, betArea, tBet);
        vBox2.setSpacing(40);
        vBox2.setPadding(new Insets(10));
        secondaryLayout.setRight(vBox2);


        HBox buttons = new HBox(); //Create a hbox layout to hold the buttons


        Button addCoin = new Button("Add Coin"); //Create the addCoin button
        setStyles(addCoin); //Set styles to the button
        addCoin.setOnMouseClicked(e -> {
            addCoin();
            new Thread(new EditCreditAreaWhenAddOrBetCoins()).start(); //Let the Credit Area to be updated when the player adds one coin
        });


        Button betOne = new Button("Bet One"); //Create the betOne button
        setStyles(betOne); //Set styles to the button
        betOne.setOnAction(e -> {
            betOneCoin();
            new Thread(new EditCreditAreaWhenAddOrBetCoins()).start();  //Let the Credit Area to be updated when the player bets one coin
            new Thread(new EditBetAreaWhenBetCoins()).start(); //Let the Bet Area to be updated when the player bets one coin
        });


        Button betMax = new Button("Bet Max"); //Create the betMax button
        setStyles(betMax); //Set styles to the button
        betMax.setOnAction(e -> {
            getBetMaxClickCount++; //Increment the count each time the bet max button is clicked

            if(currentCreditBalance == 0)
                gameOverAlert();
            else if (getBetMaxClickCount == 1) {
                betMaxCoins();
                new Thread(new EditCreditAreaWhenAddOrBetCoins()).start(); //Let the Credit Area to be updated when the player bets three coin
                new Thread(new EditBetAreaWhenBetCoins()).start(); //Let the Bet Area to be updated when the player bets three coins
            }

            //Display an alert if the betMax button is clicked more than once per game
            else {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText(null);
                a.setContentText("Bet max can be clicked only once per game");
                a.showAndWait();
            }
        });


        Button reset = new Button("Reset"); //Create the reset button
        setStyles(reset); //Set styles to the button
        reset.setOnAction(e -> {
            reset(); //Let the Credit & the Bet Areas be updated when it is reset
        });


        try {
            Reel reel1 = new Reel();
            Reel reel2 = new Reel();
            Reel reel3 = new Reel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        class slot implements Runnable {
            //To avoid data corruption when a resource is accessed by multiple threads at the same time it is good to make the run method synchronized
            public synchronized void run() {

                try {
                    while (true) {
                        //Generate three random indexes and display the corresponding images
                        int index1 = (int) (Math.random() * 6);
                        int index2 = (int) (Math.random() * 6);
                        int index3 = (int) (Math.random() * 6);

                        Platform.runLater(new Runnable() { // Run from JavaFX GUI
                            @Override
                            public void run() {

                                //Get the corresponding images displayed by the random indexes and set their height & width
                                Reel.symbols[index1].getImage().setFitHeight(350);
                                Reel.symbols[index1].getImage().setFitWidth(280);
                                Reel.symbols[index2].getImage().setFitHeight(350);
                                Reel.symbols[index2].getImage().setFitWidth(280);
                                Reel.symbols[index3].getImage().setFitHeight(350);
                                Reel.symbols[index3].getImage().setFitWidth(280);


                                //Display the images generated by the random indexes
                                lblText1.setGraphic(Reel.symbols[index1].getImage());
                                lblText2.setGraphic(Reel.symbols[index2].getImage());
                                lblText3.setGraphic(Reel.symbols[index3].getImage());
                                //   Reel.symbols[index1].getValue();
                                //   Reel.symbols[index2].getValue();
                                //   Reel.symbols[index3].getValue();
                                // new Image(reel1.spin()[0].getImage());




                                  /*   v[index1].setFitWidth(100);
                                       v[index1].setFitHeight(100);
                                       v[index2].setFitWidth(100);
                                       v[index2].setFitHeight(100);
                                       v[index3].setFitWidth(100);
                                       v[index3].setFitHeight(100);*/
                                // lblText1.setGraphic(v[index1]);
                                // lblText2.setGraphic(v[index2]);
                                // lblText3.setGraphic(v[index3]);
                                // lblText2.setText(text);
                                //lblText3.setText(text);
                            }
                        });

                        Thread.sleep(400); //Let the thread sleep for a specified time
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        final Thread[] t1 = new Thread[3];


        btnSpin = new Button("Spin"); //Create the Spin button
        setStyles(btnSpin); //Set styles to the button
        btnSpin.setOnAction(e -> {
            //Give a message when the player tries to play the game when the creditbalance is zero
            if (currentCreditBalance == 0)
                gameOverAlert();

                //Check if the player has bet any coins before spinning the reels and if not display a warning
            else if (numberOfBetsPerGame.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING"); //Specify the title of the alert window
                alert.setHeaderText(null); //Specify there is no content in the header section
                alert.setContentText("Please bet credits to spin the reels!"); //Content of the alert box
                alert.showAndWait(); //Display the alert box
            }

            //Launch the threads
            else {
                //Play a sound when the spin button is clicked
                String musicFile = "C:\\Users\\Sanushi Salgado\\Downloads\\2-1-10070.mp3";
                Media sound = new Media(new File(musicFile).toURI().toString());
                mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.play();
                t1[0] = new Thread(new slot());
                t1[0].start(); //Start the thread
                t1[1] = new Thread(new slot());
                t1[1].start(); //Start the thread
                t1[2] = new Thread(new slot());
                t1[2].start(); //Start the thread

                numberOfGames++; //Increment the number of games each time the spin button is clicked
            }

        });


        Button statistics = new Button("Statistics"); //Create the statistics button
        setStyles(statistics); //Set styles to the button
        statistics.setOnAction(e -> {
            window.setTitle("Statistics Window"); //Set the title of the window when the window changes
            displayStatistics(); //Display statistics by invoking the displayStatistics() method when the statistics button is clicked
            window.setScene(thirdScene); //Move to the statistics window when the statistics button is clicked
        });


        //Defining the file path of the image
        FileInputStream exit2 = null;
        try {
            exit2 = new FileInputStream("D:\\Practice\\src\\S\\exit.png"); //Giving the path of the image
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Create an image button
        ImageView exitView2 = new ImageView(new Image(exit2));
        exitView2.setFitWidth(200);
        exitView2.setFitHeight(130);


        //Creating the button

        exitView2.setOnMouseClicked(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog"); //Set the title for the alert box
            alert.setHeaderText(null); //Specify there is no content in the header section
            alert.setContentText("Are you sure you want to exit?"); //Set the content of the alert box

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.exit(1); //Terminate the programme
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        });

        buttons.getChildren().addAll(addCoin, betOne, betMax, reset, btnSpin, statistics, exitView2);
        buttons.setSpacing(60);
        buttons.setPadding(new Insets(10));
        secondaryLayout.setBottom(buttons);
        BorderPane.setAlignment(buttons, Pos.BOTTOM_CENTER);


        secondScene = new Scene(secondaryLayout, 1300, 1300); //Add the pane to the scene
        //End of the second window


        lblText1.setOnMouseClicked(e -> {
            //Stop the three threads when a label is clicked
            t1[0].stop();
            t1[1].stop();
            t1[2].stop();
            mediaPlayer.stop();
            isLbl1Clicked = true;

            if (lblText1.getGraphic() == lblText2.getGraphic() || lblText1.getGraphic() == lblText3.getGraphic() || lblText2.getGraphic() == lblText3.getGraphic()) {
                addCreditsWhenWon();
                gameWonAlert();
                numberOfWins++;
            } else if (lblText1.getGraphic() == lblText2.getGraphic() && lblText1.getGraphic() == lblText3.getGraphic() && lblText2.getGraphic() == lblText3.getGraphic()) {
                addCreditsWhenWon();
                gameWonAlert();
                numberOfWins++;
            } else {
                gameLoseAlert();
                numberOfLosses++;
            }

            numberOfBetsPerGame.clear();

            tBet.setText("0"); //Update the Bet Area before each new game
            getBetMaxClickCount = 0; //Set the getBetMaxClickCount to zero so that the player will be able to click the bet max button in each new game

        });


        lblText2.setOnMouseClicked(e -> {
            //Stop the three threads when a label is clicked
            t1[0].stop();
            t1[1].stop();
            t1[2].stop();
            mediaPlayer.stop();
            isLbl2Clicked = true;

            if (lblText1.getGraphic() == lblText2.getGraphic() || lblText1.getGraphic() == lblText3.getGraphic() || lblText2.getGraphic() == lblText3.getGraphic()) {
                addCreditsWhenWon();
                gameWonAlert();
                numberOfWins++;
            } else if (lblText1.getGraphic() == lblText2.getGraphic() && lblText1.getGraphic() == lblText3.getGraphic() && lblText2.getGraphic() == lblText3.getGraphic()) {
                addCreditsWhenWon();
                gameWonAlert();
                numberOfWins++;
            } else {
                gameLoseAlert();
                numberOfLosses++;
            }
            numberOfBetsPerGame.clear();

            tBet.setText("0"); //Update the Bet Area before each new game
            getBetMaxClickCount = 0; //Set the getBetMaxClickCount to zero so that the player will be able to click the bet max button in each new game

        });


        lblText3.setOnMouseClicked(e -> {
            //Stop the three threads when a label is clicked
            t1[0].stop();
            t1[1].stop();
            t1[2].stop();
            mediaPlayer.stop();
            isLbl3Clicked = true;

            if (lblText1.getGraphic() == lblText2.getGraphic() || lblText1.getGraphic() == lblText3.getGraphic() || lblText2.getGraphic() == lblText3.getGraphic()) {
                addCreditsWhenWon();
                gameWonAlert();
                numberOfWins++;
            } else if (lblText1.getGraphic() == lblText2.getGraphic() && lblText1.getGraphic() == lblText3.getGraphic() && lblText2.getGraphic() == lblText3.getGraphic()) {
                addCreditsWhenWon();
                gameWonAlert();
                numberOfWins++;
            } else {
                gameLoseAlert();
                numberOfLosses++;
            }

            numberOfBetsPerGame.clear();

            tBet.setText("0"); //Update the Bet Area before each new game
            getBetMaxClickCount = 0; //Set the getBetMaxClickCount to zero so that the player will be able to click the bet max button in each new game


        });


        //Display an alert when the game is over
        if(isLbl1Clicked && isLbl2Clicked && isLbl3Clicked)
            gameOverAlert();


        scene = new Scene(root, 1000, 1000);
        window.setTitle("WELCOME"); //Set the title of the winodw
        window.setScene(scene); //Add the scene to the stage
        window.show(); //Display the primary stage


    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
