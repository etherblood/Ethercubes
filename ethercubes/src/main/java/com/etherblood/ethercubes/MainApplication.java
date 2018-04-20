package com.etherblood.ethercubes;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.etherblood.ethercubes.chunk.compression.CompressedChunks;
import com.etherblood.ethercubes.chunk.implementation.ArrayChunk;
import com.etherblood.ethercubes.chunk.implementation.ArrayChunkFactory;
import com.etherblood.ethercubes.chunk.implementation.ChunkPoolImpl;
import com.etherblood.ethercubes.data.ChunkSize;
import com.etherblood.ethercubes.data.GlobalBlockPosition;
import com.etherblood.ethercubes.display.meshing.ChunkMesher;
import com.etherblood.ethercubes.display.meshing.CubesMaterial;
import com.etherblood.ethercubes.display.meshing.implementation.ConcurrentGreedyMesher;
import com.etherblood.ethercubes.display.models.MaterialFactory;
import com.etherblood.ethercubes.display.models.ModelObject;
import com.etherblood.ethercubes.pagination.PaginatedWorldManager;
import com.etherblood.ethercubes.pagination.TaskExecutor;
import com.etherblood.ethercubes.pagination.WorldGen;
import com.etherblood.ethercubes.pagination.WorldGraph;
import com.etherblood.ethercubes.settings.implementation.ChunkSettingsImpl;
import com.etherblood.ethercubes.settings.implementation.TestBlockSettings;
import com.etherblood.ethercubes.settings.implementation.TileBlockSettings;
import com.etherblood.ethercubes.statistics.TimeStatistics;
import com.etherblood.ethercubes.units.Hitbox;
import com.etherblood.ethercubes.world.implementation.AllmightyBlockChunkWorld;
import com.etherblood.ethercubes.world.worldgen.ConcurrentTerrainChunkFactory;
import com.etherblood.ethercubes.world.worldgen.OreGenerator;
import com.etherblood.ethercubes.world.worldgen.TreeGenerator;
import com.etherblood.ethercubes.world.worldgen.WorldGenerator;
import com.etherblood.ethercubes.world.worldgen.WorldGeneratorImpl;
import com.jme3.asset.plugins.FileLocator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Philipp
 */
public class MainApplication extends SimpleApplication {

//    private Hitbox playerHitbox = new Hitbox(0.4f, 1.9f);
    private Material material;
//    private ModelObject steve;
    PaginatedWorldManager<ArrayChunk> worldManager;
    private final ConcurrentLinkedDeque<Runnable> renderTasks = new ConcurrentLinkedDeque<>();
//    private int range = 8;
    private boolean wireframe = false;
    private boolean dayNight = false;
    private final long seed = 238457;
    AllmightyBlockChunkWorld<ArrayChunk> world;
    private TileBlockSettings blockSettings;
    private ChunkSettingsImpl chunkSettings;
//    private CamChunkLoadManager camChunks;
    DirectionalLight directionalLight = new DirectionalLight();
    DirectionalLightShadowRenderer directionalLightShadowRenderer;
    private ExecutorService executor = Executors.newFixedThreadPool(5);
    private ChunkPoolImpl<ArrayChunk> chunkPool;
    private boolean active = true;
    private final Thread logicLoop = new Thread(new Runnable() {
        @Override
        public void run() {
            while(active) {
                long time = System.nanoTime();
                long start = TimeStatistics.TIME_STATISTICS.start();
                Vector3f pos = cam.getLocation().divide(3).divide(new Vector3f(chunkSettings.getSize().getX(), chunkSettings.getSize().getY(), chunkSettings.getSize().getZ()));
                worldManager.update(pos);
                TimeStatistics.TIME_STATISTICS.end(start, "ChunkUpdates");
                time -= System.nanoTime();
                time /= 1000000;
                time += 10;//cap at 100 fps => 10 millis per frame
                if(time > 0) {
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    });
    
    public static void main(String[] args) {
        MainApplication app = new MainApplication();

//        NeighborVisibilityCalculatorImpl.test();

        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        settings.setVSync(true);
        app.setSettings(settings);

        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        String resourcePath = "./src/main/resources/assets";
        getAssetManager().registerLocator(resourcePath, FileLocator.class);
        
        blockSettings = new TileBlockSettings();
        chunkSettings = new ChunkSettingsImpl(new ChunkSize(32, 32, 32));
//        chunkSettings = new ChunkSettingsImpl(new ChunkSize(64, 64, 64));
        material = new CubesMaterial(getAssetManager(), "Textures/tiles.png");//"Textures/terrain.png");
        RenderState state = material.getAdditionalRenderState();
        state.setWireframe(wireframe);
        initInput();

        ChunkSize size = chunkSettings.getSize();
        world = new AllmightyBlockChunkWorld(size);
        ChunkFactory<ArrayChunk> environment = new ConcurrentTerrainChunkFactory<ArrayChunk>(seed, chunkSettings);
//                new RoomsGenerator<ArrayChunk>();
        ChunkMesher mesher = new ConcurrentGreedyMesher(blockSettings, size);
        final CompressedChunks<ArrayChunk> compressedChunks = new CompressedChunks<ArrayChunk>();

//        flyCam.setEnabled(false);
//        new PlayerCam(cam, playerHitbox, world).registerWithInput(inputManager);
        inputManager.setCursorVisible(false);
//        playerHitbox.setBottomCenter(new Vector3f(-10, 23, 16));
        cam.setLocation(new Vector3f(-10, 23, 16));
        cam.lookAtDirection(new Vector3f(1, -0.56f, -1), Vector3f.UNIT_Y);
        
        flyCam.setMoveSpeed(300);

        WorldGenerator<ArrayChunk> worldGen = new WorldGeneratorImpl(environment, new TreeGenerator(seed), size, new OreGenerator(seed));
        WorldGraph<ArrayChunk> worldGraph = new WorldGraph<ArrayChunk>(renderTasks, mesher, material);
        chunkPool = new ChunkPoolImpl<ArrayChunk>(new ArrayChunkFactory(size));
        WorldGen<ArrayChunk> worldGen1 = new WorldGen<ArrayChunk>(worldGen, world, compressedChunks, chunkPool);
        worldManager = new PaginatedWorldManager<ArrayChunk>(world, worldGraph, new TaskExecutor(executor), worldGen1);

//        camChunks = new CamChunkLoadManager(worldManager);
        rootNode.attachChild(worldGraph.getRootNode());
        worldGraph.getRootNode().setLocalScale(3);
        worldGraph.getRootNode().setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        initStuff();
        setDisplayStatView(true);
        getCamera().setFrustumFar(5000);
        
//        steve = new ModelObject(new MaterialFactory(assetManager), renderTasks, "/Models/steve/skin_default.xml");
//        steve.setAnimationName("walk");
//        steve.setLocalTranslation(0, 10, 0);
////        Spatial steve = assetManager.loadModel("/Models/steve/steve.j3o");//new ModelObject(new MaterialFactory(assetManager), renderTasks, "/Models/steve/skin_default.xml");
////        steve.setCullHint(Spatial.CullHint.Never);
////        steve.setQueueBucket(RenderQueue.Bucket.Opaque);
//        steve.setLocalScale(steve.getLocalScale().divide(2));
//        worldGraph.getRootNode().attachChild(steve);
        
        logicLoop.start();
    }

    protected void initStuff() {
        getRootNode().attachChild(SkyFactory.createSky(getAssetManager(), "Textures/sky.jpg", true));

        Vector3f lightDirection = new Vector3f(0.4f, -0.8f, 0.6f).normalize();//new Vector3f(-0.2f, -1, -0.8f).normalizeLocal();
        directionalLight.setDirection(lightDirection);
        directionalLight.setColor(new ColorRGBA(1f, 1f, 1f, 1f));
//        directionalLightShadowRenderer = new DirectionalLightShadowRenderer(getAssetManager(), 2048, 3);
//        directionalLightShadowRenderer.setLight(directionalLight);
//        directionalLightShadowRenderer.setShadowIntensity(0.3f);
//        getViewPort().addProcessor(directionalLightShadowRenderer);
        
//        FilterPostProcessor fogPPS = new FilterPostProcessor(assetManager);
//        FogFilter fog = new FogFilter(ColorRGBA.randomColor(), 1f, 500f);
//        fogPPS.addFilter(fog);
//        viewPort.addProcessor(fogPPS);
    }
    boolean perFrame = false;
    float totalTime = 0;
//    float speedY = 0;
//    float playerSpeedY = 0;
    
    @Override
    public void simpleUpdate(float elapsedSeconds) {
        inputManager.setCursorVisible(false);
        TimeStatistics.TIME_STATISTICS.nextFrame();
        long start = TimeStatistics.TIME_STATISTICS.start();
        totalTime += elapsedSeconds;
//        steve.setLocalRotation(new Quaternion().fromAngleNormalAxis(totalTime, Vector3f.UNIT_Y));
//        blockSettings.getMaterial().setFloat("Time", totalTime);

        Runnable runnable;
        while ((runnable = renderTasks.poll()) != null) {
            runnable.run();
        }
        
        if (dayNight) {
            float timeSin = (float) Math.sin(totalTime / 10);
            float brightness = timeSin / 2;
            brightness += 0.5f;
            Vector3f ambientLight = new Vector3f(brightness, brightness, brightness);
            material.setVector3(CubesMaterial.AMBIENT_LIGHT, ambientLight);
            float timeCos = (float) Math.cos(totalTime / 10);
            Vector3f lightDirection = new Vector3f(timeCos, -Math.abs(timeSin), timeCos / 4).normalize();
            material.setVector3(CubesMaterial.LIGHT_DIRECTION, lightDirection.negate());
            directionalLight.setDirection(lightDirection);
//            directionalLightShadowRenderer.setShadowIntensity(0.3f * Math.max(0, timeSin));
        }
        
        Hitbox hitbox = new Hitbox(0.4f, 1.9f);
//        AABBCCHitbox steveBox = new AABBCCHitbox(0.4f, 1.8f);
//        steveBox.setBottomCenter(steve.getLocalTranslation());
//        hitbox.setBottomCenter(steve.getLocalTranslation());
//        steve.setLocalTranslation(steveBox.getBottomCenter());
        
//        Vector3f delta = cam.getLocation().divide(3).subtract(steveBox.getBottomCenter());
//        Vector3f delta = cam.getLocation().divide(3).subtract(hitbox.getBottomCenter());
//        delta.y = 0;
//        float distance = elapsedSeconds * 5;
//        if(delta.length() > distance) {
//            delta.normalizeLocal().multLocal(distance);
//            Quaternion quaternion = new Quaternion();
//            quaternion.lookAt(cam.getLocation().divide(3).subtract(steveBox.getBottomCenter()).mult(new Vector3f(1, 0, 1)), Vector3f.UNIT_Y);
//            quaternion.lookAt(cam.getLocation().divide(3).subtract(hitbox.getBottomCenter()).mult(new Vector3f(1, 0, 1)), Vector3f.UNIT_Y);
//            steve.setLocalRotation(quaternion);
//        }
//        float gravity = -50;
//        speedY += gravity * elapsedSeconds;
//        playerSpeedY += gravity * elapsedSeconds / 2;
//        delta.y = speedY * elapsedSeconds;
//        moveHitbox(steveBox, delta);
//        float y = hitbox.getBottomCenter().y;
//        hitbox.move(world, delta);
//        if(Math.abs(hitbox.getBottomCenter().y - y) < Math.abs(delta.y / 2)) {
//            if(delta.y < 0) {
//                speedY = 20;//jump
//            } else {
//                speedY = 0;
//            }
//        }
        
//        y = playerHitbox.getBottomCenter().y;
//        float deltaY = playerSpeedY * elapsedSeconds;
//        playerHitbox.move(world, new Vector3f(0, deltaY, 0));
//        if(Math.abs(playerHitbox.getBottomCenter().y - y) < Math.abs(deltaY / 2)) {
//            playerSpeedY = 0;
//        }
        
//        steve.setLocalTranslation(steveBox.getBottomCenter());
//        steve.setLocalTranslation(hitbox.getBottomCenter());
        
//        cam.setLocation(playerHitbox.getTopCenter().mult(3));

        TimeStatistics.TIME_STATISTICS.end(start, "Update");
    }
//    
//    private void moveHitbox(AABBCCHitbox steveBox, Vector3f delta) {
//        Vector3f pos = steveBox.getBottomCenter();
//        pos.addLocal(delta);
//        int x = (int)pos.x;
//        int z = (int)pos.z;
//        int y = (int)Math.floor(pos.y);
//        while (world.getBlock(new GlobalBlockPosition(x, y, z)) != 0) {
//            y++;
//        }
//        if(pos.y < y) {
//            pos.y = y;
//        }
////        Vector3f min = steveBox.min();
////        Vector3f max = steveBox.max();
////        for (int x = (int) Math.floor(min.x); x < max.x; x++) {
////            for (int y = (int) Math.floor(min.y); y < max.y; y++) {
////                for (int z = (int) Math.floor(min.z); z < max.z; z++) {
////                    if (world.getBlock(new GlobalBlockPosition(x, y, z)) != 0) {
////                        if (steveBox.intersects(x, y, z)) {
////                            steveBox.solveIntersectionWith(x, y, z);
////                        }
////                    }
////                }
////            }
////        }
//    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            active = false;
            logicLoop.join();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        executor.shutdown();
    }

    private void initInput() {
        inputManager.addMapping("lookatNorth", new KeyTrigger(KeyInput.KEY_F3));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
                cam.lookAtDirection(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
            }
        }, "lookatNorth");
        
        inputManager.addMapping("incRange", new KeyTrigger(KeyInput.KEY_ADD));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
                synchronized(worldManager.getPagination()) {
                    worldManager.getPagination().setRadius(worldManager.getPagination().getRadius() + 1);
                }
                System.out.println("range: " + worldManager.getPagination().getRadius());
            }
        }, "incRange");
        inputManager.addMapping("decRange", new KeyTrigger(KeyInput.KEY_SUBTRACT));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
                synchronized(worldManager.getPagination()) {
                    worldManager.getPagination().setRadius(worldManager.getPagination().getRadius() - 1);
                }
                System.out.println("range: " + worldManager.getPagination().getRadius());
            }
        }, "decRange");

        inputManager.addMapping("toggleWireframe", new KeyTrigger(KeyInput.KEY_F1));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
                RenderState state = material.getAdditionalRenderState();
                wireframe = !wireframe;
                state.setWireframe(wireframe);
            }
        }, "toggleWireframe");

        inputManager.addMapping("toggleDayNight", new KeyTrigger(KeyInput.KEY_F2));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
                dayNight = !dayNight;
                if (!dayNight) {
                    material.setVector3("AmbientLight", Vector3f.UNIT_XYZ);
                }
            }
        }, "toggleDayNight");

        inputManager.addMapping("displayTotalTimes", new KeyTrigger(KeyInput.KEY_PGDN));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
                System.out.println("");
                System.out.print(TimeStatistics.TIME_STATISTICS.displayString());
                System.out.println("");
            }
        }, "displayTotalTimes");

        inputManager.addMapping("resetStats", new KeyTrigger(KeyInput.KEY_DELETE));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
//                MemoryStatistics.MEMORY_STATISTICS.clear();
                TimeStatistics.TIME_STATISTICS.clear();
            }
        }, "resetStats");

        inputManager.addMapping("setBlock", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
                
                Vector3f currentPos = cam.getWorldCoordinates(new Vector2f(0, 0), 0).divide(3);
                Vector3f direction = cam.getDirection().divide(10);

                GlobalBlockPosition last = null;
                GlobalBlockPosition current = null;
                int left = 1000;
                do {
                    if (left-- == 0) {
                        return;
                    }
                    last = current;
                    current = new GlobalBlockPosition((int) currentPos.getX(), (int) currentPos.getY(), (int) currentPos.getZ());
                    currentPos.addLocal(direction);
                } while (world.getBlock(current) == TestBlockSettings.AIR);

                if (last != null) {
                    worldManager.setBlock(last, (byte) (new Random().nextInt(TileBlockSettings.NUM_BLOCKS - 1) + 1));
                }
            }
        }, "setBlock");
        inputManager.addMapping("delBlock", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
                Vector3f currentPos = cam.getWorldCoordinates(new Vector2f(0, 0), 0).divide(3);
                Vector3f direction = cam.getDirection().divide(10);

                GlobalBlockPosition current = null;
                int left = 1000;
                do {
                    if (left-- == 0) {
                        return;
                    }
                    current = new GlobalBlockPosition((int) currentPos.getX(), (int) currentPos.getY(), (int) currentPos.getZ());
                    currentPos.addLocal(direction);
                } while (world.getBlock(current) == TestBlockSettings.AIR);

                worldManager.setBlock(current, TestBlockSettings.AIR);
            }
        }, "delBlock");
        inputManager.addMapping("fillChunk", new KeyTrigger(KeyInput.KEY_INSERT));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
                Vector3f currentPos = cam.getWorldCoordinates(new Vector2f(0, 0), 0).divide(3);
                Vector3f direction = cam.getDirection().divide(3);

                GlobalBlockPosition current = null, last = null;
                int left = 1000;
                do {
                    if (left-- == 0) {
                        return;
                    }
                    last = current;
                    current = new GlobalBlockPosition((int) currentPos.getX(), (int) currentPos.getY(), (int) currentPos.getZ());
                    currentPos.addLocal(direction);
                } while (world.getBlock(current) == TestBlockSettings.AIR);

                if (last != null) {
                    worldManager.setChunk(last, TestBlockSettings.WATER);
                }
            }
        }, "fillChunk");
        inputManager.addMapping("delChunk", new KeyTrigger(KeyInput.KEY_DELETE));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
                Vector3f currentPos = cam.getWorldCoordinates(new Vector2f(0, 0), 0).divide(3);
                Vector3f direction = cam.getDirection().divide(3);

                GlobalBlockPosition current = null;
                int left = 1000;
                do {
                    if (left-- == 0) {
                        return;
                    }
                    current = new GlobalBlockPosition((int) currentPos.getX(), (int) currentPos.getY(), (int) currentPos.getZ());
                    currentPos.addLocal(direction);
                } while (world.getBlock(current) == TestBlockSettings.AIR);

                worldManager.setChunk(current, TestBlockSettings.AIR);
            }
        }, "delChunk");

        inputManager.addMapping("jump", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
//                cam.setLocation(new Vector3f(-797.8698f, 107.08753f, 3188.575f));
                cam.setLocation(new Vector3f(-177.86205f, -19.75606f, -2820.8303f));
            }
        }, "jump");

        inputManager.addMapping("gc", new KeyTrigger(KeyInput.KEY_PGUP));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
                chunkPool.sout();
            }
        }, "gc");
    }

}
