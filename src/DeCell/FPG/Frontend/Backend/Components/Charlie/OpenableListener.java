package DeCell.FPG.Frontend.Backend.Components.Charlie;

@FunctionalInterface
public interface OpenableListener {
    void onOpenStateChanged(boolean isNowOpen);
}
