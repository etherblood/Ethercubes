package ethercubes.main;

import com.jme3.system.AppSettings;
import ethercubes.display.meshing.CubesMaterial;

/**
 *
 * @author Philipp
 */
public class StartupMain {

    public static void main(String[] args) throws InterruptedException {
        EthercubesApplication app = new EthercubesApplication();

//        NeighborVisibilityCalculatorImpl.test();

        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        settings.setVSync(true);
        app.setSettings(settings);

        app.setShowSettings(false);
        app.start();
        Thread.sleep(1000);//workaround for race condition
        new CubesStartup().run(new GuiService(app), new CubesMaterial(app.getAssetManager(), "Textures/tiles.png"));
    }
}
