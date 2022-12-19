# FrameUI

FrameUI is a library for creating GUI layouts in Minecraft using item frames and maps. It allows you to create custom screens with a variety of components, including buttons, icons, etc.

## Installation

To install FrameUI, simply download the latest release from the releases page and place it in your server's `plugins` directory. FrameUI requires Spigot 1.19.x to run.

## Usage

To use FrameUI, you will need to create a new `Screen` using the `Screen.Builder` class. The `Screen.Builder` allows you to specify the background image, size, and location of the screen.

Here is an example of how to create a simple screen with a button:

```java
Location location = ...;
BufferedImage background = ...;
World world = ...;
List<Player> viewers = ...;
Screen screen = new Screen.Builder(location, background, world, viewers)
        .width(5)
        .height(3);
Component button = new Component(texture, 0, 0, 10, 10);
button.setClickAction((component, clickX, clickY) -> System.out.println("Button clicked!"));
screen.addContent(button);
screen.open();
```
To open the screen for a player, simply call the `screen.open()` method. The screen will be displayed to the player as a series of item frames and maps.



## Customization
FrameUI provides a number of customization options to allow you to create the perfect GUI for your project. You can create custom components to display any content you like.

You can also customize the appearance of the screen by setting the background image and adjusting the size and location of the screen.

## License
FrameUI is released under the [MIT License](/LICENSE).

## Credits
FrameUI was developed by SquidXTV with help from [Kademlia](https://www.twitch.tv/kademlia).

## Contact
If you have any questions or suggestions for FrameUI, please don't hesitate to contact me via email at [squidxtv@gmail.com](mailto:squidxtv@gmail.com).

I hope this README.md file is helpful! Let me know if you have any questions or need further assistance.
