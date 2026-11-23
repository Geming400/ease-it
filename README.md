<div align="center">
    <img src="./src/main/resources/assets/ease-it/icon.png" height=35% width=35% alt="Mod's logo">
</div>

# Waiter, ease it please !

"Waiter, ease it please !" is a very simply library for Minecraft that enables you to
apply easing to Minecraft widgets.

## Downloading

To download "Waiter, ease it please !", put this into your `build.gradle`:

```build.gradle
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    modImplementation "com.github.geming400:ease-it:v[VERSION]" // eg: VERSION=1.0.1
}
```

And in your `fabric.mod.json`:

```json
{
  "depends": {
    "ease-it": ">=[VERSION]"
  }
}
```

## Usage

This mod makes every `Screen` and `AbstractWidget` (or `ClickableWidget` in yarn) implement the `Animatable` interface.
This interface makes you able to add `Animation`s to a given screen or widget.

Each instance of this interface has its **own queue** and **only 1 animation can play at a time**.

### Adding animations

To add animations, use `easeit$addAnimation`:

```java
class MyScreen extends Screen {
    // ...
    
    protected void init() {
        // We add an animation to the widget's queue
        // with the BOUNCE easing (IN_OUT) -> https://easings.net/#easeInOutBounce
        // that lasts for 1 second and moves to the
        // center of the screen (new Vector2i())
        
        var myWidget = /* ... */;
        myWidget.easeit$addAnimation(Animation.ofInt(
                myWidget,
                Easing.BOUNCE, EasingType.IN_OUT,
                20, // 1 second (20 ticks = 1 second)
                Action.createMove(new Vector2i())
        ));
    }
}
```

You probably noticed the animation static constructor `Animation.ofInt`.
There are 3 types of these:
- `Animation.ofInt`
- `Animation.ofFloat`
- `Animation.ofDouble`

For example, if you have a position that you want to apply an easing to, and this position
uses integers, you're going to use `Animation.ofInt`.

If you need another type for whatever reason, you can call `Animation`'s constructor instead.

---

You can look at the `Easing` class for all the available easing that were implemented.

### Actions

Actions are what actually animate a widget.
They are represented using the `Action` functional interface.<br>
All "Waiter, ease it please !"'s actions can be found in the `Action` interface as static methods. As of v1.0.1 it
contains these ones:
- `Action.createMove`
- `Action.createResize`
- `Action.createAlpha`

Actions have a generic type, `T`. This type is the same as the `Animation` class generic type.

#### Making your own actions

To make your own "actions", you need to implement the `Action` functional interface.
This action executes each time the animation ticks and is what lets you modify a widget
depending on the easing:

---

When making an action, you must not mutate directly a variable.
For example, this is safe:

```java
static Action<Float> createAlpha(@Range(from = 0, to = 1) float from, @Range(from = 0, to = 1) float to) {
    float clampedFrom = Math.clamp(from, 0, 1);
    float clampedTo = Math.clamp(to, 0, 1);

    return (widget, animation, easing, progress) ->
            widget.setAlpha(easing.get(progress, clampedFrom, clampedTo));
}
```

But this:

```java
static Action<Float> myAction() {
    float myMutableVar = Math.clamp(from, 0, 1);

    return (widget, animation, easing, progress) -> {
        myMutableVar = 0.6f; // THIS IS NOT ALLOWED
        // ...
    };
}
```

To write safe code, use `AtomicReference`s:

```java
static Action<Float> myAction() {
    AtomicReference<Float> myMutableVar = new AtomicReference<>(Math.clamp(from, 0, 1));

    return (widget, animation, easing, progress) -> {
        myMutableVar.set(0.6f); // THIS IS NOT ALLOWED
        // ...
    };
}
```

Effectively, **all variables should be final**.

## Contributing

For setup instructions, please see the [Fabric Documentation page](https://docs.fabricmc.net/develop/getting-started/creating-a-project#setting-up) related to the IDE that you are using.
