package com.nttdata.steps;
import com.nttdata.screens.SauceLabsCartScreen;

import java.math.BigDecimal;

public class SauceLabsCartSteps {
    private final SauceLabsCartScreen screen = new SauceLabsCartScreen();

    public boolean validarGaleria() { return screen.galeriaCargada(); }
    public void elegirProducto(String producto) { screen.abrirProducto(producto); }
    public BigDecimal leerPrecio() { return screen.precioUnitario(); }
    public void elegirUnidades(int cantidad) { screen.seleccionarCantidad(cantidad); }
    public void agregar(int cantidad) { screen.agregarAlCarrito(cantidad); }
    public void abrirCarrito() { screen.abrirCarrito(); }
    public String leerProductoCarrito() { return screen.nombreProductoEnCarrito(); }
    public int leerCantidadCarrito() { return screen.cantidadEnCarrito(); }
    public int leerTotalArticulos() { return screen.totalUnidadesEnCarrito(); }
    public BigDecimal leerTotal() { return screen.precioTotalEnCarrito(); }
    public String normalizarNombre(String nombre) { return screen.nombreEsperado(nombre); }
}
