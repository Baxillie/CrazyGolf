package nl.dke13.physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;


public class Ball {

    //mass of dynamic object is always 1 unit
    private final float DECCELERATION = 0.95f; //arbitrary number depending on input for velocity
    private ModelInstance object;
    private ObjectBox box;
    private Vector3 velocity;
    private Vector3 position;

    //stuff for the arrow
    ModelInstance arrow;
    Material arrowMaterial;
    boolean displayArrow;
    ModelBuilder mb;
    Vector3 arrowPointer;

    public Ball(ModelInstance object, float modelX, float modelY, float modelZ, float modelWidth, float modelHeight, float modelDepth)
    {
        this.object = object;
        this.box = new ObjectBox(modelX, modelY, modelZ, modelWidth, modelHeight, modelDepth);
        velocity = new Vector3(0,0,0);
        position = new Vector3(modelX,modelY,modelZ);

        mb = new ModelBuilder();
        displayArrow = true;
        arrowPointer = new Vector3(position.x, position.y + 3, position.z);
        arrowMaterial = new Material(ColorAttribute.createDiffuse(Color.FIREBRICK));
        arrow = new ModelInstance(mb.createArrow(position.x, position.y, position.z,
                arrowPointer.x, arrowPointer.y, arrowPointer.z, 0.2f, 0.05f, 100, 1, arrowMaterial, VertexAttributes.Usage.Position));
    }

    public void updateVelocity(Vector3 velocity)
    {
        if(!velocity.isZero(0.001f))
        {
            this.velocity.x = velocity.x * DECCELERATION;
            this.velocity.y = velocity.y * DECCELERATION;
            this.velocity.z = velocity.z * DECCELERATION;
        }
        else
        {
            this.velocity.set(0, 0, 0);
        }
    }

    //update object position according to velocity
    public void update()
    {
        updateVelocity(velocity);
        object.transform.translate(velocity);
        position.add(velocity);
        box.setXYZ(velocity.x, velocity.y, velocity.z);
    }

    private void updateArrowPointer()
    {

    }

    public void setVelocity(Vector3 velocity)
    {
        this.velocity = velocity;
    }

    public ModelInstance getModel()
        {
            return object;
        }

    public ObjectBox getBox()
        {
            return box;
        }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getVelocity()
        {
            return velocity;
        }

    public void moveArrow(float x, float y, float z)
    {
        arrow = new ModelInstance(mb.createArrow(position.x, position.y, position.z,
            arrowPointer.x + x, arrowPointer.y + y, arrowPointer.z + z, 0.2f, 0.05f, 100, 1, arrowMaterial, VertexAttributes.Usage.Position));

    }

    public boolean displayArrow()
    {
        return velocity.isZero();
    }

    public ModelInstance getArrow()
    {
        return arrow;
    }

}


