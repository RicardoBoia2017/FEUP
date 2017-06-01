package com.feup.superslimefootball.view.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.feup.superslimefootball.view.MenuView;

public class HowToPlayMenuState extends MenuState {
    public HowToPlayMenuState(MenuView menuView) {
        super(menuView);
    }

    @Override
    public void drawButtons() {

        Texture howToPlayBackground = this.menu.getAssetManager().get("howToPlayBackground.png", Texture.class);
        Texture goBack = this.menu.getAssetManager().get("goBack.png", Texture.class);

        this.menu.getBatch().draw(howToPlayBackground, 0.0f, 0.0f);
        this.menu.getBatch().draw(goBack, Gdx.graphics.getWidth()*(10.0f/13.0f), Gdx.graphics.getHeight()*(17.0f/20.0f));

    }

    @Override
    public void handleMouse() {
        if(Gdx.input.justTouched()) {
            if(touchButton(10.0f/13.0f,17.0f/20.0f))
                this.menuView.setState(new OptionsMenuState(this.menuView));
        }
    }
}
