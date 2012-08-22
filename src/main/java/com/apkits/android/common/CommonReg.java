/** 
 * Filename:	CommonReg.java
 * Description:	TODO
 * Copyright:   Copyright (c) 2012
 * Company:		leg3s
 * @author:	yongjia.chen
 * @version:	1.0
 * Create at:	2012-8-17 下午4:53:33
 *  
 * Modification History:  
 * Date           Author       Version      Description 
 * ------------------------------------------------------------------ 
 * 2012-8-17        yongjia.chen      1.0        	1.0 Version 
 */
package com.apkits.android.common;

import java.util.regex.Pattern;

/** 
 * @ClassName: CommonReg 
 * @Description: TODO
 * @author yongjia.chen
 * @date 2012-8-17 下午4:53:33
 *  
 */

public class CommonReg {

    /**
     * <b>description :</b>     执行正则表达匹配
     * </br><b>time :</b>       2012-8-16 下午10:03:26
     * @param number
     * @return
     */
    public static boolean matcherRegex(String reg,String str){
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(str).matches();
    }
    
    /**
     * <b>description :</b>     匹配有效字符（中英文数字及常用标点）
     * </br><b>time :</b>       2012-8-16 下午10:03:26
     * @param number
     * @return
     */
    public static boolean matchCommonChar(String str){
        return matcherRegex("^[\\w \\p{P}]+$",str);
    }
    
    /**
     * <b>description :</b>     判断手机号码
     * </br><b>time :</b>       2012-8-16 下午10:03:26
     * @param number
     * @return
     */
    public static boolean matchCNMobileNumber(String number){
        return matcherRegex("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))\\d{8}$",number);
    }
    
    /**
     * <b>description :</b>     匹配空行
     * </br><b>time :</b>       2012-8-16 下午10:03:26
     * @param number
     * @return
     */
    public static boolean matchEmptyLine(String line){
        return null == line ? true : matcherRegex("^[\\s\\n]*$", line);
    }
    
    /**
     * <b>description :</b>     清除标点符号
     * </br><b>time :</b>       2012-8-22 下午16:53:26
     * @param number
     * @return
     */
    public static String cleanPunctuation(String content){
        return content.replaceAll("[\\p{P}‘’“”]", content);
    }
    
    /**
     * <b>description :</b>     判断是否为有效的人物名字，可带·间隔符号。
     * </br><b>time :</b>       2012-8-22 下午18:03:26
     * @param number
     * @return
     */
    public static boolean matchPersionName(String name){
        return null == name ? false : matcherRegex("^[\\w .\u00B7-]+$", name);
    }
}
