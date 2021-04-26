#version 330 core

in vec3 passColor;
in vec2 passTextureCoord;

out vec4 outColor;

uniform sampler2D tex;
uniform bool isTransparent;

void main() {
	outColor = texture(tex, passTextureCoord);
	if (outColor.a < 0.5 && isTransparent){
		discard;
	}
}