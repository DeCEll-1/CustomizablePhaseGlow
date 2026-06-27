#version 120

uniform sampler2D textureSampler;
uniform float flux;

uniform vec3 lowFluxColor;
uniform vec3 highFluxColor;

void main()
{
    vec4 texColor = texture2D(textureSampler, gl_TexCoord[0].st);

    vec3 mixedColor = mix(lowFluxColor, highFluxColor, clamp(flux, 0.0, 1.0));

    vec3 finalRGB = texColor.rgb * mixedColor * gl_Color.rgb;
    float finalAlpha = texColor.a * gl_Color.a;

    gl_FragColor = vec4(finalRGB, finalAlpha);
}