package cn.eeepay.framework.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


public class RandomValidateCodeUtils {

    /**
     * 图片宽
     **/
    private static int width = 85;

    /**
     * 图片高
     **/
    private static int height = 26;

    /**
     * 干扰线数量
     **/
    private static int lineSize = 40;

    private static Random random = new Random();

    /**
     * getFont(这里用一句话描述这个方法的作用)
     * 获取字体样式
     *
     * @return Font
     * @throws
     * @since v2.0
     */
    private static Font getFont() {
        return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
    }


    /**
     * getRandColor(这里用一句话描述这个方法的作用)
     * 获取颜色
     *
     * @param fc
     * @param bc
     * @return Color
     * @throws
     * @since v2.0
     */
    private static Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    /**
     * getRandcode(这里用一句话描述这个方法的作用)
     * 生成随机图片
     *
     * @throws
     * @since v2.0
     */
    public static BufferedImage getRandCodeImage(String code) {
        //BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();//产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
        g.setColor(getRandColor(110, 133));
        //绘制干扰线
        for (int i = 0; i <= lineSize; i++) {
            drowLine(g);
        }
        //绘制字符
        for (int i = 0, len = code.length(); i < len; i++) {
            drowString(g, code.charAt(i) + "", i + 1);
        }
        g.dispose();
        return image;
    }


    /**
     * drowString(这里用一句话描述这个方法的作用)
     * 绘制字符串
     *
     * @since v2.0
     */
    private static void drowString(Graphics g, String ch, int index) {
        g.setFont(getFont());
        g.setColor(new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121)));
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(ch, 13 * index, 16);
    }

    /**
     * drowLine(这里用一句话描述这个方法的作用)
     * 绘制干扰线
     *
     * @param g
     * @throws
     * @since v2.0
     */
    private static void drowLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x + xl, y + yl);
    }

//    public static void main(String[] args) throws Exception {
//        String code = "code";
//        BufferedImage randcodeImage = RandomValidateCodeUtil.getRandCodeImage(code);
//        File file = new File("d:/1.jpg");
//        FileOutputStream fos = new FileOutputStream(file);
//        ImageIO.write(randcodeImage, "JPEG", fos);
//    }
}
