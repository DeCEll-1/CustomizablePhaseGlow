package DeCell.FPG;

import com.fs.starfarer.api.Global;
import org.apache.log4j.Priority;

public class FancyPhaseGlow {
    public static boolean Debug = false;

    public static void Log(String s) {
        Global.getLogger(FancyPhaseGlow.class).log(Priority.INFO, s);
    }
    public static void LogWarn(String s){
        Global.getLogger(FancyPhaseGlow.class).log(Priority.WARN, s);
    }
    public static void LogErr(String s){
        Global.getLogger(FancyPhaseGlow.class).log(Priority.ERROR, s);
    }
}
