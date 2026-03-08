#version 330 core

in vec2 vUV;

uniform sampler2D uTex;

uniform vec4 uFontColor;
uniform vec4 uFontPos;
uniform float uScreenPxRange;

out vec4 fragColor;

float median(float r, float g, float b) {
    return max(min(r, g), min(max(r, g), b));
}

void main() {
    vec2 uv = mix(uFontPos.xy, uFontPos.zw, vUV);
    vec3 msd = texture2D(uTex, uv).rgb;
    float sd = median(msd.r, msd.g, msd.b);
    float screenPxDistance = uScreenPxRange * (sd - 0.5);
    float opacity = clamp(screenPxDistance + 0.5, 0.0, 1.0);
    fragColor = vec4(uFontColor.rgb, uFontColor.a * opacity);
}
