/**
 * @author Pawel Rubin
 *
 */

package sample;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Controller {
  
  @FXML private Pane pane;
  @FXML private ColorPicker colorPicker;
  @FXML private Group group;
  @FXML private Label label;
  
  private String typeOfShape;
  private String mode = "";
  private Paint color = Color.WHITE;
  private double pressX, pressY;
  private boolean pressed = false;
  
  /**
   * Method exits program.
   */
  @FXML private void exitButton() {
    System.exit(0);
  }
  
  /**
   * Method sets typeOfShape to 1 and mode to "Creation".
   */
  @FXML private void circleButton() {
    mode = "Creation";
    typeOfShape = "Circle";
  }
  
  /**
   * Method sets typeOfShape to 2 and mode to "Creation".
   */
  @FXML private void rectangleButton() {
    mode = "Creation";
    typeOfShape = "Rectangle";
  }
  
  /**
   * Method sets typeOfShape to 3 and mode to "Creation".
   */
  @FXML private void polygonButton() {
    mode = "Creation";
    typeOfShape = "Polygon";
  }
  
  /**
   * Method sets mode to "Move".
   */
  @FXML private void editButton() {
    mode = "Edit";
  }
  
  /**
   * Method clears whole Pane
   */
  @FXML private void clearButton() {
    group.getChildren().clear();
  }
  
  /**
   * Method sets color to value of colorPicker.
   */
  @FXML private void colorPick() {
    color = colorPicker.getValue();
  }
  
  /**
   *
   */
  @FXML private void openButton() {
    FileChooser fileChooser = new FileChooser();
    File file = fileChooser.showOpenDialog(Main.primaryStage);
    if (file != null) {
      try {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        List<String> shapes = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
          shapes.add(line);
        }
        group.getChildren().clear();
        List<List<String>> partsList = new ArrayList<>();
    
        for (int i = 0; i < shapes.size(); i++) {
          List<String> parts = Arrays.asList(shapes.get(i).split(";"));
          partsList.add(parts);
          String shapeType = partsList.get(i).get(0);
      
          switch (shapeType) {
            case "Rectangle": {
              Double[] args = new Double[4];
              for (int j = 0; j < 4; j++) {
                args[j] = Double.parseDouble(partsList.get(i).get(j + 1));
              }
              Rectangle rectangle = new Rectangle();
              rectangle.setX(args[0]);
              rectangle.setY(args[1]);
              rectangle.setWidth(args[2]);
              rectangle.setHeight(args[3]);
              rectangle.setFill(Paint.valueOf(partsList.get(i).get(5)));
              group.getChildren().add(rectangle);
              break;
            }
            case "Circle": {
              Double[] args = new Double[3];
              for (int j = 0; j < 3; j++) {
                args[j] = Double.parseDouble(partsList.get(i).get(j + 1));
              }
          
              Circle circle = new Circle();
              circle.setCenterX(args[0]);
              circle.setCenterY(args[1]);
              circle.setRadius(args[2]);
              circle.setFill(Paint.valueOf(partsList.get(i).get(4)));
              group.getChildren().add(circle);
              break;
            }
            case "Polygon": {
              Double[] points = new Double[partsList.get(i).size() - 2];
              for (int j = 0; j < points.length; j++) {
                points[j] = Double.parseDouble(partsList.get(i).get(j + 1));
              }
              String fill = partsList.get(i).get(partsList.get(i).size() - 1);
              Polygon polygon = new Polygon();
              polygon.setFill(Paint.valueOf(fill));
              for (int j = 0; j < points.length; j += 2) {
                polygon.getPoints().addAll(points[j], points[j + 1]);
              }
              group.getChildren().add(polygon);
            }
          }
        }
      } catch (Exception ignored) {
    
      }
    }
  }
  
  /**
   * Method handling info button
   */
  @FXML private void infoButton() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("About");
    alert.setHeaderText("MacynPaint2137");
    alert.setContentText("This is simple graphic editor.");
  
    alert.showAndWait();
    
  }
  
  /**
   * Methods handling saving user's work to file
   */
  @FXML private void saveButton() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save your work");
    File file = fileChooser.showSaveDialog(Main.primaryStage);
    if (file != null) {
      try {
        PrintWriter printWriter = new PrintWriter(file);
        for (int i = 0; i < group.getChildren().size(); i++) {
          Shape shape = (Shape) group.getChildren().get(i);
          String typeOfShape = shape.getClass().getSimpleName();
          String shapeData = typeOfShape + ";";
          switch (typeOfShape) {
            case "Rectangle": {
              Rectangle rectangle = (Rectangle) shape;
              shapeData += rectangle.getX() + ";";
              shapeData += rectangle.getY() + ";";
              shapeData += rectangle.getWidth() + ";";
              shapeData += rectangle.getHeight() + ";";
              break;
            }
            case "Circle": {
              Circle circle = (Circle) shape;
              shapeData += circle.getCenterX() + ";";
              shapeData += circle.getCenterY() + ";";
              shapeData += circle.getRadius() + ";";
              break;
            }
            case "Polygon": {
              Polygon polygon = (Polygon) shape;
              for (int j = 0; j < polygon.getPoints().size(); j++) {
                StringBuilder stringBuilder = new StringBuilder();
                shapeData = stringBuilder.append(shapeData).append(polygon.getPoints().get(j)).append(";").toString();
              }
              break;
            }
          }
          shapeData += shape.getFill() + ";";
          System.out.println(shapeData);
          printWriter.println(shapeData);
        }
        printWriter.close();
        group.getChildren().clear();
      } catch (Exception ignored) {
      }
    }
  }
  
  /**
   * Methods handling context menu on particular shape
   *
   * @param event ContextMenuEvent on particular shape
   */
  @FXML private void contextMenu(ContextMenuEvent event) {
    if (mode.equals("Edit")) {
      ColorPicker colorPicker = new ColorPicker();
      colorPicker.setValue((Color) ((Shape) event.getTarget()).getFill());
      ContextMenu contextMenu = new ContextMenu();
      MenuItem colorButton = new MenuItem(null, colorPicker);
      colorButton.setOnAction(event1 -> ((Shape) event.getTarget()).setFill(colorPicker.getValue()));
      contextMenu.getItems().add(colorButton);
      
      Slider slider = new Slider();
      slider.setValue(1);
      MenuItem strokeButton = new MenuItem("Set Stroke", slider);
      contextMenu.getItems().add(strokeButton);
      
      strokeButton.setOnAction(event1 -> {
        ((Shape) event.getTarget()).setStroke(Color.BLACK);
        ((Shape) event.getTarget()).setStrokeWidth(slider.getValue());
      });
      
      contextMenu.show((Shape) event.getTarget(), event.getScreenX(), event.getScreenY());
      contextMenu.setAutoHide(true);
    }
  }
  
  /**
   * Method responsible to catch first mouse press
   *
   * @param event MouseEvent
   */
  @FXML private void getPress(MouseEvent event) {
    if (event.getButton().equals(MouseButton.PRIMARY)) {
      if (!pressed) {
        pressX = event.getX();
        pressY = event.getY();
        pressed = true;
      }
      group.setOnDragDetected(event1 -> pressed = false);
    }
  }
  
  /**
   * Method responsible for handling shape Creation according to MousePressed
   *
   * @param event MousePressed
   */
  @FXML private void shapeCreation(MouseEvent event) {
    if (mode.equals("Creation")) {
      switch (typeOfShape) {
        case "Circle": {
          System.out.println("adding circle");
          addCircle(event);
          break;
        }
        case "Rectangle": {
          System.out.println("adding rectangle");
          addRectangle(event);
          break;
        }
        case "Polygon": {
          System.out.println("adding polygon");
          addPolygon(event);
          break;
        }
      }
    }
  }
  
  /**
   * Method responsible for handling shape Scale according to ScrollEvent
   *
   * @param event ScrollEvent
   */
  @FXML private void shapeScale(ScrollEvent event) {
    if (mode.equals("Edit")) {
      Shape shape = (Shape) event.getTarget();
      typeOfShape = shape.getClass().getSimpleName();
      label.setText(typeOfShape);
      updateLabel(shape);
  
      // Setting scale
      double scaleFactor = 1.05;
      double deltaY = event.getDeltaY();
      if (deltaY < 0) {
        scaleFactor = 1 / scaleFactor;
      }
      Scale scale = new Scale(scaleFactor, scaleFactor, event.getX(), event.getY());
      
      switch (typeOfShape) {
        case "Circle": {
          Circle circle = (Circle) shape;
          scaleCircle(circle, scaleFactor, scale);
          break;
        }
        case "Rectangle": {
          Rectangle rectangle = (Rectangle) shape;
          scaleRectangle(rectangle, scaleFactor, scale);
          break;
        }
        case "Polygon": {
          Polygon polygon = (Polygon) shape;
          scalePolygon(polygon, scale);
          break;
        }
      }
      
    }
  }
  
  /**
   * Method responsible for handling shape Movement according to DragEvent
   *
   * @param event DragEvent
   */
  @FXML private void shapeMove(MouseEvent event) {
    if (mode.equals("Edit")) {
      Shape shape = (Shape) event.getTarget();
      typeOfShape = shape.getClass().getSimpleName();
      label.setText(typeOfShape);
      
      Translate translate = new Translate();
      translate.setX(event.getX() - pressX);
      translate.setY(event.getY() - pressY);
      
      switch (typeOfShape) {
        case "Rectangle": {
          Rectangle rectangle = (Rectangle) shape;
          moveRectangle(rectangle, translate);
          break;
        }
        case "Circle": {
          Circle circle = (Circle) shape;
          moveCircle(circle, translate);
          break;
        }
        case "Polygon": {
          Polygon polygon = (Polygon) shape;
          movePolygon(polygon, translate);
          break;
        }
      }
      
      updateLabel(shape);
      
      pressX = event.getX();
      pressY = event.getY();
    }
  }
  
  /**
   * Method creates Circle according to DragEvent
   *
   * @param event MouseEvent from Pane.
   */
  private void addCircle(MouseEvent event) {
    Circle circle = new Circle();
    circle.setCenterX(event.getX());
    circle.setCenterY(event.getY());
    circle.setFill(color);
    group.getChildren().add(circle);
  
    pane.setOnMouseDragged(event1 -> {
      double X = event1.getX() - circle.getCenterX();
      double Y = event1.getY() - circle.getCenterY();
      double radius = sqrt(X*X + Y*Y);
      circle.setRadius(radius);
    });
  
    pane.setOnMouseReleased(event1 -> pane.setOnMouseDragged(null));
  }
  
  /**
   * Method creates Rectangle according to DragEvent
   *
   * @param event MouseEvent from Pane.
   */
  private void addRectangle(MouseEvent event) {
    Rectangle rectangle = new Rectangle();
    rectangle.setX(event.getX());
    rectangle.setY(event.getY());
    rectangle.setFill(color);
    group.getChildren().add(rectangle);

    pane.setOnMouseDragged(event1 -> {
      rectangle.setHeight(abs(event1.getY()-rectangle.getY()));
      rectangle.setWidth(abs(event1.getX()-rectangle.getX()));
    });
    
    pane.setOnMouseReleased(event1 -> pane.setOnMouseDragged(null));
  }
  
  /**
   * Method creates Polygon according to MouseEvent
   *
   * @param event MouseEvent from Pane.
   */
  private void addPolygon(MouseEvent event) {
    if (event.isPrimaryButtonDown()) {
      Polygon polygon = new Polygon(event.getX(), event.getY());
      polygon.setFill(color);
      group.getChildren().add(polygon);
    } else if (event.isSecondaryButtonDown()) {
      try {
        Polygon polygon = (Polygon) group.getChildren().get(group.getChildren().size() - 1);
        polygon.getPoints().addAll(event.getX(), event.getY());
        label.setText("Number of vertices: " + polygon.getPoints().size() / 2);
      } catch (IndexOutOfBoundsException ex) {
        label.setText("No Polygons created so far");
      }
    }
  }
  
  /**
   * Methods scales given circle according to ScrollEvent
   *
   * @param circle to be scaled
   * @param scaleFactor depends on deltaY
   * @param scale depends on ScrollEvent and scaleFactor
   */
  private void scaleCircle(Circle circle, double scaleFactor, Scale scale) {
    double[] point = {circle.getCenterX(), circle.getCenterY()};
    scale.transform2DPoints(point, 0, point, 0, 1);
    circle.setCenterX(point[0]);
    circle.setCenterY(point[1]);
    circle.setRadius(circle.getRadius() * scaleFactor);
  }
  
  /**
   * Methods scales given rectangle according to ScrollEvent
   *
   * @param rectangle to be scaled
   * @param scaleFactor depends on deltaY
   * @param scale depends on ScrollEvent and scaleFactor
   */
  private void scaleRectangle(Rectangle rectangle, double scaleFactor, Scale scale) {
    double[] point = {rectangle.getX(), rectangle.getY()};
    scale.transform2DPoints(point, 0, point, 0, 1);
    rectangle.setX(point[0]);
    rectangle.setY(point[1]);
    rectangle.setHeight(rectangle.getHeight() * scaleFactor);
    rectangle.setWidth(rectangle.getWidth() * scaleFactor);
  }
  
  /**
   * Methods scales given polygon according to ScrollEvent
   *
   * @param polygon to be scaled
   * @param scale depends on ScrollEvent and scaleFactor
   */
  private void scalePolygon(Polygon polygon, Scale scale) {
    double[] points = polygon.getPoints().stream().mapToDouble(Number::doubleValue).toArray();
    scale.transform2DPoints(points, 0, points, 0, points.length / 2);
    for (int i = 0; i < points.length; i++) {
      polygon.getPoints().set(i, points[i]);
    }
  }
  
  /**
   * Method moves given circle according to DragEvent
   *
   * @param circle to be moved
   * @param translate depends on DragEvent
   */
  private void moveCircle(Circle circle, Translate translate) {
    double[] points = {circle.getCenterX(), circle.getCenterY()};
    translate.transform2DPoints(points,0, points, 0, 1);
    circle.setCenterX(points[0]);
    circle.setCenterY(points[1]);
  }
  
  /**
   * Method moves given rectangle according to DragEvent
   *
   * @param rectangle to be moved
   * @param translate depends on DragEvent
   */
  private void moveRectangle(Rectangle rectangle, Translate translate) {
    double[] points = {rectangle.getX(), rectangle.getY()};
    translate.transform2DPoints(points,0, points, 0, 1);
    rectangle.setX(points[0]);
    rectangle.setY(points[1]);
  }
  
  /**
   * Method moves given polygon according to DragEvent
   *
   * @param polygon to be moved
   * @param translate depends on DragEvent
   */
  private void movePolygon(Polygon polygon, Translate translate) {
    double[] points =  polygon.getPoints().stream().mapToDouble(Number::doubleValue).toArray();
    translate.transform2DPoints(points, 0, points, 0, points.length/2);
    for (int i = 0; i < points.length; i++) {
      polygon.getPoints().set(i, points[i]);
    }
  }
  
  /**
   * Method updates information about particular shape
   *
   * @param shape particular shape
   */
  private void updateLabel(Shape shape) {
    switch (typeOfShape) {
      case "Rectangle": {
        Rectangle rectangle = (Rectangle) shape;
        label.setText(label.getText() + " X: " + rectangle.getX() + " Y: " + rectangle.getY() + " Height: " +
                rectangle.getHeight() + " Width: " + rectangle.getWidth() + " Fill: " + rectangle.getFill());
        break;
      }
      case "Circle": {
        Circle circle = (Circle) shape;
        label.setText(label.getText() + " X: " + circle.getCenterX() + " Y: " + circle.getCenterY() + " Radius: " +
                circle.getRadius() + " Fill: " + circle.getFill());
        break;
      }
      case "Polygon": {
        Polygon polygon = (Polygon) shape;
        label.setText(label.getText() + " (first point) X: " + polygon.getPoints().get(0) + " Y: " +
                polygon.getPoints().get(1) + " Fill: " + polygon.getFill());
        break;
      }
    }
  }
}
