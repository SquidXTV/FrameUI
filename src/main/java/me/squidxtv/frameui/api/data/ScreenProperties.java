package me.squidxtv.frameui.api.data;

import lombok.Data;
import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.ScreenLocation;
import org.joml.Vector2i;


@Data
public class ScreenProperties {

    private String name;
    private Vector2i size;
    private ScreenLocation screenLocation;
    private double clickRadius;
    private double scrollRadius;
    private Screen.State state = Screen.State.CLOSED;

    public int getWidth() {
        return size.x;
    }

    public int getHeight() {
        return size.y;
    }
}
