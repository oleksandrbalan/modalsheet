package eu.wewox.modalsheet

/**
 * Enumeration of available demo examples.
 *
 * @param label Example name.
 * @param description Brief description.
 */
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
    DynamicModalSheet(
        "Dynamic Content Modal Sheet",
        "Example of modal sheet with dynamic content"
    ),
    ScrollableModalSheet(
        "Scrollable Modal Sheet",
        "Example of scrollable modal sheet"
    ),
    CustomModalSheet(
        "Custom Modal Sheet",
        "Example of customizable input arguments"
    ),
    ObservableModalSheet(
        "Observable Modal Sheet",
        "Example how progress of modal sheet could be observed"
    ),
    SheetStateModalSheet(
        "Sheet State Modal Sheet",
        "Example of modal sheet driven by sheet state"
    ),
}
