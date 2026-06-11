package DeCell.FPG.Frontend.Backend;

import java.util.function.BiConsumer;

public abstract class BaseBuilder<T> {
    protected UIElement<?, ?> sibling;

    public UIElement<?, ?> getSibling() {
        return this.sibling;
    }

    public T setSibling(UIElement<?, ?> sblng) {
        this.sibling = sblng;
        return (T) this;
    }

    public T position(BiConsumer<UIElement<?, ?>, BaseBuilder<?>> zaza) {
        return (T) this;
    }
}
