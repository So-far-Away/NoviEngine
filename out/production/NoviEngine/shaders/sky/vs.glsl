#version 460 core

in vec3 position;

out vec3 worldPosition;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
	gl_Position = projection * view * model * vec4(position, 1.0);
	worldPosition = (model * vec4(position, 1.0)).xyz * 10 + 1000;
}