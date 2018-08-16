package com.microsoft.projectoxford.visionsample.ardinosensors.arduinosensors;

import android.os.Vibrator;

public class Bump {
    Vibrator v = null;
   // long [] bump = {500,100};
    public Bump(Vibrator v) {
        this.v = v;
    }
    public void bumpVibrate() {
        v.vibrate(100);
    }
}
