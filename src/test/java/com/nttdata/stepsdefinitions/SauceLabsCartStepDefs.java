package com.nttdata.stepsdefinitions;
import com.nttdata.steps.AppConfigSteps;
import com.nttdata.steps.SauceLabsCartSteps;
import com.nttdata.support.ScreenshotAttacher;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Y;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class SauceLabsCartStepDefs {
    private final AppConfigSteps app = new AppConfigSteps();
    private final SauceLabsCartSteps negocio = new SauceLabsCartSteps();
    private Scenario escenario;
    private String productoEsperado;
    private int unidadesEsperadas;
    private BigDecimal precioEsperado;

    @Before("@CarritoRegresion")
    public void guardarEscenario(Scenario scenario) {
        this.escenario = scenario;
    }

    @Dado("estoy en la aplicación de SauceLabs")
    public void abrirAplicacion() {
        app.iniciarAplicacion("mydemoapp.properties");
        ScreenshotAttacher.attach(escenario, "01 - Aplicacion iniciada");
    }

    @Y("valido que carguen correctamente los productos en la galeria")
    public void validarGaleria() {
        assertTrue("La galería de productos no cargó correctamente", negocio.validarGaleria());
        ScreenshotAttacher.attach(escenario, "02 - Galeria de productos cargada");
    }

    @Cuando("agrego {int} del siguiente producto {string}")
    public void agregarProducto(int unidades, String producto) {
        assertTrue("Unidades inválidas: " + unidades, unidades >= 1);
        productoEsperado = negocio.normalizarNombre(producto);
        unidadesEsperadas = unidades;
        negocio.elegirProducto(producto);
        ScreenshotAttacher.attach(escenario, "03 - Detalle: " + producto);
        precioEsperado = negocio.leerPrecio().multiply(BigDecimal.valueOf(unidades));
        negocio.elegirUnidades(unidades);
        ScreenshotAttacher.attach(escenario, "04 - Cantidad seleccionada: " + unidades);
        negocio.agregar(unidades);
        ScreenshotAttacher.attach(escenario, "05 - Badge del carrito actualizado");
    }

    @Entonces("valido el carrito de compra actualice correctamente")
    public void validarCarrito() {
        negocio.abrirCarrito();
        ScreenshotAttacher.attach(escenario,  "05 - Despues de agregar producto");
        assertEquals("Producto incorrecto en el carrito", productoEsperado,
                negocio.leerProductoCarrito());
        assertEquals("Cantidad incorrecta del producto", unidadesEsperadas,
                negocio.leerCantidadCarrito());
        assertEquals("Total de unidades incorrecto", unidadesEsperadas,
                negocio.leerTotalArticulos());
        assertEquals("Precio total incorrecto", 0,
                precioEsperado.compareTo(negocio.leerTotal()));
        System.out.println("[Carrito] PASS: " + productoEsperado + " x " +
                unidadesEsperadas + " | Total: " + precioEsperado);
        ScreenshotAttacher.attach(escenario, "07 - Carrito validado OK");
    }

    @After(value = "@CarritoRegresion", order = 0)
    public void cerrarAplicacion() {
        try {
            app.cerrarAplicacion("com.saucelabs.mydemoapp.android");
        } catch (Exception error) {
            System.err.println("[Carrito] No se pudo cerrar la sesión: " + error.getMessage());
        }
    }
}
