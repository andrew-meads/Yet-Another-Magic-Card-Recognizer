package com.forohfor.yamcr.webcam;

import com.github.sarxos.webcam.Webcam;

public class DummyWebcam extends Webcam
{

    public DummyWebcam()
    {
        super(new DummyWebcamDevice());
    }

}