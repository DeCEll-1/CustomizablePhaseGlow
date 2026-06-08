package DeCell.FPG.Frontend.Backend.Components;

import DeCell.FPG.Frontend.Backend.UIElement;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.CutStyle;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.function.Consumer;

public class MyButton extends UIElement<MyButton, ButtonAPI> {

    public MyButton(String text, float width, float height, float pad, MyTooltip parent) {
        super(parent.addButton(text, null, width, height, pad));
        parent.addElement(this);
    }

    public MyButton(String text, Color base, Color bg,
                    float width, float height, float pad, MyTooltip parent) {
        super(parent.addButton(text, null, base, bg, width, height, pad));
        parent.addElement(this);
    }

    public MyButton(String text, Color base, Color bg,
                    Alignment align, CutStyle style,
                    float width, float height, float pad, MyTooltip parent) {
        super(parent.addButton(text, null, base, bg, align, style, width, height, pad));
        parent.addElement(this);
    }

    public MyButton(String text, Alignment align, CutStyle style,
                    float width, float height, float pad, MyTooltip parent) {
        this(text, Misc.getButtonTextColor(), Misc.getDarkPlayerColor(),
                align, style, width, height, pad, parent);
    }

    protected Consumer<ButtonAPI> onClick;
    protected boolean wasChecked = false;

    @Override
    public void advance(float amount) {
        if (u == null) return;

        boolean currentlyChecked = u.isChecked();
        if (currentlyChecked && !wasChecked) {
            if (onClick != null) {
                onClick.accept(u);
            }
            u.setChecked(false);
        }
        wasChecked = currentlyChecked;
    }


    public MyButton setOnClick(Consumer<ButtonAPI> onClick) {
        this.onClick = onClick;
        return this;
    }

    public MyButton setCustomData(Object data) {
        u.setCustomData(data);
        return this;
    }

    public static class Builder {
        private final String text;
        private final float width;
        private final float height;
        private final MyTooltip parent;

        private float pad = 0f;
        private Color baseColor = null;
        private Color bgColor = null;
        private Alignment alignment = null;
        private CutStyle cutStyle = null;

        public Builder(String text, float width, float height, MyTooltip parent) {
            this.text = text;
            this.width = width;
            this.height = height;
            this.parent = parent;
        }

        public Builder(String text, float width, float height, MyPanel parent) {
            this.text = text;
            this.width = width;
            this.height = height;
            this.parent = new MyTooltip.Builder(width, height, parent).b();
        }

        public Builder modifyParent(Consumer<MyTooltip> zaza) { // needed for when you input panel and need to update the tooltips properties
            zaza.accept(parent);
            return this;
        }

        public Builder setPad(float pad) {
            this.pad = pad;
            return this;
        }

        public Builder setColors(Color baseColor, Color bgColor) {
            this.baseColor = baseColor;
            this.bgColor = bgColor;
            return this;
        }

        public Builder setStyle(Alignment alignment, CutStyle cutStyle) {
            this.alignment = alignment;
            this.cutStyle = cutStyle;
            return this;
        }

        public MyButton build() {
            if (baseColor != null && bgColor != null && alignment != null && cutStyle != null) {
                return new MyButton(text, baseColor, bgColor, alignment, cutStyle, width, height, pad, parent);
            }

            if (baseColor != null && bgColor != null) {
                return new MyButton(text, baseColor, bgColor, width, height, pad, parent);
            }

            if (alignment != null && cutStyle != null) {
                return new MyButton(text, alignment, cutStyle, width, height, pad, parent);
            }

            return new MyButton(text, width, height, pad, parent);
        }

    }
}
