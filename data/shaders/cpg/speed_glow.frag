#version 120

uniform sampler2D textureSampler;
uniform float speed;

uniform vec3 lowSpeedColor;
uniform vec3 highSpeedColor;

void main()
{
    vec4 texColor = texture2D(textureSampler, gl_TexCoord[0].st);

    vec3 mixedColor = mix(lowSpeedColor, highSpeedColor, clamp(speed, 0.0, 1.0));

    vec3 finalRGB = texColor.rgb * mixedColor * gl_Color.rgb;
    float finalAlpha = texColor.a * gl_Color.a;

    gl_FragColor = vec4(finalRGB, finalAlpha);
}