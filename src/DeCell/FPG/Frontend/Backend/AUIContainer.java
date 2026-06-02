package DeCell.FPG.Frontend.Backend;

import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.UIComponentAPI;

import java.util.ArrayList;
import java.util.List;

public abstract class AUIContainer<T extends AUIElement<T, U>, U extends UIComponentAPI> extends AUIElement<T, U> {

    protected final List<AUIElement<?, ?>> activeUIElements = new ArrayList<>();
    protected final List<AUIElement<?, ?>> UIElements = new ArrayList<>();

    public AUIContainer(U u) {
        super(u);
    }

    public void addElement(AUIElement<?, ?> element) {
        this.UIElements.add(element);
    }

    @Override
    public void advance(float amount) {
        for (AUIElement<?, ?> element : activeUIElements) {
            element.advance(amount);
        }

        if (!UIElements.isEmpty()) {
            activeUIElements.addAll(UIElements);
            UIElements.clear();
        }
    }

    @Override
    public void processInput(List<InputEventAPI> events) {
        for (AUIElement<?, ?> element : activeUIElements) {
            element.processInput(events);
        }
    }

    @Override
    public T update() {
        for (AUIElement<?, ?> element : activeUIElements) {
            element.update();
        }

        return super.update();
    }
}
