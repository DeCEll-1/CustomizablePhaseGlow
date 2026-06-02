package DeCell.FPG.Frontend.Backend.Components;

import DeCell.FPG.Frontend.Backend.AUIContainer;
import DeCell.FPG.Frontend.Backend.AUIElement;
import DeCell.FPG.Frontend.Backend.Plugins.LambdaUIPanelPlugin;
import DeCell.FPG.Frontend.Backend.Plugins.MultiPluginHandler;
import DeCell.FPG.Frontend.Backend.Rect;
import DeCell.FPG.Frontend.Backend.Renderable.BackgroundRenderable;
import DeCell.FPG.Frontend.Backend.Renderable.BorderRenderable;
import DeCell.FPG.Frontend.Backend.Renderable.RenderableHandlerPlugin;
import DeCell.FPG.JavaSlop.TriConsumer;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;

import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;

public class OpenableButtonPanel extends AUIContainer<OpenableButtonPanel, CustomPanelAPI> {

    private Rect parentRect;
    private MyPanel surrounder;

    private MyButton button;
    private MyPanel container;
    private boolean uiOpen = false;
    private Dictionary<String, Object> internalData = new Hashtable<>();

    public OpenableButtonPanel(float w, float h, MyButton _1, MyPanel parent) {
        super(parent.u.createCustomPanel(w, h, null));
        parent.addComponent(this.u);

        this.button = _1;
        this.button.setOnClick(this::click);
    }

    private void createContainer() {
        this.container = new MyPanel(this.w(), this.h(), new MultiPluginHandler() // main window
                .add(new RenderableHandlerPlugin()
                        .addBelow(
                                // TODO: make these more modifyable
                                new BorderRenderable(Global.getSettings().getSprite("fpg", "border2"), 32)
                                        .setPadding(-8).setRenderInside(true))
                ).add(new LambdaUIPanelPlugin()
                        .onProcessInput(e ->
                                e.forEach(InputEventAPI::consume)
                        )
                ), this.u).addTo(UIElements).inBL(0, 0);


        MyTooltip exitTooltip = new MyTooltip(24, 24, false, container).addTo(UIElements).inTR(26, 16);
        new MyButton("X", 24, 24, 0, exitTooltip).
                setOnClick(this::click).addTo(UIElements);
    }

    public OpenableButtonPanel(CustomPanelAPI underlying) {
        super(underlying);
    }

    protected TriConsumer<MyPanel, Dictionary<String, Object>, List<AUIElement<?, ?>>> onUIOpen;
    protected Consumer<Dictionary<String, Object>> onUIClose;

    private void click(ButtonAPI b) {
        if (uiOpen) {
            this.u.removeComponent(container.u);
            if (onUIClose != null)
                onUIClose.accept(internalData);
        } else {
            createContainer();
            if (onUIOpen != null)
                onUIOpen.accept(container, internalData, UIElements);
        }
        uiOpen = !uiOpen;
    }


    public OpenableButtonPanel setOnUIOpen(TriConsumer<MyPanel, Dictionary<String, Object>, List<AUIElement<?, ?>>> onClick) {
        this.onUIOpen = onClick;
        return this;
    }

    public OpenableButtonPanel setOnUIClose(Consumer<Dictionary<String, Object>> onClick) {
        this.onUIClose = onClick;
        return this;
    }


}
