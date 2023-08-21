package me.squidxtv.frameui.core.content;

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
 * @see Div
 * @see ScreenModel
 */
public interface Parent {

    @NotNull List<Content> getChildren();

    /**
     * Gets all children with the given type.
     * @param type to search for
     * @return all children with the given type
     */
    @Unmodifiable
    default <T extends Content> @NotNull List<T> getChildrenByType(@NotNull Class<T> type) {
        return getContentsByType(getChildren(), type, false).toList();
    }

    /**
     * Gets all descendants with the given type.
     * @param type to search for
     * @return all descendants with the given type
     */
    @Unmodifiable
    default <T extends Content> @NotNull List<T> getDescendantsByType(@NotNull Class<T> type) {
        return getContentsByType(getChildren(), type, true).toList();
    }

    /**
     * Gets the first child with the given type.
     * @param type to search for
     * @return first child found with the given type
     */
    default <T extends Content> @NotNull Optional<T> getFirstChildByType(@NotNull Class<T> type) {
        return getContentsByType(getChildren(), type, false).findFirst();
    }

    /**
     * Gets the first descendant with the given type.
     * @param type to search for
     * @return the first descendant with the given type
     */
    default <T extends Content> @NotNull Optional<T> getFirstDescendantByType(@NotNull Class<T> type) {
        return getContentsByType(getChildren(), type, true).findFirst();
    }

    /**
     * Gets any child with the given type.
     * @param type to search for
     * @return any child with the given type
     */
    default <T extends Content> @NotNull Optional<T> getAnyChildByType(@NotNull Class<T> type) {
        return getContentsByType(getChildren(), type, false).findAny();
    }

    /**
     * Gets any descendant with the given type.
     * @param type to search for
     * @return any descendant with the given type
     */
    default <T extends Content> @NotNull Optional<T> getAnyDescendantByType(@NotNull Class<T> type) {
        return getContentsByType(getChildren(), type, true).findAny();
    }

    /**
     * Gets all children with the given id.
     * @param id to search for
     * @return all children with the given id
     */
    @Unmodifiable
    default @NotNull List<Content> getChildrenById(@NotNull String id) {
        return getContentsById(getChildren(), id, false).toList();
    }

    /**
     * Gets all descendants with the given id.
     * @param id to search for
     * @return all descendants with the given id
     */
    @Unmodifiable
    default @NotNull List<Content> getDescendantsById(@NotNull String id) {
        return getContentsById(getChildren(), id, true).toList();
    }

    /**
     * Gets the first child with given id.
     * @param id to search for
     * @return first child found with given id
     */
    default @NotNull Optional<Content> getFirstChildById(@NotNull String id) {
        return getContentsById(getChildren(), id, false).findFirst();
    }

    /**
     * Gets the first descendant with given id.
     * @param id to search for
     * @return first descendant found with given id
     */
    default @NotNull Optional<Content> getFirstDescendantById(@NotNull String id) {
        return getContentsById(getChildren(), id, true).findFirst();
    }

    /**
     * Gets any child with given id.
     * @param id to search for
     * @return any child found with given id
     */
    default @NotNull Optional<Content> getAnyChildById(@NotNull String id) {
        return getContentsById(getChildren(), id, false).findAny();
    }

    /**
     * Gets any descendant with given id.
     * @param id the id to search for
     * @return any descendant found with given id
     */
    default @NotNull Optional<Content> getAnyDescendantById(@NotNull String id) {
        return getContentsById(getChildren(), id, true).findAny();
    }

    private static @NotNull Stream<Content> getContentsById(@NotNull List<Content> contents, @NotNull String id, boolean deep) {
        Stream<Content> byId = contents.stream()
                .filter(content -> content.getId().equals(id));

        if (deep) {
            Stream<Content> childrenById = contents.stream()
                    .filter(Parent.class::isInstance)
                    .flatMap(content -> getContentsById(((Parent) content).getChildren(), id, true));

            byId = Stream.concat(byId, childrenById);
        }

        return byId;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Content> @NotNull Stream<T> getContentsByType(@NotNull List<Content> contents, Class<T> type, boolean deep) {
        Stream<T> filteredContent = contents.stream()
                .filter(type::isInstance)
                .map(content -> (T) content);

        if (deep) {
            Stream<T> descendantFilteredContent = contents.stream()
                    .filter(Parent.class::isInstance)
                    .flatMap(content -> getContentsByType(((Parent) content).getChildren(), type, true));

            filteredContent = Stream.concat(filteredContent, descendantFilteredContent);
        }

        return filteredContent;
    }

}
