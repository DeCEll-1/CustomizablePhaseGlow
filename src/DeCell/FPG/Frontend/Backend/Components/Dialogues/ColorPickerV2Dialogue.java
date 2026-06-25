package DeCell.FPG.Frontend.Backend.Components.Dialogues;

import DeCell.FPG.Frontend.Backend.Components.ColorPicker.Adapters.RgbColorPickerAdapter;
import DeCell.FPG.Frontend.Backend.Components.ColorPicker.ColorPickerV2;
import DeCell.FPG.Frontend.Backend.Components.DialougeButtonPanel;
import DeCell.FPG.Frontend.Backend.Components.MyButton;
import DeCell.FPG.Frontend.Backend.DataPair;
import DeCell.FPG.Frontend.Backend.UIElement;

import java.util.List;
import java.util.function.Consumer;

public class ColorPickerV2Dialogue implements Dialogueable<ColorPickerV2> {
    @Override
    public void popup(MyButton btn, List<UIElement<?, ?>> UIElements, Consumer<ColorPickerV2> onClose, DataPair... externalData) {
        new DialougeButtonPanel
                .Builder(ColorPickerV2.sizeRect.w, ColorPickerV2.sizeRect.h,
                btn
        ).withCharlie().build(UIElements)
                .showCloseButton(false)
                .addToInternalData(externalData)
                .addToInternalData("externalData", externalData) // need to set these accessable by the inner element as well
                .addToInternalData("on_close", onClose)
                .setOnUIOpen(
                        (_panel, _dialogue, __UIElements) ->
                        {
                            new ColorPickerV2.Builder().build(_panel).setAdapter(new RgbColorPickerAdapter())
                                    .inMid()
                                    .addToInternalData("dialogue", _dialogue)
                                    .addToInternalData(_dialogue.getFromInternal("externalData"))
                                    .setOnChange(s -> {
                                        s.<DialougeButtonPanel>getFromInternal("dialogue")
                                                .addToInternalData("out", s);
                                    })
                            ;
                        }
                ).setOnUIClose(_dialogue ->
                        _dialogue.<Consumer<ColorPickerV2>>getFromInternal("on_close").accept(_dialogue.getFromInternal("out"))
                );
    }
}
