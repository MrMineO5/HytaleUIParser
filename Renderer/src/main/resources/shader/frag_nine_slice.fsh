#version 330 core

in vec2 vUV;

uniform sampler2D uTex;

uniform vec4 uSourceRect;
uniform vec4 uTargetRect;

out vec4 fragColor;

float remap3(float x, float a, float b, float na, float nb) {
    const float eps = 1e-6;

    float t0 = clamp(x/max(a, eps), 0.0, 1.0);
    float t1 = clamp((x-a) / max(b-a, eps), 0.0, 1.0);
    float t2 = clamp((x-b) / max(1.0-b, eps), 0.0, 1.0);

    float y = 0.0;
    y += na * t0;
    y += (nb - na) * t1;
    y += (1.0 - nb) * t2;
    return y;
}

void main() {
    vec2 uv = vec2(
        remap3(vUV.x, uSourceRect.x, uSourceRect.z, uTargetRect.x, uTargetRect.z),
        remap3(vUV.y, uSourceRect.y, uSourceRect.w, uTargetRect.y, uTargetRect.w)
    );
    fragColor = texture2D(uTex, uv);
}
