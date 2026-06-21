package DeCell.FPG.JavaSlop.ShaderJsonParsing;

import DeCell.FPG.FancyPhaseGlow;
import DeCell.FPG.Shader;
import com.fs.graphics.Sprite;
import com.fs.starfarer.api.combat.ShipAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShaderJsonModel {
    public String title;
    public String id;
    public String fragmentShaderPath;
    public String vertexShaderPath; //?
    public List<ShaderUniformModel> uniforms;
    private Shader shader;

    public ShaderJsonModel(JSONObject jsonObject) {
        try {
            this.title = jsonObject.getString("Title");
            this.id = jsonObject.getString("ID");
            this.fragmentShaderPath = jsonObject.getString("FragmentShaderPath");

            this.vertexShaderPath = jsonObject.optString("VertexShaderPath", "data/shaders/fpg/main.vert");

            this.uniforms = new ArrayList<>();
            if (jsonObject.has("Uniforms")) {
                JSONArray uniformsArray = jsonObject.getJSONArray("Uniforms");
                for (int i = 0; i < uniformsArray.length(); i++) {
                    this.uniforms.add(new ShaderUniformModel(uniformsArray.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public Shader compile() {
        if (shader == null)
            shader = Shader.fromFile(vertexShaderPath, fragmentShaderPath, title);
        return shader;
    }

    public Shader getShader() {
        return shader;
    }

    public void updateUniformValues(ShipAPI ship, Sprite sprite) {
        for (ShaderUniformModel uniform : uniforms) {
            uniform.update(shader.getUniformManager(), ship, sprite);
        }
    }

    public void dispose() {
        this.shader.dispose();
    }

    @Override
    public String toString() {
        return "ShaderJsonModel{" +
                "\ntitle='" + title + '\'' +
                "\n, id='" + id + '\'' +
                "\n, fragmentShaderPath='" + fragmentShaderPath + '\'' +
                "\n, vertexShaderPath='" + vertexShaderPath + '\'' +
                "\n, shader=" + shader +
                (FancyPhaseGlow.Debug ? ", uniforms=" + uniforms : ", uniforms=" + uniforms.size()) +
                "\n}\n";
    }
}
