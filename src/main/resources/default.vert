#version 450 core

layout (location = 0) in vec3 position;

layout (location = 1) out vec3 outPosition;

layout (std140, binding = 2) uniform Camera {
    mat4 model;
    mat4 projection;
    mat4 view;
} camera;

void main() {
    gl_Position = camera.view * camera.projection * camera.model * vec4(position, 1.0f);
    outPosition = position;
}
