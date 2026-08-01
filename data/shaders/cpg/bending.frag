#version 120

uniform sampler2D textureSampler;
uniform float time;
uniform float strength; // Suggestion: 0.1 to 0.5 range

void main()
{
    // 1. Center the UV coordinates (remap from 0.0-1.0 to -0.5 to 0.5)
    vec2 center = vec2(0.5, 0.5);
    vec2 uv = gl_TexCoord[0].st - center;

    // 2. Calculate distance from center
    float dist = length(uv);

    // 3. Create the radial distortion factor
    // Using smoothstep or simple math to make it bulge/pinch toward the center
    // We multiply 'dist' by the strength to create the "bend"
    float distortion = sin(dist * 20.0 - time * 5.0) * strength;
    
    // 4. Offset the UVs based on the direction (uv normalized)
    vec2 distortedUV = center + uv * (1.0 + distortion);
    
    // 5. Sample the texture
    vec4 texColor = texture2D(textureSampler, distortedUV);

    // 6. Apply colors
    vec3 finalRGB = texColor.rgb * gl_Color.rgb;
    float finalAlpha = texColor.a * gl_Color.a;

    gl_FragColor = vec4(finalRGB, finalAlpha);
}