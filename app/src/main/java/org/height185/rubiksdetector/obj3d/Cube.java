package org.height185.rubiksdetector.obj3d;

import android.util.Log;

import java.util.ArrayList;

public class Cube {
	// pull request test from whilescape
	public static final   int front 	= 0;
	public static final	  int back 		= 1;
	public static final	  int right 	= 2;
	public static final	  int left 		= 3;
	public static final	  int up 		= 4;
	public static final	  int down 		= 5;
	public static final   int slide  	= 6;
	public static final   int middle 	= 7;
	public static final   int equator 	= 8;

	public static final int[][] pieceInLayer = {
			{1-1, 2-1, 3-1, 4-1,   8+1-1, 8+2-1, 8+3-1, 8+4-1,     20+0},   // front
			{5-1, 6-1, 7-1, 8-1,   8+9-1, 8+10-1, 8+11-1, 8+12-1,  20+1},   // back
			{2-1, 3-1, 7-1, 8-1,   8+2-1, 8+6-1, 8+7-1, 8+11-1,    20+2},   // right
			{1-1, 4-1, 5-1, 6-1,   8+4-1, 8+5-1, 8+8-1, 8+9-1,     20+3},   // left
			{1-1, 2-1, 6-1, 7-1,   8+1-1, 8+5-1, 8+6-1, 8+10-1,    20+4},   // up
			{3-1, 4-1, 5-1, 8-1,   8+3-1, 8+7-1, 8+8-1, 8+12-1,    20+5},   // down
			// slide, middle, equator는 8개의 피스만 있으므로 앞에 -1을 더미로 넣어준다.
			{-1, 8+5-1, 8+6-1,  8+7-1,  8+8-1,    20+2, 20+3, 20+4, 20+5}, 			  // slide
			{-1, 8+1-1, 8+10-1, 8+12-1, 8+3-1,    20+0, 20+1, 20+4, 20+5},			  // middle
			{-1, 8+2-1, 8+11-1, 8+4-1,  8+9-1,    20+0, 20+1, 20+2, 20+3}             // equator
	}; // 이건 static으로 해도 문제 없지 않을까?

	public int rotatedLayer = -1; // 회전될 면을 나타내는 변수, 회전될 면이 없으면 -1으로

	public int[][] state_pxo = new int [2][26];
	public int[][] state_xpo = new int [2][26]; // deprecated variable, 사용하지 않는 변수 입니다. 191028
	public Piece[] pieceAt = new Piece[26]; // 8개의 corner, 12개의 edge, 6개의 center 순으로

	// 이건 오로지 이미지 처리 후 얻어진 데이터를 저장하는 배열에 해당하고,
	// 그 배열 데이터를 cube의 stata_pxo로 전달하는데 필요한 배열이다.
	public int[] surfaceColor = new int [6*3*3]; // 54개 surface의 색을 저장하는 배열

	public ArrayList<String> solution = new ArrayList<>();

	public  boolean flag_transcription = true;
	public  boolean flag_opencv = false;

	public Cube() {
		initializeState(state_pxo);
		initializePiece();
		initializeSurfaceColor();
		transcriptionProcess();
	}

	// call by reference라는 게 참 다행이다.
	public void initializeState(int[][] state){

		for(int j = 0 ; j < 26; j++) {state[0][j] = j ;}
		for(int j = 0 ; j < 26; j++) {state[1][j] = 0 ;}
	}

	// 상태 배열 복사
	public void pasteState(int to[][], int from[][]) {
		for (int i = 0; i < from.length; i++) {
			for (int j = 0; j < from[0].length; j++) {
				to[i][j] = from[i][j];
			}
		}
	}

	// 피스 초기화
	public void initializePiece() {

		for(int i = 0; i < 26; i++){
			pieceAt[i] = new Piece(i);
			pieceAt[i].pieceNumber = i;
			pieceAt[i].placeNumber = i;
			pieceAt[i].orientation = 0;
			for(int j = 0; j < 3; j++){
				pieceAt[i].face[j] = j;
				pieceAt[i].fixedFace[j] = j;
			}
		}
	}

	// opencv 연동을 위한 준비
	public void initializeSurfaceColor() {
		for (int i = 0; i < 54; i++) {
			surfaceColor[i] = i / 9;
		}
	}

	// color detection 후  넘어온 surface 배열의 데이터를 state_pxo 배열 데이터로 전환하는 메서드
	public void transferSurfaceColorToState() {
		int[] result = {0, 0}; // 초기화

		// 코너
		for(int i = 0; i < 8; i++){
			result =  Connector.transferSurfaceColorToState(
					surfaceColor[Connector.pieceAsFaces[i][0]],
					surfaceColor[Connector.pieceAsFaces[i][1]],
					surfaceColor[Connector.pieceAsFaces[i][2]]);
			state_pxo[0][i] = result[0];
			state_pxo[1][i] = result[1];
		}

		// 엣지
		for(int i = 8; i < 8+12; i++){
			result =  Connector.transferSurfaceColorToState(
					surfaceColor[Connector.pieceAsFaces[i][0]],
					surfaceColor[Connector.pieceAsFaces[i][1]]);
			state_pxo[0][i] = result[0];
			state_pxo[1][i] = result[1];
		}

		// 센터
		for(int i = 20; i < 20+6; i++){
			result =  Connector.transferSurfaceColorToState(
					surfaceColor[Connector.pieceAsFaces[i][0]]);
			state_pxo[0][i] = result[0];
			state_pxo[1][i] = 0;
		}

		flag_opencv = false; // 전달했으면 닫아주는 역할
	}

	// 상태배열의 정보를 Piece 객체로 전달한다.
    public void transferStateToPiece() {

		for(int i = 0; i < 26; i++) {
			for(int j = 0; j < 26; j++) {
				if(j == state_pxo[0][i]) {
					state_xpo[0][j] = i;
					state_xpo[1][j] = state_pxo[1][i];
					break;
				}
			}
		}

		for(int i = 0; i< pieceAt.length; i++) {
			pieceAt[i].pieceNumber    = state_pxo[0][i];
			pieceAt[i].orientation    = state_pxo[1][i];
		}
	}

	//이게 핵심
    public void renewalPieceOrientation() {

    	for(int i = 8+0 ; i < 8+12; i++) {
    		if(pieceAt[i].orientation == 0) {pieceAt[i].face01();}
    		else {pieceAt[i].face10();}
    	}

    	for(int i = 0; i < 8; i++) {
    		switch(pieceAt[i].pieceNumber%2) {
    		case 0:
    			switch(pieceAt[i].orientation) {
    			case 0:
    				if(pieceAt[i].placeNumber%2 == 0) {pieceAt[i].face012();} //012
    				else {pieceAt[i].face210();} //210
    			break;
    			case 1:
    				if(pieceAt[i].placeNumber%2 == 0) {pieceAt[i].face201();} //201
    				else {pieceAt[i].face102();} //102
    			break;
    			case 2:
    				if(pieceAt[i].placeNumber%2 == 0) {pieceAt[i].face120();} //120
    				else {pieceAt[i].face021();} //021
    			break;
    			}
    		break;

    		case 1:
    			switch(pieceAt[i].orientation) {
    			case 0:
    				if(pieceAt[i].placeNumber%2 == 1) {pieceAt[i].face012();} //012
    				else {pieceAt[i].face210();} //210
    			break;
    			case 1:
    				if(pieceAt[i].placeNumber%2 == 1) {pieceAt[i].face120();} //120
    				else {pieceAt[i].face021();} //021
    			break;
    			case 2:
    				if(pieceAt[i].placeNumber%2 == 1) {pieceAt[i].face201();} //201
    				else {pieceAt[i].face102();} //102
    			break;
    			}
    		break;

    		}
    	}

    	// 여기서 딱 한번 해준다. facexyz 조작 같은 것을 할 때 마다 하는게 아니라.
    	for(Piece p : pieceAt){	p.renewalColorOfFace();}
    }

    // 상태 변화가 있는 경우에 상태 정보가 필요한 모든 곳?에 상태 정보를 전달하여 업데이트함.
    public void transcriptionProcess() {

    	if(flag_transcription) {
    		if(flag_opencv) {
                transferSurfaceColorToState();
            }
	    	transferStateToPiece();
	    	renewalPieceOrientation();
    	}

    }

    // 현재 190111, 완벽한 판별식은 아니다. center 분리-재장착된 상태를 판별하지 못하는 수준. 향후 수정할 계획.
    // surfaceColor의 정보가 전달된 stata_pxo가 legal한지 확인하는 메서드
	public boolean isLegalState(){
		boolean result = true;
		int centerDetermine[][] = {
				{0, 1, 2, 3, 4, 5}, {0,1,4,5,3,2}, {0,1,3,2,5,4}, {0,1,5,4,2,3},
				{1,0,2,3,5,4}, {1,0,5,4,3,2}, {1,0,3,2,4,5}, {1, 0, 4, 5, 2, 3},
				{2,3,4,5,0,1}, {2,3,0,1,5,4}, {2,3,5,4,1,0}, {2,3,1,0,4,5},
				{3,2,0,1,4,5}, {3,2,4,5,1,0}, {3,2,1,0,4,5}, {3,2,5,4,0,1},
				{4,5,3,2,0,1}, {4,5,0,1,2,3}, {4,5,2,3,1,0}, {4,5,1,0,3,2},
				{5,4,0,1,3,2}, {5,4,3,2,1,0}, {5,4,1,0,2,3}, {5,4,2,3,0,1}
		};
		int counting[] = new int[26]; // 0으로 초기화 되어 있음
		for(int i = 0; i < 26;  i++) {
			counting[state_pxo[0][i]] += 1;
			if (counting[state_pxo[0][i]] != 1) {
				result = false;
				break;
			}
		}

		// 일단 한 번 걸러줌
		if(result != true){
			return false;
		}

		// center 판별 (20 to 25)
		result = false;
		for(int i = 0; i < 24; i++){
			int con = 0;
			for(int j = 0; j < 6; j++){
				if(state_pxo[0][20+j] != centerDetermine[i][j]+20){
					break;
				}else{
					con++;
				}
			}
			if(con == 6){
				result = true;
				break;
			}
		}

		// 한 번 더 걸러줌
		if(result != true){
			return false;
		}

		// edge, corner 판별 (20 to 25)
		result = false;
		int sum_corner = 0;
		for(int i = 0; i < 8; i++){
			sum_corner += state_pxo[1][i];
		}

		if(sum_corner % 3 != 0){
			return false;
		}

		int sum_edge = 0;
		for(int i = 8; i < 20; i++){
			sum_edge += state_pxo[1][i];
		}

		if(sum_edge % 2 != 0){
			return false;
		}

		return true;
 	}

    public int cornerOrientationFunction_zaxis(int orientationNumber, int positionNumber){

	    	int result = 0;
			if((positionNumber%2) == 0) {
				switch(orientationNumber) {
				case 0:  result = 2; break;
				case 1:  result = 0; break;
				case 2:  result = 1; break;
				}
			}
			else {
				switch(orientationNumber) {
				case 0:  result = 1; break;
				case 1:  result = 2; break;
				case 2:  result = 0; break;
				}
			}
	    	return result;
		}

    public int cornerOrientationFunction_yaxis(int orientationNumber, int positionNumber){
		 	int result = 0;
				if((positionNumber%2) == 0) {
					switch(orientationNumber) {
					case 0:  result = 1; break;
					case 1:  result = 2; break;
					case 2:  result = 0; break;
					}
				}
				else {
					switch(orientationNumber) {
					case 0:  result = 2; break;
					case 1:  result = 0; break;
					case 2:  result = 1; break;
					}
				}
		    	return result;
		}

    public int edgeOrientationFunction(int number) {
		return 1-number;
	}

    public void rotate_F() {
		int temp[][] = new int[2][26];
		initializeState(temp);
		pasteState(temp, state_pxo);

		temp[0][1-1]   = state_pxo[0][4-1];
		temp[0][2-1]   = state_pxo[0][1-1];
		temp[0][3-1]   = state_pxo[0][2-1];
		temp[0][4-1]   = state_pxo[0][3-1];

		temp[0][8-1+1] = state_pxo[0][8-1+4];
		temp[0][8-1+2] = state_pxo[0][8-1+1];
		temp[0][8-1+3] = state_pxo[0][8-1+2];
		temp[0][8-1+4] = state_pxo[0][8-1+3];

		temp[1][1-1]   = cornerOrientationFunction_zaxis(state_pxo[1][4-1], 4-1);
		temp[1][2-1]   = cornerOrientationFunction_zaxis(state_pxo[1][1-1], 1-1);
		temp[1][3-1]   = cornerOrientationFunction_zaxis(state_pxo[1][2-1], 2-1);
		temp[1][4-1]   = cornerOrientationFunction_zaxis(state_pxo[1][3-1], 3-1);

		temp[1][8-1+1] = state_pxo[1][8-1+4];
		temp[1][8-1+2] = state_pxo[1][8-1+1];
		temp[1][8-1+3] = state_pxo[1][8-1+2];
		temp[1][8-1+4] = state_pxo[1][8-1+3];

		pasteState(state_pxo, temp);
		transcriptionProcess();
		solution.add("F");
	}

    public void rotate_R() {
		int temp[][] = new int[2][26];
		initializeState(temp);
		pasteState(temp, state_pxo);

		temp[0][2-1]   = state_pxo[0][3-1];
		temp[0][7-1]   = state_pxo[0][2-1];
		temp[0][8-1]   = state_pxo[0][7-1];
		temp[0][3-1]   = state_pxo[0][8-1];

		temp[0][8-1+6]  = state_pxo[0][8-1+2];
		temp[0][8-1+11] = state_pxo[0][8-1+6];
		temp[0][8-1+7]  = state_pxo[0][8-1+11];
		temp[0][8-1+2]  = state_pxo[0][8-1+7];

		temp[1][2-1]   = state_pxo[1][3-1];
		temp[1][7-1]   = state_pxo[1][2-1];
		temp[1][8-1]   = state_pxo[1][7-1];
		temp[1][3-1]   = state_pxo[1][8-1];

		temp[1][8-1+6]  = edgeOrientationFunction(state_pxo[1][8-1+2]);
		temp[1][8-1+11] = edgeOrientationFunction(state_pxo[1][8-1+6]);
		temp[1][8-1+7]  = edgeOrientationFunction(state_pxo[1][8-1+11]);
		temp[1][8-1+2]  = edgeOrientationFunction(state_pxo[1][8-1+7]);

		pasteState(state_pxo, temp);
		transcriptionProcess();
		solution.add("R");
	}

    public void rotate_U() {
		int temp[][] = new int[2][26];
		initializeState(temp);
		pasteState(temp, state_pxo);

		temp[0][6-1]   = state_pxo[0][1-1];
		temp[0][7-1]   = state_pxo[0][6-1];
		temp[0][2-1]   = state_pxo[0][7-1];
		temp[0][1-1]   = state_pxo[0][2-1];

		temp[0][8-1+5]  = state_pxo[0][8-1+1];
		temp[0][8-1+10] = state_pxo[0][8-1+5];
		temp[0][8-1+6]  = state_pxo[0][8-1+10];
		temp[0][8-1+1]  = state_pxo[0][8-1+6];

		temp[1][6-1]   = cornerOrientationFunction_yaxis(state_pxo[1][1-1], 1-1);
		temp[1][7-1]   = cornerOrientationFunction_yaxis(state_pxo[1][6-1], 6-1);
		temp[1][2-1]   = cornerOrientationFunction_yaxis(state_pxo[1][7-1], 7-1);
		temp[1][1-1]   = cornerOrientationFunction_yaxis(state_pxo[1][2-1], 2-1);

		temp[1][8-1+5]  = state_pxo[1][8-1+1];
		temp[1][8-1+10] = state_pxo[1][8-1+5];
		temp[1][8-1+6]  = state_pxo[1][8-1+10];
		temp[1][8-1+1]  = state_pxo[1][8-1+6];

		pasteState(state_pxo, temp);
		transcriptionProcess();
		solution.add("U");
	}

    public void rotate_B() {
		int temp[][] = new int[2][26];
		initializeState(temp);
		pasteState(temp, state_pxo);

		temp[0][7-1]   = state_pxo[0][8-1];
		temp[0][6-1]   = state_pxo[0][7-1];
		temp[0][5-1]   = state_pxo[0][6-1];
		temp[0][8-1]   = state_pxo[0][5-1];

		temp[0][8-1+10] = state_pxo[0][8-1+11];
		temp[0][8-1+9]  = state_pxo[0][8-1+10];
		temp[0][8-1+12] = state_pxo[0][8-1+9];
		temp[0][8-1+11] = state_pxo[0][8-1+12];

		temp[1][7-1]   = cornerOrientationFunction_zaxis(state_pxo[1][8-1], 8-1);
		temp[1][6-1]   = cornerOrientationFunction_zaxis(state_pxo[1][7-1], 7-1);
		temp[1][5-1]   = cornerOrientationFunction_zaxis(state_pxo[1][6-1], 6-1);
		temp[1][8-1]   = cornerOrientationFunction_zaxis(state_pxo[1][5-1], 5-1);

		temp[1][8-1+10] = state_pxo[1][8-1+11];
		temp[1][8-1+9]  = state_pxo[1][8-1+10];
		temp[1][8-1+12] = state_pxo[1][8-1+9];
		temp[1][8-1+11] = state_pxo[1][8-1+12];

		pasteState(state_pxo, temp);
		transcriptionProcess();
		solution.add("B");
	}

    public void rotate_L() {
		int temp[][] = new int[2][26];
		initializeState(temp);
		pasteState(temp, state_pxo);

		temp[0][6-1]   = state_pxo[0][5-1];
		temp[0][1-1]   = state_pxo[0][6-1];
		temp[0][4-1]   = state_pxo[0][1-1];
		temp[0][5-1]   = state_pxo[0][4-1];

		temp[0][8-1+5]  = state_pxo[0][8-1+9];
		temp[0][8-1+4]  = state_pxo[0][8-1+5];
		temp[0][8-1+8]  = state_pxo[0][8-1+4];
		temp[0][8-1+9]  = state_pxo[0][8-1+8];

		temp[1][6-1]   = state_pxo[1][5-1];
		temp[1][1-1]   = state_pxo[1][6-1];
		temp[1][4-1]   = state_pxo[1][1-1];
		temp[1][5-1]   = state_pxo[1][4-1];

		temp[1][8-1+5]  = edgeOrientationFunction(state_pxo[1][8-1+9]);
		temp[1][8-1+4]  = edgeOrientationFunction(state_pxo[1][8-1+5]);
		temp[1][8-1+8]  = edgeOrientationFunction(state_pxo[1][8-1+4]);
		temp[1][8-1+9]  = edgeOrientationFunction(state_pxo[1][8-1+8]);

		pasteState(state_pxo, temp);
		transcriptionProcess();
		solution.add("L");
	}

    public void rotate_D() {
		int temp[][] = new int[2][26];
		initializeState(temp);
		pasteState(temp, state_pxo);

		temp[0][4-1]   = state_pxo[0][5-1];
		temp[0][3-1]   = state_pxo[0][4-1];
		temp[0][8-1]   = state_pxo[0][3-1];
		temp[0][5-1]   = state_pxo[0][8-1];

		temp[0][8-1+3]   = state_pxo[0][8-1+8];
		temp[0][8-1+7]   = state_pxo[0][8-1+3];
		temp[0][8-1+12]  = state_pxo[0][8-1+7];
		temp[0][8-1+8]   = state_pxo[0][8-1+12];

		temp[1][4-1]   = cornerOrientationFunction_yaxis(state_pxo[1][5-1], 5-1);
		temp[1][3-1]   = cornerOrientationFunction_yaxis(state_pxo[1][4-1], 4-1);
		temp[1][8-1]   = cornerOrientationFunction_yaxis(state_pxo[1][3-1], 3-1);
		temp[1][5-1]   = cornerOrientationFunction_yaxis(state_pxo[1][8-1], 8-1);

		temp[1][8-1+3]   = state_pxo[1][8-1+8];
		temp[1][8-1+7]   = state_pxo[1][8-1+3];
		temp[1][8-1+12]  = state_pxo[1][8-1+7];
		temp[1][8-1+8]   = state_pxo[1][8-1+12];

		pasteState(state_pxo, temp);
		transcriptionProcess();
		solution.add("D");
	}

	public void rotate_F(int rotationNumber) {
		switch(rotationNumber%4) {
		case 0: break;
		case 1: case -3: rotate_F();						 break;
		case 2: case -2: rotate_F(); rotate_F();			 break;
		case 3: case -1: rotate_F(); rotate_F(); rotate_F(); break;
		}
	}

    public void rotate_R(int rotationNumber) {
		switch(rotationNumber%4) {
		case 0: break;
		case 1: case -3: rotate_R();						 break;
		case 2: case -2: rotate_R(); rotate_R();			 break;
		case 3: case -1: rotate_R(); rotate_R(); rotate_R(); break;
		}
	}

    public void rotate_U(int rotationNumber) {
		switch(rotationNumber%4) {
		case 0: break;
		case 1: case -3: rotate_U();						 break;
		case 2: case -2: rotate_U(); rotate_U();			 break;
		case 3: case -1: rotate_U(); rotate_U(); rotate_U(); break;
		}
	}

    public void rotate_B(int rotationNumber) {
		switch(rotationNumber%4) {
		case 0: break;
		case 1: case -3: rotate_B();						 break;
		case 2: case -2: rotate_B(); rotate_B();			 break;
		case 3: case -1: rotate_B(); rotate_B(); rotate_B(); break;
		}
	}

    public void rotate_L(int rotationNumber) {
		switch(rotationNumber%4) {
		case 0: break;
		case 1: case -3: rotate_L();						 break;
		case 2: case -2: rotate_L(); rotate_L();			 break;
		case 3: case -1: rotate_L(); rotate_L(); rotate_L(); break;
		}
	}

    public void rotate_D(int rotationNumber) {
		switch(rotationNumber%4) {
		case 0: break;
		case 1: case -3: rotate_D();						 break;
		case 2: case -2: rotate_D(); rotate_D();			 break;
		case 3: case -1: rotate_D(); rotate_D(); rotate_D(); break;
		}
	}

    public void rotate_S() {

    	int temp[][] = new int[2][26];
		initializeState(temp);
		pasteState(temp, state_pxo);

		temp[0][8-1+5]  = state_pxo[0][8-1+8];
		temp[0][8-1+6]  = state_pxo[0][8-1+5];
		temp[0][8-1+7]  = state_pxo[0][8-1+6];
		temp[0][8-1+8]  = state_pxo[0][8-1+7];

		temp[1][8-1+5]  = edgeOrientationFunction(state_pxo[1][8-1+8]);
		temp[1][8-1+6]  = edgeOrientationFunction(state_pxo[1][8-1+5]);
		temp[1][8-1+7]  = edgeOrientationFunction(state_pxo[1][8-1+6]);
		temp[1][8-1+8]  = edgeOrientationFunction(state_pxo[1][8-1+7]);

		temp[0][20+2] = state_pxo[0][20+4];
		temp[0][20+5] = state_pxo[0][20+2];
		temp[0][20+3] = state_pxo[0][20+5];
		temp[0][20+4] = state_pxo[0][20+3];

		pasteState(state_pxo, temp);
		transcriptionProcess();
		solution.add("S");
    }

    public void rotate_M() {

    	int temp[][] = new int[2][26];
		initializeState(temp);
		pasteState(temp, state_pxo);

		temp[0][8-1+1]  = state_pxo[0][8-1+10];
		temp[0][8-1+10] = state_pxo[0][8-1+12];
		temp[0][8-1+12] = state_pxo[0][8-1+3];
		temp[0][8-1+3]  = state_pxo[0][8-1+1];

		temp[1][8-1+1]  = edgeOrientationFunction(state_pxo[1][8-1+10]);
		temp[1][8-1+10] = edgeOrientationFunction(state_pxo[1][8-1+12]);
		temp[1][8-1+12] = edgeOrientationFunction(state_pxo[1][8-1+3]);
		temp[1][8-1+3]  = edgeOrientationFunction(state_pxo[1][8-1+1]);

		temp[0][20+0] = state_pxo[0][20+4];
		temp[0][20+4] = state_pxo[0][20+1];
		temp[0][20+1] = state_pxo[0][20+5];
		temp[0][20+5] = state_pxo[0][20+0];


		pasteState(state_pxo, temp);
		transcriptionProcess();
		solution.add("M");
    }

    public void rotate_E() {

    	int temp[][] = new int[2][26];
		initializeState(temp);
		pasteState(temp, state_pxo);


		temp[0][8-1+2]   = state_pxo[0][8-1+4];
		temp[0][8-1+4]   = state_pxo[0][8-1+9];
		temp[0][8-1+9]   = state_pxo[0][8-1+11];
		temp[0][8-1+11]  = state_pxo[0][8-1+2];


		temp[1][8-1+2]   = edgeOrientationFunction(state_pxo[1][8-1+4]);
		temp[1][8-1+4]   = edgeOrientationFunction(state_pxo[1][8-1+9]);
		temp[1][8-1+9]   = edgeOrientationFunction(state_pxo[1][8-1+11]);
		temp[1][8-1+11]  = edgeOrientationFunction(state_pxo[1][8-1+2]);

		temp[0][20+0] = state_pxo[0][20+3];
		temp[0][20+2] = state_pxo[0][20+0];
		temp[0][20+1] = state_pxo[0][20+2];
		temp[0][20+3] = state_pxo[0][20+1];

		pasteState(state_pxo, temp);
		transcriptionProcess();
		solution.add("E");
    }

    public void rotate_S(int rotationNumber) {
  		switch(rotationNumber%4) {
  		case 0: break;
  		case 1: case -3: rotate_S();						 break;
  		case 2: case -2: rotate_S(); rotate_S();			 break;
  		case 3: case -1: rotate_S(); rotate_S(); rotate_S(); break;
  		}
  	}

    public void rotate_M(int rotationNumber) {
  		switch(rotationNumber%4) {
  		case 0: break;
  		case 1: case -3: rotate_M();						 break;
  		case 2: case -2: rotate_M(); rotate_M();			 break;
  		case 3: case -1: rotate_M(); rotate_M(); rotate_M(); break;
  		}
  	}

    public void rotate_E(int rotationNumber) {
  		switch(rotationNumber%4) {
  		case 0: break;
  		case 1: case -3: rotate_E();						 break;
  		case 2: case -2: rotate_E(); rotate_E();			 break;
  		case 3: case -1: rotate_E(); rotate_E(); rotate_E(); break;
  		}
  	}



    public void rotate_Z() {

    	rotate_F();
    	rotate_S();
    	rotate_B(-1);

    }

    public void rotate_X() {

    	rotate_R();
    	rotate_M(-1);
    	rotate_L(-1);

    }

    public void rotate_Y() {

    	rotate_U();
    	rotate_E(-1);
    	rotate_D(-1);

    }

    public void rotate_Z(int rotationNumber) {
		switch(rotationNumber%4) {
		case 0: break;
		case 1: case -3: rotate_Z();						 break;
		case 2: case -2: rotate_Z(); rotate_Z();			 break;
		case 3: case -1: rotate_Z(); rotate_Z(); rotate_Z(); break;
		}
	}

    public void rotate_X(int rotationNumber) {
  		switch(rotationNumber%4) {
  		case 0: break;
  		case 1: case -3: rotate_X();						 break;
  		case 2: case -2: rotate_X(); rotate_X();			 break;
  		case 3: case -1: rotate_X(); rotate_X(); rotate_X(); break;
  		}
  	}

    public void rotate_Y(int rotationNumber) {
  		switch(rotationNumber%4) {
  		case 0: break;
  		case 1: case -3: rotate_Y();						 break;
  		case 2: case -2: rotate_Y(); rotate_Y();			 break;
  		case 3: case -1: rotate_Y(); rotate_Y(); rotate_Y(); break;
  		}
  	}



    public void rotate_f() {
    	rotate_F();
    	rotate_S();
    }

    public void rotate_r() {
    	rotate_R();
    	rotate_M(-1);
    }

    public void rotate_u() {
    	rotate_U();
    	rotate_E(-1);
    }

    public void rotate_b() {
    	rotate_B();
    	rotate_S(-1);
    }

    public void rotate_l() {
    	rotate_L();
    	rotate_M();
    }

    public void rotate_d() {
    	rotate_D();
    	rotate_E();
    }

    public void rotate_f(int rotationNumber) {
  		switch(rotationNumber%4) {
  		case 0: break;
  		case 1: case -3: rotate_f();						 break;
  		case 2: case -2: rotate_f(); rotate_f();			 break;
  		case 3: case -1: rotate_f(); rotate_f(); rotate_f(); break;
  		}
  	}

    public void rotate_r(int rotationNumber) {
  		switch(rotationNumber%4) {
  		case 0: break;
  		case 1: case -3: rotate_r();						 break;
  		case 2: case -2: rotate_r(); rotate_r();			 break;
  		case 3: case -1: rotate_r(); rotate_r(); rotate_r(); break;
  		}
  	}

    public void rotate_u(int rotationNumber) {
  		switch(rotationNumber%4) {
  		case 0: break;
  		case 1: case -3: rotate_u();						 break;
  		case 2: case -2: rotate_u(); rotate_u();			 break;
  		case 3: case -1: rotate_u(); rotate_u(); rotate_u(); break;
  		}
  	}

    public void rotate_b(int rotationNumber) {
  		switch(rotationNumber%4) {
  		case 0: break;
  		case 1: case -3: rotate_b();						 break;
  		case 2: case -2: rotate_b(); rotate_b();			 break;
  		case 3: case -1: rotate_b(); rotate_b(); rotate_b(); break;
  		}
  	}

    public void rotate_l(int rotationNumber) {
  		switch(rotationNumber%4) {
  		case 0: break;
  		case 1: case -3: rotate_l();						 break;
  		case 2: case -2: rotate_l(); rotate_l();			 break;
  		case 3: case -1: rotate_l(); rotate_l(); rotate_l(); break;
  		}
  	}

    public void rotate_d(int rotationNumber) {
  		switch(rotationNumber%4) {
  		case 0: break;
  		case 1: case -3: rotate_d();						 break;
  		case 2: case -2: rotate_d(); rotate_d();			 break;
  		case 3: case -1: rotate_d(); rotate_d(); rotate_d(); break;
  		}
  	}



    public void rotate_LFmLmF() {
    	rotate_L();
    	rotate_F(-1);
    	rotate_L(-1);
    	rotate_F();
    }

    public void rotate_FmLFLm() {
    	rotate_F(-1);
    	rotate_L();
    	rotate_F();
    	rotate_L(-1);
    }

    public void rotate_UmLmUL() {
    	rotate_U(-1);
    	rotate_L(-1);
    	rotate_U();
    	rotate_L();
    }

    public void rotate_UFUmFm() {
    	rotate_U();
    	rotate_F();
    	rotate_U(-1);
    	rotate_F(-1);
    }


    public void rotate_BLmBmL() {
    	rotate_B();
    	rotate_L(-1);
    	rotate_B(-1);
    	rotate_L();
    }

    public void rotate_LmBLBm() {
    	rotate_L(-1);
    	rotate_B();
    	rotate_L();
    	rotate_B(-1);
    }

    public void rotate_UmBmUB() {
    	rotate_U(-1);
    	rotate_B(-1);
    	rotate_U();
    	rotate_B();
    }

    public void rotate_ULUmLm() {
    	rotate_U();
    	rotate_L();
    	rotate_U(-1);
    	rotate_L(-1);
    }


    public void rotate_RBmRmB() {
    	rotate_R();
    	rotate_B(-1);
    	rotate_R(-1);
    	rotate_B();
    }

    public void rotate_BmRBRm() {
    	rotate_B(-1);
    	rotate_R();
    	rotate_B();
    	rotate_R(-1);
    }

    public void rotate_UmRmUR() {
    	rotate_U(-1);
    	rotate_R(-1);
    	rotate_U();
    	rotate_R();
    }

    public void rotate_UBUmBm() {
    	rotate_U();
    	rotate_B();
    	rotate_U(-1);
    	rotate_B(-1);
    }


    public void rotate_FRmFmR() {
    	rotate_F();
    	rotate_R(-1);
    	rotate_F(-1);
    	rotate_R();
    }

    public void rotate_RmFRFm() {
    	rotate_R(-1);
    	rotate_F();
    	rotate_R();
    	rotate_F(-1);
    }

    public void rotate_UmFmUF() {
    	rotate_U(-1);
    	rotate_F(-1);
    	rotate_U();
    	rotate_F();
    }

    public void rotate_URUmRm() {
    	rotate_U();
    	rotate_R();
    	rotate_U(-1);
    	rotate_R(-1);
    }



    public void rotate_RURmUm() {
    	rotate_R();
		rotate_U();
		rotate_R(-1);
		rotate_U(-1);
    }

    public void rotate_car() {
    	rotate_F();
    	for(int i = 0 ; i < 3; i++) {rotate_RURmUm();}
    	rotate_F(-1);
    }
    public void rotate_pi() {
    	rotate_f();
    	rotate_RURmUm();
    	rotate_f(-1);
    	rotate_F();
    	rotate_RURmUm();
    	rotate_F(-1);
    }
    public void rotate_cml() {
    	rotate_r();
    	rotate_U();
    	rotate_R(-1);
    	rotate_U(-1);

    	rotate_r(-1);
    	rotate_F();
    	rotate_R();
    	rotate_F(-1);

    }
    public void rotate_hl()  {
    	rotate_R(2);
    	rotate_D();
    	rotate_R(-1);
    	rotate_U(2);

    	rotate_R();
    	rotate_D(-1);
    	rotate_R(-1);
    	rotate_U(2);

    	rotate_R(-1);
    }
    public void rotate_bowtie() {
    	rotate_F(-1);
    	rotate_r();
    	rotate_U();
    	rotate_R(-1);

    	rotate_U(-1);
    	rotate_r(-1);
    	rotate_F();
    	rotate_R();
    }
    public void rotate_antisune() {
    	rotate_R(-1);
    	rotate_U(-1);
    	rotate_R();
    	rotate_U(-1);
    	rotate_R(-1);
    	rotate_U(2);
    	rotate_R();
    }
    public void rotate_sune() {
    	rotate_L();
    	rotate_U();
    	rotate_L(-1);
    	rotate_U();
    	rotate_L();
    	rotate_U(2);
    	rotate_L(-1);
    }

    public void rotate_Jb_perm() {
    	rotate_R(); rotate_U(); rotate_R(-1); rotate_F(-1);
    	rotate_R(); rotate_U(); rotate_R(-1); rotate_U(-1);
    	rotate_R(-1); rotate_F(); rotate_R(2); rotate_U(-1); rotate_R(-1);
    }
    public void rotate_Y_perm() {
    	rotate_F(); rotate_R(); rotate_U(-1); rotate_R(-1);
    	rotate_U(-1); rotate_R(); rotate_U(); rotate_R(-1);
    	rotate_F(-1); rotate_R(); rotate_U(); rotate_R(-1);
    	rotate_U(-1); rotate_R(-1); rotate_F(); rotate_R(); rotate_F(-1);
    }

    public void rotate_H_perm() {
    	rotate_M(2); rotate_U(-1);
    	rotate_M(2); rotate_U(2);  rotate_M(2);
    	rotate_U(-1); rotate_M(2);
    }
    public void rotate_Z_perm() {
    	rotate_M(2); rotate_U(-1); rotate_M(2); rotate_U(-1);
    	rotate_M();
    	rotate_U(2);  rotate_M(2); rotate_U(2);
    	rotate_M();
    }
    public void rotate_Ua_perm() {
    	rotate_M(2); rotate_U(); rotate_M(-1); rotate_U(2);
    	rotate_M();  rotate_U(); rotate_M(2);
    }
    public void rotate_Ub_perm() {
    	rotate_M(2); rotate_U(-1); rotate_M(-1); rotate_U(2);
    	rotate_M();  rotate_U(-1); rotate_M(2);
    }


}
