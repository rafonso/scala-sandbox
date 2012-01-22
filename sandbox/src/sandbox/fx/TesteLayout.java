/**
 * 
 */
package sandbox.fx;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TesteLayout extends Application {

	private Button buttonTop = new Button("Top");
	private Button buttonCenter = new Button("Center");
	private Button buttonBottom = new Button("Bottom");
	private Button buttonLeft = new Button("Left");
	private Button buttonRight = new Button("Right");

	private Pane getBorderPane() {
		BorderPane pane = new BorderPane();

		pane.setTop(buttonTop);
		BorderPane.setAlignment(buttonTop, Pos.CENTER);
		BorderPane.setMargin(buttonTop, new Insets(2.5, 1, 2.5, 2.5));

		pane.setCenter(buttonCenter);
		BorderPane.setMargin(buttonCenter, new Insets(2.5, 2.5, 2.5, 2.5));

		pane.setBottom(buttonBottom);
		BorderPane.setAlignment(buttonBottom, Pos.CENTER);
		BorderPane.setMargin(buttonBottom, new Insets(2.5, 1, 1, 1));

		pane.setLeft(buttonLeft);
		// BorderPane.setAlignment(buttonBottom, Pos.CENTER);
		BorderPane.setMargin(buttonLeft, new Insets(2.5, 2.5, 2.5, 1));

		pane.setRight(buttonRight);
		// BorderPane.setAlignment(buttonBottom, Pos.CENTER);
		BorderPane.setMargin(buttonRight, new Insets(2.5, 1, 2.5, 2.5));

		return pane;
	}

	@Override
	public void start(Stage stage) throws Exception {
		Pane pane = this.getBorderPane();
		stage.setHeight(200);
		stage.setWidth(300);
		Scene scene = new Scene(pane);
		scene.setFill(Color.AQUAMARINE);

		buttonTop.setPrefWidth(stage.getWidth());
		buttonCenter.setPrefWidth(stage.getWidth());
		buttonCenter.setPrefHeight(stage.getHeight());
		buttonBottom.setPrefWidth(stage.getWidth());
		buttonLeft.setPrefHeight(stage.getHeight());
		buttonRight.setPrefHeight(stage.getHeight());

		stage.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				buttonTop.setPrefWidth(newValue.doubleValue());
				buttonCenter.setPrefWidth(newValue.doubleValue());
				buttonBottom.setPrefWidth(newValue.doubleValue());
			}
		});
		stage.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				buttonCenter.setPrefHeight(newValue.doubleValue());
				buttonRight.setPrefHeight(newValue.doubleValue());
				buttonLeft.setPrefHeight(newValue.doubleValue());
			}
		});

		stage.setTitle("Teste Layout FX");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
