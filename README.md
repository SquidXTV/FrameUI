# FrameUI
FrameUI is a Minecraft plugin library designed for developers to easily create and manage screens within the server.
A `Screen` is composed of multiple item frames, each displaying a custom image, collectively forming a larger, cohesive image.

They support click and scroll interactions, allowing for dynamic player interactions. Developers can also set the permissions for
these actions and define their max interaction distance to trigger the event. Server owners can also disable these features entirely
via the config. 

The plugin includes a default `Renderer` that uses a simple `BufferedImage` and a `ScreenSpawner` that uses packets to spawn
the screens item frames in the world of specific players. Both the renderer and spawner can be customized top fit specific needs.

