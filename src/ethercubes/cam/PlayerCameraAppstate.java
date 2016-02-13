//package ethercubes.cam;
//
//import com.jme3.app.Application;
//import com.jme3.app.state.AppStateManager;
//import com.jme3.bullet.control.CharacterControl;
//import com.jme3.input.ChaseCamera;
//import com.jme3.math.Vector3f;
//import com.jme3.renderer.Camera;
//import com.jme3.scene.Spatial;
//
///**
// *
// * @author Carl
// */
//public class PlayerCameraAppstate extends BaseAppState{
//
//    public PlayerCameraAppstate(){
//        
//    }
//    public enum CameraType{
//        FIRST_PERSON,
//        THIRD_PERSON
//    }
//    private CameraType cameraType;
//    private ChaseCamera chaseCamera;
//
//    @Override
//    public void initialize(AppStateManager stateManager, Application application){
//        super.initialize(stateManager, application);
//        mainApplication.getCamera().setFrustumPerspective(45, (((float) mainApplication.getCamera().getWidth()) / mainApplication.getCamera().getHeight()), 0.1f, 1000);
//        initializeChaseCamera();
//        assignChaseCamera();
//        setCameraType(CameraType.FIRST_PERSON);
//    }
//    
//    private void initializeChaseCamera(){
//        chaseCamera = new ChaseCamera(mainApplication.getCamera(), mainApplication.getInputManager());
//        chaseCamera.setSmoothMotion(true);
//        chaseCamera.setMinDistance(5);
//        chaseCamera.setMaxDistance(40);
//        chaseCamera.setLookAtOffset(new Vector3f(0, ((PlayerAppState.HITBOX.getY() / 1.5f)  * getBlockSize()), 0));
//    }
//    
//    public void onPlayerModelChanged(){
//        assignChaseCamera();
//        setCameraType(cameraType);
//    }
//    
//    private void assignChaseCamera(){
//        getAppState(PlayerAppState.class).getPlayerModel().addControl(chaseCamera);
//    }
//    
//    private float getBlockSize(){
//        return 1;
////        WorldAppState worldAppState = getAppState(WorldAppState.class);
////        return worldAppState.getWorld().getBlockTerrain().getSettings().getBlockSize();
//    }
//
//    public void setCameraType(CameraType cameraType){
//        this.cameraType = cameraType;
//        Mod mod = getAppState(ModAppState.class).getMod();
//        PlayerAppState playerAppState = getAppState(PlayerAppState.class);
//        NiftyAppState niftyAppState = getAppState(NiftyAppState.class);
//        switch(cameraType){
//            case FIRST_PERSON:
//                playerAppState.getPlayerModel().setCullHint(Spatial.CullHint.Always);
//                chaseCamera.setEnabled(false);
//                mainApplication.getFlyByCamera().setEnabled(true);
//                mainApplication.getInputManager().setCursorVisible(false);
//                if(mod.getProperties().getBoolean("isItemBarVisible")){
//                    niftyAppState.getScreenController(ScreenController_IngameMenu.class).setItemBarVisible(true);
//                }
//                if(mod.getProperties().getBoolean("isCrosshairVisible")){
//                    niftyAppState.getScreenController(ScreenController_IngameMenu.class).setCrosshairVisible(true);
//                }
//                break;
//            
//            case THIRD_PERSON:
//                playerAppState.getPlayerModel().setCullHint(Spatial.CullHint.Inherit);
//                chaseCamera.setEnabled(true);
//                mainApplication.getFlyByCamera().setEnabled(false);
//                niftyAppState.getScreenController(ScreenController_IngameMenu.class).setBlockToolsVisible(false);
//                break;
//        }
//    }
//
//    @Override
//    public void update(float lastTimePerFrame){
//        PlayerAppState playerAppState = getAppState(PlayerAppState.class);
//        CharacterControl playerControl = playerAppState.getPlayerControl();
//        Camera camera = mainApplication.getCamera();
//        switch(cameraType){
//            case FIRST_PERSON:
//                camera.setLocation(playerControl.getPhysicsLocation().add(0, 1, 0));
//                break;
//            
//            case THIRD_PERSON:
//                Vector3f walkDirection = playerControl.getWalkDirection();
//                if(walkDirection.length() > 0){
//                    playerControl.setViewDirection(walkDirection);
//                }
//                break;
//        }
//    }
//
//    public CameraType getCameraType(){
//        return cameraType;
//    }
//}
