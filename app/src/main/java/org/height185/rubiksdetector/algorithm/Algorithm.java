package org.height185.rubiksdetector.algorithm;

import android.util.Log;

import org.height185.rubiksdetector.obj3d.Cube;

public class Algorithm {

    public static int getEdgePosition(Cube cube, int pieceNumber) {
        int position = 0;
        for (int i = 0; i < 12; i++) {
            if (cube.pieceAt[8 + i].pieceNumber == pieceNumber) {
                position = 8 + i;
                break;
            }
        }
        return position;
    }

    public static int getCornerPosition(Cube cube, int pieceNumber) {
        int position = 0;
        for (int i = 0; i < 8; i++) {
            if (cube.pieceAt[i].pieceNumber == pieceNumber) {
                position = i;
                break;
            }
        }
        return position;
    }

    public static void setCoordinate(Cube cube) {

        int blueCenterPos = 0;
        int yellowCenterPos = 0;

        for (int i = 0; i < 6; i++) {
            if (cube.pieceAt[20 + i].pieceNumber == 20 + 0) {
                // blue center라면 해당 자리 번호를 저장한다
                blueCenterPos = i;
                break;
            }
        }

        switch (blueCenterPos) {
            case 0:
                break; // 아무것도 하지 않는다.
            case 1:
                cube.rotate_M(2);
                break;
            case 2:
                cube.rotate_E(3);
                break;
            case 3:
                cube.rotate_E(1);
                break;
            case 4:
                cube.rotate_M(1);
                break;
            case 5:
                cube.rotate_M(3);
                break;
        }


        for (int i = 0; i < 6; i++) {
            if (cube.pieceAt[20 + i].pieceNumber == 20 + 4) {
                // yellow center라면 해당 자리 번호를 저장한다
                yellowCenterPos = i;
                break;
            }
        }

        switch (yellowCenterPos) {

            case 2:
                cube.rotate_S(3);
                break;
            case 3:
                cube.rotate_S(1);
                break;
            case 4:
                break; // 아무것도 하지 않는다.
            case 5:
                cube.rotate_S(2);
                break;
        }


    }


    public static void crossPerm(Cube cube) {

        cube.transcriptionProcess();

        int position = 0;
        position = getEdgePosition(cube, 8 + 3 - 1);

//		BW edge(3-1번 edge)를 3-1번 자리에 옮기는 작업을 먼저 수행한다.
        switch (position - 8) {
            case 1 - 1:
                cube.rotate_F(2);
                break;
            case 2 - 1:
                cube.rotate_F();
                break;
            case 3 - 1:
                break;
            case 4 - 1:
                cube.rotate_F(-1);
                break;
            case 5 - 1:
                cube.rotate_L();
                cube.rotate_F(-1);
                break;
            case 6 - 1:
                cube.rotate_R(-1);
                cube.rotate_F();
                break;
            case 7 - 1:
                cube.rotate_D(-1);
                break;
            case 8 - 1:
                cube.rotate_D();
                break;
            case 9 - 1:
                cube.rotate_L(-1);
                cube.rotate_D();
                break;
            case 10 - 1:
                cube.rotate_U(2);
                cube.rotate_F(2);
                break;
            case 11 - 1:
                cube.rotate_R();
                cube.rotate_D(-1);
                break;
            case 12 - 1:
                cube.rotate_D(2);
                break;
        }


        position = getEdgePosition(cube, 8 + 8 - 1);
        switch (position - 8) {
            case 1 - 1:
                cube.rotate_U();
                cube.rotate_L(2);
                break;
            case 2 - 1:
                cube.rotate_F(2);
                cube.rotate_L();
                cube.rotate_F(2);
                break;
//			case 3-1:  break;
            case 4 - 1:
                cube.rotate_L();
                break;
            case 5 - 1:
                cube.rotate_L(2);
                break;
            case 6 - 1:
                cube.rotate_U(2);
                cube.rotate_L(2);
                break;
            case 7 - 1:
                cube.rotate_R(2);
                cube.rotate_U(2);
                cube.rotate_L(2);
                break;
            case 8 - 1:
                break;
            case 9 - 1:
                cube.rotate_L(-1);
                break;
            case 10 - 1:
                cube.rotate_B();
                cube.rotate_L(-1);
                break;
            case 11 - 1:
                cube.rotate_B(2);
                cube.rotate_L(-1);
                break;
            case 12 - 1:
                cube.rotate_B(-1);
                cube.rotate_L(-1);
                break;
        }

        position = getEdgePosition(cube, 8 + 12 - 1);
        switch (position - 8) {
            case 1 - 1:
                cube.rotate_U(2);
                cube.rotate_B(2);
                break;
            case 2 - 1:
                cube.rotate_R(2);
                cube.rotate_B(-1);
                break;
//		    case 3-1:  break;
            case 4 - 1:
                cube.rotate_L(2);
                cube.rotate_B();
                cube.rotate_L(2);
                break;
            case 5 - 1:
                cube.rotate_L(-1);
                cube.rotate_B();
                cube.rotate_L();
                break;
            case 6 - 1:
                cube.rotate_R();
                cube.rotate_B(-1);
                break;
            case 7 - 1:
                cube.rotate_R(-1);
                cube.rotate_B(-1);
                break;
//		    case 8-1:  break;
            case 9 - 1:
                cube.rotate_B();
                break;
            case 10 - 1:
                cube.rotate_B(2);
                break;
            case 11 - 1:
                cube.rotate_B(-1);
                break;
            case 12 - 1:
                break;
        }

        position = getEdgePosition(cube, 8 + 7 - 1);
        switch (position - 8) {
            case 1 - 1:
                cube.rotate_U(-1);
                cube.rotate_R(2);
                break;
            case 2 - 1:
                cube.rotate_R(-1);
                break;
//	    	case 3-1:  break;
            case 4 - 1:
                cube.rotate_L(-1);
                cube.rotate_U(2);
                cube.rotate_L();
                cube.rotate_R(2);
                break;
            case 5 - 1:
                cube.rotate_U(2);
                cube.rotate_R(2);
                break;
            case 6 - 1:
                cube.rotate_R(2);
                break;
            case 7 - 1:
                break;
//	    	case 8-1:  break;
            case 9 - 1:
                cube.rotate_B(-1);
                cube.rotate_U();
                cube.rotate_B();
                cube.rotate_R(2);
                break;
            case 10 - 1:
                cube.rotate_U();
                cube.rotate_R(2);
                break;
            case 11 - 1:
                cube.rotate_R();
                break;
//			case 12-1: break;
        }

        cube.transcriptionProcess();
    }

    public static void crossOrient(Cube cube) {

        cube.transcriptionProcess();

        int position = getEdgePosition(cube, 8 + 3 - 1);
        if (cube.pieceAt[position].orientation != 0) {
            cube.rotate_F(2);
            cube.rotate_U();
            cube.rotate_L();
            cube.rotate_F(-1);
            cube.rotate_L(-1);
        }

        position = getEdgePosition(cube, 8 + 8 - 1);
        if (cube.pieceAt[position].orientation != 0) {
            cube.rotate_L(2);
            cube.rotate_U();
            cube.rotate_B();
            cube.rotate_L(-1);
            cube.rotate_B(-1);
        }

        position = getEdgePosition(cube, 8 + 12 - 1);
        if (cube.pieceAt[position].orientation != 0) {
            cube.rotate_B(2);
            cube.rotate_U();
            cube.rotate_R();
            cube.rotate_B(-1);
            cube.rotate_R(-1);
        }

        position = getEdgePosition(cube, 8 + 7 - 1);
        if (cube.pieceAt[position].orientation != 0) {
            cube.rotate_R(2);
            cube.rotate_U();
            cube.rotate_F();
            cube.rotate_R(-1);
            cube.rotate_F(-1);
        }

    }

    public static void cross(Cube cube) {
        crossPerm(cube);
        crossOrient(cube);
    }


    public static void f2l_slotBO(Cube cube) {

        cube.transcriptionProcess();

        int position = getCornerPosition(cube, 4 - 1);
        boolean condition1 = position == 4 - 1;
        boolean condition2 = cube.pieceAt[position].orientation == 0;

        position = getEdgePosition(cube, 8 + 4 - 1);
        boolean condition3 = position == 8 + 4 - 1;
        boolean condition4 = cube.pieceAt[position].orientation == 0;
        boolean condition = condition1 && condition2 && condition3 && condition4;

        if (!condition) {

            position = getCornerPosition(cube, 4 - 1);
            // 일단 코너를 1-1자리에 옮겨둔다
            switch (position) {
//			case 1-1: break;
                case 2 - 1:
                    cube.rotate_U();
                    break;
                case 3 - 1:
                    cube.rotate_F(-1);
                    cube.rotate_U();
                    cube.rotate_F();
                    cube.rotate_U();
                    break;
                case 4 - 1:
                    cube.rotate_F();
                    cube.rotate_U(-1);
                    cube.rotate_F(-1);
                    break;
                case 5 - 1:
                    cube.rotate_L();
                    cube.rotate_U(2);
                    cube.rotate_L(-1);
                    cube.rotate_U();
                    break;
                case 6 - 1:
                    cube.rotate_U(-1);
                    break;
                case 7 - 1:
                    cube.rotate_U(2);
                    break;
                case 8 - 1:
                    cube.rotate_R(-1);
                    cube.rotate_U(2);
                    cube.rotate_R();
                    break;
            }


            position = getEdgePosition(cube, 8 + 4 - 1);
            // 걸리적거리는 위치에 있는 pair edge를 다른 곳(6-1)로 보내버린다.
            switch (position - 8) {
                case 1 - 1:
                    cube.rotate_L();
                    cube.rotate_U(-1);
                    cube.rotate_L(-1);
                    break; // edge[4-1]의 위치는 6-1이다
                case 4 - 1:
                    cube.rotate_L(-1);
                    cube.rotate_U(-1);
                    cube.rotate_L();
                    cube.rotate_U(2);
                    cube.rotate_L(-1);
                    cube.rotate_U();
                    cube.rotate_L();
                    break; // edge[4-1]의 위치는 6-1이다
                case 5 - 1:
                    cube.rotate_U();
                    cube.rotate_L(-1);
                    cube.rotate_U(2);
                    cube.rotate_L();
                    cube.rotate_U(-1);
                    break;                                      // edge[4-1]의 위치는 6-1이다
            }


            position = getCornerPosition(cube, 4 - 1);
            // 코너의 오리값에 따라 처리하여 코너를 제자리에 넣어둔다.
            switch (cube.pieceAt[position].orientation) {
                case 0:
                    cube.rotate_LFmLmF();
                    break;
                case 1:
                    cube.rotate_FmLFLm();
                    break;
                case 2:
                    cube.rotate_LFmLmF();
                    cube.rotate_LFmLmF();
                    cube.rotate_LFmLmF();
                    break;
            }


            position = getEdgePosition(cube, 8 + 4 - 1);
            // edge를 6-1번 위치에 넣어준다.
            switch (position - 8) {
                case 1 - 1:
                    cube.rotate_U(-1);
                    break; // 처리해야하나? 일단 처리하자
                case 2 - 1:
                    cube.rotate_R();
                    cube.rotate_U();
                    cube.rotate_R(-1);
                    cube.rotate_U(-1);
                    break;
//			case 3-1 : break; // 바닥
                case 4 - 1:
                    break; // 처리해야하나? 이건 좀 오바임
                case 5 - 1:
                    cube.rotate_U(2);
                    break; // 처리해야하나? 일단 처리하자
//			case 6-1 : break;
//			case 7-1 : break; // 바닥
//			case 8-1 : break; // 바닥
                case 9 - 1:
                    cube.rotate_B(-1);
                    cube.rotate_U();
                    cube.rotate_B();
                    break;
                case 10 - 1:
                    cube.rotate_U();
                    break;
                case 11 - 1:
                    cube.rotate_R(-1);
                    cube.rotate_U();
                    cube.rotate_R();
                    cube.rotate_U(-1);
                    break;
//			case 12-1 : break;

            }


            position = getEdgePosition(cube, 8 + 4 - 1);
            // edge의 오리값에 따라 적절한 위치로 옮긴 후 그 위치에 맞는 오퍼레이션을 실시하여 완성된 슬롯을 만든다.
            switch (cube.pieceAt[position].orientation) {
                case 0:
                    cube.rotate_U();
                    cube.rotate_UmLmUL();
                    cube.rotate_UFUmFm();
                    break;
                case 1:
                    cube.rotate_U(2);
                    cube.rotate_UFUmFm();
                    cube.rotate_UmLmUL();
                    break;
            }

            cube.transcriptionProcess();

        } // if문
    } // 메서드 끝

    public static void f2l_slotOG(Cube cube) {

        cube.transcriptionProcess();

        int position = getCornerPosition(cube, 5 - 1);
        boolean condition1 = position == 5 - 1;
        boolean condition2 = cube.pieceAt[position].orientation == 0;

        position = getEdgePosition(cube, 8 + 9 - 1);
        boolean condition3 = position == 8 + 9 - 1;
        boolean condition4 = cube.pieceAt[position].orientation == 0;
        boolean condition = condition1 && condition2 && condition3 && condition4;

        if (!condition) {

            position = getCornerPosition(cube, 5 - 1);
            // 일단 코너를 6-1자리에 옮겨둔다
            switch (position) {
                case 1 - 1:
                    cube.rotate_U();
                    break;
                case 2 - 1:
                    cube.rotate_U(2);
                    break;
                case 3 - 1:
                    cube.rotate_R();
                    cube.rotate_U(2);
                    cube.rotate_R(-1);
                    break;
//			case 4-1: break;
                case 5 - 1:
                    cube.rotate_L();
                    cube.rotate_U(-1);
                    cube.rotate_L(-1);
                    break;
//			case 6-1: break;
                case 7 - 1:
                    cube.rotate_U(-1);
                    break;
                case 8 - 1:
                    cube.rotate_B();
                    cube.rotate_U(2);
                    cube.rotate_B(-1);
                    cube.rotate_U();
                    break;
            }

            position = getEdgePosition(cube, 8 + 9 - 1);
            // 걸리적거리는 위치에 있는 pair edge를 다른 곳(1-1)로 보내버린다.
            switch (position - 8) {
                case 5 - 1:
                    cube.rotate_B();
                    cube.rotate_U(-1);
                    cube.rotate_B(-1);
                    break; // edge[9-1]의 위치는 1-1이다
                case 9 - 1:
                    cube.rotate_B(-1);
                    cube.rotate_U(-1);
                    cube.rotate_B();
                    cube.rotate_U(2);
                    cube.rotate_B(-1);
                    cube.rotate_U();
                    cube.rotate_B();
                    break; // edge[9-1]의 위치는 1-1이다
                case 10 - 1:
                    cube.rotate_U();
                    cube.rotate_B(-1);
                    cube.rotate_U(2);
                    cube.rotate_B();
                    cube.rotate_U(-1);
                    break;                                      // edge[9-1]의 위치는 1-1이다
            }

            position = getCornerPosition(cube, 5 - 1);
            // 코너의 오리값에 따라 처리하여 코너를 제자리에 넣어둔다.
            switch (cube.pieceAt[position].orientation) {
                case 2:
                    cube.rotate_BLmBmL();
                    break;
                case 0:
                    cube.rotate_LmBLBm();
                    break;
                case 1:
                    cube.rotate_BLmBmL();
                    cube.rotate_BLmBmL();
                    cube.rotate_BLmBmL();
                    break;
            }

            position = getEdgePosition(cube, 8 + 9 - 1);
            // edge를 1-1번 위치에 넣어준다.
            switch (position - 8) {
//			case 1-1 : break;
                case 2 - 1:
                    cube.rotate_F(-1);
                    cube.rotate_U();
                    cube.rotate_F();
                    cube.rotate_U(-1);
                    break;
//			case 3-1 : break; // 바닥
//			case 4-1 : break; // BO SLOT으로 채워짐
                case 5 - 1:
                    cube.rotate_U(-1);
                    break; // 처리해야하나? 일단 처리
                case 6 - 1:
                    cube.rotate_U();
                    break;
//			case 7-1 : break; // 바닥
//			case 8-1 : break; // 바닥
                case 9 - 1:
                    break; // 처리해야하나? 이건 좀
                case 10 - 1:
                    cube.rotate_U(-2);
                    break; // 처리해야하나? 일단 처리
                case 11 - 1:
                    cube.rotate_B();
                    cube.rotate_U(2);
                    cube.rotate_B(-1);
                    break;
//			case 12-1 : break;

            }

            position = getEdgePosition(cube, 8 + 9 - 1);
            // edge의 오리값에 따라 적절한 위치로 옮긴 후 그 위치에 맞는 오퍼레이션을 실시하여 완성된 슬롯을 만든다.
            switch (cube.pieceAt[position].orientation) {
                case 1:
                    cube.rotate_U();
                    cube.rotate_UmBmUB();
                    cube.rotate_ULUmLm();
                    break;
                case 0:
                    cube.rotate_U(2);
                    cube.rotate_ULUmLm();
                    cube.rotate_UmBmUB();
                    break;
            }

            cube.transcriptionProcess();

        } // if문
    } // 메서드 끝

    public static void f2l_slotGR(Cube cube) {

        cube.transcriptionProcess();

        int position = getCornerPosition(cube, 8 - 1);
        boolean condition1 = position == 8 - 1;
        boolean condition2 = cube.pieceAt[position].orientation == 0;

        position = getEdgePosition(cube, 8 + 11 - 1);
        boolean condition3 = position == 8 + 11 - 1;
        boolean condition4 = cube.pieceAt[position].orientation == 0;
        boolean condition = condition1 && condition2 && condition3 && condition4;

        if (!condition) {


            position = getCornerPosition(cube, 8 - 1);
            // 일단 코너를 7-1자리에 옮겨둔다
            switch (position) {
                case 1 - 1:
                    cube.rotate_U(2);
                    break;
                case 2 - 1:
                    cube.rotate_U(-1);
                    break;
                case 3 - 1:
                    cube.rotate_R();
                    cube.rotate_U(2);
                    cube.rotate_R(-1);
                    cube.rotate_U();
                    break;
//			case 4-1: break; // BO SLOT으로 채워짐
//			case 5-1: break; // OG SLOT으로 채워짐
                case 6 - 1:
                    cube.rotate_U();
                    break;
//			case 7-1: break;
                case 8 - 1:
                    cube.rotate_R(-1);
                    cube.rotate_U(-1);
                    cube.rotate_R();
                    cube.rotate_U();
                    break;
            }

            position = getEdgePosition(cube, 8 + 11 - 1);
            // 걸리적거리는 위치에 있는 pair edge를 다른 곳(1-1)로 보내버린다.
            switch (position - 8) {
                case 10 - 1:
                    cube.rotate_R();
                    cube.rotate_U(-1);
                    cube.rotate_R(-1);
                    break; // edge[11-1]의 위치는 5-1이다
                case 11 - 1:
                    cube.rotate_R(-1);
                    cube.rotate_U(-1);
                    cube.rotate_R();
                    cube.rotate_U(2);
                    cube.rotate_R(-1);
                    cube.rotate_U();
                    cube.rotate_R();
                    break;  // edge[11-1]의 위치는 5-1이다
                case 6 - 1:
                    cube.rotate_U();
                    cube.rotate_R(-1);
                    cube.rotate_U(2);
                    cube.rotate_R();
                    cube.rotate_U(-1);
                    break;                                       // edge[11-1]의 위치는 5-1이다
            }

            position = getCornerPosition(cube, 8 - 1);
            // 코너의 오리값에 따라 처리하여 코너를 제자리에 넣어둔다.
            switch (cube.pieceAt[position].orientation) {
                case 0:
                    cube.rotate_RBmRmB();
                    break;
                case 1:
                    cube.rotate_BmRBRm();
                    break;
                case 2:
                    cube.rotate_RBmRmB();
                    cube.rotate_RBmRmB();
                    cube.rotate_RBmRmB();
                    break;
            }

            position = getEdgePosition(cube, 8 + 11 - 1);
            // edge를 5-1번 위치에 넣어준다.
            switch (position - 8) {
                case 1 - 1:
                    cube.rotate_U();
                    break;
                case 2 - 1:
                    cube.rotate_F(-1);
                    cube.rotate_U();
                    cube.rotate_F();
                    break;
//			case 3-1 : break;  // 바닥
//			case 4-1 : break;  // BO SLOT
//			case 5-1 : break;
                case 6 - 1:
                    cube.rotate_U(2);
                    break; // 처리해야하나? 일단 처리함
//			case 7-1 : break;  // 바닥
//			case 8-1 : break;  // 바닥
//			case 9-1 : break;  // OG SLOT
                case 10 - 1:
                    cube.rotate_U(-1);
                    break; // 처리해야하나?
                case 11 - 1:
                    break; // 처리해야하나? 이건 좀
//			case 12-1 : break;

            }

            position = getEdgePosition(cube, 8 + 11 - 1);
            // edge의 오리값에 따라 적절한 위치로 옮긴 후 그 위치에 맞는 오퍼레이션을 실시하여 완성된 슬롯을 만든다.
            switch (cube.pieceAt[position].orientation) {
                case 0:
                    cube.rotate_U();
                    cube.rotate_UmRmUR();
                    cube.rotate_UBUmBm();
                    break;
                case 1:
                    cube.rotate_U(2);
                    cube.rotate_UBUmBm();
                    cube.rotate_UmRmUR();
                    break;
            }

        } // if문
    } // 메서드 끝

    public static void f2l_slotRB(Cube cube) {

        cube.transcriptionProcess();

        int position = getCornerPosition(cube, 3 - 1);
        boolean condition1 = position == 3 - 1;
        boolean condition2 = cube.pieceAt[position].orientation == 0;

        position = getEdgePosition(cube, 8 + 2 - 1);
        boolean condition3 = position == 8 + 2 - 1;
        boolean condition4 = cube.pieceAt[position].orientation == 0;
        boolean condition = condition1 && condition2 && condition3 && condition4;

        if (!condition) {


            position = getCornerPosition(cube, 3 - 1);
            // 일단 코너를 2-1자리에 옮겨둔다
            switch (position) {
                case 1 - 1:
                    cube.rotate_U(-1);
                    break;
//			case 2-1: break;
                case 3 - 1:
                    cube.rotate_F(-1);
                    cube.rotate_U();
                    cube.rotate_F();
                    break;
//			case 4-1: break; // BO SLOT
//			case 5-1: break; // OG SLOT
                case 6 - 1:
                    cube.rotate_U(2);
                    break;
                case 7 - 1:
                    cube.rotate_U();
                    break;
//			case 8-1: break; // GR SLOT
            }

            position = getEdgePosition(cube, 8 + 2 - 1);
            // 걸리적거리는 위치에 있는 pair edge를 다른 곳(10-1)로 보내버린다.
            switch (position - 8) {
                case 6 - 1:
                    cube.rotate_F(-1);
                    cube.rotate_U(2);
                    cube.rotate_F();
                    cube.rotate_U();
                    break;  // edge[2-1]의 위치는 10-1이다
                case 2 - 1:
                    cube.rotate_F(-1);
                    cube.rotate_U(-1);
                    cube.rotate_F();
                    cube.rotate_U(2);
                    cube.rotate_F(-1);
                    cube.rotate_U();
                    cube.rotate_F();
                    break;  // edge[2-1]의 위치는 10-1이다
                case 1 - 1:
                    cube.rotate_U();
                    cube.rotate_F(-1);
                    cube.rotate_U(2);
                    cube.rotate_F();
                    cube.rotate_U(-1);
                    break;                                       // edge[2-1]의 위치는 10-1이다
            }

            position = getCornerPosition(cube, 3 - 1);
            // 코너의 오리값에 따라 처리하여 코너를 제자리에 넣어둔다.
            switch (cube.pieceAt[position].orientation) {
                case 2:
                    cube.rotate_FRmFmR();
                    break;
                case 0:
                    cube.rotate_RmFRFm();
                    break;
                case 1:
                    cube.rotate_FRmFmR();
                    cube.rotate_FRmFmR();
                    cube.rotate_FRmFmR();
                    break;
            }

            position = getEdgePosition(cube, 8 + 2 - 1);
            // edge를 10-1번 위치에 넣어준다.
            switch (position - 8) {
                case 1 - 1:
                    cube.rotate_U(2);
                    break;  // 처리해야하나? // 일단 처리함
                case 2 - 1:
                    break; // 처리해야하나? 이건 좀
//			case 3-1 : break; // 바닥
//			case 4-1 : break; // BO SLOT
                case 5 - 1:
                    cube.rotate_U();
                    break;
                case 6 - 1:
                    cube.rotate_U(-1);
                    break;  // 처리해야하나?
//			case 7-1 : break; // 바닥
//			case 8-1 : break; // 바닥
//			case 9-1 : break; // OG SLOT
//			case 10-1 : break;
//			case 11-1 : break; // GR SLOT
//			case 12-1 : break; // 바닥

            }

            position = getEdgePosition(cube, 8 + 2 - 1);
            // edge의 오리값에 따라 적절한 위치로 옮긴 후 그 위치에 맞는 오퍼레이션을 실시하여 완성된 슬롯을 만든다.
            switch (cube.pieceAt[position].orientation) {
                case 1:
                    cube.rotate_U();
                    cube.rotate_UmFmUF();
                    cube.rotate_URUmRm();
                    break;
                case 0:
                    cube.rotate_U(2);
                    cube.rotate_URUmRm();
                    cube.rotate_UmFmUF();
                    break;
            }

            cube.transcriptionProcess();

        } // if문
    } // 메서드 끝

    public static void f2l(Cube cube) {

        f2l_slotBO(cube);
        f2l_slotOG(cube);
        f2l_slotGR(cube);
        f2l_slotRB(cube);

    }


    public static void oll_edge(Cube cube) {

        cube.transcriptionProcess();

        // 관심 edge 1-1, 5-1, 6-1, 10-1
        int p1 = getEdgePosition(cube, 8 + 1 - 1);
        int p2 = getEdgePosition(cube, 8 + 5 - 1);
        int p3 = getEdgePosition(cube, 8 + 6 - 1);
        int p4 = getEdgePosition(cube, 8 + 10 - 1);

        int discriminant = cube.pieceAt[p1].orientation + cube.pieceAt[p2].orientation
                + cube.pieceAt[p3].orientation + cube.pieceAt[p4].orientation;

        switch (discriminant) {

            case 0:
                break; // 이미 edge OLL이 된 상태

            case 4:
                cube.rotate_F();
                cube.rotate_R();
                cube.rotate_U();
                cube.rotate_R(-1);
                cube.rotate_U(-1);
                cube.rotate_S();
                cube.rotate_R();
                cube.rotate_U();
                cube.rotate_R(-1);
                cube.rotate_U(-1);
                cube.rotate_f(-1);
                break;

            case 2:

                if (cube.state_pxo[1][8 + 5 - 1] == 0 && cube.state_pxo[1][8 + 6 - 1] == 0) {
                    cube.rotate_F();
                    cube.rotate_R();
                    cube.rotate_U();
                    cube.rotate_R(-1);
                    cube.rotate_U(-1);
                    cube.rotate_F(-1);
                } else if (cube.state_pxo[1][8 + 1 - 1] == 0 && cube.state_pxo[1][8 + 10 - 1] == 0) {
                    cube.rotate_Y();
                    cube.rotate_F();
                    cube.rotate_R();
                    cube.rotate_U();
                    cube.rotate_R(-1);
                    cube.rotate_U(-1);
                    cube.rotate_F(-1);
                    cube.rotate_Y(-1);
                } else if (cube.state_pxo[1][8 + 1 - 1] == 0 && cube.state_pxo[1][8 + 6 - 1] == 0) {
                    cube.rotate_f();
                    cube.rotate_R();
                    cube.rotate_U();
                    cube.rotate_R(-1);
                    cube.rotate_U(-1);
                    cube.rotate_f(-1);
                } else if (cube.state_pxo[1][8 + 1 - 1] == 0 && cube.state_pxo[1][8 + 5 - 1] == 0) {
                    cube.rotate_Y(-1);
                    cube.rotate_f();
                    cube.rotate_R();
                    cube.rotate_U();
                    cube.rotate_R(-1);
                    cube.rotate_U(-1);
                    cube.rotate_f(-1);
                    cube.rotate_Y();
                } else if (cube.state_pxo[1][8 + 5 - 1] == 0 && cube.state_pxo[1][8 + 10 - 1] == 0) {
                    cube.rotate_Y(2);
                    cube.rotate_f();
                    cube.rotate_R();
                    cube.rotate_U();
                    cube.rotate_R(-1);
                    cube.rotate_U(-1);
                    cube.rotate_f(-1);
                    cube.rotate_Y(2);
                } else if (cube.state_pxo[1][8 + 10 - 1] == 0 && cube.state_pxo[1][8 + 6 - 1] == 0) {
                    cube.rotate_Y();
                    cube.rotate_f();
                    cube.rotate_R();
                    cube.rotate_U();
                    cube.rotate_R(-1);
                    cube.rotate_U(-1);
                    cube.rotate_f(-1);
                    cube.rotate_Y(-1);
                }

                break;

        } // switch 끝

        cube.transcriptionProcess();


    }// 메서드 끝

    public static void oll_corner(Cube cube) {

        boolean flag = false;
        int uRot = 0;

        cube.transcriptionProcess();

        while (true) {

            if (isAlreadyCornerOlled(cube)) {
                break;
            } else if (isCar(cube)) {
                cube.rotate_car();
                break;
            } else if (isPi(cube)) {
                cube.rotate_pi();
                break;
            } else if (isChameleon(cube)) {
                cube.rotate_cml();
                break;
            } else if (isHighLight(cube)) {
                cube.rotate_hl();
                break;
            } else if (isBowtie(cube)) {
                cube.rotate_bowtie();
                break;
            } else if (isAntiSune(cube)) {
                cube.rotate_antisune();
                break;
            } else if (isSune(cube)) {
                cube.rotate_sune();
                break;
            }

            cube.rotate_U();
            uRot++;
            // System.out.println("uRot : " + uRot);
            if (uRot > 3) {
                // System.out.println("what the **** is wrong with this?");
                break;
            }
        }

        switch (uRot) {
            case 1:
                cube.rotate_U(-1);
                break;
            case 2:
                cube.rotate_U(2);
                break;
            case 3:
                cube.rotate_U(1);
                break;
        }

        cube.transcriptionProcess();
    }

    public static boolean isCar(Cube cube) {

        cube.transcriptionProcess();

        // 여기서 ==4는 노란색을 뜻함.
        boolean condition = cube.pieceAt[1 - 1].colorOfFace[0] == 4
                && cube.pieceAt[2 - 1].colorOfFace[0] == 4
                && cube.pieceAt[6 - 1].colorOfFace[1] == 4
                && cube.pieceAt[7 - 1].colorOfFace[1] == 4;

        if (condition) {
            return true;
        } else {
            return false;
        }


    }

    public static boolean isPi(Cube cube) {

        cube.transcriptionProcess();


        boolean condition = cube.pieceAt[1 - 1].colorOfFace[3] == 4
                && cube.pieceAt[2 - 1].colorOfFace[0] == 4
                && cube.pieceAt[6 - 1].colorOfFace[3] == 4
                && cube.pieceAt[7 - 1].colorOfFace[1] == 4;


        if (condition) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isChameleon(Cube cube) {
        cube.transcriptionProcess();

        boolean condition = cube.pieceAt[1 - 1].colorOfFace[0] == 4
                && cube.pieceAt[2 - 1].colorOfFace[4] == 4
                && cube.pieceAt[6 - 1].colorOfFace[1] == 4
                && cube.pieceAt[7 - 1].colorOfFace[4] == 4;

        if (condition) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isHighLight(Cube cube) {
        cube.transcriptionProcess();


        boolean condition = cube.pieceAt[1 - 1].colorOfFace[0] == 4
                && cube.pieceAt[2 - 1].colorOfFace[0] == 4
                && cube.pieceAt[6 - 1].colorOfFace[4] == 4
                && cube.pieceAt[7 - 1].colorOfFace[4] == 4;


        if (condition) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBowtie(Cube cube) {
        cube.transcriptionProcess();


        boolean condition = cube.pieceAt[1 - 1].colorOfFace[4] == 4
                && cube.pieceAt[2 - 1].colorOfFace[0] == 4
                && cube.pieceAt[6 - 1].colorOfFace[3] == 4
                && cube.pieceAt[7 - 1].colorOfFace[4] == 4;


        if (condition) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAntiSune(Cube cube) {
        cube.transcriptionProcess();

        boolean condition = cube.pieceAt[1 - 1].colorOfFace[0] == 4
                && cube.pieceAt[2 - 1].colorOfFace[2] == 4
                && cube.pieceAt[6 - 1].colorOfFace[4] == 4
                && cube.pieceAt[7 - 1].colorOfFace[1] == 4;

        if (condition) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSune(Cube cube) {
        cube.transcriptionProcess();

        boolean condition = cube.pieceAt[1 - 1].colorOfFace[3] == 4
                && cube.pieceAt[2 - 1].colorOfFace[0] == 4
                && cube.pieceAt[6 - 1].colorOfFace[1] == 4
                && cube.pieceAt[7 - 1].colorOfFace[4] == 4;


        if (condition) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAlreadyCornerOlled(Cube cube) {
        cube.transcriptionProcess();

        boolean condition = cube.pieceAt[1 - 1].colorOfFace[4] == 4
                && cube.pieceAt[2 - 1].colorOfFace[4] == 4
                && cube.pieceAt[6 - 1].colorOfFace[4] == 4
                && cube.pieceAt[7 - 1].colorOfFace[4] == 4;

        if (condition) {
            return true;
        } else {
            return false;
        }
    }

    public static void oll(Cube cube) {
        oll_edge(cube);
        oll_corner(cube);
    }


    public static void pll_corner(Cube cube) {

        cube.transcriptionProcess();

        boolean c1 = cube.pieceAt[1 - 1].colorOfFace[0]
                == cube.pieceAt[2 - 1].colorOfFace[0];
        boolean c2 = cube.pieceAt[2 - 1].colorOfFace[2]
                == cube.pieceAt[7 - 1].colorOfFace[2];
        boolean c3 = cube.pieceAt[7 - 1].colorOfFace[1]
                == cube.pieceAt[6 - 1].colorOfFace[1];
        boolean c4 = cube.pieceAt[6 - 1].colorOfFace[3]
                == cube.pieceAt[1 - 1].colorOfFace[3];

        if (c1 && c2 && c3 && c4) {/*do nothing*/} else if (!c1 && !c2 && !c3 && !c4) {
            cube.rotate_Y_perm();
        } else {
            if (c4) {
                cube.rotate_Jb_perm();
            } else if (c1) {
                cube.rotate_U();
                cube.rotate_Jb_perm();
            } else if (c2) {
                cube.rotate_U(2);
                cube.rotate_Jb_perm();
            } else if (c3) {
                cube.rotate_U(-1);
                cube.rotate_Jb_perm();
            }
        }


    }

    public static void pll_edge(Cube cube) {

        cube.transcriptionProcess();

        boolean c1 = cube.pieceAt[1 - 1].colorOfFace[0]
                == cube.pieceAt[8 + 1 - 1].colorOfFace[0];
        boolean c2 = cube.pieceAt[2 - 1].colorOfFace[2]
                == cube.pieceAt[8 + 6 - 1].colorOfFace[2];
        boolean c3 = cube.pieceAt[7 - 1].colorOfFace[1]
                == cube.pieceAt[8 + 10 - 1].colorOfFace[1];
        boolean c4 = cube.pieceAt[6 - 1].colorOfFace[3]
                == cube.pieceAt[8 + 5 - 1].colorOfFace[3];


        Log.d("TESTING1", c1 + "  " + c2 + "  " + c3 + "  " + c4);


        if (c1 && c2 && c3 && c4) {
            // System.out.println("without pll_edge");
        } else if (!c1 && !c2 && !c3 && !c4) {

            if (isNeedZperm(cube)) {
                boolean z1 = cube.pieceAt[8 + 1 - 1].colorOfFace[0]
                        == cube.pieceAt[6 - 1].colorOfFace[3];
                boolean z2 = cube.pieceAt[8 + 6 - 1].colorOfFace[2]
                        == cube.pieceAt[6 - 1].colorOfFace[1];

                if (z1 && z2) {
                    cube.rotate_Z_perm();
                } else {
                    cube.rotate_U();
                    cube.rotate_Z_perm();
                }
            } else {
                cube.rotate_H_perm();
            }
        } else {
            if (c1) {
            } else if (c2) {
                cube.rotate_U();
            } else if (c3) {
                cube.rotate_U(2);
            } else if (c4) {
                cube.rotate_U(-1);
            }

            boolean cub = cube.pieceAt[8 + 6 - 1].colorOfFace[2]
                    == cube.pieceAt[6 - 1].colorOfFace[3];

            if (cub) {
                cube.rotate_Ub_perm();
            } else {
                cube.rotate_Ua_perm();
            }
        }

        boolean u1 = cube.pieceAt[8 + 1 - 1].colorOfFace[0] == 0; // 여기서 == 0 의 0은 BLUE를 뜻한다.
        boolean u2 = cube.pieceAt[8 + 6 - 1].colorOfFace[2] == 0;
        boolean u3 = cube.pieceAt[8 + 10 - 1].colorOfFace[1] == 0;
        boolean u4 = cube.pieceAt[8 + 5 - 1].colorOfFace[3] == 0;

        if (u1) {
        } else if (u2) {
            cube.rotate_U();
        } else if (u3) {
            cube.rotate_U(2);
        } else if (u4) {
            cube.rotate_U(-1);
        }

        // System.out.println("puzzel solved");
    }

    public static boolean isNeedZperm(Cube cube) {

        boolean c1 = cube.pieceAt[8 + 1 - 1].colorOfFace[0]
                == cube.pieceAt[6 - 1].colorOfFace[3];
        boolean c2 = cube.pieceAt[8 + 6 - 1].colorOfFace[2]
                == cube.pieceAt[6 - 1].colorOfFace[1];

        boolean c3 = cube.pieceAt[8 + 1 - 1].colorOfFace[0]
                == cube.pieceAt[7 - 1].colorOfFace[2];
        boolean c4 = cube.pieceAt[8 + 5 - 1].colorOfFace[3]
                == cube.pieceAt[7 - 1].colorOfFace[1];

        if (c1 && c2 || c3 && c4) {
            return true;
        } else {
            return false;
        }
    }

    public static void pll(Cube cube) {
        pll_corner(cube);
        pll_edge(cube);
    }


}
