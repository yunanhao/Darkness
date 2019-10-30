package demo;

import javax.sound.sampled.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class MediaPlayer {
    private MediaPlayer() {
    }

    public static void play(InputStream inputStream) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if (inputStream == null) {
            inputStream = new FileInputStream("src/audio/刀击中(铁器)11.wav");
        }
        // 获取音频输入流
        AudioInputStream audioInputStream = AudioSystem
                .getAudioInputStream(inputStream);
        // 获取音频编码对象
        AudioFormat audioFormat = audioInputStream.getFormat();
        // 设置数据输入
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

        /*
         * 从输入流中读取数据发送到混音器
         */
        int count;
        byte tempBuffer[] = new byte[1024];
        while ((count = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
            if (count > 0) {
                sourceDataLine.write(tempBuffer, 0, count);
            }
        }

        // 清空数据缓冲,并关闭输入
        sourceDataLine.drain();
        sourceDataLine.close();

    }
}
