package sample.objects;

import javafx.scene.image.Image;
import javafx.scene.shape.Sphere;

/**
 * Created by HP on 12.02.2017.
 */
public class Planet {


    private double radius;
    private String image_url;
    private Image image;
    private Sphere sphere;


    public Planet(double radius) {
        this.radius = radius;
        this.sphere = new Sphere(radius);
    }

    public Planet(double radius, String image_url){
        this.radius = radius;
        this.sphere = new Sphere(radius,50);
        this.image = new Image(image_url);
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setImage_url(String url){
        this.image = new Image(url);
    }

    public Image getImage(){
        return image;
    }
}
