package ethercubes.main;

import com.jme3.scene.Node;
import java.util.concurrent.Callable;

/**
 *
 * @author Philipp
 */
public class GuiService {
    private EthercubesApplication app;
    
    public GuiService(EthercubesApplication app) {
        this.app = app;
    }

    public void attach(final Node node) {
        app.enqueue(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                app.getRootNode().attachChild(node);
                return null;
            }
        });
    }

    public void detach(final Node node) {
        app.enqueue(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                node.removeFromParent();
                return null;
            }
        });
    }
    
}
