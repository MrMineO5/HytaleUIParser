#version 330 core

layout(location = 0) in vec2 aPos; // Unit quad (0, 0) to (1, 1)
layout(location = 1) in vec2 aUV; // Same, could remove but maybe useful

uniform vec2 uViewport; // Window (width, height)
uniform vec4 uRect; // Window-space rect (x, y, w, h)

out vec2 vUV; // UV for frag shader

void main() {
    vUV = aUV;

    vec2 px = uRect.xy + uRect.zw * aPos;
    vec2 v = (px / uViewport) * 2.0 - 1.0;
    gl_Position = vec4(
        v.x, -v.y,
        0.0, 1.0
    );
}
