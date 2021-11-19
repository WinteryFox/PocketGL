#version 450 core

layout (location = 1) in vec3 position;

out vec4 color;

void main() {
    color = vec4((position + 1.0f) / 2.0f, 1.0f);
}
