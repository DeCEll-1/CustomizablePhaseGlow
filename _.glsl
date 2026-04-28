
int iArmCount = 16;
float iSpeed = 1.0;
float iThickness = 0.3;
int iNumColors = 4;

vec3 iColors[4] = vec3[4](
        vec3(0.0, 1.0, 1.0),
        vec3(1.0, 0.0, 1.0),
        vec3(1.0, 1.0, 0.0),
        vec3(1.0, .05, 0.0)
    );

void mainImage(out vec4 fragColor, in vec2 fragCoord)
{
    vec2 texUV = fragCoord / iResolution.xy;

    vec2 uv = (fragCoord / iResolution.xy) * 2.0 - 1.0;
    uv.x *= iResolution.x / iResolution.y;

    float angle = atan(uv.y, uv.x);

    angle += iTime * iSpeed;

    float spiral = sin(angle * float(iArmCount)) * 0.5 + 0.5;
    spiral = smoothstep(0.5 - iThickness, 0.5 + iThickness, spiral);

    float colorIndex = mod(angle * float(iArmCount) / (3.14159 * 2.0), float(iNumColors));
    int idx1 = int(floor(colorIndex));
    int idx2 = idx1 + 1;
    if (idx2 >= iNumColors) idx2 = 0;
    float frac = fract(colorIndex);

    vec3 spiralColor = mix(iColors[idx1], iColors[idx2], frac);

    spiralColor *= (1.0 * 0.6);

    vec3 finalRGB = mix(vec3(1.0), spiralColor, spiral * 0.85);
    float finalAlpha = 1.0;

    fragColor = vec4(finalRGB, finalAlpha);
}
