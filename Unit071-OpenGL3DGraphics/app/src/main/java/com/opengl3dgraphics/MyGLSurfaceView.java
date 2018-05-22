package com.opengl3dgraphics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLSurfaceView extends GLSurfaceView
        implements GLSurfaceView.Renderer {

    private final static int BYTE_PER_FLOAT = 4,
            SIZE_VERTEX_LOCATION = 3,   // 每一個頂點座標的資料量。
            SIZE_VERTEX_COLOR = 4;   // 每一個頂點顏色的資料量。

    // OpenGL專用的buffer，儲存物件的頂點座標和顏色
    private FloatBuffer mVertLocationBuf,
            mVertColorBuf;

    // OpenGL物件的頂點座標，在程式中會將它們設定給OpenGL專用的buffer
    // 每一列是一個頂點的xyz座標
    private float[] m3DObjVertLocation = {
            -0.5f, -0.25f, 0.0f,
            0.5f, -0.25f, 0.0f,
            0.0f, 0.56f, 0.0f
    };

    // OpenGL物件的頂點顏色，在程式中會將它們設定給OpenGL專用的buffer
    // 每一列是一個頂點的顏色，顏色值的順序為rgba，a是alpha值
    private float[] m3DObjVertColor = {
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f
    };

    // 眼睛使用的轉換矩陣
    private float[] mViewMatrix = new float[16];

    // 把世界空間座標，轉換到投影幕座標
    // 也就是把3D物體投影到投影幕（眼睛）
    private float[] mProjectionMatrix = new float[16];

    // 3D物體使用的轉換矩陣
    private float[] mModelMatrix = new float[16];

    // 儲存以上三種轉換矩陣結合後的結果
    private float[] mMVPMatrix = new float[16];

    // 最遠方的底色
    private float backColorR = 1.0f,
            backColorG = 1.0f,
            backColorB = 1.0f,
            backColorA = 1.0f;

    // 眼睛（投影幕）的位置
    final float eyeX = 0.0f;
    final float eyeY = 0.0f;
    final float eyeZ = 1.5f;

    // 眼睛看的方向，Z軸負值表示前方
    final float lookX = 0.0f;
    final float lookY = 0.0f;
    final float lookZ = -1.0f;

    // 頭頂的方向，Y軸正值表示正上方
    final float headUpX = 0.0f;
    final float headUpY = 1.0f;
    final float headUpZ = 0.0f;

    private float mfRotateAng = 0f;

    // 把資料傳給OpenGL需要用到的屬性，我們要用這些屬性傳入資料
    int mMVPMatrixHandle,
            mPositionHandle,
            mColorHandle;

    // OpenGL繪製3D物體的vertex shader程式
    final String vertexShader =
            "uniform mat4 u_MVPMatrix;      \n"		// 表示要使用 model, view, projection matrix
                    + "attribute vec4 a_Position;     \n"		// 表示要設定頂點位置
                    + "attribute vec4 a_Color;        \n"		// 表示要設定頂點顏色
                    + "varying vec4 v_Color;          \n"		// 這個要傳給fragment shader使用
                    + "void main()                    \n"		// vertex shader主程式
                    + "{                              \n"
                    + "   v_Color = a_Color;          \n"		// 把設定的頂點顏色傳給fragment shader.
                    + "   gl_Position = u_MVPMatrix   \n" 	// gl_Position是專門用來儲存最後得到的頂點位置
                    + "               * a_Position;   \n"
                    + "}                              \n";

    // OpenGL繪製3D物體的fragment shader程式
    final String fragmentShader =
            "precision mediump float;       \n"		// 設定計算的準確度
                    + "varying vec4 v_Color;          \n"		// 接收vertex shader傳過來的顏色資料
                    + "void main()                    \n"		// fragment shader主程式
                    + "{                              \n"
                    + "   gl_FragColor = v_Color;     \n"		// gl_FragColor是專門用來儲存最後得到的顏色
                    + "}                              \n";

    private void setup() {
        // 建立OpenGL專用的buffer
        mVertLocationBuf = ByteBuffer.allocateDirect(
                m3DObjVertLocation.length * BYTE_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        // 建立OpenGL專用的buffer
        mVertColorBuf = ByteBuffer.allocateDirect(
                m3DObjVertColor.length * BYTE_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        // 把3D物體的頂點座標和顏色，放到OpenGL專用的buffer
        mVertLocationBuf.put(m3DObjVertLocation).position(0);
        mVertColorBuf.put(m3DObjVertColor).position(0);

        // 設定3D場景的背景顏色，也就是clipping wall的顏色
        GLES20.glClearColor(backColorR, backColorG, backColorB, backColorA);

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, headUpX, headUpY, headUpZ);

        // 建立和載入vertex shader以及fragment shader
        int vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        int fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        // 建立使用vertex shader和fragment shader的OpenGL GPU程式
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            // 連結vertex shader
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // 連結fragment shader
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // 連結vertex shader定義的屬性
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");

            // 載入程式
            GLES20.glLinkProgram(programHandle);

            // 取得載入結果
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // 如果失敗，刪除程式
            if (linkStatus[0] == 0)
            {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        // 建立和OpenGL GPU程式傳送資料用的屬性，我們要用這些屬性傳入資料
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");

        // 設定OpenGL使用我們建立的GPU程式
        GLES20.glUseProgram(programHandle);
    }

    public MyGLSurfaceView(Context context) {
        super(context);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        setup();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        // 設定透視投影參數
        // 設定最近和最遠的可視範圍和左右視角
        // 上下可視範圍由左右視角和螢幕的寬高比來計算
        final float nearest = .01f,
                farest = 100f,
                angle = 45f;
        final float viewWidth = nearest * (float) Math.tan(Math.toRadians(angle) / 2);
        final float aspectRatio = (float)width / (float)height;
        final float left = -viewWidth;
        final float right = viewWidth;
        final float bottom = -viewWidth / aspectRatio;
        final float top = viewWidth / aspectRatio;

        // 設定投影矩陣
        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, nearest, farest);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // 清除場景填上背景顏色，並且清除z-buffer
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // 初始化3D物體的轉換矩陣
        Matrix.setIdentityM(mModelMatrix, 0);

        // 將3D物體沿指定的軸轉動
        Matrix.rotateM(mModelMatrix, 0, mfRotateAng, 0.0f, 0.0f, 1.0f);
        mfRotateAng += 1f;

        // 把3D物體的頂點座標，利用OpenGL屬性，傳給OpenGL程式
        mVertLocationBuf.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, SIZE_VERTEX_LOCATION, GLES20.GL_FLOAT, false,
                3 * BYTE_PER_FLOAT, mVertLocationBuf);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 把3D物體的頂點顏色，利用OpenGL屬性，傳給OpenGL程式
        mVertColorBuf.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, SIZE_VERTEX_COLOR, GLES20.GL_FLOAT, false,
                4 * BYTE_PER_FLOAT, mVertColorBuf);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // 結合model, view和projection矩陣，得到MVP矩陣
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // 套用MVP矩陣，然後繪圖
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }

    private int compileShader(final int shaderType, final String shaderSource) {
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if (shaderHandle != 0) {
            // 載入然後編譯shader程式碼
            GLES20.glShaderSource(shaderHandle, shaderSource);
            GLES20.glCompileShader(shaderHandle);

            // 取得編譯結果
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // 如果編譯失敗，刪除shader
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0)
            throw new RuntimeException("Error creating shader.");

        return shaderHandle;
    }
}
