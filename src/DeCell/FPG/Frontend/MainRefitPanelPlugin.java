package DeCell.FPG.Frontend;

import DeCell.FPG.Frontend.Backend.*;
import DeCell.FPG.Frontend.Backend.Components.Combobox.ComboboxElement;
import DeCell.FPG.Frontend.Backend.Components.Combobox.MyCombobox;
import DeCell.FPG.Frontend.Backend.Components.MyButton;
import DeCell.FPG.Frontend.Backend.Components.MyPanel;
import DeCell.FPG.Frontend.Backend.Components.MyTooltip;
import DeCell.FPG.Frontend.Backend.Components.OpenableButtonPanel;
import DeCell.FPG.Frontend.Backend.Plugins.CPanelPlugin;
import DeCell.FPG.Frontend.Backend.Plugins.LambdaUIPanelPlugin;
import DeCell.FPG.Frontend.Backend.Plugins.MultiPluginHandler;
import DeCell.FPG.Frontend.Backend.Renderable.BackgroundRenderable;
import DeCell.FPG.Frontend.Backend.Renderable.BorderRenderable;
import DeCell.FPG.Frontend.Backend.Renderable.RenderableHandlerPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static DeCell.FPG.Reflections.invokeMethod;


public class MainRefitPanelPlugin extends CPanelPlugin {
    public List<AUIElement<?, ?>> ActiveUIElements = new ArrayList<>();
    private final List<AUIElement<?, ?>> UIElements = new ArrayList<>();

    public MainRefitPanelPlugin() {
    }

    @Override
    public void init(CustomPanelAPI _p) {
        MyPanel parent = new MyPanel(_p);

        MyPanel refitWindowOpenerButtonPanel = new MyPanel(190, 25, null, parent)
                .addTo(UIElements).inBL(601, 40);

        MyTooltip refitWindowOpenerTooltip = new MyTooltip(190, 25, false, refitWindowOpenerButtonPanel).addTo(UIElements);
        MyButton panelOpeningButton = new MyButton("Modify Phase Effects", new Color(0xDDDDDD), new Color(0x444444), Alignment.MID, CutStyle.TL_BR, 190, 25, 0f, refitWindowOpenerTooltip)
                .addTo(UIElements);

        new OpenableButtonPanel(720, 640, panelOpeningButton, parent).inTL(210, 50).addTo(UIElements)
                .setOnUIOpen((panel, internalData, _UIElements) ->
                {
                    // evil
                    MyTooltip debugOpeningTooltip = new MyTooltip(190, 24, false, panel).addTo(_UIElements).inBR(16, 4);
                    MyButton debugButton = new MyButton("Debug", Alignment.MID, CutStyle.TOP, 190, 24, 0, debugOpeningTooltip).addTo(_UIElements).inMid();


                    MyPanel shaderSelectionContainer = new MyPanel(190, 25, null, panel).addTo(_UIElements).inTL(26, 30);
                    MyTooltip shaderSelectionTooltip = new MyTooltip(190, 25, false, shaderSelectionContainer).addTo(_UIElements);
                    new MyCombobox(new MyButton("Select Element", Alignment.MID, CutStyle.TOP, 190, 25, 0, shaderSelectionTooltip).addTo(_UIElements), shaderSelectionContainer)
                            .addItem(new ComboboxElement("balls"))
                            .addItem(new ComboboxElement("balls2"))
                            .addItem(new ComboboxElement("balls3"))
                            .addItem(new ComboboxElement("balls4"))
                            .addTo(_UIElements)
                            .setOnUpdate(el -> {
                            })
                    ;
                });
    }

    private static com.fs.starfarer.coreui.refit.U getRefitPanel() {
        CampaignUIAPI campaignUI = Global.getSector().getCampaignUI();
        InteractionDialogAPI dialog = campaignUI.getCurrentInteractionDialog();

        Object core = dialog == null
                ? invokeMethod("getCore", campaignUI)
                : invokeMethod("getCoreUI", dialog);
        return (com.fs.starfarer.coreui.refit.U) invokeMethod("getRefitPanel", invokeMethod("getCurrentTab", core));
    }

    @Override
    public void advance(float amount) {
        for (AUIElement<?, ?> element : ActiveUIElements) {
            element.advance(amount);
        }

        if (!UIElements.isEmpty()) {
            ActiveUIElements.addAll(UIElements);
            UIElements.clear();
        }
    }

    @Override
    public void processInput(List<InputEventAPI> events) {
        for (AUIElement<?, ?> element : ActiveUIElements) {
            element.processInput(events);
        }
    }
}
