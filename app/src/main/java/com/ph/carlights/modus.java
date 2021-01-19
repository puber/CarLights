package com.ph.carlights;

public class modus {

    private String geschwindikeit, farbe;

    modus(String farbe, String Geschwindigkeit){
        this.farbe = farbe;
        this.geschwindikeit = geschwindikeit;
    }

    public String getGeschwindikeit() {
        return geschwindikeit;
    }

    public void setGeschwindikeit(String geschwindikeit) {
        this.geschwindikeit = geschwindikeit;
    }

    public String getFarbe() {
        return farbe;
    }

    public void setFarbe(String farbe) {
        this.farbe = farbe;
    }
}
