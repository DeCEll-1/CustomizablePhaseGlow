package DeCell.FPG.Frontend.Backend;

import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.UIComponentAPI;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;

import static DeCell.FPG.Frontend.Backend.DataPair.pair;

public abstract class UIElement<T extends UIElement<T, U>, U extends UIComponentAPI> {
    public final U u; // underlying
    public String name = java.util.UUID.randomUUID().toString(); // solely for debugging

    protected Consumer<T> onMouseEnter;
    protected Consumer<T> onMouseExit;
    protected Consumer<T> onHover;
    protected Consumer<T> onMouseDown;
    protected Consumer<T> onMouseUp;
    protected boolean wasClickedLastFrame = false;
    protected boolean isDragging = false;

    protected boolean wasHovered = false;
    protected boolean markedForDeletion = false;


    protected UIContainer<?, ?> parent;
    protected boolean ignoreEvents = false;

    public T setIgnoreEvents(boolean ignore) {
        this.ignoreEvents = ignore;
        return (T) this;
    }

    protected boolean consumeEvents = true;

    public T setConsumeEvents(boolean consumeEvents) {
        this.consumeEvents = consumeEvents;

        return (T) this;
    }

    public <P extends UIContainer<?, ?>> P getParent() {
        return (P) parent;
    }

    protected Dictionary<String, Object> internalData = new Hashtable<>();

    public T addToInternalData(String s, Object data) {
        addToInternalData(pair(s, data));
        return (T) this;
    }

    public T addToInternalData(DataPair... entries) {
        for (DataPair entry : entries) {
            if (entry != null) {
                if (internalData.get(entry.key) != null)
                    internalData.remove(entry.key);
                internalData.put(entry.key, entry.value);
            }
        }
        return (T) this;
    }

    public <Z> Z getFromInternal(String s) {
        return (Z) this.internalData.get(s);
    }

    public UIElement(U underlying) {
        this.u = underlying;
    }

    public void markForDeletion() {
        this.markedForDeletion = true;
    }

    public boolean isMarkedForDeletion() {
        return this.markedForDeletion;
    }

    public void processInput(List<InputEventAPI> events) {
        if (u == null || ignoreEvents) return;

        boolean isMouseOver = this.rect().containsMouse();

        if (isMouseOver && !wasHovered) {
            if (onMouseEnter != null) onMouseEnter.accept((T) this);
        } else if (!isMouseOver && wasHovered) {
            if (onMouseExit != null) onMouseExit.accept((T) this);
        }

        if (isMouseOver && onHover != null) {
            onHover.accept((T) this);
        }

        if (!Mouse.isButtonDown(0))
            isDragging = false; // only left mouse

        for (InputEventAPI event : events) {
            if (event.isConsumed()) continue;

            Rect thisRect = this.rect();
            boolean eventInside = this.getPosition().containsEvent(event);

            if (event.isLMBDownEvent() && eventInside) {
                isDragging = true;
                if (consumeEvents)
                    event.consume();

                if (onMouseDown != null) {
                    onMouseDown.accept((T) this);
                }
            }

            if (event.isLMBUpEvent() && isDragging) {
                isDragging = false;
                if (consumeEvents)
                    event.consume();

                if (onMouseUp != null) {
                    onMouseUp.accept((T) this);
                }
            }
        }

        wasHovered = isMouseOver;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public T setDragging(boolean dragging) {
        this.isDragging = dragging;
        return (T) this;
    }

    public void advance(float amount) {
    }

    public T update() {

        return (T) this;
    }

    public T addTo(List<UIElement<?, ?>> l) {
        l.add(this);
        return (T) this;
    }

    public T setOnMouseEnter(Consumer<T> onMouseEnter) {
        this.onMouseEnter = onMouseEnter;
        return (T) this;
    }

    public T setOnHover(Consumer<T> onHover) {
        this.onHover = onHover;
        return (T) this;
    }

    public T setOnMouseExit(Consumer<T> onMouseExit) {
        this.onMouseExit = onMouseExit;
        return (T) this;
    }

    public T setOnMouseDown(Consumer<T> onMouseDown) {
        this.onMouseDown = onMouseDown;
        return (T) this;
    }

    public T setOnMouseUp(Consumer<T> onMouseUp) {
        this.onMouseUp = onMouseUp;
        return (T) this;
    }

    public PositionAPI getPosition() {
        return u.getPosition();
    }

    public Rect rect() {
        return new Rect(x(), y(), w(), h());
    }

    public float x() {
        return u.getPosition().getX();
    }

    public float y() {
        return u.getPosition().getY();
    }

    public float w() {
        return u.getPosition().getWidth();
    }

    public float h() {
        return u.getPosition().getHeight();
    }

    public T setLocation(float x, float y) {
        getPosition().setLocation(x, y);
        return (T) this;
    }

    public T setSize(float width, float height) {
        getPosition().setSize(width, height);
        return (T) this;
    }

    public T setXAlignOffset(float xAlignOffset) {
        getPosition().setXAlignOffset(xAlignOffset);
        return (T) this;
    }

    public T setYAlignOffset(float yAlignOffset) {
        getPosition().setYAlignOffset(yAlignOffset);
        return (T) this;
    }

//#region --- Parent Container Positioning ---

    public T inTL(float xPad, float yPad) {
        getPosition().inTL(xPad, yPad);
        return (T) this;
    }

    public T inTMid(float yPad) {
        getPosition().inTMid(yPad);
        return (T) this;
    }

    public T inTR(float xPad, float yPad) {
        getPosition().inTR(xPad, yPad);
        return (T) this;
    }

    public T inRMid(float xPad) {
        getPosition().inRMid(xPad);
        return (T) this;
    }

    public T inMid() {
        getPosition().inMid();
        return (T) this;
    }

    public T inBR(float xPad, float yPad) {
        getPosition().inBR(xPad, yPad);
        return (T) this;
    }

    public T inBMid(float yPad) {
        getPosition().inBMid(yPad);
        return (T) this;
    }

    public T inBL(float xPad, float yPad) {
        getPosition().inBL(xPad, yPad);
        return (T) this;
    }

    public T inLMid(float xPad) {
        getPosition().inLMid(xPad);
        return (T) this;
    }

    //#region Vector2f

    public T inTL(Vector2f pad) {
        getPosition().inTL(pad.x, pad.y);
        return (T) this;
    }

    public T inTR(Vector2f pad) {
        getPosition().inTR(pad.x, pad.y);
        return (T) this;
    }

    public T inBR(Vector2f pad) {
        getPosition().inBR(pad.x, pad.y);
        return (T) this;
    }

    public T inBL(Vector2f pad) {
        getPosition().inBL(pad.x, pad.y);
        return (T) this;
    }

    //#endregion

    //#endregion

//#region --- Sibling Relative Positioning ---

    public T leftOfTop(UIComponentAPI sibling, float xPad) {
        getPosition().leftOfTop(sibling, xPad);
        return (T) this;
    }

    public T leftOfMid(UIComponentAPI sibling, float xPad) {
        getPosition().leftOfMid(sibling, xPad);
        return (T) this;
    }

    public T leftOfBottom(UIComponentAPI sibling, float xPad) {
        getPosition().leftOfBottom(sibling, xPad);
        return (T) this;
    }

    public T rightOfTop(UIComponentAPI sibling, float xPad) {
        getPosition().rightOfTop(sibling, xPad);
        return (T) this;
    }

    public T rightOfMid(UIComponentAPI sibling, float xPad) {
        getPosition().rightOfMid(sibling, xPad);
        return (T) this;
    }

    public T rightOfBottom(UIComponentAPI sibling, float xPad) {
        getPosition().rightOfBottom(sibling, xPad);
        return (T) this;
    }

    public T aboveLeft(UIComponentAPI sibling, float yPad) {
        getPosition().aboveLeft(sibling, yPad);
        return (T) this;
    }

    public T aboveMid(UIComponentAPI sibling, float yPad) {
        getPosition().aboveMid(sibling, yPad);
        return (T) this;
    }

    public T aboveRight(UIComponentAPI sibling, float yPad) {
        getPosition().aboveRight(sibling, yPad);
        return (T) this;
    }

    public T belowLeft(UIComponentAPI sibling, float yPad) {
        getPosition().belowLeft(sibling, yPad);
        return (T) this;
    }

    public T belowMid(UIComponentAPI sibling, float yPad) {
        getPosition().belowMid(sibling, yPad);
        return (T) this;
    }

    public T belowRight(UIComponentAPI sibling, float yPad) {
        getPosition().belowRight(sibling, yPad);
        return (T) this;
    }

    //#endregion


    @Override
    public String toString() {
        return this.name;
    }
}
