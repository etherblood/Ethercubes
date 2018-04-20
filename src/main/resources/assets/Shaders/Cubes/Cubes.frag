#if defined(DISCARD_ALPHA)
    uniform float m_AlphaDiscardThreshold;
#endif

uniform vec3 m_AmbientLight;
uniform vec2 m_NumTiles;
//uniform float m_Time;
uniform vec3 m_LightDirection;
uniform sampler2D m_ColorMap;

varying vec3 texCoord;
varying vec3 vertNormal;
varying float vertColor;

void main(){
    vec4 color = vec4(1.0);

    float tile = round(texCoord.z);//round to fix floating point inaccuracy introduced by interpolation
    vec2 tex = vec2(mod(tile, m_NumTiles.x), floor(tile / m_NumTiles.x));
    tex += vec2(mod(texCoord.x, 1.0), mod(texCoord.y, 1.0));
    tex /= m_NumTiles;

    color *= texture2D(m_ColorMap, tex);

    #if defined(DISCARD_ALPHA)
        if(color.a < m_AlphaDiscardThreshold){
           discard;
        }
    #endif
    
    color.rgb *= vertColor;
    color.rgb *= 0.7 * m_AmbientLight + 0.3 * dot(vertNormal, m_LightDirection);//TODO: get values from material instead of hardcoding

    gl_FragColor = color;
}