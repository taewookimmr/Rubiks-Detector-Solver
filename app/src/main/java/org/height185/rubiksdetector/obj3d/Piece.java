package org.height185.rubiksdetector.obj3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.microedition.khronos.opengles.GL10;

// Piece는 (26개의 정육면체로 구성된) 큐브를 이루는 하나의 정육면체를 나타낸다.
// 가운데는 비어있으므로 26개입니다.

public class Piece {

	public int pieceNumber;
	public int placeNumber;
	public int orientation;
	public int[] fixedFace;
	public int[] face;
	public int[] colorOfFace;

	public FloatBuffer vertexBuffer; // Buffer for vertex-array

	public float[] vertices = { // Vertices for a face at z=0
			-1.0f, -1.0f, 0.0f,  // 0. left-bottom-front
			1.0f, -1.0f, 0.0f,  // 1. right-bottom-front
			-1.0f,  1.0f, 0.0f,  // 2. left-top-front
			1.0f,  1.0f, 0.0f   // 3. right-top-front
	};

	public static float[]  blue   = {0.0f, 0.0f, 1.0f, 1.0f};
	public static float[]  green  = {0.3f, 1.0f, 0.0f, 1.0f};
	public static float[]  red    = {1.0f, 0.0f, 0.2f, 1.0f};
	public static float[]  orange = {1.0f, 0.5f, 0.0f, 1.0f};
	public static float[]  yellow = {1.0f, 1.0f, 0.0f, 1.0f};
	public static float[]  white  = {1.0f, 1.0f, 1.0f, 1.0f};
	public static float[]  black  = {0.0f, 0.0f, 0.0f, 1.0f};

	public static float[][] color = {
			blue, green, red, orange, yellow, white, black
	};

	public static int BLUE 		= 0;
	public static int GREEN 	= 1;
	public static int RED 		= 2;
	public static int ORANGE 	= 3;
	public static int YELLOW 	= 4;
	public static int WHITE 	= 5;
	public static int BLACK 	= 6;

	public static int[][] COLOR ={
			{BLUE, ORANGE, YELLOW}, {BLUE, RED, YELLOW}, {BLUE, RED, WHITE}, {BLUE, ORANGE, WHITE},
			{GREEN, ORANGE, WHITE}, {GREEN, ORANGE, YELLOW}, {GREEN, RED, YELLOW}, {GREEN, RED, WHITE},

			{BLUE, YELLOW}, {BLUE, RED}, {BLUE, WHITE}, {BLUE, ORANGE},
			{ORANGE, YELLOW}, {RED, YELLOW}, {RED, WHITE}, {ORANGE, WHITE},
			{GREEN, ORANGE}, {GREEN, YELLOW}, {GREEN, RED}, {GREEN, WHITE},

			{BLUE}, {GREEN}, {RED}, {ORANGE}, {YELLOW}, {WHITE}
	};


	public Piece(int placeNumber){
		this.pieceNumber = 0;
		this.placeNumber = placeNumber;
		this.orientation = 0;
		this.fixedFace   = new int[3];
		this.face        = new int[3];
		this.colorOfFace = new int[6];
		renewalColorOfFace();

		// Setup vertex-array buffer. Vertices in float. An float has 4 bytes
		// memory allocation : 12 * 4 byte
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder()); // Use native byte order
		vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
		vertexBuffer.put(vertices);         // Copy data into buffer
		vertexBuffer.position(0);           // Rewind
	}

	// Draw the shape
	public void draw(GL10 gl) {
		// 3차원 공간의 물체에는 물체의 표면 부분(front face)과 그 반대인 뒷면 부분(back face)이 존재한다.
		// glCullFace()와 glEnable()을 사용하여 OpenGL에게 front 또는 back facing 폴리곤만 존재함을 알릴 수 있다.
		gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
		gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
		gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		// front
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, 1.0f);
		gl.glColor4f(color[colorOfFace[0]][0], color[colorOfFace[0]][1], color[colorOfFace[0]][2], color[colorOfFace[0]][3]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		// back
		gl.glPushMatrix();
		gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(0.0f, 0.0f, 1.0f);
		gl.glColor4f(color[colorOfFace[1]][0], color[colorOfFace[1]][1], color[colorOfFace[1]][2], color[colorOfFace[1]][3]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		// right
		gl.glPushMatrix();
		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(0.0f, 0.0f, 1.0f);
		gl.glColor4f(color[colorOfFace[2]][0], color[colorOfFace[2]][1], color[colorOfFace[2]][2], color[colorOfFace[2]][3]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		// left
		gl.glPushMatrix();
		gl.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(0.0f, 0.0f, 1.0f);
		gl.glColor4f(color[colorOfFace[3]][0], color[colorOfFace[3]][1], color[colorOfFace[3]][2], color[colorOfFace[3]][3]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		// top
		gl.glPushMatrix();
		gl.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
		gl.glTranslatef(0.0f, 0.0f, 1.0f);
		gl.glColor4f(color[colorOfFace[4]][0], color[colorOfFace[4]][1], color[colorOfFace[4]][2], color[colorOfFace[4]][3]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		// bottom
		gl.glPushMatrix();
		gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		gl.glTranslatef(0.0f, 0.0f, 1.0f);
		gl.glColor4f(color[colorOfFace[5]][0], color[colorOfFace[5]][1], color[colorOfFace[5]][2], color[colorOfFace[5]][3]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}

	public void  face01() {
		face[0] = fixedFace[0];
		face[1] = fixedFace[1];
	}

	public void   face10() {
		face[0] = fixedFace[1];
		face[1] = fixedFace[0];
	}

	public void   face012() {
		face[0] = fixedFace[0];
		face[1] = fixedFace[1];
		face[2] = fixedFace[2];
	}

	public void   face210() {
		face[0] = fixedFace[2];
		face[1] = fixedFace[1];
		face[2] = fixedFace[0];
	}

	public  void  face201() {
		face[0] = fixedFace[2];
		face[1] = fixedFace[0];
		face[2] = fixedFace[1];
	}

	public  void  face102() {
		face[0] = fixedFace[1];
		face[1] = fixedFace[0];
		face[2] = fixedFace[2];
	}

	public  void  face120() {
		face[0] = fixedFace[1];
		face[1] = fixedFace[2];
		face[2] = fixedFace[0];
	}

	public  void  face021() {
		face[0] = fixedFace[0];
		face[1] = fixedFace[2];
		face[2] = fixedFace[1];
	}


	public void renewalColorOfFace(){

		Arrays.fill(colorOfFace, BLACK);

		if(placeNumber>=0 && placeNumber <8){
			colorOfFace[COLOR[placeNumber][0]] = COLOR[pieceNumber][face[0]];
			colorOfFace[COLOR[placeNumber][1]] = COLOR[pieceNumber][face[1]];
			colorOfFace[COLOR[placeNumber][2]] = COLOR[pieceNumber][face[2]];
		}

		if(placeNumber>=8+0 && placeNumber <8+12){
			colorOfFace[COLOR[placeNumber][0]] =  COLOR[pieceNumber][face[0]];
			colorOfFace[COLOR[placeNumber][1]] =  COLOR[pieceNumber][face[1]];
		}

		if(placeNumber>=20+0 && placeNumber <20+6){
			colorOfFace[COLOR[placeNumber][0]] =  COLOR[pieceNumber][face[0]];
		}

	}

}
