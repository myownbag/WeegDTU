package com.weegcn.weegdtu.utils;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Administrator on 2018/1/8.
 */

public class CodeFormat {
    static String dataOne ;
    /*
      * 16���������ַ���
      */
    private static String hexString="0123456789ABCDEF";
    /*
    * ���ַ��������16��������,�����������ַ����������ģ�
    */
    public static String encode(String str)
    {
        dataOne = str;
        //����Ĭ�ϱ����ȡ�ֽ�����
        byte[] bytes=str.getBytes();
        StringBuilder sb=new StringBuilder(bytes.length*2);
        //���ֽ�������ÿ���ֽڲ���2λ16��������
        for(int i=0;i<bytes.length;i++)
        {
            sb.append(hexString.charAt((bytes[i]&0xf0)>>4));
            sb.append(hexString.charAt((bytes[i]&0x0f)>>0)+" ");
        }

        return sb.toString();

    }
    /*
    * ��16�������ֽ�����ַ���,�����������ַ����������ģ�
    */
    public static String decode(String bytes)
    {
        String temp=null;
        ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2);
        //��ÿ2λ16����������װ��һ���ֽ�
        for(int i=0;i<bytes.length();i+=2)
            baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1))));
        temp= new String(baos.toByteArray());
        return temp ;

    }

    public   static String StringFilter(String str)   throws PatternSyntaxException {
        // ֻ������ĸ������
        // String   regEx  =  "[^a-zA-Z0-9]";
        // ��������������ַ�
        String regEx="[`~!@#$%^&*()+=|{}':;',//[//].<>/?~��@#��%����&*��������+|{}������������������������]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }
    /**
     ����* Convert byte[] to hex string.�������ǿ��Խ�byteת����int��Ȼ������Integer.toHexString(int)��ת����16�����ַ�����

     ����* @param src byte[] data

     ����* @return hex string

     ����*/

    public static String bytesToHexString(byte[] src){

        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {

            return null;

        }

        for (int i = 0; i < 20; i++) {

            int v = src[i] & 0xFF;

            String hv = Integer.toHexString(v);

            if (hv.length() < 2) {

                stringBuilder.append(0);
                System.out.println(stringBuilder);
            }

            stringBuilder.append(hv);

        }

        return stringBuilder.toString();

    }

    /** *//**
     * ���ֽ�����ת����16�����ַ���
     * @param bArray
     * @return
     */
    public static final String bytesToHexStringTwo(byte[] bArray, int count) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < count; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }



    //�ָ��ַ���
    public static String Stringspace(String str){

        String temp="";
        String temp2="";
        for(int i=0;i<str.length();i++)
        {

            if (i%2==0) {
                temp=str.charAt(i)+"";
                temp2+=temp;
                System.out.println(temp);
            }else {
                temp2+=str.charAt(i)+" ";
            }

        }
        return temp2;
    }
    /**
     * Byte -> Hex
     *
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes, int count) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex).append(" ");
        }
        return sb.toString();
    }
    /**
     * String -> Hex
     *
     * @param s
     * @return
     */
    public static String stringToHex(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            if (s4.length() == 1) {
                s4 = '0' + s4;
            }
            str = str + s4 + " ";
        }
        return str;
    }
    private static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = (byte)asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }
    //����crc����ֵ
    public static short crcencode(byte[] b){
        int ret = 0;
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;


        int buflen= b.length;

        if (buflen == 0)
        {
            return (short) ret;
        }
        for (i = 0; i < buflen-2; i++)
        {
            CRC ^= ((int)b[i] & 0x000000ff);
            for (j = 0; j < 8; j++)
            {
                if ((CRC & 0x00000001) != 0)
                {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                }
                else
                {
                    CRC >>= 1;
                }
            }
            //System.out.println(Integer.toHexString(CRC));
        }

        //System.out.println(Integer.toHexString(CRC));
        b[buflen-2] = (byte)(CRC & 0x00ff);
        b[buflen-1] = (byte)(CRC >> 8);


        return (short) CRC;
    }
    public static byte HEX2BCD(byte tempbyte)
    {
        byte result=0;
        result=(byte)((tempbyte/10)<<4);
        result|=(byte)(tempbyte%10);
        return result;
    }
    public static byte[] HEX2BCD_BUF(byte[] buf,int lenth)
    {
        byte[] result=new byte[lenth];
        int i=0;
        for(i=0;i<lenth;i++)
        {
            result[i]=HEX2BCD(buf[i]);
        }
        return result;
    }
}
