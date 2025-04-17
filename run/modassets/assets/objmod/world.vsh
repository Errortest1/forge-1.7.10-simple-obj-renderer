#version 130

in vec3 in_position;
in vec2 in_textureCords;
in vec3 in_normal;

out vec2 textureCords;
out vec2 lightCords;
out vec3 normal;
out vec3 lightPosition;

uniform bool lightingEnabled;
uniform vec2 lightMapCords;
uniform vec3 lightSourcePosition;

void main(void) {
    gl_Position = gl_ModelViewProjectionMatrix * vec4(in_position, 1.0);
    textureCords = in_textureCords;
    normal = normalize(gl_NormalMatrix * in_normal);
    lightPosition = normalize(gl_NormalMatrix * lightSourcePosition);
    gl_FrontColor = gl_Color;

    lightCords = lightMapCords;
}
