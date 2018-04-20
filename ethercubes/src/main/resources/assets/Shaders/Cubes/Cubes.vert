uniform mat4 g_WorldViewProjectionMatrix;
attribute vec3 inPosition;

attribute vec3 inTexCoord;
varying vec3 texCoord;

attribute vec3 inNormal;
varying vec3 vertNormal;

attribute float inColor;
varying float vertColor;

void main(){
    texCoord = inTexCoord;
    vertNormal = inNormal;
    vertColor = inColor;
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
}