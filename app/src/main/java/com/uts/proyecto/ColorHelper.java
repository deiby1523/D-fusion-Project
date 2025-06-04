package com.uts.proyecto;

import androidx.core.graphics.ColorUtils;

public class ColorHelper {
    public static int reducirBrillo(int color, float factor) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);

        hsl[2] = Math.max(0f, hsl[2] * factor);

        return ColorUtils.HSLToColor(hsl);
    }
}
