import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

/**
 * Created by liying on 2016/2/14.
 */
public class GeneratorQRCode {
    private static final String ENCODING = "UTF-8";

    public static void main(String[] args) {
        String baseStr = getBase64("北京微昂科技有限公司111");
        System.out.println("encode:"+baseStr);
        System.out.println(getFromBase64(baseStr));


        createQRCode("D:/dist", "QR.png", "北京微昂科技有限公司111", 125);
    }


    /**
     * @param distDir      目录名称
     * @param distFileName 二维码文件名称
     * @param content      二维码内容
     * @param size         二维码图片大小
     */
    public static boolean createQRCode(String distDir, String distFileName, String content, int size) {


        String fileType = "png";
        File dir = new File(distDir);


        try {
            if (!dir.exists()) {
                dir.mkdir();
            }
            File myFile = new File(dir, distFileName);
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            Hashtable<EncodeHintType, Object> hintMap = new Hashtable<EncodeHintType, Object>();
            hintMap.put(EncodeHintType.CHARACTER_SET, ENCODING);
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hintMap.put(EncodeHintType.MARGIN, 2);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hintMap);

            int width = byteMatrix.getWidth();

            BufferedImage image = new BufferedImage(width, width,

                    BufferedImage.TYPE_INT_RGB);

            image.createGraphics();


            Graphics2D graphics = (Graphics2D) image.getGraphics();

            graphics.setColor(Color.WHITE);

            graphics.fillRect(0, 0, width, width);

            graphics.setColor(Color.BLACK);


            for (int i = 0; i < width; i++) {

                for (int j = 0; j < width; j++) {

                    if (byteMatrix.get(i, j)) {

                        graphics.fillRect(i, j, 1, 1);

                    }

                }

            }

            ImageIO.write(image, fileType, myFile);

        } catch (WriterException e) {

            e.printStackTrace();
            return false;

        } catch (IOException e) {

            e.printStackTrace();
            return false;

        }
        return true;
    }

    public static String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes(ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    // 解密
    public static String getFromBase64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, ENCODING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
