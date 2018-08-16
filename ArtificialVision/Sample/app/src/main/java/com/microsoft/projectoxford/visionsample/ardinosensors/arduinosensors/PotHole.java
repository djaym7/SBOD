package com.microsoft.projectoxford.visionsample.ardinosensors.arduinosensors;

import android.os.Vibrator;

public class PotHole {
    Vibrator v = null;
   // long [] pot = {100,500};
    public PotHole(Vibrator v) {
        this.v = v;
    }
   public void potVibrate() {
       v.vibrate(500);
   }

}
