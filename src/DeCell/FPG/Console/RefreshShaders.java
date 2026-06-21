package DeCell.FPG.Console;

import DeCell.FPG.FancyPhaseGlow;
import org.lazywizard.console.BaseCommand;

public class RefreshShaders implements BaseCommand {
    @Override
    public CommandResult runCommand(String args, CommandContext context) {
        FancyPhaseGlow.UpdateShaders();
        return CommandResult.SUCCESS;
    }
}
