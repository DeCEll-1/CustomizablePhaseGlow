package DeCell.FPG;

import org.lwjgl.opengl.Display;


import java.awt.Color;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Misc {


    /**
     * Converts a List of java.awt.Color into a flat float array for GLSL uniform vec3 colors[]
     * Format: [r0, g0, b0, r1, g1, b1, ..., rn, gn, bn]
     * All values are normalized to 0.0 - 1.0
     */
    public static float[] colorsToFloatArray(List<Color> colors) {
        if (colors == null || colors.isEmpty()) {
            return new float[0];
        }

        int numColors = colors.size();
        float[] array = new float[numColors * 3];   // 3 floats per color (RGB)

        for (int i = 0; i < numColors; i++) {
            Color c = colors.get(i);

            array[i * 3 + 0] = c.getRed()   / 255.0f;
            array[i * 3 + 1] = c.getGreen() / 255.0f;
            array[i * 3 + 2] = c.getBlue()  / 255.0f;
        }

        return array;
    }










    // from cmukits
    public static void openGLForMisc() {
        final int w = (int) (Display.getWidth() * Display.getPixelScaleFactor());
        final int h = (int) (Display.getHeight() * Display.getPixelScaleFactor());

        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glViewport(0, 0, w, h);
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, w, 0, h, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glTranslatef(0.01f, 0.01f, 0);
    }

    public static void closeGLForMisc() {
        glDisable(GL_BLEND);
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glPopAttrib();
    }

}
