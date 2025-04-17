#version 130

in vec2 textureCords;
in vec2 lightCords;
in vec3 normal;
in vec3 lightPosition;


uniform sampler2D diffuseMap;
uniform sampler2D lightMap;

uniform bool texturingEnabled;
uniform bool lightingEnabled;
uniform bool lightMapEnabled;

void main() {
    vec4 color = vec4(1);

    if (texturingEnabled) {
        color.rgb = texture2D(diffuseMap, textureCords).rgb;
    }

    if (lightingEnabled) {
        float diffuseTerm1 = min(1.0, max(dot(normal, lightPosition), 0.0));
        color.rgb *= diffuseTerm1;
    }

    color *= gl_Color;

    color.rgb *= texture(lightMap, lightCords).rgb;

    gl_FragColor = color;
}
