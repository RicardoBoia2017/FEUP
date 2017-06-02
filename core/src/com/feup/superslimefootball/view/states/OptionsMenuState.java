package com.feup.superslimefootball.view.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.feup.superslimefootball.view.MenuView;

public class OptionsMenuState extends MenuState {
    /**
     * The width of the option buttons.
     */
    public static final float OPTIONS_BUTTONS_WIDTH = 3.0f/50.0f;

    /**
     * The height of the option buttons.
     */
    public static final float OPTIONS_BUTTONS_HEIGHT = 1.3f/10.0f;

    /*
     * Booleans of each option in the GoalLimit
     */
    boolean three = true, five = false, seven = false;

    /*
     * Booleans of sound and comments options
     */
    boolean soundOption = false, commentsOption = false;

    public OptionsMenuState(MenuView menuView) {
        super(menuView);
    }

    @Override
    public void drawButtons() {

        Texture howToPlay = this.menu.getAssetManager().get("howToPlay.png", Texture.class);
        Texture sound = this.menu.getAssetManager().get("sound.png", Texture.class);
        Texture comments = this.menu.getAssetManager().get("comments.png", Texture.class);
        Texture goalLimit = this.menu.getAssetManager().get("goalLimit.png", Texture.class);
        Texture goBack = this.menu.getAssetManager().get("goBack.png", Texture.class);

        this.menu.getBatch().draw(howToPlay, Gdx.graphics.getWidth()*(1.0f/13.0f), Gdx.graphics.getHeight()*(17.0f/20.0f));
        this.menu.getBatch().draw(sound, Gdx.graphics.getWidth()*(1.0f/5.0f), Gdx.graphics.getHeight()*(1.0f/25.0f));
        this.menu.getBatch().draw(comments, Gdx.graphics.getWidth()*(2.0f/5.0f), Gdx.graphics.getHeight()*(1.0f/25.0f));
        this.menu.getBatch().draw(goalLimit, Gdx.graphics.getWidth()*(3.0f/5.0f), Gdx.graphics.getHeight()*(1.0f/25.0f));
        this.menu.getBatch().draw(goBack, Gdx.graphics.getWidth()*(10.0f/13.0f), Gdx.graphics.getHeight()*(17.0f/20.0f));
        drawSoundComments();
        drawGoalLimit();

    }

    @Override
    public void handleMouse() {
        if(Gdx.input.justTouched()) {
            if (touchButton(1.0f / 13.0f, 17.0f / 20.0f, BUTTONS_WIDTH, BUTTONS_HEIGHT))
                this.menuView.setState(new HowToPlayMenuState(this.menuView));
            else if(touchButton(10.0f/13.0f,17.0f/20.0f, BUTTONS_WIDTH, BUTTONS_HEIGHT))
                this.menuView.setState(new InitialMenuState(this.menuView));
            handleSoundCommentsMouse();
            handleGoalLimitMouse();
        }
    }

    public void handleSoundCommentsMouse(){
        if (touchButton(5.0f/20.0f, 9.0f/50.0f, 875.0f/10000.0f, 6.0f/50.0f))
            if(soundOption)
                this.soundOption = false;
            else
                this.soundOption = true;
        else if (touchButton(9.0f/20.0f, 9.0f/50.0f, 875.0f/10000.0f, 6.0f/50.0f))
            if(commentsOption)
                this.commentsOption = false;
            else
                this.commentsOption = true;
    }

    public void handleGoalLimitMouse(){
        if (touchButton(30.0f/50.0f, 1.0f / 5.0f, OPTIONS_BUTTONS_WIDTH, OPTIONS_BUTTONS_HEIGHT))
            setGoalLimit(true,false,false);
        else if (touchButton(33.0f/50.0f, 1.0f / 5.0f, OPTIONS_BUTTONS_WIDTH, OPTIONS_BUTTONS_HEIGHT))
            setGoalLimit(false,true,false);
        else if (touchButton(36.0f/50.0f, 1.0f / 5.0f, OPTIONS_BUTTONS_WIDTH, OPTIONS_BUTTONS_HEIGHT))
            setGoalLimit(false,false,true);
    }

    public void setGoalLimit(boolean three, boolean five, boolean seven){
        this.three = three;
        this.five = five;
        this.seven = seven;
    }

    public void drawSoundComments(){
        Texture onButton = this.menu.getAssetManager().get("optionsButtons/onButton.png", Texture.class);
        Texture offButton = this.menu.getAssetManager().get("optionsButtons/offButton.png", Texture.class);

        if(soundOption)
            this.menu.getBatch().draw(offButton, Gdx.graphics.getWidth()*(5.0f/20.0f), Gdx.graphics.getHeight()*(9.0f/50.0f));
        else
            this.menu.getBatch().draw(onButton, Gdx.graphics.getWidth()*(5.0f/20.0f), Gdx.graphics.getHeight()*(9.0f/50.0f));
        if(commentsOption)
            this.menu.getBatch().draw(offButton, Gdx.graphics.getWidth()*(9.0f/20.0f), Gdx.graphics.getHeight()*(9.0f/50.0f));
        else
            this.menu.getBatch().draw(onButton, Gdx.graphics.getWidth()*(9.0f/20.0f), Gdx.graphics.getHeight()*(9.0f/50.0f));
    }

    public void drawGoalLimit(){
        Texture threeChosen = this.menu.getAssetManager().get("optionsButtons/threeChosen.png", Texture.class);
        Texture fiveChosen = this.menu.getAssetManager().get("optionsButtons/fiveChosen.png", Texture.class);
        Texture sevenChosen = this.menu.getAssetManager().get("optionsButtons/sevenChosen.png", Texture.class);
        Texture threeNotChosen = this.menu.getAssetManager().get("optionsButtons/threeNotChosen.png", Texture.class);
        Texture fiveNotChosen = this.menu.getAssetManager().get("optionsButtons/fiveNotChosen.png", Texture.class);
        Texture sevenNotChosen = this.menu.getAssetManager().get("optionsButtons/sevenNotChosen.png", Texture.class);

        if(three)
            this.menu.getBatch().draw(threeChosen, Gdx.graphics.getWidth()*(30.0f/50.0f), Gdx.graphics.getHeight()*(1.0f/5.0f));
        else
            this.menu.getBatch().draw(threeNotChosen, Gdx.graphics.getWidth()*(30.0f/50.0f), Gdx.graphics.getHeight()*(1.0f/5.0f));
        if(five)
            this.menu.getBatch().draw(fiveChosen, Gdx.graphics.getWidth()*(33.0f/50.0f), Gdx.graphics.getHeight()*(1.0f/5.0f));
        else
            this.menu.getBatch().draw(fiveNotChosen, Gdx.graphics.getWidth()*(33.0f/50.0f), Gdx.graphics.getHeight()*(1.0f/5.0f));
        if(seven)
            this.menu.getBatch().draw(sevenChosen, Gdx.graphics.getWidth()*(36.0f/50.0f), Gdx.graphics.getHeight()*(1.0f/5.0f));
        else
            this.menu.getBatch().draw(sevenNotChosen, Gdx.graphics.getWidth()*(36.0f/50.0f), Gdx.graphics.getHeight()*(1.0f/5.0f));
    }

}
