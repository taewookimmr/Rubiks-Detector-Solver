package org.height185.rubiksdetector.typedef;

public class BGR_SDATA {
    public SDATA[] sdata;

    public BGR_SDATA(){
        sdata = new SDATA[3];
        for(int i = 0; i < 3; i++){
            sdata[i] = new SDATA();
        }
    }

}
