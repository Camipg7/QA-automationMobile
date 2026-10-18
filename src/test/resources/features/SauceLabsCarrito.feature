# language: es
@CarritoRegresion
Característica: Validación de carrito de compras en My Demo App Android
  Como equipo de QA
  Quiero ejecutar una regresión por producto y cantidad
  Para detectar defectos antes de aprobar el APK para pruebas manuales

  Esquema del escenario: CP - Agregar <UNIDADES> unidades de <PRODUCTO> al carrito
    Dado estoy en la aplicación de SauceLabs
    Y valido que carguen correctamente los productos en la galeria
    Cuando agrego <UNIDADES> del siguiente producto "<PRODUCTO>"
    Entonces valido el carrito de compra actualice correctamente

    Ejemplos:
      | PRODUCTO                  | UNIDADES |
      | Sauce Labs Backpack       | 1        |
      | Sauce Labs Bolt - T-Shirt | 1        |
      | Sauce Labs Bike Light     | 2        |
