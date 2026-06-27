#version 120

uniform sampler2D textureSampler;
uniform float hull;

uniform vec3 lowHullColor;
uniform vec3 highHullColor;

void main()
{
    vec4 texColor = texture2D(textureSampler, gl_TexCoord[0].st);

    vec3 mixedColor = mix(lowHullColor, highHullColor, clamp(hull, 0.0, 1.0));

    vec3 finalRGB = texColor.rgb * mixedColor * gl_Color.rgb;
    float finalAlpha = texColor.a * gl_Color.a;

    gl_FragColor = vec4(finalRGB, finalAlpha);
}