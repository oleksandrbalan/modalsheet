[![Maven Central](https://img.shields.io/maven-central/v/io.github.oleksandrbalan/modalsheet.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.github.oleksandrbalan/modalsheet)

<img align="right" src="https://user-images.githubusercontent.com/20944869/211682502-ea30da26-178f-4e91-82d3-75207dbb6356.png">

# Modal Sheet

Modal Sheet library for Jetpack Compose.

## Motivation

Sometimes we want bottom sheets to behave as real modals, thus overlaying all content on the screen. Official `ModalBottomSheetState` however, is laid out only in bounds of the parent Composable. This means that to display a bottom sheet above, for example bottom bar navigation, it must be placed "higher" in the hierarchy than usage in one of the tab screen.

The `ModalSheet` Composable from this library allows creating real modal bottom sheets which could be placed anywhere in the hierarchy. 

## Solution

The `ModalSheet` Composable internally uses `FullScreenPopup` which is basically a window placed above the current window and allows placing Composables inside. Then `ModalSheet` adds swipe-to-dismiss, scrim and shaping to simulate bottom sheet behavior.

## Usage

### Get a dependency

**Step 1.** Add the MavenCentral repository to your build file.
Add it in your root `build.gradle` at the end of repositories:
```
allprojects {
    repositories {
        ...
        mavenCentral()
    }
}
```

Or in `settings.gradle`:
```
dependencyResolutionManagement {
    repositories {
        ...
        mavenCentral()
    }
}
```

**Step 2.** Add the dependency.
Check latest version on the [releases page](https://github.com/oleksandrbalan/modalsheet/releases).
```
dependencies {
    implementation 'io.github.oleksandrbalan:modalsheet:$version'
}
```

### Use in Composable

The `ModalSheet` has 2 mandatory arguments:
* **visible** - True if modal should be visible.
* **onVisibleChange** -  Called when visibility changes.

```
var visible by remember { mutableStateOf(false) }

Button(onClick = { visible = true }) {
    Text(text = "Show modal sheet")
}

ModalSheet(
    visible = visible,
    onVisibleChange = { visible = it },
) {
    Box(Modifier.height(200.dp))
}
```

See Demo application and [examples](demo/src/main/kotlin/eu/wewox/modalsheet/screens) for more usage examples.

<img src="https://user-images.githubusercontent.com/20944869/166837599-3b7423db-cee1-4444-b760-3986bc1aa695.gif" width="250" />&emsp;<img src="https://user-images.githubusercontent.com/20944869/166837878-06c73b4e-6b6e-4eae-ab91-56ba2dffbb8d.gif" width="250" />


#### Alternative overloads

Library also has a `ModalSheet` overload which receives `data` to be shown in the bottom sheet. When `data` is not null, bottom sheet is shown; when `null`, bottom sheet is hidden.

This is useful when bottom sheet content dependents on some state, which may be change with a time, for example the bottom sheet which shows an error message. See [DynamicModalSheetScreen](demo/src/main/kotlin/eu/wewox/modalsheet/screens/DynamicModalSheetScreen.kt).

Also there is an option to use `ModalSheet` overload with `ModalBottomSheetState` to have a full control when sheet should be shown or hidden. See [SheetStateModalSheetScreen](demo/src/main/kotlin/eu/wewox/modalsheet/screens/SheetStateModalSheetScreen.kt).

## Authors

* [Filip Wiesner](https://github.com/wooodenleg) Initial modal sheet idea and implementation.

* [Oleksandr Balan](https://github.com/oleksandrbalan) Popup enhancements and support.


## TODO list

* Provide swipe state to `ModalSheetScope`
