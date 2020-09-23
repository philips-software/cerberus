package Shapes;

public class Circle extends Shape {
    private double radius;

    public Circle(double radius) {
        this.setRadius(radius);
    }

    public final double getRadius() {
        return this.radius;
    }

    private void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    protected double calculatePerimeter() {
        return Math.PI * 2 * this.getRadius();
    }

    @Override
    protected double calculateArea() {
        return Math.PI * this.getRadius() * this.getRadius();
    }
}
