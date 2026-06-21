package DeCell.FPG;

import DeCell.FPG.JavaSlop.ShaderJsonParsing.ShaderJsonModel;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import org.apache.log4j.Priority;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL40.*;
import static org.lwjgl.opengl.GL41.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.opengl.GL44.*;
import static org.lwjgl.opengl.GL45.*;
// i would global static these if java had that feature

public class FancyPhaseGlow {
    public static boolean Debug = false;
    public static boolean DebugUI = false;

    public static List<ShaderJsonModel> PhaseShaders = new ArrayList<>();
    public final static String FPGPrefix = "FPG_";
    public final static String ShipPrefix = "ShaderShip_";

    public static ShaderJsonModel getShaderForShip(ShipAPI ship) {
        String key = FPGPrefix + ShipPrefix + ship.getFleetMemberId();

        if (!Global.getSector().getPersistentData().containsKey(key))
            return null;

        Object data = Global.getSector().getPersistentData().get(key);
        String shaderId = (String) data;
        if (shaderId == null) return null;

        // TODO: make a system for per ship shader editing
        for (ShaderJsonModel phaseShader : PhaseShaders) {
            if (Objects.equals(phaseShader.id, shaderId))
                return phaseShader;
            // java streams suck im missing LINQ :(
        }
        return null;
    }

    public static void setShaderForShip(ShipAPI ship, ShaderJsonModel shaderJsonModel) {
        String key = FPGPrefix + ShipPrefix + ship.getFleetMemberId();

        if (shaderJsonModel == null) {
            Global.getSector().getPersistentData().remove(key);
            return;
        }
        Global.getSector().getPersistentData().put(key, shaderJsonModel.id);

        if (Debug)
            Log("PUT " + shaderJsonModel + " TO " + key);
    }

    public static void Log(String s) {
        Global.getLogger(FancyPhaseGlow.class).log(Priority.INFO, s);
    }

    public static void LogWarn(String s) {
        Global.getLogger(FancyPhaseGlow.class).log(Priority.WARN, s);
    }

    public static void LogErr(String s) {
        Global.getLogger(FancyPhaseGlow.class).log(Priority.ERROR, s);
    }

    public static void UpdateShaders() {
        for (ShaderJsonModel phaseShader : PhaseShaders) {
            phaseShader.dispose();
        }
        PhaseShaders.clear();
        try {
            JSONObject master = Global.getSettings().getMergedJSON("data/shaders/fpg/PhaseShaders.hjson");
            JSONArray masterArray = master.getJSONArray("Shaders");
            for (int i = 0; i < masterArray.length(); i++) {
                ShaderJsonModel zaza = new ShaderJsonModel(masterArray.getJSONObject(i));
                if (zaza.fragmentShaderPath.startsWith("./"))
                    zaza.fragmentShaderPath = zaza.fragmentShaderPath.replace("./", "data/shaders/fpg/");
                if (zaza.vertexShaderPath.startsWith("./"))
                    zaza.vertexShaderPath = zaza.vertexShaderPath.replace("./", "data/shaders/fpg/");

                zaza.compile();
                PhaseShaders.add(zaza);
            }

        } catch (IOException | JSONException e) { // İ HATE YOU JAVA, İ HATE YOU
            throw new RuntimeException("IRONED WITH HATE, FUELED BY SPITE\n" + e.getMessage());
        }
    }

    public static class Patterns {
        public final static Pattern NUMBER_ONLY = Pattern.compile("-?[0-9]*");
        public final static Pattern DECIMAL_ONLY = Pattern.compile("-?[0-9]*\\.?[0-9]*");

        public static Pattern decimalWithMaxDecimalPlaces(int decimalPlaces) {
            if (decimalPlaces == 0)
                return NUMBER_ONLY;
            return Pattern.compile("-?[0-9]*\\.?[0-9]{0," + decimalPlaces + "}");
        }
    }
}
