package DeCell.FPG.Frontend.Backend.Components;

import DeCell.FPG.Frontend.Backend.BaseBuilder;
import DeCell.FPG.Frontend.Backend.UIContainer;
import com.fs.starfarer.api.ui.UIComponentAPI;

public class NumericUpDown extends UIContainer<NumericUpDown, UIComponentAPI> {

    public NumericUpDown(UIComponentAPI uiComponentAPI) {
        super(uiComponentAPI);
    }


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
        public final static float buttonHeight = 8;


        //#endregion

        // width and height includes the buttons
        public Builder(float w, float h, MyPanel parent) {
            MyPanel container = new MyPanel.Builder(w, h).build(parent);

            final float tbWidth = w - (xPadding + buttonWidth);

//            MyTooltip tb = new MyTooltip.Builder(tbWidth, h, container).build().addTextField();


        }

    }
}
