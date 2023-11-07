package com.example.historialbotones

class ButtonClass(
    var Nombre: String,
    var action: Int
) {
    companion object {
        val buttonList = listOf(
            ButtonClass("Button 1", 1),
            ButtonClass("Button 2", 2),
            ButtonClass("Button 3", 3),
            ButtonClass("Button 4", 4),
            ButtonClass("Button 5", 5)
        )
    }
}

fun main() {
    val buttons = ButtonClass.buttonList
    for (button in buttons) {
        println("Button Name: ${button.Nombre}, Action: ${button.action}")
    }
}








