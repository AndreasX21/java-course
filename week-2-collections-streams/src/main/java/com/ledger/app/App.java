package com.ledger.app;

interface Forma {
    double area();

    String tipo();
}

class Rectangulo implements Forma {
    private final double ancho;
    private final double alto;

    public Rectangulo(double ancho, double alto) {
        this.ancho = ancho;
        this.alto = alto;
    }

    @Override
    public double area() {
        return ancho * alto;
    }

    public double area(double escala) {
        return area() * escala;
    }

    public double perimetro() {
        return 2 * (ancho + alto);
    }

    @Override
    public String tipo() {
        return "rectangulo";
    }
}

class Cuadrado extends Rectangulo {
    public Cuadrado(double lado) {
        super(lado, lado);
    }

    @Override
    public String tipo() {
        return "cuadrado";
    }
}

class ImpresoraDeFormas {
    String formatear(Forma forma) {
        return "forma " + forma.tipo() + " area=" + forma.area();
    }

    String formatear(Rectangulo rectangulo) {
        return "rectangulo " + rectangulo.tipo() + " area=" + rectangulo.area();
    }

    String formatear(Cuadrado cuadrado) {
        return "cuadrado " + cuadrado.tipo() + " area=" + cuadrado.area();
    }
}

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        ImpresoraDeFormas printer = new ImpresoraDeFormas();
        Cuadrado square = new Cuadrado(4);
        Forma shape = square;

        System.out.println(square.area()); // 16.0
        System.out.println(square.area(2)); // 32.0
        System.out.println(shape.tipo()); // cuadrado
        // System.out.println(shape.perimetro()); // undefined
        System.out.println(printer.formatear(shape)); // forma cuadrado area=16.0
        System.out.println(printer.formatear(square)); // cuadrado cuadrado area=16.0

        // Compile time polymorphism -> method overloading
        // Runtime polymorphism -> method overriding

    }
}
