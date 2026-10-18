package com.nttdata.screens;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;

import static com.nttdata.screens.AppConfigScreen.getDriver;
public class SauceLabsCartScreen {
    private static final String PKG = "com.saucelabs.mydemoapp.android:id/";
    private static final Duration TIMEOUT = Duration.ofSeconds(25);

    private final By tituloPantalla = AppiumBy.id(PKG + "productTV");
    private final By btnAgregar = AppiumBy.id(PKG + "cartBt");
    private final By btnMas = AppiumBy.accessibilityId("Increase item quantity");
    private final By badgeCarrito = AppiumBy.id(PKG + "cartTV");
    private final By btnAbrirCarrito = AppiumBy.accessibilityId("View cart");
    private final By cantidadDetalle = AppiumBy.id(PKG + "noTV");
    private final By precioDetalle = AppiumBy.id(PKG + "priceTV");
    private final By nombreEnCarrito = AppiumBy.id(PKG + "titleTV");
    private final By totalArticulos = AppiumBy.id(PKG + "itemsTV");
    private final By totalPrecio = AppiumBy.id(PKG + "totalPriceTV");

    private WebDriverWait esperar() {
        WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT);
        wait.pollingEvery(Duration.ofMillis(400));
        return wait;
    }

    private WebElement visible(By locator) {
        return esperar().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private By imagenDe(String nombreReal) {
        return AppiumBy.accessibilityId(nombreReal);
    }

    private String nombreEnApp(String nombreDelCaso) {
        return nombreDelCaso.replace(" - ", " ").trim();
    }

    public boolean galeriaCargada() {
        WebElement titulo = visible(tituloPantalla);
        WebElement producto = visible(AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"Sauce Labs Backpack\")"));
        return titulo.getText().trim().equalsIgnoreCase("Products") && producto.isDisplayed();
    }

    public void abrirProducto(String productoSolicitado) {
        String nombre = nombreEnApp(productoSolicitado);
        By etiqueta = AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + nombre + "\")");

        if (getDriver().findElements(etiqueta).stream().noneMatch(WebElement::isDisplayed)) {
            String selector = "new UiScrollable(new UiSelector().scrollable(true))" +
                    ".setMaxSearchSwipes(12).scrollIntoView(new UiSelector().text(\"" + nombre + "\"))";
            getDriver().findElement(AppiumBy.androidUIAutomator(selector));
        }

        visible(etiqueta);
        WebElement imagen = visible(imagenDe(nombre));
        imagen.click();

        visible(btnAgregar);
        String nombreDetalle = visible(tituloPantalla).getText().trim();
        if (!nombreDetalle.equalsIgnoreCase(nombre)) {
            throw new AssertionError("Producto abierto incorrecto. Esperado: " + nombre +
                    " | Obtenido: " + nombreDetalle);
        }
    }

    public BigDecimal precioUnitario() {
        return monto(visible(precioDetalle).getText());
    }

    public void seleccionarCantidad(int unidades) {
        if (unidades < 1) throw new IllegalArgumentException("Las unidades deben ser >= 1");
        int inicial = entero(visible(cantidadDetalle).getText());
        if (inicial != 1) {
            throw new AssertionError("El detalle no inició con 1 unidad. Cantidad: " + inicial);
        }
        for (int i = 1; i < unidades; i++) {
            esperar().until(ExpectedConditions.elementToBeClickable(btnMas)).click();
            int esperada = i + 1;
            esperar().until(d -> entero(d.findElement(cantidadDetalle).getText()) == esperada);
        }
        int finalCantidad = entero(visible(cantidadDetalle).getText());
        if (finalCantidad != unidades) {
            throw new AssertionError("Cantidad de detalle incorrecta: " + finalCantidad);
        }
    }

    public void agregarAlCarrito(int unidades) {
        esperar().until(ExpectedConditions.elementToBeClickable(btnAgregar)).click();esperar().until(
                ExpectedConditions.elementToBeClickable(btnAbrirCarrito)
        );
    }

    public void abrirCarrito() {
        esperar().until(ExpectedConditions.elementToBeClickable(btnAbrirCarrito)).click();
        esperar().until(d -> d.findElements(totalPrecio).stream().anyMatch(WebElement::isDisplayed));
        String titulo = visible(tituloPantalla).getText().trim();
        if (!titulo.equalsIgnoreCase("My Cart")) {
            throw new AssertionError("No se abrió My Cart. Pantalla: " + titulo);
        }
    }

    public String nombreProductoEnCarrito() {
        return visible(nombreEnCarrito).getText().trim();
    }

    public int cantidadEnCarrito() {
        return entero(visible(cantidadDetalle).getText());
    }

    public int totalUnidadesEnCarrito() {
        return entero(visible(totalArticulos).getText());
    }

    public BigDecimal precioTotalEnCarrito() {
        return monto(visible(totalPrecio).getText());
    }

    public String nombreEsperado(String nombreFeature) {
        return nombreEnApp(nombreFeature);
    }

    private int entero(String texto) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\d+").matcher(texto);
        if (!matcher.find()) throw new NoSuchElementException("No hay número en: " + texto);
        return Integer.parseInt(matcher.group());
    }

    private BigDecimal monto(String texto) {
        String limpio = texto.replaceAll("[^0-9.]", "");
        if (limpio.isEmpty()) throw new AssertionError("No es un monto válido: " + texto);
        return new BigDecimal(limpio);
    }
}
