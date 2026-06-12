package DeCell.FPG.Frontend.Backend.Components;

import DeCell.FPG.FancyPhaseGlow;
import DeCell.FPG.Frontend.Backend.BaseBuilder;
import DeCell.FPG.Frontend.Backend.Components.Gears.Scroll;
import DeCell.FPG.Frontend.Backend.Components.Gears.UpDownArrow;
import DeCell.FPG.Frontend.Backend.UIContainer;
import DeCell.FPG.Helpers.ElapsingInterval;
import DeCell.FPG.Misc;
import Kryz.Tweening.EasingFunctions;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.CutStyle;
import com.fs.starfarer.api.ui.UIComponentAPI;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.List;

import static DeCell.FPG.FancyPhaseGlow.Patterns.decimalWithMaxDecimalPlaces;
import static DeCell.FPG.Frontend.Backend.DataPair.pair;

public class NumericUpDown extends UIContainer<NumericUpDown, UIComponentAPI> {

    private final MyTextBox tb;
    private final MyButton up;
    private final MyButton down;
    private final UpDownArrow upDownArrowGear;
    // TODO: allow users to hold down left click on the buttons for changing the value
    // TODO: add sounds for inputs

    public NumericUpDown(MyPanel _container, MyTextBox _tb, MyButton _up, MyButton _down) {
        super(_container.u);
        _container.addElement(this);
        this.parent = _container;
        this.tb = _tb;
        this.up = _up;
        this.down = _down;

        tb.setValidationRegex(FancyPhaseGlow.Patterns.DECIMAL_ONLY);
        tb.setTextValidator((t) -> {
            double newVal = getValueForString(t);
            if (newVal < minValue || newVal > maxValue)
                return false;
            return true;
        });

        this.setValue(0);

        up.setOnMouseDown(this::buttonUpdate).addToInternalData(pair("type", UpDownArrow.ButtonType.UP));
        down.setOnMouseDown(this::buttonUpdate).addToInternalData(pair("type", UpDownArrow.ButtonType.DOWN));


        Scroll scrollGear = new Scroll();
        tb.setOnHover(scrollGear::onHover);
        up.setOnHover(scrollGear::onHover);
        down.setOnHover(scrollGear::onHover);
        scrollGear.addScrollListener(this::onScrollEnd);

        this.upDownArrowGear = new UpDownArrow(intervalMin, intervalMax, initialDelay);
        upDownArrowGear.addUpDownListener(s -> {
            switch (s) {
                case UP -> up();
                case DOWN -> down();
            }
        });

        setAmountOfDecimalPlaces(2);
    }

    private void onScrollEnd(Float val) {
        if (val > 0)
            up();
        else
            down();
    }

    private void buttonUpdate(MyButton button) {
        UpDownArrow.ButtonType type = button.getFromInternal("type");

        switch (type) {
            case UP -> up();
            case DOWN -> down();
        }
    }

    private void up() {
        this.setValue(this.getValue() + stepSize);
    }

    private void down() {
        this.setValue(this.getValue() - stepSize);
    }


    //#region arrow keys
    private static final float initialDelay = 0.4f;
    private static final float intervalMin = 0;
    private static final float intervalMax = 0.4f;

    @Override
    public void processInput(List<InputEventAPI> events) {
        if (!tb.u.hasFocus()) {
            return;
        }

        upDownArrowGear.advance();
    }

    private void handleArrowKeys(boolean upPressed) {
        if (upPressed) up();
        else down();
    }
    //#endregion

    //#region getter setters
    private float stepSize = 1f;

    public NumericUpDown setStepSize(float _stepSize) {
        this.stepSize = _stepSize;
        return this;
    }

    public MyTextBox getTextBox() {
        return this.tb;
    }
    //#endregion

    //#region value handling
    private int amountOfDecimalPlaces;
    private double maxValue = Double.MAX_VALUE;
    private double minValue = -Double.MAX_VALUE; // MIN_VALUE isnt actually the minimum value, thanks java

    public NumericUpDown setAmountOfDecimalPlaces(int zaza) {
        this.amountOfDecimalPlaces = zaza;
        this.tb.setValidationRegex(decimalWithMaxDecimalPlaces(amountOfDecimalPlaces));
        return this;
    }

    public NumericUpDown setMinMax(double min, double max) {
        this.minValue = min;
        this.maxValue = max;
        return this;
    }

    public NumericUpDown setValue(double val) {
        val = Misc.clamp(val, minValue, maxValue);
        String text = String.format("%." + amountOfDecimalPlaces + "f", val);
        this.tb.setText(text);
        return this;
    }

    public double getValue() {
        return getValueForString(this.tb.getText());
    }

    private double getValueForString(String text) {
        if (text == null || text.isEmpty())
            text = "0";
        return Double.parseDouble(text);
    }
    //#endregion


    // this wwill be a regular ass winforms numeric up down, it must have 2 buttons for up down
    // properties such as:
    // min, max, step size, end floating points, value changed event, if the button is getting hold keep increase/decreasing it
    // custom regex match, shift arrow keys bla bla allat jazz
    // requred components sis a text box and a button
    public static class Builder extends BaseBuilder<Builder> {

        //#region constants
        public final static float xPadding = 2f;
        public final static float yPadding = 2f;

        public final static float buttonWidth = 16;
        public final static float buttonHeight = MyTextBox.defaultHeight / 2f;


        //#endregion

        // width and height includes the buttons
        public NumericUpDown build(float w, MyPanel parent) {
            final float tbWidth = w - (xPadding + buttonWidth);
            final float tbHeight = MyTextBox.defaultHeight;

            MyPanel container = new MyPanel.Builder(w, tbHeight).build(parent);

            MyTextBox tb = new MyTextBox.Builder(tbWidth, tbHeight, container)
                    .position((item, builder) -> item.inTL(0, 0)).build();

            MyButton upButton = new MyButton.Builder("", buttonWidth, buttonHeight, container)
                    .position((i, b) -> i.inTR(0, 0))
                    .setStyle(Alignment.MID, CutStyle.TOP)
                    .build();

            MyButton downButton = new MyButton.Builder("", buttonWidth, buttonHeight, container)
                    .position((i, b) -> i.inBR(0, 0))
                    .setStyle(Alignment.MID, CutStyle.BOTTOM)
                    .build();

            return new NumericUpDown(container, tb, upButton, downButton);
        }

    }
}
