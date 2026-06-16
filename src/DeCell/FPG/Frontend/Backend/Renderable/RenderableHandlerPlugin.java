package DeCell.FPG.Frontend.Backend.Renderable;

import DeCell.FPG.FancyPhaseGlow;
import DeCell.FPG.Frontend.Backend.Plugins.PanelPlugin;
import com.fs.starfarer.api.ui.CustomPanelAPI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RenderableHandlerPlugin extends PanelPlugin {
    private List<PluginRenderable> renderBelows = new ArrayList<>();
    private List<PluginRenderable> renderAboves = new ArrayList<>();
    private List<PluginRenderable> debugRenderable = new ArrayList<>();

    public RenderableHandlerPlugin() {
        if (FancyPhaseGlow.DebugUI)
            this.debugRenderable.add(new MonoColorRenderable(
                    new Color(
                            (new Random().nextInt() & 0x00FFFFFF) | (0x55 << 24)
                            , true)));
    }

    public RenderableHandlerPlugin addBelow(PluginRenderable _0) {
        renderBelows.add(_0);
        return this;
    }

    public List<PluginRenderable> getRenderBelows() {
        return renderBelows;
    }

    public <T extends PluginRenderable> T getRenderBelow(int index) {
        return (T) renderBelows.get(index);
    }

    public RenderableHandlerPlugin addAbove(PluginRenderable _0) {
        renderAboves.add(_0);
        return this;
    }

    public List<PluginRenderable> getRenderAboves() {
        return renderAboves;
    }

    public <T extends PluginRenderable> T getRenderAbove(int index) {
        return (T) renderAboves.get(index);
    }

    @Override
    public void init(CustomPanelAPI parent) {
        for (PluginRenderable $_ : renderBelows) {
            $_.init(parent);
        }

        for (PluginRenderable $_ : renderAboves) {
            $_.init(parent);
        }

        for (PluginRenderable $_ : debugRenderable) {
            $_.init(parent);
        }
    }

    @Override
    public void update(CustomPanelAPI parent) {
        for (PluginRenderable $_ : renderBelows) {
            $_.update(parent);
        }

        for (PluginRenderable $_ : renderAboves) {
            $_.update(parent);
        }

        for (PluginRenderable $_ : debugRenderable) {
            $_.update(parent);
        }

        this.needsUpdate = false;
    }

    @Override
    public void advance(float amount) {
        if (updateNeeded())
            this.needsUpdate = true;
    }

    @Override
    public void renderBelow(float alphaMult) {
        if (updateNeeded())
            return;

        super.renderBelow(alphaMult);
        for (PluginRenderable $_ : renderBelows) {
            if ($_.render)
                $_.renderBelow(alphaMult);
        }

        for (PluginRenderable $_ : debugRenderable) {
            if ($_.render)
                $_.renderBelow(alphaMult);
        }
    }

    @Override
    public void render(float alphaMult) {
        if (updateNeeded())
            return;

        for (PluginRenderable $_ : renderAboves) {
            if ($_.render)
                $_.render(alphaMult);
        }

        for (PluginRenderable $_ : debugRenderable) {
            if ($_.render)
                $_.render(alphaMult);
        }
    }

    private boolean updateNeeded() {
        return
                renderBelows.stream().anyMatch(PluginRenderable::needsUpdate)
                        || renderAboves.stream().anyMatch(PluginRenderable::needsUpdate)
                        || debugRenderable.stream().anyMatch(PluginRenderable::needsUpdate);
    }
}
