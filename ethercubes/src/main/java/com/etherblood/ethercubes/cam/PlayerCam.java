package com.etherblood.ethercubes.cam;

import com.etherblood.ethercubes.units.Hitbox;
import com.etherblood.ethercubes.world.BlockWorld;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author Philipp
 */
public class PlayerCam implements AnalogListener, ActionListener {

    private static String[] mappings = new String[]{
        "FLYCAM_Left",
        "FLYCAM_Right",
        "FLYCAM_Up",
        "FLYCAM_Down",
        "FLYCAM_StrafeLeft",
        "FLYCAM_StrafeRight",
        "FLYCAM_Forward",
        "FLYCAM_Backward",
        "FLYCAM_ZoomIn",
        "FLYCAM_ZoomOut",
        "FLYCAM_RotateDrag",
        "FLYCAM_Rise",
        "FLYCAM_Lower",
        "FLYCAM_InvertY"
    };
    protected Camera cam;
    protected Vector3f initialUpVec;
    protected float rotationSpeed = 1f;
    protected float moveSpeed = 20f;
    protected float zoomSpeed = 1f;
//    protected MotionAllowedListener motionAllowed = null;
    protected boolean enabled = true;
    protected boolean dragToRotate = false;
    protected boolean canRotate = false;
    protected boolean invertY = false;
    protected InputManager inputManager;
    private final Hitbox playerHitbox;
    private final BlockWorld world;

    /**
     * Creates a new FlyByCamera to control the given Camera object.
     * @param cam
     */
    public PlayerCam(Camera cam, Hitbox playerHitbox, BlockWorld world){
        this.cam = cam;
        initialUpVec = cam.getUp().clone();
        this.playerHitbox = playerHitbox;
        this.world = world;
    }
    
    /**
     * Registers the FlyByCamera to receive input events from the provided
     * Dispatcher.
     *
     * @param inputManager
     */
    public void registerWithInput(InputManager inputManager) {
        this.inputManager = inputManager;

        // both mouse and button - rotation of cam
        inputManager.addMapping("FLYCAM_Left", new MouseAxisTrigger(MouseInput.AXIS_X, true),
                new KeyTrigger(KeyInput.KEY_LEFT));

        inputManager.addMapping("FLYCAM_Right", new MouseAxisTrigger(MouseInput.AXIS_X, false),
                new KeyTrigger(KeyInput.KEY_RIGHT));

        inputManager.addMapping("FLYCAM_Up", new MouseAxisTrigger(MouseInput.AXIS_Y, false),
                new KeyTrigger(KeyInput.KEY_UP));

        inputManager.addMapping("FLYCAM_Down", new MouseAxisTrigger(MouseInput.AXIS_Y, true),
                new KeyTrigger(KeyInput.KEY_DOWN));

        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping("FLYCAM_ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("FLYCAM_ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        // keyboard only WASD for movement and WZ for rise/lower height
        inputManager.addMapping("FLYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("FLYCAM_StrafeRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("FLYCAM_Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("FLYCAM_Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_Z));

        inputManager.addListener(this, mappings);
        inputManager.setCursorVisible(dragToRotate || !enabled);
    }

    protected void rotateCamera(float value, Vector3f axis) {
        if (dragToRotate) {
            if (canRotate) {
//                value = -value;
            } else {
                return;
            }
        }

        Matrix3f mat = new Matrix3f();
        mat.fromAngleNormalAxis(rotationSpeed * value, axis);

        Vector3f up = cam.getUp();
        Vector3f left = cam.getLeft();
        Vector3f dir = cam.getDirection();

        mat.mult(up, up);
        mat.mult(left, left);
        mat.mult(dir, dir);

        Quaternion q = new Quaternion();
        q.fromAxes(left, up, dir);
        q.normalizeLocal();

        cam.setAxes(q);
    }

    protected void zoomCamera(float value) {
        // derive fovY value
        float h = cam.getFrustumTop();
        float w = cam.getFrustumRight();
        float aspect = w / h;

        float near = cam.getFrustumNear();

        float fovY = FastMath.atan(h / near)
                / (FastMath.DEG_TO_RAD * .5f);
        float newFovY = fovY + value * 0.1f * zoomSpeed;
        if (newFovY > 0f) {
            // Don't let the FOV go zero or negative.
            fovY = newFovY;
        }

        h = FastMath.tan(fovY * FastMath.DEG_TO_RAD * .5f) * near;
        w = h * aspect;

        cam.setFrustumTop(h);
        cam.setFrustumBottom(-h);
        cam.setFrustumLeft(-w);
        cam.setFrustumRight(w);
    }

    protected void riseCamera(float value) {
        Vector3f vel = new Vector3f(0, value * moveSpeed, 0);
        Vector3f pos = cam.getLocation().clone();

//        if (motionAllowed != null) {
//            motionAllowed.checkMotionAllowed(pos, vel);
//        } else {
//            pos.addLocal(vel);
//        }

        playerHitbox.move(world, vel);

        cam.setLocation(playerHitbox.getTopCenter().mult(3));
    }

    protected void moveCamera(float value, boolean sideways) {
        Vector3f vel = new Vector3f();
        Vector3f pos = cam.getLocation().clone();

        if (sideways) {
            cam.getLeft(vel);
        } else {
            cam.getDirection(vel);
        }
        vel.multLocal(value * moveSpeed);

//        if (motionAllowed != null) {
//            motionAllowed.checkMotionAllowed(pos, vel);
//        } else {
//            pos.addLocal(vel);
//        }
        playerHitbox.move(world, vel);

        cam.setLocation(playerHitbox.getTopCenter().mult(3));
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (!enabled) {
            return;
        }
        switch (name) {
            case "FLYCAM_Left":
                rotateCamera(value, initialUpVec);
                break;
            case "FLYCAM_Right":
                rotateCamera(-value, initialUpVec);
                break;
            case "FLYCAM_Up":
                rotateCamera(-value * (invertY ? -1 : 1), cam.getLeft());
                break;
            case "FLYCAM_Down":
                rotateCamera(value * (invertY ? -1 : 1), cam.getLeft());
                break;
            case "FLYCAM_Forward":
                moveCamera(value, false);
                break;
            case "FLYCAM_Backward":
                moveCamera(-value, false);
                break;
            case "FLYCAM_StrafeLeft":
                moveCamera(value, true);
                break;
            case "FLYCAM_StrafeRight":
                moveCamera(-value, true);
                break;
            case "FLYCAM_Rise":
                riseCamera(value);
                break;
            case "FLYCAM_Lower":
                riseCamera(-value);
                break;
            case "FLYCAM_ZoomIn":
                zoomCamera(value);
                break;
            case "FLYCAM_ZoomOut":
                zoomCamera(-value);
                break;
        }
    }

    @Override
    public void onAction(String name, boolean value, float tpf) {
        if (!enabled) {
            return;
        }

        if (name.equals("FLYCAM_RotateDrag") && dragToRotate) {
            canRotate = value;
            inputManager.setCursorVisible(!value);
        } else if (name.equals("FLYCAM_InvertY")) {
            // Toggle on the up.
            if (!value) {
                invertY = !invertY;
            }
        }
    }
}
