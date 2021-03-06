package com.feup.superslimefootball.view.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.feup.superslimefootball.SuperSlimeFootball;
import com.feup.superslimefootball.network.NetworkManager;

import static com.feup.superslimefootball.view.GameView.VIEWPORT_HEIGHT;
import static com.feup.superslimefootball.view.GameView.VIEWPORT_WIDTH;

/**
 * The type Multi player menu state.
 */
public class MultiPlayerMenuState extends MenuState {

    /**
     * the network manager of the menu
     */
    NetworkManager networkManager;

    /**
     * Creates a MultiPlayer MenuState in the game
     *
     * @param game the game
     */
    public MultiPlayerMenuState(SuperSlimeFootball game) {
        super(game);

        networkManager = NetworkManager.getInstance();
        Thread thread = new Thread(networkManager);
        thread.start();
    }


    @Override
    public void drawButtons() {

        Texture refresh = this.game.getAssetManager().get("refresh.png", Texture.class);
        Texture findIP = this.game.getAssetManager().get("findIP.png", Texture.class);
        Texture goBack = this.game.getAssetManager().get("goBack.png", Texture.class);

        this.game.getBatch().draw(refresh, VIEWPORT_WIDTH*(1.0f/5.0f), VIEWPORT_HEIGHT*(1.0f/25.0f));
        this.game.getBatch().draw(findIP, VIEWPORT_WIDTH*(3.0f/5.0f), VIEWPORT_HEIGHT*(1.0f/25.0f));
        this.game.getBatch().draw(goBack, VIEWPORT_WIDTH*(1.0f/13.0f), VIEWPORT_HEIGHT*(17.0f/20.0f));
    }

    @Override
    public void handleMouse() {
        if(Gdx.input.justTouched()) {
            if(touchButton(1.0f/13.0f,17.0f/20.0f, BUTTONS_WIDTH, BUTTONS_HEIGHT))
                this.game.setGameState(new InitialMenuState(this.game));
       /*     if (touchButton(1.0f/5.0f,1.0f/25.0f, BUTTONS_WIDTH, BUTTONS_HEIGHT))
                // refresh
            else if(touchButton(3.0f/5.0f,1.0f/25.0f, BUTTONS_WIDTH, BUTTONS_HEIGHT))
                // find ip*/
        }
    }

    @Override
    public boolean isConnected() {
        return networkManager.isConnected();
    }

    @Override
    public boolean isServer() {
        return networkManager.isServer();
    }
}
