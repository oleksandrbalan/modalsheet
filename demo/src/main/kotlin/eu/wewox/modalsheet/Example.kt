package eu.wewox.modalsheet

enum class Example(
    val label: String,
    val description: String,
) {
    SimpleModalSheet(
        "Simple Modal Sheet",
        "Basic modal sheet usage"
    ),
    SheetAboveBottomBar(
        "Sheet Above Bottom Bar",
        "Showcases the fact, that modal is displayed above bottom bar"
    ),
    ScrollableModalSheet(
        "Scrollable Modal Sheet",
        "Example of scrollable modal sheet"
    ),
    CustomModalSheet(
        "Custom Modal Sheet",
        "Example of customizable input arguments"
    ),
    CustomFullScreenPopup(
        "Custom FullScreen Popup",
        "Bonus: Use internal fullscreen popup to build your own components"
    ),
}