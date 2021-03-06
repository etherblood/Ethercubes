package com.etherblood.ethercubes.cam;

import com.etherblood.ethercubes.MainApplication;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;

/**
 *
 * @author Carl
 */
public class BaseAppState extends AbstractAppState{

    public BaseAppState(){
        
    }
    protected MainApplication mainApplication;

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        mainApplication = (MainApplication) application;
    }
    
    protected <T extends AppState> T getAppState(Class<T> appStateClass){
        return mainApplication.getStateManager().getState(appStateClass);
    }
}
