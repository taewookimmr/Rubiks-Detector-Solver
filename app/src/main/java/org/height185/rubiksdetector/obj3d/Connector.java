package org.height185.rubiksdetector.obj3d;

// OpenCV로 처리한 영상데이터를 Cube 클래스의 인스턴스에게 전달하는 중간자 역할을 하는 클래스,
// Connector. static 함수로만 구성하도록 합시다.


public class Connector {

    // 각 corner, edge를 구별하기 위해 sum of 2^(색번호 of 구성 surface)를 구함
    public static int[] sumOfColorOfCorner = {25, 21, 37, 41, 42, 26, 22,  38};
    public static int[] sumOfColorOfEdge = {17, 5, 33, 9, 24, 20, 36, 40, 10, 18, 6, 34};
    public static int[] sumOfColorOfCenter = {1, 2, 4, 8, 16, 32};

    public static int[][] pieceAsFaces = {
            // 코너 8개
            {0*9+0, 3*9+2, 4*9+6}, {0*9+2, 2*9+0, 4*9+8}, {0*9+8, 2*9+6, 5*9+2}, {0*9+6, 3*9+8, 5*9+0},
            {1*9+8, 3*9+6, 5*9+6}, {1*9+2, 3*9+0, 4*9+0}, {1*9+0, 2*9+2, 4*9+2}, {1*9+6, 2*9+8, 5*9+8},

            // 엣지 12개
            {0*9+1, 4*9+7}, {0*9+5, 2*9+3}, {0*9+7, 5*9+1}, {0*9+3, 3*9+5},
            {3*9+1, 4*9+3}, {2*9+1, 4*9+5}, {2*9+7, 5*9+5}, {3*9+7, 5*9+3},
            {1*9+5, 3*9+3}, {1*9+1, 4*9+1}, {1*9+3, 2*9+5}, {1*9+7, 5*9+7},

            // 코너 6개
            {0*9+4},  {1*9+4}, {2*9+4}, {3*9+4}, {4*9+4}, {5*9+4}
    };

    public static int getSmallest(int... color){
        int smallest = color[0];
        for(int a : color){
            if(a <= smallest){
                smallest = a;
            }
        }
        return smallest;
    }

    public static int getBiggest(int... color){
        int biggest = color[0];
        for(int a : color){
            if(a >= biggest){
                biggest = a;
            }
        }
        return biggest;
    }


    // 가변인수에 입력할 때, 우선 순위가 높은 face 순서대로 입력한다.
    // (앞, 뒤), (우, 좌), (위, 아래) 순서대로 입력한다.
    public static int[] transferSurfaceColorToState(int... color){

        int result[] = {0,0}; // 첫번째는 피스번호, 두번째는 orientation.

        int sum = 0;
        for(int a : color){sum += (int)Math.pow(2.0, a);}

        if(color.length == 3){ // corner

            for(int i = 0; i < 8; i++) {
                if (sum == sumOfColorOfCorner[i]) {
                    result[0] = i; // 몇번 코너인지 알 수 있다
                    int smallest = Connector.getSmallest(color[0], color[1], color[2]);
                    int biggest  = Connector.getBiggest(color[0], color[1], color[2]);
                    switch(i%2){
                        case 0:
                            if(color[1] == smallest){
                                result[1] = 1;
                            }else if(color[1] == biggest){
                                result[1] = 2;
                            }else {
                                result[1] = 0;
                            }
                            break;
                        case 1:
                            if(color[1] == smallest){
                                result[1] = 2;
                            }else if(color[1] == biggest){
                                result[1] = 1;
                            }else {
                                result[1] = 0;
                            }
                            break;
                    }
                    break;
                }
            }

        }else if(color.length == 2){ // edge

            for(int i = 0; i < 12; i++) {
                if (sum == sumOfColorOfEdge[i]) {
                    result[0] = i+8; // 몇번 엣지인지 알 수 있다
                    int smallest = Connector.getSmallest(color[0], color[1]);
                    if(smallest == color[0]){
                        result[1] = 0;
                    }else{
                        result[1] = 1;
                    }
                    break;
                }
            }
        } else { // center
            for(int i = 0; i < 6; i++) {
                if (sum == sumOfColorOfCenter[i]) {
                    result[0] = i+20;
                    result[1] = 0; // center는 orientation이 없다.
                    break;
                }
            }
        }
        return result;
    }


}
