package DeCell.FPG.Frontend.Backend.Components.Dialogues;

import DeCell.FPG.Frontend.Backend.Components.ColorPicker.ColorPickerV2;
import DeCell.FPG.Frontend.Backend.Components.MyButton;
import DeCell.FPG.Frontend.Backend.DataPair;
import DeCell.FPG.Frontend.Backend.UIElement;

import java.util.List;
import java.util.function.Consumer;

public interface Dialogueable<T> { // "T" is the type that will be given when the dialogue closes
    public void popup(MyButton btn, List<UIElement<?, ?>> UIElements, Consumer<T> onClose, DataPair... externalData);
}
