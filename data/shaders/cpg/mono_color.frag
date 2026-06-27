#version 120

uniform sampler2D textureSampler;

uniform vec3 color;

void main()
{
    vec4 texColor = texture2D(textureSampler, gl_TexCoord[0].st);

    vec3 finalRGB = texColor.rgb * color.rgb * gl_Color.rgb;
    float finalAlpha = texColor.a * gl_Color.a;

    gl_FragColor = vec4(finalRGB, finalAlpha);
}