package nl.dke12.heightmap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Terra extends Game {

    private PerspectiveCamera camera;
    private CameraInputController camController;

    private Mesh mesh;

    private ShaderProgram shader;
    private Texture terrainTexture;

    private final Matrix3 normalMatrix = new Matrix3();

    private static final float[] lightPosition = { 5, 35, 5 };
    private static final float[] ambientColor = { 0.2f, 0.2f, 0.2f, 1.0f };
    private static final float[] diffuseColor = { 0.5f, 0.5f, 0.5f, 1.0f };
    private static final float[] specularColor = { 0.7f, 0.7f, 0.7f, 1.0f };

    private static final float[] fogColor = { 0.2f, 0.1f, 0.6f, 1.0f };

    private Matrix4 model = new Matrix4();
    private Matrix4 modelView = new Matrix4();

    private final String vertexShader =
            "attribute vec4 a_position; \n" +
                    "attribute vec3 a_normal; \n" +
                    "attribute vec2 a_texCoord; \n" +
                    "attribute vec4 a_color; \n" +

                    "uniform mat4 u_MVPMatrix; \n" +
                    "uniform mat3 u_normalMatrix; \n" +

                    "uniform vec3 u_lightPosition; \n" +

                    "varying float intensity; \n" +
                    "varying vec2 texCoords; \n" +
                    "varying vec4 v_color; \n" +

                    "void main() { \n" +
                    "    vec3 normal = normalize(u_normalMatrix * a_normal); \n" +
                    "    vec3 light = normalize(u_lightPosition); \n" +
                    "    intensity = max( dot(normal, light) , 0.0); \n" +

                    "    v_color = a_color; \n" +
                    "    texCoords = a_texCoord; \n" +

                    "    gl_Position = u_MVPMatrix * a_position; \n" +
                    "}";

    private final String fragmentShader =
            "#ifdef GL_ES \n" +
                    "precision mediump float; \n" +
                    "#endif \n" +

                    "uniform vec4 u_ambientColor; \n" +
                    "uniform vec4 u_diffuseColor; \n" +
                    "uniform vec4 u_specularColor; \n" +

                    "uniform sampler2D u_texture; \n" +
                    "varying vec2 texCoords; \n" +
                    "varying vec4 v_color; \n" +

                    "varying float intensity; \n" +

                    "void main() { \n" +
                    "    gl_FragColor = v_color * intensity * texture2D(u_texture, texCoords); \n" +
                    "}";

    @Override
    public void create() {



    }

    @Override
    public void render() {

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camController.update();
        camera.update();


        // This is wrong?
        model.setToRotation(new Vector3(0, 1, 0), 45f);
        modelView.set(camera.view).mul(model);


        terrainTexture.bind();

        shader.begin();

        shader.setUniformMatrix("u_MVPMatrix", camera.combined);
        shader.setUniformMatrix("u_normalMatrix", normalMatrix.set(modelView).inv().transpose());

        shader.setUniform3fv("u_lightPosition", lightPosition, 0, 3);
        shader.setUniform4fv("u_ambientColor", ambientColor, 0, 4);
        shader.setUniform4fv("u_diffuseColor", diffuseColor, 0, 4);
        shader.setUniform4fv("u_specularColor", specularColor, 0, 4);

        shader.setUniformi("u_texture", 0);

        mesh.render(shader, GL20.GL_LINE_STRIP);

        shader.end();

    }
}