package sample.controllers;

import javafx.animation.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;


public class MainController {

    @FXML
    private Button btn1;
    @FXML
    private Slider sliderX;
    private static final double earthradius = 127.56;
    private static final double sunradius = earthradius*3;
    private static final double mercuryradius = earthradius*0.3829;
    private static final double venusradius = earthradius*0.949;
    private static final double moonradius = earthradius*0.25;
    private static final double marsradius = earthradius*0.533;
    private static final double jupiterradius = earthradius*2.5;
    private static final double saturnradius = earthradius*1.5;
    private static final double uranradius = earthradius*2;
    private static final double neptunradius = earthradius*2;
    private static final double plutonradius = earthradius*0.3;

    private static final double mercury_distance_to_sun = earthradius*4;
    private static final double moon_distance_to_earth = earthradius*2;
    private static final double venus_distance_to_sun = earthradius*6;
    private static final double earth_distance_to_sun = earthradius*9;
    private static final double mars_distance_to_sun = earthradius*12;
    private static final double jupiter_distance_to_sun = earthradius*17;
    private static final double saturn_distance_to_sun = earthradius*24;
    private static final double ring_distance_to_saturn = earthradius*3.25;
    private static final double uran_distance_to_sun = earthradius*28;
    private static final double neptun_distance_to_sun = earthradius*31;
    private static final double pluton_distance_to_sun = earthradius*34;


    final int stageWidth = 1900;
    final int stageHeight = 1000;
    final double centerX = stageWidth*0.75;
    final double centerY = stageHeight*0.5;
    private Stage mainStage;
    private Stage solarSystemStage;
    private Button btnTopCam;
    private Slider rotateXSlider;
    private Slider rotateYSlider;
    private Slider zoomSlider;
    private Camera camera;
    private Camera topCamera;
    private final int EARTH_ROTATION_DURATION = 5;
    private final DoubleProperty moonRevolutionAngle = new SimpleDoubleProperty(0.0);
    private final DoubleProperty earthPos = new SimpleDoubleProperty(0.0);

    public void setMainStage(Stage mainStage){
        this.mainStage = mainStage;
    }

    @FXML
    private void initialize(){
        try {

            //SUN
            Sphere sun = createSpere(sunradius,"sample/images/sun.jpg",centerX,centerY,0);
            rotate(sun,60);

            //Mercury
            Sphere mercury = createSpere(mercuryradius,"sample/images/mercury.jpg",centerX,centerY,-mercury_distance_to_sun);
            orbitRotation(mercury,mercury_distance_to_sun,10);

            //Venus
            Sphere venus = createSpere(venusradius,"sample/images/venus.jpg",centerX,centerY,-venus_distance_to_sun);
            orbitRotation(venus,venus_distance_to_sun,5);

            //Earth
            Sphere earth = createSpere(earthradius,"sample/images/earth.jpg",centerX,centerY,-earth_distance_to_sun);
            earth.toFront();
            //earth.setCullFace(CullFace.FRONT);
            orbitRotation(earth,earth_distance_to_sun,15);

            //Moon
            Sphere moon = createSpere(moonradius,"sample/images/moon.jpg",earth.translateXProperty().getValue(),
                    earth.translateYProperty().getValue(),earth.translateZProperty().getValue()-moon_distance_to_earth);
            moonRotation(moon);

            //Mars
            Sphere mars = createSpere(marsradius,"sample/images/mars.jpg",centerX,centerY,-mars_distance_to_sun);
            orbitRotation(mars,mars_distance_to_sun,6);

            //Jupiter
            Sphere jupiter = createSpere(jupiterradius,"sample/images/jupiter.jpg",centerX,centerY,-jupiter_distance_to_sun);
            orbitRotation(jupiter,jupiter_distance_to_sun,11);

            //Saturn
            Sphere saturn = createSpere(saturnradius,"sample/images/saturn.jpg",centerX,centerY,-saturn_distance_to_sun);

            Image image = new Image("sample/images/saturn.png");
            ImageView ring = new ImageView(image);
            ring.setTranslateX(saturn.getTranslateX()-500);
            ring.setTranslateY(saturn.getTranslateY()-500);
            ring.setTranslateZ(saturn.getTranslateZ());
            ring.setRotationAxis(Rotate.X_AXIS);
            ring.setRotate(90);
            ring.setFitHeight(1000);
            ring.setFitWidth(1000);
            //ring.setPreserveRatio(true);

            final Rotate ringRotationTransform = new Rotate(360,500,saturn_distance_to_sun+500,500);
            ring.getTransforms().addAll(ringRotationTransform);
            final Rotate saturnRotationTransform = new Rotate(0,0, 0,saturn_distance_to_sun,Rotate.Y_AXIS);
            saturn.getTransforms().addAll(saturnRotationTransform);
            final Timeline saturnRotationAnimation = new Timeline();
            saturnRotationAnimation.getKeyFrames().addAll(new KeyFrame(Duration.seconds(12),new KeyValue(saturnRotationTransform.angleProperty(),360)),
                    new KeyFrame(Duration.seconds(12),new KeyValue(ringRotationTransform.angleProperty(),0)));
            saturnRotationAnimation.setCycleCount(Animation.INDEFINITE);
            saturnRotationAnimation.play();

            //Uran
            Sphere uran = createSpere(uranradius,"sample/images/uranus.jpg",centerX,centerY,-uran_distance_to_sun);
            orbitRotation(uran,uran_distance_to_sun,15);

            //Neptun
            Sphere neptun = createSpere(neptunradius,"sample/images/neptune.jpg",centerX,centerY,-neptun_distance_to_sun);
            orbitRotation(neptun,neptun_distance_to_sun,18);

            //Pluton
            Sphere pluton = createSpere(plutonradius,"sample/images/pluton.jpg",centerX,centerY,-pluton_distance_to_sun);
            orbitRotation(pluton,pluton_distance_to_sun,8);

            solarSystemStage = new Stage();
            solarSystemStage.setTitle("Solar System");
            solarSystemStage.setHeight(stageHeight);
            solarSystemStage.setWidth(stageWidth);
            //solarSystemStage.setFullScreen(true);
            camera = makeTopCamera();
            //topCamera = makeTopCamera();
            Group root = new Group(sun,mercury,venus,earth,moon,mars,jupiter,saturn,ring,uran,neptun,pluton,camera); //add Nodes

            final SubScene subScene = make3DScene(root,camera);
            Scene scene = makeScene(subScene);
            solarSystemStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private Sphere createSpere(double radius, String material, double x, double y,double z){
        Sphere sphere = new Sphere(radius);
        Image map = new Image(material);
        PhongMaterial phongMaterial = new PhongMaterial(Color.BLACK,null,null,null,map);
        sphere.setMaterial(phongMaterial);
        sphere.setTranslateX(x);
        sphere.setTranslateY(y);
        sphere.setTranslateZ(z);
        return sphere;
    }

    private void rotate(Sphere sphere,int duration){
        RotateTransition rotateSunTransition = new RotateTransition(Duration.seconds(duration),sphere);
        rotateSunTransition.setFromAngle(0);
        rotateSunTransition.setToAngle(360);
        rotateSunTransition.setAxis(Rotate.Y_AXIS);
        rotateSunTransition.setInterpolator(Interpolator.LINEAR);
        rotateSunTransition.setCycleCount(Timeline.INDEFINITE);
        rotateSunTransition.play();
    }

    private void orbitRotation(Sphere sphere,double distance, int duration){
        final Rotate rotationTransform = new Rotate(0,0, 0,distance,Rotate.Y_AXIS);
        sphere.getTransforms().add(rotationTransform);
        final Timeline rotationAnimation = new Timeline();
        rotationAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(duration),new KeyValue(rotationTransform.angleProperty(),360)));
        rotationAnimation.setCycleCount(Animation.INDEFINITE);
        rotationAnimation.play();
    }


    private void moonRotation(Sphere moon) {
        final Rotate rotationTransform2 = new Rotate(0,0,0,earth_distance_to_sun+moon_distance_to_earth,Rotate.Y_AXIS);
        moon.getTransforms().addAll(rotationTransform2);
        final Timeline rotationAnimation2 = new Timeline();
        rotationAnimation2.getKeyFrames().addAll(new KeyFrame(Duration.seconds(15),new KeyValue(rotationTransform2.angleProperty(),360)));
        rotationAnimation2.setCycleCount(Animation.INDEFINITE);
        rotationAnimation2.play();

        final Rotate rotationTransform = new Rotate(0,0,0, moon_distance_to_earth,Rotate.Y_AXIS);
        moon.getTransforms().addAll(rotationTransform);
        final Timeline rotationAnimation = new Timeline();
        rotationAnimation.getKeyFrames().addAll(new KeyFrame(Duration.seconds(7),new KeyValue(rotationTransform.angleProperty(),360)));
        rotationAnimation.setCycleCount(Animation.INDEFINITE);
        rotationAnimation.play();

//            RotateTransition rotateMoonTransition = new RotateTransition(Duration.seconds(15),moon);
//            rotateMoonTransition.setFromAngle(0);
//            rotateMoonTransition.setToAngle(360);
//            rotateMoonTransition.setAxis(new Point3D(10,15,0));
//            rotateMoonTransition.setInterpolator(Interpolator.LINEAR);
//            rotateMoonTransition.setCycleCount(Timeline.INDEFINITE);
    }

    private void zoom(Node node){
        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setNode(node);
        zoomSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                scaleTransition.setByX(zoomSlider.getValue());
                scaleTransition.setByY(zoomSlider.getValue());
                scaleTransition.setByZ(zoomSlider.getValue());
            }
        });

        scaleTransition.setCycleCount(Timeline.INDEFINITE);
        scaleTransition.setInterpolator(Interpolator.LINEAR);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }

//    private void earthRotation(Sphere earth){
//        Shape pathEarth = new Ellipse(centerX,centerY,300,300);
//        PathTransition earthPathTransition = new PathTransition(Duration.seconds(15), pathEarth, earth);
//        earthPathTransition.setInterpolator(Interpolator.LINEAR);
//        earthPathTransition.setCycleCount(Timeline.INDEFINITE);
//        earthPathTransition.play();
//    }

    private Scene makeScene(SubScene subScene){

        StackPane root = new StackPane();
        rotateXSlider = getRotateXSlider();
        StackPane.setAlignment(rotateXSlider, Pos.TOP_RIGHT);
        rotateYSlider = getRotateYSlider();
        StackPane.setAlignment(rotateYSlider,Pos.TOP_RIGHT);
//        zoomSlider = getZoomSlider();
//        StackPane.setAlignment(zoomSlider,Pos.BOTTOM_RIGHT);
//        btnTopCam = makeButtonTopCam();
//        StackPane.setAlignment(btnTopCam,Pos.TOP_LEFT);
        root.getChildren().addAll(subScene,rotateXSlider,rotateYSlider);
        Scene scene = new Scene (root,stageWidth ,stageHeight , true);
        return scene;
    }

    private SubScene make3DScene(Group root,Camera camera) {
        SubScene scene = new SubScene(root, stageWidth, stageHeight, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.rgb(5, 5, 5));
        scene.setCamera(camera);
        return scene;
    }
//    private Button makeButtonTopCam(){
//        Button button = new Button();
//        button.setPrefWidth(32);
//        button.setPrefHeight(32);
//        button.setOnAction(event -> setTopCam(event));
//        return button;
//    }

//    private void setTopCam(ActionEvent event){
//        Node sourceWindow = (Node) event.getSource();
//        Scene scene = sourceWindow.getScene();
//        Camera camera = makeTopCamera();
//        scene.setCamera(camera);
//    }
    private void cameraInAction() {
        rotateXSlider.valueProperty().addListener((new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            camera.setRotationAxis(Rotate.Y_AXIS);
            camera.setRotate(-1*rotateXSlider.getValue());
            camera.setTranslateX(300.0);
            camera.setTranslateY(rotateXSlider.getValue());
            camera.setTranslateZ(rotateXSlider.getValue()-2000);
        }
        }));

        rotateYSlider.valueProperty().addListener((new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                camera.setRotationAxis(Rotate.X_AXIS);
                camera.setRotate(-1*rotateYSlider.getValue());
                camera.setTranslateY(-500);
                camera.setTranslateX(Math.sin(Math.toRadians(rotateYSlider.getValue())));
                camera.setTranslateZ(rotateYSlider.getValue()-2000);
                //camera.setTranslateZ(Math.sin(Math.toRadians(rotateYSlider.getValue())));
            }
        }));
    }

    private Slider getRotateXSlider() {
        Slider slider = new Slider(0.0, 360, 0.0);
        slider.setPadding(new Insets(20, stageWidth*0.9 , 20, 0));
        slider.setBlockIncrement(5.0);
        slider.setShowTickMarks(true);
        slider.setOrientation(Orientation.HORIZONTAL);
        slider.setTranslateX(50);
        slider.setTranslateY(30);
        return slider;
    }

    private Camera makeTopCamera(){
        Camera cam = new PerspectiveCamera(false);
        cam.setTranslateX(100);
        cam.setTranslateY(15000);
        cam.setTranslateZ(0);
        cam.setRotationAxis(Rotate.X_AXIS);
        cam.setRotate(90);
        return cam;
    }

    private Slider getRotateYSlider() {
        Slider slider = new Slider(0.0, 360, 0.0);
        slider.setPadding(new Insets(0, stageWidth*0.98, stageHeight*0.79, 0));
        slider.setBlockIncrement(5.0);
        slider.setShowTickMarks(true);
        slider.setOrientation(Orientation.VERTICAL);
        slider.setTranslateX(50);
        slider.setTranslateY(100);
        return slider;
    }

    private Slider getZoomSlider(){
        Slider slider = new Slider(-50,50,0);
        slider.setPadding(new Insets(stageWidth*0.9,stageWidth*0.9,20,20));
        slider.setBlockIncrement(1);
        slider.setShowTickMarks(true);
        slider.setOrientation(Orientation.HORIZONTAL);
        return slider;
    }

    private Camera makeCamera(){
        Camera cam = new PerspectiveCamera(false);
        cam.setTranslateX(0);
        cam.setTranslateY(-200);
        cam.setTranslateZ(-5000);
        cam.setFarClip(300);
        return cam;
    }

    public void action (ActionEvent actionEvent){
        cameraInAction();
        solarSystemStage.show();
    }

}
