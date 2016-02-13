/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.display.models;


import java.net.URL;
import java.util.List;
import java.util.LinkedList;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import java.io.IOException;
import org.jdom2.Attribute;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author Carl
 */
public class ModelSkin{
   
    public ModelSkin(MaterialFactory materialFactory, String fileResourcePath){
        this.materialFactory = materialFactory;
        this.fileResourceURL = Util.getResourceURL(fileResourcePath);
        loadFile();
    }
    private static final String[] FILE_EXTENSIONS = new String[]{"j3o", "mesh.xml", "blend"};
    private final MaterialFactory materialFactory;
    private URL fileResourceURL;
    private Element rootElement;
    private Element modelElement;
    private Element positionElement;
    private Element materialElement;
    private Element modifiersElement;
    private String name;
    private float modelNormScale;
    private Vector3f modelScale;
    private float materialAmbient;
    private LinkedList<ModelModifier> modelModifiers = new LinkedList<ModelModifier>();
   
    private void loadFile(){
        try{
            Document document = new SAXBuilder().build(fileResourceURL);
            rootElement = document.getRootElement();
            name = rootElement.getAttributeValue("name");
            modelElement = rootElement.getChild("model");
            positionElement = modelElement.getChild("position");
            materialElement = modelElement.getChild("material");
            modifiersElement = modelElement.getChild("modifiers");
            modelNormScale = getAttributeValue(modelElement, "normScale", 1);
            modelScale = getAttributeValue(modelElement, "scale", Vector3f.UNIT_XYZ);
            materialAmbient = getAttributeValue(materialElement, "ambient", 0.15f);
        }catch(JDOMException | IOException ex){
            System.out.println("Error while loading object skin '" + fileResourceURL + "'");
        }
    }
   
    private boolean getAttributeValue(Element element, String attributeName, boolean defaultValue){
        return (getAttributeValue(element, attributeName, (defaultValue?1:0)) == 1);
    }
   
    private float getAttributeValue(Element element, String attributeName, float defaultValue){
        if(element != null){
            Attribute attribute = element.getAttribute(attributeName);
            if(attribute != null){
                try{
                    return attribute.getFloatValue();
                }catch(DataConversionException ex){
                }
            }
        }
        return defaultValue;
    }
   
    private Vector3f getAttributeValue(Element element, String attributeName, Vector3f defaultValue){
        if(element != null){
            Attribute attribute = element.getAttribute(attributeName);
            if(attribute != null){
                String[] coordinates = attribute.getValue().split(",");
                if(coordinates.length == 3){
                    float x = Float.parseFloat(coordinates[0]);
                    float y = Float.parseFloat(coordinates[1]);
                    float z = Float.parseFloat(coordinates[2]);
                    return new Vector3f(x, y, z);
                }
                else{
                    try{
                        float value = attribute.getFloatValue();
                        return new Vector3f(value, value, value);
                    }catch(DataConversionException ex){
                    }
                }
            }
        }
        return defaultValue;
    }
   
    public Spatial loadSpatial(){
        Spatial spatial = loadModel();
        loadMaterial(spatial);
        loadPosition(spatial);
        loadModifiers();
        applyGeometryInformation(spatial);
        return spatial;
    }
   
    private Spatial loadModel(){
        String modelPath = getModelFilePath();
        Spatial spatial = materialFactory.getAssetManager().loadModel(modelPath);
        spatial.setLocalScale(modelScale.mult(modelNormScale));
        return spatial;
    }
   
    private String getModelFilePath(){
        for(int i=0;i<FILE_EXTENSIONS.length;i++){
            String modelFilePath = getModelFilePath(FILE_EXTENSIONS[i]);
            if(Util.existsResource("/" + modelFilePath)){
                return modelFilePath;
            }
        }
        return null;
    }
   
    private String getModelFilePath(String fileExtension){
        return "Models/" + name + "/" + name + "." + fileExtension;
    }
   
    private void loadMaterial(Spatial spatial){
        if(materialElement != null){
            List<Element> materialElements = materialElement.getChildren();
            for(int i=0;i<materialElements.size();i++){
                Element currentMaterialElement = materialElements.get(i);
                String materialDefintion = currentMaterialElement.getText();
                Material material = null;
                if(currentMaterialElement.getName().equals("color")){
                    float[] colorComponents = Util.parseToFloatArray(materialDefintion.split(","));
                    ColorRGBA colorRGBA = new ColorRGBA(colorComponents[0], colorComponents[1], colorComponents[2], colorComponents[3]);
                    material = materialFactory.generateLightingMaterial(colorRGBA);
                }
                else if(currentMaterialElement.getName().equals("texture")){
                    String textureFilePath = getResourcesFilePath() + currentMaterialElement.getText();
                    material = materialFactory.generateUnshadedMaterial(textureFilePath);
                    //[jME 3.0 Stable] Hardware skinning currently doesn't seem to support normal maps correctly
//                    if(!Settings.getBoolean("hardware_skinning")){
                        loadTexture(material, "NormalMap", currentMaterialElement.getAttributeValue("normalMap"));
//                    }
                    loadTexture(material, "AlphaMap", currentMaterialElement.getAttributeValue("alphaMap"));
                    loadTexture(material, "SpecularMap", currentMaterialElement.getAttributeValue("specularMap"));
                    loadTexture(material, "GlowMap", currentMaterialElement.getAttributeValue("glowMap"));
                }
                if(material != null){
                    String filter = currentMaterialElement.getAttributeValue("filter", "bilinear");
                    if(filter.equals("nearest")){
                        materialFactory.setFilter_Nearest(material);
                    }
                    try{
                        int childIndex = currentMaterialElement.getAttribute("index").getIntValue();
                        Geometry child = (Geometry) getChild(spatial, childIndex);
                        if(getAttributeValue(currentMaterialElement, "alpha", false)){
                            child.setQueueBucket(RenderQueue.Bucket.Transparent);
                            material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                        }
                        child.setMaterial(material);
                    }catch(Exception ex){
                        System.out.println("Error while reading material for object '" + name + "'");
                    }
                }
            }
        }
    }
    
    private static Spatial getChild(Spatial spatial, int... index){
        for(int i=0;i<index.length;i++){
            if(spatial instanceof Node){
                Node node = (Node) spatial;
                spatial = node.getChild(index[i]);
            }
            else{
                break;
            }
        }
        return spatial;
    }
   
    private void loadTexture(Material material, String materialParameter, String textureName){
        if(textureName != null){
            Texture texture = materialFactory.loadTexture(getResourcesFilePath() + textureName);
            material.setTexture(materialParameter, texture);
        }
    }
   
    private String getResourcesFilePath(){
        return "Models/" + name + "/resources/";
    }
   
    private void loadPosition(Spatial spatial){
        if(positionElement != null){
            Element locationElement = positionElement.getChild("location");
            if(locationElement != null){
                float[] location = Util.parseToFloatArray(locationElement.getText().split(","));
                spatial.setLocalTranslation(location[0], location[1], location[2]);
            }
            Element directionElement = positionElement.getChild("direction");
            if(directionElement != null){
                float[] direction = Util.parseToFloatArray(directionElement.getText().split(","));
                setLocalRotation(spatial, new Vector3f(direction[0], direction[1], direction[2]));
            }
        }
    }
    
    private static void setLocalRotation(Spatial spatial, Vector3f rotation){
        Vector3f lookAtLocation = spatial.getWorldTranslation().add(rotation);
        spatial.lookAt(lookAtLocation, Vector3f.UNIT_Y);
    }
   
    private void loadModifiers(){
        modelModifiers.clear();
        if(modifiersElement != null){
            for(Object childObject : modifiersElement.getChildren("modifier")){
                Element modifierElement = (Element) childObject;
                ModelModifier modelModifier = Util.createObjectByClassName(modifierElement.getText(), ModelModifier.class);
                if(modelModifier != null){
                    modelModifiers.add(modelModifier);
                }
            }
        }
    }

    public LinkedList<ModelModifier> getModelModifiers(){
        return modelModifiers;
    }
   
    private void applyGeometryInformation(Spatial spatial){
        LinkedList<Geometry> geometryChilds = getAllGeometryChilds(spatial);
        Vector3f scaleVector = modelScale.mult(modelNormScale);
        for(int i=0;i<geometryChilds.size();i++){
            Geometry geometry = geometryChilds.get(i);
            Material material = geometry.getMaterial();
            materialFactory.generateAmbientColor(material, materialAmbient);
            material.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
            RigidBodyControl rigidBodyControl = geometry.getControl(RigidBodyControl.class);
            if(rigidBodyControl != null){
                rigidBodyControl.getCollisionShape().setScale(scaleVector);
            }
            geometry.setUserData("layer", 3);
        }
    }
    
    private static LinkedList<Geometry> getAllGeometryChilds(Spatial spatial){
        LinkedList<Geometry> geometryChilds = new LinkedList<Geometry>();
        if(spatial instanceof Node){
            Node node = (Node) spatial;
            for(int i=0;i<node.getChildren().size();i++){
                Spatial child = node.getChild(i);
                if(child instanceof Geometry){
                    Geometry geometry = (Geometry) child;
                    geometryChilds.add(geometry);
                }
                else{
                    geometryChilds.addAll(getAllGeometryChilds(child));
                }
            }
        }
        return geometryChilds;
    }
   
    public String getIconFilePath(){
        String iconFilePath = "Models/" + name + "/icon.jpg";
        if(!Util.existsResource("/" + iconFilePath)){
            iconFilePath = "Interface/images/icon_unknown.jpg";
        }
        return iconFilePath;
    }

    public String getName(){
        return name;
    }

    public Vector3f getModelScale(){
        return modelScale;
    }

    public float getModelNormScale(){
        return modelNormScale;
    }
   
    public float getMaterialAmbient(){
        return materialAmbient;
    }
}
