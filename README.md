# FrameUI
FrameUI is a Minecraft plugin library designed for developers to easily create and manage screens within the server.

## Core Concepts

At its core, a `Screen` represents a visual interface displayed on one or more item frames, which can be customized to show various content.
Each `Screen` uses a `Renderer` implementation to define how its content is visually represented, allowing developers to customize the logic
for rendering. On the other hand, the `ScreenSpawner` implementation is used to define how to manage the lifecycle of screens, how they are created, updated
and despawned. 

The library itself provides a `BufferedImageRenderer` implementation that renders a `BufferedImage` on a `Screen`. It also provides 
the `PacketScreenSpawner` as an implementation for `ScreenSpawner` that uses packets to spawn, update and despawn the screens in the Minecraft World.
Map data is sent through packets as well.

They also support click and scroll interactions, allowing for dynamic player interactions. 
Developers can customize the permissions for these actions and define their max interaction distance to trigger the event. 
Server owners can also disable these features entirely via the config. 
