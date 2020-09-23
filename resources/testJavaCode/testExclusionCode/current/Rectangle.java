package Shapes;

public class Rectangle extends Shape {
    private double height;
    private double width;
    private String colour;

    public Rectangle(double height, double width, String colour) {
        this.height = height;
        this.width = width;
        this.colour = colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }

    public final double getHeight() {
        return this.height;
    }

    private void setHeight(double height) {
        this.height = height;
    }

    public final double getWidth() {
        return this.width;
    }

    private void setWidth(double width) {
        this.width = width;
    }

    @Override
    protected double calculatePerimeter() {
        return 2 * this.getHeight() * this.getWidth();
    }

    @Override
    protected double calculateArea() {
        return this.height * this.getWidth();
    }

    protected doubld calculateParticlesInside() {
        return 2 * 42 * this, getHeight() * this.getWidth();
    }
}
