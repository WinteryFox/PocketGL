#version 450 core

layout (location = 0) in vec3 position;

layout (location = 1) out vec3 outPosition;

void main() {
    gl_Position = vec4(position, 1.0);
    outPosition = position;
}
