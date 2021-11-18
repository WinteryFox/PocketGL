#version 450 core

layout (location = 0) in vec3 position;

layout (location = 1) out vec3 outPosition;

layout (binding = 0) uniform Camera {
    mat4 model;
    mat4 view;
    mat4 projection;
} camera;

void main() {
    gl_Position = camera.projection * camera.view * camera.model * vec4(position, 1.0f);
    outPosition = position;
}
