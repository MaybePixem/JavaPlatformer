
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Game extends Application implements Runnable {

    private static final float GRAVITY = 0.2f;
    private static final float JUMPHEIGHT = 4.5f;
    private static final int MAXJUMPS = 2;
    private final int WIDTH = 800, HEIGHT = 400;
    private Pane pane = new Pane();
    private boolean running = true;
    boolean canMove = true;
    LevelData lv = new LevelData();
    Thread thread = new Thread(this);
    private Rectangle playerRect = new Rectangle();
    private Player player = new Player(100, HEIGHT - 60);
    private BorderPane root = new BorderPane();
    private int jumps = 0;
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private int playerSpeed = 3;
    private List<Rectangle> objects = new ArrayList<>();
    private int LvlNumber = 0;
    private boolean nextlvl = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(root);
        pane.setMinHeight(HEIGHT);
        primaryStage.setTitle("Copyright Tim");
        playerRect.setFill(Color.WHITE);
        pane.getChildren().add(playerRect);
        playerRect.setStroke(Color.WHITE);
        playerRect.setWidth(player.getSize());
        playerRect.setHeight(player.getSize());
        pane.setMinWidth(WIDTH);
        root.setStyle("-fx-background-color: #111111;");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        root.setCenter(pane);
        primaryStage.show();
        scene.setOnKeyPressed(e -> {
            keys.put(e.getCode(), true);
        });
        scene.setOnKeyReleased(e -> {
            keys.put(e.getCode(), false);
        });
        draw();
        thread.setDaemon(true);
        thread.start();

    }

    private void tick() {

        Platform.runLater(() -> {

            if (isPressed(KeyCode.SPACE) || isPressed(KeyCode.UP)) {
                /*for (int i = 0; i < objects.size(); i++) {
                        if (player.getX() - objects.get(i).getWidth() <= objects.get(i).getX()
                                && player.getX() + player.getSize() >= objects.get(i).getX()) {

                            if (player.getY() - objects.get(i).getHeight() <= objects.get(i).getY()
                                    && player.getY() + player.getSize() + 2 >= objects.get(i).getY()) {*/
                if (jumps < MAXJUMPS) {
                    player.setMomentum( - JUMPHEIGHT);
                    jumps++;
                    keys.put(KeyCode.SPACE, false);
                    keys.put(KeyCode.UP, false);
                }

                //}

                //}

                //}
            }
            if (isPressed(KeyCode.D) || isPressed(KeyCode.RIGHT)) {
                for (int i = 0; i < objects.size(); i++) {
                    if (player.getX() + player.getSize() + playerSpeed >= objects.get(i).getX()
                            && !(objects.get(i).getX() < player.getX())) {

                        if (player.getY() - objects.get(i).getHeight() <= objects.get(i).getY()
                                && player.getY() + player.getSize() >= objects.get(i).getY()) {
                            canMove = false;
                        }
                    }
                }
                if (canMove) {
                    if (pane.getTranslateX() - playerSpeed > -(lv.getLvls()[LvlNumber][1].length() * 50) + WIDTH) {
                        player.setX(player.getX() + playerSpeed);
                        pane.setTranslateX(pane.getTranslateX() - playerSpeed);
                    } else {

                        player.setX(player.getX() + playerSpeed);
                    }

                }
                canMove = true;
            }

            if (isPressed(KeyCode.A) || isPressed(KeyCode.LEFT)) {
                for (int j = 0; j < objects.size(); j++) {
                    if (player.getX() - objects.get(j).getWidth() - playerSpeed <= objects.get(j).getX()
                            && !(objects.get(j).getX() > player.getX())) {

                        if (player.getY() - objects.get(j).getHeight() <= objects.get(j).getY()
                                && player.getY() + player.getSize() >= objects.get(j).getY()) {
                            canMove = false;
                        }
                    }
                }
                if (canMove) {
                    if (pane.getTranslateX() + playerSpeed <= 0) {
                        player.setX(player.getX() - playerSpeed);
                        pane.setTranslateX(pane.getTranslateX() + playerSpeed);
                    } else {

                        player.setX(player.getX() - playerSpeed);
                    }

                }
                canMove = true;

            }

            gravity();
            playerRect.setX(player.getX());
            playerRect.setY(player.getY());
            playerRect.setWidth(player.getSize());
            playerRect.setHeight(player.getSize());

            if (player.getY() > HEIGHT) {
                running = false;
            }
            int temp = lv.getLvls()[LvlNumber][1].length() * 50;
            if (player.getX() > temp) {
                LvlNumber++;
                running = false;
                nextlvl = true;
            }

        });

    }

    private void gravity() {
        boolean space = true;
        for (int i = 0; i < objects.size(); i++) {
            if (player.getX() - objects.get(i).getWidth() <= objects.get(i).getX()
                    && player.getX() + player.getSize() >= objects.get(i).getX()) {

                if (player.getY() - objects.get(i).getHeight() <= objects.get(i).getY()
                        && player.getY() + player.getSize() + GRAVITY + player.getMomentum() >= objects.get(i).getY()) {

                    space = false;
                }
            }
        }

        if (space || player.getMomentum() < 0) {
            player.setMomentum(player.getMomentum() + GRAVITY);
            player.setY(player.getY() + player.getMomentum());
        } else {
            player.setMomentum(0);
            jumps = 0;
        }

    }


    private void draw() {


        int temp = 50;
        int tempH = HEIGHT / lv.getLvls()[LvlNumber].length;
        int tempPos = 0;
        for (int i = 0; i < lv.getLvls()[LvlNumber].length; i++) {
            char[] tempChar = lv.getLvls()[LvlNumber][i].toCharArray();
            for (int j = 0; j < lv.getLvls()[LvlNumber][0].length(); j++) {
                if (tempChar[j] == '1') {
                    Rectangle rect = new Rectangle(tempPos, i * tempH, temp, tempH);
                    rect.setFill(Color.WHITE);
                    rect.setStroke(Color.WHITE);
                    pane.getChildren().add(rect);
                    objects.add(rect);
                    tempPos += temp;
                } else {
                    tempPos += temp;
                }
            }
            tempPos = 0;
        }

    }

    @Override
    public void run() {

        while (running) {
            tick();

            try {
                Thread.sleep(10);

            } catch (InterruptedException e) {
            }
            if (running == false) {
                gameOver();
            }
        }

    }

    public void gameOver() {
        Platform.runLater(() -> {
            Stage stage = new Stage();

            VBox pane = new VBox();
            String s2 = nextlvl ? "Congratulations!" : "Gameover";
            Text label = new Text(s2);
            String s = nextlvl ? "Next Level" : "Try Again";
            nextlvl = false;
            Button button = new Button(s);
            pane.getChildren().addAll(label, button);
            label.setFill(Color.WHITE);
            label.setFont(new Font("Verdana", 40));
            button.setPrefHeight(50);
            button.setPrefWidth(150);
            pane.setAlignment(Pos.CENTER);
            pane.setStyle("-fx-background-color: #111111;");
            pane.setMinHeight(HEIGHT - 200);
            pane.setMinWidth(WIDTH - 200);
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            button.setOnAction(e -> {
                stage.close();
                reset();

            });
            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
                    stage.close();
                    reset();
                }
            });
            stage.showAndWait();

        });
    }


    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    private void reset() {
        Platform.runLater(() -> {
            keys.clear();
            objects.clear();
            pane.getChildren().clear();
            pane.getChildren().add(playerRect);
            draw();
            player.setX(100);
            player.setY(HEIGHT - 60);
            playerRect.setX(player.getX());
            playerRect.setY(player.getY());
            pane.setTranslateX(0);
            this.running = true;
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        });
    }

}