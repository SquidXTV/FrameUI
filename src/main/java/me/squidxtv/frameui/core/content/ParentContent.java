package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.scroll.ScrollDirection;
import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This interface represents {@link Content} with the ability to have children.
 *
 * <p>Provides methods to access the children of this element.
 * "Children" refers to elements directly under this parent.
 * "Descendants" refer to children of all subsequent generations
 * (i.e., Children, Grandchildren, Great Grandchildren, etc.).
 *
 * @see Div
 * @see ScreenModel
 */
public abstract class ParentContent extends AbstractContent {

    private final @NotNull List<Content> children;

    protected ParentContent(@NotNull String id, @NotNull BorderAttribute borderAttribute, @NotNull List<Content> children) {
        super(id, borderAttribute);
        this.children = children;
    }

    @Override
    public void click(@NotNull ActionInitiator initiator, int clickX, int clickY, BoundingBox parentBoundingBox) {
        BoundingBox absolutePosition = getAbsolutePosition(parentBoundingBox);

        if (absolutePosition.width() <= 0 || absolutePosition.height() <= 0) {
            return;
        }

        if (absolutePosition.isPositionOutside(clickX, clickY)) {
            return;
        }

        for (Content child : children) {
            child.click(initiator, clickX, clickY, absolutePosition);
        }
        getClickAction().perform(initiator, clickX, clickY);
    }

    @Override
    public void scroll(@NotNull ActionInitiator initiator, @NotNull ScrollDirection direction, int scrollX, int scrollY, @NotNull BoundingBox parentBoundingBox) {
        BoundingBox absolutePosition = getAbsolutePosition(parentBoundingBox);

        if (absolutePosition.width() <= 0 || absolutePosition.height() <= 0) {
            return;
        }

        if (absolutePosition.isPositionOutside(scrollX, scrollY)) {
            return;
        }

        for (Content child : children) {
            child.scroll(initiator, direction, scrollX, scrollY, absolutePosition);
        }
        getScrollAction().perform(initiator, direction, scrollX, scrollY);
    }

    public @NotNull List<Content> getChildren() {
        return children;
    }

    /**
     * Gets all children with the given type.
     *
     * @param type to search for
     * @return all children with the given type
     */
    @Unmodifiable
    public <T extends Content> @NotNull List<T> getChildrenByType(@NotNull Class<T> type) {
        return getContentsByType(getChildren(), type, false).toList();
    }

    /**
     * Gets all descendants with the given type.
     *
     * @param type to search for
     * @return all descendants with the given type
     */
    @Unmodifiable
    public <T extends Content> @NotNull List<T> getDescendantsByType(@NotNull Class<T> type) {
        return getContentsByType(getChildren(), type, true).toList();
    }

    /**
     * Gets the first child with the given type.
     *
     * @param type to search for
     * @return first child found with the given type
     */
    public <T extends Content> @NotNull Optional<T> getFirstChildByType(@NotNull Class<T> type) {
        return getContentsByType(getChildren(), type, false).findFirst();
    }

    /**
     * Gets the first descendant with the given type.
     *
     * @param type to search for
     * @return the first descendant with the given type
     */
    public <T extends Content> @NotNull Optional<T> getFirstDescendantByType(@NotNull Class<T> type) {
        return getContentsByType(getChildren(), type, true).findFirst();
    }

    /**
     * Gets any child with the given type.
     *
     * @param type to search for
     * @return any child with the given type
     */
    public <T extends Content> @NotNull Optional<T> getAnyChildByType(@NotNull Class<T> type) {
        return getContentsByType(getChildren(), type, false).findAny();
    }

    /**
     * Gets any descendant with the given type.
     *
     * @param type to search for
     * @return any descendant with the given type
     */
    public <T extends Content> @NotNull Optional<T> getAnyDescendantByType(@NotNull Class<T> type) {
        return getContentsByType(getChildren(), type, true).findAny();
    }

    /**
     * Gets all children with the given id.
     *
     * @param id to search for
     * @return all children with the given id
     */
    @Unmodifiable
    public @NotNull List<Content> getChildrenById(@NotNull String id) {
        return getContentsById(getChildren(), id, false).toList();
    }

    /**
     * Gets all descendants with the given id.
     *
     * @param id to search for
     * @return all descendants with the given id
     */
    @Unmodifiable
    public @NotNull List<Content> getDescendantsById(@NotNull String id) {
        return getContentsById(getChildren(), id, true).toList();
    }

    /**
     * Gets the first child with given id.
     *
     * @param id to search for
     * @return first child found with given id
     */
    public @NotNull Optional<Content> getFirstChildById(@NotNull String id) {
        return getContentsById(getChildren(), id, false).findFirst();
    }

    /**
     * Gets the first descendant with given id.
     *
     * @param id to search for
     * @return first descendant found with given id
     */
    public @NotNull Optional<Content> getFirstDescendantById(@NotNull String id) {
        return getContentsById(getChildren(), id, true).findFirst();
    }

    /**
     * Gets any child with given id.
     *
     * @param id to search for
     * @return any child found with given id
     */
    public @NotNull Optional<Content> getAnyChildById(@NotNull String id) {
        return getContentsById(getChildren(), id, false).findAny();
    }

    /**
     * Gets any descendant with given id.
     *
     * @param id the id to search for
     * @return any descendant found with given id
     */
    public @NotNull Optional<Content> getAnyDescendantById(@NotNull String id) {
        return getContentsById(getChildren(), id, true).findAny();
    }

    private static @NotNull Stream<Content> getContentsById(@NotNull List<Content> contents, @NotNull String id, boolean deep) {
        return contents.stream()
                .flatMap(content -> {
                    Stream<Content> byId = content.getId().equals(id) ? Stream.of(content) : Stream.empty();
                    if (deep && content instanceof ParentContent parent) {
                        byId = Stream.concat(byId, getContentsById(parent.getChildren(), id, true));
                    }
                    return byId;
                });
    }

    @SuppressWarnings("unchecked")
    private static <T extends Content> @NotNull Stream<T> getContentsByType(@NotNull List<Content> contents, Class<T> type, boolean deep) {
        return contents.stream()
                .flatMap(content -> {
                    Stream<T> byId = type.isInstance(content) ? Stream.of((T) content) : Stream.empty();
                    if (deep && content instanceof ParentContent parent) {
                        byId = Stream.concat(byId, getContentsByType(parent.getChildren(), type, true));
                    }
                    return byId;
                });
    }

}
