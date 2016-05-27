package nl.dke12.heightmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/** Simple test showing how to use a height map. Uses {@link HeightField}.
 * @author Xoppa */
public class HeightMapTest{
    HeightField field;
    Renderable ground;
    Environment environment;
    boolean morph = true;
    Texture texture;

    public AssetManager assets;

    public PerspectiveCamera cam;
    public CameraInputController inputController;
    public ModelBatch modelBatch;
    public Model axesModel;
    public ModelInstance axesInstance;
    public boolean showAxes = true;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public final Color bgColor = new Color(0, 0, 0, 1);

    public static void main(String args[])
    {
        HeightMapTest heightMapTest = new HeightMapTest();
        heightMapTest.create();
        ModelBatch modelBatch = new ModelBatch();
        ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();
        heightMapTest.render();
    }

    //@Override
    public void create () {
        if (assets == null)
            assets = new AssetManager();

        //modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 0.1f;
        cam.far = 1000f;
        cam.update();

        //createAxes();

        Gdx.input.setInputProcessor(inputController = new CameraInputController(cam));

        ModelBatch modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -0.5f, -1.0f, -0.8f));

        texture = new Texture(Gdx.files.internal("data/concree2.png"));

        int w = 20, h = 20;
        Pixmap data = new Pixmap(Gdx.files.internal("data/map1.png"));
        field = new HeightField(true, data, true, Usage.Position | Usage.Normal | Usage.ColorUnpacked | Usage.TextureCoordinates);
        data.dispose();
        field.corner00.set(-10f, 0, -10f);
        field.corner10.set(10f, 0, -10f);
        field.corner01.set(-10f, 0, 10f);
        field.corner11.set(10f, 0, 10f);
        field.color00.set(0, 0, 1, 1);
        field.color01.set(0, 1, 1, 1);
        field.color10.set(1, 0, 1, 1);
        field.color11.set(1, 1, 1, 1);
        field.magnitude.set(0f, 5f, 0f);
        field.update();

        ground = new Renderable();
        ground.environment = environment;
        ground.meshPart.mesh = field.mesh;
        ground.meshPart.primitiveType = GL20.GL_TRIANGLES;
        ground.meshPart.offset = 0;
        ground.meshPart.size = field.mesh.getNumIndices();
        ground.meshPart.update();
        ground.material = new Material(TextureAttribute.createDiffuse(texture));
    }

    final float GRID_MIN = -10f;
    final float GRID_MAX = 10f;
    final float GRID_STEP = 1f;

    private void createAxes () {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("grid", GL20.GL_LINES, Usage.Position | Usage.ColorUnpacked, new Material());
        builder.setColor(Color.LIGHT_GRAY);
        for (float t = GRID_MIN; t <= GRID_MAX; t += GRID_STEP) {
            builder.line(t, 0, GRID_MIN, t, 0, GRID_MAX);
            builder.line(GRID_MIN, 0, t, GRID_MAX, 0, t);
        }
        builder = modelBuilder.part("axes", GL20.GL_LINES, Usage.Position | Usage.ColorUnpacked, new Material());
        builder.setColor(Color.RED);
        builder.line(0, 0, 0, 100, 0, 0);
        builder.setColor(Color.GREEN);
        builder.line(0, 0, 0, 0, 100, 0);
        builder.setColor(Color.BLUE);
        builder.line(0, 0, 0, 0, 0, 100);
        axesModel = modelBuilder.end();
        axesInstance = new ModelInstance(axesModel);
    }

    protected boolean loading = false;

    protected void onLoaded () {
    }

    public void render (final Array<ModelInstance> instances) {
        modelBatch.begin(cam);
        if (showAxes) modelBatch.render(axesInstance);
       // if (instances != null) render(modelBatch, instances);
        modelBatch.end();
    }

    public void render () {
        if (loading && assets.update()) {
            loading = false;
            onLoaded();
        }

        inputController.update();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);

        render(instances);
    }

    public void dispose () {
        modelBatch.dispose();
        assets.dispose();
        assets = null;
        axesModel.dispose();
        axesModel = null;

        texture.dispose();
        field.dispose();

    }

}
