package org.height185.rubiksdetector.renderer;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import org.height185.rubiksdetector.obj3d.Vector;
import org.height185.rubiksdetector.obj3d.Cube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CubeRenderer implements GLSurfaceView.Renderer {

	public Cube cube = new Cube();

	private float transZ;
	private float angleOperation = 0f;
	private float xAngleCube = 0f;
	private float yAngleCube = 0f;
	private float zAngleCube = 0f;
	private float xRotate = 1f;
	private float yRotate = 9f;
	private float zRotate = 0f;

	public CubeRenderer(float value) {
		this.transZ = value;
	}

	// Call back when the surface is first created or re-created
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glDisable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);
	}

	// Call back after onSurfaceCreated() or whenever the window's size changes
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0)
			height = 1; // To prevent divide by zero
		float aspect = (float) width / height;

		// Set the viewport (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
		gl.glLoadIdentity(); // Reset projection matrix
		GLU.gluPerspective(gl, 30.0f, aspect, 0.1f, 100.f);	 // Use perspective projection

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();                 // Reset
		// 이걸 여기서 하네!
	}

	// Call back to draw the current frame.
	@Override
	public void onDrawFrame(GL10 gl) {

		// Clear color and depth buffers using clear-value set earlier
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		xAngleCube = xRotate / 10 * 360;
		yAngleCube = yRotate / 10 * 360;
		zAngleCube = zRotate / 10 * 360;

		if(xAngleCube > 360){ xAngleCube -= 360; }
		if(yAngleCube > 360){ yAngleCube -= 360; }
		if(zAngleCube > 360){ zAngleCube -= 360; }

		float xSpace = 2.2f;
		float ySpace = 2.2f;
		float zSpace = 2.2f;

		// ----- Render the Color Cube -----

		Vector vector[] = new Vector[26];

		//corner 8개
		vector[0] = new Vector(-xSpace, ySpace, +zSpace);
		vector[1] = new Vector(xSpace, ySpace, +zSpace);
		vector[2] = new Vector(xSpace, -ySpace, +zSpace);
		vector[3] = new Vector(-xSpace, -ySpace, +zSpace);
		vector[4] = new Vector(-xSpace, -ySpace, -zSpace);
		vector[5] = new Vector(-xSpace, +ySpace, -zSpace);
		vector[6] = new Vector(+xSpace, +ySpace, -zSpace);
		vector[7] = new Vector(+xSpace, -ySpace, -zSpace);

		//edge 12개
		vector[8+0] = new Vector(0, ySpace, +zSpace);
		vector[8+1] = new Vector(xSpace, 0, +zSpace);
		vector[8+2] = new Vector(0, -ySpace, +zSpace);
		vector[8+3] = new Vector(-xSpace, 0, +zSpace);
		vector[8+4] = new Vector(-xSpace, ySpace, 0);
		vector[8+5] = new Vector(xSpace, ySpace, 0);
		vector[8+6] = new Vector(xSpace, -ySpace, 0);
		vector[8+7] = new Vector(-xSpace, -ySpace, 0);
		vector[8+8] = new Vector(-xSpace, 0, 	-zSpace);
		vector[8+9] = new Vector(0, ySpace, 	-zSpace);
		vector[8+10] = new Vector(xSpace, 0, -zSpace);
		vector[8+11] = new Vector(0, -ySpace, -zSpace);

		// center 6개
		vector[20+0] = new Vector(0, 0, zSpace);
		vector[20+1] = new Vector(0, 0, -zSpace);
		vector[20+2] = new Vector(xSpace, 0, 0);
		vector[20+3] = new Vector(-xSpace, 0, 0);
		vector[20+4] = new Vector(0, ySpace, 0);
		vector[20+5] = new Vector(0, -ySpace, 0);


		for(int i = 0; i < cube.pieceAt.length; i++){
			gl.glLoadIdentity(); // Reset the model-view matrix
			gl.glTranslatef(0, 0, transZ);

			gl.glRotatef(xAngleCube, 1.0f, 0.0f, 0.0f);
			gl.glRotatef(yAngleCube, 0.0f, 1.0f, 0.0f);
			gl.glRotatef(zAngleCube, 0.0f, 0.0f, 1.0f);

			operationAnimation(gl, i, cube.rotatedLayer);
			gl.glTranslatef(vector[i].x, vector[i].y, vector[i].z); // Translate right and into the // screen
			cube.pieceAt[i].draw(gl); // Draw the cube (NEW)
		}

	}

	public float getxRotate() {
		return xRotate;
	}

	public void setxRotate(float xRotate) {
		this.xRotate = xRotate;
	}

	public float getyRotate() {
		return yRotate;
	}

	public void setyRotate(float yRotate) {
		this.yRotate = yRotate;
	}

	public float getzRotate() {
		return zRotate;
	}

	public void setzRotate(float zRotate) {
		this.zRotate = zRotate;
	}

	public float getTransZ() {
		return transZ;
	}

	public void setTransZ(float transZ){
		this.transZ = transZ;
	}

	public void operationAnimation(GL10 gl, int index, int rotatedLayer){

		switch (rotatedLayer){
			case -1: break;

			case Cube.front : 	if(condition(index, Cube.front)) 	gl.glRotatef(angleOperation, 0.0f, 0.0f, -1.0f); break;
			case Cube.back  : 	if(condition(index, Cube.back)) 	gl.glRotatef(angleOperation, 0.0f, 0.0f, 1.0f); break;
			case Cube.right :	if(condition(index, Cube.right)) 	gl.glRotatef(angleOperation, -1.0f, 0.0f, 0.0f); break;
			case Cube.left  : 	if(condition(index, Cube.left)) 	gl.glRotatef(angleOperation, 1.0f, 0.0f, 0.0f); break;
			case Cube.up    : 	if(condition(index, Cube.up)) 		gl.glRotatef(angleOperation, 0.0f, -1.0f, 0.0f); break;
			case Cube.down  : 	if(condition(index, Cube.down)) 	gl.glRotatef(angleOperation, 0.0f, 1.0f, 0.0f); break;
			case Cube.slide  : 	if(condition(index, Cube.slide)) 	gl.glRotatef(angleOperation, 0.0f, 0.0f, -1.0f); break;
			case Cube.middle  : if(condition(index, Cube.middle)) 	gl.glRotatef(angleOperation, 1.0f, 0.0f, 0.0f); break;
			case Cube.equator : if(condition(index, Cube.equator)) 	gl.glRotatef(angleOperation, 0.0f, 1.0f, 0.0f); break;

			case 9+Cube.front : 	if(condition(index, Cube.front)) 	gl.glRotatef(angleOperation, 0.0f, 0.0f, 1.0f); break;
			case 9+Cube.back  :  	if(condition(index, Cube.back)) 	gl.glRotatef(angleOperation, 0.0f, 0.0f, -1.0f); break;
			case 9+Cube.right : 	if(condition(index, Cube.right)) 	gl.glRotatef(angleOperation, 1.0f, 0.0f, 0.0f); break;
			case 9+Cube.left  :  	if(condition(index, Cube.left)) 	gl.glRotatef(angleOperation, -1.0f, 0.0f, 0.0f); break;
			case 9+Cube.up    : 	if(condition(index, Cube.up)) 		gl.glRotatef(angleOperation, 0.0f, 1.0f, 0.0f); break;
			case 9+Cube.down  : 	if(condition(index, Cube.down)) 	gl.glRotatef(angleOperation, 0.0f, -1.0f, 0.0f); break;
			case 9+Cube.slide  : 	if(condition(index, Cube.slide)) 	gl.glRotatef(angleOperation, 0.0f, 0.0f, 1.0f); break;
			case 9+Cube.middle  :	if(condition(index, Cube.middle)) 	gl.glRotatef(angleOperation, -1.0f, 0.0f, 0.0f); break;
			case 9+Cube.equator : 	if(condition(index, Cube.equator)) 	gl.glRotatef(angleOperation, 0.0f, -1.0f, 0.0f); break;
		}

		angleOperation += 0.1;
		if(angleOperation > 90){
			angleOperation = 0;
			cube.rotatedLayer = -1;
			// 회전 에니메이션이 다 끝나고 상태배열을 바꿔준다.
			switch (rotatedLayer){
				case 0: cube.rotate_F(); break;
				case 1: cube.rotate_B(); break;
				case 2: cube.rotate_R(); break;
				case 3: cube.rotate_L(); break;
				case 4: cube.rotate_U(); break;
				case 5: cube.rotate_D(); break;

				case 6: cube.rotate_S(); break;
				case 7: cube.rotate_M(); break;
				case 8: cube.rotate_E(); break;
			}
		}

	}

	public boolean condition(int index, int rotatedLayer){
		boolean result = false;

		for(int i = 0; i < 9; i++){
			if(index == Cube.pieceInLayer[rotatedLayer][i]) {
				result = true; break;
			}
		}
		return result;
	}

}
