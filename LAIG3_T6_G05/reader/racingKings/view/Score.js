/**
 * Score class
 * @param scene
 */
function Score(scene){
    this.scene = scene;

    this.unitsCounter = 0;
    this.dozensCounter = 0;
    this.hundredsCounter = 0;

    this.units = new Obj(this.scene, 'scenes/pieces/0.obj');
    this.dozens = new Obj(this.scene, 'scenes/pieces/0.obj');
    this.hundreds = new Obj(this.scene, 'scenes/pieces/0.obj');
}

/**
 * Updates the dozens and units counter
 * @param score amount of points to be added
 */
Score.prototype.update = function(score) {

    if ((this.unitsCounter + score) < 10) {
        this.unitsCounter += score;
        this.dozensCounter += score;
        this.hundredsCounter += score;
    } else if ((this.dozensCounter + score) < 100) {
        this.unitsCounter += score - 10;
        this.dozensCounter += score;
        this.hundredsCounter += score;
    } else {
        this.unitsCounter += score - 100;
        this.dozensCounter += score - 10;
        this.hundredsCounter += score;
    }

    this.units = new Obj(this.scene, 'scenes/pieces/' + this.unitsCounter + '.obj');
    this.dozens = new Obj(this.scene, 'scenes/pieces/' + (this.dozensCounter - this.unitsCounter)/10 + '.obj');
    this.hundreds = new Obj(this.scene, 'scenes/pieces/' + (this.hundredsCounter - this.dozensCounter)/100 + '.obj');
};

/**
 * Displays the counter
 */
Score.prototype.display = function() {

    this.scene.pushMatrix();
        this.scene.scale(0.1, 0.1, 0.1);
        this.scene.pushMatrix();
            this.scene.translate(25, 100, -150);
            this.units.display();
        this.scene.popMatrix();
        this.scene.pushMatrix();
            this.scene.translate(0, 100, -150);
            this.dozens.display();
        this.scene.popMatrix();
        this.scene.pushMatrix();
            this.scene.translate(-25, 100, -150);
            this.hundreds.display();
        this.scene.popMatrix();
    this.scene.popMatrix();

};